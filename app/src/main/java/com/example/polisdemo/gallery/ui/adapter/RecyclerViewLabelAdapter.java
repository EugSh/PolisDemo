package com.example.polisdemo.gallery.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polisdemo.R;
import com.example.polisdemo.gallery.model.db.FireBaseDBService;
import com.example.polisdemo.gallery.model.db.LightLabelEntityV2;
import com.example.polisdemo.gallery.model.utils.PhotoInfoExtractor;
import com.example.polisdemo.utils.Utils;

import java.util.List;

public class RecyclerViewLabelAdapter extends RecyclerView.Adapter<RecyclerViewLabelAdapter.RecyclerViewLabelHolder> {
    private final FireBaseDBService dbService;
    private List<LightLabelEntityV2> lightLabelEntityV2s;
    private final PhotoInfoExtractor extractor;
    private final View.OnClickListener onLabelClickListener;
    private final Context context;
    private final int margin;

    public RecyclerViewLabelAdapter(FireBaseDBService dbService, PhotoInfoExtractor extractor, View.OnClickListener onLabelClickListener, Context context) {
        this.dbService = dbService;
        this.lightLabelEntityV2s = dbService.getOneEntitiesForEachLabel();
        this.extractor = extractor;
        this.onLabelClickListener = onLabelClickListener;
        this.context = context;
        this.margin = Utils.getImageMargin(context);
    }

    @NonNull
    @Override
    public RecyclerViewLabelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_label_item, parent, false);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(Utils.getImageWidth(context), Utils.getImageHeight(context));
        layoutParams.setMargins(margin,
                margin,
                margin,
                margin);
        v.setLayoutParams(layoutParams);
        return new RecyclerViewLabelHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewLabelHolder holder, int position) {
        final LightLabelEntityV2 entity = lightLabelEntityV2s.get(position);
        Bitmap m = extractor.extractThumbnail(entity.getContentUrl());
        holder.imageView.setImageBitmap(m);
        holder.textView.setText(entity.getLabel());
        holder.imageView.setOnClickListener(onLabelClickListener);
        Utils.setViewTag(holder.imageView, Utils.LABEL_TAG_KEY, entity.getLabel());
    }

    @Override
    public int getItemCount() {
        return lightLabelEntityV2s.size();
    }

    public static class RecyclerViewLabelHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView textView;

        public RecyclerViewLabelHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.label_preview);
            textView = itemView.findViewById(R.id.label_name);
        }
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
        lightLabelEntityV2s = dbService.getOneEntitiesForEachLabel();
    }


}
