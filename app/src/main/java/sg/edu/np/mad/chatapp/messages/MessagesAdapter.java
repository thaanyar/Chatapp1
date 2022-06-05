package sg.edu.np.mad.chatapp.messages;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import sg.edu.np.mad.chatapp.R;
import sg.edu.np.mad.chatapp.chat.Chat;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {

    private List<MessagesList> messagesLists;
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

        MessagesList list2 = messagesLists.get(position);

        if (!list2.getProfilepicture().isEmpty()) {
            Picasso.get().load(list2.getProfilepicture()).into(holder.profilepicture);
        }

        holder.name.setText(list2.getName());
        holder.lastMessage.setText(list2.getLastMessage());

        if (list2.getUnseenMessages() == 0) {
            holder.unseenMessages.setVisibility(View.GONE);
            holder.lastMessage.setTextColor(Color.parseColor("#959595"));
        } else {
            holder.unseenMessages.setVisibility(View.VISIBLE);
            holder.unseenMessages.setText(list2.getUnseenMessages()+"");
            holder.lastMessage.setTextColor(context.getResources().getColor(R.color.theme_color_80));
        }

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Chat.class);
                intent.putExtra("name", list2.getName());
                intent.putExtra("profile_pic", list2.getProfilepicture());

                context.startActivity(intent);

            }
        });
    }

    public void updateData(List<MessagesList> messagesLists) {
        this.messagesLists = messagesLists;
        notifyDataSetChanged();
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
        private LinearLayout rootLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profilepicture =  itemView.findViewById(R.id.profilepicture);
            name =  itemView.findViewById(R.id.name);
            lastMessage =  itemView.findViewById(R.id.lastMessages);
            unseenMessages =  itemView.findViewById(R.id.unseenMessages);
            rootLayout = itemView.findViewById(R.id.rootLayout);

        }

    }

}

