package com.example.StudentsAttendanceSystemFacialRecognition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ConfirmAttendeeStudentList extends AppCompatActivity {

    ListView listStudentAttendeeConfirm;
    ListView listStudentAbsentConfirm;
    ArrayList<String> studentsAttendeeArr = new ArrayList<>();
    ArrayList<String> studentsAbsentArr = new ArrayList<>();
    ListView v1;
    ListView v2;
    Students_attendee students_attendee;
    Button pAttendee2;
    String date;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_attendee_student_list);

        initialize();
        setAdpterAttendee();
        setAdpterAbsent();


        v1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                studentsAbsentArr.add(studentsAttendeeArr.get(position));
                setAdpterAbsent2();
                studentsAttendeeArr.remove(studentsAttendeeArr.get(position));
                setAdpterAttendee2();
            }
        });


        v2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                studentsAttendeeArr.add(studentsAbsentArr.get(position));
                setAdpterAttendee2();
                studentsAbsentArr.remove(studentsAbsentArr.get(position));
                setAdpterAbsent2();
            }
        });

        pAttendee2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String course = getIntent().getStringExtra("course");
                String section = getIntent().getStringExtra("section");


//                Intent intent = new Intent(attendeeStudentsList.this, ConfirmAttendeeStudentList.class);
//                intent.putStringArrayListExtra("atArrayStudents", arrStudentAt1);
//                intent.putStringArrayListExtra("abArrayStudents", arrStudentAb1);
//                startActivity(intent);
//
                students_attendee.setNamesList(studentsAttendeeArr);
//
                databaseReference.child("studentsAttendee").child(course).child(section).child(date).setValue(students_attendee);


                Toast.makeText(ConfirmAttendeeStudentList.this, "Students attendee uploaded done! ", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void initialize() {
        v1 = findViewById(R.id.listStudentAttendeeConfirm);
        v2 = findViewById(R.id.listStudentAbsentConfirm);
        pAttendee2 = findViewById(R.id.postAttendee2);
        students_attendee = new Students_attendee();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    public void setAdpterAttendee() {
        studentsAttendeeArr = getIntent().getStringArrayListExtra("atArrayStudents");
        ArrayAdapter<String> adapterAttendee = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentsAttendeeArr);
        v1.setAdapter(adapterAttendee);
    }

    public void setAdpterAbsent() {
        studentsAbsentArr = getIntent().getStringArrayListExtra("abArrayStudents");
        ArrayAdapter<String> adapterAbsent = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentsAbsentArr);
        v2.setAdapter(adapterAbsent);
    }

    public void setAdpterAttendee2() {
        studentsAttendeeArr = getIntent().getStringArrayListExtra("atArrayStudents");
        ArrayAdapter<String> adapterAttendee = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentsAttendeeArr);
        v1.setAdapter(adapterAttendee);
    }

    public void setAdpterAbsent2() {
        studentsAbsentArr = getIntent().getStringArrayListExtra("abArrayStudents");
        ArrayAdapter<String> adapterAbsent = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentsAbsentArr);
        v2.setAdapter(adapterAbsent);
    }

    @Override
    public void onBackPressed() {
        Intent n = new Intent(this, MainActivity.class);
        startActivity(n);

        finish();

        super.onBackPressed();
    }


}
