package com.javamentor.qa.platform.dao.impl.filter;

public enum DayFilter {
    WEEK(7), YEAR(365), MONTH(30), ALL;

    private int i;

    DayFilter() {
    }

    DayFilter(int i) {
        this.i = i;
    }

    public int getDay() {
        return i;
    }

}
