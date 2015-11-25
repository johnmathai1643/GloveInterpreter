package com.example.john.gloveinterpreter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by john on 11/4/15.
 */
public class SQLiteHelper extends SQLiteOpenHelper{

    // database version
    private static final int database_VERSION = 1;
    private static final String TAG = "DATABASE";
   //database name
    private static final String DATABASE_NAME = "Memory";
    private static final int DATABASE_VERSION = 1;

   //database table
    private static final String TABLE_MATRIX = "matrix";
    private static final String TABLE_THRESHOLD = "threshold";
    private static final String TABLE_INPUTS = "input";
    private static final String TABLE_OUTPUTS = "output";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MATRIX = "CREATE TABLE "+TABLE_MATRIX+" ( id DOUBLE PRIMARY KEY )";
        String CREATE_THRESHOLD = "CREATE TABLE "+TABLE_THRESHOLD+" ( id DOUBLE PRIMARY KEY )";
        String CREATE_INPUTS = "CREATE TABLE "+TABLE_INPUTS+" ( x1 DOUBLE, x2 DOUBLE, x3 DOUBLE, x4 DOUBLE, x5 DOUBLE)";
        String CREATE_OUTPUTS = "CREATE TABLE "+TABLE_OUTPUTS+" ( y1 DOUBLE, y2 DOUBLE, y3 DOUBLE, y4 DOUBLE, y5 DOUBLE, y6 DOUBLE, y7 DOUBLE, y8 DOUBLE, y9 DOUBLE, y10 DOUBLE, y11 DOUBLE, y12 DOUBLE, y13 DOUBLE, y14 DOUBLE, y15 DOUBLE, y16 DOUBLE, y17 DOUBLE, y18 DOUBLE, y19 DOUBLE, y20 DOUBLE, y21 DOUBLE, y22 DOUBLE, y23 DOUBLE, y24 DOUBLE, y25 DOUBLE, y26 DOUBLE, y27 DOUBLE)";

        db.execSQL(CREATE_MATRIX);
        db.execSQL(CREATE_THRESHOLD);
        db.execSQL(CREATE_INPUTS);
        db.execSQL(CREATE_OUTPUTS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_MATRIX);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_THRESHOLD);
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_INPUTS + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_OUTPUTS + "'");
        this.onCreate(db);
    }

    public void onDrop(SQLiteDatabase db){
        Log.i("Drop Database","drop database");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_MATRIX + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_THRESHOLD + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_INPUTS + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_OUTPUTS + "'");

        onCreate(db);
    }

    public void wipedataMemory(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_INPUTS + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_OUTPUTS + "'");
        String CREATE_INPUTS = "CREATE TABLE "+TABLE_INPUTS+" ( x1 DOUBLE, x2 DOUBLE, x3 DOUBLE, x4 DOUBLE, x5 DOUBLE)";
        String CREATE_OUTPUTS = "CREATE TABLE "+TABLE_OUTPUTS+" ( y1 DOUBLE, y2 DOUBLE, y3 DOUBLE, y4 DOUBLE, y5 DOUBLE, y6 DOUBLE, y7 DOUBLE, y8 DOUBLE, y9 DOUBLE, y10 DOUBLE, y11 DOUBLE, y12 DOUBLE, y13 DOUBLE, y14 DOUBLE, y15 DOUBLE, y16 DOUBLE, y17 DOUBLE, y18 DOUBLE, y19 DOUBLE, y20 DOUBLE, y21 DOUBLE, y22 DOUBLE, y23 DOUBLE, y24 DOUBLE, y25 DOUBLE, y26 DOUBLE, y27 DOUBLE)";
        db.execSQL(CREATE_INPUTS);
        db.execSQL(CREATE_OUTPUTS);
        Log.i(TAG,"WIPE NEURAL DATA");
    }

    public void wipematrixMemory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_MATRIX + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_THRESHOLD + "'");
        String CREATE_MATRIX = "CREATE TABLE "+TABLE_MATRIX+" ( id DOUBLE PRIMARY KEY )";
        String CREATE_THRESHOLD = "CREATE TABLE "+TABLE_THRESHOLD+" ( id DOUBLE PRIMARY KEY )";
        db.execSQL(CREATE_MATRIX);
        db.execSQL(CREATE_THRESHOLD);
        Log.i(TAG,"WIPE NEURAL MEMORY");
    }

    public void Create_INPUTS(double input[][]) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for(int i = 0; i < input.length; i++)
        for (int j = 0; j < input.length; j++) {
            values.put("x1", input[i][j]);
            values.put("x2", input[i][j]);
            values.put("x3", input[i][j]);
            values.put("x4", input[i][j]);
            values.put("x5", input[i][j]);
            db.insert(TABLE_INPUTS, null, values);
        }
        db.close();
    }

    public void Create_OUTPUTS(double output[][]) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for(int i = 0; i < output.length; i++)
            for (int j = 0; j < output.length; j++) {
                values.put("y1", output[i][j]);
                values.put("y2", output[i][j]);
                values.put("y3", output[i][j]);
                values.put("y4", output[i][j]);
                values.put("y5", output[i][j]);
                values.put("y5", output[i][j]);
                values.put("y6", output[i][j]);
                values.put("y7", output[i][j]);
                values.put("y8", output[i][j]);
                values.put("y9", output[i][j]);
                values.put("y10", output[i][j]);
                values.put("y11", output[i][j]);
                values.put("y12", output[i][j]);
                values.put("y13", output[i][j]);
                values.put("y14", output[i][j]);
                values.put("y15", output[i][j]);
                values.put("y16", output[i][j]);
                values.put("y17", output[i][j]);
                values.put("y18", output[i][j]);
                values.put("y19", output[i][j]);
                values.put("y20", output[i][j]);
                values.put("y21", output[i][j]);
                values.put("y22", output[i][j]);
                values.put("y23", output[i][j]);
                values.put("y24", output[i][j]);
                values.put("y25", output[i][j]);
                values.put("y26", output[i][j]);
                values.put("y27", output[i][j]);
                db.insert(TABLE_OUTPUTS, null, values);
            }
        db.close();
    }

    public void Create_MATRIX(double matrix[]) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (int j = 0; j < matrix.length; j++) {
            values.put("id", matrix[j]);
            db.insert(TABLE_MATRIX, null, values);
        }
            db.close();
    }

    public void Create_THRESHOLD(double thresholds[]) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (int j = 0; j < thresholds.length; j++) {
            values.put("id", thresholds[j]);
            db.insert(TABLE_THRESHOLD, null, values);
        }
        db.close();
    }

//    public double[][] getAllInput(){
//        double[][] input;
//
//        String selectQuery = "SELECT id FROM " + TABLE_MATRIX;
//        SQLiteDatabase db = this.getReadableDatabase();
//        try {
//            Cursor cursor = db.rawQuery(selectQuery, null);
//            try {
//                if (cursor.moveToFirst()) {
//                    do {
//                        double x1 = Double.valueOf(cursor.getString(0));
//                        double x2 = Double.valueOf(cursor.getString(1));
//                        double x3 = Double.valueOf(cursor.getString(2));
//                        double x4 = Double.valueOf(cursor.getString(3));
//                        double x5 = Double.valueOf(cursor.getString(4));
//                        matrix_list.add(matrix_element);
//                    } while (cursor.moveToNext());
//                }
//
//            } finally {
//                try { cursor.close(); } catch (Exception ignore) {}
//            }
//
//        } finally {
//            try { db.close(); } catch (Exception ignore) {}
//        }
//
//        double matrix[] = new double[input_list.size()];
//
//        for (int j = 0; j < input_list.size(); j++) {
//            matrix[j] = input_list.get(j);
//        }
//
//        return input;
//    }

    public double[] getAllMatrix() {

        ArrayList<Double> matrix_list = new ArrayList<Double>();
        Double matrix_element;

        String selectQuery = "SELECT id FROM " + TABLE_MATRIX;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        matrix_element = Double.valueOf(cursor.getString(0));
                        matrix_list.add(matrix_element);
                    } while (cursor.moveToNext());
                }

            } finally {
                try { cursor.close(); } catch (Exception ignore) {}
            }

        } finally {
            try { db.close(); } catch (Exception ignore) {}
        }

        double matrix[] = new double[matrix_list.size()];

        for (int j = 0; j < matrix_list.size(); j++) {
          matrix[j] = matrix_list.get(j);
        }

        return matrix;
    }

    public double[] getAllThreshold() {

        ArrayList<Double> threshold_list = new ArrayList<Double>();
        Double threshold_element;

        String selectQuery = "SELECT id FROM " + TABLE_THRESHOLD;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        threshold_element = Double.valueOf(cursor.getString(0));
                        threshold_list.add(threshold_element);
                    } while (cursor.moveToNext());
                }

            } finally {
                try { cursor.close(); } catch (Exception ignore) {}
            }

        } finally {
            try { db.close(); } catch (Exception ignore) {}
        }

        double threshold[] = new double[threshold_list.size()];

        for (int j = 0; j < threshold_list.size(); j++) {
            threshold[j] = threshold_list.get(j);
        }

        return threshold;
    }


}
