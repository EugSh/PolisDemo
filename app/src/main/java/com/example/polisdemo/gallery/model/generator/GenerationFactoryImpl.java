package com.example.polisdemo.gallery.model.generator;

import android.content.Context;

import com.example.polisdemo.gallery.model.category.Categories;
import com.example.polisdemo.gallery.model.category.CategoryGeneratorImpl;
import com.example.polisdemo.gallery.model.db.FireBaseDBService;
import com.example.polisdemo.gallery.model.dto.Photo;
import com.example.polisdemo.gallery.model.utils.PhotoInfoExtractor;
import com.example.polisdemo.gallery.model.utils.ProdInfoExtractor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class GenerationFactoryImpl implements GenerationFactory<Photo> {
    private static GenerationFactoryImpl instanse;
    private PhotoGenerator photoGenerator;
    private final PhotoInfoExtractor extractor;
    private final FireBaseDBService dbService;
    private boolean hasReadPermission;

    private GenerationFactoryImpl(final Context context, final boolean hasReadPermission, final FireBaseDBService dbService) {
        this.hasReadPermission = hasReadPermission;
        this.photoGenerator = hasReadPermission ? new ProdPhotoGenerator(context) : ArrayList::new;
        this.extractor = new ProdInfoExtractor(context);
        this.dbService = dbService;
    }

    public static GenerationFactory<Photo> getInstanse(final Context context, final boolean hasReadPermission, final FireBaseDBService dbService) {
        if (instanse == null) {
            instanse = new GenerationFactoryImpl(context, hasReadPermission, dbService);
        }
        if ((!instanse.hasReadPermission) && hasReadPermission) {
            instanse.hasReadPermission = hasReadPermission;
            instanse.photoGenerator = new ProdPhotoGenerator(context);
        }
        return instanse;
    }

    @Override
    public CategoryGenerator<Photo> getCategoryGenerator(final String sortType, final List<String> labels) {
        if (labels.isEmpty() || !hasReadPermission) {
            return new CategoryGeneratorImpl<>(Categories.parse(sortType), photoGenerator.generate(), extractor);
        }
        return new CategoryGeneratorImpl<>(Categories.parse(sortType),
                dbService.getEntities(labels).stream().map(e -> new Photo(e.getContentUrl(), e.getCreationDate(), e.getOrientation())).collect(Collectors.toList()),
                extractor);

    }

    @Override
    public PhotoInfoExtractor getPhotoInfoExtractor() {
        return extractor;
    }
}
