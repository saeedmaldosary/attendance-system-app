package com.example.StudentsAttendanceSystemFacialRecognition;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowAttendanceSheet extends AppCompatActivity {

    ListView v1;
    ListView v2;
    ListView v3;
    Button eAttendanceSheet;
    ArrayList<String> arrStudentAt = new ArrayList<>();
    ArrayList<String> arrStudentAb = new ArrayList<>();
    ArrayList<String> arrAttendanceAbsentCounter = new ArrayList<>();
    int counterAttend;
    int counterAbsent;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_attendance_sheet);

        initialize();
        attendanceAbsentCounter();
        fillArrays();


        eAttendanceSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String course = getIntent().getStringExtra("course");
                String section = getIntent().getStringExtra("section");
                String date = getIntent().getStringExtra("date");
                Intent intent = new Intent(ShowAttendanceSheet.this, editAttendanceSheet.class);
                intent.putStringArrayListExtra("attendStudents", arrStudentAt);
                intent.putStringArrayListExtra("absentStudents", arrStudentAb);
                intent.putExtra("course", course);
                intent.putExtra("section", section);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });

    }//End of onCreate

    private void initialize() {
        v1 = findViewById(R.id.listStudentAttendeeSheet1);
        v2 = findViewById(R.id.listStudentAbsentSheet1);
        v3 = findViewById(R.id.listAttednanceAbsentCounter);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        eAttendanceSheet = findViewById(R.id.editAttendanceSheet);
        hideButton();
    }//End of initialize

    public void attendanceAbsentCounter() {
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
                DataSnapshot studentsNames = dataSnapshot.child("coursesInfo").child(course).child(section);
                DataSnapshot attendancePath = dataSnapshot.child("studentsAttendee").child(course).child(section);
                for (DataSnapshot childSnapshot : studentsNames.getChildren()) {
                    if (!childSnapshot.getKey().toString().equals("courseTeacher")) {

                        for (DataSnapshot childSnapshot2 : attendancePath.getChildren()) {
                            counterAbsent++;

                            DataSnapshot countt = dataSnapshot.child("studentsAttendee").child(course).child(section).child(childSnapshot2.getKey()).child("namesList");
                            for (DataSnapshot childSnapshot3 : countt.getChildren()) {
                                if (childSnapshot.getValue(String.class).equals(childSnapshot3.getValue(String.class))) {
                                    counterAttend++;

                                }
                            }//End of childsnapshot 3
                        }//End of childsnapshot 2
                        if (userRealName.equals(childSnapshot.getValue(String.class)) || usertype.equals("Teacher")) {

                            arrAttendanceAbsentCounter.add(childSnapshot.getValue(String.class) + " Attendance " + counterAttend + "  Absents " + (counterAbsent - counterAttend));
                        }
                        counterAttend = 0;
                        counterAbsent = 0;


                    }
                } //End of childsnapshot 1
                setAdapter3();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }//End of abasentCounter function

    public void fillArrays() {

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
                String date = getIntent().getStringExtra("date");
                DataSnapshot attendancePath = dataSnapshot.child("studentsAttendee").child(course).child(section).child(date).child("namesList");
                for (DataSnapshot childSnapshot : attendancePath.getChildren()) {
                    if (userRealName.equals(childSnapshot.getValue(String.class)) || usertype.equals("Teacher")) {
                        arrStudentAt.add(childSnapshot.getValue(String.class));
                    }
                }

                setAdapter1();

                DataSnapshot absentPath = dataSnapshot.child("coursesInfo").child(course).child(section);
                for (DataSnapshot ds : absentPath.getChildren()) {
                    if (absentPath.child(ds.getKey().toString()).getValue(String.class) != null
                            && !arrStudentAt.contains(absentPath.child(ds.getKey().toString()).getValue(String.class)) && !ds.getKey().toString().equals("courseTeacher")) {
                        if (userRealName.equals(absentPath.child(ds.getKey().toString()).getValue(String.class)) || usertype.equals("Teacher")) {
                            arrStudentAb.add(absentPath.child(ds.getKey().toString()).getValue(String.class));
                        }
                    }
                }

                setAdapter2();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }//End of fillArrays

    public void removeNames() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userEmail = user.getEmail();

                int index = userEmail.indexOf('@');
                userEmail = userEmail.substring(0, index);

                String usertype = dataSnapshot.child("usersInfo").child(userEmail).child("userType").getValue(String.class);
                if (usertype.equals("Student")) {
                    String userRealName = dataSnapshot.child("usersInfo").child(userEmail).child("userRealName").getValue(String.class);

                    for (int i = 0; i < arrStudentAt.size(); i++) {
                        if (!userRealName.equals(arrStudentAt.get(i))) {
                            arrStudentAt.remove(1);
                        }
                    }

                    for (int i = 0; i < arrStudentAb.size(); i++) {
                        if (!userRealName.equals(arrStudentAb.get(i))) {
                            arrStudentAb.remove(1);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setAdapter1() {
        ArrayAdapter<String> arrayAdapterAttendee1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrStudentAt);
        v1.setAdapter(arrayAdapterAttendee1);
    }//End setAdapter1

    public void setAdapter2() {
        ArrayAdapter<String> arrayAdapterAbsent1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrStudentAb);
        v2.setAdapter(arrayAdapterAbsent1);
    }//End setAdapter2

    public void setAdapter3() {
        ArrayAdapter<String> arrayAdapterStudentsNames1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrAttendanceAbsentCounter);
        v3.setAdapter(arrayAdapterStudentsNames1);
    }//End setAdapter2

    public void hideButton() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userEmail = user.getEmail();

                int index = userEmail.indexOf('@');
                userEmail = userEmail.substring(0, index);

                String usertype = dataSnapshot.child("usersInfo").child(userEmail).child("userType").getValue(String.class);
                if (usertype.equals("Student")) {
                    eAttendanceSheet.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }//End of hideButton


}
