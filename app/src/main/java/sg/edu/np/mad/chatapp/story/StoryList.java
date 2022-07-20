package sg.edu.np.mad.chatapp.story;

public class StoryList {

    private String username, prof_url, story_url;


    public StoryList(String username, String prof_url, String story_url) {
        this.username = username;
        this.prof_url = prof_url;
        this.story_url = story_url;
    }

    public String getUsername() {
        return username;
    }

    public String getProf_url() {
        return prof_url;
    }

    public String getStory_url() {
        return story_url;
    }


}
