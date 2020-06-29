package com.example.polisdemo.listener;

import android.view.View;

import com.example.polisdemo.utils.Utils;

import java.util.Set;

public class OnImageSelectListener implements OnSelectListener {
    private final ActionModeProvider actionModeProvider;
    private final Set<String> selectedImageUris;
    private final String actionModeTitleTemplate;

    public OnImageSelectListener(ActionModeProvider actionModeProvider, Set<String> selectedImageUris, String actionModeTitleTemplate) {
        this.actionModeProvider = actionModeProvider;
        this.selectedImageUris = selectedImageUris;
        this.actionModeTitleTemplate = actionModeTitleTemplate;
    }

    @Override
    public void onSelect(View view, boolean isSelect) {
        if (isSelect) {
            selectedImageUris.add(Utils.getViewTag(view, Utils.URI_TAG_KEY));
        } else {
            selectedImageUris.remove(Utils.getViewTag(view, Utils.URI_TAG_KEY));
        }
        actionModeProvider.get().ifPresent(actionMode -> actionMode.setTitle(String.format(actionModeTitleTemplate, selectedImageUris.size())));
    }

    @Override
    public Set<String> getSelectedImageUris() {
        return selectedImageUris;
    }

}
