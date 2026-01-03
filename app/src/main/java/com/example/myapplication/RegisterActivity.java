package com.example.myapplication;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    // UI Elements
    private EditText etRegUser, etRegPass, etRegConfirmPass;
    private Button btnDoRegister;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Section 1.4: Apply the saved theme before setting the content view
        ThemeUtil.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Database Helper
        dbHelper = new DatabaseHelper(this);

        // Initialize UI components
        etRegUser = findViewById(R.id.etRegUser);
        etRegPass = findViewById(R.id.etRegPass);
        // Added a confirm password field for better validation (Section 8)
        etRegConfirmPass = findViewById(R.id.etRegConfirmPass);
        btnDoRegister = findViewById(R.id.btnDoRegister);

        // Register Button Click Logic
        btnDoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegistration();
            }
        });
    }

    private void attemptRegistration() {
        String username = etRegUser.getText().toString().trim();
        String password = etRegPass.getText().toString().trim();
        String confirm = etRegConfirmPass.getText().toString().trim();

        // Section 8.2: User Input Validation
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirm)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Section 4.1 & 4.2: Store data into SQLite
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user", username);
        values.put("pass", password);

        try {
            long newRowId = db.insert("users", null, values);

            if (newRowId != -1) {
                Toast.makeText(this, "Registration Successful! You can now login.", Toast.LENGTH_LONG).show();
                // Close this activity and return to MainActivity (Login)
                finish();
            } else {
                Toast.makeText(this, "Username already exists or error occurred", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Database Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }
    }

    // Section 9.2: Preserve UI state during rotation if needed
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("saved_user", etRegUser.getText().toString());
    }
}