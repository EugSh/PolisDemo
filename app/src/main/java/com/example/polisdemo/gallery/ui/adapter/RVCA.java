package com.example.polisdemo.gallery.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polisdemo.MainActivity;
import com.example.polisdemo.R;
import com.example.polisdemo.gallery.model.dto.Photo;
import com.example.polisdemo.gallery.model.generator.CategoryGenerator;
import com.example.polisdemo.gallery.model.utils.PhotoInfoExtractor;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

public class RVCA extends RecyclerView.Adapter<RVCA.RVCAHolder> {
    private final CategoryGenerator<Photo> categoryGenerator;
    private final PhotoInfoExtractor extractor;
    private final Context context;

    public RVCA(CategoryGenerator<Photo> categoryGenerator, PhotoInfoExtractor extractor, Context context) {
        this.categoryGenerator = categoryGenerator;
        this.extractor = extractor;
        this.context = context;
    }

    @NonNull
    @Override
    public RVCAHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rvca, parent, false);
        return new RVCAHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RVCAHolder holder, int position) {
        final List<Photo> photos = categoryGenerator.getItems(categoryGenerator.getCategories().get(position));
//        holder.recyclerView.setAdapter(new RecyclerViewItemsAdapter(photos, extractor, context));
        holder.flexboxLayout.removeAllViews();
        photos.forEach(e -> {
            FrameLayout frameLayout = new FrameLayout(context);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) MainActivity.pxFromDp(context, 62),
                    (int) MainActivity.pxFromDp(context, 62));
            layoutParams.setMargins((int) MainActivity.pxFromDp(context, 2),
                    (int) MainActivity.pxFromDp(context, 2),
                    (int) MainActivity.pxFromDp(context, 2),
                    (int) MainActivity.pxFromDp(context, 2));
            frameLayout.setLayoutParams(layoutParams);
            frameLayout.setPadding((int) MainActivity.pxFromDp(context, 2),
                    (int) MainActivity.pxFromDp(context, 2),
                    (int) MainActivity.pxFromDp(context, 2),
                    (int) MainActivity.pxFromDp(context, 2));
            ImageView image = new ImageView(context);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image.setImageBitmap(extractor.extractThumbnail(e.getContentUri()));
            border(frameLayout, e.isSelected());
            frameLayout.addView(image);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (e.isSelected()) {
                        MainActivity.selectedImages.remove(e.getContentUri());
                        e.setSelected(false);
                    } else {
                        MainActivity.selectedImages.add(e.getContentUri());
                        e.setSelected(true);
                    }
                    border(frameLayout, e.isSelected());
                }
            });
            holder.flexboxLayout.addView(frameLayout);
        });
        holder.textView.setText(categoryGenerator.getCategories().get(position));
    }

    private void border(View view, boolean isSelected) {
        if (isSelected) {
            System.out.println("TRUE");
            view.setBackground(ContextCompat.getDrawable(context, R.drawable.imageview_selected));
        } else {
            System.out.println("FALSE");
            view.setBackground(ContextCompat.getDrawable(context, R.drawable.imageview_border));
        }
    }

    @Override
    public int getItemCount() {
        return categoryGenerator.getCategories().size();
    }

    class RVCAHolder extends RecyclerView.ViewHolder {
        private FlexboxLayout flexboxLayout;
        private TextView textView;

        public RVCAHolder(@NonNull View itemView) {
            super(itemView);
            flexboxLayout = itemView.findViewById(R.id.rvca_flexbox);
            textView = itemView.findViewById(R.id.rvca_recycler_view_category_name);
        }
    }
}
