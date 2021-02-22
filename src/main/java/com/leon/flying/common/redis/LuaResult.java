package com.leon.flying.common.redis;

import java.io.Serializable;

/**
 * Copyright (c) 2020 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 *
 * @author longmu
 * @since 2020/8/6
 */
public class LuaResult implements Serializable {
    private static final long serialVersionUID = 5422323554332353537L;

    private boolean evalResult;
    private Object result;
    private String errorMsg;

    private LuaResult(boolean evalResult, Object result) {
        this.evalResult = evalResult;
        this.result = result;
    }

    private LuaResult(boolean evalResult, String errorMsg) {
        this.evalResult = evalResult;
        this.errorMsg = errorMsg;
    }

    public boolean getEvalResult() {
        return evalResult;
    }

    public void setEvalResult(boolean evalResult) {
        this.evalResult = evalResult;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "LuaResult{" + "evalResult=" + evalResult + ", result=" + result + ", errorMsg='" + errorMsg + '\''
                + '}';
    }

    public static LuaResult success(Object object) {
        return new LuaResult(true, object);
    }

    public static LuaResult fail(String message) {
        return new LuaResult(false, message);
    }
}