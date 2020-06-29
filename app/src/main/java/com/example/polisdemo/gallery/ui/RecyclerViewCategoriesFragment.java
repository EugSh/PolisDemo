package com.example.polisdemo.gallery.ui;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polisdemo.R;
import com.example.polisdemo.gallery.model.db.FireBaseDBService;
import com.example.polisdemo.gallery.model.generator.GenerationFactory;
import com.example.polisdemo.gallery.model.generator.GenerationFactoryImpl;
import com.example.polisdemo.gallery.ui.adapter.RecyclerViewCategoryAdapter;
import com.example.polisdemo.gallery.model.dto.Photo;
import com.example.polisdemo.listener.OnSelectListener;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewCategoriesFragment extends Fragment {
    private static final String TAG = "RecyclerViewGalleryFragment";

    private final FireBaseDBService dbService;
    private final boolean readPermission;
    private final View.OnClickListener clickListener;
    private final View.OnLongClickListener longClickListener;
    private final OnSelectListener selectListener;

    private RecyclerView recyclerView;
    private RecyclerViewCategoryAdapter adapter;
    private Context context;

    private Spinner spinnerSortTypes;

    private GenerationFactory<Photo> factory;

    private String prevSortType;
    private List<String> labels = new ArrayList<>();
    private boolean wasChanges = false;


    public RecyclerViewCategoriesFragment(FireBaseDBService dbService,
                                          boolean readPermissionEnable,
                                          View.OnClickListener clickListener,
                                          View.OnLongClickListener longClickListener, OnSelectListener selectListener) {
        this.dbService = dbService;
        this.readPermission = readPermissionEnable;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
        this.selectListener = selectListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
        rootView.setTag(TAG);
        context = container.getContext();
        factory = GenerationFactoryImpl.getInstanse(context, readPermission, dbService);
        recyclerView = rootView.findViewById(R.id.recycler_view_category);
        spinnerSortTypes = rootView.findViewById(R.id.bottom_sheet_peek_spinner);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        if (prevSortType != null) {
            changeAdapter(prevSortType, labels);
        }
        initSpinnerSortType();
        return rootView;
    }

    public String getPrevSortType() {
        return prevSortType;
    }

    public void setLabels(List<String> labels) {
        this.wasChanges = !this.labels.equals(labels);
        this.labels = labels;
    }

    public void setPrevSortType(String prevSortType) {
        this.prevSortType = prevSortType;
    }

    public List<String> getLabels() {
        return labels==null? new ArrayList<>(): labels;
    }

    public boolean isWasChanges() {
        return wasChanges;
    }

    //    private void initBtnIndex(final View rootView) {
//        btnIndex = rootView.findViewById(R.id.btn_do_index);
//        btnIndex.setOnClickListener(v -> {
//            if (isIndexing.compareAndSet(false, true)) {
//                Date newestDate = MainActivity.dbService
//                        .getNewestEntry()
//                        .orElseGet(() -> new LabelEntity("", "", new Date(0), new Date(0), 0f, 0))
//                        .getDate();
//                FireBaseLabeler labeler = new FireBaseLabeler(context.getContentResolver(), newestDate, 0.8f);
//                CompletableFuture<List<LabelEntity>> future = labeler.getLabelsV2();
//                future.thenAccept(entities -> {
//                    MainActivity.dbService.putAllEntities(entities);
//                    labelAdapter.clear();
//                    spinnerLabelStates.addAll(entities.stream()
//                            .map(e -> new SpinnerLabelState(e.getLabel(), false))
//                            .collect(Collectors.toSet()));
//                    labelAdapter.addAll(spinnerLabelStates.stream().map(SpinnerLabelState::getTitle).collect(Collectors.toList()));
//                    labelAdapter.notifyDataSetChanged();
//                    isIndexing.set(false);
//                    MainActivity.runInUIThread(() -> {
//                        new AlertDialog.Builder(context)
//                                .setMessage("Маркировка фотографий завершена. Извлечено " + entities.size() + " меток.")
//                                .setNeutralButton("Ок", null)
//                                .create()
//                                .show();
//                    });
//
//                });
//            } else {
//                Snackbar.make(mLayout, "уже индексируется",
//                        Snackbar.LENGTH_SHORT)
//                        .show();
//            }
//        });
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

//    private void initSpinnerLabels(final View rootView) {
//        spinnerLabels = rootView.findViewById(R.id.spinner_labels);
//        spinnerLabelStates = MainActivity.dbService.getLabels().stream().map(s -> new SpinnerLabelState(s, false)).collect(Collectors.toList());
//        labelAdapter = new ArrayAdapter<>(context, R.layout.spinner_label_item);
//        labelAdapter.addAll(spinnerLabelStates.stream().map(SpinnerLabelState::getTitle).collect(Collectors.toList()));
////        spinnerLabels.setDefaultText("Select some labels ");
//        spinnerLabels.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (MainActivity.dbService.size() == 0) {
//                    new AlertDialog.Builder(context).setMessage("У вас отсутствет выбор меток, потому " +
//                            "что вы не промаркеровали свои фотографии.")
//                            .setNeutralButton("Ок", null)
//                            .create().show();
//                    return true;
//                }
//                return false;
//            }
//        });
//        spinnerLabels.setAdapter(labelAdapter, false, selected -> {
//            if (MainActivity.dbService.size() == 0) {
//                new AlertDialog.Builder(context).setMessage("У вас отсутствет выбор меток, потому " +
//                        "что вы не промаркеровали свои фотографии.")
//                        .setNeutralButton("Ок", null).show();
//                return;
//            }
//            final StringBuilder builder = new StringBuilder();
//            builder.append("Labels : ");
//            for (int i = 0; i < selected.length; i++) {
//                spinnerLabelStates.get(i).setSelected(selected[i]);
//                if (selected[i]) {
//                    builder.append(spinnerLabelStates.get(i).getTitle())
//                            .append(" ,");
//                }
//            }
//            builder.deleteCharAt(builder.length() - 1);
//            changeAdapter();
//        });
////        spinnerLabels
//    }


    public void changeAdapter(String sortType, List<String> labels) {
        prevSortType = sortType;
        adapter = new RecyclerViewCategoryAdapter(factory.getCategoryGenerator(sortType, labels), factory.getPhotoInfoExtractor(), context, clickListener, longClickListener, selectListener);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void notifyDataChanged(){
        changeAdapter(prevSortType, labels);
    }


    private void initSpinnerSortType() {

        spinnerSortTypes.setAdapter(new ArrayAdapter<String>(context,
                R.layout.spinner_sort_types_item,
                R.id.spinner_sort_type_item,
                getResources().getStringArray(R.array.types_sort_name)));
        spinnerSortTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeAdapter((String) parent.getItemAtPosition(position), labels);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void setIsSelectedMode(boolean isSelectedMode) {
        adapter.setIsSelectMode(isSelectedMode);
        adapter.notifyDataSetChanged();
    }
}

