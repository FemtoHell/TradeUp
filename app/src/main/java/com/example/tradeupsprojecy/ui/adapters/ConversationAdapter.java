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
import com.example.tradeupsprojecy.data.models.Conversation;
import com.example.tradeupsprojecy.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {

    private Context context;
    private List<Conversation> conversations;
    private OnConversationClickListener listener;

    public interface OnConversationClickListener {
        void onConversationClick(Conversation conversation);
        void onConversationLongClick(Conversation conversation);
    }

    public ConversationAdapter(Context context) {
        this.context = context;
        this.conversations = new ArrayList<>();
    }

    public void setOnConversationClickListener(OnConversationClickListener listener) {
        this.listener = listener;
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations != null ? conversations : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addConversation(Conversation conversation) {
        if (conversation != null) {
            this.conversations.add(0, conversation);
            notifyItemInserted(0);
        }
    }

    public void updateConversation(Conversation updatedConversation) {
        if (updatedConversation == null || updatedConversation.getId() == null) return;

        for (int i = 0; i < conversations.size(); i++) {
            Conversation conv = conversations.get(i);
            if (conv != null && conv.getId() != null &&
                    conv.getId().equals(updatedConversation.getId())) {
                conversations.set(i, updatedConversation);
                notifyItemChanged(i);
                break;
            }
        }
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
        holder.bind(conversation);
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    class ConversationViewHolder extends RecyclerView.ViewHolder {

        // FIX: Match với IDs trong item_conversation.xml
        private ImageView avatarImageView;
        private TextView nameTextView;
        private TextView lastMessageTextView;
        private TextView timeTextView;
        private TextView unreadCountTextView;
        private TextView itemTitleTextView;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);

            // FIX: Sử dụng đúng ID từ layout
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            lastMessageTextView = itemView.findViewById(R.id.lastMessageTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            unreadCountTextView = itemView.findViewById(R.id.unreadCountTextView);
            itemTitleTextView = itemView.findViewById(R.id.itemTitleTextView);

            // Click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && position < conversations.size()) {
                        listener.onConversationClick(conversations.get(position));
                    }
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && position < conversations.size()) {
                        listener.onConversationLongClick(conversations.get(position));
                        return true;
                    }
                }
                return false;
            });
        }

        public void bind(Conversation conversation) {
            if (conversation == null) return;

            // Set user info
            if (nameTextView != null) {
                nameTextView.setText(conversation.getOtherUserName() != null ?
                        conversation.getOtherUserName() : "Unknown User");
            }

            // Load user avatar
            if (avatarImageView != null) {
                if (conversation.getOtherUserAvatar() != null &&
                        !conversation.getOtherUserAvatar().isEmpty()) {
                    try {
                        Glide.with(context)
                                .load(conversation.getOtherUserAvatar())
                                .placeholder(android.R.drawable.ic_menu_report_image)
                                .error(android.R.drawable.ic_menu_report_image)
                                .circleCrop()
                                .into(avatarImageView);
                    } catch (Exception e) {
                        avatarImageView.setImageResource(android.R.drawable.ic_menu_report_image);
                    }
                } else {
                    avatarImageView.setImageResource(android.R.drawable.ic_menu_report_image);
                }
            }

            // Set last message
            if (lastMessageTextView != null) {
                if (conversation.getLastMessage() != null &&
                        !conversation.getLastMessage().isEmpty()) {
                    lastMessageTextView.setText(conversation.getLastMessage());
                } else {
                    lastMessageTextView.setText("Chưa có tin nhắn");
                }
            }

            // Set time
            if (timeTextView != null) {
                if (conversation.getLastMessageTime() != null) {
                    timeTextView.setText(DateUtils.formatConversationTime(conversation.getLastMessageTime()));
                } else {
                    timeTextView.setText("2m ago");
                }
            }

            // Set unread count
            if (unreadCountTextView != null) {
                Integer unreadCount = conversation.getUnreadCount();
                if (unreadCount != null && unreadCount > 0) {
                    unreadCountTextView.setVisibility(View.VISIBLE);
                    unreadCountTextView.setText(String.valueOf(unreadCount));
                } else {
                    unreadCountTextView.setVisibility(View.GONE);
                }
            }

            // Set listing title
            if (itemTitleTextView != null) {
                if (conversation.getListingTitle() != null &&
                        !conversation.getListingTitle().isEmpty()) {
                    itemTitleTextView.setText(conversation.getListingTitle());
                    itemTitleTextView.setVisibility(View.VISIBLE);
                } else {
                    itemTitleTextView.setText("iPhone 12 Pro Max"); // Default for demo
                    itemTitleTextView.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}