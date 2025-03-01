    package com.example.deepbreath;

    import android.content.Context;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;

    public class SQLiteHelper extends SQLiteOpenHelper {

        // Database name
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "patient_info.db";

        // Patient Table
        public static final String TABLE_PATIENT = "patient";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_AGE = "age";
        public static final String COLUMN_DOB = "dob";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_MEDICAL_HISTORY = "medical_history";
        public static final String COLUMN_ALLERGIES = "allergies";

        // Create table SQL query
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_PATIENT + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_AGE + " TEXT, "
                + COLUMN_DOB + " TEXT, "
                + COLUMN_GENDER + " TEXT, "
                + COLUMN_MEDICAL_HISTORY + " TEXT, "
                + COLUMN_ALLERGIES + " TEXT);";

        public SQLiteHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //  patient table
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENT);
            onCreate(db);
        }
    }
