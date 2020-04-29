package com.example.polisdemo.gallery.model.generator;

import com.example.polisdemo.gallery.model.utils.PhotoInfoExtractor;

import java.util.List;

public interface GenerationFactory<T> {

    CategoryGenerator<T> getCategoryGenerator(final String sortType, final List<String> labels);

    PhotoInfoExtractor getPhotoInfoExtractor();
}
