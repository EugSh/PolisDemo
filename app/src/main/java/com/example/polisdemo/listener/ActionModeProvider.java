package com.example.polisdemo.listener;

import android.view.ActionMode;

import java.util.Optional;

public interface ActionModeProvider {
    Optional<ActionMode> get();
}
