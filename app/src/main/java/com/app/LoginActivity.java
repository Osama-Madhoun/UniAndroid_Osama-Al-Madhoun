package com.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText email = findViewById(R.id.et_email);
        EditText password = findViewById(R.id.et_password);
        Storage storage = new Storage(this);
        findViewById(R.id.btn_create_your_account).setOnClickListener(v -> {
            if (!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())) {
                if (email.getText().toString().equals(storage.getEmail()) && password.getText().toString().equals(storage.getPassword())) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else
                    Toast.makeText(this, "Sorry, email or passord incorrect!!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "Fill empty filed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openSignUp(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }
}