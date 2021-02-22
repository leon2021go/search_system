package com.leon.flying.common.redis;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Copyright (c) 2020 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 *
 * @author longmu
 * @since 2020/8/6
 */
public class HostAndPort implements Serializable {
        private static final long serialVersionUID = -237222847486052360L;
        static final String HOST_PORT_SEPARATOR = ":";
        private String host;
        private Integer port;

        public HostAndPort() {
        }

        public HostAndPort(String host, Integer port) {
            this.host = host;
            this.port = port;
        }

        public String getHost() {
            return this.host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return this.port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        static HostAndPort toHostAndPort(List<String> masterAddr) {
            if (masterAddr != null && masterAddr.size() == 2) {
                String host = (String)masterAddr.get(0);
                int port = Integer.parseInt((String)masterAddr.get(1));
                return port <= 0 ? null : new HostAndPort(host, port);
            } else {
                return null;
            }
        }

        static List<HostAndPort> toHostAndPortMulti(List<String> serverList) {
            if (serverList != null && serverList.size() > 0) {
                List<HostAndPort> list = new ArrayList(serverList.size());
                Iterator var2 = serverList.iterator();

                while(var2.hasNext()) {
                    String server = (String)var2.next();
                    HostAndPort hap = toHostAndPort(server);
                    if (hap != null) {
                        list.add(hap);
                    }
                }

                return list;
            } else {
                return Collections.emptyList();
            }
        }

        static HostAndPort toHostAndPort(String server) {
            if (server != null && server.length() > 0) {
                String[] hapStr = server.split(":");
                if (hapStr.length != 2) {
                    return null;
                } else {
                    HostAndPort hap = new HostAndPort();
                    hap.setHost(hapStr[0]);
                    if (!NumberUtils.isNumber(hapStr[1])) {
                        return null;
                    } else {
                        hap.setPort(Integer.parseInt(hapStr[1]));
                        return hap;
                    }
                }
            } else {
                return null;
            }
        }

        @Override
        public String toString() {
            return String.format("[%s%s%s]", this.host, ":", this.port);
        }
    }