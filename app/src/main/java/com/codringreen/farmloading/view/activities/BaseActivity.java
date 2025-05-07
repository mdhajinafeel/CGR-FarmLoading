package com.codringreen.farmloading.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;

import com.codringreen.farmloading.R;
import com.codringreen.farmloading.utils.CommonUtils;
import com.google.firebase.FirebaseApp;

import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;

public abstract class BaseActivity extends DaggerAppCompatActivity {

    protected abstract void initVariable(Bundle savedInstanceState);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);

        //FIREBASE
        FirebaseApp.initializeApp(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            lockPortraitMode();
        }

        initVariable(savedInstanceState);
    }

    public void hideKeyboard(Context ctx) {
        InputMethodManager inputMethodManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocus = ((Activity) ctx).getCurrentFocus();
        if (currentFocus == null) {
            return;
        }
        inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
    }

    public void showDialog(String message, String positiveText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(message);
        builder.setCancelable(false);
        builder.setPositiveButton(positiveText, (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        if (!dialog.isShowing())
            dialog.show();
    }

    public void showDialog(String message, String title, @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener, String positiveText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(CommonUtils.customFontTypeFace(ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_bold), title));
        builder.setMessage(CommonUtils.customFontTypeFace(ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_medium), message));
        builder.setCancelable(false);

        if (onPositiveButtonClickListener == null) {
            onPositiveButtonClickListener = (dialog, which) -> dialog.dismiss();
        }

        builder.setPositiveButton(CommonUtils.customFontTypeFace(ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_semibold), positiveText),
                onPositiveButtonClickListener);

        AlertDialog dialog = builder.create();
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void showDialogWithCancel(String message, String title, @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                                     String positiveText, String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(CommonUtils.customFontTypeFace(ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_bold), title));
        builder.setMessage(CommonUtils.customFontTypeFace(ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_medium), message));
        builder.setCancelable(false);
        builder.setPositiveButton(CommonUtils.customFontTypeFace(ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_semibold), positiveText),
                onPositiveButtonClickListener);
        builder.setNegativeButton(CommonUtils.customFontTypeFace(ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat_semibold), negativeText),
                (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        if (!dialog.isShowing())
            dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public void lockPortraitMode() {
        getWindow().setAttributes(new WindowManager.LayoutParams() {{
            preferredDisplayModeId = 1;
        }});
    }
}