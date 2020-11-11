package com.example.StudentsAttendanceSystemFacialRecognition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NameActivity extends AppCompatActivity {
    private EditText name;
    private Button next;
    DatabaseReference databaseReference;
    Students_attendee students_attendee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        name = findViewById(R.id.name);
        next = findViewById(R.id.nextButton);

        students_attendee = new Students_attendee();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Students_attendee");


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(name.getText().toString().equals("")) {
                    Toast.makeText(NameActivity.this, "Please enter student name", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(NameActivity.this, Training.class);
                    intent.putExtra("name", name.getText().toString().trim());
                    students_attendee.setStudentName(name.getText().toString());
                    databaseReference.push().setValue(students_attendee);
                    startActivity(intent);
                }

            }
        });
    }
}
