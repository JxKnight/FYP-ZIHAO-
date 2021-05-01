package com.example.fyp1.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.fyp1.Adapter.ChatListAdapter;
import com.example.fyp1.Adapter.FriendListAdapter;
import com.example.fyp1.Adapter.GroupFriendListAdapter;
import com.example.fyp1.Adapter.GroupListAdapter;
import com.example.fyp1.Adapter.LecturerSubjectDetailLectureAdapter;
import com.example.fyp1.Class.Chat;
import com.example.fyp1.Class.ChatDetails;
import com.example.fyp1.Class.Group;
import com.example.fyp1.Class.User;
import com.example.fyp1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class FriendFragment extends Fragment {
    View view;
    private FirebaseFirestore fstore = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FloatingActionButton addFriend, addGroup;
    ListView friendList, groupFriendList, groupsList;
    ArrayList<User> selectedFriendList = new ArrayList<>();
    ArrayList<User> groupList = new ArrayList<>();
    ArrayList<User> friendsList = new ArrayList<>();
    String id = "", seperator = "@";
    ArrayList<String> groups = new ArrayList<>();
    ArrayList<String> list = new ArrayList<>();
    ArrayList<ChatDetails> finalChatList = new ArrayList<>();
    ArrayList<ChatDetails> finalGroupChatList = new ArrayList<>();
    ArrayList<Group> GroupsList = new ArrayList<>();
    User currentUser;
    String groupDoc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friend, container, false);
        addFriend = (FloatingActionButton) view.findViewById(R.id.fragment_friend_add_friend);
        addGroup = (FloatingActionButton) view.findViewById(R.id.fragment_friend_add_group);
        friendList = (ListView) view.findViewById(R.id.fragment_friend_list);
        groupsList = (ListView) view.findViewById(R.id.fragment_group_list);
        groupsList.setVisibility(View.GONE);
        friendList.setVisibility(View.GONE);

        fstore.collection("Users").document(fAuth.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                id = value.getString("id");
                groups = (ArrayList<String>) value.get("Groups");
                if (groups == null || groups.size() == 0) {
                    Toast.makeText(getActivity(), "Not Group Found", Toast.LENGTH_LONG).show();
                } else {
                    groupsList.setVisibility(View.VISIBLE);

                    for (String var : groups) {
                        DocumentReference documentReference = fstore.collection("Groups").document(var);
                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                Group group = new Group(value.getString("ChatUID"), value.getString("CreatedTime"), value.getString("Name"), value.getString("doc"));
                                GroupsList.add(group);
                                final GroupListAdapter adapter = new GroupListAdapter(getActivity(), R.layout.adapter_group_list, GroupsList);
                                adapter.notifyDataSetChanged();
                                groupsList.setAdapter(adapter);
                                getFriendList();
                            }
                        });
                    }
                }
            }
        });

        groupsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder GroupChatBuilder = new AlertDialog.Builder(getActivity());
                View GCBView = getLayoutInflater().inflate(R.layout.dialog_group_chat, null);
                GroupChatBuilder.setView(GCBView);
                AlertDialog GCBDialog = GroupChatBuilder.create();
                GCBDialog.show();
                Button chatSend = (Button) GCBView.findViewById(R.id.group_chat_send_button);
                EditText chatContent = (EditText) GCBView.findViewById(R.id.group_chat_send_messenge);
                TextView GroupName = (TextView) GCBView.findViewById(R.id.group_name);
                ListView groupChatsList = (ListView) GCBView.findViewById(R.id.group_chat_details_list);
                groupDoc = GroupsList.get(i).getChatUID();
                //getGroupChatList
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fstore.collection("Chats").document(groupDoc).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                groupChatsList.setAdapter(null);
                                GroupName.append(GroupsList.get(i).getName());
                                if (value.get("Details") == null) {
                                    Toast.makeText(getActivity(), "Not Chat Found", Toast.LENGTH_LONG).show();
                                } else {
                                    list = (ArrayList<String>) value.get("Details");
                                    for (String a : list) {
                                        String[] test = a.split("@");
                                        if (test[2].equals(currentUser.getID())) {
                                            ChatDetails newChat = new ChatDetails(test[1], test[0], null, test[2]);
                                            finalGroupChatList.add(newChat);
                                        } else {
                                            ChatDetails newChat = new ChatDetails(test[1], test[0], test[2], null);
                                            finalGroupChatList.add(newChat);
                                        }
                                    }
                                    final ChatListAdapter adapter = new ChatListAdapter(getActivity(), R.layout.adapter_chats_list, finalGroupChatList);
                                    adapter.notifyDataSetChanged();
                                    groupChatsList.setAdapter(adapter);
                                }
                            }
                        });
                    }
                }, 500);
                chatSend.setOnClickListener(z -> {
                    DateFormat df = new SimpleDateFormat("ddMMYYYYhhmmss");
                    String currentTime = df.format(Calendar.getInstance().getTime());
                    String details = currentTime + seperator + chatContent.getText().toString() + seperator + id;
                    list.add(details);
                    Map<String, Object> chatlist = new HashMap<>();
                    chatlist.put("Details", list);
                    DocumentReference documentReference = fstore.collection("Chats").document(groupDoc);
                    documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                            DocumentReference documentReference = fstore.collection("Chats").document(groupDoc);
                            documentReference.set(chatlist, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getActivity(), "Send", Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    });
                });
            }
        });

        DocumentReference documentReference = fstore.collection("Users").document(fAuth.getCurrentUser().getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                currentUser = new User(value.getString("name"), value.getString("id"), value.getString("contact"), value.getString("faculty"), value.getString("email"), value.getString("firstEntry"), value.getString("role"), value.getString("doc"));
                groupList.add(currentUser);
            }
        });
        getFriendList();
        addFriend.setOnClickListener(e -> {
            AlertDialog.Builder addFriendBuilder = new AlertDialog.Builder(getActivity());
            View qView = getLayoutInflater().inflate(R.layout.dialog_add_friend, null);
            addFriendBuilder.setView(qView);
            AlertDialog dialog = addFriendBuilder.create();
            LinearLayout LL = (LinearLayout) qView.findViewById(R.id.SearchFriendResult);
            TextView textView = (TextView) qView.findViewById(R.id.SearchFriendResultName);
            EditText searchFriend = (EditText) qView.findViewById(R.id.add_friend_editText);
            Button searchFriendButton = (Button) qView.findViewById(R.id.add_friend_button_search);
            Button addFriendButton = (Button) qView.findViewById(R.id.addFriendButton);
            LL.setVisibility(View.GONE);
            searchFriendButton.setOnClickListener(q -> {
                fstore.collection("Users").whereEqualTo("id", searchFriend.getText().toString()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                        if (error != null || snapshots.size() == 0) {
                            Toast.makeText(getActivity(), "User not found", Toast.LENGTH_SHORT).show();
                        } else {
                            for (DocumentChange userDoc : snapshots.getDocumentChanges()) {
                                User user = userDoc.getDocument().toObject(User.class);
                                if (user.getName() != null) {
                                    LL.setVisibility(View.VISIBLE);
                                    textView.setText(user.getName());
                                }
                            }
                        }
                    }
                });
            });

            addFriendButton.setOnClickListener(w -> {
                DateFormat df = new SimpleDateFormat("dd:MM:yyyy:HH:mm");
                String currentTime = df.format((Calendar.getInstance().getTime()));
                Map<String, Object> note = new HashMap<>();
                note.put("CreatedDate", currentTime);
                DocumentReference documentReference1 = fstore.collection("Users").document(fAuth.getUid()).collection("Friends").document(searchFriend.getText().toString());
                documentReference1.set(note, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        fstore.collection("Users").whereEqualTo("id", searchFriend.getText().toString()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                                for (DocumentChange userDoc : snapshots.getDocumentChanges()) {
                                    User user = userDoc.getDocument().toObject(User.class);
                                    if (user.getDoc() != null) {
                                        DocumentReference documenteReference = fstore.collection("Users").document(user.getDoc()).collection("Friends").document(id);
                                        documenteReference.set(note, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getActivity(), "Added Friend", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                });
            });
            dialog.show();
        });
        addGroup.setOnClickListener(q -> {
            AlertDialog.Builder addFriendBuilder = new AlertDialog.Builder(getActivity());
            View qView = getLayoutInflater().inflate(R.layout.dialog_add_group_chat, null);
            addFriendBuilder.setView(qView);
            AlertDialog dialog = addFriendBuilder.create();
            dialog.show();
            groupFriendList = (ListView) qView.findViewById(R.id.group_friend_list);
            TextView listuser = (TextView) qView.findViewById(R.id.group_friend_selected_list);
            Button undo = (Button) qView.findViewById(R.id.group_friend_selected_cancel);
            Button createGroup = (Button) qView.findViewById(R.id.add_group_button);
            GroupFriendListAdapter adapter = new GroupFriendListAdapter(getActivity(), R.layout.adapter_group_friend_list, friendsList);
            adapter.notifyDataSetChanged();
            groupFriendList.setAdapter(adapter);
            groupFriendList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                    if (selectedFriendList.size() == 0) {
                        selectedFriendList.add(friendsList.get(i));
                        groupList.add(friendsList.get(i));
                    } else if (selectedFriendList.size() > 0) {
                        for (User var : selectedFriendList) {
                            if (var.getID() == friendsList.get(i).getID()) {
                                Toast.makeText(getActivity(), "This friend already in the list ", Toast.LENGTH_LONG).show();
                                break;
                            } else {
                                selectedFriendList.add(friendsList.get(i));
                                groupList.add(friendsList.get(i));
                                break;
                            }
                        }
                    }
                    listuser.setText("");
                    for (User var : selectedFriendList) {
                        listuser.append("[" + var.getName() + "],");
                    }
                    return false;
                }
            });


            undo.setOnClickListener(o -> {
                selectedFriendList.clear();
                listuser.setText("");
            });
            createGroup.setOnClickListener(p -> {
                AlertDialog.Builder confirmGroupBuilder = new AlertDialog.Builder(getActivity());
                View CGBView = getLayoutInflater().inflate(R.layout.dialog_confirm_group, null);
                confirmGroupBuilder.setView(CGBView);
                AlertDialog CGBDialog = confirmGroupBuilder.create();
                CGBDialog.show();
                TextView confirmList = (TextView) CGBView.findViewById(R.id.group_confirm_list);
                for (User var : selectedFriendList) {
                    confirmList.append("[" + var.getName() + "] ");
                }
                Button cancel = (Button) CGBView.findViewById(R.id.cancel_group_button);
                Button confirm = (Button) CGBView.findViewById(R.id.confirm_group_button);
                cancel.setOnClickListener(i -> {
                    CGBDialog.dismiss();
                });
                confirm.setOnClickListener(u -> {
                    createGroupsDocument();
                    getFriendList();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            CGBDialog.dismiss();
                        }
                    }, 5000);
                });
            });
        });
        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder qBuilder = new AlertDialog.Builder(getActivity());
                View qView = getLayoutInflater().inflate(R.layout.dialog_friend_chat, null);
                TextView receiverName = (TextView) qView.findViewById(R.id.chat_with_who);
                ListView chatList = (ListView) qView.findViewById(R.id.chat_with_who_list);
                EditText chatContent = (EditText) qView.findViewById(R.id.friend_chat_send_messenge);
                Button chatSend = (Button) qView.findViewById(R.id.friend_chat_send_button);
                receiverName.append(friendsList.get(i).getName());

                //getChatList
                fstore.collection("Users").document(fAuth.getCurrentUser().getUid()).collection("Friends").document(friendsList.get(i).getID()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                        String x = value.getString("ChatUID");
                        if (x == null) {
                            Toast.makeText(getActivity(), "Let Say Hi To Your Friend" + friendsList.get(i).getName(), Toast.LENGTH_LONG).show();
                        } else {

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    fstore.collection("Chats").document(x).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                            chatList.setAdapter(null);
                                            finalChatList.clear();
                                            if (value.get("Details") == null) {
                                                Toast.makeText(getActivity(), "Not Chat Found", Toast.LENGTH_LONG).show();
                                            } else {
                                                list = (ArrayList<String>) value.get("Details");
                                                for (String a : list) {
                                                    String[] test = a.split("@");
                                                    if (test[2].equals(currentUser.getID())) {
                                                        ChatDetails newChat = new ChatDetails(test[1], test[0], null, test[2]);
                                                        finalChatList.add(newChat);
                                                    } else {
                                                        ChatDetails newChat = new ChatDetails(test[1], test[0], test[2], null);
                                                        finalChatList.add(newChat);
                                                    }
                                                }
                                                final ChatListAdapter adapter = new ChatListAdapter(getActivity(), R.layout.adapter_chats_list, finalChatList);
                                                adapter.notifyDataSetChanged();
                                                chatList.setAdapter(adapter);
                                            }
                                        }
                                    });
                                }
                            }, 1000);
                        }
                    }
                });

                //sendChat
                chatSend.setOnClickListener(p -> {
                    DateFormat df = new SimpleDateFormat("ddMMYYYYhhmmss");
                    String currentTime = df.format(Calendar.getInstance().getTime());
                    String details = currentTime + seperator + chatContent.getText().toString() + seperator + id;
                    list.add(details);
                    Map<String, Object> chatlist = new HashMap<>();
                    chatlist.put("Details", list);
                    DocumentReference documentReference = fstore.collection("Users").document(fAuth.getCurrentUser().getUid()).collection("Friends").document(friendsList.get(i).getID());
                    documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (value.getString("ChatUID") != null) {
                                DocumentReference documentReference = fstore.collection("Chats").document(value.getString("ChatUID"));
                                documentReference.set(chatlist, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getActivity(), "Send", Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                DocumentReference documentReference = fstore.collection("Chats").document();
                                documentReference.set(chatlist, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getActivity(), "Send", Toast.LENGTH_LONG).show();
                                        updateUserFriendChatUID(documentReference.getId(), friendsList.get(i).getID());
                                    }
                                });
                            }
                        }
                    });

                });
                qBuilder.setView(qView);
                AlertDialog dialog = qBuilder.create();
                dialog.show();
            }
        });
        return view;
    }

    private void getFriendList() {
        friendsList = new ArrayList<>();
        friendsList.clear();
        friendList.setAdapter(null);
        fstore.collection("Users").document(fAuth.getUid()).collection("Friends").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    //Toast.makeText(getActivity(), snapshot.getId(), Toast.LENGTH_SHORT).show();
                    fstore.collection("Users").whereEqualTo("id", snapshot.getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {

                            for (DocumentChange userDoc : snapshots.getDocumentChanges()) {
                                User user = userDoc.getDocument().toObject(User.class);
                                if (user.getName() != null) {
                                    User friend = new User(user.getName(), user.getID(), user.getContact(), user.getFaculty(), user.getEmail(), user.getFirstEntry(), user.getRole(), user.getDoc());
                                    friendsList.add(friend);
                                    FriendListAdapter adapter = new FriendListAdapter(getActivity(), R.layout.adapter_friend_list, friendsList);
                                    adapter.notifyDataSetChanged();
                                    friendList.setAdapter(adapter);
                                    friendList.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void updateUserFriendChatUID(String id, String friendUID) {
        Map<String, Object> createAttendance = new HashMap<>();
        createAttendance.put("ChatUID", id);
        DocumentReference documentReference = fstore.collection("Users").document(fAuth.getUid()).collection("Friends").document(friendUID);
        documentReference.set(createAttendance, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "Success Created", Toast.LENGTH_LONG).show();
            }
        });

        fstore.collection("Users").document(fAuth.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                fstore.collection("Users").whereEqualTo("id", friendUID).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                        for (DocumentChange userDoc : snapshots.getDocumentChanges()) {
                            User chitchat = userDoc.getDocument().toObject(User.class);
                            if (chitchat.getDoc() != null) {
                                Map<String, Object> createeAttendance = new HashMap<>();
                                createeAttendance.put("ChatUID", id);
                                DocumentReference documenteReference = fstore.collection("Users").document(chitchat.getDoc()).collection("Friends").document(value.getString("id"));
                                documenteReference.set(createAttendance, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getActivity(), "Success Created", Toast.LENGTH_LONG).show();

                                    }
                                });
                            }
                        }
                    }
                });
            }
        });


    }

    private void createGroupsDocument() {
        ArrayList<String> idList = new ArrayList<>();
        for (User var : groupList) {
            idList.add(var.getID());
        }
        DateFormat df = new SimpleDateFormat("ddMMYYYYhhmmss");
        String currentTime = df.format(Calendar.getInstance().getTime());
        Map<String, Object> createCHATGROUP = new HashMap<>();
        createCHATGROUP.put("CreatedTime", currentTime);
        DocumentReference docRefChatGroup = fstore.collection("Chats").document();
        docRefChatGroup.set(createCHATGROUP, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //getDocumentID
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, Object> createGroups = new HashMap<>();
                        createGroups.put("CreatedTime", currentTime);
                        createGroups.put("Users", idList);
                        createGroups.put("Name", "GROUPNAME");
                        createGroups.put("ChatUID", docRefChatGroup.getId());
                        DocumentReference documentReference = fstore.collection("Groups").document();
                        documentReference.set(createGroups, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //getDocumentID
                                ArrayList<String> groupsList = new ArrayList<>();
                                groupsList.add(documentReference.getId());
                                Map<String, Object> createGroups = new HashMap<>();
                                createGroups.put("Groups", groupsList);
                                for (User var : groupList) {
                                    DocumentReference documentReference = fstore.collection("Users").document(var.getDoc());
                                    documentReference.set(createGroups, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getActivity(), "Success Created", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                            }
                        });
                    }
                }, 1000);


            }
        });


    }
}
