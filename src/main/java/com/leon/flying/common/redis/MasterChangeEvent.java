package com.leon.flying.common.redis;

import java.util.EventObject;

/**
 * Copyright (c) 2020 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 *
 * @author longmu
 * @since 2020/8/6
 */
public class MasterChangeEvent extends EventObject {


    private static final long serialVersionUID = 1L;
    private HostAndPort oldMaster;
    private HostAndPort newMaster;

    public HostAndPort getOldMaster() {
        return oldMaster;
    }

    public void setOldMaster(HostAndPort oldMaster) {
        this.oldMaster = oldMaster;
    }

    public HostAndPort getNewMaster() {
        return newMaster;
    }

    public void setNewMaster(HostAndPort newMaster) {
        this.newMaster = newMaster;
    }

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public MasterChangeEvent(Object source) {
        super(source);
    }}