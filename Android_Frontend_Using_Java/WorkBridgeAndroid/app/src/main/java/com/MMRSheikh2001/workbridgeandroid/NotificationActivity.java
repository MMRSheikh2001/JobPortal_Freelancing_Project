package com.MMRSheikh2001.workbridgeandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.MMRSheikh2001.workbridgeandroid.adapter.NotificationAdapter;
import com.MMRSheikh2001.workbridgeandroid.repository.NotificationRepository;
import com.MMRSheikh2001.workbridgeandroid.response.LoginResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.NotificationResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private RecyclerView rvNotifications;
    private View layoutEmpty;
    private MaterialButton btnMarkAllRead;
    private MaterialButton btnClearAll;

    private NotificationAdapter adapter;

    private final List<NotificationResponseDTO> notificationList =
            new ArrayList<>();

    private NotificationRepository repository;
    private SessionManager sessionManager;

    private Long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        init();

        loadNotifications();

        toolbar.setNavigationOnClickListener(v -> finish());

        btnMarkAllRead.setOnClickListener(v -> markAllRead());

        btnClearAll.setOnClickListener(v -> deleteAll());
    }

    private void init() {

        toolbar = findViewById(R.id.toolbar);
        rvNotifications = findViewById(R.id.rvNotifications);
        layoutEmpty = findViewById(R.id.layoutEmpty);

        btnMarkAllRead = findViewById(R.id.btnMarkAllRead);
        btnClearAll = findViewById(R.id.btnClearAll);

        repository = new NotificationRepository(this);

        sessionManager = new SessionManager(this);

        LoginResponseDTO user = sessionManager.getUser();

        if (user == null || user.getUserId() == null) {
            Toast.makeText(this, "No session found. Please log in again.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        userId = user.getUserId();

        adapter = new NotificationAdapter(
                this,
                notificationList,
                new NotificationAdapter.OnNotificationClickListener() {

                    @Override
                    public void onNotificationClick(NotificationResponseDTO notification) {
                        markAsRead(notification);
                    }

                    @Override
                    public void onDelete(NotificationResponseDTO notification) {
                        deleteOne(notification);
                    }
                });

        rvNotifications.setLayoutManager(
                new LinearLayoutManager(this));

        rvNotifications.setAdapter(adapter);
    }

    private void loadNotifications() {

        repository.getUserNotifications(
                userId,
                new Callback<List<NotificationResponseDTO>>() {

                    @Override
                    public void onResponse(
                            Call<List<NotificationResponseDTO>> call,
                            Response<List<NotificationResponseDTO>> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            notificationList.clear();

                            notificationList.addAll(response.body());

                            adapter.notifyDataSetChanged();

                            updateEmptyState();

                        } else {

                            Toast.makeText(
                                    NotificationActivity.this,
                                    "Failed to load notifications",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<List<NotificationResponseDTO>> call,
                            Throwable t) {

                        Toast.makeText(
                                NotificationActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    private void markAllRead() {

        repository.markAllNotificationsAsRead(
                userId,
                new Callback<Void>() {

                    @Override
                    public void onResponse(
                            Call<Void> call,
                            Response<Void> response) {

                        if (response.isSuccessful()) {

                            Toast.makeText(
                                    NotificationActivity.this,
                                    "All notifications marked as read",
                                    Toast.LENGTH_SHORT
                            ).show();

                            loadNotifications();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<Void> call,
                            Throwable t) {

                        Toast.makeText(
                                NotificationActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    private void markAsRead(NotificationResponseDTO notification) {

        if (notification == null || notification.getId() == null) {
            return;
        }

        repository.markNotificationAsRead(
                notification.getId(),
                userId,
                new Callback<NotificationResponseDTO>() {

                    @Override
                    public void onResponse(
                            Call<NotificationResponseDTO> call,
                            Response<NotificationResponseDTO> response) {

                        if (response.isSuccessful()) {
                            loadNotifications();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<NotificationResponseDTO> call,
                            Throwable t) {

                        Toast.makeText(
                                NotificationActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    private void deleteOne(NotificationResponseDTO notification) {

        if (notification == null || notification.getId() == null) {
            return;
        }

        repository.deleteNotification(
                notification.getId(),
                userId,
                new Callback<Void>() {

                    @Override
                    public void onResponse(
                            Call<Void> call,
                            Response<Void> response) {

                        if (response.isSuccessful()) {
                            loadNotifications();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<Void> call,
                            Throwable t) {

                        Toast.makeText(
                                NotificationActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    private void deleteAll() {

        repository.deleteAllNotifications(
                userId,
                new Callback<Void>() {

                    @Override
                    public void onResponse(
                            Call<Void> call,
                            Response<Void> response) {

                        if (response.isSuccessful()) {

                            Toast.makeText(
                                    NotificationActivity.this,
                                    "Notifications cleared",
                                    Toast.LENGTH_SHORT
                            ).show();

                            notificationList.clear();

                            adapter.notifyDataSetChanged();

                            updateEmptyState();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<Void> call,
                            Throwable t) {

                        Toast.makeText(
                                NotificationActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    private void updateEmptyState() {
        layoutEmpty.setVisibility(
                notificationList.isEmpty() ? View.VISIBLE : View.GONE);

        rvNotifications.setVisibility(
                notificationList.isEmpty() ? View.GONE : View.VISIBLE);
    }
}