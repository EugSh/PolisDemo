package com.example.polisdemo.gallery.ui.adapter;

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

import java.util.List;

public class RecyclerViewCategoriesAdapter extends RecyclerView.Adapter<RecyclerViewCategoriesAdapter.RecyclerViewCategoriesHolder>{
    private final CategoryGenerator<Photo> categoryGenerator;
    private final PhotoInfoExtractor extractor;

    public RecyclerViewCategoriesAdapter(CategoryGenerator<Photo> categoryGenerator, PhotoInfoExtractor extractor) {
        this.categoryGenerator = categoryGenerator;
        this.extractor = extractor;
    }


    @NonNull
    @Override
    public RecyclerViewCategoriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_items, parent, false);
        RecyclerViewCategoriesHolder r = new RecyclerViewCategoriesHolder(v);
        return new RecyclerViewCategoriesHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewCategoriesHolder holder, int position) {
        final List<Photo> photos = categoryGenerator.getItems(categoryGenerator.getCategories().get(position));
        holder.recyclerView.setAdapter(new RecyclerViewItemsAdapter(photos, extractor));
        holder.textView.setText(categoryGenerator.getCategories().get(position));
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
            final RecyclerView.LayoutManager layoutManager = new GridLayoutManager(itemView.getContext(),
                    gridItemCount,
                    GridLayoutManager.HORIZONTAL,
                    false);
            recyclerView.setLayoutManager(layoutManager);
        }
    }
}
