package com.example.polisdemo.gallery.model.utils;

public class TranslateUtils {
    private TranslateUtils() {
    }

    public static String translateDayOfWeek(String s) {
        switch (s) {
            case "Sunday":
                return "Воскресенье";
            case "Monday":
                return "Понедельник";
            case "Tuesday":
                return "Вторник";
            case "Wednesday":
                return "Среда";
            case "Thursday":
                return "Четверг";
            case "Friday":
                return "Пятница";
            case "Saturday":
                return "Суббота";
            default:
                return s;
        }
    }

    public static String translateMonth(String s) {
        switch (s) {
            case "January":
                return "Январь";
            case "February":
                return "Февраль";
            case "March":
                return "Март";
            case "April":
                return "Апрель";
            case "May":
                return "Май";
            case "June":
                return "Июнь";
            case "July":
                return "Июль";
            case "August":
                return "Август";
            case "September":
                return "Сентябрь";
            case "October":
                return "Октябрь";
            case "November":
                return "Ноябрь";
            case "December":
                return "Декабрь";
            default:
                return s;
        }
    }
}
