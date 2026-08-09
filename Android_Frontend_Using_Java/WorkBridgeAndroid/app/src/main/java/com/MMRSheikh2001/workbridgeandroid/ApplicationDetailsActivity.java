package com.MMRSheikh2001.workbridgeandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.enums.ApplicationStatus;
import com.MMRSheikh2001.workbridgeandroid.repository.JobApplicationRepository;
import com.MMRSheikh2001.workbridgeandroid.response.JobApplicationResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.LoginResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicationDetailsActivity extends AppCompatActivity {

    private Long applicationId;


    private JobApplicationRepository jobApplicationRepository;

    private ImageView imgCompanyLogo;

    private TextView tvJobTitle;
    private TextView tvCompany;
    private TextView tvStatus;
    private TextView tvAppliedDate;
    private TextView tvMatchScore;
    private TextView tvInterviewScore;
    private TextView tvFinalScore;
    private TextView tvFeedback;
    private TextView tvCompanyNotes;

    private MaterialButton btnWithdraw;
    private MaterialButton btnInterview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_details);
        init();

        applicationId = getIntent().getLongExtra("APPLICATION_ID", 0);

        if (applicationId != 0) {
            loadApplication();
        }

        btnInterview.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            ApplicationDetailsActivity.this,
                            AIInterviewActivity.class);

            intent.putExtra(
                    "APPLICATION_ID",
                    applicationId);

            startActivity(intent);
        });

        btnWithdraw.setOnClickListener(v -> {
            withdrawApplication();
        });

    }

    private void init() {

        imgCompanyLogo = findViewById(R.id.imgCompanyLogo);

        tvJobTitle = findViewById(R.id.tvJobTitle);
        tvCompany = findViewById(R.id.tvCompany);
        tvStatus = findViewById(R.id.tvStatus);
        tvAppliedDate = findViewById(R.id.tvAppliedDate);
        tvMatchScore = findViewById(R.id.tvMatchScore);
        tvInterviewScore = findViewById(R.id.tvInterviewScore);
        tvFinalScore = findViewById(R.id.tvFinalScore);
        tvFeedback = findViewById(R.id.tvFeedback);
        tvCompanyNotes = findViewById(R.id.tvCompanyNotes);

        btnWithdraw = findViewById(R.id.btnWithdraw);
        btnInterview = findViewById(R.id.btnInterview);

        jobApplicationRepository = new JobApplicationRepository(this);
    }


    private void loadApplication() {

        jobApplicationRepository.getJobApplicationById(
                applicationId,
                new Callback<JobApplicationResponseDTO>() {

                    @Override
                    public void onResponse(
                            Call<JobApplicationResponseDTO> call,
                            Response<JobApplicationResponseDTO> response) {

                        if (!response.isSuccessful() || response.body() == null) {
                            Toast.makeText(
                                    ApplicationDetailsActivity.this,
                                    "Application not found",
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }

                        bind(response.body());
                    }

                    @Override
                    public void onFailure(
                            Call<JobApplicationResponseDTO> call,
                            Throwable t) {

                        Toast.makeText(
                                ApplicationDetailsActivity.this,
                                "Connection failed",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }


    private void bind(JobApplicationResponseDTO dto) {

        tvJobTitle.setText(dto.getJobTitle());

        tvCompany.setText(dto.getCompanyName());

        tvStatus.setText(dto.getStatus().name());

        tvAppliedDate.setText(
                String.valueOf(dto.getAppliedAt()));

        tvMatchScore.setText(
                "Match Score : "
                        + dto.getAiMatchScore());

        tvInterviewScore.setText(
                "Interview Score : "
                        + dto.getAiInterviewScore());

        tvFinalScore.setText(
                "Final Score : "
                        + dto.getAiFinalScore());

        tvFeedback.setText(dto.getAiMatchFeedback());

        tvCompanyNotes.setText(dto.getCompanyNotes());

        Glide.with(this)
                .load(ApiClient.getCompanyLogoUrl(dto.getCompanyLogo()))
                .placeholder(R.drawable.ic_company)
                .error(R.drawable.ic_company)
                .into(imgCompanyLogo);

        updateButtons(dto);
    }


    private void updateButtons(JobApplicationResponseDTO dto) {

        btnWithdraw.setVisibility(View.GONE);
        btnInterview.setVisibility(View.GONE);

        if (dto.getStatus() == ApplicationStatus.APPLIED
                || dto.getStatus() == ApplicationStatus.AI_PENDING
                || dto.getStatus() == ApplicationStatus.AI_COMPLETED
                || dto.getStatus() == ApplicationStatus.AUTOMATIC_QUALIFIED
        ) {

            btnWithdraw.setVisibility(View.VISIBLE);

            if (Boolean.TRUE.equals(dto.getAiInterviewEnabled())) {
                btnInterview.setVisibility(View.VISIBLE);
                btnInterview.setText("Start AI Interview");
            }

        } else if (dto.getStatus() == ApplicationStatus.AI_PENDING) {

            btnInterview.setVisibility(View.VISIBLE);
            btnInterview.setText("Continue AI Interview");
        } else if (dto.getStatus() == ApplicationStatus.AI_COMPLETED
                || dto.getStatus() == ApplicationStatus.AUTOMATIC_QUALIFIED


        ) {

            btnInterview.setVisibility(View.VISIBLE);
            btnInterview.setText("View Interview");
        }
    }


    private void withdrawApplication() {

        SessionManager sessionManager = new SessionManager(this);

        LoginResponseDTO user = sessionManager.getUser();
        if (user == null || user.getProfileId() == null) {
            Toast.makeText(this, "No session found. Please log in again.", Toast.LENGTH_LONG).show();
            return;
        }
        Long userProfileId = user.getProfileId();

        jobApplicationRepository.withdrawApplication(
                applicationId,
                userProfileId,
                new Callback<JobApplicationResponseDTO>() {

                    @Override
                    public void onResponse(Call<JobApplicationResponseDTO> call,
                                           Response<JobApplicationResponseDTO> response) {

                        if (!response.isSuccessful() || response.body() == null) {

                            Toast.makeText(
                                    ApplicationDetailsActivity.this,
                                    "Unable to withdraw application",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        Toast.makeText(
                                ApplicationDetailsActivity.this,
                                "Application withdrawn successfully",
                                Toast.LENGTH_SHORT
                        ).show();

                        bind(response.body());
                    }

                    @Override
                    public void onFailure(Call<JobApplicationResponseDTO> call,
                                          Throwable t) {

                        Toast.makeText(
                                ApplicationDetailsActivity.this,
                                "Unable to connect to server",
                                Toast.LENGTH_SHORT
                        ).show();

                        t.printStackTrace();
                    }
                });
    }


}