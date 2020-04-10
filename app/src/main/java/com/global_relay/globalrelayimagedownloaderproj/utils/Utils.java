package com.global_relay.globalrelayimagedownloaderproj.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;

public class Utils {
    private static Utils utils = new Utils();
    private Context context;

    private Utils() {
    }

    public static Utils getInstance(Context context) {
        if (utils.context == null) {
            utils.context = context;
        }
        return utils;
    }

    public boolean writeToFile(String path, byte[] data) {
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.mkdirs();
            }
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(data, 0, data.length);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    public boolean isInternetAvailable() {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiConn = false;
        boolean isMobileConn = false;
        for (Network network : connMgr.getAllNetworks()) {
            NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                isWifiConn |= networkInfo.isConnected();
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                isMobileConn |= networkInfo.isConnected();
            }
        }
        return isWifiConn || isMobileConn;
    }

    public void openDeviceConnectionSetting() {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public Snackbar showSnackBar(View contextView, String msg, int duration, String actionTitle, View.OnClickListener actionListener) {
        Snackbar snackbar = Snackbar.make(contextView, msg, duration);
        if (actionListener != null) {
            snackbar.setAction(actionTitle, actionListener);
        }
        snackbar.show();
        return snackbar;
    }

    public void cancelSnackBar(Snackbar snackbar) {
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }
}
