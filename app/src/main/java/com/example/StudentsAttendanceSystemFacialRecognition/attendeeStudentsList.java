package com.example.StudentsAttendanceSystemFacialRecognition;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

public class attendeeStudentsList extends AppCompatActivity {


    ListView listViewAt1;
    ListView listViewAb1;
    ArrayList<String> arrStudentAt1 = new ArrayList<>();
    ArrayList<String> arrStudentAb1 = new ArrayList<>();
    HashSet<String> hashSet = new HashSet<String>();
    Button eAttendance;
    DatabaseReference databaseReference3;
    int total;
    Students_attendee students_attendee;
    String date;
    DatabaseReference databaseReference;
    Boolean isAacResp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_students_list);

        initialize();

        getExtras();

        eAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String course = getIntent().getStringExtra("course");
                String section = getIntent().getStringExtra("section");


                Intent intent = new Intent(attendeeStudentsList.this, ConfirmAttendeeStudentList.class);
                intent.putStringArrayListExtra("atArrayStudents", arrStudentAt1);
                intent.putStringArrayListExtra("abArrayStudents", arrStudentAb1);
                intent.putExtra("course", course);
                intent.putExtra("section", section);
                startActivity(intent);


            }
        });


    }


    private void initialize() {
        listViewAt1 = findViewById(R.id.listStudentAttendee1);
        listViewAb1 = findViewById(R.id.listStudentAbsent1);
        eAttendance = findViewById(R.id.editAttendance);
        students_attendee = new Students_attendee();
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        isAacResp = false;
    }

    public void setAdapter1() {
        if(!isAacResp) {
            ArrayAdapter<String> arrayAdapterAttendee1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrStudentAt1);
            listViewAt1.setAdapter(arrayAdapterAttendee1);
            isAacResp = true;
        }
    }

    public void setAdapter2() {
        for (int d = 0; d < arrStudentAt1.size(); d++) {
            if (hashSet.contains(arrStudentAt1.get(d))) {
                hashSet.remove(arrStudentAt1.get(d));
            }
        }

        arrStudentAb1.clear();
        arrStudentAb1.addAll(hashSet);
        ArrayAdapter<String> arrayAdapterAbsent1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrStudentAb1);
        listViewAb1.setAdapter(arrayAdapterAbsent1);
    }


    public void getExtras() {


        if (getIntent().getExtras() != null) {

            total = getIntent().getExtras().getInt("TotalNames");

            databaseReference3 = FirebaseDatabase.getInstance().getReference();
            databaseReference3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (int i = 0; i < total; i++) {
                        String personName = getIntent().getExtras().getString("Name" + i);
                        String course = getIntent().getStringExtra("course");
                        String section = getIntent().getStringExtra("section");
                        DataSnapshot coursesInfo = dataSnapshot.child("coursesInfo").child(course).child(section);
                        for (DataSnapshot ds : coursesInfo.getChildren()) {
                            if (coursesInfo.child(ds.getKey().toString()).getValue(String.class) != null && personName != null && !ds.getKey().toString().equals("courseTeacher")) {
                                if (coursesInfo.child(ds.getKey().toString()).getValue(String.class).equals(personName)) {

                                    arrStudentAt1.add(personName);
                                    setAdapter1();


                                } else {
                                    hashSet.add(coursesInfo.child(ds.getKey().toString()).getValue(String.class));
                                    setAdapter2();
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
    }


}
