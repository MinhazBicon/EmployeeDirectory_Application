package com.example.employee_directoryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton Add_employee_btn;
    private EditText update_name, update_age, update_gender;
    private ImageView update_imageview;
    private Button update_Choose_image_btn, update_submit;
    ArrayList<Employee> employees_list;
    Employee_Adapter employee_adapter ;
    private MySQLiteHelper sqLiteHelper;
    final int REQUEST_CODE_GALLERY = 777;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Add_employee_btn = findViewById(R.id.add_employee_btn);
        recyclerView = findViewById(R.id.recyclerview_id);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        employees_list = new ArrayList<>();

         //SQLite database table create
        sqLiteHelper = new MySQLiteHelper(this, "Employee.DB", null, 2);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS EMPLOYEE (Id INTEGER PRIMARY KEY AUTOINCREMENT, Name VARCHAR, Age VARCHAR, Gender VARCHAR, IMAGE BLOG )");

        //getting all data from sqLite_database
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
        employee_adapter.notifyDataSetChanged();

        Add_employee_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this,Employee_information_insert_PopUp.class));
            }
        });

        employee_adapter.SetOnItemClickListener(new Employee_Adapter.OnLongItemClickListener() {
            @Override
            public void ONLongItemClick(final int position) {
                employees_list.get(position);
                CharSequence[] dialogOption = {"Update", "Delete"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose an Action");
                builder.setItems(dialogOption, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      if (i == 0){
                          //Update
                          Cursor cursor1 = sqLiteHelper.getData("SELECT Id FROM EMPLOYEE");
                          ArrayList<Integer> arrID = new ArrayList<>();
                          while (cursor1.moveToNext()){
                             arrID.add(cursor1.getInt(0));}
                         // update dialog show
                          UpdateDialog(MainActivity.this,arrID.get(position));
                      } else {
                          //Delete
                          Cursor cursor1 = sqLiteHelper.getData("SELECT Id FROM EMPLOYEE");
                          ArrayList<Integer> arrID = new ArrayList<>();
                          while (cursor1.moveToNext()){
                              arrID.add(cursor1.getInt(0));}
                          deleteDialog(arrID.get(position));
                      }
                    }
                });
                builder.show();
            }
        });
    }

    private void deleteDialog(final int position){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Warning !!!");
        builder.setMessage("Are you sure want to delete this?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
             try {
                 sqLiteHelper.DeleteData(position);
                 startActivity(new Intent(MainActivity.this,MainActivity.class));
                 dialogInterface.dismiss();
                 Toast.makeText(getApplicationContext(),"Delete Successful",Toast.LENGTH_SHORT).show();
             }catch (Exception error){
                 error.getStackTrace();
             }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
         builder.show();
    }

    private void  UpdateDialog(final Activity activity, final int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.employee_update_page);
        dialog.setTitle("Update");

        update_Choose_image_btn= dialog.findViewById(R.id.update_choose_image_btn);
        update_name =dialog.findViewById(R.id.update_name);
        update_age =dialog.findViewById(R.id.update_age);
        update_gender =dialog.findViewById(R.id.update_gender);
        update_imageview =dialog.findViewById(R.id.update_imageview);
        update_submit =dialog.findViewById(R.id.update_btn);

        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.9);
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();
        update_Choose_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // request for gallery
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new  String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY);
            }
        });

        update_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             try {
                  String UpdateName = update_name.getText().toString();
                  String UpdateAge = update_age.getText().toString();
                  String UpdateGender = update_gender.getText().toString();
                  byte[] image= Employee_information_insert_PopUp.imageViewTOByte(update_imageview);

                 sqLiteHelper.UpdateData(UpdateName,UpdateAge.trim(),UpdateGender.trim(),image,position);
                 startActivity(new Intent(MainActivity.this,MainActivity.class));
                 dialog.dismiss();
                 Toast.makeText(getApplicationContext(),"Updated Successful",Toast.LENGTH_SHORT).show();

             }catch (Exception error){
                 error.getStackTrace();
             }
            }

        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_GALLERY){
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(),"You don't permission to access file location!",Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                update_imageview.setImageBitmap(bitmap);

            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}