package com.kriska.customsearchview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.kriska.customsearchview.R;


/**
 * How to user ?
 * app:typeface="Light or Bold or Regular or italic"
 * <p/>
 * By default it will be a regular file
 */

public class CustomTextView extends TextView {
    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomTextView);

        setupFont(a);

    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomTextView);

        setupFont(a);
    }

    public CustomTextView(Context context) {
        super(context);

        setTypefaceRegular();
    }

    private void setupFont(TypedArray a) {

        String fontType = a.getString(R.styleable.CustomTextView_typeface);
        if (fontType == null) {
            setTypefaceRegular();
            return;
        }
        if (fontType.equals("Bold")) {
            setTypefaceBold();
        } else if (fontType.equals("Light")) {
            setTypefaceLight();
        } else if (fontType.equals("Italic")) {
            setTypefaceItalic();
        } else {
            setTypefaceRegular();
        }

        a.recycle();


    }


    private void setTypefaceBold() {
        this.setTypeface(Fonts.BOLD);
    }

    private void setTypefaceLight() {
        this.setTypeface(Fonts.LIGHT);
    }

    private void setTypefaceRegular() {
        this.setTypeface(Fonts.REGULAR);
    }

    private void setTypefaceItalic() {
        this.setTypeface(Fonts.ITALIC);
    }
}
