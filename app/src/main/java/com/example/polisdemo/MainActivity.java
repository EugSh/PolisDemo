package com.example.polisdemo;

import android.location.Geocoder;
import android.os.Bundle;

import com.example.polisdemo.gallery.model.db.FireBaseDBService;
import com.example.polisdemo.gallery.model.db.FireBaseDBServiceImpl;
import com.example.polisdemo.gallery.model.db.FireBaseSQLHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    public static Geocoder GEO_CODER;
    public static Set<String> selectedImages = new HashSet<>();
    public static FireBaseDBService dbService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GEO_CODER = new Geocoder(this, Locale.getDefault());
        long t = System.currentTimeMillis();
        final FireBaseSQLHelper dbHelper = new FireBaseSQLHelper(this,2);
        dbService = new FireBaseDBServiceImpl(dbHelper);
        System.out.println("время подключения к базе " + (System.currentTimeMillis() - t));
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_gallery, R.id.navigation_collage)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

}
