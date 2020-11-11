package com.example.StudentsAttendanceSystemFacialRecognition;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class confirmAttendee extends AppCompatActivity {

    ListView listViewAt;
    ListView listViewAb;
    ArrayList<String> arrStudentAt;
    ArrayList<String> arrStudentAb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_attendee);
        listViewAt = findViewById(R.id.listStudentAttendee);
        listViewAb = findViewById(R.id.listStudentAbsent);
        arrStudentAt = new ArrayList<>();
        arrStudentAb = new ArrayList<>();

        readAt();

    }

    public void readAt() {



        int studentsAtSize = getIntent().getExtras().getInt("sizeNameAt");
        ArrayList<String> arrStudentAtIntent = getIntent().getStringArrayListExtra("NameAt");

        int studentsAbSize = getIntent().getExtras().getInt("sizeNameAb");
        ArrayList<String> arrStudentAbIntent = getIntent().getStringArrayListExtra("NameAb");

        for (int i = 0; i < studentsAtSize; i++) {
            String studentAt = arrStudentAtIntent.get(i);
            if (!arrStudentAbIntent.contains(studentAt) && !arrStudentAb.contains(studentAt) &&  !arrStudentAt.contains(studentAt)) {
                arrStudentAt.add(studentAt);
            } else {
                arrStudentAb.add(studentAt);
            }

        }


        for (int i = 0; i < studentsAbSize; i++) {
            String studentAb = arrStudentAbIntent.get(i);
            if (!arrStudentAtIntent.contains(studentAb) && !arrStudentAt.contains(studentAb) && !arrStudentAb.contains(studentAb)) {
                arrStudentAb.add(studentAb);
            } else {
                arrStudentAt.add(studentAb);
            }
        }


        for (int i=0; i<arrStudentAt.size(); i++) {
            if (arrStudentAb.contains(arrStudentAt.get(i))){
                arrStudentAb.remove(arrStudentAt.get(i));
            }
        }

        for (int i=0; i<arrStudentAb.size(); i++) {
            if (arrStudentAt.contains(arrStudentAb.get(i))){
                arrStudentAt.remove(arrStudentAb.get(i));
            }
        }

        ArrayAdapter<String> arrayAdapterAttendee = new ArrayAdapter<>(confirmAttendee.this, android.R.layout.simple_list_item_1, arrStudentAt);
        listViewAt.setAdapter(arrayAdapterAttendee);

        ArrayAdapter<String> arrayAdapterAbsent = new ArrayAdapter<>(confirmAttendee.this, android.R.layout.simple_list_item_1, arrStudentAb);
        listViewAb.setAdapter(arrayAdapterAbsent);

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
