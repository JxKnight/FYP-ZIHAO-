package com.example.fyp1.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.fyp1.Adapter.ClassAbsentListAdapter;
import com.example.fyp1.Adapter.ClassSubjectAdapter;
import com.example.fyp1.Adapter.HomeAnnouncementListAdapter;
import com.example.fyp1.Adapter.LecturerSubjectAdapter;
import com.example.fyp1.Adapter.LecturerSubjectDetailLectureAdapter;
import com.example.fyp1.Adapter.LecturerSubjectDetailTutorialAdapter;
import com.example.fyp1.Adapter.TimeListAdapter;
import com.example.fyp1.Class.Announcement;
import com.example.fyp1.Class.Attendance;
import com.example.fyp1.Class.Subject;
import com.example.fyp1.Class.SubjectLectureClass;
import com.example.fyp1.Class.SubjectTutorialClass;
import com.example.fyp1.Class.User;
import com.example.fyp1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static android.app.Activity.RESULT_OK;

public class ClassFragment extends Fragment {
    View view;
    private FirebaseFirestore fstore = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    ArrayList<String> SubjectList = new ArrayList<>();
    ListView SubjectListView;
    Integer count = 0,attend=0;
    ArrayList<User> absentList = new ArrayList<>();
    ArrayList<String> subjectDoc = new ArrayList<>();
    ArrayList<String> y = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_class, container, false);
        SubjectListView = view.findViewById(R.id.fragment_class_list);
        fstore.collection("Users").document(fAuth.getUid()).collection("Subject").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                SubjectList.clear();
                SubjectListView.setAdapter(null);
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    SubjectList.add(snapshot.getId());
                    final ClassSubjectAdapter adapter = new ClassSubjectAdapter(getActivity(), R.layout.adapter_class_subject_list, SubjectList);
                    adapter.notifyDataSetChanged();
                    //lectureClassList.setAdapter(null);
                    SubjectListView.setAdapter(adapter);
                }
            }
        });

        SubjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder addFriendBuilder = new AlertDialog.Builder(getActivity());
                View qView = getLayoutInflater().inflate(R.layout.dialog_class_subject_class, null);
                ListView listView = qView.findViewById(R.id.class__subject_class_list);
                addFriendBuilder.setView(qView);
                AlertDialog dialog = addFriendBuilder.create();
                dialog.show();

                fstore.collection("Attendance").whereEqualTo("SubjectCode", SubjectList.get(i)).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                        subjectDoc.clear();
                        listView.setAdapter(null);
                        for (DocumentChange userDoc : snapshots.getDocumentChanges()) {
                            User user = userDoc.getDocument().toObject(User.class);
                            if (user.getDoc() != null) {
                                subjectDoc.add(user.getDoc());

                            }
                        }
                        final ClassSubjectAdapter adapter = new ClassSubjectAdapter(getActivity(), R.layout.adapter_class_subject_list, subjectDoc);
                        adapter.notifyDataSetChanged();
                        listView.setAdapter(adapter);
                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int ii, long l) {
                        count=0;
                        attend=0;
                        AlertDialog.Builder addFriendBuilder = new AlertDialog.Builder(getActivity());
                        View qView = getLayoutInflater().inflate(R.layout.dialog_class_details, null);
                        LinearLayout llView = qView.findViewById(R.id.class_details_ll);
                        ListView listVView = qView.findViewById(R.id.class_details_lv);
                        TextView tv1 = qView.findViewById(R.id.attendance_amount);
                        TextView tv2 = qView.findViewById(R.id.class_details_time);
                        addFriendBuilder.setView(qView);
                        AlertDialog dialog = addFriendBuilder.create();
                        dialog.show();
                        llView.setVisibility(View.GONE);
                        listVView.setAdapter(null);
                        absentList.clear();
                        fstore.collection("Attendance").document(subjectDoc.get(ii)).collection("StudentList").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

                                if (queryDocumentSnapshots == null) {
                                    Toast.makeText(getActivity(), "No StudentList", Toast.LENGTH_LONG).show();
                                } else {
                                    String[] x = subjectDoc.get(ii).split("@");
                                    tv2.setText(x[2]);
                                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                        //Toast.makeText(getActivity(), snapshot.getId(), Toast.LENGTH_SHORT).show();
                                        count++;
                                        if (snapshot.get("Check").equals("0")) {
                                            y.add(snapshot.getId());
                                        }else{
                                            attend++;
                                        }
                                        if (y.size() > 0) {
                                            llView.setVisibility(View.VISIBLE);
                                        }

                                    }
                                    tv1.setText(attend + "/" + count);
                                    for (int i=0;i<y.size();i++) {
                                        fstore.collection("Users").whereEqualTo("id", y.get(i)).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                                                for (DocumentChange userDoc : snapshots.getDocumentChanges()) {
                                                    User user = userDoc.getDocument().toObject(User.class);
                                                    absentList.add(user);
                                                    ClassAbsentListAdapter adapter = new ClassAbsentListAdapter(getActivity(), R.layout.adapter_class_absent, absentList);
                                                    adapter.notifyDataSetChanged();
                                                    listVView.setAdapter(adapter);
                                                }

                                            }
                                        });
                                    }

                                }
                            }
                        });
                    }
                });


            }
        });


        return view;
    }
}