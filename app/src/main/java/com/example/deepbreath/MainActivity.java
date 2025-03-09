package com.example.deepbreath;

import android.content.Intent;
import android.text.style.UnderlineSpan;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.widget.ImageButton;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // TextView redirection for "Login"
        TextView textView = findViewById(R.id.loginRedirectText); // Ensure you have a TextView with this ID in activity_main.xml

        String fullText = "Already a user? Login";
        SpannableString spannableString = new SpannableString(fullText);

        int startIndex = fullText.indexOf("Login");
        int endIndex = startIndex + "Login".length();

        // Underline "Login"
        spannableString.setSpan(new UnderlineSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // ClickableSpan for "Login"
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        };
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance()); // Enables clicking

        // Handle insets for UI adjustments
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ImageButton navigation
        ImageButton doctorButton = findViewById(R.id.imageButtonDoctor);
        ImageButton personButton = findViewById(R.id.imageButtonPerson);

        doctorButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignupDoctor.class);
            startActivity(intent);
        });

        personButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignupPatient.class);
            startActivity(intent);
        });
    }
}

