package com.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.StringRes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    private static Toast mToast;
    private static ProgressDialog mDialog;

    public static void showToast(Context context, @StringRes int messageId) {
        if (mToast != null)
            mToast.cancel();

        mToast = Toast.makeText(context, messageId, Toast.LENGTH_LONG);
        mToast.show();
    }

    public static void showLoading(Context context) {
        hideLoading();
        mDialog = ProgressDialog.show(context, "", "Loading ...", true);
        mDialog.show();
    }

    public static void hideLoading() {
        if (mDialog == null) {
            return;
        }
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    /**
     * Check email validity
     *
     * @param email the email that entered by the user
     * @return is email valid or not
     */
    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String parseDateToMMddYYYY(long timeinMilliSeccond) {
        return new SimpleDateFormat("MM/dd/yyyy").format(new Date(timeinMilliSeccond));
    }

}