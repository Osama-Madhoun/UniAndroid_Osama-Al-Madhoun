package com.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TaskDetailsActivity extends AppCompatActivity {
    private DatabaseReference mReferenceTasks;
    private FirebaseAuth mAuth;
    String listID;
    String taskID;
    String taskTitle;
    String taskDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Initialize Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mReferenceTasks = database.getReference("tasks");
        listID = getIntent().getStringExtra("listID");
        taskID = getIntent().getStringExtra("taskID");
        taskTitle = getIntent().getStringExtra("taskTitle");
        taskDesc = getIntent().getStringExtra("taskDesc");
        EditText title = findViewById(R.id.et_title_task);
        title.setText(taskTitle);
        EditText desc = findViewById(R.id.et_description);
        desc.setText(taskDesc);
        TextView edit = findViewById(R.id.edit);
        edit.setOnClickListener(v -> {
            Utils.showLoading(this);
            mReferenceTasks.child(mAuth.getUid()).child(listID).child(taskID).setValue(new TaskItem(title.getText().toString(), desc.getText().toString()))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Utils.hideLoading();
                            // Write was successful!
                            Toast.makeText(TaskDetailsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Utils.hideLoading();
                            // Write failed
                            Toast.makeText(TaskDetailsActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        TextView delete = findViewById(R.id.tv_delete);
        delete.setOnClickListener(v -> {
            Utils.showLoading(this);
            mReferenceTasks.child(mAuth.getUid()).child(listID).child(taskID).removeValue();
            finish();
        });
    }

    public void backArrow(View view) {
        finish();
    }
}