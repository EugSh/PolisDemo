package com.example.polisdemo.gallery.model.utils;


import com.example.polisdemo.gallery.model.dto.Photo;

import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Locale;

public interface PhotoCategoryExtractor {
    String extract(final PhotoInfoExtractor infoExtractor, final Photo photo);

    Comparator<String> getComparator();

    class DayOfWeek implements PhotoCategoryExtractor {

        @Override
        public String extract(PhotoInfoExtractor infoExtractor, Photo photo) {
            final GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(photo.getCreationDate());
            final String s = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);
            System.out.println(s);
            return TranslateUtils.translateDayOfWeek(s);
        }


        @Override
        public Comparator<String> getComparator() {
            return CategoryComparator.DAY_OF_WEEK;
        }
    }

    class DayOfMonth implements PhotoCategoryExtractor {

        @Override
        public String extract(PhotoInfoExtractor infoExtractor, Photo photo) {
            final GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(photo.getCreationDate());
            return String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        }

        @Override
        public Comparator<String> getComparator() {
            return CategoryComparator.DAY_OF_MONTH;
        }
    }

    class Month implements PhotoCategoryExtractor {
        @Override
        public String extract(PhotoInfoExtractor infoExtractor, Photo photo) {
            final GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(photo.getCreationDate());
            return TranslateUtils.translateMonth(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        }


        @Override
        public Comparator<String> getComparator() {
            return CategoryComparator.MONTH;
        }
    }

    class Year implements PhotoCategoryExtractor {
        @Override
        public String extract(PhotoInfoExtractor infoExtractor, Photo photo) {
            final GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(photo.getCreationDate());
            return String.valueOf(calendar.get(Calendar.YEAR));
        }

        @Override
        public Comparator<String> getComparator() {
            return CategoryComparator.YEAR;
        }
    }

    class YearMonth implements PhotoCategoryExtractor {

        @Override
        public String extract(PhotoInfoExtractor infoExtractor, Photo photo) {
            final GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(photo.getCreationDate());
            return calendar.get(Calendar.YEAR) + " " +
                    TranslateUtils.translateMonth(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        }

        @Override
        public Comparator<String> getComparator() {
            return CategoryComparator.YEAR_MONTH;
        }
    }

    class YearMonthDay implements PhotoCategoryExtractor {

        @Override
        public String extract(PhotoInfoExtractor infoExtractor, Photo photo) {
            final GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(photo.getCreationDate());
            return calendar.get(Calendar.YEAR) + " " +
                    TranslateUtils.translateMonth(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)) + " " +
                    calendar.get(Calendar.DAY_OF_MONTH);
        }

        @Override
        public Comparator<String> getComparator() {
            return CategoryComparator.YEAR_MONTH_DAY;
        }
    }


}
