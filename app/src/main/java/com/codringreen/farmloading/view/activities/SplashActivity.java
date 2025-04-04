package com.codringreen.farmloading.view.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.codringreen.farmloading.R;
import com.codringreen.farmloading.helper.PreferenceManager;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends BaseActivity {

    @Override
    protected void initVariable(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);
        initComponents();
    }

    private void initComponents() {
        try {

            hideKeyboard(this);

            new Handler().postDelayed(() -> {
                if (PreferenceManager.INSTANCE.isLoggedIn()) {
                    startActivity(new Intent(SplashActivity.this, DashboardActivity.class)
                            .putExtra("DownloadMaster", "No")
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else {
                    PreferenceManager.INSTANCE.clearLoginDetails();
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            }, 2500);

        } catch (Exception e) {
            Log.e("SplashActivity", "Error in SplashActivity InitComponents", e);
        }
    }
}