package com.example.polisdemo.listener;

import android.view.View;

import com.example.polisdemo.utils.Utils;

import java.util.function.Consumer;

public class OnLabelClickListener implements View.OnClickListener {
    private final Consumer<String> fragmentChanger;

    public OnLabelClickListener(Consumer<String> fragmentChanger) {
        this.fragmentChanger = fragmentChanger;
    }

    @Override
    public void onClick(View v) {
        System.out.println("click label");
        String label = Utils.getViewTag(v, Utils.LABEL_TAG_KEY);
        fragmentChanger.accept(label);
    }
}
