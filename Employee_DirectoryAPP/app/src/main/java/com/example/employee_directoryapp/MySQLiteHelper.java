package com.example.employee_directoryapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class MySQLiteHelper extends SQLiteOpenHelper {
    public MySQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql){
      SQLiteDatabase sqLiteDatabase = getWritableDatabase();
      sqLiteDatabase.execSQL(sql);
    }

    public void InsertData(String name, String age, String gender, byte[] image){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "INSERT INTO EMPLOYEE VALUES (NULL, ?, ?, ?, ?)";
        SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(sql);
        sqLiteStatement.clearBindings();
        sqLiteStatement.bindString(1,name);
        sqLiteStatement.bindString(2,age);
        sqLiteStatement.bindString(3,gender);
        sqLiteStatement.bindBlob(4, image);
        sqLiteStatement.executeInsert();
    }

    public void UpdateData(String name, String age, String gender, byte[] image, int id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String sql = "UPDATE EMPLOYEE SET Name = ?, Age = ?, Gender = ?, IMAGE = ? WHERE Id = ?";
        SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(sql);
        sqLiteStatement.bindString(1,name);
        sqLiteStatement.bindString(2,age);
        sqLiteStatement.bindString(3,gender);
        sqLiteStatement.bindBlob(4,image);
        sqLiteStatement.bindDouble(5,(double)id);
        sqLiteStatement.execute();
        sqLiteDatabase.close();
    }


    public Cursor getData(String sql){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor= sqLiteDatabase.rawQuery(sql,null);
       return cursor;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
