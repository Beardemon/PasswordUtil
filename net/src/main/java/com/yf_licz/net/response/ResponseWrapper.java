package com.yf_licz.net.response;

import com.google.gson.annotations.SerializedName;

/**
 * 新接口返回数据包装类
 *
 * author liu haonan on 2016/07/13
 */
public class ResponseWrapper<Result> {

    @SerializedName("success")
    boolean success;
    @SerializedName("result")
    Result result;
    @SerializedName("errorCode")
    String errorCode;
    @SerializedName("errorMsg")
    String errorMsg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
