// app/src/main/java/com/example/tradeupsprojecy/ui/adapters/MessageAdapter.java
package com.example.tradeupsprojecy.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.entities.Message;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_MESSAGE_SENT = 1;
    private static final int TYPE_MESSAGE_RECEIVED = 2;

    private final Context context;
    private List<Message> messages;
    private final String currentUserId;

    public MessageAdapter(Context context, List<Message> messages, String currentUserId) {
        this.context = context;
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    public void updateMessages(List<Message> newMessages) {
        this.messages = newMessages;
        notifyDataSetChanged();
    }

    public void addMessage(Message message) {
        this.messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getSenderId().equals(currentUserId)) {
            return TYPE_MESSAGE_SENT;
        } else {
            return TYPE_MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_MESSAGE_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        if (holder instanceof SentMessageViewHolder) {
            ((SentMessageViewHolder) holder).bind(message);
        } else if (holder instanceof ReceivedMessageViewHolder) {
            ((ReceivedMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private String formatTime(Message message) {
        if (message.getCreatedAt() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return sdf.format(message.getCreatedAt());
        }
        return "";
    }

    class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        TextView timeTextView;

        public SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }

        public void bind(Message message) {
            messageTextView.setText(message.getContent());
            timeTextView.setText(formatTime(message));
        }
    }

    class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        TextView timeTextView;

        public ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }

        public void bind(Message message) {
            messageTextView.setText(message.getContent());
            timeTextView.setText(formatTime(message));
        }
    }
}