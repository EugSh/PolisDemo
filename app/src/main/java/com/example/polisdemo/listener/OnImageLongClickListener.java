package com.example.polisdemo.listener;

import android.app.Activity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.polisdemo.R;

import java.util.Optional;
import java.util.function.Consumer;

public class OnImageLongClickListener implements View.OnLongClickListener, ActionModeProvider {
    private final Activity activity;
    private ActionMode actionMode;
    private final Consumer<Boolean> changeIsSelectMode;
    private final Runnable refreshBeforeCreate;
    private final Runnable changeCollageFragment;

    public OnImageLongClickListener(Activity activity, Consumer<Boolean> changeSelectMode, Runnable refreshBefore, Runnable changeCollageFragment) {
        this.activity = activity;
        this.changeIsSelectMode = changeSelectMode;
        this.refreshBeforeCreate = refreshBefore;
        this.changeCollageFragment = changeCollageFragment;
    }

    @Override
    public boolean onLongClick(View v) {
        actionMode = activity.startActionMode(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.action_mode_bar, menu);
                changeIsSelectMode.accept(true);
                refreshBeforeCreate.run();
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.app_bar_collage) {
                    changeCollageFragment.run();
                    actionMode.finish();
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                actionMode = null;
                changeIsSelectMode.accept(false);

            }
        });
        actionMode.setTitle(activity.getResources().getString(R.string.title_action_mode) + " 1");
        return true;
    }

    @Override
    public Optional<ActionMode> get() {
        return Optional.of(actionMode);
    }
}
