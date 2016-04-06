package net.polybugger.apollot.db;

public enum DaysBits {

    M  (1 << 0),
    T  (1 << 1),
    W  (1 << 2),
    Th (1 << 3),
    F  (1 << 4),
    S  (1 << 5),
    Su (1 << 6),
    M_F (M.mValue + T.mValue + W.mValue + Th.mValue + F.mValue),
    MWF (M.mValue + W.mValue + F.mValue),
    TTh (T.mValue + Th.mValue);

    private final int mValue;

    private DaysBits(int value) {
        mValue = value;
    }

    public int getValue() {
        return mValue;
    }

}
