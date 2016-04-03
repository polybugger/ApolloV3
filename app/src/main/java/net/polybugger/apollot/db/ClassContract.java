package net.polybugger.apollot.db;

import android.provider.BaseColumns;

public class ClassContract {

    public static final String TABLE_NAME = "Classes";

    private ClassContract() { }

    public static class ClassEntry implements BaseColumns {

        public static final String CODE = "Code";
        public static final String DESCRIPTION = "Description";
        public static final String ACADEMIC_TERM_ID = "AcademicTermId";
        public static final String YEAR = "Year";
        public static final String CURRENT = "Current";
        public static final String DATE_CREATED = "DateCreated";

    }

}
