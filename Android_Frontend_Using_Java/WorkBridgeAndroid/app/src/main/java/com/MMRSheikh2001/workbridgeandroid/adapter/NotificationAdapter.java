package com.MMRSheikh2001.workbridgeandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.MMRSheikh2001.workbridgeandroid.R;
import com.MMRSheikh2001.workbridgeandroid.enums.NotificationType;
import com.MMRSheikh2001.workbridgeandroid.response.NotificationResponseDTO;

import java.util.List;

public class NotificationAdapter
        extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    public interface OnNotificationClickListener {

        void onNotificationClick(NotificationResponseDTO notification);

        void onDelete(NotificationResponseDTO notification);
    }

    private final Context context;
    private final List<NotificationResponseDTO> notificationList;
    private final OnNotificationClickListener listener;

    public NotificationAdapter(
            Context context,
            List<NotificationResponseDTO> notificationList,
            OnNotificationClickListener listener) {

        this.context = context;
        this.notificationList = notificationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(
                        R.layout.item_notification,
                        parent,
                        false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        NotificationResponseDTO notification =
                notificationList.get(position);

        holder.tvTitle.setText(notification.getTitle());

        holder.tvMessage.setText(notification.getMessage());

        holder.tvType.setText(
                notification.getType().name());

        holder.tvTime.setText(
                notification.getCreatedAt() == null
                        ? ""
                        : notification.getCreatedAt().toString());

        //==========================================
        // Unread Indicator
        //==========================================

        if (Boolean.TRUE.equals(notification.getIsRead())) {

            holder.viewUnread.setVisibility(View.INVISIBLE);

        } else {

            holder.viewUnread.setVisibility(View.VISIBLE);

        }

        //==========================================
        // Icon
        //==========================================

        holder.imgType.setImageResource(
                getNotificationIcon(notification.getType()));

        //==========================================
        // Clicks
        //==========================================

        holder.itemView.setOnClickListener(v ->
                listener.onNotificationClick(notification));

        holder.btnDelete.setOnClickListener(v ->
                listener.onDelete(notification));

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    //==========================================================
    // Notification Icon
    //==========================================================

    private int getNotificationIcon(NotificationType type) {

        if (type == null) {
            return R.drawable.ic_notifications;
        }

        switch (type) {

            case JOB_APPLIED:
            case JOB_SHORTLISTED:
            case JOB_REJECTED:
            case JOB_HIRED:
                return android.R.drawable.ic_menu_agenda;

            case GIG_APPLICATION:
            case GIG_ORDER:
            case GIG_COMPLETED:
                return android.R.drawable.ic_menu_manage;

            case DEPOSIT_SUCCESS:
            case WITHDRAW_APPROVED:
            case WITHDRAW_REJECTED:
                return android.R.drawable.ic_menu_save;

            case SYSTEM:
            case ADMIN_MESSAGE:
            default:
                return R.drawable.ic_notifications;
        }

    }

    //==========================================================
    // ViewHolder
    //==========================================================

    static class ViewHolder
            extends RecyclerView.ViewHolder {

        View viewUnread;

        ImageView imgType;

        TextView tvTitle;
        TextView tvMessage;
        TextView tvType;
        TextView tvTime;

        ImageButton btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            viewUnread = itemView.findViewById(R.id.viewUnread);

            imgType = itemView.findViewById(R.id.imgType);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvType = itemView.findViewById(R.id.tvType);
            tvTime = itemView.findViewById(R.id.tvTime);

            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}