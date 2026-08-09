package com.MMRSheikh2001.workbridgeandroid;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.MMRSheikh2001.workbridgeandroid.adapter.EducationAdapter;
import com.MMRSheikh2001.workbridgeandroid.adapter.ExperienceAdapter;
import com.MMRSheikh2001.workbridgeandroid.adapter.ExtracurricularAdapter;
import com.MMRSheikh2001.workbridgeandroid.adapter.PortfolioAdapter;
import com.MMRSheikh2001.workbridgeandroid.adapter.ReferenceAdapter;
import com.MMRSheikh2001.workbridgeandroid.adapter.TrainingAdapter;
import com.MMRSheikh2001.workbridgeandroid.adapter.UserLanguageAdapter;
import com.MMRSheikh2001.workbridgeandroid.adapter.UserSkillAdapter;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.ResumeRepository;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.EducationResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ExperienceResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ExtracurricularResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.PortfolioResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ReferenceResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ResumeResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.TrainingResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.UserLanguageResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.UserSkillResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResumeActivity extends AppCompatActivity {


    //==========================================================
    // Views
    //==========================================================

    private MaterialToolbar toolbar;

    private MaterialButton btnDownloadPdf;

    private TextView tvName;
    private TextView tvHeadline;
    private TextView tvPhone;
    private TextView tvEmail;
    private TextView tvCareerObjective;

    private RecyclerView rvEducation;
    private RecyclerView rvExperience;
    private RecyclerView rvSkill;
    private RecyclerView rvLanguage;
    private RecyclerView rvTraining;
    private RecyclerView rvPortfolio;
    private RecyclerView rvReference;
    private RecyclerView rvExtracurricular;

    //==========================================================
    // Data
    //==========================================================

    private ResumeRepository repository;

    private ResumeResponseDTO resume;

    private Long userProfileId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);


        repository = new ResumeRepository(this);

        SessionManager sessionManager = new SessionManager(this);

        if (sessionManager.getUser() == null || sessionManager.getUser().getProfileId() == null) {
            Toast.makeText(this, "No profile found. Please log in again.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        userProfileId = sessionManager.getUser().getProfileId();

        initializeViews();

        setupRecyclerViews();

        loadResume();

        btnDownloadPdf.setOnClickListener(v ->
                downloadPdf());

        toolbar.setNavigationOnClickListener(v ->
                finish());


    }


    //==========================================================
    // Initialize Views
    //==========================================================

    private void initializeViews() {

        toolbar = findViewById(R.id.toolbar);

        btnDownloadPdf = findViewById(R.id.btnDownloadPdf);

        tvName = findViewById(R.id.tvName);
        tvHeadline = findViewById(R.id.tvHeadline);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvCareerObjective = findViewById(R.id.tvCareerObjective);

        rvEducation = findViewById(R.id.rvEducation);
        rvExperience = findViewById(R.id.rvExperience);
        rvSkill = findViewById(R.id.rvSkill);
        rvLanguage = findViewById(R.id.rvLanguage);
        rvTraining = findViewById(R.id.rvTraining);
        rvPortfolio = findViewById(R.id.rvPortfolio);
        rvReference = findViewById(R.id.rvReference);
        rvExtracurricular = findViewById(R.id.rvExtracurricular);

    }

    //==========================================================
    // RecyclerViews
    //==========================================================

    private void setupRecyclerViews() {

        rvEducation.setLayoutManager(
                new LinearLayoutManager(this));

        rvExperience.setLayoutManager(
                new LinearLayoutManager(this));

        rvSkill.setLayoutManager(
                new LinearLayoutManager(this));

        rvLanguage.setLayoutManager(
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
    // Load Resume
    //==========================================================

    private void loadResume() {

        repository.getResume(
                userProfileId,
                new Callback<ResumeResponseDTO>() {

                    @Override
                    public void onResponse(
                            Call<ResumeResponseDTO> call,
                            Response<ResumeResponseDTO> response) {

                        if (!response.isSuccessful()
                                || response.body() == null) {

                            Toast.makeText(
                                    ResumeActivity.this,
                                    "Failed to load resume (code " + response.code() + ")",
                                    Toast.LENGTH_LONG
                            ).show();

                            return;
                        }

                        resume = response.body();

                        populateProfile();

                        bindRecyclerViews();

                    }

                    @Override
                    public void onFailure(
                            Call<ResumeResponseDTO> call,
                            Throwable t) {

                        Toast.makeText(
                                ResumeActivity.this,
                                "Network error: " + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();

                    }

                });

    }

    //==========================================================
    // Populate Profile
    //==========================================================

    private void populateProfile() {

        if (resume == null || resume.getProfile() == null) {
            return;
        }

        tvName.setText(
                valueOrEmpty(resume.getProfile().getName()));

        tvHeadline.setText(
                valueOrEmpty(resume.getProfile().getHeadline()));

        tvPhone.setText(
                valueOrEmpty(resume.getProfile().getPhone()));

        tvEmail.setText(
                valueOrEmpty(resume.getProfile().getUserEmail()));

        tvCareerObjective.setText(
                valueOrEmpty(resume.getProfile().getCareerObjective()));

    }

    //==========================================================
    // Bind RecyclerViews
    //==========================================================

    private void bindRecyclerViews() {

        //==========================================
        // Education
        //==========================================

        if (resume.getEducations() != null &&
                !resume.getEducations().isEmpty()) {

            EducationAdapter educationAdapter =
                    new EducationAdapter(
                            this,
                            resume.getEducations(),
                            new EducationAdapter.OnEducationClickListener() {

                                @Override
                                public void onEdit(EducationResponseDTO education) {
                                }

                                @Override
                                public void onDelete() {
                                }

                            });

            rvEducation.setAdapter(educationAdapter);

        }

        //==========================================
        // Experience
        //==========================================

        if (resume.getExperiences() != null &&
                !resume.getExperiences().isEmpty()) {

            ExperienceAdapter experienceAdapter =
                    new ExperienceAdapter(
                            this,
                            resume.getExperiences(),
                            new ExperienceAdapter.OnExperienceClickListener() {

                                @Override
                                public void onEdit(ExperienceResponseDTO experience) {
                                }


                            });

            rvExperience.setAdapter(experienceAdapter);

        }

        //==========================================
        // Skills
        //==========================================

        if (resume.getSkills() != null &&
                !resume.getSkills().isEmpty()) {

            UserSkillAdapter adapter =
                    new UserSkillAdapter(
                            this,
                            resume.getSkills(),
                            new UserSkillAdapter.OnUserSkillClickListener() {

                                @Override
                                public void onEdit(UserSkillResponseDTO skill) {
                                }


                            });

            rvSkill.setAdapter(adapter);

        }

        //==========================================
        // Languages
        //==========================================

        if (resume.getLanguages() != null &&
                !resume.getLanguages().isEmpty()) {

            UserLanguageAdapter adapter =
                    new UserLanguageAdapter(
                            this,
                            resume.getLanguages(),
                            new UserLanguageAdapter.OnUserLanguageClickListener() {

                                @Override
                                public void onEdit(UserLanguageResponseDTO language) {
                                }


                            });

            rvLanguage.setAdapter(adapter);

        }

        //==========================================
        // Training
        //==========================================

        if (resume.getTrainings() != null &&
                !resume.getTrainings().isEmpty()) {

            TrainingAdapter adapter =
                    new TrainingAdapter(
                            this,
                            resume.getTrainings(),
                            new TrainingAdapter.OnTrainingClickListener() {

                                @Override
                                public void onEdit(TrainingResponseDTO training) {
                                }


                            });

            rvTraining.setAdapter(adapter);

        }

        //==========================================
        // Portfolio
        //==========================================

        if (resume.getPortfolios() != null &&
                !resume.getPortfolios().isEmpty()) {

            PortfolioAdapter adapter =
                    new PortfolioAdapter(
                            this,
                            resume.getPortfolios(),
                            new PortfolioAdapter.OnPortfolioClickListener() {

                                @Override
                                public void onEdit(PortfolioResponseDTO portfolio) {
                                }


                            });

            rvPortfolio.setAdapter(adapter);

        }

        //==========================================
        // Reference
        //==========================================

        if (resume.getReferences() != null &&
                !resume.getReferences().isEmpty()) {

            ReferenceAdapter adapter =
                    new ReferenceAdapter(
                            this,
                            resume.getReferences(),
                            new ReferenceAdapter.OnReferenceClickListener() {

                                @Override
                                public void onEdit(ReferenceResponseDTO reference) {
                                }


                            });

            rvReference.setAdapter(adapter);

        }

        //==========================================
        // Extracurricular
        //==========================================

        if (resume.getExtracurriculars() != null &&
                !resume.getExtracurriculars().isEmpty()) {

            ExtracurricularAdapter adapter =
                    new ExtracurricularAdapter(
                            this,
                            resume.getExtracurriculars(),
                            new ExtracurricularAdapter.OnExtracurricularClickListener() {

                                @Override
                                public void onEdit(ExtracurricularResponseDTO extracurricular) {
                                }


                            });

            rvExtracurricular.setAdapter(adapter);

        }

    }


    //==========================================================
    // Download Resume PDF
    //==========================================================

    private void downloadPdf() {

        repository.downloadResumePdf(
                userProfileId,
                new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(
                            Call<ResponseBody> call,
                            Response<ResponseBody> response) {

                        if (!response.isSuccessful()
                                || response.body() == null) {

                            Toast.makeText(
                                    ResumeActivity.this,
                                    "Download failed.",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        savePdf(response.body());

                    }

                    @Override
                    public void onFailure(
                            Call<ResponseBody> call,
                            Throwable t) {

                        Toast.makeText(
                                ResumeActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();

                    }

                });

    }

    //==========================================================
    // Save PDF
    //==========================================================

    private void savePdf(ResponseBody body) {

        String fileName = "Resume.pdf";

        try {

            InputStream inputStream = body.byteStream();

            OutputStream outputStream;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                ContentValues values = new ContentValues();

                values.put(
                        MediaStore.Downloads.DISPLAY_NAME,
                        fileName);

                values.put(
                        MediaStore.Downloads.MIME_TYPE,
                        "application/pdf");

                values.put(
                        MediaStore.Downloads.RELATIVE_PATH,
                        Environment.DIRECTORY_DOWNLOADS);

                Uri uri = getContentResolver().insert(
                        MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                        values);

                if (uri == null) {

                    Toast.makeText(
                            this,
                            "Cannot create file.",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                outputStream =
                        getContentResolver().openOutputStream(uri);

            } else {

                File downloads =
                        Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOWNLOADS);

                File file =
                        new File(downloads, fileName);

                outputStream =
                        new FileOutputStream(file);

            }

            byte[] buffer = new byte[4096];

            int length;

            while ((length = inputStream.read(buffer)) != -1) {

                outputStream.write(buffer, 0, length);

            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

            Toast.makeText(
                    this,
                    "Resume saved to Downloads.",
                    Toast.LENGTH_LONG
            ).show();

        } catch (IOException e) {

            Toast.makeText(
                    this,
                    e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();

        }

    }

    //==========================================================
    // Helper
    //==========================================================

    private String valueOrEmpty(String value) {

        return value == null ? "" : value;

    }

}