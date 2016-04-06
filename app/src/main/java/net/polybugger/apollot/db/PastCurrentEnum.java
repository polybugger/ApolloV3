package net.polybugger.apollot.db;

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
}
