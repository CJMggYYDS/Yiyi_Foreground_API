package com.yiyi_app.util;

import lombok.Data;

/**
 * 统一返回体
 * @date: 2022/7/2
 */
@Data
public class ResponseResult {

    private int code;
    private String msg;
    private Object data;

    public ResponseResult(int code, String msg) {
        this.code=code;
        this.msg=msg;
    }

    public ResponseResult(int code, String msg, Object data) {
        this.setCode(code);
        this.setMsg(msg);
        this.setData(data);
    }

    //无需返回数据的请求成功
    public static ResponseResult success() {
        return new ResponseResult(ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.getMsg());
    }

    //有返回数据的请求成功
    public static ResponseResult success(Object data) {
        return new ResponseResult(ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.getMsg(), data);
    }

    public static ResponseResult error() {
        return new ResponseResult(ResponseCodeEnum.FAIL.getCode(), ResponseCodeEnum.FAIL.getMsg());
    }

    public static ResponseResult error(int code, String msg) {
        return new ResponseResult(code, msg);
    }

    public static ResponseResult error(int code, String msg, Object data) {
        return new ResponseResult(code, msg, data);
    }
}
