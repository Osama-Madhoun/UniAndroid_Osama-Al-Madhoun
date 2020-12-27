package com.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        EditText email = findViewById(R.id.et_email);
        EditText password = findViewById(R.id.et_password);
        Storage storage = new Storage(this);
        findViewById(R.id.btn_create_your_account).setOnClickListener(v -> {
            if (!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())) {
                signInAccount(email.getText().toString(), password.getText().toString());
            } else {
                Toast.makeText(LoginActivity.this, "Fill empty filed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signInAccount(String email, String password) {
        //show progress bar
        Utils.showLoading(this);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Utils.hideLoading();
                        if (task.isSuccessful()) {
                            // Sign in success
                            Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails
                            Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void openSignUp(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }
}