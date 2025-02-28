package com.example.deepbreath;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PatientInfo extends AppCompatActivity {

    TextView textViewName, textViewAge, textViewDOB, textViewGender, textViewMedicalHistory, textViewAllergies;
    Button RecordCoughAudio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_info);

        // TextViews

        textViewName = findViewById(R.id.textViewName);
        textViewAge = findViewById(R.id.textViewAge);
        textViewDOB = findViewById(R.id.textViewDOB);
        textViewGender = findViewById(R.id.textViewGender);
        textViewMedicalHistory = findViewById(R.id.textViewMedicalHistory);
        textViewAllergies = findViewById(R.id.textViewAllergies);
        RecordCoughAudio = findViewById(R.id.RecordCoughAudio);

        SQLiteHelper dbHelper = new SQLiteHelper(PatientInfo.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.query(
                    SQLiteHelper.TABLE_PATIENT,   // Table name
                    null,                        //  columns
                    null,                        // No where clause
                    null,                        // No selection args
                    null,                        // No group by
                    null,                        // No having
                    null                         // No order by
            );

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();

                // Check if column index is valid
                int nameIndex = cursor.getColumnIndex(SQLiteHelper.COLUMN_NAME);
                String name = (nameIndex != -1) ? cursor.getString(nameIndex) : "N/A";

                int ageIndex = cursor.getColumnIndex(SQLiteHelper.COLUMN_AGE);
                String age = (ageIndex != -1) ? cursor.getString(ageIndex) : "N/A";

                int dobIndex = cursor.getColumnIndex(SQLiteHelper.COLUMN_DOB);
                String dob = (dobIndex != -1) ? cursor.getString(dobIndex) : "N/A";

                int genderIndex = cursor.getColumnIndex(SQLiteHelper.COLUMN_GENDER);
                String gender = (genderIndex != -1) ? cursor.getString(genderIndex) : "N/A";

                int medicalHistoryIndex = cursor.getColumnIndex(SQLiteHelper.COLUMN_MEDICAL_HISTORY);
                String medicalHistory = (medicalHistoryIndex != -1) ? cursor.getString(medicalHistoryIndex) : "N/A";

                int allergiesIndex = cursor.getColumnIndex(SQLiteHelper.COLUMN_ALLERGIES);
                String allergies = (allergiesIndex != -1) ? cursor.getString(allergiesIndex) : "N/A";

                textViewName.setText("Name: " + name);
                textViewAge.setText("Age: " + age);
                textViewDOB.setText("DOB: " + dob);
                textViewGender.setText("Gender: " + gender);
                textViewMedicalHistory.setText("Medical History: " + medicalHistory);
                textViewAllergies.setText("Allergies: " + allergies);
            } else {
                // No data found
                textViewName.setText("No patient data found.");
                textViewAge.setText("No patient data found.");
                textViewDOB.setText("No patient data found.");
                textViewGender.setText("No patient data found.");
                textViewMedicalHistory.setText("No patient data found.");
                textViewAllergies.setText("No patient data found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }


        RecordCoughAudio.setOnClickListener(v -> {
            Intent intent = new Intent(PatientInfo.this, StartRecordAct.class);
            startActivity(intent);
        });
    }
}
