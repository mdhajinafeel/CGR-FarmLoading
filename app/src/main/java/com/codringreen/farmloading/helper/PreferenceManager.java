package com.codringreen.farmloading.helper;

import android.content.Context;

public enum PreferenceManager {

    INSTANCE;
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_INSTANCEID = "instanceId";
    private static final String KEY_ACCESSTOKEN = "accessToken";
    private static final String KEY_REFRESHTOKEN = "refreshToken";
    private static final String KEY_NAME = "name";
    private static final String KEY_LOGIN_EXPIRY = "loginExpiry";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_ORIGIN_ID = "originId";
    private static final String LAST_TEMP_RECEPTION_ID = "LAST_TEMP_RECEPTION_ID";
    private static final String KEY_FIREBASE_TOKEN = "fb_token";
    public static final String PREF_NAME = "CGR_FARM_PREF";

    private UserPreferences preferenceHandle;

    public void createPreferences(Context context) {
        this.preferenceHandle = new UserPreferences(context, PREF_NAME);
    }

    public void setKeyInstanceid(String instanceid) {
        this.preferenceHandle.setString(KEY_INSTANCEID, instanceid);
    }

    public String getAccessToken() {
        return this.preferenceHandle.getString(KEY_ACCESSTOKEN, "");
    }

    public void setAccessToken(String accessToken) {
        this.preferenceHandle.setString(KEY_ACCESSTOKEN, accessToken);
    }

    public void setRefreshToken(String refreshToken) {
        this.preferenceHandle.setString(KEY_REFRESHTOKEN, refreshToken);
    }

    public void setLoggedIn(Boolean loggedIn) {
        this.preferenceHandle.setBoolean(KEY_IS_LOGGED_IN, loggedIn);
    }

    public Boolean isLoggedIn() {
        return this.preferenceHandle.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getName() {
        return this.preferenceHandle.getString(KEY_NAME, "");
    }

    public void setName(String name) {
        this.preferenceHandle.setString(KEY_NAME, name);
    }

    public Long getLoginExpiry() {
        return this.preferenceHandle.getLong(KEY_LOGIN_EXPIRY, 0L);
    }

    public void setLoginExpiry(Long loginExpiry) {
        this.preferenceHandle.setLong(KEY_LOGIN_EXPIRY, loginExpiry);
    }

    public String getPhoto() {
        return this.preferenceHandle.getString(KEY_PHOTO, "");
    }

    public void setPhoto(String photo) {
        this.preferenceHandle.setString(KEY_PHOTO, photo);
    }

    public int getUserId() {
        return this.preferenceHandle.getInt(KEY_USER_ID, 0);
    }

    public void setUserId(int userId) {
        this.preferenceHandle.setInt(KEY_USER_ID, userId);
    }

    public int getOriginId() {
        return this.preferenceHandle.getInt(KEY_ORIGIN_ID, 0);
    }

    public void setOriginId(int originId) {
        this.preferenceHandle.setInt(KEY_ORIGIN_ID, originId);
    }

    public void setLastTempReceptionId(String value) {
        this.preferenceHandle.setString(LAST_TEMP_RECEPTION_ID, value);
    }

    public String getLastTempReceptionId() {
        return this.preferenceHandle.getString(LAST_TEMP_RECEPTION_ID, "");
    }

    public String getKeyFirebaseToken() {
        return this.preferenceHandle.getString(KEY_FIREBASE_TOKEN, "");
    }

    public void setKeyFirebaseToken(String firebaseToken) {
        this.preferenceHandle.setString(KEY_FIREBASE_TOKEN, firebaseToken);
    }

    public void clearLoginDetails() {
        preferenceHandle.removePreferenceWithKey(KEY_ACCESSTOKEN);
        preferenceHandle.removePreferenceWithKey(KEY_REFRESHTOKEN);
        preferenceHandle.removePreferenceWithKey(KEY_IS_LOGGED_IN);
        preferenceHandle.removePreferenceWithKey(KEY_INSTANCEID);
        preferenceHandle.removePreferenceWithKey(KEY_ORIGIN_ID);
        preferenceHandle.removePreferenceWithKey(KEY_NAME);
        preferenceHandle.removePreferenceWithKey(KEY_PHOTO);
        preferenceHandle.removePreferenceWithKey(KEY_LOGIN_EXPIRY);
        preferenceHandle.removePreferenceWithKey(KEY_USER_ID);
    }
}