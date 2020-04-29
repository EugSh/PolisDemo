package com.example.polisdemo.collage.ui.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polisdemo.R;

import java.util.List;

public class RecyclerViewTemplateAdapter extends RecyclerView.Adapter<RecyclerViewTemplateAdapter.TemplateHolder> {
    private final List<Bitmap> templates;
    private final View.OnClickListener clickListener;

    public RecyclerViewTemplateAdapter(List<Bitmap> templates, View.OnClickListener clickListener) {
        this.templates = templates;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public TemplateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_collage_template_item, parent, false);
        return new TemplateHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateHolder holder, int position) {
        holder.setBitmap(templates.get(position));
        holder.imageView.setTag(String.valueOf(position));
        holder.imageView.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return templates.size();
    }

    public class TemplateHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        public TemplateHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.image_collage_template);
        }

        public void setBitmap(final Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
