package com.piesat.kettlescheduler.common;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;

public class Result {

    /**
     * 定义返回值状态
     */
    private String status;
    /**
     * 返回值提示信息
     */
    private String message;
    /**
     * 返回值数据
     */
    private Object data;

    /**
     * jackson进行json序列化工具
     */
    private static ObjectMapper objectMapper = new ObjectMapper();
    /**
     * 序列化中的时间格式化
     */
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private Result(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public Result(Object data, String status, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public Result(ResultCode resultCode) {
        this.status = resultCode.getStatus();
        this.message = resultCode.getMessage();
    }

    public Result(Object data, ResultCode resultCode) {
        this.data = data;
        this.status = resultCode.getStatus();
        this.message = resultCode.getMessage();
    }

    /**
     * 返回成功，无data
     *
     * @return JSON字符串
     */
    public static String success() {
        try {
            return objectMapper.writeValueAsString(new Result(ResultCode.SUCCESS));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 返回成功，返回单个实体
     *
     * @param data 单个实体的数据
     * @return JSON字符串
     */
    public static String success(Object data) {
        try {
            objectMapper.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_FORMAT));
            return objectMapper.writeValueAsString(new Result(data, ResultCode.SUCCESS));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 返回成功，返回单个实体
     *
     * @param status  单个实体的返回状态
     * @param message 单个实体的提示信息
     * @param data    单个实体的数据
     * @return JSON字符串
     */
    public static String success(Object data, String status, String message) {
        try {
            objectMapper.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_FORMAT));
            return objectMapper.writeValueAsString(new Result(data, status, message));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 返回成功，返回单个实体
     *
     * @param resultCode  提示码、信息
     * @param data    单个实体的数据
     * @return JSON字符串
     */
    public static String success(Object data, ResultCode resultCode) {
        try {
            objectMapper.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_FORMAT));
            return objectMapper.writeValueAsString(new Result(data, resultCode));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 返回失败
     *
     * @param message 返回失败的提示信息
     * @return JSON字符串
     */
    public static String fail(String message) {
        try {
            return objectMapper.writeValueAsString(new Result("100000", message));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 返回失败
     *
     * @param resultCode 返回失败的提示码、信息
     * @return JSON字符串
     */
    public static String fail(ResultCode resultCode) {
        try {
            return objectMapper.writeValueAsString(new Result(resultCode));
        } catch (Exception e) {
            return null;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}