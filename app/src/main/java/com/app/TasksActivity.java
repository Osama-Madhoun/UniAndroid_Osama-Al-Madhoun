package com.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TasksActivity extends AppCompatActivity {
    private static final String TAG = "TasksActivity";
    private DatabaseReference mReferenceTasks;
    private FirebaseAuth mAuth;
    private TaskAdapter taskAdapter;
    String listID;
    String listTitle;
    List<TaskItem> taskItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        listID = getIntent().getStringExtra("listID");
        listTitle = getIntent().getStringExtra("titleList");
        init();
    }

    private void init() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Initialize Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mReferenceTasks = database.getReference("tasks");
        taskAdapter = new TaskAdapter(this, taskItems, (taskID, taskTitle, taskDesc) -> startActivity(new Intent(TasksActivity.this, TaskDetailsActivity.class)
                .putExtra("listID", listID)
                .putExtra("taskID", taskID)
                .putExtra("taskTitle", taskTitle)
                .putExtra("taskDesc", taskDesc)));
        TextView title = findViewById(R.id.tv_title_task);
        title.setText(listTitle);
        RecyclerView rvList = findViewById(R.id.rv_task);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(taskAdapter);
        mReferenceTasks.child(mAuth.getUid()).child(listID).addValueEventListener(getTasksFromDB());
    }

    private ValueEventListener getTasksFromDB() {
        ValueEventListener taskListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                taskItems.clear();
                taskAdapter.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String taskID = snapshot.getKey();
                    TaskItem taskItem = snapshot.getValue(TaskItem.class);
                    taskItem.setTitleTask(taskItem.getTitleTask());
                    taskItem.setTaskDesc(taskItem.getTaskDesc());
                    taskItem.setTaskID(taskID);
                    taskItems.add(taskItem);
                }
                taskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Category failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        return taskListener;
    }

    public void addNewTask(View view) {
        createDialog();
    }

    public void createDialog() {
        //set up dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_layout_new_task);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        EditText title = dialog.findViewById(R.id.et_title_task);
        EditText desc = dialog.findViewById(R.id.et_desc_task);
        TextView addTask = dialog.findViewById(R.id.btn_add);
        addTask.setOnClickListener(v -> {
            Utils.showLoading(this);
            mReferenceTasks.child(mAuth.getUid()).child(listID).push().setValue(new TaskItem(title.getText().toString(), desc.getText().toString()))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Utils.hideLoading();
                            // Write was successful!
                            Toast.makeText(TasksActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Utils.hideLoading();
                            // Write failed
                            Toast.makeText(TasksActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                        }
                    });

        });
        // now that the dialog is set up, it's time to show it
        dialog.show();
    }

    public void backArrow(View view) {
        finish();
    }
}