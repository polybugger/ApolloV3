package net.polybugger.apollot.db;

import android.content.Context;

import net.polybugger.apollot.R;

public enum GenderEnum {

    MALE(0),
    FEMALE(1),
    UNKNOWN(2);

    private static final GenderEnum[] sValues = GenderEnum.values();

    public static GenderEnum fromInt(int x) {
        return (x < 0 || x > 2) ? UNKNOWN : sValues[x];
    }

    private int mValue;

    private GenderEnum(int value) {
        mValue = value;
    }

    public int getValue() {
        return mValue;
    }

    public static String intToString(int gender, Context context) {
        if(gender == MALE.mValue)
            return context.getString(R.string.male);
        if(gender == FEMALE.mValue)
            return context.getString(R.string.female);
        if(gender == UNKNOWN.mValue)
            return context.getString(R.string.unknown);
        return context.getString(R.string.unknown);
    }
}
