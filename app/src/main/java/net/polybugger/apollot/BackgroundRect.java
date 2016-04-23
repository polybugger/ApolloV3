package net.polybugger.apollot;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;

public class BackgroundRect {

    private static final HashMap<String, Integer> mBackgroundMap = new HashMap<>();
    private static final ArrayList<String> mHexStrings = new ArrayList<>();

    private BackgroundRect() { }

    public static int getBackgroundResource(String color, Context context) {
        if(mBackgroundMap.size() == 0)
            initHashMap(context);

        if(mBackgroundMap.containsKey(color)) {
            return mBackgroundMap.get(color);
        }
        return R.drawable.bg_rounded_rect_0;
    }

    public static String getHexString(int index) {
        return mHexStrings.get(index);
    }

    private static void initHashMap(Context context) {
        mHexStrings.add(0, String.format("#%08X", 0xFFFFFFFF & ContextCompat.getColor(context, R.color.bg_color_0)));
        mHexStrings.add(1, String.format("#%08X", 0xFFFFFFFF & ContextCompat.getColor(context, R.color.bg_color_1)));
        mHexStrings.add(2, String.format("#%08X", 0xFFFFFFFF & ContextCompat.getColor(context, R.color.bg_color_2)));
        mHexStrings.add(3, String.format("#%08X", 0xFFFFFFFF & ContextCompat.getColor(context, R.color.bg_color_3)));
        mHexStrings.add(4, String.format("#%08X", 0xFFFFFFFF & ContextCompat.getColor(context, R.color.bg_color_4)));
        mHexStrings.add(5, String.format("#%08X", 0xFFFFFFFF & ContextCompat.getColor(context, R.color.bg_color_5)));
        mHexStrings.add(6, String.format("#%08X", 0xFFFFFFFF & ContextCompat.getColor(context, R.color.bg_color_6)));
        mHexStrings.add(7, String.format("#%08X", 0xFFFFFFFF & ContextCompat.getColor(context, R.color.bg_color_7)));

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
            mBackgroundMap.put(mHexStrings.get(i), bgDrawables.get(i));
        }
    }
}
