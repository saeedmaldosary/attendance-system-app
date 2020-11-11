package com.example.StudentsAttendanceSystemFacialRecognition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivityStudents extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
//    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_students);

        firebaseAuth=FirebaseAuth.getInstance();
        CardView aButton = findViewById(R.id.attendanceSheetButtonStudents);


        aButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivityStudents.this, AttendanceSheetSelect.class));
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1Main:
                Toast.makeText(this,"item 1 selected",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item2Main:
                FirebaseAuth.getInstance().signOut();
                Intent intLogin = new Intent(MainActivityStudents.this,LoginActivity.class);
                startActivity(intLogin);
                finish();
                return true;
            case R.id.subItem1Main:
                Toast.makeText(this,"sub item 1 selected",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.subItem2Main:
                Toast.makeText(this,"sub item 2 selected",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
