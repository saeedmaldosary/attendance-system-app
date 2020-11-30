package com.example.StudentsAttendanceSystemFacialRecognition;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText emailL, passwordL;
    Button login;
    FirebaseAuth firebaseAuth;
    String userT;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        emailL = findViewById(R.id.emailLogin);
        passwordL = findViewById(R.id.passwordLogin);
        login = findViewById(R.id.loginButton);

        authStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String userEmail = user.getEmail();

                    int index = userEmail.indexOf('@');
                    userEmail = userEmail.substring(0, index);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("usersInfo").child(userEmail).child("userType");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue().equals("Teacher")) {
                                Toast.makeText(LoginActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                            } else {
                                    Toast.makeText(LoginActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(LoginActivity.this, MainActivityStudents.class);
                                    startActivity(i);
                            }
                        }

                        @Override
                        public void onCancelled(@androidx.annotation.NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    Toast.makeText(LoginActivity.this, "Please login!", Toast.LENGTH_SHORT);
                }
            }
        };
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String password23 = passwordL.getText().toString();
                String emailToUserName2323 = emailL.getText().toString().toLowerCase();

                if (emailToUserName2323.isEmpty()) {
                    emailL.setError("Please enter your email");
                    emailL.requestFocus();
                } else if (password23.isEmpty()) {
                    passwordL.setError("Please enter your password!");
                    passwordL.requestFocus();
                } else if (password23.isEmpty() && emailToUserName2323.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
                } else {

                    String emailLowerCase = emailL.getText().toString().toLowerCase();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("usersInfo").child(emailL.getText().toString().toLowerCase());
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild("userType")) {
                                userT = dataSnapshot.child("userType").getValue().toString();


                                String emailToUserName;
                                if (userT.equals("Student")) {
                                    emailToUserName = emailL.getText().toString() + "@sm.imamu.edu.sa";
                                } else {
                                    emailToUserName = emailL.getText().toString() + "@imamu.edu.sa";
                                }
                                String password = passwordL.getText().toString();
                                if (emailToUserName.isEmpty()) {
                                    emailL.setError("Please enter your email");
                                    emailL.requestFocus();
                                } else if (password.isEmpty()) {
                                    passwordL.setError("Please enter your password!");
                                    passwordL.requestFocus();
                                } else if (password.isEmpty() && emailToUserName.isEmpty()) {
                                    Toast.makeText(LoginActivity.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
                                } else if (!(password.isEmpty() && emailToUserName.isEmpty())) {
                                    firebaseAuth.signInWithEmailAndPassword(emailToUserName, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()) {
                                                emailL.requestFocus();
                                                passwordL.requestFocus();
                                                Toast.makeText(LoginActivity.this, "Login error, please login again!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                if (userT.equals("Student")) {
                                                    Intent intToHome = new Intent(LoginActivity.this, MainActivityStudents.class);
                                                    startActivity(intToHome);
                                                } else {
                                                    Intent intToHome = new Intent(LoginActivity.this, MainActivity.class);
                                                    startActivity(intToHome);
                                                }
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(LoginActivity.this, "Error occurred!", Toast.LENGTH_SHORT).show();

                                }
                            }else {
                                Toast.makeText(LoginActivity.this, "Please check your username or password!", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@androidx.annotation.NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
