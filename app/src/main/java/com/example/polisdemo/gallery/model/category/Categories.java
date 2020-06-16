package com.example.polisdemo.gallery.model.category;


import com.example.polisdemo.gallery.model.utils.PhotoCategoryExtractor;

public enum Categories {
    DAY_OF_WEEK,
    DAY_OF_MONTH,
    MONTH,
    YEAR,
    YEAR_MONTH,
    YEAR_MONTH_DAY,
    Location;

    public static Categories parse(final String category) {
        switch (translate(category)) {
            case "Day of week":
                return DAY_OF_WEEK;
            case "Day of month":
                return DAY_OF_MONTH;
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

    public static String translate(final String category){
        switch (category) {
            case "День недели":
                return "Day of week";
            case "День месяца":
                return  "Day of month";
            case "Месяц":
                return "Month";
            case "Год":
                return "Year";
            case "Год и месяц":
                return "Year month";
            case "Год, месяц, день":
                return "Year month day";
            default:
                return category;
        }
    }

    public PhotoCategoryExtractor getCategoryExtractor() {
        switch (this) {
            case DAY_OF_WEEK:
                return new PhotoCategoryExtractor.DayOfWeek();
            case DAY_OF_MONTH:
                return new PhotoCategoryExtractor.DayOfMonth();
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
