package com.example.StudentsAttendanceSystemFacialRecognition;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.StudentsAttendanceSystemFacialRecognition.NamesAdapter.NameAdapter;
import com.example.StudentsAttendanceSystemFacialRecognition.NamesAdapter.NamesNotDetectedAdapter;
import com.google.firebase.auth.FirebaseAuth;
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

public class DetectedNames extends AppCompatActivity {


    TextView txttotal;
    RecyclerView recyclerList;
    RecyclerView recyclerList2;
    int totalNames = 0;
    ArrayList<String> namesList = new ArrayList<>();
    ArrayList<String> namesListNew = new ArrayList<>();
    ArrayList<String> namesListTest = new ArrayList<>();
    ArrayList<String> namesListNotDetected = new ArrayList<>();
    ArrayList<String> namesListNotDetectedNew = new ArrayList<>();
    HashSet<String> hashSet = new HashSet<String>();
    HashSet<String> hashSet2 = new HashSet<String>();
    Button btnMarkAtt;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Students_attendee");
    DatabaseReference databaseReference2;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    Students_attendee students_attendee;
    FirebaseAuth firebaseAuth;
    CheckBox checkBox;

    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_names);

        initialize();
        firebaseAuth = FirebaseAuth.getInstance();

        if (namesList.size() >= 1 && namesListNotDetected.size() >= 1) {
            getExtras();
        }

        students_attendee = new Students_attendee();
        firebaseDatabase = FirebaseDatabase.getInstance();


        btnMarkAtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String course = getIntent().getStringExtra("course");
                //String section = getIntent().getStringExtra("section");


                //students_attendee.setNamesList(namesList);

               // databaseReference.child(course).child(section).child(date).setValue(students_attendee);


               // Toast.makeText(DetectedNames.this, "Students attendee uploaded done! ", Toast.LENGTH_SHORT).show();

                Intent n = new Intent(DetectedNames.this, confirmAttendee.class);

                n.putStringArrayListExtra("NameAt",namesList);

                n.putExtra("sizeNameAt",namesList.size());

                n.putStringArrayListExtra("NameAb",namesListNotDetected);

                n.putExtra("sizeNameAb",namesListNotDetected.size());

                startActivity(n);

            }
        });


    }


    public void setAdapter() {


        NameAdapter adapter = new NameAdapter(this, namesList, namesListNotDetected);


        recyclerList.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerList.setAdapter(adapter);

    }



    public void setAdapter2() {


        for (int d = 0; d < namesList.size(); d++) {
            if (hashSet2.contains(namesList.get(d))) {
                hashSet2.remove(namesList.get(d));
            }
        }

        namesListNotDetected.clear();
        namesListNotDetected.addAll(hashSet2);
        NamesNotDetectedAdapter adapter2 = new NamesNotDetectedAdapter(this, namesListNotDetected, namesList);
        recyclerList2.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerList2.setAdapter(adapter2);

    }



    public void getExtras() {


        if (getIntent().getExtras() != null) {

            totalNames = getIntent().getExtras().getInt("TotalNames");
            txttotal.setText("Total Name : " + totalNames);

            databaseReference2 = FirebaseDatabase.getInstance().getReference();
            databaseReference2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (int i = 0; i < totalNames; i++) {
                        String personName = getIntent().getExtras().getString("Name" + i);
                        String course = getIntent().getStringExtra("course");
                        String section = getIntent().getStringExtra("section");
                        DataSnapshot coursesInfo = dataSnapshot.child("coursesInfo").child(course).child(section);
                        for (DataSnapshot ds : coursesInfo.getChildren()) {
                            if (coursesInfo.child(ds.getKey().toString()).getValue(String.class) != null && personName != null && !ds.getKey().toString().equals("courseTeacher")) {
                                if (coursesInfo.child(ds.getKey().toString()).getValue(String.class).equals(personName)) {
                                    namesList.add(personName);
                                    setAdapter();

                                } else {

                                    hashSet2.add(coursesInfo.child(ds.getKey().toString()).getValue(String.class));
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


    private void initialize() {

        txttotal = findViewById(R.id.txttotal);
        recyclerList = findViewById(R.id.recyclerList);
        recyclerList2 = findViewById(R.id.recyclerList2);
        btnMarkAtt = findViewById(R.id.btnMarkAtt);


    }

    @Override
    public void onBackPressed() {
        Intent n = new Intent(this, MainActivity.class);
        startActivity(n);

        finish();

        super.onBackPressed();
    }
}
