package com.MMRSheikh2001.workbridgeandroid;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.MMRSheikh2001.workbridgeandroid.adapter.EducationAdapter;
import com.MMRSheikh2001.workbridgeandroid.adapter.ExperienceAdapter;
import com.MMRSheikh2001.workbridgeandroid.adapter.ExtracurricularAdapter;
import com.MMRSheikh2001.workbridgeandroid.adapter.PortfolioAdapter;
import com.MMRSheikh2001.workbridgeandroid.adapter.ReferenceAdapter;
import com.MMRSheikh2001.workbridgeandroid.adapter.TrainingAdapter;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.ResumeImportRepository;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.EducationResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ExperienceResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ExtracurricularResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.PortfolioResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ReferenceResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ResumeImportPreviewDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.TrainingResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResumePreviewActivity extends AppCompatActivity {


    //==========================================================
    // Views
    //==========================================================

    private MaterialToolbar toolbar;

    private TextView tvName;
    private TextView tvPhone;
    private TextView tvHeadline;
    private TextView tvFreelancerTitle;
    private TextView tvGithub;
    private TextView tvLinkedin;
    private TextView tvPortfolio;
    private TextView tvNid;
    private TextView tvCareerObjective;

    private RecyclerView rvEducation;
    private RecyclerView rvExperience;
    private RecyclerView rvTraining;
    private RecyclerView rvPortfolio;
    private RecyclerView rvReference;
    private RecyclerView rvExtracurricular;

    private MaterialButton btnCancel;
    private MaterialButton btnImport;

    //==========================================================
    // Repository
    //==========================================================

    private ResumeImportRepository repository;

    //==========================================================
    // Data
    //==========================================================

    private Long userProfileId;

    private ResumeImportPreviewDTO preview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_preview);


        repository = new ResumeImportRepository(this);

        SessionManager sessionManager = new SessionManager(this);

        if (sessionManager.getUser() == null || sessionManager.getUser().getProfileId() == null) {
            Toast.makeText(this, "No profile found. Please log in again.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        userProfileId =
                sessionManager.getUser().getProfileId();

        initViews();

        setupToolbar();

        setupRecyclerViews();

        btnCancel.setOnClickListener(v -> finish());

        btnImport.setOnClickListener(v -> importResume());

        loadPreview();


    }


    //==========================================================
    // Initialize Views
    //==========================================================

    private void initViews() {

        toolbar = findViewById(R.id.toolbar);

        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvHeadline = findViewById(R.id.tvHeadline);
        tvFreelancerTitle = findViewById(R.id.tvFreelancerTitle);
        tvGithub = findViewById(R.id.tvGithub);
        tvLinkedin = findViewById(R.id.tvLinkedin);
        tvPortfolio = findViewById(R.id.tvPortfolio);
        tvNid = findViewById(R.id.tvNid);
        tvCareerObjective = findViewById(R.id.tvCareerObjective);

        rvEducation = findViewById(R.id.rvEducation);
        rvExperience = findViewById(R.id.rvExperience);
        rvTraining = findViewById(R.id.rvTraining);
        rvPortfolio = findViewById(R.id.rvPortfolio);
        rvReference = findViewById(R.id.rvReference);
        rvExtracurricular = findViewById(R.id.rvExtracurricular);

        btnCancel = findViewById(R.id.btnCancel);
        btnImport = findViewById(R.id.btnImport);

    }

    //==========================================================
    // Toolbar
    //==========================================================

    private void setupToolbar() {

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();

            return true;

        }

        return super.onOptionsItemSelected(item);

    }

    //==========================================================
    // RecyclerViews
    //==========================================================

    private void setupRecyclerViews() {

        rvEducation.setLayoutManager(
                new LinearLayoutManager(this));

        rvExperience.setLayoutManager(
                new LinearLayoutManager(this));

        rvTraining.setLayoutManager(
                new LinearLayoutManager(this));

        rvPortfolio.setLayoutManager(
                new LinearLayoutManager(this));

        rvReference.setLayoutManager(
                new LinearLayoutManager(this));

        rvExtracurricular.setLayoutManager(
                new LinearLayoutManager(this));

    }

    //==========================================================
    // Load Preview
    //==========================================================

    private void loadPreview() {

        btnImport.setEnabled(false);

        repository.getResumeImportPreview(
                userProfileId,
                new Callback<ResumeImportPreviewDTO>() {

                    @Override
                    public void onResponse(
                            Call<ResumeImportPreviewDTO> call,
                            Response<ResumeImportPreviewDTO> response) {

                        btnImport.setEnabled(true);

                        if (!response.isSuccessful()
                                || response.body() == null) {

                            Toast.makeText(
                                    ResumePreviewActivity.this,
                                    "Failed to generate AI preview.",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        preview = response.body();

                        populateProfile();

                        bindRecyclerViews();

                    }

                    @Override
                    public void onFailure(
                            Call<ResumeImportPreviewDTO> call,
                            Throwable t) {

                        btnImport.setEnabled(true);

                        Toast.makeText(
                                ResumePreviewActivity.this,
                                "Network Error : " + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();

                    }

                });

    }

    //==========================================================
    // Populate Profile
    //==========================================================

    private void populateProfile() {

        if (preview == null || preview.getProfile() == null) {
            return;
        }

        if (preview.getProfile().getName() != null)
            tvName.setText("Name : " + preview.getProfile().getName());

        if (preview.getProfile().getPhone() != null)
            tvPhone.setText("Phone : " + preview.getProfile().getPhone());

        if (preview.getProfile().getHeadline() != null)
            tvHeadline.setText("Headline : " + preview.getProfile().getHeadline());

        if (preview.getProfile().getFreelancerTitle() != null)
            tvFreelancerTitle.setText(
                    "Freelancer Title : "
                            + preview.getProfile().getFreelancerTitle());

        if (preview.getProfile().getGithubLink() != null)
            tvGithub.setText(
                    "GitHub : "
                            + preview.getProfile().getGithubLink());

        if (preview.getProfile().getLinkedinLink() != null)
            tvLinkedin.setText(
                    "LinkedIn : "
                            + preview.getProfile().getLinkedinLink());

        if (preview.getProfile().getPortfolioWebsite() != null)
            tvPortfolio.setText(
                    "Portfolio : "
                            + preview.getProfile().getPortfolioWebsite());

        if (preview.getProfile().getNidNumber() != null)
            tvNid.setText(
                    "NID : "
                            + preview.getProfile().getNidNumber());

        if (preview.getProfile().getCareerObjective() != null)
            tvCareerObjective.setText(
                    preview.getProfile().getCareerObjective());

    }

    //==========================================================
    // Bind RecyclerViews
    //==========================================================

    private void bindRecyclerViews() {

        //==========================================
        // Education
        //==========================================

        if (preview.getEducations() != null &&
                !preview.getEducations().isEmpty()) {

            EducationAdapter educationAdapter =
                    new EducationAdapter(
                            this,
                            preview.getEducations(),
                            new EducationAdapter.OnEducationClickListener() {

                                @Override
                                public void onEdit(EducationResponseDTO education) {
                                    // Do nothing
                                }

                                @Override
                                public void onDelete() {
                                    // Do nothing
                                }

                            });

            rvEducation.setAdapter(educationAdapter);

        }

        //==========================================
// Experience
//==========================================

        if (preview.getExperiences() != null &&
                !preview.getExperiences().isEmpty()) {

            ExperienceAdapter experienceAdapter =
                    new ExperienceAdapter(
                            this,
                            preview.getExperiences(),
                            new ExperienceAdapter.OnExperienceClickListener() {

                                @Override
                                public void onEdit(ExperienceResponseDTO experience) {
                                }


                            });

            rvExperience.setAdapter(experienceAdapter);

        }

        //==========================================
// Training
//==========================================

        if (preview.getTrainings() != null &&
                !preview.getTrainings().isEmpty()) {

            TrainingAdapter trainingAdapter =
                    new TrainingAdapter(
                            this,
                            preview.getTrainings(),
                            new TrainingAdapter.OnTrainingClickListener() {

                                @Override
                                public void onEdit(TrainingResponseDTO training) {
                                }


                            });

            rvTraining.setAdapter(trainingAdapter);

        }
//==========================================
// Portfolio
//==========================================

        if (preview.getPortfolios() != null &&
                !preview.getPortfolios().isEmpty()) {

            PortfolioAdapter portfolioAdapter =
                    new PortfolioAdapter(
                            this,
                            preview.getPortfolios(),
                            new PortfolioAdapter.OnPortfolioClickListener() {

                                @Override
                                public void onEdit(PortfolioResponseDTO portfolio) {
                                }


                            });

            rvPortfolio.setAdapter(portfolioAdapter);

        }

//==========================================
// Reference
//==========================================

        if (preview.getReferences() != null &&
                !preview.getReferences().isEmpty()) {

            ReferenceAdapter referenceAdapter =
                    new ReferenceAdapter(
                            this,
                            preview.getReferences(),
                            new ReferenceAdapter.OnReferenceClickListener() {

                                @Override
                                public void onEdit(ReferenceResponseDTO reference) {
                                }


                            });

            rvReference.setAdapter(referenceAdapter);

        }
        //==========================================
// Extracurricular
//==========================================

        if (preview.getExtracurriculars() != null &&
                !preview.getExtracurriculars().isEmpty()) {

            ExtracurricularAdapter extracurricularAdapter =
                    new ExtracurricularAdapter(
                            this,
                            preview.getExtracurriculars(),
                            new ExtracurricularAdapter.OnExtracurricularClickListener() {

                                @Override
                                public void onEdit(ExtracurricularResponseDTO extracurricular) {
                                }


                            });

            rvExtracurricular.setAdapter(extracurricularAdapter);

        }

    }
    //==========================================================
    // Import Resume
    //==========================================================

    private void importResume() {

        if (preview == null) {

            Toast.makeText(
                    this,
                    "Nothing to import.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        btnImport.setEnabled(false);
        btnImport.setText("Importing...");

        repository.saveImportedResume(
                userProfileId,
                preview,
                new Callback<Void>() {

                    @Override
                    public void onResponse(
                            Call<Void> call,
                            Response<Void> response) {

                        btnImport.setEnabled(true);
                        btnImport.setText("Import Resume");

                        if (response.isSuccessful()) {

                            Toast.makeText(
                                    ResumePreviewActivity.this,
                                    "Resume imported successfully.",
                                    Toast.LENGTH_LONG
                            ).show();

                            finish();

                        } else {

                            Toast.makeText(
                                    ResumePreviewActivity.this,
                                    "Failed to import resume.",
                                    Toast.LENGTH_SHORT
                            ).show();

                        }

                    }

                    @Override
                    public void onFailure(
                            Call<Void> call,
                            Throwable t) {

                        btnImport.setEnabled(true);
                        btnImport.setText("Import Resume");

                        Toast.makeText(
                                ResumePreviewActivity.this,
                                "Network Error : " + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();

                    }

                });

    }


}