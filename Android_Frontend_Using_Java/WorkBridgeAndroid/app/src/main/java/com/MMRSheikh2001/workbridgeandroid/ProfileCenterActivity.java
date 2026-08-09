package com.MMRSheikh2001.workbridgeandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.MMRSheikh2001.workbridgeandroid.repository.AuthRepository;
import com.MMRSheikh2001.workbridgeandroid.response.LoginResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.UserDashboardDTO;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;
import com.google.android.material.card.MaterialCardView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileCenterActivity extends AppCompatActivity {

    private TextView tvCompletion;

    private ProgressBar progressCompletion;

    private MaterialCardView cardPersonalInfo;
    private MaterialCardView cardEducation;
    private MaterialCardView cardExperience;
    private MaterialCardView cardTraining;
    private MaterialCardView cardSkills;
    private MaterialCardView cardLanguages;
    private MaterialCardView cardPortfolio;
    private MaterialCardView cardReference;
    private MaterialCardView cardExtra;
    private MaterialCardView cardResumeFile;
    private MaterialCardView cardResumePreview;

    private AuthRepository authRepository;

    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_center);


        init();

        setClickListeners();

        sessionManager = new SessionManager(this);

        authRepository = new AuthRepository(this);

        loadProfileCompletion();

    }


    private void init() {

        tvCompletion = findViewById(R.id.tvCompletion);
        progressCompletion = findViewById(R.id.progressCompletion);

        cardPersonalInfo = findViewById(R.id.cardPersonalInfo);
        cardEducation = findViewById(R.id.cardEducation);
        cardExperience = findViewById(R.id.cardExperience);
        cardTraining = findViewById(R.id.cardTraining);
        cardSkills = findViewById(R.id.cardSkills);
        cardLanguages = findViewById(R.id.cardLanguages);
        cardPortfolio = findViewById(R.id.cardPortfolio);
        cardReference = findViewById(R.id.cardReference);
        cardExtra = findViewById(R.id.cardExtra);
        cardResumeFile = findViewById(R.id.cardResumeFile);
        cardResumePreview = findViewById(R.id.cardResumePreview);

    }


    private void setClickListeners() {

        cardPersonalInfo.setOnClickListener(v -> goToUserProfile());

        cardEducation.setOnClickListener(v -> goToEducation());

        cardExperience.setOnClickListener(v -> goToExperience());

        cardTraining.setOnClickListener(v -> goToTraining());

        cardSkills.setOnClickListener(v -> goToSkills());

        cardLanguages.setOnClickListener(v -> goToLanguages());

        cardPortfolio.setOnClickListener(v -> goToPortfolio());

        cardReference.setOnClickListener(v -> goToReference());

        cardExtra.setOnClickListener(v -> goToExtracurricular());

        cardResumeFile.setOnClickListener(v -> goToResumeUpload());

        cardResumePreview.setOnClickListener(v -> goToResumePreview());

    }

    //Navigation methods
    private void goToUserProfile() {
        startActivity(new Intent(this, UserProfileActivity.class));
    }

    private void goToEducation() {
        startActivity(new Intent(this, EducationActivity.class));
    }

    private void goToExperience() {
        startActivity(new Intent(this, ExperienceActivity.class));
    }

    private void goToTraining() {
        startActivity(new Intent(this, TrainingActivity.class));
    }

    private void goToSkills() {
        startActivity(new Intent(this, UserSkillActivity.class));
    }

    private void goToLanguages() {
        startActivity(new Intent(this, UserLanguageActivity.class));
    }

    private void goToPortfolio() {
        startActivity(new Intent(this, PortfolioActivity.class));
    }

    private void goToReference() {
        startActivity(new Intent(this, ReferenceActivity.class));
    }

    private void goToExtracurricular() {
        startActivity(new Intent(this, ExtracurricularActivity.class));
    }

    private void goToResumeUpload() {
        startActivity(new Intent(this, ResumeFileActivity.class));
    }

    private void goToResumePreview() {
        startActivity(new Intent(this, ResumeActivity.class));
    }


    private void loadProfileCompletion() {

        LoginResponseDTO login = sessionManager.getUser();

        if (login == null) {
            return;
        }

        authRepository.getUserDashboard(
                login.getUserId(),
                new Callback<UserDashboardDTO>() {

                    @Override
                    public void onResponse(
                            Call<UserDashboardDTO> call,
                            Response<UserDashboardDTO> response) {

                        if (!response.isSuccessful()
                                || response.body() == null) {
                            return;
                        }

                        Integer completion =
                                response.body().getProfileCompletion();

                        if (completion == null) {
                            completion = 0;
                        }

                        progressCompletion.setProgress(completion);

                        tvCompletion.setText(
                                "Profile Completion : "
                                        + completion
                                        + "%");

                    }

                    @Override
                    public void onFailure(
                            Call<UserDashboardDTO> call,
                            Throwable t) {

                    }

                });

    }


}