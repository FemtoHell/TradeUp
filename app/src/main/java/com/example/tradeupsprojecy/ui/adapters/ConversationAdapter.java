// app/src/main/java/com/example/tradeupsprojecy/ui/adapters/ConversationAdapter.java
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
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.entities.Conversation;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {

    private final Context context;
    private List<Conversation> conversations;
    private OnConversationClickListener onConversationClickListener;

    public interface OnConversationClickListener {
        void onConversationClick(Conversation conversation);
    }

    public ConversationAdapter(Context context, List<Conversation> conversations) {
        this.context = context;
        this.conversations = conversations;
    }

    public void setOnConversationClickListener(OnConversationClickListener listener) {
        this.onConversationClickListener = listener;
    }

    public void updateConversations(List<Conversation> newConversations) {
        this.conversations = newConversations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_conversation, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        Conversation conversation = conversations.get(position);

        // User avatar
        if (conversation.getOtherUserAvatarUrl() != null && !conversation.getOtherUserAvatarUrl().isEmpty()) {
            Glide.with(context)
                    .load(conversation.getOtherUserAvatarUrl())
                    .transform(new CircleCrop())
                    .placeholder(R.drawable.ic_person_placeholder)
                    .into(holder.avatarImageView);
        } else {
            holder.avatarImageView.setImageResource(R.drawable.ic_person_placeholder);
        }

        // User name
        holder.userNameTextView.setText(conversation.getOtherUserName() != null ?
                conversation.getOtherUserName() : "Unknown User");

        // Item title
        holder.itemTitleTextView.setText(conversation.getItemTitle() != null ?
                conversation.getItemTitle() : "Unknown Item");

        // Last message
        holder.lastMessageTextView.setText(conversation.getLastMessage() != null ?
                conversation.getLastMessage() : "No messages yet");

        // Time
        if (conversation.getLastMessageTime() != null) {
            String timeText = formatTime(conversation.getLastMessageTime());
            holder.timeTextView.setText(timeText);
        } else {
            holder.timeTextView.setText("");
        }

        // Unread indicator
        if (conversation.hasUnreadMessages()) {
            holder.unreadIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.unreadIndicator.setVisibility(View.GONE);
        }

        // Click listener
        holder.itemView.setOnClickListener(v -> {
            if (onConversationClickListener != null) {
                onConversationClickListener.onConversationClick(conversation);
            }
        });
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    private String formatTime(Date date) {
        long now = System.currentTimeMillis();
        long messageTime = date.getTime();
        long diff = now - messageTime;

        if (diff < 60 * 1000) { // Less than 1 minute
            return "now";
        } else if (diff < 60 * 60 * 1000) { // Less than 1 hour
            return (diff / (60 * 1000)) + "m";
        } else if (diff < 24 * 60 * 60 * 1000) { // Less than 1 day
            return (diff / (60 * 60 * 1000)) + "h";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd", Locale.getDefault());
            return sdf.format(date);
        }
    }

    static class ConversationViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImageView;
        TextView userNameTextView;
        TextView itemTitleTextView;
        TextView lastMessageTextView;
        TextView timeTextView;
        View unreadIndicator;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            itemTitleTextView = itemView.findViewById(R.id.itemTitleTextView);
            lastMessageTextView = itemView.findViewById(R.id.lastMessageTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            unreadIndicator = itemView.findViewById(R.id.unreadIndicator);
        }
    }
}