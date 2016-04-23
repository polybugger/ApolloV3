package net.polybugger.apollot;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.ArrayList;

import net.polybugger.apollot.db.AcademicTermContract;
import net.polybugger.apollot.db.ApolloDbAdapter;

public class SettingsAcademicTermsActivity extends AppCompatActivity implements SettingsAcademicTermsActivityFragment.Listener,
        AcademicTermInsertUpdateDialogFragment.Listener,
        AcademicTermDeleteDialogFragment.Listener {

    private RecyclerView mRecyclerView;
    private Adapter mAdapter;

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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new Adapter(this);
        mRecyclerView.setAdapter(mAdapter);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                AcademicTermInsertUpdateDialogFragment df = (AcademicTermInsertUpdateDialogFragment) fm.findFragmentByTag(AcademicTermInsertUpdateDialogFragment.TAG);
                if(df == null) {
                    df = AcademicTermInsertUpdateDialogFragment.newInstance(null, getString(R.string.new_academic_term), getString(R.string.add));
                    df.show(fm, AcademicTermInsertUpdateDialogFragment.TAG);
                }
            }
        });

        // this will initialize the retained fragment and get the academic term entries
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
    public void onGetAcademicTerms(ArrayList<AcademicTermContract.AcademicTermEntry> arrayList) {
        mAdapter.setArrayList(arrayList);
    }

    @Override
    public void onConfirmInsertUpdateAcademicTerm(AcademicTermContract.AcademicTermEntry entry) {
        SettingsAcademicTermsActivityFragment rf = (SettingsAcademicTermsActivityFragment) getSupportFragmentManager().findFragmentByTag(SettingsAcademicTermsActivityFragment.TAG);
        if(rf != null) {
            if(entry.getId() == -1)
                rf.insertAcademicTerm(entry);
            else
                rf.updateAcademicTerm(entry);
        }
    }

    // TODO add description text to snackbar
    @Override
    public void onInsertAcademicTerm(AcademicTermContract.AcademicTermEntry entry, long id) {
        if(entry.getId() == -1) {
            entry.setId(id);
            mAdapter.add(entry);
            mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.academic_term_added), Snackbar.LENGTH_SHORT).show();
                }
            }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
        }
    }

    // TODO add description text to snackbar
    @Override
    public void onUpdateAcademicTerm(AcademicTermContract.AcademicTermEntry entry, int rowsUpdated) {
        if(rowsUpdated >= 1) {
            mAdapter.update(entry);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.academic_term_updated), Snackbar.LENGTH_SHORT).show();
                }
            }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
        }
    }

    @Override
    public void onConfirmDeleteAcademicTerm(AcademicTermContract.AcademicTermEntry entry) {
        SettingsAcademicTermsActivityFragment rf = (SettingsAcademicTermsActivityFragment) getSupportFragmentManager().findFragmentByTag(SettingsAcademicTermsActivityFragment.TAG);
        if(rf != null)
            rf.deleteAcademicTerm(entry);
    }

    // TODO add description text to snackbar
    @Override
    public void onDeleteAcademicTerm(AcademicTermContract.AcademicTermEntry entry, int rowsDeleted) {
        if(rowsDeleted >= 1) {
            mAdapter.remove(entry);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.academic_term_removed), Snackbar.LENGTH_SHORT).show();
                }
            }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
        }
    }

    public static class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private AppCompatActivity mActivity;
        private ArrayList<AcademicTermContract.AcademicTermEntry> mArrayList;

        public Adapter(AppCompatActivity activity) {
            mActivity = activity;
            mArrayList = new ArrayList<>();
        }

        public void setArrayList(ArrayList<AcademicTermContract.AcademicTermEntry> arrayList) {
            mArrayList = arrayList;
            notifyDataSetChanged();
        }

        public void add(AcademicTermContract.AcademicTermEntry entry) {
            mArrayList.add(entry);
            notifyDataSetChanged();
        }

        public void update(AcademicTermContract.AcademicTermEntry entry) {
            int pos = mArrayList.indexOf(entry);
            mArrayList.remove(pos);
            mArrayList.add(pos, entry);
            notifyDataSetChanged();
        }

        public void remove(AcademicTermContract.AcademicTermEntry entry) {
            mArrayList.remove(entry);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_settings_academic_terms, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            AcademicTermContract.AcademicTermEntry entry = mArrayList.get(position);
            holder.mBackgroundLayout.setBackgroundResource(BackgroundRect.getBackgroundResource(entry.getColor(), mActivity));
            holder.mClickableLayout.setTag(entry);
            holder.mClickableLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = mActivity.getSupportFragmentManager();
                    AcademicTermInsertUpdateDialogFragment df = (AcademicTermInsertUpdateDialogFragment) fm.findFragmentByTag(AcademicTermInsertUpdateDialogFragment.TAG);
                    if(df == null) {
                        df = AcademicTermInsertUpdateDialogFragment.newInstance((AcademicTermContract.AcademicTermEntry) v.getTag(), mActivity.getString(R.string.update_academic_term), mActivity.getString(R.string.save_changes));
                        df.show(fm, AcademicTermInsertUpdateDialogFragment.TAG);
                    }
                }
            });
            holder.mTextView.setText(entry.getDescription());
            holder.mImageButton.setTag(entry);
            holder.mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = mActivity.getSupportFragmentManager();
                    AcademicTermDeleteDialogFragment df = (AcademicTermDeleteDialogFragment) fm.findFragmentByTag(AcademicTermDeleteDialogFragment.TAG);
                    if(df == null) {
                        df = AcademicTermDeleteDialogFragment.newInstance((AcademicTermContract.AcademicTermEntry) v.getTag());
                        df.show(fm, AcademicTermDeleteDialogFragment.TAG);
                    }
                }
            });
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
                mTextView =  (TextView) itemView.findViewById(R.id.text_view);
                mImageButton = (ImageButton) itemView.findViewById(R.id.image_button);
            }
        }
    }
}
