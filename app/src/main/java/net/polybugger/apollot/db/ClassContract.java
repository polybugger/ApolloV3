package net.polybugger.apollot.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.annotation.StyleableRes;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ClassContract {

    public static final String TABLE_NAME = "Classes";
    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
            ClassEntry._ID + " INTEGER PRIMARY KEY, " +
            ClassEntry.CODE + " TEXT NOT NULL, " +
            ClassEntry.DESCRIPTION + " TEXT NULL, " +
            ClassEntry.ACADEMIC_TERM_ID + " INTEGER NULL REFERENCES " +
                AcademicTermContract.TABLE_NAME + " (" + AcademicTermContract.AcademicTermEntry._ID + ") ON DELETE SET NULL, " +
            ClassEntry.YEAR + " INTEGER NULL, " +
            ClassEntry.CURRENT + " INTEGER NOT NULL DEFAULT 1, " +
            ClassEntry.DATE_CREATED + " TEXT NULL)";
    public static final String SELECT_TABLE_SQL = "SELECT " +
            "c." + ClassEntry._ID + ", " + // 0
            "c." + ClassEntry.CODE + ", " + // 1
            "c." + ClassEntry.DESCRIPTION + ", " + // 2 nullable
            "c." + ClassEntry.ACADEMIC_TERM_ID + ", " + // 3 nullable
                "at." + AcademicTermContract.AcademicTermEntry.DESCRIPTION + ", " + // 4
                "at." + AcademicTermContract.AcademicTermEntry.COLOR + ", " + // 5
            "c." + ClassEntry.YEAR + ", " + // 6 nullable
            "c." + ClassEntry.CURRENT + ", " + // 7
            "c." + ClassEntry.DATE_CREATED + // 8 nullable
            " FROM " + TABLE_NAME + " AS c LEFT OUTER JOIN " +
                AcademicTermContract.TABLE_NAME + " AS at ON c." + ClassEntry.ACADEMIC_TERM_ID + "=at." + AcademicTermContract.AcademicTermEntry._ID;
    public static final String DELETE_ALL_SQL = "DELETE FROM " + TABLE_NAME;

    private ClassContract() { }

    public static long _insert(SQLiteDatabase db, String code, String description, AcademicTermContract.AcademicTermEntry academicTerm, Long year, PastCurrentEnum pastCurrent, Date dateCreated) {
        ContentValues values = new ContentValues();
        values.put(ClassEntry.CODE, code);
        values.put(ClassEntry.DESCRIPTION, description);
        values.put(ClassEntry.ACADEMIC_TERM_ID, academicTerm != null ? academicTerm.getId() : null);
        values.put(ClassEntry.YEAR, year);
        values.put(ClassEntry.CURRENT, pastCurrent.getValue());
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_TIME_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        values.put(ClassEntry.DATE_CREATED, (dateCreated != null) ? sdf.format(dateCreated) : null);
        return db.insert(TABLE_NAME, null, values);
    }

    public static int _update(SQLiteDatabase db, long id, String code, String description, AcademicTermContract.AcademicTermEntry academicTerm, Long year, PastCurrentEnum pastCurrent, Date dateCreated) {
        ContentValues values = new ContentValues();
        values.put(ClassEntry.CODE, code);
        values.put(ClassEntry.DESCRIPTION, description);
        values.put(ClassEntry.ACADEMIC_TERM_ID, academicTerm != null ? academicTerm.getId() : null);
        values.put(ClassEntry.YEAR, year);
        values.put(ClassEntry.CURRENT, pastCurrent.getValue());
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_TIME_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        values.put(ClassEntry.DATE_CREATED, (dateCreated != null) ? sdf.format(dateCreated) : null);
        return db.update(TABLE_NAME, values, ClassEntry._ID + "=?", new String[]{String.valueOf(id)});
    }

    public static int _delete(SQLiteDatabase db, long id) {
        return db.delete(TABLE_NAME, ClassEntry._ID + "=?", new String[]{String.valueOf(id)});
    }

    public static ClassEntry _getEntry(SQLiteDatabase db, long id) {
        ClassEntry entry = null;
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_TIME_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        Date dateCreated;
        Cursor cursor = db.query(TABLE_NAME + " AS c LEFT OUTER JOIN " +
                        AcademicTermContract.TABLE_NAME + " AS at ON c." + ClassEntry.ACADEMIC_TERM_ID + "=at." + AcademicTermContract.AcademicTermEntry._ID,
                new String[]{"c." + ClassEntry._ID, // 0
                        "c." + ClassEntry.CODE, // 1
                        "c." + ClassEntry.DESCRIPTION, // 2 nullable
                        "c." + ClassEntry.ACADEMIC_TERM_ID, // 3 nullable
                        "at." + AcademicTermContract.AcademicTermEntry.DESCRIPTION, // 4
                        "at." + AcademicTermContract.AcademicTermEntry.COLOR, // 5
                        "c." + ClassEntry.YEAR, // 6 nullable
                        "c." + ClassEntry.CURRENT, // 7
                        "c." + ClassEntry.DATE_CREATED}, // 8 nullable
                "c." + ClassEntry._ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            try {
                dateCreated = sdf.parse(cursor.getString(8));
            }
            catch(Exception e) {
                dateCreated = null;
            }
            entry = new ClassEntry(cursor.getLong(0),
                    cursor.getString(1),
                    cursor.isNull(2) ? null : cursor.getString(2),
                    cursor.isNull(3) ? null : new AcademicTermContract.AcademicTermEntry(cursor.getLong(3), cursor.getString(4), cursor.isNull(5) ? null : cursor.getString(5)),
                    cursor.isNull(6) ? null : cursor.getLong(6),
                    PastCurrentEnum.fromInt(cursor.getInt(7)),
                    dateCreated); // 8
        }
        cursor.close();
        return entry;
    }

    public static ClassEntry _getEntryByCode(SQLiteDatabase db, String code) {
        ClassEntry entry = null;
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_TIME_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        Date dateCreated;
        Cursor cursor = db.query(TABLE_NAME + " AS c LEFT OUTER JOIN " +
                        AcademicTermContract.TABLE_NAME + " AS at ON c." + ClassEntry.ACADEMIC_TERM_ID + "=at." + AcademicTermContract.AcademicTermEntry._ID,
                new String[]{"c." + ClassEntry._ID, // 0
                        "c." + ClassEntry.CODE, // 1
                        "c." + ClassEntry.DESCRIPTION, // 2 nullable
                        "c." + ClassEntry.ACADEMIC_TERM_ID, // 3 nullable
                        "at." + AcademicTermContract.AcademicTermEntry.DESCRIPTION, // 4
                        "at." + AcademicTermContract.AcademicTermEntry.COLOR, // 5
                        "c." + ClassEntry.YEAR, // 6 nullable
                        "c." + ClassEntry.CURRENT, // 7
                        "c." + ClassEntry.DATE_CREATED}, // 8 nullable
                "c." + ClassEntry.CODE + "=?",
                new String[]{String.valueOf(code)},
                null, null, null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast()) {
            try {
                dateCreated = sdf.parse(cursor.getString(8));
            }
            catch(Exception e) {
                dateCreated = null;
            }
            entry = new ClassEntry(cursor.getLong(0),
                    cursor.getString(1),
                    cursor.isNull(2) ? null : cursor.getString(2),
                    cursor.isNull(3) ? null : new AcademicTermContract.AcademicTermEntry(cursor.getLong(3), cursor.getString(4), cursor.isNull(5) ? null : cursor.getString(5)),
                    cursor.isNull(6) ? null : cursor.getLong(6),
                    PastCurrentEnum.fromInt(cursor.getInt(7)),
                    dateCreated); // 8
        }
        cursor.close();
        return entry;
    }

    public static ArrayList<ClassEntry> _getEntries(SQLiteDatabase db) {
        ArrayList<ClassEntry> entries = new ArrayList<>();
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_TIME_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        Date dateCreated;
        Cursor cursor = db.query(TABLE_NAME + " AS c LEFT OUTER JOIN " +
                        AcademicTermContract.TABLE_NAME + " AS at ON c." + ClassEntry.ACADEMIC_TERM_ID + "=at." + AcademicTermContract.AcademicTermEntry._ID,
                new String[]{"c." + ClassEntry._ID, // 0
                        "c." + ClassEntry.CODE, // 1
                        "c." + ClassEntry.DESCRIPTION, // 2 nullable
                        "c." + ClassEntry.ACADEMIC_TERM_ID, // 3 nullable
                        "at." + AcademicTermContract.AcademicTermEntry.DESCRIPTION, // 4
                        "at." + AcademicTermContract.AcademicTermEntry.COLOR, // 5
                        "c." + ClassEntry.YEAR, // 6 nullable
                        "c." + ClassEntry.CURRENT, // 7
                        "c." + ClassEntry.DATE_CREATED}, // 8 nullable
                null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            try {
                dateCreated = sdf.parse(cursor.getString(8));
            }
            catch(Exception e) {
                dateCreated = null;
            }
            entries.add(new ClassEntry(cursor.getLong(0),
                    cursor.getString(1),
                    cursor.isNull(2) ? null : cursor.getString(2),
                    cursor.isNull(3) ? null : new AcademicTermContract.AcademicTermEntry(cursor.getLong(3), cursor.getString(4), cursor.isNull(5) ? null : cursor.getString(5)),
                    cursor.isNull(6) ? null : cursor.getLong(6),
                    PastCurrentEnum.fromInt(cursor.getInt(7)),
                    dateCreated)); // 8
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }

    public static ArrayList<ClassEntry> _getEntriesByPastCurrent(SQLiteDatabase db, PastCurrentEnum pastCurrent) {
        ArrayList<ClassEntry> entries = new ArrayList<>();
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_TIME_DB_TEMPLATE, ApolloDbAdapter.getAppContext().getResources().getConfiguration().locale);
        Date dateCreated;
        Cursor cursor = db.query(TABLE_NAME + " AS c LEFT OUTER JOIN " +
                        AcademicTermContract.TABLE_NAME + " AS at ON c." + ClassEntry.ACADEMIC_TERM_ID + "=at." + AcademicTermContract.AcademicTermEntry._ID +
                        " LEFT OUTER JOIN " +
                        ClassPasswordContract.TABLE_NAME + " AS cp ON c." + ClassEntry._ID + "=cp." + ClassPasswordContract.ClassPasswordEntry.CLASS_ID,
                new String[]{"c." + ClassEntry._ID, // 0
                        "c." + ClassEntry.CODE, // 1
                        "c." + ClassEntry.DESCRIPTION, // 2 nullable
                        "c." + ClassEntry.ACADEMIC_TERM_ID, // 3 nullable
                        "at." + AcademicTermContract.AcademicTermEntry.DESCRIPTION, // 4
                        "at." + AcademicTermContract.AcademicTermEntry.COLOR, // 5
                        "c." + ClassEntry.YEAR, // 6 nullable
                        "c." + ClassEntry.CURRENT, // 7
                        "c." + ClassEntry.DATE_CREATED,
                        "cp." + ClassPasswordContract.ClassPasswordEntry._ID}, // 8 nullable
                "c." + ClassEntry.CURRENT + "=?",
                new String[]{String.valueOf(pastCurrent.getValue())},
                null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            try {
                dateCreated = sdf.parse(cursor.getString(8));
            }
            catch(Exception e) {
                dateCreated = null;
            }
            ClassEntry entry = new ClassEntry(cursor.getLong(0),
                    cursor.getString(1),
                    cursor.isNull(2) ? null : cursor.getString(2),
                    cursor.isNull(3) ? null : new AcademicTermContract.AcademicTermEntry(cursor.getLong(3), cursor.getString(4), cursor.isNull(5) ? null : cursor.getString(5)),
                    cursor.isNull(6) ? null : cursor.getLong(6),
                    PastCurrentEnum.fromInt(cursor.getInt(7)),
                    dateCreated);
            entry.setLocked(!cursor.isNull(9));
            entries.add(entry); // 8
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }

    public static long _insertDummyClass(SQLiteDatabase db, int classResourceId, Context context) {
        @StyleableRes final int CODE_INDEX = 0;
        @StyleableRes final int DESCRIPTION_INDEX = 1;
        @StyleableRes final int ACADEMIC_TERM_INDEX = 2;
        @StyleableRes final int YEAR_INDEX = 3;
        @StyleableRes final int PAST_CURRENT_INDEX = 4;
        @StyleableRes final int DATE_CREATED_INDEX = 5;
        final SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormat.DATE_TIME_DB_TEMPLATE, context.getResources().getConfiguration().locale);
        Resources res = context.getResources();
        TypedArray ta = res.obtainTypedArray(classResourceId);
        String code, description, academicTerm;
        code = res.getString(ta.getResourceId(CODE_INDEX, 0));
        description = res.getString(ta.getResourceId(DESCRIPTION_INDEX, 0));
        academicTerm = res.getString(ta.getResourceId(ACADEMIC_TERM_INDEX, 0));
        Long year;
        try {
            year = Long.parseLong(res.getString(ta.getResourceId(YEAR_INDEX, 0)));
        }
        catch(Exception e) {
            year = null;
        }
        int pastCurrent = res.getInteger(ta.getResourceId(PAST_CURRENT_INDEX, 0));
        Date dateCreated;
        try {
            dateCreated = sdf.parse(res.getString(ta.getResourceId(DATE_CREATED_INDEX, 0)));
        }
        catch(Exception e) {
            dateCreated = null;
        }
        ta.recycle();
        return _insert(db, code,
                description,
                AcademicTermContract._getEntryByDescription(db, academicTerm),
                year,
                PastCurrentEnum.fromInt(pastCurrent),
                dateCreated);
    }

    public static class ClassEntry implements BaseColumns, Serializable {

        public static final String CODE = "Code";
        public static final String DESCRIPTION = "Description";
        public static final String ACADEMIC_TERM_ID = "AcademicTermId";
        public static final String YEAR = "Year";
        public static final String CURRENT = "Current";
        public static final String DATE_CREATED = "DateCreated";

        private long mId; // 0
        private String mCode; // 1
        private String mDescription; // 2 nullable
        private AcademicTermContract.AcademicTermEntry mAcademicTerm; // 3, 4, 5 nullable
        private Long mYear; // 6 nullable
        private PastCurrentEnum mPastCurrent; // 7
        private Date mDateCreated; // 8 nullable
        private boolean mLocked; // hack for getting password

        public ClassEntry(long id, String code, String description, AcademicTermContract.AcademicTermEntry academicTerm, Long year, PastCurrentEnum pastCurrent, Date dateCreated) {
            mId = id;
            mCode = code;
            mDescription = description;
            mAcademicTerm = academicTerm;
            mYear = year;
            mPastCurrent = pastCurrent;
            mDateCreated = dateCreated;
            mLocked = false;
        }

        public long getId() {
            return mId;
        }

        public void setId(long id) {
            mId = id;
        }

        public String getCode() {
            return mCode;
        }

        public void setCode(String code) {
            mCode = code;
        }

        public String getDescription() {
            return mDescription;
        }

        public void setDescription(String description) {
            mDescription = description;
        }

        public AcademicTermContract.AcademicTermEntry getAcademicTerm() {
            return mAcademicTerm;
        }

        public void setAcademicTerm(AcademicTermContract.AcademicTermEntry academicTerm) {
            mAcademicTerm = academicTerm;
        }

        public Long getYear() {
            return mYear;
        }

        public void setYear(Long year) {
            mYear = year;
        }

        public PastCurrentEnum getPastCurrent() {
            return mPastCurrent;
        }

        public void setPastCurrent(PastCurrentEnum pastCurrent) {
            mPastCurrent = pastCurrent;
        }

        public boolean isCurrent() {
            return mPastCurrent == PastCurrentEnum.CURRENT;
        }

        public Date getDateCreated() {
            return mDateCreated;
        }

        public void setDateCreated(Date dateCreated) {
            mDateCreated = dateCreated;
        }

        public boolean isLocked() {
            return mLocked;
        }

        public void setLocked(boolean locked) {
            mLocked = locked;
        }

        @Override
        public String toString() {
            return mCode + " - " + mDescription;
        }

        public String getTitle() {
            StringBuilder sb = new StringBuilder(mCode);
            if(!StringUtils.isBlank(mDescription))
                sb.append(" - " + mDescription);
            return sb.toString();
        }

        public String getAcademicTermYear() {
            StringBuilder sb = new StringBuilder();
            if(mAcademicTerm != null) {
                String academicTerm = mAcademicTerm.getDescription();
                if(!StringUtils.isBlank(academicTerm))
                    sb.append(academicTerm);
            }
            if(mYear != null) {
                if(sb.length() > 0)
                    sb.append(" ");
                sb.append(mYear);
            }
            return sb.toString();
        }

        @Override
        public boolean equals(Object object) {
            if(!(object instanceof ClassEntry))
                return false;
            if(object == this)
                return true;
            ClassEntry entry = (ClassEntry) object;
            return new EqualsBuilder()
                    .append(mId, entry.mId).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(11, 13)
                    .append(mId).toHashCode();
        }
    }
}
