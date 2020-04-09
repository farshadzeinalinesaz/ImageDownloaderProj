package com.global_relay.globalrelayimagedownloaderproj.view.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.global_relay.globalrelayimagedownloaderproj.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class SplashActivity extends AppCompatActivity
{
    private static final int DURATION=1000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkRequirePermissions();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int permissionStatus : grantResults) {
            if (permissionStatus == PackageManager.PERMISSION_DENIED) {
                SplashActivity.this.finish();
                return;
            }
        }
        goNextActivity();
    }


    private void checkRequirePermissions() {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                showPermissionDialog(permissions);
                return;
            }
        }
        goNextActivity();
    }

    private void showPermissionDialog(String[] permissions) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(getResources().getString(R.string.permission_title))
                .setMessage(getResources().getString(R.string.permission_msg))
                .setPositiveButton(getResources().getString(R.string.agree), (DialogInterface dialog, int which) -> {
                    ActivityCompat.requestPermissions(SplashActivity.this, permissions, 0);
                })
                .setNegativeButton(getResources().getString(R.string.disagree), (DialogInterface dialog, int which) -> {
                    SplashActivity.this.finish();
                })
                .show();
    }

    private void goNextActivity()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }
        }, DURATION);
    }

    @Override
    public void onBackPressed() {
    }
}
