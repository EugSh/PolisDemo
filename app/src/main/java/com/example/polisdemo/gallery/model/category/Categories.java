package com.example.polisdemo.gallery.model.category;


import com.example.polisdemo.gallery.model.utils.PhotoCategoryExtractor;

public enum Categories {
    DAY_OF_WEEK,
    MONTH,
    YEAR,
    YEAR_MONTH,
    YEAR_MONTH_DAY;

    public static Categories parse(final String category) {
        switch (category) {
            case "Day of week":
                return DAY_OF_WEEK;
            case "Month":
                return MONTH;
            case "Year":
                return YEAR;
            case "Year month":
                return YEAR_MONTH;
            case "Year month day":
            default:
                return YEAR_MONTH_DAY;
        }
    }

    public PhotoCategoryExtractor getCategoryExtractor() {
        switch (this) {
            case DAY_OF_WEEK:
                return new PhotoCategoryExtractor.DayOfWeek();
            case MONTH:
                return new PhotoCategoryExtractor.Month();
            case YEAR:
                return new PhotoCategoryExtractor.Year();
            case YEAR_MONTH:
                return new PhotoCategoryExtractor.YearMonth();
            case YEAR_MONTH_DAY:
            default:
                return new PhotoCategoryExtractor.YearMonthDay();
        }
    }

}
