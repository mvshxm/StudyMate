package univie.hci.studymate;

import static univie.hci.studymate.MainActivity.USER_MATCHING_ALGO_STRING;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity implements ChatListAdapter.OnItemClickListener {

    private RecyclerView chatListRecyclerView;
    private ChatListAdapter chatListAdapter;
    private ImageView settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        chatListRecyclerView = findViewById(R.id.chat_list);
        chatListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FriendList friendList = FriendList.getInstance(this);

        List<Message> messagesWithChatHistory = filterMessagesWithChatHistory(friendList.getFriends());

        chatListAdapter = new ChatListAdapter(messagesWithChatHistory);
        chatListAdapter.setOnItemClickListener(this);
        chatListRecyclerView.setAdapter(chatListAdapter);

        ImageView settingsButton = findViewById(R.id.settingsButton);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatListActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private List<Message> filterMessagesWithChatHistory(List<User> allUsers) {
        List<Message> messagesWithChatHistory = new ArrayList<>();
        ChatList chatList = ChatList.getInstance(this);

        for (User user : allUsers) {
            Message lastMessage = chatList.getLastMessageForUser(user.getId());
            if (lastMessage != null) {
                messagesWithChatHistory.add(lastMessage);
            }
        }

        return messagesWithChatHistory;
    }

    @Override
    public void onItemClick(Message message) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("userId", message.getFrom().getId());
        intent.putExtra("userName", message.getFrom().getName());
        startActivity(intent);
    }
}
