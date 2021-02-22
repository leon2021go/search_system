package com.leon.flying.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Copyright (c) 2020 mengzhua, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 *
 * @author longmu
 * @since 2020/8/6
 */
//@ConfigurationProperties(prefix = "weimai.redis.sentinel")
public class SentinelClientProperty {

    private List<String> server;

    private List<String> masterName;


    public List<String> getServer() {
        return server;
    }

    public void setServer(List<String> server) {
        this.server = server;
    }

    public List<String> getMasterName() {
        return masterName;
    }

    public void setMasterName(List<String> masterName) {
        this.masterName = masterName;
    }

}