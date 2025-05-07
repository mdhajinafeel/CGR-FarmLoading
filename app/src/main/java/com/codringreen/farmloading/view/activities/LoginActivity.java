package com.codringreen.farmloading.view.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.codringreen.farmloading.BuildConfig;
import com.codringreen.farmloading.R;
import com.codringreen.farmloading.helper.PreferenceManager;
import com.codringreen.farmloading.model.request.LoginRequest;
import com.codringreen.farmloading.utils.CustomProgress;
import com.codringreen.farmloading.viewmodel.LoginViewModel;

import java.util.Objects;

import javax.inject.Inject;

public class LoginActivity extends BaseActivity {

    private AppCompatEditText etUserName, etPassword;
    private AppCompatButton btnLogin;
    private LoginViewModel loginViewModel;
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    protected void initVariable(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        initComponents();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initComponents() {
        try {

            hideKeyboard(this);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
                }
            }

            etUserName = findViewById(R.id.etUserName);
            etPassword = findViewById(R.id.etPassword);
            btnLogin = findViewById(R.id.btnLogin);

            loginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);

            etPassword.setOnTouchListener((v, event) -> {
                int drawableRight = etPassword.getCompoundDrawablesRelative()[2] != null ?
                        etPassword.getCompoundDrawablesRelative()[2].getBounds().width() : 0;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etPassword.getRight() - drawableRight)) {
                        if (etPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_eye_closed, 0);
                        } else {
                            etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_eye_opened, 0);
                        }
                        etPassword.setSelection(Objects.requireNonNull(etPassword.getText()).length());
                        v.performClick();
                        return true;
                    }
                }
                return false;
            });

            etUserName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    validateFields();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            etPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    validateFields();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            btnLogin.setOnClickListener(v -> {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
                    }
                }

                LoginRequest loginRequest = new LoginRequest(1, BuildConfig.ROLE_ID, Objects.requireNonNull(etUserName.getText()).toString(),
                        Objects.requireNonNull(etPassword.getText()).toString(), PreferenceManager.INSTANCE.getKeyFirebaseToken());

                loginViewModel.postLogin(loginRequest);
            });

            loginViewModel.getError().observe(this, s -> showDialog(s, loginViewModel.getErrorTitle(), null, getString(R.string.text_ok)));

            loginViewModel.getLoginStatus().observe(this, aBoolean -> startMainActivity());

            loginViewModel.getProgressBar().observe(this, aBoolean -> {
                if (aBoolean) {
                    if (getApplicationContext() != null) {
                        CustomProgress.getInstance().showProgress(this);
                    } else {
                        CustomProgress.getInstance().dismissProgress();
                    }
                } else {
                    CustomProgress.getInstance().dismissProgress();
                }
            });

        } catch (Exception e) {
            Log.e("LoginActivity", "Error in LoginActivity InitComponents", e);
        }
    }

    private void validateFields() {
        if (!Objects.requireNonNull(etUserName.getText()).toString().isEmpty()) {
            btnLogin.setEnabled(!Objects.requireNonNull(etPassword.getText()).toString().isEmpty());
        } else {
            btnLogin.setEnabled(false);
        }
    }

    private void startMainActivity() {
        try {
            if (PreferenceManager.INSTANCE.isLoggedIn()) {
                startActivity(new Intent(LoginActivity.this, DashboardActivity.class)
                        .putExtra("DownloadMaster", "Yes")
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } catch (Exception e) {
            Log.e("LoginActivity", "Error in LoginActivity startMainActivity", e);
        }
    }
}