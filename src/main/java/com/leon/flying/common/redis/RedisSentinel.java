package com.leon.flying.common.redis;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Copyright (c) 2020 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 *
 * @author leon
 * @since 2020/8/6
 */
public class RedisSentinel {
    private static final Logger logger = LoggerFactory.getLogger(RedisSentinel.class);
    private volatile HostAndPort currentHostMaster;
    private List<String> serverList;
    private String masterName;
    private Set<MasterListener> masterListeners = new HashSet();
    private RedisClient.MasterChangeEventListener masterChangeEventListener;

    public RedisSentinel() {
    }

    public RedisSentinel(List<String> serverList, String masterName) {
        this.serverList = serverList;
        this.masterName = masterName;
        this.currentHostMaster = this.initSentinels(this.serverList, masterName);
    }

    public RedisSentinel(List<String> serverList, String masterName, RedisClient.MasterChangeEventListener masterChangeEventListener) {
        this.serverList = serverList;
        this.masterName = masterName;
        this.masterChangeEventListener = masterChangeEventListener;
        this.currentHostMaster = this.initSentinels(this.serverList, masterName);
    }

    public void destroy() {
        Iterator var1 = this.masterListeners.iterator();

        while(var1.hasNext()) {
            RedisSentinel.MasterListener m = (RedisSentinel.MasterListener)var1.next();
            m.shutdown();
        }

    }

    public HostAndPort getCurrentHostMaster() {
        return this.currentHostMaster;
    }

    private HostAndPort initSentinels(List<String> sentinels, String masterName) {
        if (!Objects.isNull(this.serverList) && this.serverList.size() > 0 && !StringUtils.isEmpty(this.masterName)) {
            HostAndPort master = null;
            boolean sentinelAvailable = false;
            logger.info("Trying to find master from available Sentinels...");
            List<HostAndPort> hostAndPorts = HostAndPort.toHostAndPortMulti(sentinels);
            Iterator var6 = hostAndPorts.iterator();

            HostAndPort hap;
            while(var6.hasNext()) {
                hap = (HostAndPort)var6.next();
                logger.warn("Connecting to Sentinel {}", hap);
                Jedis jedis = null;

                try {
                    jedis = new Jedis(hap.getHost(), hap.getPort());
                    List<String> masterAddr = jedis.sentinelGetMasterAddrByName(masterName);
                    sentinelAvailable = true;
                    if (masterAddr != null && masterAddr.size() == 2) {
                        master = HostAndPort.toHostAndPort(masterAddr);
                        if (master != null) {
                            logger.warn("Found Redis master at {}", master);
                            break;
                        }

                        logger.warn("Can not find Redis master on {}", hap);
                    } else {
                        logger.warn("Can not get master addr, master name: {}. Sentinel: {}.", masterName, hap);
                    }
                } catch (JedisException var13) {
                    logger.error("Cannot get master address from sentinel running @ {}. Reason: {}. Trying next one.", new Object[]{hap, var13.getMessage(), var13});
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }

                }
            }

            if (master == null) {
                if (sentinelAvailable) {
                    throw new JedisException("Can connect to sentinel, but " + masterName + " seems to be not monitored...");
                } else {
                    throw new JedisConnectionException("All sentinels down, cannot determine where is " + masterName + " master is running...");
                }
            } else {
                logger.info("Redis master running at {}, starting Sentinel listeners...", master);
                this.currentHostMaster = master;
                var6 = hostAndPorts.iterator();

                while(var6.hasNext()) {
                    hap = (HostAndPort)var6.next();
                    RedisSentinel.MasterListener masterListener = new RedisSentinel.MasterListener(masterName, hap.getHost(), hap.getPort());
                    this.masterListeners.add(masterListener);
                    masterListener.addMasterChangeListener(this.masterChangeEventListener);
                    Thread thread = new Thread(masterListener);
                    thread.setDaemon(true);
                    thread.start();
                }

                return master;
            }
        } else {
            logger.error("Sentinel init param error, param is null or empty: serverList: {}, masterName: {}", this.serverList, masterName);
            return null;
        }
    }

    public class MasterListener implements Runnable {
        String masterName;
        String host;
        int port;
        long subscribeRetryWaitTimeMillis;
        volatile Jedis j;
        AtomicBoolean running;
        private Set<RedisClient.MasterChangeEventListener> listenerSet;

        protected MasterListener() {
            this.subscribeRetryWaitTimeMillis = 5000L;
            this.running = new AtomicBoolean(false);
            this.listenerSet = null;
        }

        MasterListener(String masterName, String host, int port) {
            this.subscribeRetryWaitTimeMillis = 5000L;
            this.running = new AtomicBoolean(false);
            this.listenerSet = null;
            this.masterName = masterName;
            this.host = host;
            this.port = port;
        }

        public MasterListener(String masterName, String host, int port, long subscribeRetryWaitTimeMillis) {
            this(masterName, host, port);
            this.subscribeRetryWaitTimeMillis = subscribeRetryWaitTimeMillis;
        }

        void addMasterChangeListener(RedisClient.MasterChangeEventListener listener) {
            if (this.listenerSet == null) {
                this.listenerSet = new HashSet();
            }

            if (listener != null) {
                this.listenerSet.add(listener);
            }

        }

        public void run() {
            this.running.set(true);

            while(this.running.get()) {
                this.j = new Jedis(this.host, this.port);

                try {
                    if (!this.running.get()) {
                        break;
                    }

                    this.j.subscribe(new JedisPubSub() {
                        @Override
                        public void onMessage(String channel, String message) {
                            RedisSentinel.logger.warn("Sentinel {}:{} published: {}.", new Object[]{MasterListener.this.host, MasterListener.this.port, message});
                            String[] switchMasterMsg = message.split(" ");
                            if (switchMasterMsg.length > 3) {
                                if (MasterListener.this.masterName.equals(switchMasterMsg[0])) {
                                    if (!StringUtils.isNumeric(switchMasterMsg[4])) {
                                        RedisSentinel.logger.error("Invalid message received on Sentinel {}:{} on channel +switch-master: {}", new Object[]{switchMasterMsg[3], switchMasterMsg[4], MasterListener.this.masterName});
                                    } else {
                                        HostAndPort newHostAndPort = new HostAndPort(switchMasterMsg[3], Integer.parseInt(switchMasterMsg[4]));
                                        MasterChangeEvent event = new MasterChangeEvent(MasterListener.this);
                                        event.setNewMaster(newHostAndPort);
                                        event.setOldMaster(RedisSentinel.this.currentHostMaster);
                                        Iterator var6 = MasterListener.this.listenerSet.iterator();

                                        while(var6.hasNext()) {
                                            RedisClient.MasterChangeEventListener listener = (RedisClient.MasterChangeEventListener)var6.next();
                                            listener.onMasterChange(event);
                                        }

                                        RedisSentinel.this.currentHostMaster = newHostAndPort;
                                    }
                                } else {
                                    RedisSentinel.logger.info("Ignoring message on +switch-master for master name {}, our master name is {}", switchMasterMsg[0], MasterListener.this.masterName);
                                }
                            } else {
                                RedisSentinel.logger.warn("Invalid message received on Sentinel {}:{} on channel +switch-master: {}", new Object[]{MasterListener.this.host, MasterListener.this.port, message});
                            }

                        }

                        @Override
                        public void onSubscribe(String channel, int subscribedChannels) {
                            RedisSentinel.logger.warn("Sentinel {}:{} subscribed: {}:{}.", new Object[]{MasterListener.this.host, MasterListener.this.port, channel, subscribedChannels});
                            Jedis jedis = new Jedis(MasterListener.this.host, MasterListener.this.port);

                            try {
                                List<String> masterAddr = jedis.sentinelGetMasterAddrByName(MasterListener.this.masterName);
                                HostAndPort hap = HostAndPort.toHostAndPort(masterAddr);
                                if (hap == null) {
                                    RedisSentinel.logger.error("sentinel subscribe can not get masterAddr on {}:{}", MasterListener.this.host, MasterListener.this.port);
                                    return;
                                }

                                if (RedisSentinel.this.currentHostMaster != null) {
                                    if (RedisSentinel.this.currentHostMaster.getHost().equals(hap.getHost()) && RedisSentinel.this.currentHostMaster.getPort().equals(hap.getPort())) {
                                        RedisSentinel.logger.warn("sentinel subscribe get the same master: {}", hap);
                                        return;
                                    }

                                    RedisSentinel.logger.warn("sentinel subscribe get new master: {}, old: {}", hap, RedisSentinel.this.currentHostMaster);
                                    MasterChangeEvent event = new MasterChangeEvent(MasterListener.this);
                                    event.setNewMaster(hap);
                                    event.setOldMaster(RedisSentinel.this.currentHostMaster);
                                    Iterator var7 = MasterListener.this.listenerSet.iterator();

                                    while(var7.hasNext()) {
                                        RedisClient.MasterChangeEventListener listener = (RedisClient.MasterChangeEventListener)var7.next();
                                        listener.onMasterChange(event);
                                    }

                                    RedisSentinel.this.currentHostMaster = hap;
                                    return;
                                }

                                RedisSentinel.logger.error("sentinel error, subscribe get master, but null when init");
                            } catch (Exception var12) {
                                RedisSentinel.logger.error("sentinel subscribe exception: {}", var12.getMessage(), var12);
                                return;
                            } finally {
                                if (jedis != null) {
                                    jedis.close();
                                }

                            }

                        }
                    }, new String[]{"+switch-master"});
                } catch (JedisConnectionException var8) {
                    if (this.running.get()) {
                        RedisSentinel.logger.error("Lost connection to Sentinel at {}:{}. Sleeping 5000ms and retrying.", new Object[]{this.host, this.port, var8});

                        try {
                            Thread.sleep(this.subscribeRetryWaitTimeMillis);
                        } catch (InterruptedException var7) {
                            RedisSentinel.logger.error("Sleep interrupted: ", var7);
                        }
                    } else {
                        RedisSentinel.logger.error("Unsubscribing from Sentinel at {}:{}", this.host, this.port);
                    }
                } finally {
                    this.j.close();
                }
            }

        }

        void shutdown() {
            try {
                RedisSentinel.logger.warn("Shutting down listener on {}:{}", this.host, this.port);
                this.running.set(false);
                if (this.j != null) {
                    this.j.disconnect();
                }
            } catch (Exception var2) {
                RedisSentinel.logger.error("Caught exception while shutting down: ", var2);
            }

        }
    }

}