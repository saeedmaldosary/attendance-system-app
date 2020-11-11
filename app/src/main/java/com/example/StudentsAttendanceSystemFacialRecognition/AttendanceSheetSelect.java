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

public class AttendanceSheetSelect extends AppCompatActivity {

    private Spinner sectionSpinner;
    private Spinner courseSpinner;
    private Spinner dateSpinner;
    private Button attendanceBtn;
    String section;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<String> sectionArrayList = new ArrayList<>();
    private ArrayList<String> courseArrayList = new ArrayList<>();
    private ArrayList<String> dateArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_sheet_select);


        sectionSpinner = findViewById(R.id.spinnerSectionAttendance);
        courseSpinner = findViewById(R.id.spinnerCourseAttendance);
        dateSpinner = findViewById(R.id.spinnerDateAttendance);
        attendanceBtn = findViewById(R.id.showAttendanceStudentButton);

        sectionSpinner.setEnabled(false);
        dateSpinner.setEnabled(false);

        showCourseDataSpinner();

        attendanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String courseSelected = courseSpinner.getSelectedItem().toString();
                String sectionSelected = sectionSpinner.getSelectedItem().toString();
                String dateSelected = dateSpinner.getSelectedItem().toString();
                Intent intent = new Intent(AttendanceSheetSelect.this, ShowAttendanceSheet.class);
                intent.putExtra("course", courseSelected);
                intent.putExtra("section", sectionSelected);
                intent.putExtra("date", dateSelected);
                startActivity(intent);
            }
        });

        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String courseSelected = courseSpinner.getSelectedItem().toString();
                sectionArrayList.add("Section");
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AttendanceSheetSelect.this, R.layout.style_spinner, sectionArrayList);
                sectionSpinner.setAdapter(arrayAdapter);
                if (courseSelected.equals("Course")) {
                    sectionSpinner.setEnabled(false);
                    attendanceBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(AttendanceSheetSelect.this, "Please select attendance info!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    sectionArrayList.clear();
                    sectionSpinner.setEnabled(true);
                    showSectionDataSpinner(courseSelected);

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String userEmail = user.getEmail();

                            int index = userEmail.indexOf('@');
                            userEmail = userEmail.substring(0, index);

                            String usertype = dataSnapshot.child("usersInfo").child(userEmail).child("userType").getValue(String.class);

                            if (usertype.equals("Student")) {
                                sectionSpinner.post(new Runnable() {
                                    public void run() {
                                        sectionSpinner.setSelection(1);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


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
                String courseSelected = courseSpinner.getSelectedItem().toString();
                dateArrayList.add("Date");
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AttendanceSheetSelect.this, R.layout.style_spinner, dateArrayList);
                dateSpinner.setAdapter(arrayAdapter);

                if (sectionSelected.equals("Section")) {
                    dateSpinner.setEnabled(false);
                    attendanceBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(AttendanceSheetSelect.this, "Please select attendance info!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    dateArrayList.clear();
                    dateSpinner.setEnabled(true);
                    showDateSpinner(sectionSelected, courseSelected);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }); //End of when section selected

        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String dateSelected = dateSpinner.getSelectedItem().toString();
                if (dateSelected.equals("Date") && dateArrayList.size() > 1) {
                    attendanceBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(AttendanceSheetSelect.this, "Please select attendance info!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (dateSelected.equals("Date") && dateArrayList.size() == 1) {
                    attendanceBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(AttendanceSheetSelect.this, "There is no date to select!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {

                    attendanceBtn.setOnClickListener(new View.OnClickListener() {
                        @Override

                        public void onClick(View view) {
                            Intent intent = new Intent(AttendanceSheetSelect.this, ShowAttendanceSheet.class);
                            String courseSelected = courseSpinner.getSelectedItem().toString();
                            String sectionSelected = sectionSpinner.getSelectedItem().toString();
                            intent.putExtra("course", courseSelected);
                            intent.putExtra("section", sectionSelected);
                            intent.putExtra("date", dateSelected.trim());
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }); // End of student name selected


    }//End of onCreate

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

                String usertype = dataSnapshot.child("usersInfo").child(userEmail).child("userType").getValue(String.class);
                if (usertype.equals("Teacher")) {
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
                } else {
                    String studentName = dataSnapshot.child("usersInfo").child(userEmail).child("userRealName").getValue(String.class);
                    String[] CoursesO = {"IS395", "IS203", "IS441", "IS391"};
                    for (int i = 0; i < CoursesO.length; i++) {
                        DataSnapshot coursesInfo = dataSnapshot.child("coursesInfo").child(CoursesO[i]);

                        for (DataSnapshot ds : coursesInfo.getChildren()) {
                            DataSnapshot coursesInfo2 = dataSnapshot.child("coursesInfo").child(CoursesO[i]).child(ds.getKey().toString());
                            for (DataSnapshot df : coursesInfo2.getChildren()) {
                                if (coursesInfo.child(ds.getKey().toString()).child(df.getKey().toString()).getValue(String.class) != null) {
                                    if (coursesInfo.child(ds.getKey().toString()).child(df.getKey().toString()).getValue(String.class).equals(studentName) && !courseArrayList.contains(coursesInfo.getKey().toString())) {
                                        courseArrayList.add(coursesInfo.getKey().toString());
                                    }
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


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AttendanceSheetSelect.this, R.layout.style_spinner, courseArrayList);
        courseSpinner.setAdapter(arrayAdapter);

    } //End of showCourseDataSpinner


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

                String usertype = dataSnapshot.child("usersInfo").child(userEmail).child("userType").getValue(String.class);

                if (usertype.equals("Teacher")) {
                    String teacherName = dataSnapshot.child("usersInfo").child(userEmail).child("userRealName").getValue(String.class);

                    DataSnapshot coursesInfo = dataSnapshot.child("coursesInfo").child(a);
                    for (DataSnapshot ds : coursesInfo.getChildren()) {
                        if (coursesInfo.child(ds.getKey().toString()).hasChild("courseTeacher")) {
                            if (coursesInfo.child(ds.getKey().toString()).child("courseTeacher").getValue(String.class).equals(teacherName)) {
                                sectionArrayList.add(coursesInfo.child(ds.getKey().toString()).getKey().toString());
                            }
                        }
                    }

                } else {
                    String studentName = dataSnapshot.child("usersInfo").child(userEmail).child("userRealName").getValue(String.class);
                    DataSnapshot coursesInfo = dataSnapshot.child("coursesInfo").child(a);
                    for (DataSnapshot ds : coursesInfo.getChildren()) {
                        DataSnapshot coursesInfo2 = dataSnapshot.child("coursesInfo").child(a).child(ds.getKey().toString());
                        for (DataSnapshot df : coursesInfo2.getChildren()) {
                            if (coursesInfo.child(ds.getKey().toString()).child(df.getKey().toString()).getValue(String.class) != null) {
                                if (coursesInfo.child(ds.getKey().toString()).child(df.getKey().toString()).getValue(String.class).equals(studentName)) {
                                    sectionArrayList.add(coursesInfo.child(ds.getKey().toString()).getKey().toString());
                                    section = coursesInfo.child(ds.getKey().toString()).getKey().toString();
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

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AttendanceSheetSelect.this, R.layout.style_spinner, sectionArrayList);
        sectionSpinner.setAdapter(arrayAdapter);


    } //End of showSectionDataSpinner

    private void showDateSpinner(final String sec, final String cou) {
        dateArrayList.add("Date");

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                DataSnapshot coursesInfo = dataSnapshot.child("studentsAttendee").child(cou).child(sec);

                for (DataSnapshot ds : coursesInfo.getChildren()) {
                    dateArrayList.add(ds.getKey());
                }

                if (dateArrayList.size() == 1) {
                    Toast.makeText(AttendanceSheetSelect.this, "There is no date to select!", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AttendanceSheetSelect.this, R.layout.style_spinner, dateArrayList);
        dateSpinner.setAdapter(arrayAdapter);

    } //End of showDateSpinner

    @Override
    public void onBackPressed() {
        Intent n = new Intent(this, MainActivity.class);
        startActivity(n);

        finish();

        super.onBackPressed();
    }

}
