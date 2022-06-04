package sg.edu.np.mad.chatapp.messages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.core.Context;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import sg.edu.np.mad.chatapp.R;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {

    private final List<MessagesList> messagesLists;
    private final Context context;

    public MessagesAdapter(List<MessagesList> messagesLists, Context context) {
        this.messagesLists = messagesLists;
        this.context = context;
    }

    @NonNull
    @Override
    public MessagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return messagesLists.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView profilepicture ;
        private TextView name ;
        private TextView lastMessage;
        private TextView unseenMessages ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.profilepicture = profilepicture;
            this.name = name;
            this.lastMessage = lastMessage;
            this.unseenMessages = unseenMessages;

            profilepicture =  itemView.findViewById(R.id.profilepicture);
            name =  itemView.findViewById(R.id.r_name);
            lastMessage =  itemView.findViewById(R.id.lastMessages);
            unseenMessages =  itemView.findViewById(R.id.unseenMessages);


        }

    }

}

