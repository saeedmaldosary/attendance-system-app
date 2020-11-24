package com.example.StudentsAttendanceSystemFacialRecognition;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference databaseReference;
    CardView recognizeButton;
    CardView trainingButton;
    CardView aButton;

//    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();



        initl();


        recognizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ChooseCourseDetectdNames.class));
            }
        });

        trainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SelectCourseTrain.class));
            }
        });


        aButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AttendanceSheetSelect.class));
            }
        });


    }

    public void initl() {
        recognizeButton = findViewById(R.id.recognizeButton);
        trainingButton = findViewById(R.id.trainingButton);
        aButton = findViewById(R.id.attendanceSheetButton);
        hideCards();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.item2Main:
                FirebaseAuth.getInstance().signOut();
                Intent intLogin = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intLogin);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void hideCards() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userEmail = user.getEmail();

                int index = userEmail.indexOf('@');
                userEmail = userEmail.substring(0, index);

                String usertype = dataSnapshot.child("usersInfo").child(userEmail).child("userType").getValue(String.class);
                if (usertype.equals("Student")) {
                    recognizeButton.setVisibility(View.GONE);
                    trainingButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
