package com.example.StudentsAttendanceSystemFacialRecognition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentsEmails extends AppCompatActivity {

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_emails);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        getEmails();
    }


    public void getEmails() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String course = getIntent().getStringExtra("course");
                String section = getIntent().getStringExtra("section");
                DataSnapshot sectionNames = dataSnapshot.child("coursesInfo").child(course).child(section);
                for (DataSnapshot childSnapshot : sectionNames.getChildren()) {

                    DataSnapshot studentsUserNames = dataSnapshot.child("usersInfo");
                    for (DataSnapshot childSnapshot2 : studentsUserNames.getChildren()) {
                        DataSnapshot studentsRealNames = dataSnapshot.child("usersInfo").child(childSnapshot2.getKey().toString());
                        for (DataSnapshot childSnapshot3 : studentsRealNames.getChildren()) {
                            if (!childSnapshot.getKey().toString().equals("courseTeacher")) {
                                if (childSnapshot3.getValue().toString().equals(childSnapshot.getValue().toString())) {
                                    dynmicName(childSnapshot.getValue().toString());
                                    dynmicEmail(childSnapshot2.getKey().toString());
                                    dynmicLine();
                                }
                            }
                        }
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void dynmicName(String name) {
        LinearLayout llMain = findViewById(R.id.rlMain2);

        TextView myText = new TextView(this);
        myText.setText(name);
        myText.setGravity(Gravity.CENTER);
        myText.setTextColor(Color.BLACK);
        llMain.addView(myText);
    }


    public void dynmicEmail(String name) {
        LinearLayout llMain = findViewById(R.id.rlMain2);

        TextView myText = new TextView(this);
        myText.setText(name + "@sm.imamu.edu.sa");
        myText.setGravity(Gravity.CENTER);
        myText.setTextColor(Color.BLACK);
        llMain.addView(myText);
    }

    public void dynmicLine() {
        LinearLayout llMain = findViewById(R.id.rlMain2);
        View v = new View(this);
        v.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                5
        ));
        v.setBackgroundColor(Color.parseColor("#B3B3B3"));

        llMain.addView(v);
    }


}