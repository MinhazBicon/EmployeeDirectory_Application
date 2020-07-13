package com.example.employee_directoryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;

public class Employee_information_insert_PopUp extends Activity implements View.OnClickListener {
    private EditText Name, Age, Gender;
    private ImageView  choose_imageView;
    private Button chose_image_btn, submit_btn;
    public static MySQLiteHelper mySQLiteHelper;
    final int REQUEST_CODE_GALLERY = 999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_information_insert__pop_up);

        Init();

        mySQLiteHelper = new MySQLiteHelper(this,"Employee.DB",null,2);
        mySQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS EMPLOYEE (Id INTEGER PRIMARY KEY AUTOINCREMENT, Name VARCHAR, Age VARCHAR, Gender VARCHAR, IMAGE BLOG )");

        chose_image_btn.setOnClickListener(this);
        submit_btn.setOnClickListener(this);

        // PopUp Window Work
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        // getWindow().setBackgroundDrawableResource(flag);
        getWindow().setLayout((int) (width*.85),(int) (height*.65));

        //setting the Popup window position
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);
    }

    public void Init(){
        Name = findViewById(R.id.Name_id);
        Age = findViewById(R.id.Age_id);
        Gender = findViewById(R.id.Gender_id);
        choose_imageView = findViewById(R.id.choose_image_view);
        chose_image_btn = findViewById(R.id.ChoseImage_btn);
        submit_btn = findViewById(R.id.Submit_btn);
    }

    @Override
    public void onClick(View view) {
        if (view == chose_image_btn){
           // request for gallery
            ActivityCompat.requestPermissions(
                    Employee_information_insert_PopUp.this,
                    new  String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_GALLERY);
        }
        if (view == submit_btn){
             String name = Name.getText().toString();
             String age = Age.getText().toString();
             String gender = Gender.getText().toString();

           try {
               mySQLiteHelper.InsertData(name,age,gender,imageViewTOByte(choose_imageView));
               Toast.makeText(getApplicationContext(),"Record added successful",Toast.LENGTH_SHORT).show();
               startActivity(new Intent(Employee_information_insert_PopUp.this,MainActivity.class));
               finish();
           }catch (Exception e){
               e.printStackTrace();
           }

        }


    }

    public static byte[] imageViewTOByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, byteArrayOutputStream);
        byte[] bytes_array = byteArrayOutputStream.toByteArray();
        return bytes_array;
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
                choose_imageView.setImageBitmap(bitmap);

            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}