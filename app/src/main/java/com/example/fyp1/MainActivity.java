package com.example.fyp1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.fyp1.Class.User;
import com.example.fyp1.Fragment.CheckAttendanceFragment;
import com.example.fyp1.Fragment.ClassFragment;
import com.example.fyp1.Fragment.FriendFragment;
import com.example.fyp1.Fragment.HomeFragment;
import com.example.fyp1.Fragment.LecturerFragment;
import com.example.fyp1.Fragment.ProfileFragment;
import com.example.fyp1.Fragment.RegisterSubjectFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import io.opencensus.internal.Utils;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String SHARED_PREFS = "UTAR_PREFS";
    private static final String TAG = "MainActivity";
    private DrawerLayout drawerLayout;
    private Toolbar toolbar,conversationBar;
    private NavigationView navigationView,conversationView;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    String fName, fEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fName = fAuth.getCurrentUser().getDisplayName();
        fEmail = fAuth.getCurrentUser().getEmail();

        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        conversationView = findViewById(R.id.conversation_view);

        toolbar = findViewById(R.id.toolbar);
        conversationBar = findViewById(R.id.conversation_Bar);

        View v = navigationView.getHeaderView(0);

        TextView name = v.findViewById(R.id.userName_header);
        TextView studID = v.findViewById(R.id.stuID_header);

        DocumentReference documentReference = fireStore.collection("Users").document(fAuth.getCurrentUser().getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                name.setText(value.getString("name"));
                studID.setText(value.getString("id"));
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        navigationView.bringToFront(); // light up effect
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        findViewById(R.id.conversation_Bar).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.END);
                conversationView.bringToFront(); // light up effect
            }
        });
        conversationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_add_friend:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FriendFragment()).commit();
                        break;
                    case R.id.nav_utar:
                        goToUrl("https://www.utar.edu.my/");
                        break;
                    case R.id.nav_utar_portal:
                        goToUrl("https://portal.utar.edu.my");
                        break;
                    case R.id.nav_utar_wble:
                       goToUrl("https://wble.utar.edu.my/");
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.END);
                return false;
            }
        });
        checkFirstEntry();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }
    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    public void checkFirstEntry() {
        DocumentReference documentReference = fireStore.collection("Users").document(fAuth.getCurrentUser().getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (null == value.getString("email")) {
                    User updateUser = new User(fName, fEmail,"TRUE");
                    documentReference.set(updateUser);
                }
            }
        });
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (null == (value.getString("firstEntry")) || (value.getString("firstEntry").equals("TRUE"))) {
                    toolbar.setVisibility(View.GONE);
                    conversationBar.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                } else if (value.getString("firstEntry").equals("FALSE")) {
                    toolbar.setVisibility(View.VISIBLE);
                    conversationBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DocumentReference documentReference = fireStore.collection("Users").document(fAuth.getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.getString("role")==null){
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                            break;
                        case R.id.nav_profile:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                            break;
                        case R.id.nav_attendance:
                            Toast.makeText(getApplicationContext(),"You dont have this permission to access",Toast.LENGTH_LONG).show();
                            break;
                        case R.id.nav_registerSubject:
                            Toast.makeText(getApplicationContext(),"You dont have this permission to access",Toast.LENGTH_LONG).show();
                            break;
                        case R.id.nav_lecturer:
                            Toast.makeText(getApplicationContext(),"You dont have this permission to access",Toast.LENGTH_LONG).show();
                            break;
                        case R.id.nav_classes:
                            Toast.makeText(getApplicationContext(),"You dont have this permission to access",Toast.LENGTH_LONG).show();
                            break;
                        case R.id.nav_logout:
                            AuthUI.getInstance().signOut(getApplicationContext())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                startLoginActivity();
                                            } else {
                                                Log.e(TAG, "onComplete: ", task.getException());
                                            }
                                        }
                                    });
                            break;
                    }
                }else if(value.getString("role").equals("lecturer")){
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                            break;
                        case R.id.nav_profile:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                            break;
                        case R.id.nav_attendance:
                            Toast.makeText(getApplicationContext(),"You dont have this permission to access",Toast.LENGTH_LONG).show();
                            break;
                        case R.id.nav_registerSubject:
                            Toast.makeText(getApplicationContext(),"You dont have this permission to access",Toast.LENGTH_LONG).show();
                            break;
                        case R.id.nav_lecturer:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LecturerFragment()).commit();
                            break;
                        case R.id.nav_classes:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ClassFragment()).commit();
                            break;
                        case R.id.nav_logout:
                            AuthUI.getInstance().signOut(getApplicationContext())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                startLoginActivity();
                                            } else {
                                                Log.e(TAG, "onComplete: ", task.getException());
                                            }
                                        }
                                    });
                            break;
                    }
                }else if(value.getString("role").equals("student")){
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                            break;
                        case R.id.nav_profile:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                            break;
                        case R.id.nav_attendance:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CheckAttendanceFragment()).commit();
                            break;
                        case R.id.nav_registerSubject:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RegisterSubjectFragment()).commit();
                            break;
                        case R.id.nav_lecturer:
                            Toast.makeText(getApplicationContext(),"You dont have this permission to access",Toast.LENGTH_LONG).show();
                            break;
                        case R.id.nav_classes:
                            Toast.makeText(getApplicationContext(),"You dont have this permission to access",Toast.LENGTH_LONG).show();
                            break;
                        case R.id.nav_logout:
                            AuthUI.getInstance().signOut(getApplicationContext())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                startLoginActivity();
                                            } else {
                                                Log.e(TAG, "onComplete: ", task.getException());
                                            }
                                        }
                                    });
                            break;
                    }
                }
            }
        });


//        switch (item.getItemId()) {
//            case R.id.nav_home:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
//                break;
//            case R.id.nav_profile:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
//                break;
//            case R.id.nav_attendance:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CheckAttendanceFragment()).commit();
//                break;
//            case R.id.nav_registerSubject:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RegisterSubjectFragment()).commit();
//                break;
//            case R.id.nav_lecturer:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LecturerFragment()).commit();
//                break;
//            case R.id.nav_classes:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ClassFragment()).commit();
//                break;
//            case R.id.nav_logout:
//                AuthUI.getInstance().signOut(this)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    startLoginActivity();
//                                } else {
//                                    Log.e(TAG, "onComplete: ", task.getException());
//                                }
//                            }
//                        });
//                break;
//        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}