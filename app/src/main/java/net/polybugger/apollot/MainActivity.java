package net.polybugger.apollot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import net.polybugger.apollot.db.ApolloDbAdapter;
import net.polybugger.apollot.db.ClassContract;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApolloDbAdapter.setAppContext(this);
        ApolloDbAdapter.open();
        ApolloDbAdapter.close();

        setContentView(R.layout.activity_main);

        ((TextView) findViewById(R.id.text_view)).setText(ClassContract.SELECT_TABLE_SQL);
        Log.d("SQLITE", ClassContract.SELECT_TABLE_SQL);
    }
}
