package com.codringreen.farmloading.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class UserPreferences {

    private final SharedPreferences mPreference;

    public UserPreferences(Context context, String preferenceName) {
        this.mPreference = context.getSharedPreferences(preferenceName, 0);
    }

    public void setString(String key, String value) {
        this.mPreference.edit().putString(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return this.mPreference.getString(key, defaultValue);
    }

    public void setInt(String key, int value) {
        this.mPreference.edit().putInt(key, value).apply();
    }

    public int getInt(String key, int defaultValue) {
        return this.mPreference.getInt(key, defaultValue);
    }

    public void setBoolean(String key, boolean value) {
        this.mPreference.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return this.mPreference.getBoolean(key, defaultValue);
    }

    public void setLong(String key, Long value) {
        this.mPreference.edit().putLong(key, value).apply();
    }

    public Long getLong(String key, Long defaultValue) {
        return this.mPreference.getLong(key, defaultValue);
    }

    public void clearPreference() {
        this.mPreference.edit().clear().apply();
    }

    public void removePreferenceWithKey(String key) {
        this.mPreference.edit().remove(key).apply();
    }

    public void setStringSet(String key, Set<String> values) {
        this.mPreference.edit().putStringSet(key, values).apply();
    }

    public Set<String> getStringSet(String key) {
        return this.mPreference.getStringSet(key, null);
    }
}