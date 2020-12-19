package com.wz.modules.common.utils;

import java.io.Serializable;

public class CommonResponse<T> implements Serializable {

    private int code;

    private String msg;

    private int count = 0;

    private T data;

    public CommonResponse() {
    }

    public CommonResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CommonResponse(int code, String msg, T data) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public CommonResponse(int code, String msg, Integer count, T data) {
        super();
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }

    private static final Integer SUCCESS = 200;
    private static final Integer SUCCESS_0 = 0;
    private static final Integer FAILD = 500;
    private static final Integer WORDFAILD = 560;
    private static final Integer PASSWORDFAILD = 550;
    private static final Integer PHONECODEFAILD = 530;

    public static <T> CommonResponse<T> success() {
        return buildResponse(SUCCESS, "success", null);
    }

    public static <T> CommonResponse<T> success(String msg) {
        return buildResponse(SUCCESS, msg, null);
    }

    public static <T> CommonResponse<T> success(T data) {
        return buildResponse(SUCCESS, "success", data);
    }

    public static <T> CommonResponse<T> success(Integer count, T data) {
        return buildResponse(SUCCESS, "success", count, data);
    }

    public static <T> CommonResponse<T> success(Integer code, String msg) {
        return buildResponse(code, msg, 0, null);
    }

    public static <T> CommonResponse<T> error() {
        return buildResponse(555, "error", 0, null);
    }

    public static <T> CommonResponse<T> error(Integer code, String msg) {
        return new CommonResponse<T>(code, msg);
    }

    public static <T> CommonResponse<T> error(String msg) {
        return buildResponse(555, msg, 0, null);
    }

    public static <T> CommonResponse<T> success(Integer code, Integer count, T data) {
        return buildResponse(code, "success", count, data);
    }

    public static <T> CommonResponse<T> success(Integer code, String msg, T data) {
        return buildResponse(code, msg, data);
    }

    public static <T> CommonResponse<T> success(Integer code, Integer count, String msg) {
        return buildResponse(code, msg, count, null);
    }

    public static <T> CommonResponse<T> success(Integer code, Integer count, String msg, T data) {
        return buildResponse(code, msg, count, data);
    }

    public static <T> CommonResponse<T> faild() {
        return buildResponse(FAILD, "source not exist", null);
    }

    public static <T> CommonResponse<T> faild(String msg) {
        return buildResponse(FAILD, msg, null);
    }

    public static <T> CommonResponse<T> phoneCodeFaild(String msg) {
        return buildResponse(PHONECODEFAILD, msg, null);
    }

    public static <T> CommonResponse<T> passWordFaild(String msg) {
        return buildResponse(PASSWORDFAILD, msg, null);
    }

    public static <T> CommonResponse<T> wordFaild(String msg) {
        return buildResponse(WORDFAILD, msg, null);
    }

    public static <T> CommonResponse<T> faild(String msg, T data) {
        return buildResponse(FAILD, msg, data);
    }

    public static <T> CommonResponse<T> buildResponse(Integer code, String msg, T data) {
        return new CommonResponse<T>(code, msg, data);
    }

    public static <T> CommonResponse<T> buildResponse(Integer code, String msg, Integer count, T data) {
        return new CommonResponse<T>(code, msg, count, data);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public CommonResponse setData(T data) {
        this.data = data;
        return this;
    }
}
