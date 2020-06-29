package com.example.polisdemo.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;

import com.example.polisdemo.ImageActivity;
import com.example.polisdemo.utils.ExtractorUtils;
import com.example.polisdemo.utils.Utils;

import java.util.function.Consumer;

public class OnImageClickListener implements View.OnClickListener {
    private final Context context;
    private final Consumer<Bitmap> changeFragment;


    public OnImageClickListener(Context context, Consumer<Bitmap> changeFragment) {
        this.context = context;
        this.changeFragment = changeFragment;
    }

    @Override
    public void onClick(View v) {
        System.out.println("image click");
        final String uri = Utils.getViewTag(v, Utils.URI_TAG_KEY);
        final Bitmap bitmap = ExtractorUtils.extractRotatedBitmap(context, uri, ExtractorUtils.getRotation(context, uri));
        changeFragment.accept(bitmap);
//
//
//        Intent intent = new Intent(activity, ImageActivity.class);
//        intent.putExtra(ImageActivity.EXTRA_URI_KEY, );
//        activity.startActivity(intent);
//        activity.finish();
    }
}
