package sg.edu.np.mad.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import sg.edu.np.mad.chatapp.chat.Chat;
import sg.edu.np.mad.chatapp.messages.MessagesAdapter;
import sg.edu.np.mad.chatapp.messages.MessagesList;

public class MainActivity extends AppCompatActivity {

    private final List<MessagesList> messagesLists = new ArrayList<>();
    private String mobile = "";
    private String email;
    private String name;
    private Boolean granted;

    private int unseenMessages = 0;

    private String chatKey = "";

    private boolean dataSet = false;
    private RecyclerView messagesRecyclerView;
    private FloatingActionButton fab;
    private String lastMessage = "";
    private MessagesAdapter messagesAdapter;
    private FirebaseAuth mAuth;
    private String userType = "";

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CircleImageView userProfilePic = findViewById(R.id.userProfilePic);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Log out?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mAuth.signOut();
                        Intent intent = new Intent(MainActivity.this, StartUp.class);


                        startActivity(intent);
                        // User clicked OK button
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                builder.show();
            }

        });


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mobile = currentUser.getDisplayName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);

        // get intent data from registerpage.class activity
//        mobile = getIntent().getStringExtra("mobile");

        if (mobile.isEmpty()) {
            mobile = getIntent().getStringExtra("mobile");
        }

        email = getIntent().getStringExtra("email");
        name = getIntent().getStringExtra("name");

        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // set adapter to recyclerview
        messagesAdapter = new MessagesAdapter(messagesLists, MainActivity.this);
        messagesRecyclerView.setAdapter(messagesAdapter);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        messagesLists.clear();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                final String profilepictureUrl = snapshot.child("users").child(mobile).child("profile_pic").getValue(String.class);


                if (profilepictureUrl == null || !profilepictureUrl.isEmpty()) {
                    // set profile pic to circle image view
                    Picasso.get().load(profilepictureUrl).into(userProfilePic);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                unseenMessages = 0;
                chatKey = "";
                granted = false;
                lastMessage = "";
                messagesLists.clear();
                final int userCount = (int) snapshot.child("users").getChildrenCount();


                for (DataSnapshot dataSnapshot : snapshot.child("users").getChildren()) {
                    final String getMobile = dataSnapshot.getKey();
//                    final int userCount = (int) dataSnapshot.ge();

                    dataSet = false;
//
                    if (!getMobile.equals(mobile)) {
                        final String getName = dataSnapshot.child("name").getValue(String.class);
                        final String getProfilePic = dataSnapshot.child("profile_pic").getValue(String.class);
                        final String getBio = dataSnapshot.child("bio").getValue(String.class);
                        databaseReference.child("chat").addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                lastMessage = "";
                                unseenMessages = 0;
                                userType = "";
                                int getChatCounts = (int) snapshot.getChildrenCount();

                                if (getChatCounts > 0) {

                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {

                                        final String pToUser = dataSnapshot1.child("permission").child("toUser").getValue(String.class);
                                        final String pFromUser = dataSnapshot1.child("permission").child("fromUser").getValue(String.class);
                                        final Boolean pGranted = dataSnapshot1.child("permission").child("granted").getValue(Boolean.class);

                                        // chat get key msg e.g. "646434681458797"
                                        final String getKey = dataSnapshot1.getKey();

                                        chatKey = getKey;

                                        if (dataSnapshot1.getKey().equals(chatKey) && dataSnapshot1.child("permission").hasChild("toUser")) {

                                            if (chatKey.contains(mobile) && chatKey.contains(getMobile) && pGranted.equals(false) && pToUser.equals(mobile)) {
                                                lastMessage = getName + " wants to chat with you!";
                                                unseenMessages = 1;
                                                userType = "recipient";


                                                // For sender: check if recipient accept

                                            }
                                            // For sender: check if recipient accept
                                            if (chatKey.contains(mobile) && chatKey.contains(getMobile) && pGranted.equals(false) && pFromUser.equals(mobile)) {
                                                userType = "sender";
                                                lastMessage = "Chat request sent!";

                                            }
                                        }

                                    }

                                }

                                dataSet = true;
                                MessagesList messagesList = new MessagesList(getName, getMobile, lastMessage, getProfilePic, unseenMessages,
                                        getMobile, granted, userType, getBio);

                                if (messagesLists.size() + 1 < userCount) {

                                    messagesLists.add(messagesList);
                                    messagesAdapter.updateData(messagesLists);
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}

//                        databaseReference.child("chat").addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                                int getChatCounts = (int)snapshot.getChildrenCount();
//
//                                if (getChatCounts > 0) {
//                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
//
//                                        final String getKey = dataSnapshot1.getKey();
//                                        chatKey = getKey;
//
//                                        if (dataSnapshot.hasChild("user_1") && dataSnapshot1.hasChild("user_2") && dataSnapshot1.hasChild("messages")) {
//                                            final String getUserOne = dataSnapshot1.child("user_1").getValue(String.class);
//                                            final String getUserTwo = dataSnapshot1.child("user_2").getValue(String.class);
//
//                                            if((getUserOne.equals(getMobile) && getUserTwo.equals(mobile)) || (getUserOne.equals(mobile) && getUserTwo.equals(getMobile))) {
//
//                                                for(DataSnapshot chatDataSnapshot : dataSnapshot1.child("messages").getChildren()) {
//
//                                                    final long getMessageKey = Long.parseLong(chatDataSnapshot.getKey());
//                                                    final long getLastSeenMessage = Long.parseLong(MemoryData.getLastMsgTS(MainActivity.this, getKey));
//
//                                                    lastMessage = chatDataSnapshot.child("msg").getValue(String.class);
//                                                    if(getMessageKey > getLastSeenMessage) {
//                                                        unseenMessages++;
//                                                    }
//
//                                                }
//                                            }
//                                        }
//
//                                    }
//                                }
//
//                                if (!dataSet) {
//                                    dataSet = true;
//                                    MessagesList messagesList = new MessagesList(getName, getMobile, lastMessage, getProfilePic, unseenMessages, chatKey);
//                                    messagesLists.add(messagesList);
//                                    messagesAdapter.updateData(messagesLists);
//                                }
//
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });