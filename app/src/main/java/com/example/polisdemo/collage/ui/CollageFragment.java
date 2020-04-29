package com.example.polisdemo.collage.ui;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polisdemo.MainActivity;
import com.example.polisdemo.R;
import com.example.polisdemo.collage.template.CollageTemplate;
import com.example.polisdemo.collage.template.Template;
import com.example.polisdemo.collage.ui.adapter.RecyclerViewTemplateAdapter;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class CollageFragment extends Fragment {

    private static final String TAG = "RecyclerViewCollageFragment";

    private RecyclerView recyclerViewTemplates;
    private RelativeLayout collageView;
    private Context context;
    private CollageTemplate template;
    private List<Bitmap> bitmaps;
    private Button btnSave;
    private Random random = new Random();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        final View rootView = inflater.inflate(R.layout.fragment_collage, container, false);
        rootView.setTag(TAG);
        context = container.getContext();
        recyclerViewTemplates = rootView.findViewById(R.id.recycler_collage_template);
        collageView = rootView.findViewById(R.id.collage_view);
        rootView.post(() -> initCollage());
        template = new CollageTemplate(context);
        RecyclerViewTemplateAdapter adapter = new RecyclerViewTemplateAdapter(
                template.getTemplateIcons(MainActivity.selectedImages.size()),
                clickListener);
        recyclerViewTemplates.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewTemplates.setAdapter(adapter);
        btnSave = rootView.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = createBitmapFromView(collageView, collageView.getWidth(), collageView.getHeight());
                try {
                    saveImage(bitmap, "saved_collage_image_" + random.nextInt());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return rootView;
    }

    private void initRecyclerView() {

    }

    private void initCollage() {
        template.setParentWidth(collageView.getWidth());
        template.setParentHeight(collageView.getHeight());
        bitmaps = getBitmaps(MainActivity.selectedImages);
        displayImages(0);
    }

    private void displayImages(int templateId) {
        final List<Template> params = template.getTemplates(MainActivity.selectedImages.size()).get();
        collageView.removeAllViews();
        if (bitmaps.isEmpty()) {
            return;
        }
        for (View v : params.get(templateId).getTemplates(context, bitmaps)) {
            collageView.addView(v);
        }
    }

    private List<Bitmap> getBitmaps(final Collection<String> uris) {
        final List<Bitmap> result = new ArrayList<>(uris.size());
        final ContentResolver resolver = context.getContentResolver();
        try {
            for (final String uri : uris) {
                final ParcelFileDescriptor pfd = resolver.openFileDescriptor(Uri.parse(uri), "r");
                final FileDescriptor fileDescriptor = pfd.getFileDescriptor();
                result.add(BitmapFactory.decodeFileDescriptor(fileDescriptor));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    private final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            displayImages(Integer.parseInt(String.valueOf(v.getTag())));
        }
    };

    public @NonNull
    static Bitmap createBitmapFromView(@NonNull View view, int width, int height) {
        if (width > 0 && height > 0) {
            view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
        }
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        final Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        final Drawable background = view.getBackground();

        if (background != null) {
            background.draw(canvas);
        }
        view.draw(canvas);

        return bitmap;
    }

    private void saveImage(Bitmap bitmap, @NonNull String name) throws IOException {
        OutputStream fos;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name + ".jpeg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            File image = new File(imagesDir, name + ".jpeg");
            fos = new FileOutputStream(image);
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        Objects.requireNonNull(fos).close();
    }
}
