package com.codringreen.farmloading.model.request;

import java.io.Serializable;

public class LoginRequest implements Serializable {

    private int originId, roleId;
    private String username, password;
    private String fcmToken;

    public int getOriginId() {
        return originId;
    }

    public void setOriginId(int originId) {
        this.originId = originId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginRequest(int originId, int roleId, String username, String password, String fcmToken) {
        this.originId = originId;
        this.roleId = roleId;
        this.username = username;
        this.password = password;
        this.fcmToken = fcmToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}