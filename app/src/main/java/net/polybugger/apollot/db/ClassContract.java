package net.polybugger.apollot.db;

import android.provider.BaseColumns;

import java.io.Serializable;
import java.util.Date;

public class ClassContract {

    public static final String TABLE_NAME = "Classes";
    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
            ClassEntry._ID + " INTEGER PRIMARY KEY, " +
            ClassEntry.CODE + " TEXT NOT NULL, " +
            ClassEntry.DESCRIPTION + " TEXT NULL, " +
            ClassEntry.ACADEMIC_TERM_ID + " INTEGER NULL REFERENCES " +
                AcademicTermContract.TABLE_NAME + " (" + AcademicTermContract.AcademicTermEntry._ID + "), " +
            ClassEntry.YEAR + " INTEGER NULL, " +
            ClassEntry.CURRENT + " INTEGER NOT NULL DEFAULT 1, " +
            ClassEntry.DATE_CREATED + " TEXT NULL)";

    private ClassContract() { }

    public static class ClassEntry implements BaseColumns, Serializable {

        public static final String CODE = "Code";
        public static final String DESCRIPTION = "Description";
        public static final String ACADEMIC_TERM_ID = "AcademicTermId";
        public static final String YEAR = "Year";
        public static final String CURRENT = "Current";
        public static final String DATE_CREATED = "DateCreated";

        private long mId;
        private String mCode;
        private String mDescription;
        private AcademicTermContract.AcademicTermEntry mAcademicTerm;
        private Integer mYear; // nullable
        private PastCurrentEnum mPastCurrent;
        private Date mDateCreated;

        public ClassEntry(long id, String code, String description, AcademicTermContract.AcademicTermEntry academicTerm, Integer year, PastCurrentEnum pastCurrent, Date dateCreated) {
            mId = id;
            mCode = code;
            mDescription = description;
            mAcademicTerm = academicTerm;
            mYear = year;
            mPastCurrent = pastCurrent;
            mDateCreated = dateCreated;
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

        public Integer getYear() {
            return mYear;
        }

        public void setYear(Integer year) {
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

        @Override
        public String toString() {
            return mCode + " - " + mDescription;
        }

        public boolean equals(ClassEntry entry) {
            if(entry != null && entry.mId == mId)
                return true;
            return false;
        }

        @Override
        public boolean equals(Object object) {
            ClassEntry entry;
            if(object != null) {
                try {
                    entry = (ClassEntry) object;
                    if(entry.mId == mId)
                        return true;
                }
                catch(ClassCastException e) {
                    throw new ClassCastException(object.toString() + " must be an instance of " + ClassEntry.class.toString());
                }
            }
            return false;
        }
    }
}
