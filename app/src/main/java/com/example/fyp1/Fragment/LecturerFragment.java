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
import android.text.format.Time;
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

import com.example.fyp1.Adapter.LecturerSubjectAdapter;
import com.example.fyp1.Adapter.LecturerSubjectDetailLectureAdapter;
import com.example.fyp1.Adapter.LecturerSubjectDetailTutorialAdapter;
import com.example.fyp1.Adapter.RegisteredSubjectAdapter;
import com.example.fyp1.Adapter.TimeListAdapter;
import com.example.fyp1.Class.Subject;
import com.example.fyp1.Class.SubjectLectureClass;
import com.example.fyp1.Class.SubjectTutorialClass;
import com.example.fyp1.Class.User;
import com.example.fyp1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
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
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static android.app.Activity.RESULT_OK;

public class LecturerFragment extends Fragment {
    View view;
    ListView lecturerSubjectList, lectureClassList, tutorialClassList, timelist;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    public FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private ArrayList<Subject> newArrayList = new ArrayList<>();
    private ArrayList<String> SubjectLectureClass = new ArrayList<>();
    private ArrayList<String> SubjectTutorialClass = new ArrayList<>();
    private ArrayList<String> OriAttendanceList = new ArrayList<>();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    String currentTime, currentDate;
    ImageView qrImg;
    Uri pdfUri; //url from local storage
    AlertDialog LecturerSubjectDetailDialog;
    User currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lecturer, container, false);
        lecturerSubjectList = (ListView) view.findViewById(R.id.lecturer_subject_list);
        DocumentReference documentReference = fStore.collection("Users").document(fAuth.getCurrentUser().getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                currentUser = new User(value.getString("name"), value.getString("id"), value.getString("contact"), value.getString("faculty"), value.getString("email"), value.getString("firstEntry"), value.getString("role"), value.getString("doc"));
            }
        });
        getRegisteredSubject();
        lecturerSubjectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder LecturerSubjectDetailBuilder = new AlertDialog.Builder(getActivity());
                View LecturerSubjectDetailView = getLayoutInflater().inflate(R.layout.dialog_lecturer_subject_detail, null);

                TextView name = LecturerSubjectDetailView.findViewById(R.id.lecturer_subject_detail_name);
                TextView hours = LecturerSubjectDetailView.findViewById(R.id.lecturer_subject_detail_credit_hours);
                Button annoucement = LecturerSubjectDetailView.findViewById(R.id.create_annoucement);

                name.setText(newArrayList.get(i).getSubjectCode() + " " + newArrayList.get(i).getSubjectName());
                hours.setText(newArrayList.get(i).getSubjectCreditHours());
                getSubjectClass(newArrayList.get(i).getSubjectCode());

                lectureClassList = LecturerSubjectDetailView.findViewById(R.id.lecturer_subject_lecture_class_list);
                tutorialClassList = LecturerSubjectDetailView.findViewById(R.id.lecturer_subject_tutorial_class_list);
                LecturerSubjectDetailBuilder.setView(LecturerSubjectDetailView);
                LecturerSubjectDetailDialog = LecturerSubjectDetailBuilder.create();
                LecturerSubjectDetailDialog.show();

                annoucement.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder createAnnouncementDialog = new AlertDialog.Builder(getActivity());
                        View cADView = getLayoutInflater().inflate(R.layout.dialog_lecturer_subject_create_annoucment, null);
//                        View cADView = getLayoutInflater().inflate(R.layout.dialog_add_friend, null);
                        EditText subjectName = cADView.findViewById(R.id.lecturer_subject_annoucment_subjectName);
                        subjectName.setText(newArrayList.get(i).getSubjectCode() + " " + newArrayList.get(i).getSubjectName());
                        subjectName.setEnabled(false);
                        EditText title = cADView.findViewById(R.id.lecturer_subject_annoucment_title);
                        EditText details = cADView.findViewById(R.id.editTextTextMultiLine);
                        Button submit = cADView.findViewById(R.id.annoucement_submit);
                        Button cancel = cADView.findViewById(R.id.annoucement_cancel);
                        DateFormat df = new SimpleDateFormat("dd:MM:yyyy:HH:mm");
                        String Time = df.format((Calendar.getInstance().getTime()));
                        createAnnouncementDialog.setView(cADView);

                        AlertDialog AnnouncementDialog = createAnnouncementDialog.create();
                        AnnouncementDialog.show();
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AnnouncementDialog.dismiss();
                            }
                        });

                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Map<String, Object> note = new HashMap<>();
                                note.put("Creator", fAuth.getUid());
                                note.put("CreatedTime", Time);
                                note.put("Title", title.getText().toString());
                                note.put("Faculty", "FICT");
                                note.put("Details", details.getText().toString());
                                note.put("SubjectName", subjectName.getText().toString());
                                DocumentReference documentReference1 = fStore.collection("Announcement").document("Subjects").collection(newArrayList.get(i).getSubjectCode()).document(Time);
                                documentReference1.set(note, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getActivity(), "Announcement Created!", Toast.LENGTH_LONG).show();
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                AnnouncementDialog.dismiss();
                                                LecturerSubjectDetailDialog.dismiss();
                                            }
                                        }, 500);
                                    }
                                });
                            }
                        });
                    }
                });
                lectureClassList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int k, long l) {
                        AlertDialog.Builder qBuilder = new AlertDialog.Builder(getActivity());
                        View qView = getLayoutInflater().inflate(R.layout.alert_qr_code, null);
                        qrImg = qView.findViewById(R.id.alert_qrCode);
                        qBuilder.setView(qView);

                        AlertDialog.Builder mBuilderr = new AlertDialog.Builder(getActivity());
                        View mView = getLayoutInflater().inflate(R.layout.dialog_ca_choose_time, null);
                        timelist = mView.findViewById(R.id.limited_time_list);
                        getTimeList(getContext());
                        mBuilderr.setView(mView);

                        AlertDialog dialog = mBuilderr.create();
                        dialog.show();

                        AlertDialog qDialog = qBuilder.create();
                        //transparent alert dialog
                        qDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                        DateFormat df = new SimpleDateFormat("dd:MM:yyyy:HH:mm");
                        DateFormat date = new SimpleDateFormat("dd:MM:yyyy");
                        currentTime = df.format((Calendar.getInstance().getTime()));
                        currentDate = date.format((Calendar.getInstance().getTime()));

                        timelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int j, long l) {
                                String expiredTime = setLimitedTime(j);
                                String x = newArrayList.get(i).getSubjectCode() + "@" + SubjectLectureClass.get(k) + "@" + currentDate;

                                Bitmap bitmap = generateQRCode(x);
                                uploadQRCode(x, bitmap);
                                qrImg.setImageBitmap(generateQRCode(x));
                                dialog.dismiss();
                                qDialog.show();
                                Map<String, Object> createAttendance = new HashMap<>();
                                createAttendance.put("CreatedDateTime", currentTime);
                                createAttendance.put("ExpiredDateTime", expiredTime);
                                createAttendance.put("SubjectCode", newArrayList.get(i).getSubjectCode());
                                DocumentReference documentReference = fStore.collection("QRCode").document(x);
                                documentReference.set(createAttendance, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getActivity(), "Attendance QR Created!", Toast.LENGTH_LONG).show();
                                        transferAttendanceList(newArrayList.get(i).getSubjectCode(), SubjectLectureClass.get(k));
                                    }
                                });
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        qDialog.dismiss();
                                        // Do something after 5s = 5000ms
                                        for (int i = 0; i < OriAttendanceList.size(); i++) {
                                            Map<String, Object> note = new HashMap<>();
                                            note.put("Check", "0");
                                            note.put("DateTime", currentTime);
                                            note.put("CheckInTime", "");
                                            DocumentReference documentReference1 = fStore.collection("Attendance").document(x).collection("StudentList").document(OriAttendanceList.get(i));
                                            documentReference1.set(note, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getActivity(), "Attendance List Created!", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }

                                        Map<String, Object> note = new HashMap<>();
                                        note.put("SubjectCode", newArrayList.get(i).getSubjectCode());
                                        note.put("DateTime", currentTime);
                                        note.put("doc",x);
                                        DocumentReference documentReference1 = fStore.collection("Attendance").document(x);
                                        documentReference1.set(note, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                LecturerSubjectDetailDialog.dismiss();
                                            }
                                        });
                                    }
                                }, 3000);


                            }
                        });

                    }
                });

                tutorialClassList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int k, long l) {
                        AlertDialog.Builder qBuilder = new AlertDialog.Builder(getActivity());
                        View qView = getLayoutInflater().inflate(R.layout.alert_qr_code, null);
                        qrImg = qView.findViewById(R.id.alert_qrCode);
                        qBuilder.setView(qView);

                        AlertDialog.Builder mBuilderr = new AlertDialog.Builder(getActivity());
                        View mView = getLayoutInflater().inflate(R.layout.dialog_ca_choose_time, null);
                        timelist = mView.findViewById(R.id.limited_time_list);
                        getTimeList(getContext());
                        mBuilderr.setView(mView);

                        AlertDialog dialog = mBuilderr.create();
                        dialog.show();

                        AlertDialog qDialog = qBuilder.create();
                        //transparent alert dialog
                        qDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                        DateFormat df = new SimpleDateFormat("dd:MM:yyyy:HH:mm");
                        DateFormat date = new SimpleDateFormat("dd:MM:yyyy");
                        currentTime = df.format((Calendar.getInstance().getTime()));
                        currentDate = date.format((Calendar.getInstance().getTime()));

                        timelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int j, long l) {
                                String expiredTime = setLimitedTime(j);
                                String x = newArrayList.get(i).getSubjectCode() + "@" + SubjectTutorialClass.get(k) + "@" + currentDate;

                                Bitmap bitmap = generateQRCode(x);
                                uploadQRCode(x, bitmap);
                                qrImg.setImageBitmap(generateQRCode(x));
                                dialog.dismiss();
                                qDialog.show();
                                Map<String, Object> createAttendance = new HashMap<>();
                                createAttendance.put("CreatedDateTime", currentTime);
                                createAttendance.put("ExpiredDateTime", expiredTime);
                                createAttendance.put("SubjectCode", newArrayList.get(i).getSubjectCode());
                                DocumentReference documentReference = fStore.collection("QRCode").document(x);
                                documentReference.set(createAttendance, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getActivity(), "Attendance QR Created!", Toast.LENGTH_LONG).show();
                                        transferAttendanceList(newArrayList.get(i).getSubjectCode(), SubjectTutorialClass.get(k));
                                    }
                                });
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        qDialog.dismiss();
                                        // Do something after 5s = 5000ms
                                        for (int i = 0; i < OriAttendanceList.size(); i++) {
                                            Map<String, Object> note = new HashMap<>();
                                            note.put("Check", 0);
                                            note.put("DateTime", currentTime);
                                            note.put("CheckInTime", "");
                                            DocumentReference documentReference1 = fStore.collection("Attendance").document(x).collection("StudentList").document(OriAttendanceList.get(i));
                                            documentReference1.set(note, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getActivity(), "Attendance List Created!", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }

                                        Map<String, Object> note = new HashMap<>();
                                        note.put("SubjectCode", newArrayList.get(i).getSubjectCode());
                                        note.put("DateTime", currentTime);
                                        DocumentReference documentReference1 = fStore.collection("Attendance").document(x);
                                        documentReference1.set(note, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                LecturerSubjectDetailDialog.dismiss();
                                            }
                                        });
                                    }
                                }, 3000);


                            }
                        });
                    }
                });
            }
        });
        lecturerSubjectList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder UMBuilder = new AlertDialog.Builder(getActivity());
                View UMView = getLayoutInflater().inflate(R.layout.dialog_upload_materials, null);
                LinearLayout ll = UMView.findViewById(R.id.selected_text_files_name_ll);
                TextView subjectName = UMView.findViewById(R.id.upload_materials_subject);
                Spinner spinnerWeek = UMView.findViewById(R.id.week_day_spinner);
                Spinner spinnerFiles = UMView.findViewById(R.id.files_spinner);
                EditText filesNames = UMView.findViewById(R.id.files_name);
                Button selectFiles = UMView.findViewById(R.id.select_files_button);
                Button uploadFiles = UMView.findViewById(R.id.upload_files_button);
                UMBuilder.setView(UMView);
                AlertDialog UMDialog = UMBuilder.create();
                UMDialog.show();
                ll.setVisibility(View.GONE);
                ArrayAdapter<CharSequence> adapterWeeks = ArrayAdapter.createFromResource(getActivity(), R.array.weeks, android.R.layout.simple_spinner_item);
                adapterWeeks.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerWeek.setAdapter(adapterWeeks);

                ArrayAdapter<CharSequence> adapterFiles = ArrayAdapter.createFromResource(getActivity(), R.array.files, android.R.layout.simple_spinner_item);
                adapterFiles.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerFiles.setAdapter(adapterFiles);

                subjectName.setText(newArrayList.get(i).getSubjectCode() + " " + newArrayList.get(i).getSubjectName());
                selectFiles.setOnClickListener(e -> {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        selectPdf();
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                    }
                });
                uploadFiles.setOnClickListener(r -> {
                    if (pdfUri == null) {
                        Toast.makeText(getActivity(), "Please select a file from upload", Toast.LENGTH_SHORT).show();
                    } else {
                        upload(pdfUri, filesNames.getText().toString(), spinnerWeek.getSelectedItem().toString(), spinnerFiles.getSelectedItem().toString(),newArrayList.get(i).getSubjectCode());
                    }
                });
                filesNames.getText().toString();
                return false;
            }
        });
        return view;
    }

    private void upload(Uri pdfUri, String fileNames, String week, String directoryFiles,String subjectCode) {
        DateFormat df = new SimpleDateFormat("ddMMYYYYhhmmss");
        String currentTime = df.format(Calendar.getInstance().getTime());
        Map<String, Object> filesList = new HashMap<>();
        filesList.put("CreatedDate", currentTime);
        filesList.put("Name", fileNames);
        filesList.put("doc", "");


        DocumentReference documentReference = fStore.collection("StudyMaterialFiles").document();
        documentReference.set(filesList, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Map<String, Object> filesDoc = new HashMap<>();
                filesDoc.put("doc", documentReference.getId());
                DocumentReference documentReference1 = fStore.collection("StudyMaterialFiles").document(documentReference.getId());
                documentReference1.set(filesDoc, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        StorageReference storageReference = storage.getReference();//returns root path
                        storageReference.child("Files/").child(documentReference.getId()).putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                ArrayList<String> SMDoc = new ArrayList<>();
                                SMDoc.add(documentReference.getId());
                                Map<String, Object> studyMaterials = new HashMap<>();
                                studyMaterials.put(directoryFiles,SMDoc);
                                DocumentReference documentReference = fStore.collection("Subjects").document(subjectCode).collection("StudyMaterial").document(week);
                                documentReference.set(studyMaterials, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getActivity(), "Upload Successful", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Fail Successful", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectPdf();
        } else {
            Toast.makeText(getActivity(), "Please provide permission..", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectPdf() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);//to fetch files
        startActivityForResult(intent, 86);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();
        } else {
            Toast.makeText(getActivity(), "Please select a file from select", Toast.LENGTH_SHORT).show();
        }
    }

    public void getSubjectClass(String subjectCode) {
        SubjectLectureClass.clear();
        SubjectTutorialClass.clear();
        DocumentReference documentReference = fStore.collection("Subjects").document(subjectCode).collection("LecturerList").document(currentUser.getID());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                Toast.makeText(getActivity(),value.getString("Lecture"),Toast.LENGTH_SHORT).show();
                String[] lecture = value.getString("Lecture").split("@");
                for(String var:lecture){
                    SubjectLectureClass.add(var);
                }
                String[] tutorial = value.getString("Tutorial").split("@");
                for(String var:tutorial){
                    SubjectTutorialClass.add(var);
                }


                LecturerSubjectDetailLectureAdapter LAdapter = new LecturerSubjectDetailLectureAdapter(getActivity(), R.layout.adapter_lecturer_subject_lecture_list, SubjectLectureClass);
                LAdapter.notifyDataSetChanged();
//                lecturerSubjectList.setAdapter(null);
                lectureClassList.setAdapter(LAdapter);
                LecturerSubjectDetailTutorialAdapter TAdapter = new LecturerSubjectDetailTutorialAdapter(getActivity(), R.layout.adapter_lecturer_subject_tutorial_list, SubjectTutorialClass);
                TAdapter.notifyDataSetChanged();
//                lecturerSubjectList.setAdapter(null);
                tutorialClassList.setAdapter(TAdapter);
            }
        });
    }

    public void getRegisteredSubject() {
        newArrayList.clear();
        fStore.collection("Users").document(fAuth.getCurrentUser().getUid()).collection("Subject").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    //Toast.makeText(getActivity(),snapshot.getId(),Toast.LENGTH_SHORT).show();
                    DocumentReference documentReference = fStore.collection("Subjects").document(snapshot.getId());
                    documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            Subject subject = new Subject(value.getString("subjectCode"), value.getString("subjectName"), value.getString("subjectCreditHours"));
                            newArrayList.add(subject);

                            final LecturerSubjectAdapter adapter = new LecturerSubjectAdapter(getActivity(), R.layout.adapter_lecturer_subject_list, newArrayList);
                            adapter.notifyDataSetChanged();
                            lecturerSubjectList.setAdapter(null);
                            lecturerSubjectList.setAdapter(adapter);
                        }
                    });
                }
            }
        });
    }

    public void getTimeList(Context context) {
        ArrayList<String> timeList = new ArrayList<>();
        timeList.add("1Minutes");
        timeList.add("3Minutes");
        timeList.add("5Minutes");
        timeList.add("10Minutes");
        final TimeListAdapter adapter = new TimeListAdapter(getActivity(), R.layout.adapter_time_list, timeList);
        adapter.notifyDataSetChanged();
        timelist.setAdapter(adapter);
    }

    public String setLimitedTime(int x) {
        String time;
        String[] TimeNow = currentTime.split(":");
        Integer Minutes = 0;
        Integer Hours = 0;
        if (x == 0) {
            Minutes = Integer.parseInt(TimeNow[4]) + 1;
        } else if (x == 1) {
            Minutes = Integer.parseInt(TimeNow[4]) + 3;
        } else if (x == 2) {
            Minutes = Integer.parseInt(TimeNow[4]) + 5;
        } else if (x == 3) {
            Minutes = Integer.parseInt(TimeNow[4]) + 10;
        }
        if (Minutes >= 60) {
            Hours = Integer.parseInt(TimeNow[3]) + 1;
            Minutes = Minutes - 60;
        }else{
            Hours = Integer.parseInt(TimeNow[3]);
        }
        TimeNow[3] = Hours.toString();
        TimeNow[4] = Minutes.toString();
        if (TimeNow[3].length() == 1) {
            TimeNow[3] = "0" + TimeNow[3];
        }
        if (TimeNow[4].length() == 1) {
            TimeNow[4] = "0" + TimeNow[4];
        }
        time = TimeNow[0] + TimeNow[1] + TimeNow[2] + TimeNow[3] + TimeNow[4];
        return time;
    }

    public Bitmap generateQRCode(String x) {
        QRGEncoder qrgEncoder = new QRGEncoder(x, QRGContents.Type.TEXT, 500);
        Bitmap qrBits = qrgEncoder.getBitmap();
        return qrBits;
    }

    public void uploadQRCode(String collection, Bitmap bitmap) {
        StorageReference storageRef = storage.getReferenceFromUrl("gs://fyp1-2150f.appspot.com");
        StorageReference qrRef = storageRef.child("/QRCode/" + collection + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = qrRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(taskSnapshot -> {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

        });
    }

    public void transferAttendanceList(String subjectCode, String LectureClass) {
        fStore.collection("Subjects").document(subjectCode).collection("StudentList").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    OriAttendanceList.add(snapshot.getId());
                }
            }
        });
    }
}