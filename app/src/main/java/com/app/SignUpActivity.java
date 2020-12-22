package com.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        EditText username = findViewById(R.id.et_name);
        EditText email = findViewById(R.id.et_email);
        EditText password = findViewById(R.id.et_password);
        Storage storage = new Storage(this);
        findViewById(R.id.btn_create_your_account).setOnClickListener(v -> {
            if (!TextUtils.isEmpty(username.getText().toString()) && !TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())) {
                storage.saveUsername(username.getText().toString());
                storage.saveEmail(email.getText().toString());
                storage.savePassword(password.getText().toString());
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }else {
                Toast.makeText(SignUpActivity.this, "Fill empty filed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onSignIn(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}