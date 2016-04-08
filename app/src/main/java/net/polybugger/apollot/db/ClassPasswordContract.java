package net.polybugger.apollot.db;

import android.provider.BaseColumns;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ClassPasswordContract {

    public static final String TABLE_NAME = "ClassPasswords";
    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
            ClassPasswordEntry._ID + " INTEGER PRIMARY KEY, " +
            ClassPasswordEntry.CLASS_ID + " INTEGER NOT NULL UNIQUE REFERENCES " +
                ClassContract.TABLE_NAME + " (" + ClassContract.ClassEntry._ID + "), " +
            ClassPasswordEntry.PASSWORD + " TEXT NOT NULL)";
    public static final String DELETE_ALL_SQL = "DELETE FROM " + TABLE_NAME;

    private ClassPasswordContract() { }

    public static class ClassPasswordEntry implements BaseColumns {

        public static final String CLASS_ID = "ClassId";
        public static final String PASSWORD = "Password";

        private long mId; // 0
        private long mClassId; // 1
        private String mPassword; // 2

        public ClassPasswordEntry(long id, long classId, String password) {
            mId = id;
            mClassId = classId;
            mPassword = password;
        }

        public long getId() {
            return mId;
        }

        public void setId(long id) {
            mId = id;
        }

        public long getClassId() {
            return mClassId;
        }

        public void setClassId(long classId) {
            mClassId = classId;
        }

        public String getPassword() {
            return mPassword;
        }

        public void setPassword(String password) {
            mPassword = password;
        }

        @Override
        public boolean equals(Object object) {
            if(!(object instanceof ClassPasswordEntry))
                return false;
            if(object == this)
                return true;
            ClassPasswordEntry entry = (ClassPasswordEntry) object;
            return new EqualsBuilder()
                    .append(mId, entry.mId)
                    .append(mClassId, entry.mClassId).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(41, 43)
                    .append(mId)
                    .append(mClassId).toHashCode();
        }
    }
}
