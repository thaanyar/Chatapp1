package sg.edu.np.mad.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import sg.edu.np.mad.chatapp.messages.MessagesAdapter;
import sg.edu.np.mad.chatapp.messages.MessagesList;

public class MainActivity extends AppCompatActivity {

    private final List<MessagesList> messagesLists = new ArrayList<>();
    private String phoneno;
    private String email;
    private String name;

    private int unseenMessages = 0;
    private RecyclerView messagesRecyclerView;
    private String lastMessage = "";
    private MessagesAdapter messagesAdapter;

    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CircleImageView userProfilePic = findViewById(R.id.userProfilePic);

        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);

        // get intent data from registerpage.class activity
        phoneno = getIntent().getStringExtra("phone no.");
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

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                final String profilepictureUrl = snapshot.child("users").child(phoneno).child("profile_pic").getValue(String.class);

                if (!profilepictureUrl.isEmpty()) {
                    // set profile pic to circle image view
                    Picasso.get().load(profilepictureUrl).into(userProfilePic);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { progressDialog.dismiss();}
        });

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                messagesLists.clear();
                unseenMessages = 0;
                lastMessage = "";

                for (DataSnapshot dataSnapshot : snapshot.child("users").getChildren()) {

                    final String getMobile = dataSnapshot.getKey();

                    if (!getMobile.equals(phoneno)) {
                        final String getName = dataSnapshot.child("name").getValue(String.class);
                        final String getProfilePic = dataSnapshot.child("profile_pic").getValue(String.class);




                        databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                int getChatCounts = (int)snapshot.getChildrenCount();

                                if (getChatCounts > 0) {
                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {

                                        final String getKey = dataSnapshot1.getKey();
                                        final String getUserOne = dataSnapshot1.child("user_1").getValue(String.class);
                                        final String getUserTwo = dataSnapshot1.child("user_2").getValue(String.class);

                                        if((getUserOne.equals(getMobile) && getUserTwo.equals(phoneno)) || (getUserOne.equals(phoneno) && getUserTwo.equals(getMobile))) {

                                            for(DataSnapshot chatDataSnapshot : dataSnapshot1.child("messages").getChildren()) {

                                                final long getMessageKey = Long.parseLong(chatDataSnapshot.getKey());
                                                final long getLastSeenMessage = Long.parseLong(MemoryData.getLastMsgTS(MainActivity.this, getKey));

                                                lastMessage = chatDataSnapshot.child("msg").getValue(String.class);
                                                if(getMessageKey > getLastSeenMessage) {
                                                    unseenMessages++;
                                                }

                                            }
                                        }
                                    }
                                }

                                MessagesList messagesList = new MessagesList(getName, getMobile, lastMessage, getProfilePic, unseenMessages);
                                messagesLists.add(messagesList);
                                messagesAdapter.updateData(messagesLists);
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