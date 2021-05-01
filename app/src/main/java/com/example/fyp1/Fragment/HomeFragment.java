package com.example.fyp1.Fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
import com.example.fyp1.Adapter.HomeAnnouncementListAdapter;
import com.example.fyp1.Class.Announcement;
import com.example.fyp1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HomeFragment extends Fragment {
    View view;
    private FirebaseFirestore fstore = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    ListView AList;
    ArrayList<Announcement> Announcement_List = new ArrayList<>();
    String UID;
    TextView textView;
    int check = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        Announcement_List.clear();
        textView = (TextView) view.findViewById(R.id.home_day);
        String day = LocalDate.now().getDayOfWeek().name();
        AList = (ListView) view.findViewById(R.id.home_annoucement_List);
        textView.setText(day);
        SegmentedButtonGroup sbg = view.findViewById(R.id.segmentedBtnGroup);
        sbg.setOnPositionChangedListener(new SegmentedButtonGroup.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(int position) {
                if (position == 0) {
                    AList.setAdapter(null);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getAnnouncementDetails(check = 0);
                        }
                    }, 500);

                } else if (position == 1) {
                    AList.setAdapter(null);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getAnnouncementDetails(check = 1);
                        }
                    }, 500);
                }
            }
        });
        return view;
    }

    public void getAnnouncementDetails(int check) {
        Announcement_List.clear();
        if (check == 1) {
            fstore.collection("Users").document(fAuth.getCurrentUser().getUid()).collection("Subject").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {

                        fstore.collection("Announcement").document("Subjects").collection(snapshot.getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                                for (DocumentSnapshot snapshot1 : queryDocumentSnapshots) {

                                    Announcement announcement = new Announcement(snapshot1.getString("Title"), snapshot1.getString("CreatedTime"), snapshot1.getString("Faculty"), snapshot1.getString("Details"), snapshot1.getString("Creator"), snapshot1.getString("SubjectName"));
                                    Announcement_List.add(announcement);
                                    if (getActivity() != null) {
                                        Collections.sort(Announcement_List, new Comparator<Announcement>() {
                                            public int compare(Announcement o1, Announcement o2) {
                                                return o1.getCreatedTime().compareTo(o2.getCreatedTime());
                                            }
                                        });
                                        HomeAnnouncementListAdapter adapter = new HomeAnnouncementListAdapter(getActivity(), R.layout.adapter_home_annoucment_list, Announcement_List);
                                        adapter.notifyDataSetChanged();
                                        AList.setAdapter(adapter);
                                    }
                                }
                            }
                        });
                    }
                }
            });
        } else if (check == 0) {
            fstore.collection("Announcement").document("School").collection("ALL").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    for (DocumentSnapshot snapshot1 : queryDocumentSnapshots) {
                        Announcement announcement = new Announcement(snapshot1.getString("Title"), snapshot1.getString("Details"));
                        Announcement_List.add(announcement);
                        if (getActivity() != null) {
                            Collections.sort(Announcement_List, new Comparator<Announcement>() {
                                public int compare(Announcement o1, Announcement o2) {
                                    return o1.getCreatedTime().compareTo(o2.getCreatedTime());
                                }
                            });
                            HomeAnnouncementListAdapter adapter = new HomeAnnouncementListAdapter(getActivity(), R.layout.adapter_home_annoucment_list, Announcement_List);
                            adapter.notifyDataSetChanged();
                            AList.setAdapter(adapter);
                        }
                    }
                }
            });

        }
    }
}