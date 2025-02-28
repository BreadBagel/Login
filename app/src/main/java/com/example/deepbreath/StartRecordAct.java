package com.example.deepbreath;
import com.example.deepbreath.api.ApiService;
import android.widget.Toast;
import java.io.File;

//Network Imports
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

public class StartRecordAct extends AppCompatActivity {

    private boolean isRecording = false;
    private TextView timerText;
    private ImageButton recordButton;
    private MediaRecorder mediaRecorder;
    private String tempFilePath;
    private static final int REQUEST_PERMISSION_CODE = 1000;

    private Handler timerHandler = new Handler();
    private int elapsedTime = 0;
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            elapsedTime++;
            timerText.setText(formatTime(elapsedTime));
            timerHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_record_layout);

        timerText = findViewById(R.id.timer_text);
        recordButton = findViewById(R.id.radio_button);
        ImageButton checkButton = findViewById(R.id.check_circle);
        ImageButton helpButton = findViewById(R.id.help_button);

        // Request permission
        if (!checkPermissions()) {
            requestPermissions();
        }

        //  record stop
        recordButton.setOnClickListener(v -> toggleRecording());

        // Save the recording when check
//        checkButton.setOnClickListener(v -> saveRecording());

        checkButton.setOnClickListener(v -> {
            if (tempFilePath != null) {
                uploadAudio(tempFilePath);
            } else {
                Toast.makeText(this, "No recording found!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void toggleRecording() {
        if (isRecording) {
            stopRecording();
        } else {
            startRecording();
        }
    }

    private void startRecording() {
        if (!checkPermissions()) {
            requestPermissions();
            return;
        }

        isRecording = true;
        tempFilePath = getExternalFilesDir(Environment.DIRECTORY_MUSIC) + "/temp_audio.3gp";

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(tempFilePath);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            elapsedTime = 0;
            timerHandler.post(timerRunnable);

            // UI TO RED
            timerText.setTextColor(Color.RED); // Change timer color to red
            recordButton.setImageResource(R.drawable.recording_button); // Change button icon

            Toast.makeText(this, "Recording started...", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            isRecording = false;
            try {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                timerHandler.removeCallbacks(timerRunnable);

                // Debugging: Check if file exists
                File recordedFile = new File(tempFilePath);
                if (recordedFile.exists()) {
                } else {
                    Toast.makeText(this, "Recording stopped: No file found!", Toast.LENGTH_LONG).show();
                }

                // UI TO BLACK RADIO BUTTON
                timerText.setTextColor(Color.BLACK);
                recordButton.setImageResource(R.drawable.radio_button);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error stopping recording: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


    private void saveRecording() {
        if (tempFilePath == null) {
            Toast.makeText(this, "No recording to save!", Toast.LENGTH_SHORT).show();
            return;
        }

        File tempFile = new File(tempFilePath);
        if (!tempFile.exists()) {
            Toast.makeText(this, "Recording file not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        //unique file name
        File saveDir = getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File finalFile = getUniqueFileName(saveDir, "recording", ".3gp");

        // Rename the file
        if (tempFile.renameTo(finalFile)) {
            Toast.makeText(this, "Recording saved as: " + finalFile.getName(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Error saving file!", Toast.LENGTH_SHORT).show();
        }

        // Reset temp file path
        tempFilePath = null;
    }

    //THE UPLOAD FUNCTION

    private void uploadAudio(String filePath) {
        File file = new File(filePath);
        String fileName = file.getName();

        RequestBody requestFile = RequestBody.create(MediaType.parse("audio/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", fileName, requestFile);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.68.107:5000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<ResponseBody> call = apiService.uploadAudio(body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(StartRecordAct.this, "Audio uploaded successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StartRecordAct.this, "Upload failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(StartRecordAct.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private File getUniqueFileName(File directory, String baseName, String extension) {
        File file = new File(directory, baseName + extension);
        int count = 1;

        while (file.exists()) {
            file = new File(directory, baseName + "(" + count + ")" + extension);
            count++;
        }

        return file;
    }

    private boolean checkPermissions() {
        int recordAudio = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int writeStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return recordAudio == PackageManager.PERMISSION_GRANTED && writeStorage == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissions denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }


}
