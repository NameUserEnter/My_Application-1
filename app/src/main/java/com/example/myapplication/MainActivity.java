package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void startWorkWithMatrixes(View v){
        Intent intent = new Intent(this, MatrixActivity.class);
        startActivity(intent);
    }
    public void startWorkWithResults(View v){
        Intent intent = new Intent(this, ResultsActivity.class);
        startActivity(intent);
    }

}