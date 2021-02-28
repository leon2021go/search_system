package com.leon.flying.common;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobelExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public MzRespose doError(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Exception e){
        e.printStackTrace();
        if(e instanceof MzBusinessException){
            return MzRespose.error(-1, e.getMessage());
        }else if(e instanceof NoHandlerFoundException){
            return MzRespose.error(-1, "找不到执行路径");
        }else{
            return MzRespose.internalError();
        }
    }
}
