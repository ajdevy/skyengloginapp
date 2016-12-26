
package com.skyeng.app.login.data.json;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse extends ErrorCodeResponse {

    public static final int ERROR_WRONG_CODE = 1;
    public static final int ERROR_WRONG_EMAIL_OR_PASSWORD = 2;

    @JsonProperty("jwt")
    private String jwt;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("jwt")
    public String getJwt() {
        return jwt;
    }

    @JsonProperty("jwt")
    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public LoginResponse(String jwt, Integer errorCode) {
        super(errorCode);
        this.jwt = jwt;
    }

    public LoginResponse(String jwt) {
        super();
        this.jwt = jwt;
    }

    public LoginResponse() {
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                super.toString() +
                ", jwt='" + jwt + '\'' +
                '}';
    }
}