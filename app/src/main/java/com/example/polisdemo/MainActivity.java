package com.example.polisdemo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Window;

import com.example.polisdemo.gallery.model.db.FireBaseDBService;
import com.example.polisdemo.gallery.model.db.FireBaseDBServiceImpl;
import com.example.polisdemo.gallery.model.db.FireBaseSQLHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.google.gson.JsonObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import okhttp3.MediaType;

public class MainActivity extends AppCompatActivity {
    static {
        System.loadLibrary("NativeImageProcessor");
    }
    private static final int PERMISSION_REQUEST_CODE_INTERNET = 2;
    public static Geocoder GEO_CODER;
    public static Set<String> selectedImages = new HashSet<>();
    public static FireBaseDBService dbService;
    public static AppCompatActivity appCompatActivity;
    public static FirebaseTranslator translator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appCompatActivity = this;
        super.onCreate(savedInstanceState);
        GEO_CODER = new Geocoder(this, Locale.getDefault());
        long t = System.currentTimeMillis();
        final FireBaseSQLHelper dbHelper = new FireBaseSQLHelper(this, 10);
        dbService = new FireBaseDBServiceImpl(dbHelper);
//        System.out.println("время подключения к базе " + (System.currentTimeMillis() - t));
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_gallery, R.id.navigation_collage)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Handler handler = new Handler();
        handler.post(MainActivity::initTranslation);

    }

    public static void runInUIThread(Runnable runnable) {
        if (appCompatActivity == null) {
            return;
        }
        appCompatActivity.runOnUiThread(runnable);
    }

    private static void initTranslation() {
        FirebaseTranslatorOptions options =
                new FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(FirebaseTranslateLanguage.EN)
                        .setTargetLanguage(FirebaseTranslateLanguage.RU)
                        .build();
        translator =
                FirebaseNaturalLanguage.getInstance().getTranslator(options);
        final FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        v -> {
                            System.out.println("OK");
                            // Model downloaded successfully. Okay to start translating.
                            // (Set a flag, unhide the translation UI, etc.)
                        })
                .addOnFailureListener(
                        e -> {
                            e.printStackTrace();
                            System.out.println("Not ok");
                            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                            translator = null;
                            // Model couldn’t be downloaded or other internal error.
                            // ...
                        });
    }

    public static CompletableFuture<List<String>> translate(final List<String> list) {
        if (translator == null) {
            return CompletableFuture.supplyAsync(() -> list);
        }
        final CompletableFuture<List<String>> result = new CompletableFuture<>();
        translator.translate(list.stream().collect(Collectors.joining(", "))).addOnSuccessListener(s -> {
            result.complete(Arrays.asList(s.split(", ")));
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
        return result;
    }

    public static CompletableFuture<String> translate(final String text) {
        if (translator == null) {
            System.out.println("SYKA");
            return CompletableFuture.supplyAsync(() -> text);
        }
        final CompletableFuture<String> result = new CompletableFuture<>();
        translator.translate(text)
                .addOnSuccessListener(t -> result.complete(t.trim()))
                .addOnFailureListener(e -> e.printStackTrace());
        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE_INTERNET) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initTranslation();

            } else {
            }
        }
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
