package org.androidtown.sw_pj;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class HC06_MonthItemView extends AppCompatTextView {

    private HC06_MonthItem item;

    public HC06_MonthItemView(Context context) {
        super(context);
        init();
    }

    public HC06_MonthItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        setBackgroundColor(Color.WHITE);
    }

    public HC06_MonthItem getItem() {
        return item;
    }

    public void setItem(HC06_MonthItem item) {
        this.item = item;

        int day = item.getDay();
        if (day != 0) {
            setText(String.valueOf(day));
        } else {
            setText("");
        }
    }
}