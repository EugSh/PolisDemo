package com.example.polisdemo.listener;

import android.view.View;

import java.util.Collection;
import java.util.Set;

public interface OnSelectListener {

    void onSelect(View view, boolean isSelected);

    Set<String> getSelectedImageUris();
}
