package com.example.polisdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

public class LogoActivity extends AppCompatActivity {
    public static final String READ_PERMISSION_EXTRA = "readPermissionEnable";
    private static final int PERMISSION_REQUEST_CODE_READ = 1;
    private static int SPLASH_TIME = 3000;
    private View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        root = findViewById(R.id.layout_logo);

        if (checkReadPerm()) {
            handle(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE_READ);
        }
    }

    private void handle(boolean readPermissionEnable) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LogoActivity.this, MainActivity.class);
                intent.putExtra(READ_PERMISSION_EXTRA, readPermissionEnable);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE_READ) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                handle(true);
            } else {
                Snackbar.make(root, "gallery could not open, permission denied",
                        Snackbar.LENGTH_SHORT)
                        .show();
                handle(false);
            }
        }
    }

    private boolean checkReadPerm() {
        final int readPerm = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return readPerm == PackageManager.PERMISSION_GRANTED;
    }
}
