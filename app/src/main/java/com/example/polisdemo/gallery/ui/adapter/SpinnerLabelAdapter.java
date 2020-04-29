package com.example.polisdemo.gallery.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.polisdemo.R;
import com.example.polisdemo.gallery.model.dto.SpinnerLabelState;

import java.util.List;

public class SpinnerLabelAdapter extends ArrayAdapter<SpinnerLabelState> {
    private final Context context;
    private final List<SpinnerLabelState> labels;

    public SpinnerLabelAdapter(@NonNull Context context, int resource, @NonNull List<SpinnerLabelState> objects) {
        super(context, resource, objects);
        this.context = context;
        this.labels = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(context);
            convertView = layoutInflator.inflate(R.layout.spinner_label_item, null);
            holder = new ViewHolder();
            holder.mTextView = convertView.findViewById(R.id.text_label);
            holder.mCheckBox = convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(labels.get(position).getTitle());

        holder.mCheckBox.setChecked(labels.get(position).isSelected());

        if (position == 0) {
            holder.mCheckBox.setVisibility(View.INVISIBLE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            labels.get(position).setSelected(isChecked);
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }
}
