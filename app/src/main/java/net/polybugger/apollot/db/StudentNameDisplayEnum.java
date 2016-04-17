package net.polybugger.apollot.db;

public enum StudentNameDisplayEnum {

    LAST_NAME_FIRST_NAME(0),
    FIRST_NAME_LAST_NAME(1);

    private static final StudentNameDisplayEnum[] sValues = StudentNameDisplayEnum.values();

    public static StudentNameDisplayEnum fromInt(int x) {
        return (x < 0 || x > 1) ? LAST_NAME_FIRST_NAME : sValues[x];
    }

    private int mValue;

    private StudentNameDisplayEnum(int value) {
        mValue = value;
    }

    public int getValue() {
        return mValue;
    }
}
