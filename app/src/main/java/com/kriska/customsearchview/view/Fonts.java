package com.kriska.customsearchview.view;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by nikita on 4/15/16.
 */
public class Fonts {


    public static Typeface FONT_ICON;
    public static Typeface REGULAR;
    public static Typeface LIGHT;
    public static Typeface BOLD;
    public static Typeface ITALIC;

    public static void initializeFonts(Context context) {

        Fonts.FONT_ICON = Typeface.create(
                Typeface.createFromAsset(context.getAssets(), "search.ttf"),
                Typeface.NORMAL);
        Fonts.ITALIC = Typeface.createFromAsset(context.getAssets(), "Roboto-LightItalic_1.ttf");
        Fonts.LIGHT = Typeface.createFromAsset(context.getAssets(), "Roboto-Light_0.ttf");
        Fonts.BOLD = Typeface.createFromAsset(context.getAssets(), "Roboto-Bold_0.ttf");
        Fonts.REGULAR = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular_0.ttf");

    }


}
