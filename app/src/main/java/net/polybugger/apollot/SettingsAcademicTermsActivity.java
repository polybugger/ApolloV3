package net.polybugger.apollot;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
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

public class SettingsAcademicTermsActivity extends AppCompatActivity implements SettingsAcademicTermsActivityFragment.Listener {

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

        FragmentManager fm = getSupportFragmentManager();
        if(fm.findFragmentByTag(SettingsAcademicTermsActivityFragment.TAG) == null)
            fm.beginTransaction().add(SettingsAcademicTermsActivityFragment.newInstance(), SettingsAcademicTermsActivityFragment.TAG).commit();
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
    public void onPreExecute() {

    }

    @Override
    public void onProgressUpdate(int percent) {

    }

    @Override
    public void onCancelled() {

    }

    @Override
    public void onPostExecute(ArrayList<AcademicTermContract.AcademicTermEntry> arrayList) {
        mAdapter.setArrayList(arrayList);
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
}
