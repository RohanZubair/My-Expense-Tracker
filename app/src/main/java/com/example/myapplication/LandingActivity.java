package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class LandingActivity extends AppCompatActivity {

    RecyclerView rv;
    DatabaseHelper dbHelper;
    ArrayList<String> expenseList;
    Button btnWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.applyTheme(this); // Section 1.4
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        dbHelper = new DatabaseHelper(this);
        rv = findViewById(R.id.rvExpenses);
        btnWeb = findViewById(R.id.btnGoWeb);
        expenseList = new ArrayList<>();

        // Section 4.3: Retrieve data from SQLite (Offline Mode)
        loadLocalData();

        // Section 3: Fetch Data from API (Simple Simulation)
        fetchApiData();

        btnWeb.setOnClickListener(v -> {
            startActivity(new Intent(this, WebActivity.class));
        });
    }

    private void loadLocalData() {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM expenses", null);
        while (cursor.moveToNext()) {
            // column 1 is item, column 2 is price
            expenseList.add(cursor.getString(1) + " - $" + cursor.getString(2));
        }
        cursor.close();

        rv.setLayoutManager(new LinearLayoutManager(this));
        // Note: You will need the ExpenseAdapter class we discussed earlier
        rv.setAdapter(new ExpenseAdapter(this, expenseList));
    }

    private void fetchApiData() {
        // Run network on a background thread (Section 3.2)
        new Thread(() -> {
            try {
                // Simulating API latency
                Thread.sleep(2000);
                runOnUiThread(() -> Toast.makeText(this, "API Sync: Successful", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Sync Failed (Offline)", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    // Section 6.1: Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        if (item.getItemId() == R.id.action_theme) {
            boolean currentTheme = prefs.getBoolean("dark", false);
            prefs.edit().putBoolean("dark", !currentTheme).apply();
            recreate(); // Restarts activity to apply theme
        } else if (item.getItemId() == R.id.action_logout) {
            prefs.edit().putBoolean("isLoggedIn", false).apply();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return true;
    }
}