package net.polybugger.apollot.db;

import android.content.Context;

import net.polybugger.apollot.R;

public enum PastCurrentEnum {

    PAST(0),
    CURRENT(1);

    private static final PastCurrentEnum[] sValues = PastCurrentEnum.values();

    public static PastCurrentEnum fromInt(int x) {
        return (x < 0 || x > 1) ? PAST : sValues[x];
    }

    private int mValue;

    private PastCurrentEnum(int value) {
        mValue = value;
    }

    public int getValue() {
        return mValue;
    }

    public static String intToString(int pastCurrent, Context context) {
        if(pastCurrent == PAST.mValue)
            return context.getString(R.string.past);
        if(pastCurrent == CURRENT.mValue)
            return context.getString(R.string.current);
        return context.getString(R.string.past);
    }

    public static String toString(PastCurrentEnum pastCurrent, Context context) {
        if(pastCurrent == PAST)
            return context.getString(R.string.past);
        if(pastCurrent == CURRENT)
            return context.getString(R.string.current);
        return context.getString(R.string.past);
    }
}
