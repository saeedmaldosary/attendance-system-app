package com.example.StudentsAttendanceSystemFacialRecognition;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
    ArrayList<String> arrAttendanceAbsentCounterNames = new ArrayList<>();
    ArrayList<Integer> arrAttendanceCounter = new ArrayList<Integer>();
    ArrayList<Integer> arrAbsentCounter = new ArrayList<Integer>();
    int counterAttend;
    int counterAbsent;
    DatabaseReference databaseReference;
    TableLayout t1;

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
        t1 = findViewById(R.id.listTable);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        eAttendanceSheet = findViewById(R.id.editAttendanceSheet);
        hideButton();
    }//End of initialize


    public  void showTableLayout(String name,int attendance,int absent){
        TableLayout stk = (TableLayout) findViewById(R.id.listTable);

            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            t1v.setText("" + name);
        t1v.setGravity(Gravity.LEFT);
        t1v.setTextSize(14);

            tbrow.addView(t1v);
            TextView t2v = new TextView(this);
            t2v.setText(" " +attendance);
        t2v.setGravity(Gravity.CENTER);
        t2v.setTextSize(14);
            tbrow.addView(t2v);
            TextView t3v = new TextView(this);
            t3v.setText("" + absent);
        t3v.setGravity(Gravity.CENTER);
        t3v.setTextSize(14);
            tbrow.addView(t3v);

            stk.addView(tbrow);

    }//End of tablelayout


    public void attendanceAbsentCounter() {

        TableLayout stk = (TableLayout) findViewById(R.id.listTable);
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText("Names ");
        tv0.setTextColor(Color.BLACK);
        tv0.setTextSize(16);
        tv0.setGravity(Gravity.LEFT);
//        tv0.setBackgroundResource(R.drawable.border);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText(" Attendance ");
        tv1.setTextColor(Color.BLACK);
        tv1.setTextSize(16);
        tv1.setGravity(Gravity.CENTER);
//        tv1.setBackgroundResource(R.drawable.border);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText(" Absents ");
        tv2.setTextColor(Color.BLACK);
        tv2.setGravity(Gravity.RIGHT);
        tv2.setTextSize(16);
//        tv2.setBackgroundResource(R.drawable.border);
        tbrow0.addView(tv2);
        stk.addView(tbrow0);

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
                            showTableLayout(childSnapshot.getValue(String.class),counterAttend,(counterAbsent - counterAttend));
                            arrAttendanceAbsentCounterNames.add(childSnapshot.getValue(String.class));
                            arrAttendanceCounter.add(counterAttend);
                            arrAbsentCounter.add(counterAbsent - counterAttend);
                        }
                        counterAttend = 0;
                        counterAbsent = 0;


                    }
                } //End of childsnapshot 1
//                setAdapter3();
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
