package com.example.polisdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.polisdemo.utils.ExtractorUtils;

public class ImageActivity extends AppCompatActivity {
    public static final String EXTRA_URI_KEY = "URI_KEY";
    private boolean readPermission;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity);
        final ImageView imageView = findViewById(R.id.image_activity_image);
        final String uri = getIntent().getStringExtra(EXTRA_URI_KEY);
        readPermission = getIntent().getBooleanExtra(LogoActivity.READ_PERMISSION_EXTRA, false);
        Bitmap bitmap = ExtractorUtils.extractRotatedBitmap(this, uri, ExtractorUtils.getRotation(this, uri));
        imageView.setImageBitmap(bitmap);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(LogoActivity.READ_PERMISSION_EXTRA, readPermission);
        startActivity(intent);
        finish();
    }
}
