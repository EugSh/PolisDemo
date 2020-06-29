package com.example.polisdemo.gallery.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.polisdemo.R;

public class ImageFragment extends Fragment {
    private ImageView imageView;
    private Bitmap bitmap;

    public ImageFragment(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_activity, container, false);
        imageView = rootView.findViewById(R.id.image_activity_image);
        imageView.setImageBitmap(bitmap);
        return rootView;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
