package com.example.a61;

// LoginActivity.java
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginActivity extends Activity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView needAccountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.loginButton);
        needAccountTextView = findViewById(R.id.needAccountTextView);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        needAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SetupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login() {
        // For now, any username and password will work.
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // If you had specific conditions or authentication, it would go here.

        // Navigate to DashboardActivity
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(intent);
    }
}
