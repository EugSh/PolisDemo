package com.example.polisdemo.gallery.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.polisdemo.R;
import com.example.polisdemo.gallery.model.dto.Photo;
import com.example.polisdemo.gallery.model.generator.CategoryGenerator;
import com.example.polisdemo.gallery.model.utils.PhotoInfoExtractor;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RecyclerViewCategoriesAdapter extends RecyclerView.Adapter<RecyclerViewCategoriesAdapter.RecyclerViewCategoriesHolder>{
    private final CategoryGenerator<Photo> categoryGenerator;
    private final PhotoInfoExtractor extractor;
    private final Context context;

    public RecyclerViewCategoriesAdapter(CategoryGenerator<Photo> categoryGenerator, PhotoInfoExtractor extractor, Context context) {
        this.categoryGenerator = categoryGenerator;
        this.extractor = extractor;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerViewCategoriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_items, parent, false);
        return new RecyclerViewCategoriesHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewCategoriesHolder holder, int position) {
//        CompletableFuture.runAsync(()->{
            final List<Photo> photos = categoryGenerator.getItems(categoryGenerator.getCategories().get(position));
            holder.recyclerView.setAdapter(new RecyclerViewItemsAdapter(photos, extractor, context));
            holder.textView.setText(categoryGenerator.getCategories().get(position));
//        });
    }

    @Override
    public int getItemCount() {
        return categoryGenerator.getCategories().size();
    }


    public class RecyclerViewCategoriesHolder extends RecyclerView.ViewHolder {
        private int gridItemCount = 2;
        private RecyclerView recyclerView;
        private TextView textView;
        public RecyclerViewCategoriesHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_view_items);
            textView = itemView.findViewById(R.id.recycler_view_category_name);
            FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(itemView.getContext());
            flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
            flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
            final RecyclerView.LayoutManager layoutManager = new GridLayoutManager(itemView.getContext(),
                    gridItemCount,
                    GridLayoutManager.HORIZONTAL,
                    false);
            recyclerView.setLayoutManager(flexboxLayoutManager);
        }
    }
}
