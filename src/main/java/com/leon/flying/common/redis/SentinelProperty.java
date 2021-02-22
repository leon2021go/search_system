package com.leon.flying.common.redis;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (c) 2020 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 *
 * @author longmu
 * @since 2020/8/6
 */


public class SentinelProperty implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<String> server;
    private List<String> masterName;

    public SentinelProperty() {
    }

    public List<String> getServer() {
        return this.server;
    }

    public void setServer(List<String> server) {
        this.server = server;
    }

    public List<String> getMasterName() {
        return this.masterName;
    }

    public void setMasterName(List<String> masterName) {
        this.masterName = masterName;
    }

}
