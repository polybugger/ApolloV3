package net.polybugger.apollot;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;

public class BackgroundRect {

    private static final HashMap<String, Integer> mBackgroundMap = new HashMap<>();

    private BackgroundRect() { }

    public static int getBackgroundResource(String color, Context context) {
        if(mBackgroundMap.size() == 0)
            initHashMap(context);

        if(mBackgroundMap.containsKey(color)) {
            return mBackgroundMap.get(color);
        }
        return R.drawable.bg_rounded_rect_0;
    }

    private static void initHashMap(Context context) {
        ArrayList<String> hexStrings = new ArrayList<>();
        hexStrings.add(0, String.format("#%08X", 0xFFFFFFFF & ContextCompat.getColor(context, R.color.bg_color_0)));
        hexStrings.add(1, String.format("#%08X", 0xFFFFFFFF & ContextCompat.getColor(context, R.color.bg_color_1)));
        hexStrings.add(2, String.format("#%08X", 0xFFFFFFFF & ContextCompat.getColor(context, R.color.bg_color_2)));
        hexStrings.add(3, String.format("#%08X", 0xFFFFFFFF & ContextCompat.getColor(context, R.color.bg_color_3)));
        hexStrings.add(4, String.format("#%08X", 0xFFFFFFFF & ContextCompat.getColor(context, R.color.bg_color_4)));
        hexStrings.add(5, String.format("#%08X", 0xFFFFFFFF & ContextCompat.getColor(context, R.color.bg_color_5)));
        hexStrings.add(6, String.format("#%08X", 0xFFFFFFFF & ContextCompat.getColor(context, R.color.bg_color_6)));
        hexStrings.add(7, String.format("#%08X", 0xFFFFFFFF & ContextCompat.getColor(context, R.color.bg_color_7)));

        ArrayList<Integer> bgDrawables = new ArrayList<>();
        bgDrawables.add(0, R.drawable.bg_rounded_rect_0);
        bgDrawables.add(1, R.drawable.bg_rounded_rect_1);
        bgDrawables.add(2, R.drawable.bg_rounded_rect_2);
        bgDrawables.add(3, R.drawable.bg_rounded_rect_3);
        bgDrawables.add(4, R.drawable.bg_rounded_rect_4);
        bgDrawables.add(5, R.drawable.bg_rounded_rect_5);
        bgDrawables.add(6, R.drawable.bg_rounded_rect_6);
        bgDrawables.add(7, R.drawable.bg_rounded_rect_7);

        int i, n = 8;
        for(i = 0; i < n; ++i) {
            mBackgroundMap.put(hexStrings.get(i), bgDrawables.get(i));
        }
    }
}
