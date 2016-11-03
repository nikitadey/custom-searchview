package com.kriska.customsearchview.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


public class FontIcon extends TextView {
    Context context;

    public FontIcon(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        setRegularFont();
    }

    public FontIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setRegularFont();

    }

    public FontIcon(Context context) {
        super(context);
        this.context = context;
        setRegularFont();
    }


    public void setRegularFont() {

        setTypeface(Typeface.create(
                Typeface.createFromAsset(context.getAssets(), "search.ttf"),
                Typeface.NORMAL));
    }


}
