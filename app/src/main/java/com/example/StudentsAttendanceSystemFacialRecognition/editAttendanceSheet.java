package com.example.StudentsAttendanceSystemFacialRecognition;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class editAttendanceSheet extends AppCompatActivity {

    Button pAttendance;
    ListView v1;
    ListView v2;
    ArrayList<String> arrStudentAt = new ArrayList<>();
    ArrayList<String> arrStudentAb = new ArrayList<>();
    Students_attendee students_attendee;
    DatabaseReference databaseReference;
    TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_attendance_sheet);
        initialize();
        setAdpterAttendee();
        setAdpterAbsent();

        v1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                arrStudentAb.add(arrStudentAt.get(position));
                setAdpterAbsent();
                arrStudentAt.remove(arrStudentAt.get(position));
                setAdpterAttendee();
            }
        });

        v2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                arrStudentAt.add(arrStudentAb.get(position));
                setAdpterAttendee();
                arrStudentAb.remove(arrStudentAb.get(position));
                setAdpterAbsent();
            }
        });

        pAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String course = getIntent().getStringExtra("course");
                String section = getIntent().getStringExtra("section");
                String date = getIntent().getStringExtra("date");
                students_attendee.setNamesList(arrStudentAt);
                databaseReference.child("studentsAttendee").child(course).child(section).child(date).setValue(students_attendee);
                Toast.makeText(editAttendanceSheet.this, "Edit Students attendee uploaded done! ", Toast.LENGTH_SHORT).show();

            }
        });

    }//End of onCreate

    private void initialize() {
        v1 = findViewById(R.id.listEditStudentAttendee);
        v2 = findViewById(R.id.listEditStudentAbsent);
        pAttendance = findViewById(R.id.editSheetAttendance);
        students_attendee = new Students_attendee();
        databaseReference = FirebaseDatabase.getInstance().getReference();
//        tv1 = findViewById(R.id.attendanceDate);
//        String date = getIntent().getStringExtra("date");
//        tv1.setText(date);

    }//End of initialize

    public void setAdpterAttendee() {
        arrStudentAt = getIntent().getStringArrayListExtra("attendStudents");
        ArrayAdapter<String> adapterAttendee = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrStudentAt);
        v1.setAdapter(adapterAttendee);
    }

    public void setAdpterAbsent() {
        arrStudentAb = getIntent().getStringArrayListExtra("absentStudents");
        ArrayAdapter<String> adapterAbsent = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrStudentAb);
        v2.setAdapter(adapterAbsent);
    }


    @Override
    public void onBackPressed() {
        Intent n = new Intent(this, AttendanceSheetSelect.class);
        startActivity(n);

        finish();

        super.onBackPressed();
    }

}
