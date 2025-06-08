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
        this.currentUserId = currentUserId;
        this.messages = new ArrayList<>();
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public void addMessage(Message message) {
        this.messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    public void addMessages(List<Message> newMessages) {
        int startPosition = this.messages.size();
        this.messages.addAll(newMessages);
        notifyItemRangeInserted(startPosition, newMessages.size());
    }

    @Override
    public int getViewType(int position) {
        Message message = messages.get(position);
        if (message.getSenderId().equals(currentUserId)) {
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

        private TextView tvMessage;
        private TextView tvTime;
        private ImageView ivMessageStatus;

        public SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTime = itemView.findViewById(R.id.tv_time);
            ivMessageStatus = itemView.findViewById(R.id.iv_message_status);
        }

        public void bind(Message message) {
            tvMessage.setText(message.getContent());
            tvTime.setText(DateUtils.formatMessageTime(message.getCreatedAt()));

            // Set message status icon
            switch (message.getStatus()) {
                case "SENT":
                    ivMessageStatus.setImageResource(R.drawable.ic_message_sent);
                    break;
                case "DELIVERED":
                    ivMessageStatus.setImageResource(R.drawable.ic_message_delivered);
                    break;
                case "READ":
                    ivMessageStatus.setImageResource(R.drawable.ic_message_read);
                    break;
                default:
                    ivMessageStatus.setImageResource(R.drawable.ic_message_pending);
                    break;
            }
        }
    }

    // Received message ViewHolder
    class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

        private TextView tvMessage;
        private TextView tvTime;
        private TextView tvSenderName;
        private ImageView ivSenderAvatar;

        public ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvSenderName = itemView.findViewById(R.id.tv_sender_name);
            ivSenderAvatar = itemView.findViewById(R.id.iv_sender_avatar);
        }

        public void bind(Message message) {
            tvMessage.setText(message.getContent());
            tvTime.setText(DateUtils.formatMessageTime(message.getCreatedAt()));
            tvSenderName.setText(message.getSenderName());

            // Load sender avatar
            if (message.getSenderAvatar() != null && !message.getSenderAvatar().isEmpty()) {
                Glide.with(context)
                        .load(message.getSenderAvatar())
                        .placeholder(R.drawable.default_avatar)
                        .error(R.drawable.default_avatar)
                        .circleCrop()
                        .into(ivSenderAvatar);
            } else {
                ivSenderAvatar.setImageResource(R.drawable.default_avatar);
            }
        }
    }
}