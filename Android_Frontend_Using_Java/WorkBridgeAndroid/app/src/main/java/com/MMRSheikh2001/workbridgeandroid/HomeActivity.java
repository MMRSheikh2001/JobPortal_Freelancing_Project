package com.MMRSheikh2001.workbridgeandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.MMRSheikh2001.workbridgeandroid.repository.AuthRepository;
import com.MMRSheikh2001.workbridgeandroid.repository.NotificationRepository;
import com.MMRSheikh2001.workbridgeandroid.response.LoginResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.UserDashboardDTO;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private MaterialButton btnFindJobs;
    private MaterialButton btnMyApplications;
    private MaterialButton btnProfile;
    private MaterialButton btnLogout;
    private SessionManager sessionManager;

    private TextView tvUserName;
    private TextView tvProfileCompletion;
    private ProgressBar progressProfile;

    private TextView tvAppliedJobs;
    private TextView tvSavedJobs;
    private TextView tvMessages;
    private TextView tvNotifications;

    private MaterialCardView cardNotifications;

    private AuthRepository authRepository;

    private NotificationRepository notificationRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        init();

        loadDashboard();

        LoginResponseDTO user = sessionManager.getUser();
        if (user != null) {
            tvWelcome.setText("Welcome, " + user.getDisplayName() + " !");
        }

        btnLogout.setOnClickListener(v -> logout());

        cardNotifications.setOnClickListener(v -> goToNotifications());

    }

    private void init() {
        tvWelcome = findViewById(R.id.tvWelcome);

        btnFindJobs = findViewById(R.id.btnFindJobs);
        btnMyApplications = findViewById(R.id.btnApplications);
        btnProfile = findViewById(R.id.btnProfile);

        btnFindJobs.setOnClickListener(v -> goToJobList());
        btnMyApplications.setOnClickListener(v -> goToMyApplications());
        btnProfile.setOnClickListener(v -> goToMyProfile());

        btnLogout = findViewById(R.id.btnLogout);

        tvUserName = findViewById(R.id.tvUserName);
        tvProfileCompletion = findViewById(R.id.tvProfileCompletion);

        progressProfile = findViewById(R.id.progressProfile);

        tvAppliedJobs = findViewById(R.id.tvAppliedJobs);
        tvSavedJobs = findViewById(R.id.tvSavedJobs);
        tvMessages = findViewById(R.id.tvMessages);
        tvNotifications = findViewById(R.id.tvNotifications);

        cardNotifications = findViewById(R.id.cardNotifications);

        authRepository = new AuthRepository(this);
        sessionManager = new SessionManager(this);

        sessionManager = new SessionManager(this);

        notificationRepository =
                new NotificationRepository(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshNotificationCount();
    }


    private void logout() {
        sessionManager.logout();
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);


        finish();
    }

    private void goToJobList() {

        Intent intent = new Intent(HomeActivity.this, JobListActivity.class);
        startActivity(intent);

    }

    private void goToMyApplications() {

        Intent intent = new Intent(
                HomeActivity.this,
                MyApplications.class);

        startActivity(intent);

    }

    private void goToMyProfile() {

        Intent intent = new Intent(
                HomeActivity.this,
                ProfileCenterActivity.class);

        startActivity(intent);

    }


    //load dashboard
    private void loadDashboard() {

        LoginResponseDTO sessionUser = sessionManager.getUser();
        if (sessionUser == null || sessionUser.getUserId() == null) {
            return;
        }
        Long userId = sessionUser.getUserId();

        authRepository.getUserDashboard(userId,
                new Callback<UserDashboardDTO>() {

                    @Override
                    public void onResponse(Call<UserDashboardDTO> call,
                                           Response<UserDashboardDTO> response) {

                        if (!response.isSuccessful() || response.body() == null) {
                            Toast.makeText(HomeActivity.this,
                                    "Failed to load dashboard",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        UserDashboardDTO dto = response.body();

                        tvUserName.setText(dto.getUserName());

                        int completion = dto.getProfileCompletion() == null
                                ? 0
                                : dto.getProfileCompletion();

                        tvProfileCompletion.setText(
                                "Profile Completion: " + completion + "%");

                        progressProfile.setProgress(completion);

                        tvAppliedJobs.setText(
                                String.valueOf(dto.getAppliedJobs()));

                        tvSavedJobs.setText(
                                String.valueOf(dto.getSavedJobs()));

                        tvMessages.setText(
                                String.valueOf(dto.getUnreadMessages()));

                        Long unread = dto.getUnreadNotifications();

                        tvNotifications.setText(
                                unread == null ? "0" : String.valueOf(unread));
                    }

                    @Override
                    public void onFailure(Call<UserDashboardDTO> call,
                                          Throwable t) {

                        Toast.makeText(HomeActivity.this,
                                "Unable to connect to server",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void goToNotifications() {

        Intent intent =
                new Intent(
                        HomeActivity.this,
                        NotificationActivity.class);

        startActivity(intent);
    }

    private void refreshNotificationCount() {

        Long userId = sessionManager
                .getUser()
                .getUserId();

        notificationRepository.getUnreadNotificationCount(
                userId,
                new Callback<Long>() {

                    @Override
                    public void onResponse(Call<Long> call,
                                           Response<Long> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            tvNotifications.setText(
                                    String.valueOf(response.body()));

                        }
                    }

                    @Override
                    public void onFailure(Call<Long> call,
                                          Throwable t) {


                    }
                });

    }


}