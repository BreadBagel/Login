package com.example.deepbreath;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.deepbreath.LoginDBHelper;
import com.example.deepbreath.PatientInfo;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private LoginDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new LoginDBHelper(this);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (checkCredentials(username, password)) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, PatientInfo.class); // Redirect to PatientInfo
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkCredentials(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Check Doctor table
        Cursor cursor = db.rawQuery("SELECT * FROM " + LoginDBHelper.TABLE_DOCTOR +
                        " WHERE " + LoginDBHelper.COLUMN_USERNAME + "=? AND " + LoginDBHelper.COLUMN_PASSWORD + "=?",
                new String[]{username, password});

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();

        // Check Patient table
        cursor = db.rawQuery("SELECT * FROM " + LoginDBHelper.TABLE_PATIENT +
                        " WHERE " + LoginDBHelper.COLUMN_USERNAME + "=? AND " + LoginDBHelper.COLUMN_PASSWORD + "=?",
                new String[]{username, password});

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }
}
