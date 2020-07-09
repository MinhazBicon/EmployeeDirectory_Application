package com.example.employee_directoryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button Add_employee_btn;
    ArrayList<Employee> employees_list=null;
    Employee_Adapter employee_adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Add_employee_btn = findViewById(R.id.add_employee_btn);
        recyclerView = findViewById(R.id.recyclerview_id);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        employees_list = new ArrayList<>();

        //getting all data from sqLite_database

        MySQLiteHelper sqLiteHelper = new MySQLiteHelper(this, "Employee.DB", null, 2);
        Cursor cursor = sqLiteHelper.getData("SELECT * FROM EMPLOYEE");
          employees_list.clear();

          while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String age = cursor.getString(2);
            String gender = cursor.getString(3);
            byte[] image = cursor.getBlob(4);

            employees_list.add(new Employee(id,name,age,gender,image));
        }
        employee_adapter  = new Employee_Adapter(this,employees_list);
        recyclerView.setAdapter(employee_adapter);

        Add_employee_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Employee_information_insert_PopUp.class));
            }
        });
    }
}