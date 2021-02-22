package com.leon.flying.common;

/**
 * api统一响应类
 * @param <T>
 */
public class MzRespose<T> {

    private static final long serialVersionUID = -440772889286277256L;
    private static int SUCCESS_CODEID = 0;
    private static int INTERNAL_CODEID = -1;
    private int code;
    private String message;
    private T data;

    private MzRespose(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public MzRespose(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private MzRespose(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public static <T> MzRespose<T> success(T data) {
        return new MzRespose<>(SUCCESS_CODEID, data, "");
    }

    public static MzRespose error(int code, String message) {
        return new MzRespose(code, message);
    }

    public static MzRespose internalError() {
        return new MzRespose(INTERNAL_CODEID, "小爪开小差了，请您稍后重试");
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
