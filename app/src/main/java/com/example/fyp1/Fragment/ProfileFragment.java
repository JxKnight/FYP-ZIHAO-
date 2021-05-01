package com.example.fyp1.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fyp1.R;
import com.example.fyp1.Class.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    View view;
    String UID;
    FloatingActionButton edit, cancel;
    EditText name, id, email, contact, faculty;
    Button submit;
    Boolean updateUser = false;
    String txt = "Update Fail";
    private FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        edit = view.findViewById(R.id.fragment_profile_edit_btn);
        cancel = view.findViewById(R.id.fragment_profile_cancel_btn);
        name = view.findViewById(R.id.profile_name_editText);
        id = view.findViewById(R.id.profile_id_editText);
        email = view.findViewById(R.id.profile_email_editText);
        contact = view.findViewById(R.id.profile_contact_editText);
        faculty = view.findViewById(R.id.profile_faculty_editText);
        submit = view.findViewById(R.id.profile_submit);
        fAuth = FirebaseAuth.getInstance();
        name.setEnabled(false);
        id.setEnabled(false);
        contact.setEnabled(false);
        faculty.setEnabled(false);
        email.setEnabled(false);
        submit.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        edit.setVisibility(View.VISIBLE);
        getUserData();
        DocumentReference documentReference = fireStore.collection("Users").document(fAuth.getCurrentUser().getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.getString("firstEntry").equals("TRUE")){
                    edit.setVisibility(View.GONE);
                    id.setEnabled(true);
                    contact.setEnabled(true);
                    faculty.setEnabled(true);;
                    submit.setVisibility(View.VISIBLE);
                }
            }
        });
        edit.setOnClickListener(q -> {
            cancel.setVisibility(View.VISIBLE);
            edit.setVisibility(View.GONE);
            name.setEnabled(true);
            id.setEnabled(true);
            contact.setEnabled(true);
            faculty.setEnabled(true);
            submit.setVisibility(View.VISIBLE);
        });
        cancel.setOnClickListener(w -> {
            cancel.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
            name.setEnabled(false);
            id.setEnabled(false);
            contact.setEnabled(false);
            faculty.setEnabled(false);
            submit.setVisibility(View.GONE);
        });
        submit.setOnClickListener(e -> {
            Map<String, Object> note = new HashMap<>();
            note.put("name", name.getText().toString());
            note.put("id", id.getText().toString());
            note.put("contact", contact.getText().toString());
            note.put("faculty", faculty.getText().toString());
            note.put("firstEntry", "FALSE");
            note.put("doc",fAuth.getCurrentUser().getUid());
            documentReference.set(note, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getActivity(), "Update Success", Toast.LENGTH_LONG).show();
                }
            });
            cancel.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
            name.setEnabled(false);
            id.setEnabled(false);
            contact.setEnabled(false);
            faculty.setEnabled(false);
            //User user = new User(name.getText().toString(),id.getText().toString(),contact.getText().toString(),faculty.getText().toString(),email.getText().toString());
            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name.getText().toString())
                    .build();
            getUserData();
            submit.setVisibility(View.GONE);
        });
        return view;
    }

    public void getUserData() {
        DocumentReference documentReference = fireStore.collection("Users").document(fAuth.getCurrentUser().getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                name.setText(fAuth.getCurrentUser().getDisplayName());
                email.setText(fAuth.getCurrentUser().getEmail());
                id.setText(value.getString("id"));
                contact.setText(value.getString("contact"));
                faculty.setText(value.getString("faculty"));
            }
        });
    }
}