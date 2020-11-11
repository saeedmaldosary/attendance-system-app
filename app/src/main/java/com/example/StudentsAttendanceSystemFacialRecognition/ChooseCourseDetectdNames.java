package com.example.StudentsAttendanceSystemFacialRecognition;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChooseCourseDetectdNames extends AppCompatActivity {

    private Spinner sectionSpinner;
    private Spinner courseSpinner;
    private Button detectStudentsBtn;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<String> sectionArrayList = new ArrayList<>();
    private ArrayList<String> courseArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_course_detectd_names);
        sectionSpinner = findViewById(R.id.spinnerSectionDetect);
        courseSpinner = findViewById(R.id.spinnerCourseDetect);
        detectStudentsBtn = findViewById(R.id.detectStudentsButton);


        sectionSpinner.setEnabled(false);


        showCourseDataSpinner();

        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String courseSelected = courseSpinner.getSelectedItem().toString();
                sectionArrayList.add("Section");
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ChooseCourseDetectdNames.this, R.layout.style_spinner, sectionArrayList);
                sectionSpinner.setAdapter(arrayAdapter);
                if (courseSelected.equals("Course")) {
                    sectionSpinner.setEnabled(false);
                    detectStudentsBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(ChooseCourseDetectdNames.this, "Please select course info!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    sectionArrayList.clear();
                    sectionSpinner.setEnabled(true);
                    showSectionDataSpinner(courseSelected);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }); //End of when course selected


        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String sectionSelected = sectionSpinner.getSelectedItem().toString();

                if (sectionSelected.equals("Section")) {
                    detectStudentsBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(ChooseCourseDetectdNames.this, "Please select course info!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {

                    detectStudentsBtn.setOnClickListener(new View.OnClickListener() {
                        @Override

                        public void onClick(View view) {
                            Intent intent = new Intent(ChooseCourseDetectdNames.this, Recognise.class);
                            String courseSelected = courseSpinner.getSelectedItem().toString();
                            intent.putExtra("course", courseSelected);
                            intent.putExtra("section", sectionSelected);
                            startActivity(intent);
                        }
                    });


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }); //End of when section selected


    } //End of on create

    private void showSectionDataSpinner(final String a) {

        sectionArrayList.add("Section");


        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userEmail = user.getEmail();

                int index = userEmail.indexOf('@');
                userEmail = userEmail.substring(0, index);

                String teacherName = dataSnapshot.child("usersInfo").child(userEmail).child("userRealName").getValue(String.class);

                DataSnapshot coursesInfo = dataSnapshot.child("coursesInfo").child(a);
                for (DataSnapshot ds : coursesInfo.getChildren()) {
                    if (coursesInfo.child(ds.getKey().toString()).hasChild("courseTeacher")) {
                        if (coursesInfo.child(ds.getKey().toString()).child("courseTeacher").getValue(String.class).equals(teacherName)) {
                            sectionArrayList.add(ds.getKey().toString());
                        }
                    }
                }
//                for (int j = 100; j <= 1000; j++) {
//                    if (coursesInfo.child("" + j).hasChild("courseTeacher")) {
//                        if (coursesInfo.child("" + j).child("courseTeacher").getValue(String.class).equals(teacherName)) {
//
//                            sectionArrayList.add(coursesInfo.child("" + j).getKey().toString());
//                        }
//                    }
//                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ChooseCourseDetectdNames.this, R.layout.style_spinner, sectionArrayList);
        sectionSpinner.setAdapter(arrayAdapter);

    } //End of showSectionDataSpinner

    private void showCourseDataSpinner() {
        courseArrayList.add("Course");


        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userEmail = user.getEmail();

                int index = userEmail.indexOf('@');
                userEmail = userEmail.substring(0, index);

                String teacherName = dataSnapshot.child("usersInfo").child(userEmail).child("userRealName").getValue(String.class);


                String[] CoursesO = {"IS395", "IS203", "IS441", "IS391"};
                for (int i = 0; i < CoursesO.length; i++) {
                    DataSnapshot coursesInfo = dataSnapshot.child("coursesInfo").child(CoursesO[i]);

                    for (DataSnapshot ds : coursesInfo.getChildren()) {
                        if (coursesInfo.child(ds.getKey().toString()).hasChild("courseTeacher")) {
                            if (coursesInfo.child(ds.getKey().toString()).child("courseTeacher").getValue(String.class).equals(teacherName)) {
                                courseArrayList.add(coursesInfo.getKey().toString());
                            }
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ChooseCourseDetectdNames.this, R.layout.style_spinner, courseArrayList);
        courseSpinner.setAdapter(arrayAdapter);

    } //End of showCourseDataSpinner


}


