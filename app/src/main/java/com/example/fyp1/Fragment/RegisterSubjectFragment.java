package com.example.fyp1.Fragment;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.fyp1.Adapter.RegisteredSubjectAdapter;
import com.example.fyp1.Adapter.StudyMaterialListAdapter;
import com.example.fyp1.Adapter.SubjectAdapter;
import com.example.fyp1.Adapter.SubjectDetailLectureAdapter;
import com.example.fyp1.Adapter.SubjectDetailTutorialAdapter;
import com.example.fyp1.Adapter.SubjectStudyMaterialByWeekList;
import com.example.fyp1.Class.StudyMaterial;
import com.example.fyp1.R;
import com.example.fyp1.Class.Subject;
import com.example.fyp1.Class.SubjectLectureClass;
import com.example.fyp1.Class.SubjectTutorialClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class RegisterSubjectFragment extends Fragment {
    private FirebaseFirestore fstore = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    private FirebaseAuth fAuth;
    View view;
    String UID, SID;
    ListView subjectList, subjectLectureList, subjectTutorialList;
    ListView registeredListView;
    ArrayList<Subject> subjectArrayList = new ArrayList<>();
    ArrayList<SubjectLectureClass> LectureList = new ArrayList<>();
    ArrayList<SubjectTutorialClass> TutorialList = new ArrayList<>();
    ArrayList<String>ExtraFileList = new ArrayList<>();
    ArrayList<String>LectureNoteList = new ArrayList<>();
    ArrayList<String>TutorialNoteList = new ArrayList<>();
    ArrayList<String> weekList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_subject, container, false);
        fAuth = FirebaseAuth.getInstance();
        UID = fAuth.getCurrentUser().getUid();
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("UTAR_PREFS", MODE_PRIVATE);
        SID = sharedPreferences.getString("id", "");
        registeredListView = view.findViewById(R.id.fragment_registered_subject_list);
        getRegisteredSubject();

        registeredListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder sswlBuilder = new AlertDialog.Builder(getActivity());
                View sswListView = getLayoutInflater().inflate(R.layout.dialog_subject_studymaterial_week_list, null);
                ListView sswlw = sswListView.findViewById(R.id.study_material_week_list);
                sswlBuilder.setView(sswListView);
                AlertDialog sswlDialog = sswlBuilder.create();
                sswlDialog.show();
                sswlw.setAdapter(null);
                weekList.clear();
                fstore.collection("Subjects").document(subjectArrayList.get(i).getSubjectCode()).collection("StudyMaterial").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        for(QueryDocumentSnapshot snapshot:queryDocumentSnapshots){
                            weekList.add(snapshot.getId());
                        }
                        final SubjectStudyMaterialByWeekList adapter = new SubjectStudyMaterialByWeekList(getActivity(), R.layout.adapter_subject_material_details, weekList);
                        adapter.notifyDataSetChanged();
                        sswlw.setAdapter(adapter);
                    }
                });

                sswlw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int ii, long l) {
                        AlertDialog.Builder swfBuilder = new AlertDialog.Builder(getActivity());
                        View swfListView = getLayoutInflater().inflate(R.layout.dialog_studymaterials_weeks_field, null);
                        LinearLayout LLL= swfListView.findViewById(R.id.Study_Materials_Lecture_LL);
                        LinearLayout TLL= swfListView.findViewById(R.id.Study_Materials_Tutorial_LL);
                        LinearLayout EFLL= swfListView.findViewById(R.id.Study_Materials_ExtraFiles_LL);
                        ListView LLV = swfListView.findViewById(R.id.Study_Materials_Lecture_ListView);
                        ListView TLV = swfListView.findViewById(R.id.Study_Materials_Tutorial_ListView);
                        ListView EFLV = swfListView.findViewById(R.id.Study_Materials_ExtraFiles_ListView);
                        swfBuilder.setView(swfListView);
                        AlertDialog swfDialog = swfBuilder.create();
                        swfDialog.show();
                        LLL.setVisibility(View.GONE);
                        TLL.setVisibility(View.GONE);
                        EFLL.setVisibility(View.GONE);
                        fstore.collection("Subjects").document(subjectArrayList.get(i).getSubjectCode()).collection("StudyMaterial").document(weekList.get(i)).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                ExtraFileList = (ArrayList<String>) value.get("ExtraFiles");
                                LectureNoteList = (ArrayList<String>)value.get("LectureNote");
                                TutorialNoteList = (ArrayList<String>)value.get("TutorialNote");
                                if(ExtraFileList.size()==0||ExtraFileList==null){
                                    Toast.makeText(getContext(), "No Extra Files", Toast.LENGTH_LONG).show();
                                }else{
                                    EFLL.setVisibility(View.VISIBLE);
                                    for(String var:ExtraFileList){
                                        DocumentReference documentReference = fstore.collection("StudyMaterialFiles").document(var);
                                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                                ArrayList<StudyMaterial> SMList = new ArrayList<>();
                                                StudyMaterial SM = new StudyMaterial(value.getString("CreatedDate"),value.getString("Name"),value.getString("doc"));
                                                SMList.add(SM);
                                                final StudyMaterialListAdapter adapter = new StudyMaterialListAdapter(getActivity(), R.layout.adapter_study_material_list, SMList);
                                                EFLV.setAdapter(adapter);
                                                EFLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                        getDownload(SMList.get(i).getDoc(),SMList.get(i).getName());
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }

                                try {
                                    if(LectureNoteList.size()==0||LectureNoteList==null){
                                        Toast.makeText(getContext(), "No Lecture Notes", Toast.LENGTH_LONG).show();
                                    }else{
                                        LLL.setVisibility(View.VISIBLE);
                                        for(String var:LectureNoteList){
                                            DocumentReference documentReference = fstore.collection("StudyMaterialFiles").document(var);
                                            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                                    ArrayList<StudyMaterial> SMList = new ArrayList<>();
                                                    StudyMaterial SM = new StudyMaterial(value.getString("CreatedDate"),value.getString("Name"),value.getString("doc"));
                                                    SMList.add(SM);
                                                    final StudyMaterialListAdapter adapter = new StudyMaterialListAdapter(getActivity(), R.layout.adapter_study_material_list, SMList);
                                                    LLV.setAdapter(adapter);
                                                    getDownload(SMList.get(i).getDoc(),SMList.get(i).getName());
                                                }
                                            });
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    if(TutorialNoteList.size()==0||TutorialNoteList==null){
                                        Toast.makeText(getContext(), "No Tutorial Notes", Toast.LENGTH_LONG).show();
                                    }else{
                                        TLL.setVisibility(View.VISIBLE);
                                        for(String var:TutorialNoteList){
                                            DocumentReference documentReference = fstore.collection("StudyMaterialFiles").document(var);
                                            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                                    ArrayList<StudyMaterial> SMList = new ArrayList<>();
                                                    StudyMaterial SM = new StudyMaterial(value.getString("CreatedDate"),value.getString("Name"),value.getString("doc"));
                                                    SMList.add(SM);
                                                    final StudyMaterialListAdapter adapter = new StudyMaterialListAdapter(getActivity(), R.layout.adapter_study_material_list, SMList);
                                                    TLV.setAdapter(adapter);
                                                    getDownload(SMList.get(i).getDoc(),SMList.get(i).getName());
                                                }
                                            });
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }
        });
        return view;
    }

    private void getDownload(String doc,String fName) {

        String dasdasd = doc;
        StorageReference ref = storageRef.child("Files/"+doc);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                downloadFiles(getContext(),fName,"",DIRECTORY_DOWNLOADS,url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "FAIL DOWNLOAD", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void downloadFiles(Context context,String fileName,String fileExtension,String destinationDirectory, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,destinationDirectory,fileName+fileExtension);

        downloadManager.enqueue(request);
    }

    public void getRegisteredSubject() {
        subjectArrayList.clear();
        fstore.collection("Users").document(UID).collection("Subject").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    DocumentReference documentReference = fstore.collection("Subjects").document(snapshot.getId());
                    documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            Subject subject = new Subject(value.getString("subjectCode"), value.getString("subjectName"), value.getString("subjectCreditHours"));
                            subjectArrayList.add(subject);
                            final RegisteredSubjectAdapter adapter = new RegisteredSubjectAdapter(getActivity(), R.layout.adapter_registered_subject_list, subjectArrayList);
                            registeredListView.setAdapter(adapter);
                        }
                    });
                }
            }
        });
    }

    public void getSubjects() {
        fstore.collection("Subjects").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                subjectArrayList.clear();
                final SubjectAdapter adapter = new SubjectAdapter(getActivity(), R.layout.adapter_register_subject_list, subjectArrayList);
                adapter.notifyDataSetChanged();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    Subject subject = new Subject(snapshot.getString("subjectCode"), snapshot.getString("subjectName"), snapshot.getString("subjectCreditHours"));
                    subjectArrayList.add(subject);
                }
                subjectList.setAdapter(adapter);
            }
        });
    }

    public void getSubjectDetails(String id) {
        fstore.collection("Subjects").document(id).collection("Lecture").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                LectureList.clear();
                final SubjectDetailLectureAdapter adapter = new SubjectDetailLectureAdapter(getActivity(), R.layout.adapter_register_subject_lecture_list, LectureList);
                adapter.notifyDataSetChanged();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    SubjectLectureClass lecture = new SubjectLectureClass(snapshot.getString("Lecture"), snapshot.getString("class"), snapshot.getString("time"), snapshot.getString("day"));
                    LectureList.add(lecture);
                }
                subjectLectureList.setAdapter(adapter);
            }
        });

        fstore.collection("Subjects").document(id).collection("Tutorial").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                TutorialList.clear();
                final SubjectDetailTutorialAdapter adapter = new SubjectDetailTutorialAdapter(getActivity(), R.layout.adapter_register_subject_tutorial_list, TutorialList);
                adapter.notifyDataSetChanged();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    SubjectTutorialClass tutorial = new SubjectTutorialClass(snapshot.getString("Tutorial"), snapshot.getString("class"), snapshot.getString("time"), snapshot.getString("day"));
                    TutorialList.add(tutorial);
                }
                subjectTutorialList.setAdapter(adapter);
            }
        });
    }
}