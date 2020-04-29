package com.example.polisdemo.gallery.model.category;


import com.example.polisdemo.gallery.model.dto.Photo;
import com.example.polisdemo.gallery.model.generator.CategoryGenerator;
import com.example.polisdemo.gallery.model.utils.PhotoCategoryExtractor;
import com.example.polisdemo.gallery.model.utils.PhotoInfoExtractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryGeneratorImpl<T extends Photo> implements CategoryGenerator<T> {
    private final PhotoCategoryExtractor extractor;
    private final Map<String, List<T>> mapItems;
    private final List<String> categories;

    public CategoryGeneratorImpl(Categories type, List<T> items, PhotoInfoExtractor extractor) {
        this.extractor = type.getCategoryExtractor();
        this.mapItems = sort(items, extractor);
        this.categories = new ArrayList<>(this.mapItems.keySet());
        this.categories.sort(this.extractor.getComparator());
    }

    @Override
    public List<String> getCategories() {
        return categories;
    }

    @Override
    public List<T> getItems(String itemCategory) {
        return mapItems.getOrDefault(itemCategory, new ArrayList<>());
    }

    private Map<String, List<T>> sort(final List<T> photos, final PhotoInfoExtractor infoExtractor) {
        final Map<String, List<T>> result = new HashMap<>();
        for (final T photo : photos) {
            final String key = extractor.extract(infoExtractor, photo);
            result.computeIfAbsent(key, k -> new ArrayList<>()).add(photo);
        }
        return result;
    }

}
