package com.app.salesapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.app.salesapp.log.SalesAppLog;

import static com.app.salesapp.common.SalesAppConstant.LOG_TAG;

public abstract class BaseActivity extends OptimizedActivity {

    protected ActionBar action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null)
            action = getSupportActionBar();

        if (action != null) {
            View view = LayoutInflater.from(this).inflate(R.layout.title_bar, null);

            action.setDisplayOptions(ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_HOME |
                    ActionBar.DISPLAY_SHOW_CUSTOM);
            action.setDisplayHomeAsUpEnabled(true);
            action.setDisplayShowHomeEnabled(true);
            action.setHomeButtonEnabled(true);
            action.setCustomView(view);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setTranslucentStatus(true);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isTimeAutomatic()) {
            showSettingsAlert();
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Automatic Date Not Enabled");
        alertDialog.setMessage("Automatic Date Not Enable. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
                startActivity(intent);
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public boolean isTimeAutomatic() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(this.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1 &&
                    Settings.Global.getInt(this.getContentResolver(), Settings.Global.AUTO_TIME_ZONE, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(this.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1 &&
                    android.provider.Settings.System.getInt(this.getContentResolver(), android.provider.Settings.System.AUTO_TIME_ZONE, 0) == 1;
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    protected void showSnackBar(Activity activity, String message) {
        try {
            Snackbar.make(activity.getWindow().findViewById(android.R.id.content), message,
                    Snackbar.LENGTH_SHORT)
                    .show();
        } catch (NullPointerException e) {
            SalesAppLog.e(LOG_TAG, e);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItemCompat.setShowAsAction(menu.getItem(i), MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean postDelayed(Runnable r, long delayMillis) {
        return handler.postDelayed(r, delayMillis);
    }

    public void closeSoftKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
