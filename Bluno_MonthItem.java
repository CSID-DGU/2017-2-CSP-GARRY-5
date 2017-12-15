package org.androidtown.smart_diaper;

public class Bluno_MonthItem {

    private int dayValue;
    public Bluno_MonthItem() {
    }

    public Bluno_MonthItem(int day) {
        dayValue = day;
    }

    public int getDay() {
        return dayValue;
    }

    public void setDay(int day) {
        this.dayValue = day;
    }
}