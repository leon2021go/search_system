package com.leon.flying.common.redis;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.jedis.params.geo.GeoRadiusParam;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


/**
 * Copyright (c) 2018 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 * RedisClient
 *
 * @author mugua
 * @since 2018/3/21 0021
 */
public class RedisClient {
    private static final Logger logger = LoggerFactory.getLogger(RedisClient.class);

    /**
     * 连接池
     */
    private static RedisProperties redisProperty = null;
    private static JedisPoolConfig poolConfig = null;
    private static RedisSentinel redisSentinel = null;
    private static JedisPool jedisPool = null;
    private static JedisCluster jedisCluster = null;
    private static String currentHostAndPort = null;

    /**
     * key前缀
     */
    private static String keyPrefix = "";
    /**
     * 数据库索引
     */
    private static Integer database = 0;
    /**
     * 使用 pipeline 时，单次最大容量限制
     */
    public static final int PIPELINE_SIZE_LIMIT = 500;
    /**
     * lua 默认的 key
     */
    private static String lua_key = "luaKey";

    private static final Long LOCK_RELEASE_STATE = 1L;

    /**
     * lua脚本，用来释放分布式锁
     */
    private static String luaScript
            = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";

    public RedisClient(RedisProperties redisProperties, SentinelProperty sentinelProperty) {
        if (redisProperties != null && redisProperties.getServers() != null && redisProperties.getServers().size() > 1) {
            // init by cluster

        } else if (redisProperties != null && redisProperties.getServers() != null && redisProperties.getServers().size() == 1
                && sentinelProperty != null && sentinelProperty.getServer() != null && sentinelProperty.getServer().size() > 0) {
            // init by sentinel
            initBySentinel(sentinelProperty, redisProperties);
        } else {
            // init directly
            initPool(redisProperties);
        }

    }

    /**
     * 加载redis连接池
     */
    private void initPool(RedisProperties redisProperties) {
        redisProperty = redisProperties;
        if (jedisPool != null && !jedisPool.isClosed()) {
            return;
        }
        String redisServerStr = redisProperty.getServers().get(0);
        redisProperty.setPassword(StringUtils.isEmpty(redisProperty.getPassword().get(0)) ? null : redisProperty.getPassword());
        if (StringUtils.isEmpty(redisServerStr)) {
            logger.error("RedisClient init error, server address is empty");
            return;
        }
        String[] redisServerInfo = redisProperty.getServers().get(0).split(HostAndPort.HOST_PORT_SEPARATOR);
        if (redisServerInfo.length != 2) {
            logger.error("RedisClient init error, server address [{}] error", redisProperty.getServers().get(0));
            return;
        }

        synchronized (RedisClient.class) {
            if (jedisPool != null && !jedisPool.isClosed()) {
                return;
            }
            logger.info("RedisClient init begin, redis servers is [{}]", redisProperty.getServers().get(0));
            try {
                keyPrefix = redisProperty.getKeyPrefix();
                database = redisProperty.getDatabase();

                // 设置jedis连接池配置
                poolConfig = new JedisPoolConfig();
                poolConfig.setMaxTotal(redisProperty.getMaxTotal());
                poolConfig.setMaxIdle(redisProperty.getMaxIdle());
                poolConfig.setMinIdle(redisProperty.getMinIdle());
                poolConfig.setMaxWaitMillis(redisProperty.getMaxWaitMillis());
                poolConfig.setTestOnBorrow(redisProperty.getTestOnBorrow());
                String password = redisProperty.getPassword() != null && redisProperty.getPassword().size() > 0 ? redisProperty.getPassword().get(0) : null;
                jedisPool = new JedisPool(poolConfig, redisServerInfo[0], Integer.parseInt(redisServerInfo[1]), redisProperty.getTimeout(), password, database == null ? 0 : database);
                currentHostAndPort = redisProperty.getServers().get(0);
                logger.warn("RedisClient init success on [{}]", redisServerStr);
            } catch (Exception ex) {
                logger.error(String.format("RedisClient init exception, redisProperties:%s", JsonSerialize.serialize(redisProperty)), ex);
            }
        }
    }

    /**
     * 通过哨兵启动链接
     *
     * @param sentinelProperty
     * @param redisProperties
     */
    private void initBySentinel(SentinelProperty sentinelProperty, RedisProperties redisProperties) {
        if (Objects.isNull(sentinelProperty)
                || Objects.isNull(sentinelProperty.getServer()) || sentinelProperty.getServer().size() <= 0
                || Objects.isNull(sentinelProperty.getMasterName()) || sentinelProperty.getMasterName().size() <= 0
                || StringUtils.isEmpty(sentinelProperty.getMasterName().get(0))) {
            logger.error("RedisClient init by sentinel begin, sentinel config is error, now init redis directly");
            initPool(redisProperties);
            return;
        }

        synchronized (RedisClient.class) {
            if (Objects.isNull(redisSentinel) || Objects.isNull(redisSentinel.getCurrentHostMaster())) {
                logger.info("RedisClient init by sentinel begin, sentinel config is [{}]", JSONObject.toJSONString(sentinelProperty));
                try {
                    redisSentinel = new RedisSentinel(sentinelProperty.getServer(), sentinelProperty.getMasterName().get(0), new MasterChangeEventListener());
                } catch (Exception e) {
                    logger.error("sentinel init exception, config is 【{}】 error is 【{}】", JSONObject.toJSONString(sentinelProperty), e.getMessage(), e);
                }
            }
        }
        if (Objects.isNull(redisSentinel.getCurrentHostMaster()) || StringUtils.isEmpty(redisSentinel.getCurrentHostMaster().getHost()) || redisSentinel.getCurrentHostMaster().getPort() <= 0) {
            logger.error("sentinel has config, but init error, can not get masterAddr, now init redis directly.");
            initPool(redisProperties);
            return;
        }
        String masterAddr = String.format("%s%s%s", redisSentinel.getCurrentHostMaster().getHost(), HostAndPort.HOST_PORT_SEPARATOR, redisSentinel.getCurrentHostMaster().getPort());
        logger.info("sentinel init success, now replace redis property server: sentinel server: [{}], masterName: [{}], masterAddr: [{}]", sentinelProperty.getServer(), sentinelProperty.getMasterName().get(0), masterAddr);
        redisProperties.getServers().set(0, masterAddr);
        initPool(redisProperties);
    }

    /**
     * 主从切换事件监听器
     */
    static class MasterChangeEventListener implements EventListener {
        void onMasterChange(EventObject e) {
            if (!(e instanceof MasterChangeEvent && e.getSource() instanceof RedisSentinel.MasterListener)) {
                logger.warn("e is not instance of MasterChangeEvent or e.getSource() is not instance of RedisSentinel.MasterListener");
                return;
            }
            MasterChangeEvent masterChangeEvent = (MasterChangeEvent) e;
            if (masterChangeEvent.getNewMaster() == null) {
                logger.error("RedisClient master change event, error event: new master is null.");
                return;
            }
            final String newHostAndPort = String.format("%s%s%s", masterChangeEvent.getNewMaster().getHost(), HostAndPort.HOST_PORT_SEPARATOR, masterChangeEvent.getNewMaster().getPort());
            if (newHostAndPort.equals(currentHostAndPort)) {
                logger.error("RedisClient master change event, error event: new master the same as current.");
                return;
            }
            JedisPool oldPool = jedisPool;
            String oldHostAndPort = currentHostAndPort;
            synchronized (RedisClient.class) {
                if (newHostAndPort.equals(currentHostAndPort)) {
                    logger.error("RedisClient master change event, error event: new master the same as current.");
                    return;
                }
                logger.error("RedisClient master change event, now change to new pool, old is [{}], new is [{}]", oldHostAndPort, newHostAndPort);
                try {
                    String password = redisProperty.getPassword() != null && redisProperty.getPassword().size() > 0 ? redisProperty.getPassword().get(0) : null;
                    jedisPool = new JedisPool(poolConfig, masterChangeEvent.getNewMaster().getHost(), masterChangeEvent.getNewMaster().getPort(), redisProperty.getTimeout(), password, database == null ? 0 : database);
                    currentHostAndPort = newHostAndPort;
                } catch (Exception ex) {
                    logger.error("RedisClient master change event, reinstall pool exception. new master: {}", JsonSerialize.serialize(masterChangeEvent.getNewMaster()), ex);
                }
                if (jedisPool == null || jedisPool.isClosed()) {
                    logger.error("RedisClient master change event, reinstall Jedis pool failed, now rollback, old is [{}], new is [{}]", oldHostAndPort, newHostAndPort);
                    jedisPool = oldPool;
                    currentHostAndPort = oldHostAndPort;
                } else {
                    oldPool.destroy();
                    logger.warn("RedisClient master change event, new master [{}] change success.", newHostAndPort);
                }
            }
        }
    }


    class ConfigListener implements Runnable {
        AtomicBoolean running = new AtomicBoolean(false);

        @Override
        public void run() {
            running.set(true);

        }
    }

    private boolean isEmpty(String input) {
        return input == null || input.length() == 0;
    }


    /**
     * get redis key with prefix
     *
     * @param key
     * @return
     */
    private String getKey(final String key) {
        if (isEmpty(keyPrefix)) {
            return key;
        }
        return String.format("%s:%s", keyPrefix, key);
    }

    private <T> Jedis getJedis(T key) {
        return jedisPool.getResource();
    }

    /**
     * 销毁连接池f
     */
    public void destroy() {
        if (jedisPool == null) {
            return;
        }

        jedisPool.close();
    }

    /**
     * 实现jedis连接的获取和释放，具体的redis业务逻辑由executor实现
     *
     * @param executor RedisExecutor接口的实现类
     * @return
     */
    public <T> T execute(String key, RedisExecutor<T> executor) {
        Jedis jedis = getJedis(key);
        try {
            return executor.execute(jedis);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 支持参数列表的重载方法
     *
     * @param keyArr
     * @param executor
     * @param <T>
     * @return
     */
    public <T> T execute(String[] keyArr, RedisExecutor<T> executor) {
        Jedis jedis = getJedis(keyArr);
        try {
            return executor.execute(jedis);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 事务执行
     *
     * @param key
     * @param executor
     * @param <T>
     * @return
     */
    public <T> T multiExecute(String key, MultiRedisExecutor<T> executor) {
        Jedis jedis = getJedis(key);
        try {
            Transaction tx = jedis.multi();
            Response<T> response = executor.execute(tx);
            tx.exec();

            return response.get();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    //region string

    /**
     * set string
     *
     * @param key
     * @param value
     * @return
     */
    public String set(final String key, final String value) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.set(finalKey, value));
    }

    /**
     * set string
     *
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    public String set(final String key, final String value, final int seconds) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.setex(finalKey, seconds, value));
    }

    /**
     * set T
     *
     * @param key
     * @param t
     * @param <T>
     * @return
     */
    public <T> String set(final String key, final T t) {
        String value = JsonSerialize.serialize(t);
        return set(key, value);
    }

    /**
     * set T
     *
     * @param key
     * @param t
     * @param seconds
     * @param <T>
     * @return
     */
    public <T> String set(final String key, final T t, final int seconds) {
        String value = JsonSerialize.serialize(t);
        return set(key, value, seconds);
    }

    /**
     * set no prefix
     * 安全问题，不建议大规模使用
     *
     * @param key
     * @param value
     * @return
     */
    public String setNoPrefix(final String key, final String value) {
        return execute(key, jedis -> jedis.set(key, value));
    }

    /**
     * set no prefix
     * 安全问题，不建议大规模使用
     *
     * @param key
     * @param t
     * @param <T>
     * @return
     */
    public <T> String setNoPrefix(final String key, final T t) {
        String value = JsonSerialize.serialize(t);
        return setNoPrefix(key, value);
    }

    /**
     * set no prefix
     * 安全问题，不建议大规模使用
     *
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    public String setNoPrefix(final String key, final String value, final int seconds) {
        return execute(key, jedis -> jedis.setex(key, seconds, value));
    }

    /**
     * set no prefix
     * 安全问题，不建议大规模使用
     *
     * @param key
     * @param t
     * @param seconds
     * @param <T>
     * @return
     */
    public <T> String setNoPrefix(final String key, final T t, final int seconds) {
        String value = JsonSerialize.serialize(t);
        return setNoPrefix(key, value, seconds);
    }

    /**
     * set not exist
     *
     * @param key
     * @param value
     * @return
     */
    public Long setnx(final String key, final String value) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.setnx(finalKey, value));
    }

    public Long setnx(final String key, final String value, final int seconds) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> {
            String result = jedis.set(finalKey, value, "NX", "EX", seconds);
            return result != null && result.length() > 0 && result.equals("OK") ? 1L : 0L;
        });
    }

    public <T> T get(final String key, Class<T> valueType) {
        String value = get(key);
        if (value == null || value.isEmpty()) {
            return null;
        }

        return JsonSerialize.deserialize(value, valueType);
    }

    public Object get(final String key, Type valueType) {
        String value = get(key);
        if (value == null || value.isEmpty()) {
            return null;
        }

        return JsonSerialize.deserializeList(value, valueType);
    }

    public String get(final String key) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.get(finalKey));
    }

    /**
     * getNoPrefix
     *
     * @param key       key
     * @param valueType valueType
     * @param <T>       T
     * @return object
     */
    public <T> T getNoPrefix(final String key, Class<T> valueType) {
        String value = getNoPrefix(key);
        if (value == null || value.isEmpty()) {
            return null;
        }

        return JsonSerialize.deserialize(value, valueType);
    }

    /**
     * getNoPrefix
     *
     * @param key       key
     * @param valueType valueType
     * @return object
     */
    public Object getNoPrefix(final String key, Type valueType) {
        String value = getNoPrefix(key);
        if (value == null || value.isEmpty()) {
            return null;
        }

        return JsonSerialize.deserializeList(value, valueType);
    }

    /**
     * getNoPrefix
     *
     * @param key key
     * @return value
     */
    public String getNoPrefix(final String key) {
        return execute(key, jedis -> jedis.get(key));
    }

    /**
     * 获取旧值并设置新值
     *
     * @param key     key
     * @param value   新值
     * @param seconds 过期时间（秒）
     * @return 旧值
     */
    public String getset(String key, final String value, final int seconds) {

        String oldValue = get(key);
        set(key, value, seconds);

        return oldValue;
    }

    /**
     * 获取旧值并设置新值
     *
     * @param key     key
     * @param value   新值
     * @param seconds 过期时间（秒）
     * @return 旧值
     */
    public String getsetNoPrefix(String key, final String value, final int seconds) {

        String oldValue = getNoPrefix(key);
        setNoPrefix(key, value, seconds);

        return oldValue;
    }

    /**
     * 批量查询string类型key, 上限100个, 返回String 类型
     * @param keyList
     * @return
     */
    public List<String> mget(List<String> keyList) {
        if (keyList == null || keyList.size() <= 0 || keyList.size() > 100) {
            return Collections.emptyList();
        }
        String[] keys = (String[]) keyList.toArray(new String[0]);
        return execute(keys, jedis -> jedis.mget(keys));
    }

    /**
     * 批量查询string类型key, 上限100个, 返回 具体范型对象 类型
     * @param keyList
     * @param valueType
     * @return
     */
    public <T> List<T> mget(List<String> keyList, Class<T> valueType) {
        if (keyList == null || keyList.size() <= 0 || keyList.size() > 100 || valueType == null) {
            return Collections.emptyList();
        }
        String[] keys = (String[]) keyList.toArray(new String[0]);
        List<String> list = execute("", jedis -> jedis.mget(keys));
        return list.stream().map(p -> JsonSerialize.deserialize(p, valueType)).collect(Collectors.toList());
    }

    // endregion string

    // region expire

    /**
     * set expired after seconds
     *
     * @param key
     * @param seconds
     * @return
     */
    public Long expire(final String key, final int seconds) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.expire(finalKey, seconds));
    }

    /**
     * set expired at unixTime, unit: second
     *
     * @param key
     * @param unixTime
     * @return
     */
    public Long expireAt(final String key, final int unixTime) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.expireAt(finalKey, unixTime));
    }

    /**
     * ttl key剩余过期时间（单位：秒）
     *
     * @param key key
     * @return 过期时间（秒）
     */
    public Long ttl(final String key) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.ttl(finalKey));
    }

    /**
     * ttlNoPrefix key剩余过期时间（单位：秒）
     *
     * @param key key
     * @return 过期时间（秒）
     */
    public Long ttlNoPrefix(final String key) {
        return execute(key, jedis -> jedis.ttl(key));
    }

    /**
     * pttl key剩余过期时间（单位：毫秒）
     *
     * @param key key
     * @return 过期时间（毫秒）
     */
    public Long pttl(final String key) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.pttl(finalKey));
    }

    /**
     * pttlNoPrefix key剩余过期时间（单位：毫秒）
     *
     * @param key key
     * @return 过期时间（毫秒）
     */
    public Long pttlNoPrefix(final String key) {
        return execute(key, jedis -> jedis.pttl(key));
    }

    // endregion


    // region atomic

    /**
     * key incr step=1
     *
     * @param key
     * @return
     */
    public Long incr(final String key) {
        return incr(key, 1);
    }

    /**
     * key incr step=n
     *
     * @param key
     * @return
     */
    public Long incr(final String key, final long integer) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.incrBy(finalKey, integer));
    }

    /**
     * @param key
     * @param integer
     * @param seconds
     * @return
     */
    public Long incr(final String key, final long integer, final int seconds) {
        String finalKey = getKey(key);
        return multiExecute(finalKey, tx -> {
            Response<Long> value = tx.incrBy(finalKey, integer);
            tx.expire(finalKey, seconds);

            return value;
        });
    }

    /**
     * key decr step=1
     *
     * @param key
     * @return
     */
    public Long decr(final String key) {
        return decr(key, 1);
    }

    /**
     * key decr step=n
     *
     * @param key
     * @return
     */
    public Long decr(final String key, final long integer) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.decrBy(finalKey, integer));
    }

    /**
     * @param key
     * @param integer
     * @param seconds
     * @return
     */
    public Long decr(final String key, final long integer, final int seconds) {
        String finalKey = getKey(key);
        return multiExecute(finalKey, tx -> {
            Response<Long> value = tx.decrBy(finalKey, integer);
            tx.expire(finalKey, seconds);

            return value;
        });
    }

    // endregion atomic


    //region hash

    public Long hset(final String key, final String field, final String value) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.hset(finalKey, field, value));
    }

    public Long hset(final String key, final String field, final String value, final int seconds) {
        String finalKey = getKey(key);
        return multiExecute(finalKey, tx -> {
            Response<Long> value1 = tx.hset(finalKey, field, value);
            tx.expire(finalKey, seconds);
            return value1;
        });
    }

    public String hget(final String key, final String field) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.hget(finalKey, field));
    }

    public String hmset(final String key, final Map<String, String> hash) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.hmset(finalKey, hash));
    }

    public String hmset(final String key, final Map<String, String> hash, final int seconds) {
        String finalKey = getKey(key);
        return multiExecute(finalKey, tx -> {
            Response<String> value = tx.hmset(finalKey, hash);
            tx.expire(finalKey, seconds);

            return value;
        });
    }

    public List<String> hmget(final String key, final String... fields) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.hmget(finalKey, fields));
    }

    /**
     * hash field incr step=1
     *
     * @param key
     * @param field
     * @return
     */
    public Long hincr(final String key, final String field) {
        return hincr(key, field, 1);
    }

    /**
     * hash field incr step=n
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hincr(final String key, final String field, final long value) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.hincrBy(finalKey, field, value));
    }

    public Long hincr(final String key, final String field, final long value, final int seconds) {
        String finalKey = getKey(key);
        return multiExecute(finalKey, tx -> {
            Response<Long> value1 = tx.hincrBy(finalKey, field, value);
            tx.expire(finalKey, seconds);

            return value1;
        });
    }

    /**
     * field exists
     *
     * @param key
     * @param field
     * @return
     */
    public Boolean hexists(final String key, final String field) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.hexists(finalKey, field));
    }

    /**
     * hash del fields
     *
     * @param key
     * @param fields
     * @return
     */
    public Long hdel(final String key, final String... fields) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.hdel(finalKey, fields));
    }

    /**
     * hash field count
     *
     * @param key
     * @return
     */
    public Long hlen(final String key) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.hlen(finalKey));
    }

    /**
     * hash get all fields
     *
     * @param key
     * @return
     */
    public Set<String> hkeys(final String key) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.hkeys(finalKey));
    }

    /**
     * hash get all values
     *
     * @param key
     * @return
     */
    public List<String> hvals(final String key) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.hvals(finalKey));
    }

    public Map<String, String> hgetAll(final String key) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.hgetAll(finalKey));
    }

    public Map<String, String> hgetAllNoPrefix(final String key) {
        return execute(key, jedis -> jedis.hgetAll(key));
    }

    //endregion

    //region set

    public Long sadd(final String key, final String... members) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.sadd(finalKey, members));
    }

    public Long sadd(final String key, final int seconds, final String... members) {
        String finalKey = getKey(key);
        return multiExecute(finalKey, tx -> {
            Response<Long> value = tx.sadd(finalKey, members);
            tx.expire(finalKey, seconds);

            return value;
        });
    }

    public Long saddNoPrefix(final String key, final String... members) {
        return execute(key, jedis -> jedis.sadd(key, members));
    }

    public Long saddNoPrefix(final String key, final int seconds, final String... members) {
        return multiExecute(key, tx -> {
            Response<Long> value = tx.sadd(key, members);
            tx.expire(key, seconds);

            return value;
        });
    }

    public Set<String> smembers(final String key) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.smembers(finalKey));
    }

    public Set<String> smembersNoPrefix(final String key) {
        return execute(key, jedis -> jedis.smembers(key));
    }

    public Boolean sismember(final String key, final String member) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.sismember(finalKey, member));
    }

    public Long srem(final String key, final String... members) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.srem(finalKey, members));
    }

    /**
     * remove many members from set
     *
     * @param key
     * @param set
     * @return
     */
    public Long srems(final String key, final Set<String> set) {
        String finalKey = getKey(key);
        Long result = 0L;
        for (String member : set) {
            Long execute = execute(key, jedis -> jedis.srem(key, member));
            result += execute;
        }
        return result;
    }

    /**
     * remove many members from set noPrefix
     *
     * @param key
     * @param set
     * @return
     */
    public Long sremsNoPrefix(final String key, final Set<String> set) {
        Long result = 0L;
        for (String member : set) {
            Long execute = execute(key, jedis -> jedis.srem(key, member));
            result += execute;
        }
        return result;
    }

    /**
     * get Sets element count
     * @param key
     * @return
     */
    public Long scard(final String key) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.scard(finalKey));
    }

    //endregion

    //region list

    /**
     * left push
     *
     * @param key
     * @param strings
     * @return
     */
    public Long lpush(final String key, final String... strings) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.lpush(finalKey, strings));
    }

    /**
     * left push expired
     *
     * @param key
     * @param seconds
     * @param strings
     * @return
     */
    public Long lpush(final String key, final int seconds, final String... strings) {
        String finalKey = getKey(key);
        return multiExecute(finalKey, tx -> {
            Response<Long> value = tx.lpush(finalKey, strings);
            tx.expire(finalKey, seconds);

            return value;
        });
    }

    /**
     * right push
     *
     * @param key
     * @param strings
     * @return
     */
    public Long rpush(final String key, final String... strings) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.rpush(finalKey, strings));
    }

    /**
     * right push use pipeline
     *
     * @param key
     * @param values
     * @return
     */
    public Boolean rpushByPipeline(final String key, final List<String> values) {
        if (values == null || values.size() == 0) {
            return true;
        }

        if (values.size() > PIPELINE_SIZE_LIMIT) {
            return false;
        }

        String finalKey = getKey(key);
        return execute(finalKey, (jedis) -> {
            Pipeline pipelined = jedis.pipelined();
            for (String value : values) {
                pipelined.rpush(finalKey, value);
            }
            pipelined.sync();
            return true;
        });
    }

    /**
     * right push expired
     *
     * @param key
     * @param seconds
     * @param strings
     * @return
     */
    public Long rpush(final String key, final int seconds, final String... strings) {
        String finalKey = getKey(key);
        return multiExecute(finalKey, tx -> {
            Response<Long> value = tx.rpush(finalKey, strings);
            tx.expire(finalKey, seconds);

            return value;
        });
    }

    /**
     * left pop
     *
     * @param key
     * @return
     */
    public String lpop(final String key) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.lpop(finalKey));
    }

    /**
     * right pop
     *
     * @param key
     * @return
     */
    public String rpop(final String key) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.rpop(finalKey));
    }

    /**
     * get list length
     *
     * @param key
     * @return
     */
    public Long llen(final String key) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.llen(finalKey));
    }

    /**
     * list get all values
     *
     * @param key
     * @return
     */
    public List<String> lall(final String key) {
        return lrange(key, 0, -1);
    }

    /**
     * list get range
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> lrange(final String key, final long start, final long end) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.lrange(finalKey, start, end));
    }

    /**
     * list get by index
     *
     * @param key
     * @param index
     * @return
     */
    public String lindex(final String key, final long index) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.lindex(finalKey, index));
    }

    /**
     * list remove value
     *
     * @param key
     * @param value
     * @return
     */
    public Long lrem(final String key, final String value) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.lrem(finalKey, 0L, value));
    }

    /**
     * blpop
     *
     * @param timeout timeout 单位：秒
     * @param keys    keys
     * @return result
     */
    public List<String> blpop(final int timeout, final String... keys) {
        if (keys == null || keys.length == 0) {
            throw new IllegalArgumentException("keys is not valid");
        }

        try (Jedis jedis = getJedis(keys)) {
            return jedis.blpop(timeout, keys);
        }
    }

    /**
     * brpop
     *
     * @param timeout timeout 单位：秒
     * @param keys    keys
     * @return result
     */
    public List<String> brpop(final int timeout, final String... keys) {
        if (keys == null || keys.length == 0) {
            throw new IllegalArgumentException("keys is not valid");
        }

        try (Jedis jedis = getJedis(keys)) {
            return jedis.brpop(timeout, keys);
        }
    }

    /**
     * ltrim
     *
     * @param key   key
     * @param start start index
     * @param end   end
     * @return result
     */
    public String ltrim(final String key, final long start, final long end) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.ltrim(finalKey, start, end));
    }

    /**
     * lpushx
     *
     * @param key     key
     * @param strings members
     * @return result
     */
    public Long lpushx(final String key, final String... strings) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.lpushx(finalKey, strings));
    }

    /**
     * rpushx
     *
     * @param key     key
     * @param strings members
     * @return result
     */
    public Long rpushx(final String key, final String... strings) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.rpushx(finalKey, strings));
    }
    //endregion

    //region sorted set

    /**
     * zcard
     *
     * @param key key
     * @return number
     */
    public Long zcard(final String key) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.zcard(finalKey));
    }

    /**
     * add member to set
     *
     * @param key
     * @param score
     * @param member
     * @return
     */
    public Long zadd(final String key, final double score, final String member) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.zadd(finalKey, score, member));
    }

    /**
     * add member to set with expiredTime
     *
     * @param key
     * @param score
     * @param member
     * @param seconds
     * @return
     */
    public Long zadd(final String key, final double score, final String member, final int seconds) {
        String finalKey = getKey(key);
        return multiExecute(finalKey, tx -> {
            Response<Long> result = tx.zadd(finalKey, score, member);
            tx.expire(finalKey, seconds);

            return result;
        });
    }

    /**
     * add members to set
     *
     * @param key
     * @param members
     * @return
     */
    public Long zadd(final String key, final Map<String, Double> members) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.zadd(finalKey, members));
    }

    /**
     * add members to set with expiredTime
     *
     * @param key
     * @param members
     * @param seconds
     * @return
     */
    public Long zadd(final String key, final Map<String, Double> members, final int seconds) {
        String finalKey = getKey(key);
        return multiExecute(finalKey, tx -> {
            Response<Long> result = tx.zadd(finalKey, members);
            tx.expire(finalKey, seconds);

            return result;
        });
    }

    /**
     * incr member step=1
     *
     * @param key
     * @param member
     * @return
     */
    public Double zincrby(final String key, final String member) {
        return zincrby(key, 1, member);
    }

    public Double zincrby(final String key, final double score, final String member) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.zincrby(finalKey, score, member));
    }

    /**
     * remove members from set
     *
     * @param key
     * @param members
     * @return
     */
    public Long zrem(final String key, final String... members) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.zrem(finalKey, members));
    }

    /**
     * get members by rank range asc
     *
     * @param key
     * @param rank1
     * @param rank2
     * @return
     */
    public Set<String> zrange(final String key, final long rank1, final long rank2) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.zrange(finalKey, rank1, rank2));
    }

    /**
     * zrank
     *
     * @param key    key
     * @param member member
     * @return rank
     */
    public Long zrank(final String key, final String member) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.zrank(finalKey, member));
    }

    /**
     * get members by rank range desc
     *
     * @param key
     * @param rank1
     * @param rank2
     * @return
     */
    public Set<String> zrevrange(final String key, final long rank1, final long rank2) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.zrevrange(finalKey, rank1, rank2));
    }

    /**
     * zrevrank
     *
     * @param key    key
     * @param member member
     * @return rank
     */
    public Long zrevrank(final String key, final String member) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.zrevrank(finalKey, member));
    }


    /**
     * get members by score range asc
     *
     * @param key
     * @param score1
     * @param score2
     * @return
     */
    public Set<String> zrangeByScore(final String key, final double score1, final double score2) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.zrangeByScore(finalKey, score1, score2));
    }

    /**
     * zrevrangeWithScores
     *
     * @param key   key
     * @param start start
     * @param end   end
     * @return tuple set
     */
    public Set<Tuple> zrevrangeWithScores(final String key, final long start, final long end) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.zrevrangeWithScores(finalKey, start, end));
    }

    /**
     * zrangeWithScores
     *
     * @param key   key
     * @param start start
     * @param end   end
     * @return tuple set
     */
    public Set<Tuple> zrangeWithScores(final String key, final long start, final long end) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.zrangeWithScores(finalKey, start, end));
    }

    /**
     * get members by score range desc
     *
     * @param key
     * @param score1
     * @param score2
     * @return
     */
    public Set<String> zrevrangeByScore(final String key, final double score1, final double score2) {

        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.zrevrangeByScore(finalKey, score1, score2));
    }

    /**
     * get score by member
     *
     * @param key
     * @param member
     * @return
     */
    public Double zscore(final String key, final String member) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.zscore(finalKey, member));
    }

    /**
     * get members count by score range
     *
     * @param key
     * @param score1
     * @param score2
     * @return
     */
    public Long zcount(final String key, final double score1, final double score2) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.zcount(finalKey, score1, score2));
    }

    public Long zremrangeByRank(final String key, final long start, final long end) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.zremrangeByRank(finalKey, start, end));
    }

    public Long zremrangeByScore(final String key, final double start, final double end) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.zremrangeByScore(finalKey, start, end));
    }

    public Long zremrangeByScore(final String key, final String start, final String end) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.zremrangeByScore(finalKey, start, end));
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.zrangeByScoreWithScores(finalKey, min, max));
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max, final int offset, final int count) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.zrangeByScoreWithScores(finalKey, min, max, offset, count));
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double min, final double max) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.zrevrangeByScoreWithScores(finalKey, min, max));
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double min, final double max, final int offset, final int count) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.zrevrangeByScoreWithScores(finalKey, min, max, offset, count));
    }


    //endregion


    // region key

    /**
     * scan form cache to get keys ,replace "keys *"
     * <p>
     * 使用此方法请将jedis包版本升级为2.9.0，否则使用时会抛异常
     *
     * @param key
     * @return
     */
    public List<String> scan(final String key) {
        String finalkey = getKey(key);
        // 匹配以 finalKey* 为前缀的 key
        return scanKeys(finalkey);
    }

    /**
     * scan form cache to get keys ,replace "keys *" ,noPrefix
     *
     * @param key
     * @return
     */
    public List<String> scanNoPrefix(final String key) {
        // 匹配以 key* 为前缀的 key
        return scanKeys(key);
    }

    private List<String> scanKeys(String key) {
        List<String> returnResult = new ArrayList<>();
        //初始游标
        String cursor = "0";
        ScanParams scanParams = new ScanParams();
        scanParams.match(key + "*");
        scanParams.count(1000);
        while (true) {
            String finalCursor = cursor;
            ScanResult<String> scanResult = execute(key, jedis -> jedis.scan(finalCursor, scanParams));
            List<String> result = scanResult.getResult();
            returnResult.addAll(result);
            //获取此次scan的游标
            cursor = scanResult.getStringCursor();
            if ("0".equals(cursor)) {
                //scan结束，已经获取到所有key
                break;
            }
        }
        return returnResult;
    }

    /**
     * del by batch keys
     *
     * @param keys
     * @return
     */
    public Long delKeys(final Set<String> keys) {
        Set<String> finalKeys = new HashSet<>();
        Long result = 0L;
        for (String key : keys) {
            String finalKey = getKey(key);
            finalKeys.add(finalKey);
        }
        if (finalKeys.size() > 0) {
            String[] keyArr = new String[finalKeys.size()];
            result = execute(keyArr, jedis -> jedis.del(finalKeys.toArray(keyArr)));
        }
        return result;
    }

    /**
     * del by batch keys noPrefix
     *
     * @param keys
     * @return
     */
    public Long delKeysNoPrefix(final Set<String> keys) {
        Long result = 0L;
        if (keys != null && keys.size() > 0) {
            String[] keyArr = new String[keys.size()];
            result = execute(keyArr, jedis -> jedis.del(keys.toArray(keyArr)));
        }
        return result;
    }

    /**
     * del key
     *
     * @param key
     * @return
     */
    public Long del(final String key) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.del(finalKey));
    }

    /**
     * del key noPrefix
     *
     * @param key
     * @return
     */
    public Long delNoPrefix(final String key) {
        return execute(key, jedis -> jedis.del(key));
    }


    public Boolean exists(final String key) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> jedis.exists(finalKey));
    }

    public List<String> keys(final String key) {
        Set<String> stringSet = execute(key, jedis -> jedis.keys(key));
        return new ArrayList<>(stringSet);
    }

    // endregion


    // region lock

    /**
     * 获取一个分布式锁 , 超时则返回失败
     *
     * @param key       锁的key
     * @param lockValue 锁的value
     * @param timeout   允许获取锁的时间，超过该时间就返回false，避免线程长时间获取锁失败，一般很多线程竞争同一把锁时会出现获取锁时间超时，
     *                  单位为 秒（注意）
     * @param expire    redis key的缓存时间，也即一个线程一次持有锁的时间，单位为 秒（注意）
     * @param sleepTime 获取锁的线程循环尝试获取锁的间隔时间，避免过度请求Redis造成风险，不传默认为20ms；单位为毫秒
     * @return 获锁成功 - true | 获锁失败 - false
     */
    public boolean tryLock(String key, String lockValue, Integer timeout, Integer expire, Integer sleepTime) {
        int st = (sleepTime == null) ? 20 : sleepTime;
        //允许获取锁的时间，默认30秒
        int expiredNx = 30;
        final long start = System.currentTimeMillis();
        if (timeout > expiredNx) {
            timeout = expiredNx;
        }
        final long end = start + timeout * 1000;
        // 默认返回失败
        boolean res = false;
        // 调用了下面的 lock方法
        while (!(res = this.lock(key, lockValue, expire))) {
            //如果尝试获取锁的时间超过了允许时间，则直接返回
            if (System.currentTimeMillis() > end) {
                break;
            }
            try {
                //add 2018-12-25 by suoyang       线程sleep，避免过度请求Redis，该值可以适当调整
                Thread.sleep(st);
            } catch (InterruptedException e) {
            }
        }
        return res;
    }

    /**
     * 释放锁
     *
     * @param key       锁的key
     * @param lockValue 锁的value  设置锁的时候存入的值
     * @return boolean
     */
    public boolean releaseLock(String key, String lockValue) {
        if (key == null || lockValue == null) {
            return false;
        }
        Object execute = execute(key, jedis -> jedis
                .eval(luaScript, Collections.singletonList(key), Collections.singletonList(lockValue)));
        return LOCK_RELEASE_STATE.equals(execute);
    }

    /**
     * 加锁
     *
     * @param key
     * @param lockValue 锁的值，释放锁时需要使用
     * @param expire    单位 秒
     * @return
     */
    public boolean lock(String key, String lockValue, int expire) {
        if (null == key) {
            return false;
        }
        //如果key 缓存时间超过了expire，相当于自动释放了锁
        String execute = execute(key, jedis -> jedis.set(key, lockValue, "NX", "EX", expire));
        return "OK".equals(execute);
    }

    // endregion lock

    // region lua

    /**
     * lua 执行脚本
     *
     * @param luaScript
     * @param keys
     * @param args
     * @return LuaResult
     */
    public LuaResult evalLua(String luaScript, List<String> keys, List<String> args) {
        if (StringUtils.isEmpty(luaScript)) {
            return LuaResult.fail("luaScript can not be null");
        }

        final List<String> finalKeys = keys == null ? new LinkedList<>() : keys;
        final List<String> finalArgs = args == null ? new LinkedList<>() : args;

        Object execute;
        if (finalKeys.size() == 0 && finalArgs.size() == 0) {
            execute = execute(lua_key, jedis -> jedis.eval(luaScript));
        } else {
            execute = execute(lua_key, jedis -> jedis.eval(luaScript, finalKeys, finalArgs));
        }

        return LuaResult.success(execute);
    }

    public Long geoAdd(String key, double longitude, double latitude, String member) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> {
            Long result = jedis.geoadd(finalKey, longitude, latitude, member);
            return result;
        });
    }

    public Double geoDist(String key, String member1, String member2) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> {
            Double result = jedis.geodist(finalKey, member1, member2);
            return result;
        });
    }

    public List<String> geoHash(String key, String member1, String member2) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> {
            return jedis.geohash(finalKey, member1, member2);
        });
    }

    public List<GeoCoordinate> geoPos(String key, String member1, String member2) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> {
            return jedis.geopos(finalKey, member1, member2);
        });
    }

    public List<GeoRadiusResponse> geoRadius(String key, double longitude, double latitude, double radius, GeoUnit unit) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> {
            return jedis.georadius(finalKey, longitude, latitude, radius, unit);
        });
    }

    public List<GeoRadiusResponse> geoRadiusByMember(String key, String member, double radius, GeoUnit unit, GeoRadiusParam geoRadiusParam) {
        String finalKey = getKey(key);
        return execute(finalKey, jedis -> {
            return jedis.georadiusByMember(key,  member,  radius,  unit, geoRadiusParam);
        });
    }

}


