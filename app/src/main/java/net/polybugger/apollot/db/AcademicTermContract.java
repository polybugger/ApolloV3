package net.polybugger.apollot.db;

import android.provider.BaseColumns;

public class AcademicTermContract {

    public static final String TABLE_NAME = "AcademicTerms";

    private AcademicTermContract() { }

    public static class AcademicTermEntry implements BaseColumns {

        public static String DESCRIPTION = "Description";
        public static String COLOR = "Color";

    }

}

