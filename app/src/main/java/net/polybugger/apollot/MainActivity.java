package net.polybugger.apollot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.polybugger.apollot.db.ApolloDbAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApolloDbAdapter.setAppContext(this);
        ApolloDbAdapter.open();
        ApolloDbAdapter.close();

        setContentView(R.layout.activity_main);
    }
}
