package net.polybugger.apollot.db;

import android.content.Context;

import net.polybugger.apollot.R;

public enum GenderEnum {

    UNKNOWN(-1),
    MALE(0),
    FEMALE(1);

    private static final GenderEnum[] sValues = GenderEnum.values();

    public static GenderEnum fromInt(int x) {
        return (x < -1 || x > 1) ? UNKNOWN : sValues[x];
    }

    private int mValue;

    private GenderEnum(int value) {
        mValue = value;
    }

    public int getValue() {
        return mValue;
    }

    public static String intToString(Context context, int gender) {
        if(gender == UNKNOWN.mValue)
            return context.getString(R.string.unknown);
        if(gender == MALE.mValue)
            return context.getString(R.string.male);
        if(gender == FEMALE.mValue)
            return context.getString(R.string.female);
        return context.getString(R.string.unknown);
    }
}
