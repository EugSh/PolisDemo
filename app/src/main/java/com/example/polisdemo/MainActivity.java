package com.example.polisdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.polisdemo.collage.ui.CollageFragment;
import com.example.polisdemo.gallery.model.db.FireBaseDBService;
import com.example.polisdemo.gallery.model.db.FireBaseDBServiceImpl;
import com.example.polisdemo.gallery.model.db.FireBaseSQLHelper;
import com.example.polisdemo.gallery.model.db.LabelEntity;
import com.example.polisdemo.gallery.model.firebase.FireBaseLabeler;
import com.example.polisdemo.gallery.model.firebase.LabelTask;
import com.example.polisdemo.gallery.ui.ImageFragment;
import com.example.polisdemo.gallery.ui.RecyclerViewCategoriesFragment;
import com.example.polisdemo.gallery.ui.RecyclerViewLabelFragment;
import com.example.polisdemo.listener.ActionModeProvider;
import com.example.polisdemo.listener.OnImageClickListener;
import com.example.polisdemo.listener.OnImageLongClickListener;
import com.example.polisdemo.listener.OnImageSelectListener;
import com.example.polisdemo.listener.OnLabelClickListener;
import com.example.polisdemo.listener.OnSelectListener;
import com.example.polisdemo.translator.Translator;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.common.collect.Lists;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    static {
        System.loadLibrary("NativeImageProcessor");
    }

    private static final String TAG_GALLERY = "TAG_GALLERY";
    private static final String TAG_COLLAGE = "TAG_COLLAGE";
    private static final String TAG_MAGIC_GALLERY = "TAG_MAGIC_GALLERY";
    private static final String TAG_IMAGE_FRAGMENT = "TAG_IMAGE_FRAGMENT";
    public static Geocoder GEO_CODER;
    private RecyclerViewCategoriesFragment gallery;
    private RecyclerViewLabelFragment magicGallery;
    private CollageFragment collage;
    private FragmentManager manager;
    private ImageFragment imageFragment;
    private MaterialToolbar toolbar;

    private View.OnClickListener clickListener;
    private View.OnLongClickListener longClickListener;
    private View.OnClickListener onLabelClickListener;
    private OnSelectListener selectListener;
    private ActionModeProvider actionModeProvider;
    private final Set<String> selectedImageUris = new HashSet<>();
    private boolean isMagicGallery = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = getSupportFragmentManager();

        toolbar = findViewById(R.id.topAppBar);
        clickListener = new OnImageClickListener(this, this::replaceOnImage);
        OnImageLongClickListener imageLongClickListener = new OnImageLongClickListener(this, (v) -> gallery.setIsSelectedMode(v), ()->{
            System.out.println("CLEAR");
            selectedImageUris.clear();
            System.out.println(selectedImageUris.size());
        }, this::replaceOnCollage);
        longClickListener = imageLongClickListener;
        actionModeProvider = imageLongClickListener;
        selectListener = new OnImageSelectListener(actionModeProvider, selectedImageUris, getString(R.string.title_action_mode) + " %d");
        onLabelClickListener = new OnLabelClickListener(s -> replaceOnGallery(Lists.newArrayList(s)));
        toolbar.setOnMenuItemClickListener(new MenuClickListener(toolbar));
        final FireBaseSQLHelper dbHelper = new FireBaseSQLHelper(this, 1);
        final FireBaseDBService dbService = new FireBaseDBServiceImpl(dbHelper);
        final Intent intent = getIntent();
        final boolean readPermissionEnable = intent.getBooleanExtra(LogoActivity.READ_PERMISSION_EXTRA, false);
        gallery = new RecyclerViewCategoriesFragment(dbService, readPermissionEnable, clickListener, longClickListener, selectListener);
        magicGallery = new RecyclerViewLabelFragment(dbService, readPermissionEnable, onLabelClickListener);
        collage = new CollageFragment(this, () -> selectListener.getSelectedImageUris());
        imageFragment = new ImageFragment(null);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.main_content_fragment, gallery, TAG_GALLERY);
        transaction.commit();
//        appCompatActivity = this;

        if (readPermissionEnable) {
            final Translator translator = new Translator();
            final Date newestDate = dbService
                    .getNewestEntry()
                    .orElseGet(() -> new LabelEntity("", "", new Date(0), new Date(0), 0f, 0))
                    .getDate();
            final FireBaseLabeler labeler = new FireBaseLabeler(getContentResolver(), newestDate, 0.8f, translator);
            final LabelTask task = new LabelTask(labeler, dbService, 10, new Runnable() {
                @Override
                public void run() {
                    if (isMagicGallery) {
                        System.out.println("Notify");
                        magicGallery.notifyDataChange();
                    } else {
                        System.out.println("Not notify");
                    }
                }
            });
            task.execute();
        }
        System.out.println("Table size " + dbService.size());

//        GEO_CODER = new Geocoder(this, Locale.getDefault());
//        long t = System.currentTimeMillis();
//        final FireBaseSQLHelper dbHelper = new FireBaseSQLHelper(this, 10);
//        dbService = new FireBaseDBServiceImpl(dbHelper);
////        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_gallery, R.id.navigation_collage)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);
//        StrictMode.ThreadPolicy policy = new
//                StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//        Handler handler = new Handler();
//        handler.post(MainActivity::initTranslation);
    }

    private class MenuClickListener implements MenuItem.OnMenuItemClickListener, Toolbar.OnMenuItemClickListener {
        private final MaterialToolbar toolbar;

        private MenuClickListener(MaterialToolbar toolbar) {
            this.toolbar = toolbar;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.app_bar_item_gallery:
                    replaceOnGallery(new ArrayList<>());
                    isMagicGallery = false;
                    return true;
                case R.id.app_bar_item_magic_gallery:
                    replaceOnMagicGallery();
                    isMagicGallery = true;
                    return true;
                default:
                    return false;
            }

        }
    }

    private void replaceOnGallery(List<String> labels) {
        final RecyclerViewCategoriesFragment fragment = (RecyclerViewCategoriesFragment) manager.findFragmentByTag(TAG_GALLERY);
        gallery.setLabels(labels);
        if (fragment == null) {
            final FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_content_fragment, gallery, TAG_GALLERY);
//            transaction.addToBackStack(null);
            transaction.commit();
            gallery.notifyDataChanged();
        } else if (gallery.isWasChanges()) {
            gallery.notifyDataChanged();
        }

        toolbar.setTitle(getResources().getString(R.string.title_gallery));
    }

    private void replaceOnImage(Bitmap bitmap) {
        final ImageFragment fragment = (ImageFragment) manager.findFragmentByTag(TAG_IMAGE_FRAGMENT);
        if (fragment == null) {
            final FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_content_fragment, imageFragment, TAG_IMAGE_FRAGMENT);
            transaction.addToBackStack(null);
            transaction.commit();
            imageFragment.setBitmap(bitmap);
        }
        toolbar.setTitle(getResources().getString(R.string.title_image_fragment));
    }

    private void replaceOnMagicGallery() {
        final RecyclerViewLabelFragment fragment = (RecyclerViewLabelFragment) manager.findFragmentByTag(TAG_MAGIC_GALLERY);
        if (fragment == null) {
            final FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_content_fragment, magicGallery, TAG_MAGIC_GALLERY);
//            transaction.addToBackStack(null);
            transaction.commit();
        }
        toolbar.setTitle(getResources().getString(R.string.title_magic_gallery));
    }

    private void replaceOnCollage() {
        final CollageFragment fragment = (CollageFragment) manager.findFragmentByTag(TAG_COLLAGE);
        if (fragment == null) {
            final FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_content_fragment, collage, TAG_COLLAGE);
//            transaction.addToBackStack(null);
            transaction.commit();
        }

        toolbar.setTitle(getResources().getString(R.string.title_collage));
    }

//    public static void runInUIThread(Runnable runnable) {
//        if (appCompatActivity == null) {
//            return;
//        }
//        appCompatActivity.runOnUiThread(runnable);
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        if (requestCode == PERMISSION_REQUEST_CODE_INTERNET) {
//            if (grantResults.length == 1
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                initTranslation();
//
//            } else {
//            }
//        }
//    }

    //    private void initSpinnerSortType(final View rootView) {
//        spinnerSortTypes = rootView.findViewById(R.id.spinner_sort_type);
//        spinnerSortTypes.setAdapter(new ArrayAdapter<String>(context,
//                R.layout.spinner_sort_types_item,
//                R.id.spinner_sort_type_item,
//                getResources().getStringArray(R.array.types_sort_name)));
//        spinnerSortTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                changeAdapter((String) parent.getItemAtPosition(position));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//    }


}
