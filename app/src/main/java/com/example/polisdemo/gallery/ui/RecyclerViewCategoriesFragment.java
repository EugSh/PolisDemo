package com.example.polisdemo.gallery.ui;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polisdemo.MainActivity;
import com.example.polisdemo.R;
import com.example.polisdemo.gallery.model.db.LabelEntity;
import com.example.polisdemo.gallery.model.dto.SpinnerLabelState;
import com.example.polisdemo.gallery.model.firebase.FireBaseLabeler;
import com.example.polisdemo.gallery.model.generator.GenerationFactory;
import com.example.polisdemo.gallery.model.generator.GenerationFactoryImpl;
import com.example.polisdemo.gallery.ui.adapter.RecyclerViewCategoriesAdapter;
import com.example.polisdemo.gallery.model.dto.Photo;
import com.google.android.material.snackbar.Snackbar;
import com.thomashaertel.widget.MultiSpinner;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class RecyclerViewCategoriesFragment extends Fragment {
    private static final int PERMISSION_REQUEST_CODE_READ = 1;

    private static final String TAG = "RecyclerViewGalleryFragment";

    private RecyclerView recyclerView;
    private View mLayout;
    private Context context;
    private Spinner spinnerSortTypes;
    private MultiSpinner spinnerLabels;
    private ArrayAdapter labelAdapter;
    private Button btnIndex;
    private AtomicBoolean isIndexing = new AtomicBoolean(false);
    private List<SpinnerLabelState> spinnerLabelStates;

    private GenerationFactory<Photo> factory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
        rootView.setTag(TAG);
        factory = new GenerationFactoryImpl(context, false, MainActivity.dbService);
        mLayout = container;
        context = container.getContext();
        initSpinnerSortType(rootView);
        initSpinnerLabels(rootView);
        initBtnIndex(rootView);
        recyclerView = rootView.findViewById(R.id.recycler_view_category);

        if (checkReadPerm()) {
            display(true);
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE_READ);
        }
        return rootView;
    }

    private void initBtnIndex(final View rootView) {
        btnIndex = rootView.findViewById(R.id.btn_do_index);
        btnIndex.setOnClickListener(v -> {
            if (isIndexing.compareAndSet(false, true)) {
                long t = System.currentTimeMillis();
                Date newestDate = MainActivity.dbService
                        .getNewestEntry()
                        .orElseGet(() -> new LabelEntity("", "", new Date(0), new Date(0), 0f))
                        .getDate();
                FireBaseLabeler labeler = new FireBaseLabeler(context.getContentResolver(), newestDate, 0.8f);
                CompletableFuture<List<LabelEntity>> future = labeler.getLabelsV2();
                future.thenAccept(entities -> {
                    System.out.println("добавляем в базу " + entities.size());
                    MainActivity.dbService.putAllEntities(entities);
                    labelAdapter.clear();
                    spinnerLabelStates.addAll(entities.stream()
                            .map(e -> new SpinnerLabelState(e.getLabel(), false))
                            .collect(Collectors.toList()));
                    labelAdapter.addAll(spinnerLabelStates.stream().map(SpinnerLabelState::getTitle).collect(Collectors.toList()));
                    labelAdapter.notifyDataSetChanged();
                    isIndexing.set(false);
                    System.out.println("@<><>@<><>@");
                    System.out.println(System.currentTimeMillis() - t);
                });
            } else {
                Snackbar.make(mLayout, "уже индексируется",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void initSpinnerSortType(final View rootView) {
        spinnerSortTypes = rootView.findViewById(R.id.spinner_sort_type);
        spinnerSortTypes.setAdapter(new ArrayAdapter<String>(context,
                R.layout.spinner_sort_types_item,
                R.id.spinner_sort_type_item,
                getResources().getStringArray(R.array.types_sort_name)));
        spinnerSortTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeAdapter((String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("nothing");
            }
        });
    }

    private void initSpinnerLabels(final View rootView) {
        spinnerLabels = rootView.findViewById(R.id.spinner_labels);
        spinnerLabelStates = MainActivity.dbService.getLabels().stream().map(s -> new SpinnerLabelState(s, false)).collect(Collectors.toList());
        labelAdapter = new ArrayAdapter<>(context, R.layout.spinner_label_item);
        labelAdapter.addAll(spinnerLabelStates.stream().map(SpinnerLabelState::getTitle).collect(Collectors.toList()));
        spinnerLabels.setDefaultText("Select some labels ");
        spinnerLabels.setAdapter(labelAdapter, false, selected -> {
            final StringBuilder builder = new StringBuilder();
            builder.append("Labels : ");
            for (int i = 0; i < selected.length; i++) {
                spinnerLabelStates.get(i).setSelected(selected[i]);
                if (selected[i]) {
                    builder.append(spinnerLabelStates.get(i).getTitle())
                            .append(" ,");
                }
            }
            builder.deleteCharAt(builder.length() - 1);
            changeAdapter();
        });
    }

    private boolean checkReadPerm() {
        final int readPerm = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return readPerm == PackageManager.PERMISSION_GRANTED;
    }

    private void display(final boolean hasPermission) {
        factory = new GenerationFactoryImpl(context, hasPermission, MainActivity.dbService);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void changeAdapter() {
        changeAdapter((String) spinnerSortTypes.getItemAtPosition(0));
    }

    private void changeAdapter(String sortType) {
        long t = System.currentTimeMillis();
        final List<String> labels = spinnerLabelStates.stream()
                .filter(SpinnerLabelState::isSelected)
                .map(SpinnerLabelState::getTitle).collect(Collectors.toList());
        final RecyclerViewCategoriesAdapter mAdapter = new RecyclerViewCategoriesAdapter(factory.getCategoryGenerator(sortType, labels), factory.getPhotoInfoExtractor());
        recyclerView.setAdapter(mAdapter);
        recyclerView.getAdapter().notifyDataSetChanged();
        System.out.println("время смены адаптера " + (System.currentTimeMillis() - t));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE_READ) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                display(true);
            } else {
                Snackbar.make(mLayout, "gallery could not open, permission denied",
                        Snackbar.LENGTH_SHORT)
                        .show();
                display(false);
            }
        }
    }
}

