package net.polybugger.apollot.db;

public enum ColorEnum {

    TRANSPARENT("#00000000");

    private String mValue;

    private ColorEnum(String value) {
        mValue = value;
    }

    public String getValue() {
        return mValue;
    }
}
