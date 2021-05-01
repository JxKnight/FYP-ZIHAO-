package com.example.fyp1.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.fyp1.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CheckAttendanceFragment extends Fragment {
    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private TextView textView, signStatus;
    private BarcodeDetector barcodeDetector;
    final int RequestCameraPermissionID = 1001;
    View view;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    public FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private String stuID;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        cameraSource.start(surfaceView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_check_attendance, container, false);

        surfaceView = (SurfaceView) view.findViewById(R.id.check_attendance_camera);
        textView = (TextView) view.findViewById(R.id.cameraTest);

        barcodeDetector = new BarcodeDetector.Builder(getActivity()).setBarcodeFormats(Barcode.QR_CODE).build();
        getUserData();
        cameraSource = new CameraSource.Builder(getActivity(), barcodeDetector).setRequestedPreviewSize(640, 480).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, RequestCameraPermissionID);
                    return;
                }
                try {
                    cameraSource.start(surfaceView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
                    @Override
                    public void release() {

                    }

                    @Override
                    public void receiveDetections(Detector.Detections<Barcode> detections) {
                        SparseArray<Barcode> qrCodes = detections.getDetectedItems();

                        if (qrCodes.size() != 0) {

                            textView.post(new Runnable() {
                                @Override
                                public void run() {
                                    Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                                    vibrator.vibrate(1000);
                                    textView.setText(qrCodes.valueAt(0).displayValue);
                                    String[] qrText = qrCodes.valueAt(0).displayValue.split("@");
                                    checkAttendance(qrText, qrCodes.valueAt(0).displayValue);
                                }
                            });
                            cameraSource.stop();
                        }
                    }
                });
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });
        return view;
    }

    private Boolean checkAttendance(String[] qrText, String qrFullText) {
        Boolean x = true;
        DateFormat df = new SimpleDateFormat("dd:MM:yyyy:HH:mm");
        DateFormat check = new SimpleDateFormat("ddMMyyyyHHmm");
        String currentTime = df.format((Calendar.getInstance().getTime()));
        String checkTime = check.format((Calendar.getInstance().getTime()));


        DocumentReference documentReference1 = fStore.collection("QRCode").document(qrFullText);
        documentReference1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String expiredTime = value.getString("ExpiredDateTime");
                Long test = Long.parseLong(expiredTime);
                AlertDialog.Builder qBuilder = new AlertDialog.Builder(getActivity());
                View qView = getLayoutInflater().inflate(R.layout.alert_sign_status, null);
                signStatus = qView.findViewById(R.id.sign_status);
                if (Long.parseLong(checkTime) > test) {
                    signStatus.setText("SIGN ATTENDANCE FAIL,PLEASE FIND LECTURER AFTER CLASS");
                } else {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Map<String, Object> note = new HashMap<>();
                            note.put("Check", 1);
                            note.put("CheckInTime", currentTime);
                            DocumentReference documentReference = fStore.collection("Attendance").document(qrFullText).collection("StudentList").document(stuID);
                            documentReference.set(note, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    AlertDialog qDialog = qBuilder.create();
                                    //transparent alert dialog
                                    qDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                    qDialog.show();
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            qDialog.dismiss();
                                            signStatus.setText("SIGN ATTENDANCE SUCCESS, GLHF");
                                        }
                                    }, 5000);
                                }
                            });
                        }
                    }, 500);
                }
                qBuilder.setView(qView);
            }
        });
        return x;
    }

    public void getUserData() {
        DocumentReference documentReference = fStore.collection("Users").document(fAuth.getCurrentUser().getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                stuID = (value.getString("id"));
            }
        });
    }
}