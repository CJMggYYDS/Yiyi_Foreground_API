package com.yiyi_app.util;

/**
 *  统一返回体状态码
 * @author cjm
 * @date: 2022/7/2
 */
public enum ResponseCodeEnum {
    SUCCESS(200, "请求成功, 后端已处理"),
    FAIL(999, "后端错误, 操作失败"),
    PASSWORD_ERROR(1000, "密码错误"),
    LOGIN_ERROR(1001, "用户名不存在"),
    PARAM_ERROR(1002, "请求参数错误"),
    TOKEN_MISSION(1003, "未携带token,没有权限"),
    TOKEN_INVALID(1004, "token校验错误"),
    USER_EXISTED(1005, "用户名已存在"),
    RETURNITEM_ERROR(203,"归还商品失败，归还逾期，需要交费");

    private int code;
    private String msg;

    ResponseCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
