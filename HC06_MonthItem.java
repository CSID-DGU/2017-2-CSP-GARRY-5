package org.androidtown.sw_pj;

public class HC06_MonthItem {

    private int dayValue;
    public HC06_MonthItem() {
    }

    public HC06_MonthItem(int day) {
        dayValue = day;
    }

    public int getDay() {
        return dayValue;
    }

    public void setDay(int day) {
        this.dayValue = day;
    }
}