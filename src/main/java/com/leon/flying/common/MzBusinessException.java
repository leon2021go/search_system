package com.leon.flying.common;

/**
 *
 *业务异常封装
 * Copyright (c) 2017 Choice, Inc. All Rights Reserved. Choice Proprietary and
 * Confidential.
 *
 * @author leon
 * @since 2017年9月11日
 */
public class MzBusinessException extends RuntimeException {

    /**  */
    private static final long serialVersionUID = -5034167684619559206L;
    private MzRespose result;
    private CommonError commonError;

    public MzBusinessException(EmBussinessError emBussinessError){
        super();
        this.commonError = new CommonError(emBussinessError);
    }


    public MzBusinessException(MzRespose result) {
        super(result.getMessage());
        this.result = result;
    }


    public MzRespose getResult() {
        return result;
    }

    public void setResult(MzRespose result) {
        this.result = result;
    }

}
