package com.example.polisdemo.gallery.model.utils;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CategoryComparator {
    private static final Map<String, Integer> dayOfWeek = Stream.of(new String[][]{
            {"Monday", "0"},
            {"Tuesday", "1"},
            {"Wednesday", "2"},
            {"Thursday", "3"},
            {"Friday", "4"},
            {"Saturday", "5"},
            {"Sunday", "6"},
            {"Понедельник", "0"},
            {"Вторник", "1"},
            {"Среда", "2"},
            {"Четверг", "3"},
            {"Пятница", "4"},
            {"Суббота", "5"},
            {"Воскресенье", "6"}
    }).collect(Collectors.toMap(strings -> strings[0], strings -> Integer.parseInt(strings[1])));

    private static final Map<String, Integer> month = Stream.of(new String[][]{
            {"January", "0"},
            {"February", "1"},
            {"March", "2"},
            {"April", "3"},
            {"May", "4"},
            {"June", "5"},
            {"July", "6"},
            {"August", "7"},
            {"September", "8"},
            {"October", "9"},
            {"November", "10"},
            {"December", "11"},
            {"Январь", "0"},
            {"Февраль", "1"},
            {"Март", "2"},
            {"Апрель", "3"},
            {"Май", "4"},
            {"Июнь", "5"},
            {"Июль", "6"},
            {"Август", "7"},
            {"Сентябрь", "8"},
            {"Октябрь", "9"},
            {"Ноябрь", "10"},
            {"Декабрь", "11"},
    }).collect(Collectors.toMap(strings -> strings[0], strings -> Integer.parseInt(strings[1])));


    private CategoryComparator() {
    }

    public static final Comparator<String> DAY_OF_WEEK = Comparator.<String, Integer>comparing(dayOfWeek::get);

    public static final Comparator<String> DAY_OF_MONTH = Comparator.<String, Integer>comparing(Integer::parseInt);

    public static final Comparator<String> MONTH = Comparator.<String, Integer>comparing(month::get);

    public static final Comparator<String> YEAR = Comparator.<String, Integer>comparing(Integer::parseInt).reversed();

    public static final Comparator<String> YEAR_MONTH = Comparator.<String, Integer>comparing(s -> Integer.parseInt(s.split(" ")[0]))
            .thenComparing(s -> month.get(s.split(" ")[1]))
            .reversed();

    public static final Comparator<String> YEAR_MONTH_DAY = YEAR_MONTH
            .reversed()
            .thenComparing(s -> Integer.parseInt(s.split(" ")[2]))
            .reversed();

}
