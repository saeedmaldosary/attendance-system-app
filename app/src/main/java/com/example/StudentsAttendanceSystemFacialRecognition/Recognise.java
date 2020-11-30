package com.example.StudentsAttendanceSystemFacialRecognition;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Environment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.StudentsAttendanceSystemFacialRecognition.components.CameraSource;
import com.example.StudentsAttendanceSystemFacialRecognition.components.CameraSourcePreview;
import com.example.StudentsAttendanceSystemFacialRecognition.components.GraphicOverlay;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Recognise extends AppCompatActivity {
    private static final String TAG = "Recognise";
    private static final int PERMISSION_REQUESTS = 1;

    private CameraSource cameraSource = null;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    private boolean facingBack = true;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private ArrayList<String> studentsNamesArrayList = new ArrayList<>();
    private PersonRecogniser personRecogniser;
    private String path;

    TextView txtNames;
    TextView txtNames22;
    Labels nameLabels;
    int nameNumber = 1;
    Button btnSubmit;

    ArrayList<String> namesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognise);

        preview = findViewById(R.id.preview);
        graphicOverlay = findViewById(R.id.overlay);
//        txtNames = findViewById(R.id.txtNames);
//        txtNames22 = findViewById(R.id.test123);
        btnSubmit = findViewById(R.id.btnSubmit);




        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDetectedFaceClass();
            }
        });
        if (allPermissionsGranted()) {
            createCameraSource();
            startCameraSource();
        } else {
            getRuntimePermissions();
        }

        //path = Environment.getExternalStorageDirectory() + "/facerecogOCV/";
        path = Environment.getExternalStorageDirectory() + "/facerecogMLKit/";
        nameLabels = new Labels(path);
        boolean success = (new File(path)).mkdirs();
        if (!success) {
            Log.e("Error", "Error creating directory");
        }

        if (OpenCVLoader.initDebug()) {
            Log.i(TAG, "OpenCV loaded");
        } else {
            Log.e(TAG, "OpenCV not loaded");
        }


        personRecogniser = new PersonRecogniser(path);
        personRecogniser.train();




    }

    private void goToDetectedFaceClass() {


        if (namesList.size() < 1) {
            Toast.makeText(Recognise.this, "No Names to Submit", Toast.LENGTH_SHORT).show();
        } else {


            Intent n = new Intent(Recognise.this, attendeeStudentsList.class);

            for (int i = 0; i < namesList.size(); i++) {
                n.putExtra("Name" + i, namesList.get(i));
            }
            n.putExtra("TotalNames", namesList.size());
            n.putExtra("course",getIntent().getStringExtra("course"));
            n.putExtra("section",getIntent().getStringExtra("section"));
            startActivity(n);


        }

    }


    public String recogniseFace(Bitmap bmp) {
        if (bmp == null)
            return "Nil";
        String res = personRecogniser.predict(bmp);
        if (res.equals("can't predict")) {
            //Toast.makeText(Recognise.this, "Need more than one person to predict", Toast.LENGTH_LONG).show();
            return "Nil";
        }


        final String personName = personRecogniser.getPersonNameOnly(bmp);


        if (!isinList(personName) && !personName.equalsIgnoreCase("unknown")) {


//            if (nameNumber == 1)
//                txtNames.setText(null);



            // txtNames.setText(String.format("%s\n%d)\t\t%s", txtNames.getText(), nameNumber++, personName));

            namesList.add(personName);


        }

        return res;
    }



    private boolean isinList(String personName) {


        for (String name : namesList) {
            if (name.equals(personName))
                return true;
        }

        return false;
    }

    private void createCameraSource() {
        if (cameraSource == null) {
            cameraSource = new CameraSource(this, graphicOverlay);
        }

        try {
            cameraSource.setMachineLearningFrameProcessor(new FaceDetectionProcessor(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d(TAG, "preview is null");
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "graphicOverlay is null");
                }
                preview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startCameraSource();
        /*
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, baseLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            baseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        preview.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }

    private String[] getRequiredPermissions() {
        try {
            PackageInfo info =
                    this.getPackageManager()
                            .getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            if (ps != null && ps.length > 0) {
                return ps;
            } else {
                return new String[0];
            }
        } catch (Exception e) {
            return new String[0];
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        if (allPermissionsGranted()) {
            createCameraSource();
            startCameraSource();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private static boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }
}
