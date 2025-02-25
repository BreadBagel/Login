import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "DeepBreath"
        private const val DATABASE_VERSION = 1

        // Doctors Table
        const val TABLE_DOCTORS = "doctors"
        const val COLUMN_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"

        // Patients Table
        const val TABLE_PATIENTS = "patients"
        const val COLUMN_PATIENT_ID = "id"
        const val COLUMN_PATIENT_USERNAME = "username"
        const val COLUMN_PATIENT_EMAIL = "email"
        const val COLUMN_PATIENT_PASSWORD = "password"

        // Create Table SQL Queries
        private const val CREATE_TABLE_DOCTORS = """
            CREATE TABLE $TABLE_DOCTORS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT NOT NULL,
                $COLUMN_EMAIL TEXT NOT NULL UNIQUE,
                $COLUMN_PASSWORD TEXT NOT NULL
            );
        """

        private const val CREATE_TABLE_PATIENTS = """
            CREATE TABLE $TABLE_PATIENTS (
                $COLUMN_PATIENT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PATIENT_USERNAME TEXT NOT NULL,
                $COLUMN_PATIENT_EMAIL TEXT NOT NULL UNIQUE,
                $COLUMN_PATIENT_PASSWORD TEXT NOT NULL
            );
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_DOCTORS)  // Create doctors table
        db.execSQL(CREATE_TABLE_PATIENTS) // Create patients table
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DOCTORS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PATIENTS")
        onCreate(db)
    }

    // Register a doctor
    fun registerDoctor(username: String, email: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
        }
        val result = db.insert(TABLE_DOCTORS, null, values)
        return result != -1L
    }

    // Register a patient
    fun registerPatient(username: String, email: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PATIENT_USERNAME, username)
            put(COLUMN_PATIENT_EMAIL, email)
            put(COLUMN_PATIENT_PASSWORD, password)
        }
        val result = db.insert(TABLE_PATIENTS, null, values)
        return result != -1L
    }

    // Check if doctor exists
    fun checkDoctorExists(email: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_DOCTORS WHERE $COLUMN_EMAIL = ?"
        val cursor = db.rawQuery(query, arrayOf(email))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // Check if patient exists
    fun checkPatientExists(email: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_PATIENTS WHERE $COLUMN_PATIENT_EMAIL = ?"
        val cursor = db.rawQuery(query, arrayOf(email))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
}