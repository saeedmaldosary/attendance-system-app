package com.example.StudentsAttendanceSystemFacialRecognition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllStudentsAbsentsAndAttenance extends AppCompatActivity {

    ArrayList<String> arrStudentAt = new ArrayList<>();
    ArrayList<String> arrStudentAb = new ArrayList<>();
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_students_absents_and_attenance);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        attendanceAndAbsents();
    }


    public void attendanceAndAbsents() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userEmail = user.getEmail();

                int index = userEmail.indexOf('@');
                userEmail = userEmail.substring(0, index);
                String userRealName = dataSnapshot.child("usersInfo").child(userEmail).child("userRealName").getValue(String.class);
                String usertype = dataSnapshot.child("usersInfo").child(userEmail).child("userType").getValue(String.class);

                String course = getIntent().getStringExtra("course");
                String section = getIntent().getStringExtra("section");

                DataSnapshot datePath = dataSnapshot.child("studentsAttendee").child(course).child(section);

                for (DataSnapshot childSnapshot : datePath.getChildren()) {
                    //To display date
                    arrStudentAt.clear();
                    attendanceStudents(childSnapshot.getKey());
                    //Text view method
                    attendanceStudents("Attendance students");
                    DataSnapshot attendancePath = dataSnapshot.child("studentsAttendee").child(course).child(section).child(childSnapshot.getKey()).child("namesList");
                    for (DataSnapshot childSnapshot2 : attendancePath.getChildren()) {
                        if (userRealName.equals(childSnapshot2.getValue(String.class)) || usertype.equals("Teacher")) {
                            //Text view method
                            attendanceStudents(childSnapshot2.getValue(String.class));
                            arrStudentAt.add(childSnapshot2.getValue(String.class));
                        }
                    }
                    attendanceStudents("Absents students");
                    DataSnapshot absentPath = dataSnapshot.child("coursesInfo").child(course).child(section);
                    for (DataSnapshot ds : absentPath.getChildren()) {
                        if (absentPath.child(ds.getKey().toString()).getValue(String.class) != null
                                && !arrStudentAt.contains(absentPath.child(ds.getKey().toString()).getValue(String.class)) && !ds.getKey().toString().equals("courseTeacher")) {
                            if (userRealName.equals(absentPath.child(ds.getKey().toString()).getValue(String.class)) || usertype.equals("Teacher")) {

                                //Text view method
                                attendanceStudents(absentPath.child(ds.getKey().toString()).getValue(String.class));
                                arrStudentAb.add(absentPath.child(ds.getKey().toString()).getValue(String.class));
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

    public void dynmic(String attendName) {
        LinearLayout llMain = findViewById(R.id.rlMain);
        String date = attendName.substring(0, 3);

        if(date.equals("202")) {
            View v = new View(this);
            v.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    5
            ));
            v.setBackgroundColor(Color.parseColor("#B3B3B3"));

            llMain.addView(v);
        }


        TextView myText = new TextView(this);
        myText.setText(attendName);
        if (attendName.equals("Absents students") || attendName.equals("Attendance students") || date.equals("202")) {
            myText.setGravity(Gravity.CENTER);
            myText.setTextColor(Color.BLACK);
        }
        llMain.addView(myText);
    }

    public void attendanceStudents(String arrStudentAt) {
        dynmic(arrStudentAt);
    }


}





