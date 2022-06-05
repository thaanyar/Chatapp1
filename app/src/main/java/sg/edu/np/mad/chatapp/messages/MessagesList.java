package sg.edu.np.mad.chatapp.messages;

public class MessagesList {

    private String name,phoneno,lastMessage, profilepicture, chatKey;

    private int unseenMessages;

    public MessagesList(String name,String phoneno, String lastMessage, String profilepicture, int unseenMessages, String chatKey) {
        this.name = name;
        this.phoneno = phoneno;
        this.lastMessage = lastMessage;
        this.profilepicture = profilepicture;
        this.unseenMessages = unseenMessages;
        this.chatKey = chatKey;

    }

    public String getName() {
        return name;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }

    public String getProfilepicture(){
        return profilepicture;
    }

    public String getChatKey() {
        return chatKey;
    }
}
