package org.androidtown.smart_diaper;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class Bluno_MonthItemView extends AppCompatTextView {

    private Bluno_MonthItem item;

    public Bluno_MonthItemView(Context context) {
        super(context);
        init();
    }

    public Bluno_MonthItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        setBackgroundColor(Color.WHITE);
    }

    public Bluno_MonthItem getItem() {
        return item;
    }

    public void setItem(Bluno_MonthItem item) {
        this.item = item;

        int day = item.getDay();
        if (day != 0) {
            setText(String.valueOf(day));
        } else {
            setText("");
        }
    }
}