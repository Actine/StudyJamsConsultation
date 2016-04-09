package info.teib.newtest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String comment;

    private EditText mNameTextBox;
    private TextView mGreetingTextView;
    private Landmark[] landmarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        if (savedInstanceState == null) {
            String[] titles = getResources().getStringArray(R.array.landmarks);
            landmarks = new Landmark[titles.length];

            for (int i = 0; i < landmarks.length; i++) {
                landmarks[i] = new Landmark();
                landmarks[i].index = i;
                landmarks[i].isViewed = false;
                landmarks[i].title = titles[i];
                landmarks[i].pictureId = R.mipmap.ic_launcher;
            }
            landmarks[0].pictureId = R.drawable.brain;
            landmarks[1].pictureId = R.drawable.grape2;
            landmarks[4].pictureId = R.drawable.shop;
        } else {
            landmarks = (Landmark[]) savedInstanceState.getSerializable("info.teib.newtest.landmarks");
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int lastViewed = preferences.getInt("lastViewed", -1);
        if (lastViewed >= 0 && lastViewed < landmarks.length) {
            landmarks[lastViewed].isViewed = true;
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new LandmarksAdapter(this, landmarks));

        mNameTextBox = (EditText) findViewById(R.id.name);
        mGreetingTextView = (TextView) findViewById(R.id.greeting);

        if (savedInstanceState == null) {
            return;
        }

        comment = savedInstanceState.getString("info.teib.newtest.comment");
        if (comment != null) {
            updateDisplay();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.help) {
            Toast.makeText(MainActivity.this, "Натиснули допомогу", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_settings) {
            Toast.makeText(MainActivity.this, "Натиснули налаштування", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("info.teib.newtest.comment", comment);
        outState.putSerializable("info.teib.newtest.landmarks", landmarks);
    }

    public void rememberName(View v) {
        comment = mNameTextBox.getText().toString();
        updateDisplay();
    }

    private void updateDisplay() {
        mGreetingTextView.setText("Привіт, " + comment);
    }
}
