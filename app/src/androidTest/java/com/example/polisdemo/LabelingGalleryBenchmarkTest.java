package com.example.polisdemo;

import android.content.Context;
import android.util.Log;

import androidx.benchmark.BenchmarkState;
import androidx.benchmark.junit4.BenchmarkRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.polisdemo.gallery.model.db.FireBaseDBService;
import com.example.polisdemo.gallery.model.db.FireBaseDBServiceImpl;
import com.example.polisdemo.gallery.model.db.FireBaseSQLHelper;
import com.example.polisdemo.gallery.model.db.LabelEntity;
import com.example.polisdemo.gallery.model.firebase.FireBaseLabeler;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RunWith(AndroidJUnit4.class)
public class LabelingGalleryBenchmarkTest {
    private static final String TAG = "LabelingGalleryBenchmarkTest.class";
    private Context context;
    private FireBaseSQLHelper helper;
    private FireBaseDBService service;
    @Rule
    public BenchmarkRule benchmarkRule = new BenchmarkRule();

    @Before
    public void createDB() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        helper = new FireBaseSQLHelper(context, 2);
        service = new FireBaseDBServiceImpl(helper);
    }


    @Test
    public void myBenchmark() throws ExecutionException, InterruptedException {
        final BenchmarkState state = benchmarkRule.getState();
        while (state.keepRunning()) {
            Date newestDate = service
                    .getNewestEntry()
                    .orElseGet(() -> new LabelEntity("", "", new Date(0), new Date(0), 0f, 0))
                    .getDate();
            FireBaseLabeler labeler = new FireBaseLabeler(context.getContentResolver(), newestDate, 0.8f);
            CompletableFuture<List<LabelEntity>> future = labeler.getLabelsV2();
            future.thenAccept(entities -> service.putAllEntities(entities));
            future.get();
            Log.i(TAG, "Entities count - " + service.size());
        }
    }

    @After
    public void cleanDB() {
        helper.close();
        context.deleteDatabase(FireBaseSQLHelper.DB_NAME);
    }

}
