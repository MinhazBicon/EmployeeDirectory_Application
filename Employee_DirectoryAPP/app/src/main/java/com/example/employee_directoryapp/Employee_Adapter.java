package com.example.employee_directoryapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Employee_Adapter extends  RecyclerView.Adapter<Employee_Adapter.employee_ViewHolder> {
    private Context context;
    private ArrayList<Employee> EmployeeList;

    public Employee_Adapter(Context context, ArrayList<Employee> employeeList) {
        this.context = context;
        EmployeeList = employeeList;
    }

    @NonNull
    @Override
    public employee_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.employee_cardview, parent,false);
        return new employee_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull employee_ViewHolder holder, int position) {
         Employee employee = EmployeeList.get(position);
         byte[] employeeImage = employee.getImage();
         Bitmap bitmap  = BitmapFactory.decodeByteArray(employeeImage,0,employeeImage.length);
         holder.imageView.setImageBitmap(bitmap);
         holder.name.setText(employee.getName());
         holder.age.setText(employee.getAge());
         holder.gender.setText(employee.getGender());
    }

    @Override
    public int getItemCount() {
        return EmployeeList.size();
    }

    class employee_ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, age, gender;

        public employee_ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cardView_imageview);
            name = itemView.findViewById(R.id.cardView_employee_name);
            age = itemView.findViewById(R.id.cardView_employee_age);
            gender = itemView.findViewById(R.id.cardView_employee_gender);

        }
    }
}
