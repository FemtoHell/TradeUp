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
        this.conversations = conversations;
        notifyDataSetChanged();
    }

    public void addConversation(Conversation conversation) {
        this.conversations.add(0, conversation);
        notifyItemInserted(0);
    }

    public void updateConversation(Conversation updatedConversation) {
        for (int i = 0; i < conversations.size(); i++) {
            if (conversations.get(i).getId().equals(updatedConversation.getId())) {
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

        private ImageView ivUserAvatar;
        private TextView tvUserName;
        private TextView tvLastMessage;
        private TextView tvTime;
        private TextView tvUnreadCount;
        private ImageView ivListingImage;
        private TextView tvListingTitle;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);

            ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvLastMessage = itemView.findViewById(R.id.tv_last_message);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvUnreadCount = itemView.findViewById(R.id.tv_unread_count);
            ivListingImage = itemView.findViewById(R.id.iv_listing_image);
            tvListingTitle = itemView.findViewById(R.id.tv_listing_title);

            // Click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onConversationClick(conversations.get(position));
                    }
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onConversationLongClick(conversations.get(position));
                        return true;
                    }
                }
                return false;
            });
        }

        public void bind(Conversation conversation) {
            // Set user info
            tvUserName.setText(conversation.getOtherUserName());

            // Load user avatar
            if (conversation.getOtherUserAvatar() != null && !conversation.getOtherUserAvatar().isEmpty()) {
                Glide.with(context)
                        .load(conversation.getOtherUserAvatar())
                        .placeholder(R.drawable.default_avatar)
                        .error(R.drawable.default_avatar)
                        .circleCrop()
                        .into(ivUserAvatar);
            } else {
                ivUserAvatar.setImageResource(R.drawable.default_avatar);
            }

            // Set last message
            if (conversation.getLastMessage() != null && !conversation.getLastMessage().isEmpty()) {
                tvLastMessage.setText(conversation.getLastMessage());
            } else {
                tvLastMessage.setText("Chưa có tin nhắn");
            }

            // Set time
            if (conversation.getLastMessageTime() != null) {
                tvTime.setText(DateUtils.formatConversationTime(conversation.getLastMessageTime()));
            } else {
                tvTime.setText("");
            }

            // Set unread count
            if (conversation.getUnreadCount() > 0) {
                tvUnreadCount.setVisibility(View.VISIBLE);
                tvUnreadCount.setText(String.valueOf(conversation.getUnreadCount()));
            } else {
                tvUnreadCount.setVisibility(View.GONE);
            }

            // Set listing info
            if (conversation.getListingTitle() != null) {
                tvListingTitle.setText(conversation.getListingTitle());
                tvListingTitle.setVisibility(View.VISIBLE);
            } else {
                tvListingTitle.setVisibility(View.GONE);
            }

            // Load listing image
            if (conversation.getListingImage() != null && !conversation.getListingImage().isEmpty()) {
                ivListingImage.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(conversation.getListingImage())
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                        .centerCrop()
                        .into(ivListingImage);
            } else {
                ivListingImage.setVisibility(View.GONE);
            }
        }
    }
}