package com.example.polisdemo.gallery.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polisdemo.R;
import com.example.polisdemo.gallery.model.db.FireBaseDBService;
import com.example.polisdemo.gallery.model.generator.GenerationFactoryImpl;
import com.example.polisdemo.gallery.model.utils.PhotoInfoExtractor;
import com.example.polisdemo.gallery.ui.adapter.RecyclerViewLabelAdapter;
import com.example.polisdemo.utils.Utils;


public class RecyclerViewLabelFragment extends Fragment {
    private RecyclerView recyclerView;
    private Context context;
    private RecyclerViewLabelAdapter adapter;
    private final FireBaseDBService dbService;
    private final boolean readPermission;
    private final View.OnClickListener onLabelClickListener;
    private PhotoInfoExtractor extractor;

    public RecyclerViewLabelFragment(FireBaseDBService dbService, boolean readPermission, View.OnClickListener onLabelClickListener) {
        this.dbService = dbService;
        this.readPermission = readPermission;
        this.onLabelClickListener = onLabelClickListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view_label_fragment, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view_label);
        context = container.getContext();
        final RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, Utils.IMAGE_IN_ROW, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        extractor = GenerationFactoryImpl.getInstanse(context, readPermission, dbService).getPhotoInfoExtractor();
        adapter = new RecyclerViewLabelAdapter(dbService, extractor, onLabelClickListener, context);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    public void notifyDataChange() {
        adapter.notifyDataChanged();
    }
}
