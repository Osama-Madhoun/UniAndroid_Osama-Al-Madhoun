package com.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private DatabaseReference mReferenceList;
    private FirebaseAuth mAuth;
    private ListAdapter listAdapter;
    List<ListItem> lists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void addNewList(View view) {
        createDialog();
    }

    public void createDialog() {
        //set up dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_layout_new_list);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        EditText list = dialog.findViewById(R.id.et_list);
        TextView addList = dialog.findViewById(R.id.btn_add);
        addList.setOnClickListener(v -> {
            Utils.showLoading(this);
            mReferenceList.child(mAuth.getUid()).push().setValue(new ListItem(list.getText().toString()))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Utils.hideLoading();
                            // Write was successful!
                            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Utils.hideLoading();
                            // Write failed
                            Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                        }
                    });

        });
        // now that the dialog is set up, it's time to show it
        dialog.show();
    }

    private void init() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Initialize Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mReferenceList = database.getReference("lists");
        listAdapter = new ListAdapter(this, lists, new ListAdapter.IListElement() {
            @Override
            public void openTasks(String listID, String title) {
                startActivity(new Intent(MainActivity.this, TasksActivity.class).putExtra("listID", listID).putExtra("titleList", title));
            }
        });
        TextView logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, SplashActivity.class));
            finish();
        });
        RecyclerView rvList = findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(listAdapter);
        mReferenceList.child(mAuth.getUid()).addValueEventListener(getListsFromDB());
    }

    private ValueEventListener getListsFromDB() {
        ValueEventListener listListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lists.clear();
                listAdapter.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String listID = snapshot.getKey();
                    ListItem listItem = snapshot.getValue(ListItem.class);
                    listItem.setTitleList(listItem.getTitleList());
                    listItem.setListID(listID);
                    lists.add(listItem);
                }
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Category failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        return listListener;
    }
}