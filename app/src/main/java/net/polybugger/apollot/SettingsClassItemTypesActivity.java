package net.polybugger.apollot;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
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

import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassItemTypeContract;

public class SettingsClassItemTypesActivity extends AppCompatActivity implements SettingsClassItemTypesActivityFragment.Listener,
        ClassItemTypeInsertUpdateDialogFragment.Listener,
        ClassItemTypeDeleteDialogFragment.Listener {

    private RecyclerView mRecyclerView;
    private Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApolloDbAdapter.setAppContext(getApplicationContext());

        setContentView(R.layout.activity_settings_class_item_types);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);

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
                ClassItemTypeInsertUpdateDialogFragment df = (ClassItemTypeInsertUpdateDialogFragment) fm.findFragmentByTag(ClassItemTypeInsertUpdateDialogFragment.TAG);
                if(df == null) {
                    df = ClassItemTypeInsertUpdateDialogFragment.newInstance(null, getString(R.string.new_class_activity), getString(R.string.add));
                    df.show(fm, ClassItemTypeInsertUpdateDialogFragment.TAG);
                }
            }
        });

        // this will initialize the retained fragment and get the class item entries
        FragmentManager fm = getSupportFragmentManager();
        if(fm.findFragmentByTag(SettingsClassItemTypesActivityFragment.TAG) == null)
            fm.beginTransaction().add(SettingsClassItemTypesActivityFragment.newInstance(), SettingsClassItemTypesActivityFragment.TAG).commit();
    }

    @Override
    public void onBackPressed() {
        ClassesFragment.REQUERY = true;
        ClassInfoFragment.REQUERY_GRADE_BREAKDOWNS = true;
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetClassItemTypes(ArrayList<ClassItemTypeContract.ClassItemTypeEntry> arrayList) {
        mAdapter.setArrayList(arrayList);
    }

    @Override
    public void onConfirmInsertUpdateClassItemType(ClassItemTypeContract.ClassItemTypeEntry entry) {
        SettingsClassItemTypesActivityFragment rf = (SettingsClassItemTypesActivityFragment) getSupportFragmentManager().findFragmentByTag(SettingsClassItemTypesActivityFragment.TAG);
        if(rf != null) {
            if(entry.getId() == -1)
                rf.insertClassItemType(entry);
            else
                rf.updateClassItemType(entry);
        }
    }

    // TODO add description text to snackbar
    @Override
    public void onInsertClassItemType(ClassItemTypeContract.ClassItemTypeEntry entry, long id) {
        if(entry.getId() == -1) {
            entry.setId(id);
            mAdapter.add(entry);
            mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.class_activity_added), Snackbar.LENGTH_SHORT).show();
                }
            }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
        }
    }

    // TODO add description text to snackbar
    @Override
    public void onUpdateClassItemType(ClassItemTypeContract.ClassItemTypeEntry entry, int rowsUpdated) {
        if(rowsUpdated >= 1) {
            mAdapter.update(entry);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.class_activity_updated), Snackbar.LENGTH_SHORT).show();
                }
            }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
        }
    }

    @Override
    public void onConfirmDeleteClassItemType(ClassItemTypeContract.ClassItemTypeEntry entry) {
        SettingsClassItemTypesActivityFragment rf = (SettingsClassItemTypesActivityFragment) getSupportFragmentManager().findFragmentByTag(SettingsClassItemTypesActivityFragment.TAG);
        if(rf != null)
            rf.deleteClassItemType(entry);
    }

    // TODO add description text to snackbar
    @Override
    public void onDeleteClassItemType(ClassItemTypeContract.ClassItemTypeEntry entry, int rowsDeleted) {
        if(rowsDeleted >= 1) {
            mAdapter.remove(entry);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.class_activity_removed), Snackbar.LENGTH_SHORT).show();
                }
            }, MainActivity.SNACKBAR_POST_DELAYED_MSEC);
        }
    }

    public static class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private FragmentActivity mActivity;
        private ArrayList<ClassItemTypeContract.ClassItemTypeEntry> mArrayList;

        public Adapter(FragmentActivity activity) {
            mActivity = activity;
            mArrayList = new ArrayList<>();
        }

        public void setArrayList(ArrayList<ClassItemTypeContract.ClassItemTypeEntry> arrayList) {
            mArrayList = arrayList;
            notifyDataSetChanged();
        }

        public void add(ClassItemTypeContract.ClassItemTypeEntry entry) {
            mArrayList.add(entry);
            notifyDataSetChanged();
        }

        public void update(ClassItemTypeContract.ClassItemTypeEntry entry) {
            int pos = mArrayList.indexOf(entry);
            mArrayList.remove(pos);
            mArrayList.add(pos, entry);
            notifyDataSetChanged();
        }

        public void remove(ClassItemTypeContract.ClassItemTypeEntry entry) {
            mArrayList.remove(entry);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_settings_class_item_types, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ClassItemTypeContract.ClassItemTypeEntry entry = mArrayList.get(position);
            holder.mBackgroundLayout.setBackgroundResource(BackgroundRect.getBackgroundResource(entry.getColor(), mActivity));
            Resources res = mActivity.getResources();
            int topMargin = res.getDimensionPixelSize(R.dimen.recycler_view_item_margin_top);
            int rightMargin = res.getDimensionPixelSize(R.dimen.recycler_view_item_margin_right);
            int bottomMargin = res.getDimensionPixelSize(R.dimen.recycler_view_item_margin_bottom);
            int leftMargin = res.getDimensionPixelSize(R.dimen.recycler_view_item_margin_left);
            if(position == 0) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.mBackgroundLayout.getLayoutParams();
                layoutParams.setMargins(leftMargin, topMargin * 2, rightMargin, bottomMargin);
                holder.mBackgroundLayout.setLayoutParams(layoutParams);
            }
            else if(position == (mArrayList.size() - 1)) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.mBackgroundLayout.getLayoutParams();
                layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin * 2);
                holder.mBackgroundLayout.setLayoutParams(layoutParams);
            }
            else {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.mBackgroundLayout.getLayoutParams();
                layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
                holder.mBackgroundLayout.setLayoutParams(layoutParams);
            }
            holder.mClickableLayout.setTag(entry);
            holder.mClickableLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = mActivity.getSupportFragmentManager();
                    ClassItemTypeInsertUpdateDialogFragment df = (ClassItemTypeInsertUpdateDialogFragment) fm.findFragmentByTag(ClassItemTypeInsertUpdateDialogFragment.TAG);
                    if(df == null) {
                        df = ClassItemTypeInsertUpdateDialogFragment.newInstance((ClassItemTypeContract.ClassItemTypeEntry) v.getTag(), mActivity.getString(R.string.update_class_activity), mActivity.getString(R.string.save_changes));
                        df.show(fm, ClassItemTypeInsertUpdateDialogFragment.TAG);
                    }
                }
            });
            holder.mTextView.setText(entry.getDescription());
            holder.mImageButton.setTag(entry);
            holder.mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = mActivity.getSupportFragmentManager();
                    ClassItemTypeDeleteDialogFragment df = (ClassItemTypeDeleteDialogFragment) fm.findFragmentByTag(ClassItemTypeDeleteDialogFragment.TAG);
                    if(df == null) {
                        df = ClassItemTypeDeleteDialogFragment.newInstance((ClassItemTypeContract.ClassItemTypeEntry) v.getTag());
                        df.show(fm, ClassItemTypeDeleteDialogFragment.TAG);
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
                mTextView = (TextView) itemView.findViewById(R.id.text_view);
                mImageButton = (ImageButton) itemView.findViewById(R.id.image_button);
            }
        }
    }
}
