package com.codringreen.farmloading.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Window;

import com.codringreen.farmloading.R;

public class CustomProgress {

    private static volatile CustomProgress instance;
    private Dialog sDialog;

    private CustomProgress() {}

    public static synchronized CustomProgress getInstance() {
        if (instance == null) {
            instance = new CustomProgress();
        }
        return instance;
    }

    public void showProgress(Context context) {
        if (!(context instanceof Activity)) {
            Log.e("CustomProgress", "Invalid context, must be an instance of Activity.");
            return;
        }

        Activity activity = (Activity) context;
        if (activity.isFinishing() || activity.isDestroyed()) {
            Log.e("CustomProgress", "Activity is not in a valid state to show dialog.");
            return;
        }

        dismissProgress();

        try {
            sDialog = new Dialog(context, R.style.progress_dialog);
            sDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            sDialog.setContentView(R.layout.custom_progressbar);
            sDialog.setCancelable(false);
            sDialog.show();
        } catch (Exception e) {
            Log.e("CustomProgress", "Error showing progress dialog", e);
        }
    }

    public void dismissProgress() {
        if (sDialog != null && sDialog.isShowing()) {
            try {
                sDialog.dismiss();
            } catch (Exception e) {
                Log.e("CustomProgress", "Error dismissing progress dialog", e);
            } finally {
                sDialog = null;
            }
        }
    }
}