package com.example.polisdemo.gallery.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polisdemo.MainActivity;
import com.example.polisdemo.R;
import com.example.polisdemo.gallery.model.dto.Geolocation;
import com.example.polisdemo.gallery.model.dto.Photo;
import com.example.polisdemo.gallery.model.utils.PhotoInfoExtractor;
import com.google.maps.errors.ApiException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RecyclerViewItemsAdapter extends RecyclerView.Adapter<RecyclerViewItemsAdapter.RecyclerViewItemsHolder> {
    private final List<Photo> photos;
    private final PhotoInfoExtractor extractor;
    private final Context context;
    private final Paint p = new Paint();

    public RecyclerViewItemsAdapter(List<Photo> photos, PhotoInfoExtractor extractor, Context context) {
        this.photos = photos;
        this.extractor = extractor;
        this.context = context;
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.GREEN);
    }


    @NonNull
    @Override
    public RecyclerViewItemsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new RecyclerViewItemsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewItemsHolder holder, int position) {
        CompletableFuture.runAsync(() -> {
            try {
                final Photo photo = photos.get(position);
                final Bitmap bitmap = extractBitmap(photo);
                MainActivity.runInUIThread(() -> {
                    holder.imageView.setImageBitmap(bitmap);
                    border(holder.itemView, photo.isSelected());
                    holder.imageView.setOnClickListener(v -> {
                        if (photo.isSelected()) {
                            MainActivity.selectedImages.remove(photo.getContentUri());
                            photo.setSelected(false);
                            ((ImageView) v).setImageBitmap(extractBitmap(photo));
                        } else {
                            MainActivity.selectedImages.add(photo.getContentUri());
                            Geolocation g = extractor.extractGeolocation(photo.getContentUri());

                                long t = System.currentTimeMillis();
//                                System.out.println("<><><><><><><><><><>");
//                            System.out.println(g.getLocationName());
//                                System.out.println("time - " + (System.currentTimeMillis() - t));
//                                System.out.println("<><><><><><><><><><>");

                            photo.setSelected(true);
                            ((ImageView) v).setImageBitmap(extractBitmap(photo));
                        }
                        border(holder.itemView, photo.isSelected());
                    });
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void border(View view, boolean isSelected) {
        if (isSelected) {
            view.setBackground(ContextCompat.getDrawable(context, R.drawable.imageview_selected));
        } else {
            view.setBackground(ContextCompat.getDrawable(context, R.drawable.imageview_border));
        }
    }

    private Bitmap extractBitmap(Photo photo) {
        final Bitmap bitmap = photo.getOrientation() != 0 ?
                extractor.extractRotatedThumbnail(photo.getContentUri(), photo.getOrientation()) :
                extractor.extractThumbnail(photo.getContentUri());
        if (MainActivity.selectedImages.contains(photo.getContentUri())) {
            photo.setSelected(true);
        }
        return bitmap;
//        if (MainActivity.selectedImages.contains(photo.getContentUri())) {
//            photo.setSelected(true);
//        }
//        if (!photo.isSelected()) {
//            return bitmap;
//        }
//        final Canvas canvas = new Canvas(bitmap);
//        p.setStrokeWidth(Math.min(bitmap.getWidth(), bitmap.getHeight()) / 10);
//        canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), p);
//        return bitmap;
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public static class RecyclerViewItemsHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private boolean isSelected = false;

        public RecyclerViewItemsHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image_id);
        }
    }
}
