package com.example.polisdemo.gallery.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polisdemo.R;
import com.example.polisdemo.gallery.model.dto.Photo;
import com.example.polisdemo.gallery.model.generator.CategoryGenerator;
import com.example.polisdemo.gallery.model.utils.PhotoInfoExtractor;
import com.example.polisdemo.listener.OnSelectListener;
import com.example.polisdemo.utils.Utils;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

public class RecyclerViewCategoryAdapter extends RecyclerView.Adapter<RecyclerViewCategoryAdapter.RVCAHolder> {
    private final CategoryGenerator<Photo> categoryGenerator;
    private final PhotoInfoExtractor extractor;
    private final Context context;
    private final int margin;
    private final View.OnClickListener clickListener;
    private final View.OnLongClickListener longClickListener;
    private final OnSelectListener selectListener;
    private boolean isSelectMode = false;

    public RecyclerViewCategoryAdapter(CategoryGenerator<Photo> categoryGenerator,
                                       PhotoInfoExtractor extractor,
                                       Context context,
                                       View.OnClickListener clickListener,
                                       View.OnLongClickListener longClickListener,
                                       OnSelectListener selectListener) {
        this.categoryGenerator = categoryGenerator;
        this.extractor = extractor;
        this.context = context;
        this.margin = Utils.getImageMargin(context);
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
        this.selectListener = selectListener;
    }

    @NonNull
    @Override
    public RVCAHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_category, parent, false);
        return new RVCAHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RVCAHolder holder, int position) {
        final List<Photo> photos = categoryGenerator.getItems(categoryGenerator.getCategories().get(position));
        holder.flexboxLayout.removeAllViews();
        photos.forEach(e -> {
            View frameLayout = createImageItem(e);
            holder.flexboxLayout.addView(frameLayout);
        });
        holder.textView.setText(categoryGenerator.getCategories().get(position));
    }

    @Override
    public int getItemCount() {
        return categoryGenerator.getCategories().size();
    }

    class RVCAHolder extends RecyclerView.ViewHolder {
        private final FlexboxLayout flexboxLayout;
        private final TextView textView;

        public RVCAHolder(@NonNull View itemView) {
            super(itemView);
            flexboxLayout = itemView.findViewById(R.id.rvca_flexbox);
            textView = itemView.findViewById(R.id.rvca_recycler_view_category_name);
        }
    }

    private View createImageItem(Photo photo) {
        final View fr = LayoutInflater.from(context).inflate(R.layout.recycler_view_category_item, null);
        final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(Utils.getImageWidth(context), Utils.getImageHeight(context));
        layoutParams.setMargins(margin,
                margin,
                margin,
                margin);
        fr.setLayoutParams(layoutParams);
        final ImageView image = fr.findViewById(R.id.item_image);
        image.setImageBitmap(extractor.extractThumbnail(photo.getContentUri()));
        final CheckBox checkBox = fr.findViewById(R.id.item_checkbox);
        Utils.setViewTag(image, Utils.URI_TAG_KEY, photo.getContentUri());
        if (isSelectMode) {
            checkBox.setChecked(photo.isSelected());
            image.setOnClickListener(v -> {
                photo.setSelected(!photo.isSelected());
                checkBox.setChecked(photo.isSelected());
                selectListener.onSelect(v, photo.isSelected());
            });
            checkBox.setOnClickListener(v -> image.callOnClick());
        } else {
            image.setOnLongClickListener(v -> {
                photo.setSelected(true);
                final boolean result = longClickListener.onLongClick(v);
                selectListener.onSelect(v, true);
                return result;
            });
            image.setOnClickListener(clickListener);
            checkBox.setVisibility(View.GONE);
            photo.setSelected(false);
        }
        return fr;
    }


    public void setIsSelectMode(boolean isSelectMode) {
        this.isSelectMode = isSelectMode;
    }
}
