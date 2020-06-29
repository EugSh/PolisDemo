package com.example.polisdemo.collage.ui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polisdemo.MainActivity;
import com.example.polisdemo.R;
import com.example.polisdemo.collage.dto.BitmapCard;
import com.example.polisdemo.collage.template.TemplateCollage;
import com.example.polisdemo.collage.ui.adapter.ThumbnailAdapter;
import com.example.polisdemo.collage.ui.adapter.ThumbnailCallback;
import com.example.polisdemo.collage.ui.adapter.ThumbnailItem;
import com.example.polisdemo.collage.ui.adapter.ThumbnailsManager;
import com.example.polisdemo.collage.view.CollageView;
import com.example.polisdemo.utils.ExtractorUtils;
import com.google.android.material.snackbar.Snackbar;
import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;

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
import java.util.Set;
import java.util.function.Supplier;

import petrov.kristiyan.colorpicker.ColorPicker;

public class CollageFragment extends Fragment {

    private static final String TAG = "RecyclerViewCollageFragment";
    private static final int PERMISSION_REQUEST_CODE_WRITE = 3;
    private final AppCompatActivity activity;
    private final Supplier<Set<String>> selectedImageUris;

    private RecyclerView recyclerViewFilters;
    private Context context;
    private List<BitmapCard> bitmaps;
    private CollageView collageView;
    private Button btnSave;
    private Button btnBackgroundColor;
    private Random random = new Random();
    private ImageView selectedImage;
    private Bitmap selectedBitmap;
    private View mLayout;

    public CollageFragment(AppCompatActivity activity, Supplier<Set<String>> selectedImageUris) {
        this.activity = activity;
        this.selectedImageUris = selectedImageUris;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_collage, container, false);
        rootView.setTag(TAG);
        mLayout = container;
        context = container.getContext();
        recyclerViewFilters = rootView.findViewById(R.id.recycler_filters);
        collageView = rootView.findViewById(R.id.collage_view);

        collageView = rootView.findViewById(R.id.collage_view);
        btnSave = rootView.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(v -> {
            if (checkReadPerm()) {
                save();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE_WRITE);
            }
        });
        Handler handler = new Handler();
        handler.post(() -> {
            initCollage();
            initFilters();
        });
        btnBackgroundColor = rootView.findViewById(R.id.btn_background_color);
        btnBackgroundColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPicker picker = new ColorPicker(activity);
                picker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
                    @Override
                    public void setOnFastChooseColorListener(int position, int color) {
                        collageView.setBackgroundColor(color);
                    }

                    @Override
                    public void onCancel() {
                        // put code
                    }
                })
                        .setDefaultColorButton(Color.parseColor("#FFE673"))
                        .setColumns(5)
                        .show();
            }
        });
        return rootView;
    }

    private void save() {
        Bitmap bitmap = createBitmapFromView(collageView, collageView.getWidth(), collageView.getHeight());
        try {
            saveImage(bitmap, "saved_collage_image_" + random.nextInt());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initFilters() {
        final List<ThumbnailItem> thumbnailItems = new ArrayList<>();
        Bitmap thumbImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.caption), 640, 640, false);
        thumbnailItems.add(new ThumbnailItem(thumbImage, new Filter()));
        thumbnailItems.add(new ThumbnailItem(thumbImage, SampleFilters.getStarLitFilter()));
        thumbnailItems.add(new ThumbnailItem(thumbImage, SampleFilters.getBlueMessFilter()));
        thumbnailItems.add(new ThumbnailItem(thumbImage, SampleFilters.getLimeStutterFilter()));
        thumbnailItems.add(new ThumbnailItem(thumbImage, SampleFilters.getNightWhisperFilter()));
        thumbnailItems.add(new ThumbnailItem(thumbImage, SampleFilters.getAweStruckVibeFilter()));
        ThumbnailsManager.clearThumbs();
        thumbnailItems.forEach(t -> ThumbnailsManager.addThumb(t));
        ThumbnailAdapter adapter = new ThumbnailAdapter(ThumbnailsManager.processThumbs(context), new ThumbnailCallback() {
            @Override
            public void onThumbnailClick(Filter filter) {
                final Bitmap bitmap = filter.processFilter(selectedBitmap.copy(Bitmap.Config.ARGB_8888, true));
                selectedImage.setImageBitmap(bitmap);

            }
        });
        recyclerViewFilters.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        recyclerViewFilters.setAdapter(adapter);
    }

    private void initCollage() {
        collageView.setOnDoubleTorchListener((v, event) -> {
            v.performClick();
            collageView.removeView(v);
            collageView.addView(v);
            return true;
        });
        collageView.setSingleTouchClickListener((v, event) -> {
            v.performClick();
            selectedImage = (ImageView) v;
            selectedBitmap = ((BitmapDrawable) selectedImage.getDrawable()).getBitmap();
            recyclerViewFilters.getAdapter().notifyDataSetChanged();
            collageView.removeView(v);
            collageView.addView(v);
            return true;
        });
        bitmaps = getBitmaps(selectedImageUris.get());
        bitmaps.forEach((b) -> collageView.addCard(rotateIfNeed(b.getBitmap(), b.getOrientation())));
        TemplateCollage collage = new TemplateCollage(collageView.getWidth(), collageView.getHeight());
        collage.process(collageView.getListCards());

        selectedBitmap = bitmaps.isEmpty() ? null : bitmaps.get(0).getBitmap();
        selectedImage = collageView.getChildCount() == 0 ? null : (ImageView) collageView.getChildAt(0);
        displayImages(0);
    }

    private Bitmap rotateIfNeed(final Bitmap b, final int orientation) {
        if (orientation == 0) {
            Bitmap.Config config = b.getConfig();
            return b.copy(config, true);
        }
        final Matrix matrix = new Matrix();
        matrix.preRotate(orientation);
        return Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
    }

    private void displayImages(int templateId) {
    }

    private List<BitmapCard> getBitmaps(final Collection<String> uris) {
        final List<BitmapCard> result = new ArrayList<>(uris.size());
        final ContentResolver resolver = context.getContentResolver();
        try {
            for (final String uri : uris) {
                final ParcelFileDescriptor pfd = resolver.openFileDescriptor(Uri.parse(uri), "r");
                final FileDescriptor fileDescriptor = pfd.getFileDescriptor();
                final ExifInterface exifInterface = new ExifInterface(resolver
                        .openFileDescriptor(Uri.parse(uri), "r")
                        .getFileDescriptor());
                Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                result.add(new BitmapCard(Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, false),
                        exifToDegrees(exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL))));

//                result.add(new BitmapCard(ExtractorUtils.extractThumbnail(context, uri),
//                        exifToDegrees(exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL))));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
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

    private boolean checkReadPerm() {
        final int readPerm = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return readPerm == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE_WRITE) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                save();
            } else {
                Snackbar.make(mLayout, "Не возможно сохранить, так как недостаточно прав.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
