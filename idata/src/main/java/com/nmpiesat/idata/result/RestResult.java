package com.nmpiesat.idata.result;


import java.sql.Timestamp;

/**
 * 统一返回Rest风格的数据结构
 *
 * @author zhaoxiaojun
 * Created on 2020年2月20日11:20:35.
 */
public class RestResult {

    /**
     * 成功或者失败的code错误码
     */
    private String code;
    /**
     * 成功时返回的数据，失败时返回具体的异常信息
     */
    private Object data;
    /**
     * 请求失败返回的提示信息，给前端进行页面展示的信息
     */
    private String message;

    /**
     * 服务器当前时间（添加该字段的原因是便于查找定位请求时间，因为实际开发过程中服务器时间可能跟本地时间不一致，加上这个时间戳便于日后定位）
     */
    private Timestamp currentTime;

    public RestResult() {
    }

    public RestResult(String code, Object data, String message, Timestamp currentTime) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.currentTime = currentTime;
    }

    @Override
    public String toString() {
        return "RestResult{" +
                "code='" + code + '\'' +
                ", data=" + data +
                ", message='" + message + '\'' +
                ", currentTime=" + currentTime +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Timestamp getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Timestamp currentTime) {
        this.currentTime = currentTime;
    }
}
