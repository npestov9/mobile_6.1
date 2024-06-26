package com.example.a61;

// SetupActivity.java
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SetupActivity extends Activity {
    private EditText usernameEditText, emailEditText, passwordEditText, confirmPasswordEditText, phoneNumberEditText;
    private Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        usernameEditText = findViewById(R.id.usernameSetupEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        createAccountButton = findViewById(R.id.createAccountButton);

        createAccountButton.setOnClickListener(v -> {
            if (validateAccountCreation()) {
                Intent intent = new Intent(SetupActivity.this, TopicSelectionActivity.class);
                saveUserData(usernameEditText.getText().toString(), emailEditText.getText().toString());
                startActivity(intent);
            } else {
                // Handle errors (e.g., display error messages)
            }
        });
    }

    private void saveUserData(String username, String email) {
        SharedPreferences sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("USERNAME", username);
        editor.putString("EMAIL", email);
        editor.apply();  // Saves the data asynchronously
    }

    private boolean validateAccountCreation() {
        // Validate the data entered by the user
        // Here, just a simple check
        return passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString());
    }
}
