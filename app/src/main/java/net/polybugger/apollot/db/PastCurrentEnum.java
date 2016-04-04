package net.polybugger.apollot.db;

public enum PastCurrentEnum {

    PAST(0),
    CURRENT(1);

    private int mValue;

    private PastCurrentEnum(int value) {
        mValue = value;
    }

    public int getValue() {
        return mValue;
    }
}
