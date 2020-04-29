package com.example.polisdemo.gallery.model.generator;

import java.util.List;

public interface CategoryGenerator<T> {
    List<String> getCategories();

    List<T> getItems(final String itemCategory);
}
