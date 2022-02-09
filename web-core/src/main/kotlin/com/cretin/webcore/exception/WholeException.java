package com.cretin.webcore.exception;

import com.cretin.webcore.config.AppConstants;

/**
 * @author cretin
 * @since 2018-12-13
 */
public class WholeException extends RuntimeException {

    public WholeException() {
        this.msg = AppConstants.COMM_ERROR;
        this.code = 0;
    }

    public WholeException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public WholeException(String msg) {
        this.msg = msg;
        this.code = 0;
    }

    public static WholeException createLoginExpire(){
        return new WholeException(202,"用户登录过期");
    }

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}