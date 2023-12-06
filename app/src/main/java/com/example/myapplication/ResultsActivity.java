package com.example.myapplication;

import static android.content.Intent.ACTION_VIEW;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    private static TextView resText;
    private double[][] matrixB ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        resText = findViewById(R.id.ResText);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String matrixData = extras.getString("matrixData");
            if (matrixData != null) {
                resText.setText(matrixData);
                matrixB = parseMatrix(matrixData);
                GridLayout matrixGrid = findViewById(R.id.matrixGrid);
                displayMatrix(this, matrixGrid, matrixB);
            }
            String textData = extras.getString("textData");
            if (textData != null) {
                resText.setText(textData);
            }
        }
    }


    private void displayMatrix(Context context, GridLayout matrixGrid, double[][] matrix) {
        matrixGrid.removeAllViews();
        int numRows = matrix.length;
        int numCols = matrix[0].length;
        int cornerRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, context.getResources().getDisplayMetrics());
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                TextView textView = new TextView(context);
                textView.setText(String.valueOf(matrix[i][j]));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                GradientDrawable border = new GradientDrawable();
                border.setColor(Color.parseColor("#66a5ad"));
                border.setStroke(2, Color.BLACK);
                border.setCornerRadius(cornerRadius);
                textView.setBackground(border);
                textView.setGravity(Gravity.CENTER);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.rowSpec = GridLayout.spec(i,1f);
                params.columnSpec = GridLayout.spec(j,1f);
                textView.setLayoutParams(params);
                matrixGrid.addView(textView);
            }
        }
    }
    public void startWorkWithMatrixes(View v){
        Intent intent = new Intent(this, MatrixActivity.class);
        startActivity(intent);
    }
    public void startWorkWithMain(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public double[][] parseMatrix(String matrixText) {
        String[] rows = matrixText.substring(2, matrixText.length() - 2).split("\\], \\[");

        int numRows = rows.length;
        int numCols = rows[0].split(",").length;

        double[][] myMatrix = new double[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            String[] values = rows[i].split(",");

            for (int j = 0; j < numCols; j++) {
                String s = values[j].trim();
                myMatrix[i][j] = Math.round(Double.parseDouble(s)* 10) / 10.0;
            }
        }

        return myMatrix;
    }
}