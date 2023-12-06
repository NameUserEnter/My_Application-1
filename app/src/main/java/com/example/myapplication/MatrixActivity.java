package com.example.myapplication;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class MatrixActivity extends AppCompatActivity {

    private EditText firstMatrixSizeEditText;
    private EditText secondMatrixSizeEditText;
    private EditText firstMatrixEditText;
    private EditText secondMatrixEditText;

    private double[][] firstMatrix;
    private double[][] secondMatrix;

    private int row1;private int col1;
    private int row2;private int col2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix);
        firstMatrixSizeEditText = findViewById(R.id.firstMatrixSizeEditText);
        secondMatrixSizeEditText = findViewById(R.id.secondMatrixSizeEditText);
        firstMatrixEditText = findViewById(R.id.firstMatrixEditText);
        secondMatrixEditText = findViewById(R.id.secondMatrixEditText);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firstMatrixSizeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    onFirstMatrixSizeClick();
                }
            }
        });
        secondMatrixSizeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    onSecondMatrixSizeClick();
                }
            }
        });
        firstMatrixEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    onFirstMatrixClick();
                }
            }
        });
        secondMatrixEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    onSecondMatrixClick();
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    Log.d("focus", "touchevent");
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
    public void onFirstMatrixSizeClick() {
        int n, m;
        String sizeInput = firstMatrixSizeEditText.getText().toString();
        if (isValidMatrixSize(sizeInput)) {
            String[] sizeParts = sizeInput.split("x");
            n = Integer.parseInt(sizeParts[0]);
            m = Integer.parseInt(sizeParts[1]);
        } else {
            Toast.makeText(this, "Некоректний формат розміру матриці. Введіть у вигляді 'nxm'", Toast.LENGTH_SHORT).show();
            firstMatrixSizeEditText.setText("");
            return;
        }
         if (n <= 4 && m <= 4&& n >= 1 && m >= 1) {
            row1 = n;
            col1 = m;
        } else {
            Toast.makeText(this, "Розмір матриці не може бути більше 4x4 та меншим 1х1", Toast.LENGTH_SHORT).show();
             firstMatrixSizeEditText.setText("");
        }
    }
    public void onSecondMatrixSizeClick() {
        int n, m;
        String sizeInput = secondMatrixSizeEditText.getText().toString();
        if (isValidMatrixSize(sizeInput)) {
            String[] sizeParts = sizeInput.split("x");
            n = Integer.parseInt(sizeParts[0]);
            m = Integer.parseInt(sizeParts[1]);
        } else {
            Toast.makeText(this, "Некоректний формат розміру матриці. Введіть у вигляді 'nxm'", Toast.LENGTH_SHORT).show();
            secondMatrixSizeEditText.setText("");
            return;
        }
        if (n <= 4 && m <= 4&& n >= 1 && m >= 1) {
            row2 = n;
            col2 = m;
        } else {
            Toast.makeText(this, "Розмір матриці не може бути більше 4x4 та меншим 1х1", Toast.LENGTH_SHORT).show();
            secondMatrixSizeEditText.setText("");
        }
    }
    private boolean isValidMatrixSize(String input) {
        return input.matches("\\d+x\\d+");
    }
    public void onFirstMatrixClick() {
        String matrixInput = firstMatrixEditText.getText().toString();
        if(isValidMatrixFormat(matrixInput, row1, col1)){
            firstMatrix = parseMatrix(matrixInput);}
        else{Toast.makeText(this, "Не коректно введено матрицю", Toast.LENGTH_SHORT).show();
            firstMatrixEditText.setText("");}
    }
    public void onSecondMatrixClick() {
        String matrixInput = secondMatrixEditText.getText().toString();
        if(isValidMatrixFormat(matrixInput, row2, col2)){
            secondMatrix = parseMatrix(matrixInput);}
        else{Toast.makeText(this, "Не коректно введено матрицю, або матриця не відповідає заданому розміру", Toast.LENGTH_SHORT).show();
            secondMatrixEditText.setText("");}
    }
    public boolean isValidMatrixFormat(String input, int myRow, int myCol) {
        if (input.startsWith("[[") && input.endsWith("]]")) {
            String arrayContent = input.substring(2, input.length() - 2);
            String[] rows = arrayContent.split("\\],\\[");
            int numCols = -1;
            boolean isValid = true;
            for (String row : rows) {
                String[] elements = row.split(",");
                if (numCols == -1) {
                    numCols = elements.length;
                } else if (numCols != elements.length) {
                    isValid = false;
                    break;
                }
                for (String element : elements) {
                    if (element.trim().isEmpty()) {
                        return false;
                    }
                    if (!element.matches("^-?\\d*(\\.\\d+)?$")) {
                        return false;
                    }
                }
            }
            if(rows.length!= myRow && numCols!=myCol)
                return false;
            return isValid;
            }
        else{ return false;}
    }

    public double[][] parseMatrix(String matrixText) {
        String[] rows = matrixText.substring(2, matrixText.length() - 2).trim().split("\\],\\[");
        int numRows = rows.length;
        int numCols = rows[0].split(",").length;
        double[][] myMatrix = new double[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            String[] values = rows[i].split(",");
            for (int j = 0; j < numCols; j++) {
                String s = values[j].trim();
                myMatrix[i][j] = Double.parseDouble(s);
            }
        }
        return myMatrix;
    }

    public void onFirstMatrixDeterminantClick(View v){
        if(row1 == col1 && row1 != 0 ){
            double val = determinant(firstMatrix);
            String res = Double.toString(val);
            Intent intent = new Intent(this, ResultsActivity.class);
            intent.putExtra("textData", res);
            startActivity(intent);
        }
        else{Toast.makeText(this, "Матриця не квадратична", Toast.LENGTH_SHORT).show();}
    }
    public void onSecondMatrixDeterminantClick(View v){
        if(row2 == col2&&row1 != 0 ){
            double val = determinant(secondMatrix);
            String res = Double.toString(val);
            Intent intent = new Intent(this, ResultsActivity.class);
            intent.putExtra("textData", res);
            startActivity(intent);
        }
        else{Toast.makeText(this, "Матриця не квадратична", Toast.LENGTH_SHORT).show();}
    }

    public static double determinant(double[][] matrix) {
        int n = matrix.length;
        if (n == 1) {
            return matrix[0][0];
        }
        if (n == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        }
        double det = 0;
        for (int col = 0; col < n; col++) {
            double[][] subMatrix = new double[n - 1][n - 1];
            for (int i = 1; i < n; i++) {
                for (int j = 0, k = 0; j < n; j++) {
                    if (j == col) continue;
                    subMatrix[i - 1][k] = matrix[i][j];
                    k++;
                }
            }
            det += (col % 2 == 0 ? 1 : -1) * matrix[0][col] * determinant(subMatrix);
        }

        return det;
    }


    public void onFirstReverseMatrixClick(View v){
        if(row1 == col1 &&  determinant(firstMatrix)!= 0){
            double[][] val = reverseMatrix(firstMatrix);
            String res = Arrays.deepToString(val);
            Intent intent = new Intent(this, ResultsActivity.class);
            intent.putExtra("matrixData", res); // Replace "New Text" with the actual text you want to pass
            startActivity(intent);
        }
        else{Toast.makeText(this, "Матриця не квадратична", Toast.LENGTH_SHORT).show();}
    }
    public void onSecondReverseMatrixClick(View v){
        if(row2 == col2&&determinant(secondMatrix)!= 0){
            double[][] val = reverseMatrix(secondMatrix);
            String res = Arrays.deepToString(val);
            Intent intent = new Intent(this, ResultsActivity.class);
            intent.putExtra("matrixData", res); // Replace "New Text" with the actual text you want to pass
            startActivity(intent);
        }
        else{Toast.makeText(this, "Матриця не квадратична", Toast.LENGTH_SHORT).show();}
    }
        public double[][] reverseMatrix(double[][] A) {
            int N = A.length;
            double temp;
            float [][] E = new float [N][N];
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                {
                    E[i][j] = 0f;
                    if (i == j)
                        E[i][j] = 1f;
                }
            for (int k = 0; k < N; k++)
            {
                temp = A[k][k];
                for (int j = 0; j < N; j++)
                {
                    A[k][j] /= temp;
                    E[k][j] /= temp;
                }
                for (int i = k + 1; i < N; i++)
                {
                    temp = A[i][k];
                    for (int j = 0; j < N; j++)
                    {
                        A[i][j] -= A[k][j] * temp;
                        E[i][j] -= E[k][j] * temp;
                    }
                }
            }
            for (int k = N - 1; k > 0; k--)
            {
                for (int i = k - 1; i >= 0; i--)
                {
                    temp = A[i][k];
                    for (int j = 0; j < N; j++)
                    {
                        A[i][j] -= A[k][j] * temp;
                        E[i][j] -= E[k][j] * temp;
                    }
                }
            }
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                    A[i][j] = E[i][j];
        return A;
        }
    public void matrixSum(View v) {
        if (row1 != row2 || col1 != col2|| firstMatrix==null|| secondMatrix==null) {
            Toast.makeText(this, "Розміри матриць не рівні", Toast.LENGTH_SHORT).show();
        }
        double[][] result = new double[row1][col1];
        for (int i = 0; i < row1; i++) {
            for (int j = 0; j < col1; j++) {
                result[i][j] = firstMatrix[i][j] + secondMatrix[i][j];
            }
        }
        String res = Arrays.deepToString(result);
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("matrixData", res); // Replace "New Text" with the actual text you want to pass
        startActivity(intent);
    }

    public void matrixSub(View v) {
        if (row1 != row2 || col1 != col2|| firstMatrix==null|| secondMatrix==null) {
            Toast.makeText(this, "Розміри матриць не рівні", Toast.LENGTH_SHORT).show();
        }
        double[][] result = new double[row1][col1];
        for (int i = 0; i < row1; i++) {
            for (int j = 0; j < col1; j++) {
                result[i][j] = firstMatrix[i][j] - secondMatrix[i][j];
            }
        }
        String res = Arrays.deepToString(result);
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("matrixData", res); // Replace "New Text" with the actual text you want to pass
        startActivity(intent);
    }
    public void matrixMult(View v) {
        if (col1 != row2|| firstMatrix==null|| secondMatrix==null) {
            Toast.makeText(this, "Кількість стовпців першої матриці не дорівнює кількості рядків другої матриці.", Toast.LENGTH_SHORT).show();
        }

        double[][] result = new double[row1][col2];

        for (int i = 0; i < row1; i++) {
            for (int j = 0; j < col2; j++) {
                for (int k = 0; k < col1; k++) {
                    result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                }
            }
        }
        String res = Arrays.deepToString(result);
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("matrixData", res); // Replace "New Text" with the actual text you want to pass
        startActivity(intent);
    }

    public void firstMatrixSaveData(View v){
        saveData(1);
    }
    public void secondMatrixSaveData(View v){
        saveData(2);
    }
    public void saveData(int i) {
        try{
            FileOutputStream fileOutput = openFileOutput("matrix"+i, MODE_PRIVATE);
            if(i == 1){
                fileOutput.write((firstMatrixSizeEditText.getText().toString() +"#"+ firstMatrixEditText.getText().toString()).getBytes());
                fileOutput.close();
                firstMatrixSizeEditText.setText("");
                firstMatrixEditText.setText("");
            }
            else{
                fileOutput.write((secondMatrixSizeEditText.getText().toString() +"#"+ secondMatrixEditText.getText().toString()).getBytes());
                fileOutput.close();
                secondMatrixSizeEditText.setText("");
                secondMatrixEditText.setText("");
            }
            Toast.makeText(this, "Дані збережені", Toast.LENGTH_SHORT).show();
        }
        catch (FileNotFoundException ex){
            ex.printStackTrace();
            Toast.makeText(this, "Не можем опрацювати файл", Toast.LENGTH_SHORT).show();
        }
        catch (IOException ex){
            ex.printStackTrace();
            Toast.makeText(this, "Не можем опрацювати файл", Toast.LENGTH_SHORT).show();
        }
    }

    public void firstMatrixGetData(View v){
        getData(1);
    }
    public void secondMatrixGetData(View v){
        getData(2);
    }
    public void getData(int i) {
        try{
            FileInputStream fileInput = openFileInput("matrix"+i);
            InputStreamReader reader = new InputStreamReader(fileInput);
            BufferedReader bf = new BufferedReader(reader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bf.readLine();
            String matrix [] = line.split("#");
            if(i == 1){
                firstMatrixSizeEditText.setText(matrix[0]);
                firstMatrixEditText.setText(matrix[1]);
                String[] sizeParts = matrix[0].split("x");
                row1  = Integer.parseInt(sizeParts[0]);
                col1 = Integer.parseInt(sizeParts[1]);
                firstMatrix = parseMatrix(matrix[1]);
            }
            else{
                secondMatrixSizeEditText.setText(matrix[0]);
                secondMatrixEditText.setText(matrix[1]);
                String[] sizeParts = matrix[0].split("x");
                row2  = Integer.parseInt(sizeParts[0]);
                col2 = Integer.parseInt(sizeParts[1]);
                secondMatrix = parseMatrix(matrix[1]);
            }
        }
        catch (FileNotFoundException ex){
            ex.printStackTrace();
            Toast.makeText(this, "Не можем опрацювати файл", Toast.LENGTH_SHORT).show();
        }
        catch (IOException ex){
            ex.printStackTrace();
            Toast.makeText(this, "Не можем опрацювати файл", Toast.LENGTH_SHORT).show();
        }
    }
    public void startWorkWithMain(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void startWorkWithResults(View v){
        Intent intent = new Intent(this, ResultsActivity.class);
        startActivity(intent);
    }

}