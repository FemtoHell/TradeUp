package com.example.tradeupsprojecy.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.models.Message;
import com.example.tradeupsprojecy.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private Context context;
    private List<Message> messages;
    private String currentUserId;

    public MessageAdapter(Context context, String currentUserId) {
        this.context = context;
        this.currentUserId = currentUserId != null ? currentUserId : "";
        this.messages = new ArrayList<>();
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages != null ? messages : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addMessage(Message message) {
        if (message != null) {
            this.messages.add(message);
            notifyItemInserted(messages.size() - 1);
        }
    }

    public void addMessages(List<Message> newMessages) {
        if (newMessages != null && !newMessages.isEmpty()) {
            int startPosition = this.messages.size();
            this.messages.addAll(newMessages);
            notifyItemRangeInserted(startPosition, newMessages.size());
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message != null && message.getSenderId() != null &&
                message.getSenderId().equals(currentUserId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
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
        if (message == null) return;

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

    // Sent message ViewHolder
    class SentMessageViewHolder extends RecyclerView.ViewHolder {

        // FIX: Match với IDs trong item_message_sent.xml
        private TextView messageTextView;
        private TextView timeTextView;

        public SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }

        public void bind(Message message) {
            if (message == null) return;

            // Set message content
            if (messageTextView != null) {
                messageTextView.setText(message.getContent() != null ? message.getContent() : "");
            }

            // Set time
            if (timeTextView != null) {
                if (message.getCreatedAt() != null) {
                    timeTextView.setText(DateUtils.formatMessageTime(message.getCreatedAt()));
                } else {
                    timeTextView.setText("");
                }
            }
        }
    }

    // Received message ViewHolder
    class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

        // FIX: Match với IDs trong item_message_received.xml
        private TextView messageTextView;
        private TextView timeTextView;

        public ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }

        public void bind(Message message) {
            if (message == null) return;

            // Set message content
            if (messageTextView != null) {
                messageTextView.setText(message.getContent() != null ? message.getContent() : "");
            }

            // Set time
            if (timeTextView != null) {
                if (message.getCreatedAt() != null) {
                    timeTextView.setText(DateUtils.formatMessageTime(message.getCreatedAt()));
                } else {
                    timeTextView.setText("");
                }
            }
        }
    }
}