package net.polybugger.apollot.db;

public enum ColorEnum {

    TRANSPARENT("00ffffff");

    private String mValue;

    private ColorEnum(String value) {
        mValue = value;
    }

    public String getValue() {
        return mValue;
    }

    @Override
    public String toString() {
        return mValue;
    }
}
