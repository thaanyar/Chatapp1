package sg.edu.np.mad.chatapp.story;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import sg.edu.np.mad.chatapp.R;
import sg.edu.np.mad.chatapp.messages.MessagesAdapter;
import sg.edu.np.mad.chatapp.messages.MessagesList;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.MyViewHolder> {

    private List<StoryList> storyLists;
    private final Context context;

    public StoryAdapter(List<StoryList> storyLists, Context context) {

        this.storyLists = storyLists;
        this.context = context;
    }



    @NonNull
    @Override
    public StoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StoryAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.story_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull StoryAdapter.MyViewHolder holder, int position) {

    }


    public void updateData(List<StoryList> storyLists) {
        this.storyLists = storyLists;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return storyLists.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView profilepicture;
        private TextView username;
        private ImageView story_url;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profilepicture = itemView.findViewById(R.id.s_profile_pic);
            username = itemView.findViewById(R.id.s_username);
            story_url = itemView.findViewById(R.id.story_media);
        }

    }


}
