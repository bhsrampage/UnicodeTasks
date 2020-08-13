package com.example.unicodetasks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    EditText dateEditText ;
    EditText nameEditText;
    EditText numberEditText;
    EditText emailEditText;
    SharedPreferences sharedPreferences;

    ArrayList<String> information = new ArrayList<String>();
    ArrayList<String> newInformation = new ArrayList<String>();

    public void saveAndNext(View view)  {

        information.add(nameEditText.getText().toString());
        information.add(dateEditText.getText().toString());
        information.add(numberEditText.getText().toString());
        information.add(emailEditText.getText().toString());
        try{
        sharedPreferences.edit().putString("info", ObjectSerializer.serialize(information)).apply(); }
        catch (Exception e){
            e.printStackTrace();
        }

        Intent intent = new Intent(getApplicationContext(),ContactsActivity.class);
        startActivity(intent);

    }


    public void clear(View view){
        nameEditText.setText("");
        dateEditText.setText("");
        numberEditText.setText("");
        emailEditText.setText("");
        information.clear();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };
    private void updateLabel() {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateEditText.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newInformation.clear();
        sharedPreferences = this.getSharedPreferences("com.example.unicodetasks", Context.MODE_PRIVATE);

        dateEditText = findViewById(R.id.dateEditText);
        nameEditText = findViewById(R.id.nameEditText);
        numberEditText = findViewById(R.id.numberEditText);
        emailEditText = findViewById(R.id.emailEditText);

        try {
            newInformation =  (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("info", ObjectSerializer.serialize(new ArrayList<String>())));
           if(newInformation.size() > 0){
            nameEditText.setText(newInformation.get(0));
            dateEditText.setText(newInformation.get(1));
            numberEditText.setText(newInformation.get(2));
            emailEditText.setText(newInformation.get(3));}


        } catch (IOException e) {
            e.printStackTrace();
        }

        information.clear();

        dateEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
}