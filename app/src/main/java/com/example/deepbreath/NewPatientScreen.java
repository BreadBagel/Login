package com.example.deepbreath;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NewPatientScreen extends AppCompatActivity {

    // Declare UI components
    EditText editTextName, editTextAge, editTextDOB, editTextMedicalHistory, editTextAllergies;
    RadioGroup radioGroupGender;
    RadioButton radioMale, radioFemale;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_patient_dashboard);

        // Initialize UI components
        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextDOB = findViewById(R.id.editTextDOB);
        editTextMedicalHistory = findViewById(R.id.editTextMedicalHistory);
        editTextAllergies = findViewById(R.id.editTextAllergies);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        btnSubmit = findViewById(R.id.btnSubmit);


        // Set OnClickListener for Submit Button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String ageStr = editTextAge.getText().toString().trim();
                String dob = editTextDOB.getText().toString().trim();
                String medicalHistory = editTextMedicalHistory.getText().toString().trim();
                String allergies = editTextAllergies.getText().toString().trim();

                // Gender
                String gender = "";
                int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
                if (selectedGenderId != -1) {
                    RadioButton selectedGender = findViewById(selectedGenderId);
                    gender = selectedGender.getText().toString();
                }

                // Check
                if (name.isEmpty() || ageStr.isEmpty() || dob.isEmpty() || gender.isEmpty()) {
                    Toast.makeText(NewPatientScreen.this, "Please fill in all the required fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Insert data into SQLite database
                    SQLiteHelper dbHelper = new SQLiteHelper(NewPatientScreen.this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(SQLiteHelper.COLUMN_NAME, name);
                    values.put(SQLiteHelper.COLUMN_AGE, ageStr);
                    values.put(SQLiteHelper.COLUMN_DOB, dob);
                    values.put(SQLiteHelper.COLUMN_GENDER, gender);
                    values.put(SQLiteHelper.COLUMN_MEDICAL_HISTORY, medicalHistory);
                    values.put(SQLiteHelper.COLUMN_ALLERGIES, allergies);

                    // Insert the patient data into the database
                    long newRowId = db.insert(SQLiteHelper.TABLE_PATIENT, null, values);

                    // Check if insertion was successful
                    if (newRowId != -1) {
                        Toast.makeText(NewPatientScreen.this, "Patient information saved", Toast.LENGTH_SHORT).show();

                        // Optionally, navigate to PatientInfo activity after submission
                        // Intent intent = new Intent(NewPatientScreen.this, PatientInfo.class);
                        // startActivity(intent);
                    } else {
                        Toast.makeText(NewPatientScreen.this, "Error saving patient info", Toast.LENGTH_SHORT).show();
                    }
                }

                Intent intent = new Intent(NewPatientScreen.this, PatientInfo.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
