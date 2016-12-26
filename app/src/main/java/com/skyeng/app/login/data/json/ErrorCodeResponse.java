
package com.skyeng.app.login.data.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorCodeResponse {

    @JsonProperty("errorCode")
    private Integer errorCode;


    @JsonProperty("errorCode")
    public Integer getErrorCode() {
        return errorCode;
    }

    @JsonProperty("errorCode")
    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCodeResponse() {

    }

    public ErrorCodeResponse(Integer errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "errorCode=" + errorCode +
                '}';
    }

    public boolean hasError() {
        return errorCode != null;
    }
}