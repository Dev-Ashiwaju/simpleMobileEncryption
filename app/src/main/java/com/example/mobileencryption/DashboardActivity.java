package com.example.mobileencryption;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    ArrayAdapter<String> mAdapter;
    ListView lstTask;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dbHelper = new DatabaseHelper(DashboardActivity.this);

        lstTask = findViewById(R.id.lstTask);
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEncrypt = new Intent(DashboardActivity.this, EncryptActivity.class);
                startActivity(intentEncrypt);
            }
        });
        loadTaskList();

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadTaskList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTaskList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadTaskList();
    }

    private void loadTaskList() {
        ArrayList<String> taskList = dbHelper.getTaskList();
        if(mAdapter==null){
            mAdapter = new ArrayAdapter<>(this,R.layout.row,R.id.back,taskList);
            lstTask.setAdapter(mAdapter);
        }
        else{
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void deleteTask(View view){
        View parent = (View)view.getParent();
        TextView taskTextView = parent.findViewById(R.id.back);
        Log.e("String", (String) taskTextView.getText());
        String task = String.valueOf(taskTextView.getText());
        dbHelper.deleteTask(task);
        loadTaskList();
    }

    public void getTask(View view){
        View parent = (View)view.getParent();
        TextView taskTextView = parent.findViewById(R.id.back);
        Log.e("String", (String) taskTextView.getText());
        String task = String.valueOf(taskTextView.getText());

        Bundle dataBundle = new Bundle();
        dataBundle.putString("id", task);

        Intent intent = new Intent(DashboardActivity.this,DecryptActivity.class);
        intent.putExtras(dataBundle);
        startActivity(intent);
    }
}
