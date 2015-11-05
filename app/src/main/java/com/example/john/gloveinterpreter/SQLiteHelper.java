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

   //database name
    private static final String DATABASE_NAME = "Memory";
    private static final int DATABASE_VERSION = 1;

   //database table
    private static final String TABLE_MATRIX = "matrix";
    private static final String TABLE_THRESHOLD = "threshold";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MATRIX = "CREATE TABLE "+TABLE_MATRIX+" ( id DOUBLE PRIMARY KEY )";
        String CREATE_THRESHOLD = "CREATE TABLE "+TABLE_THRESHOLD+" ( id DOUBLE PRIMARY KEY )";
        db.execSQL(CREATE_MATRIX);
        db.execSQL(CREATE_THRESHOLD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_MATRIX);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_THRESHOLD);
        this.onCreate(db);
    }

    public void onDrop(SQLiteDatabase db){
        Log.i("Drop Database","drop database");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_MATRIX + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_THRESHOLD + "'");
        onCreate(db);
    }

    public void wipeMemory(){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("WIPE MEMORY","WIPE NEURAL MEMORY");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_MATRIX + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_THRESHOLD + "'");

        String CREATE_MATRIX = "CREATE TABLE "+TABLE_MATRIX+" ( id DOUBLE PRIMARY KEY )";
        String CREATE_THRESHOLD = "CREATE TABLE "+TABLE_THRESHOLD+" ( id DOUBLE PRIMARY KEY )";
        db.execSQL(CREATE_MATRIX);
        db.execSQL(CREATE_THRESHOLD);
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
//                        Log.d("MATRIX", String.valueOf(matrix_element));
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
//            Log.d("THRESHOLD", String.valueOf(threshold_list.get(j)));
            threshold[j] = threshold_list.get(j);
        }

        return threshold;
    }

}
