package com.leon.flying.common;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobelExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public MzRespose doError(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Exception e){
        if(e instanceof MzBusinessException){
            return MzRespose.error(-1, e.getMessage());
        }else{
            return MzRespose.internalError();
        }
    }
}
