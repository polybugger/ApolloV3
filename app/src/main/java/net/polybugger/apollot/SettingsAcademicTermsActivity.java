package net.polybugger.apollot;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.polybugger.apollot.db.AcademicTermContract;
import net.polybugger.apollot.db.ApolloDbAdapter;

import java.util.ArrayList;

public class SettingsAcademicTermsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<ArrayList<AcademicTermContract.AcademicTermEntry>> {

    private final static int GET_ID = 1;

    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApolloDbAdapter.setAppContext(getApplicationContext());

        setContentView(R.layout.activity_settings_academic_terms);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        getSupportLoaderManager().initLoader(GET_ID, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public android.support.v4.content.Loader<ArrayList<AcademicTermContract.AcademicTermEntry>> onCreateLoader(int id, Bundle args) {
        switch(id) {
            case GET_ID:
                return new GetLoader(SettingsAcademicTermsActivity.this);
        }
        return null;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<ArrayList<AcademicTermContract.AcademicTermEntry>> loader, ArrayList<AcademicTermContract.AcademicTermEntry> arrayList) {
        mAdapter.setArrayList(arrayList);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<ArrayList<AcademicTermContract.AcademicTermEntry>> loader) {
        mAdapter.setArrayList(new ArrayList<AcademicTermContract.AcademicTermEntry>());
    }

    public static class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private ArrayList<AcademicTermContract.AcademicTermEntry> mArrayList;

        public Adapter() {
            mArrayList = new ArrayList<>();
        }

        public void setArrayList(ArrayList<AcademicTermContract.AcademicTermEntry> arrayList) {
            mArrayList = arrayList;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_settings_academic_terms, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            AcademicTermContract.AcademicTermEntry entry = mArrayList.get(position);
            int color;
            try {
                color = Color.parseColor(entry.getColor());
            }
            catch(Exception e) {
                color = Color.TRANSPARENT;
            }
            GradientDrawable bg = (GradientDrawable) holder.mBackgroundLayout.getBackground();
            bg.setColor(color);
            holder.mTextView.setText(entry.getDescription());
            holder.mImageButton.setTag(entry);
        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            protected LinearLayout mBackgroundLayout;
            protected LinearLayout mClickableLayout;
            protected TextView mTextView;
            protected ImageButton mImageButton;

            public ViewHolder(View itemView) {
                super(itemView);
                mBackgroundLayout = (LinearLayout) itemView.findViewById(R.id.background_layout);
                mClickableLayout = (LinearLayout) itemView.findViewById(R.id.clickable_layout);
                mClickableLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                mTextView =  (TextView) itemView.findViewById(R.id.text_view);
                mImageButton = (ImageButton) itemView.findViewById(R.id.image_button);
                mImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AcademicTermContract.AcademicTermEntry entry = (AcademicTermContract.AcademicTermEntry) v.getTag();
                        SQLiteDatabase db = ApolloDbAdapter.open();
                        AcademicTermContract._delete(db, entry.getId());
                        ApolloDbAdapter.close();

                    }
                });
            }
        }
    }

    public static class GetLoader extends AsyncTaskLoader<ArrayList<AcademicTermContract.AcademicTermEntry>> {

        private ArrayList<AcademicTermContract.AcademicTermEntry> mArrayList;

        public GetLoader(Context context) {
            super(context);
        }

        @Override
        public ArrayList<AcademicTermContract.AcademicTermEntry> loadInBackground() {
            SQLiteDatabase db = ApolloDbAdapter.open();
            mArrayList = AcademicTermContract._getEntries(db);
            ApolloDbAdapter.close();
            return mArrayList;
        }

        // called to deliver result to UI thread, via callbacks
        @Override
        public void deliverResult(ArrayList<AcademicTermContract.AcademicTermEntry> arrayList) {
            if(isReset())
                return;

            if(isStarted())
                super.deliverResult(arrayList);
        }

        // called if task is cancelled, dispose of the result
        @Override
        public void onCanceled(ArrayList<AcademicTermContract.AcademicTermEntry> arrayList) {
            super.onCanceled(arrayList);
        }

        // called when task is going to be started, forceLoad starts the tasks
        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if(mArrayList != null)
                deliverResult(mArrayList);
            if(takeContentChanged() || mArrayList == null)
                forceLoad();
        }

        // called when task is going to be stopped, maybe after task has done executing
        @Override
        public void onStopLoading() {
            super.onStopLoading();
            cancelLoad();
        }

        // called when task is going to be reset, maybe configuration change?
        @Override
        protected void onReset() {
            super.onReset();
            onStopLoading();
            mArrayList = null;
        }
    }
}
