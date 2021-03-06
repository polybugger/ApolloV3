package net.polybugger.apollot;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.Date;

import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassContract;
import net.polybugger.apollot.db.ClassGradeBreakdownContract;
import net.polybugger.apollot.db.ClassItemContract;
import net.polybugger.apollot.db.ClassNoteContract;
import net.polybugger.apollot.db.ClassPasswordContract;
import net.polybugger.apollot.db.ClassScheduleContract;
import net.polybugger.apollot.db.ClassStudentContract;
import net.polybugger.apollot.db.StudentContract;

import org.apache.commons.lang3.StringUtils;

public class ClassActivityFragment extends Fragment {

    public interface Listener {
        void onUnlockClass(ClassContract.ClassEntry _class, boolean passwordMatched);
        void onLockClass(ClassContract.ClassEntry _class);
        void onUpdateClass(ClassContract.ClassEntry _class, int rowsUpdated);
        void onDeleteClass(ClassContract.ClassEntry _class, int rowsDeleted);
        void onGetClassSchedules(ArrayList<ClassScheduleContract.ClassScheduleEntry> classSchedules, String fragmentTag);
        void onInsertClassSchedule(ClassScheduleContract.ClassScheduleEntry classSchedule, long id, String fragmentTag);
        void onUpdateClassSchedule(ClassScheduleContract.ClassScheduleEntry classSchedule, int rowsUpdated, String fragmentTag);
        void onDeleteClassSchedule(ClassScheduleContract.ClassScheduleEntry classSchedule, int rowsDeleted, String fragmentTag);
        void onGetGradeBreakdowns(ArrayList<ClassGradeBreakdownContract.ClassGradeBreakdownEntry> gradeBreakdowns, String fragmentTag);
        void onInsertClassGradeBreakdown(ClassGradeBreakdownContract.ClassGradeBreakdownEntry classGradeBreakdown, long id, String fragmentTag);
        void onUpdateClassGradeBreakdown(ClassGradeBreakdownContract.ClassGradeBreakdownEntry classGradeBreakdown, int rowsUpdated, String fragmentTag);
        void onDeleteClassGradeBreakdown(ClassGradeBreakdownContract.ClassGradeBreakdownEntry classGradeBreakdown, int rowsDeleted, String fragmentTag);
        void onInsertClassNote(ClassNoteContract.ClassNoteEntry classNote, long id, String fragmentTag);
        void onUpdateClassNote(ClassNoteContract.ClassNoteEntry classNote, int rowsUpdated, String fragmentTag);
        void onDeleteClassNote(ClassNoteContract.ClassNoteEntry classNote, int rowsDeleted, String fragmentTag);
        void onGetClassNotes(ArrayList<ClassNoteContract.ClassNoteEntry> classNotes, String fragmentTag);
        void onRequeryClass(ClassContract.ClassEntry _class);
        void onGetClassItemsSummary(ArrayList<ClassItemsFragment.ClassItemSummary> arrayList, String fragmentTag);
        void onInsertClassItem(ClassItemContract.ClassItemEntry classItem, long id, String fragmentTag);
        void onGetClassStudentsSummary(ArrayList<ClassStudentsFragment.ClassStudentSummary> arrayList, String fragmentTag);
        void onGetClassItemSummary(ClassItemsFragment.ClassItemSummary classItemSummary, String fragmentTag);
        void onInsertClassStudent(ClassStudentContract.ClassStudentEntry classStudent, long id, String fragmentTag);
        void onGetExistingStudents(ArrayList<StudentContract.StudentEntry> arrayList, String fragmentTag);
        void onInsertExistingStudents(String fragmentTag);
    }

    public static final String TAG = "net.polybugger.apollot.class_activity_fragment";

    private Listener mListener;

    public static ClassActivityFragment newInstance() {
        return new ClassActivityFragment();
    }

    public ClassActivityFragment() {
        setRetainInstance(true);
    }

    public void lockClass(ClassContract.ClassEntry _class, String password) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClass = _class;
        params.mPassword = password;
        new LockClassAsyncTask().execute(params);
    }

    public void unlockClass(ClassContract.ClassEntry _class, String password) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClass = _class;
        params.mPassword = password;
        new UnlockClassAsyncTask().execute(params);
    }

    public void updateClass(ClassContract.ClassEntry _class) {
        new UpdateClassAsyncTask().execute(_class);
    }

    public void deleteClass(ClassContract.ClassEntry _class) {
        new DeleteClassAsyncTask().execute(_class);
    }

    public void insertClassSchedule(ClassScheduleContract.ClassScheduleEntry classSchedule, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClassSchedule = classSchedule;
        params.mFragmentTag = fragmentTag;
        new InsertClassScheduleAsyncTask().execute(params);
    }

    public void updateClassSchedule(ClassScheduleContract.ClassScheduleEntry classSchedule, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClassSchedule = classSchedule;
        params.mFragmentTag = fragmentTag;
        new UpdateClassScheduleAsyncTask().execute(params);
    }

    public void deleteClassSchedule(ClassScheduleContract.ClassScheduleEntry classSchedule, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClassSchedule = classSchedule;
        params.mFragmentTag = fragmentTag;
        new DeleteClassScheduleAsyncTask().execute(params);
    }

    public void insertClassGradeBreakdown(ClassGradeBreakdownContract.ClassGradeBreakdownEntry classGradeBreakdown, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClassGradeBreakdown = classGradeBreakdown;
        params.mFragmentTag = fragmentTag;
        new InsertClassGradeBreakdownAsyncTask().execute(params);
    }

    public void updateClassGradeBreakdown(ClassGradeBreakdownContract.ClassGradeBreakdownEntry classGradeBreakdown, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClassGradeBreakdown = classGradeBreakdown;
        params.mFragmentTag = fragmentTag;
        new UpdateClassGradeBreakdownAsyncTask().execute(params);
    }

    public void deleteClassGradeBreakdown(ClassGradeBreakdownContract.ClassGradeBreakdownEntry classGradeBreakdown, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClassGradeBreakdown = classGradeBreakdown;
        params.mFragmentTag = fragmentTag;
        new DeleteClassGradeBreakdownAsyncTask().execute(params);
    }

    public void insertClassNote(ClassNoteContract.ClassNoteEntry classNote, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClassNote = classNote;
        params.mFragmentTag = fragmentTag;
        new InsertClassNoteAsyncTask().execute(params);
    }

    public void updateClassNote(ClassNoteContract.ClassNoteEntry classNote, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClassNote = classNote;
        params.mFragmentTag = fragmentTag;
        new UpdateClassNoteAsyncTask().execute(params);
    }

    public void deleteClassNote(ClassNoteContract.ClassNoteEntry classNote, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClassNote = classNote;
        params.mFragmentTag = fragmentTag;
        new DeleteClassNoteAsyncTask().execute(params);
    }

    public void getClassSchedules(ClassContract.ClassEntry _class, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClass = _class;
        params.mFragmentTag = fragmentTag;
        new GetClassSchedulesAsyncTask().execute(params);
    }

    public void getGradeBreakdowns(ClassContract.ClassEntry _class, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClass = _class;
        params.mFragmentTag = fragmentTag;
        new GetGradeBreakdownsAsyncTask().execute(params);
    }

    public void getClassNotes(ClassContract.ClassEntry _class, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClass = _class;
        params.mFragmentTag = fragmentTag;
        new GetClassNotesAsyncTask().execute(params);
    }

    public void requeryClass(ClassContract.ClassEntry _class) {
        new RequeryClassAsyncTask().execute(_class);
    }

    public void getClassItemsSummary(ClassContract.ClassEntry _class, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClass = _class;
        params.mFragmentTag = fragmentTag;
        new GetClassItemsSummaryAsyncTask().execute(params);
    }

    public void insertClassItem(ClassItemContract.ClassItemEntry classItem, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClassItem = classItem;
        params.mFragmentTag = fragmentTag;
        new InsertClassItemAsyncTask().execute(params);
    }

    public void getClassStudentsSummary(ClassContract.ClassEntry _class, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClass = _class;
        params.mFragmentTag = fragmentTag;
        new GetClassStudentsSummaryAsyncTask().execute(params);
    }

    public void getClassItemSummary(ClassItemContract.ClassItemEntry classItem, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClassItem = classItem;
        params.mFragmentTag = fragmentTag;
        new GetClassItemSummaryAsyncTask().execute(params);
    }

    public void insertClassStudent(ClassStudentContract.ClassStudentEntry classStudent, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClassStudent = classStudent;
        params.mFragmentTag = fragmentTag;
        new InsertClassStudentAsyncTask().execute(params);
    }

    public void insertExistingStudents(ArrayList<Long> studentIds, ClassContract.ClassEntry _class, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mStudentIds = studentIds;
        params.mClass = _class;
        params.mFragmentTag = fragmentTag;
        new InsertExistingStudentsAsyncTask().execute(params);
    }

    public void getExistingStudents(ClassContract.ClassEntry _class, String fragmentTag) {
        AsyncTaskParams params = new AsyncTaskParams();
        params.mClass = _class;
        params.mFragmentTag = fragmentTag;
        new GetExistingStudentsAsyncTask().execute(params);
    }

    private class InsertClassScheduleAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClassSchedule = params[0].mClassSchedule;
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mId = ClassScheduleContract._insert(db, result.mClassSchedule.getClassId(), result.mClassSchedule.getTimeStart(), result.mClassSchedule.getTimeEnd(), result.mClassSchedule.getDays(), result.mClassSchedule.getRoom(), result.mClassSchedule.getBuilding(), result.mClassSchedule.getCampus());
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null)
                mListener.onInsertClassSchedule(result.mClassSchedule, result.mId, result.mFragmentTag);
        }
    }

    private class UpdateClassScheduleAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClassSchedule = params[0].mClassSchedule;
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mRowsUpdated = ClassScheduleContract._update(db, result.mClassSchedule.getId(), result.mClassSchedule.getClassId(), result.mClassSchedule.getTimeStart(), result.mClassSchedule.getTimeEnd(), result.mClassSchedule.getDays(), result.mClassSchedule.getRoom(), result.mClassSchedule.getBuilding(), result.mClassSchedule.getCampus());
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null)
                mListener.onUpdateClassSchedule(result.mClassSchedule, result.mRowsUpdated, result.mFragmentTag);
        }
    }

    private class DeleteClassScheduleAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClassSchedule = params[0].mClassSchedule;
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mRowsDeleted = ClassScheduleContract._delete(db, result.mClassSchedule.getId());
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null)
                mListener.onDeleteClassSchedule(result.mClassSchedule, result.mRowsDeleted, result.mFragmentTag);
        }
    }

    private class InsertClassGradeBreakdownAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClassGradeBreakdown = params[0].mClassGradeBreakdown;
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mId = ClassGradeBreakdownContract._insert(db, result.mClassGradeBreakdown.getClassId(), result.mClassGradeBreakdown.getItemType(), result.mClassGradeBreakdown.getPercentage());
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null) {
                mListener.onInsertClassGradeBreakdown(result.mClassGradeBreakdown, result.mId, result.mFragmentTag);
            }
        }
    }

    private class UpdateClassGradeBreakdownAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClassGradeBreakdown = params[0].mClassGradeBreakdown;
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mRowsUpdated = ClassGradeBreakdownContract._update(db, result.mClassGradeBreakdown.getId(), result.mClassGradeBreakdown.getClassId(), result.mClassGradeBreakdown.getItemType(), result.mClassGradeBreakdown.getPercentage());
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null) {
                mListener.onUpdateClassGradeBreakdown(result.mClassGradeBreakdown, result.mRowsUpdated, result.mFragmentTag);
            }
        }
    }

    private class DeleteClassGradeBreakdownAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClassGradeBreakdown = params[0].mClassGradeBreakdown;
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mRowsDeleted = ClassGradeBreakdownContract._delete(db, result.mClassGradeBreakdown.getId(), result.mClassGradeBreakdown.getClassId());
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null)
                mListener.onDeleteClassGradeBreakdown(result.mClassGradeBreakdown, result.mRowsDeleted, result.mFragmentTag);
        }
    }

    private class InsertClassNoteAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClassNote = params[0].mClassNote;
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mId = ClassNoteContract._insert(db, result.mClassNote.getClassId(), result.mClassNote.getNote(), result.mClassNote.getDateCreated());
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null)
                mListener.onInsertClassNote(result.mClassNote, result.mId, result.mFragmentTag);
        }
    }

    private class UpdateClassNoteAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClassNote = params[0].mClassNote;
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mRowsUpdated = ClassNoteContract._update(db, result.mClassNote.getId(), result.mClassNote.getClassId(), result.mClassNote.getNote(), result.mClassNote.getDateCreated());
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null)
                mListener.onUpdateClassNote(result.mClassNote, result.mRowsUpdated, result.mFragmentTag);
        }
    }

    private class DeleteClassNoteAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClassNote = params[0].mClassNote;
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mRowsDeleted = ClassNoteContract._delete(db, result.mClassNote.getId());
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null) {
                mListener.onDeleteClassNote(result.mClassNote, result.mRowsDeleted, result.mFragmentTag);
            }
        }
    }

    private class LockClassAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClass = params[0].mClass;
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mClass.setLocked(ClassPasswordContract._insert(db, result.mClass.getId(), params[0].mPassword) > 0);
            ApolloDbAdapter.close();
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null)
                mListener.onLockClass(result.mClass);
        }
    }

    private class UnlockClassAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClass = params[0].mClass;
            SQLiteDatabase db = ApolloDbAdapter.open();
            ClassPasswordContract.ClassPasswordEntry classPassword = ClassPasswordContract._getEntryByClassId(db, result.mClass.getId());
            result.mPasswordMatched = StringUtils.equals(classPassword.getPassword(), params[0].mPassword);
            if(result.mPasswordMatched)
                result.mClass.setLocked(!(ClassPasswordContract._delete(db, classPassword.getId()) > 0));
            ApolloDbAdapter.close();
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null) {
                mListener.onUnlockClass(result.mClass, result.mPasswordMatched);
            }
        }
    }

    private class UpdateClassAsyncTask extends AsyncTask<ClassContract.ClassEntry, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(ClassContract.ClassEntry... _class) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClass = _class[0];
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mRowsUpdated = ClassContract._update(db, result.mClass.getId(), result.mClass.getCode(), result.mClass.getDescription(), result.mClass.getAcademicTerm(), result.mClass.getYear(), result.mClass.getPastCurrent(), result.mClass.getDateCreated());
            ApolloDbAdapter.close();
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null)
                mListener.onUpdateClass(result.mClass, result.mRowsUpdated);
        }
    }

    private class DeleteClassAsyncTask extends AsyncTask<ClassContract.ClassEntry, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(ClassContract.ClassEntry... _class) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClass = _class[0];
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mRowsDeleted = ClassContract._delete(db, result.mClass.getId());
            ApolloDbAdapter.close();
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null)
                mListener.onDeleteClass(result.mClass, result.mRowsDeleted);
        }
    }

    private class GetClassSchedulesAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mClassSchedules = ClassScheduleContract._getEntriesByClassId(db, params[0].mClass.getId());
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null)
                mListener.onGetClassSchedules(result.mClassSchedules, result.mFragmentTag);
        }
    }

    private class GetGradeBreakdownsAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mGradeBreakdowns = ClassGradeBreakdownContract._getEntries(db, params[0].mClass.getId());
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null)
                mListener.onGetGradeBreakdowns(result.mGradeBreakdowns, result.mFragmentTag);
        }
    }

    private class GetClassNotesAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mClassNotes = ClassNoteContract._getEntriesByClassId(db, params[0].mClass.getId());
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null)
                mListener.onGetClassNotes(result.mClassNotes, result.mFragmentTag);
        }
    }

    private class RequeryClassAsyncTask extends AsyncTask<ClassContract.ClassEntry, Integer, ClassContract.ClassEntry> {

        @Override
        protected ClassContract.ClassEntry doInBackground(ClassContract.ClassEntry... _class) {
            SQLiteDatabase db = ApolloDbAdapter.open();
            ClassContract.ClassEntry requeryClass = ClassContract._getEntry(db, _class[0].getId());
            ApolloDbAdapter.close();
            return requeryClass;
        }

        @Override
        protected void onPostExecute(ClassContract.ClassEntry _class) {
            if(mListener != null)
                mListener.onRequeryClass(_class);
        }
    }

    private class GetClassItemsSummaryAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClass = params[0].mClass;
            result.mClassItemsSummary = new ArrayList<>();

            SQLiteDatabase db = ApolloDbAdapter.open();
            ArrayList<ClassItemContract.ClassItemEntry> classItems = ClassItemContract._getEntries(db, result.mClass.getId());
            for(ClassItemContract.ClassItemEntry classItem : classItems) {
                ClassItemsFragment.ClassItemSummary classItemSummary = new ClassItemsFragment.ClassItemSummary(classItem);
                // TODO fetch class item records for summary
                result.mClassItemsSummary.add(classItemSummary);
            }
            ApolloDbAdapter.close();

            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null)
                mListener.onGetClassItemsSummary(result.mClassItemsSummary, result.mFragmentTag);
        }
    }

    private class GetClassItemSummaryAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClassItem = params[0].mClassItem;
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mClassItem = ClassItemContract._getEntry(db, result.mClassItem.getId(), result.mClassItem.getClassId());
            result.mClassItemSummary = new ClassItemsFragment.ClassItemSummary(result.mClassItem);
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null)
                mListener.onGetClassItemSummary(result.mClassItemSummary, result.mFragmentTag);
        }
    }

    private class InsertClassItemAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClassItem = params[0].mClassItem;
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mId = ClassItemContract._insert(db, result.mClassItem.getClassId(), result.mClassItem.getDescription(), result.mClassItem.getItemType(), result.mClassItem.getItemDate(), result.mClassItem.isCheckAttendance(), result.mClassItem.isRecordScores(), result.mClassItem.getPerfectScore(), result.mClassItem.isRecordSubmissions(), result.mClassItem.getSubmissionDueDate());
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null)
                mListener.onInsertClassItem(result.mClassItem, result.mId, result.mFragmentTag);
        }
    }

    private class GetClassStudentsSummaryAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClass = params[0].mClass;
            result.mClassStudentsSummary = new ArrayList<>();
            SQLiteDatabase db = ApolloDbAdapter.open();
            ArrayList<ClassStudentContract.ClassStudentEntry> classStudents = ClassStudentContract._getEntries(db, result.mClass.getId());
            for(ClassStudentContract.ClassStudentEntry classStudent : classStudents) {
                ClassStudentsFragment.ClassStudentSummary classStudentSummary = new ClassStudentsFragment.ClassStudentSummary(classStudent);
                // TODO fetch class item records for summary
                result.mClassStudentsSummary.add(classStudentSummary);
            }
            ApolloDbAdapter.close();

            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null)
                mListener.onGetClassStudentsSummary(result.mClassStudentsSummary, result.mFragmentTag);
        }
    }

    private class InsertClassStudentAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClassStudent = params[0].mClassStudent;
            StudentContract.StudentEntry student = result.mClassStudent.getStudent();
            SQLiteDatabase db = ApolloDbAdapter.open();
            long studentId = StudentContract._insert(db, student.getLastName(), student.getFirstName(), student.getMiddleName(), student.getGender(), student.getEmailAddress(), student.getContactNumber());
            student.setId(studentId);
            result.mId = ClassStudentContract._insert(db, result.mClassStudent.getClassId(), studentId, result.mClassStudent.getDateCreated());
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null)
                mListener.onInsertClassStudent(result.mClassStudent, result.mId, result.mFragmentTag);
        }
    }

    private class GetExistingStudentsAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClass = params[0].mClass;
            result.mExistingStudents = new ArrayList<>();
            SQLiteDatabase db = ApolloDbAdapter.open();
            result.mExistingStudents = StudentContract._getEntriesNotInClass(db, result.mClass.getId());
            ApolloDbAdapter.close();

            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null)
                mListener.onGetExistingStudents(result.mExistingStudents, result.mFragmentTag);
        }
    }

    private class InsertExistingStudentsAsyncTask extends AsyncTask<AsyncTaskParams, Integer, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AsyncTaskParams... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            result.mClass = params[0].mClass;
            long classId = result.mClass.getId();
            SQLiteDatabase db = ApolloDbAdapter.open();
            for(Long studentId : params[0].mStudentIds)
                result.mId = ClassStudentContract._insert(db, classId, studentId, new Date());
            ApolloDbAdapter.close();
            result.mFragmentTag = params[0].mFragmentTag;
            return result;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if(mListener != null)
                mListener.onInsertExistingStudents(result.mFragmentTag);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Activity) {
            try {
                mListener = (Listener) context;
            }
            catch(ClassCastException e) {
                throw new ClassCastException(context.toString() + " must implement " + Listener.class.toString());
            }
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    private class AsyncTaskResult {
        public ClassContract.ClassEntry mClass;
        public ClassScheduleContract.ClassScheduleEntry mClassSchedule;
        public ClassGradeBreakdownContract.ClassGradeBreakdownEntry mClassGradeBreakdown;
        public ClassNoteContract.ClassNoteEntry mClassNote;
        public ClassItemContract.ClassItemEntry mClassItem;
        public ClassItemsFragment.ClassItemSummary mClassItemSummary;
        public ClassStudentContract.ClassStudentEntry mClassStudent;
        public boolean mPasswordMatched;
        public int mRowsUpdated;
        public int mRowsDeleted;
        public long mId;
        public ArrayList<ClassScheduleContract.ClassScheduleEntry> mClassSchedules;
        public ArrayList<ClassGradeBreakdownContract.ClassGradeBreakdownEntry> mGradeBreakdowns;
        public ArrayList<ClassNoteContract.ClassNoteEntry> mClassNotes;
        public ArrayList<ClassItemsFragment.ClassItemSummary> mClassItemsSummary;
        public ArrayList<StudentContract.StudentEntry> mExistingStudents;
        public ArrayList<ClassStudentsFragment.ClassStudentSummary> mClassStudentsSummary;
        public String mFragmentTag;
        public AsyncTaskResult() { }
    }

    private class AsyncTaskParams {
        public ClassContract.ClassEntry mClass;
        public ClassScheduleContract.ClassScheduleEntry mClassSchedule;
        public ClassNoteContract.ClassNoteEntry mClassNote;
        public ClassGradeBreakdownContract.ClassGradeBreakdownEntry mClassGradeBreakdown;
        public ClassItemContract.ClassItemEntry mClassItem;
        public ClassStudentContract.ClassStudentEntry mClassStudent;
        public ArrayList<Long> mStudentIds;
        public String mPassword;
        public String mFragmentTag;

        public AsyncTaskParams() { }
    }

}
