package com.example.StudentsAttendanceSystemFacialRecognition;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class SelectCourseTrain extends AppCompatActivity {

    private Spinner sectionSpinner;
    private Spinner courseSpinner;
    private Spinner studentsNamesSpinner;
    private Button trainStudentBtn;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<String> sectionArrayList = new ArrayList<>();
    private ArrayList<String> courseArrayList = new ArrayList<>();
    private ArrayList<String> studentsNamesArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_course_train);

        sectionSpinner = findViewById(R.id.spinnerSectionTrain);
        courseSpinner = findViewById(R.id.spinnerCourseTrain);
        studentsNamesSpinner = findViewById(R.id.spinnerStudentsNamesTrain);
        trainStudentBtn = findViewById(R.id.trainStudentButton);


        sectionSpinner.setEnabled(false);
        studentsNamesSpinner.setEnabled(false);
        //trainStudentBtn.setEnabled(false);

        showCourseDataSpinner();

        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String courseSelected = courseSpinner.getSelectedItem().toString();
                sectionArrayList.add("Section");
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SelectCourseTrain.this, R.layout.style_spinner, sectionArrayList);
                sectionSpinner.setAdapter(arrayAdapter);
                if (courseSelected.equals("Course")) {
                    sectionSpinner.setEnabled(false);
                    trainStudentBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(SelectCourseTrain.this, "Please select student info!", Toast.LENGTH_SHORT).show();
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
                String sectionSelected = sectionSpinner.getSelectedItem().toString();
                studentsNamesArrayList.add("Student Name");
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SelectCourseTrain.this, R.layout.style_spinner, studentsNamesArrayList);
                studentsNamesSpinner.setAdapter(arrayAdapter);

                if (sectionSelected.equals("Section")) {
                    studentsNamesSpinner.setEnabled(false);
                    trainStudentBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(SelectCourseTrain.this, "Please select student info!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    studentsNamesArrayList.clear();
                    studentsNamesSpinner.setEnabled(true);
                    showStudentsNamesDataSpinner(sectionSelected);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }); //End of when section selected

        studentsNamesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String studentSelected = studentsNamesSpinner.getSelectedItem().toString();
                if (studentSelected.equals("Student Name")) {
                    trainStudentBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(SelectCourseTrain.this, "Please select student info!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {

                    trainStudentBtn.setOnClickListener(new View.OnClickListener() {
                        @Override

                        public void onClick(View view) {
                            Intent intent = new Intent(SelectCourseTrain.this, Training.class);
                            intent.putExtra("name", studentSelected.trim());
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }); // End of student name selected


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
//                            sectionArrayList.add(coursesInfo.child("" + j).getKey().toString());
//                        }
//                    }
//                }

        }


        @Override
        public void onCancelled (@NonNull DatabaseError databaseError){

        }
    });


    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SelectCourseTrain.this, R.layout.style_spinner, sectionArrayList);
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
                            if (coursesInfo.child(ds.getKey().toString()).child("courseTeacher").getValue(String.class).equals(teacherName) && !courseArrayList.contains(coursesInfo.getKey().toString())) {
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


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SelectCourseTrain.this, R.layout.style_spinner, courseArrayList);
        courseSpinner.setAdapter(arrayAdapter);

    } //End of showCourseDataSpinner

    private void showStudentsNamesDataSpinner(final String sec) {
        studentsNamesArrayList.add("Student Name");

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
                    DataSnapshot coursesInfo = dataSnapshot.child("coursesInfo").child(CoursesO[i]).child(sec);


                    if (coursesInfo.hasChild("courseTeacher")) {
                        if (coursesInfo.child("courseTeacher").getValue(String.class).equals(teacherName)) {
                            for (DataSnapshot ds : coursesInfo.getChildren()) {
                                if (coursesInfo.child(ds.getKey().toString()).getValue(String.class) != null && !coursesInfo.child(ds.getKey().toString()).getValue(String.class).equals(teacherName))
                                    studentsNamesArrayList.add(coursesInfo.child(ds.getKey().toString()).getValue(String.class));
                            }
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SelectCourseTrain.this, R.layout.style_spinner, studentsNamesArrayList);
        studentsNamesSpinner.setAdapter(arrayAdapter);

    } //End of showStudentNamesDataSpinner


}


