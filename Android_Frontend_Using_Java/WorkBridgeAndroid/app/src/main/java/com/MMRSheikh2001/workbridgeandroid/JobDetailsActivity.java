package com.MMRSheikh2001.workbridgeandroid;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.repository.JobApplicationRepository;
import com.MMRSheikh2001.workbridgeandroid.repository.JobRepository;
import com.MMRSheikh2001.workbridgeandroid.request.JobApplicationRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.response.JobApplicationResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.JobResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.LoginResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.ResumeScreeningResult;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobDetailsActivity extends AppCompatActivity {


    private Long jobId;

    private JobRepository jobRepository;


    private TextView tvJobTitle;
    private TextView tvCompany;
    private TextView tvLocation;
    private TextView tvEmployment;
    private TextView tvSalary;
    private TextView tvDeadline;

    private TextView tvDescription;
    private TextView tvResponsibilities;
    private TextView tvEducation;
    private TextView tvExperience;
    private TextView tvAdditional;
    private TextView tvBenefits;

    private ImageView imgCompanyLogo;

    private MaterialButton btnApply;

    private MaterialButton btnJobMatch;

    private JobApplicationRepository jobApplicationRepository;

    private SessionManager sessionManager;

    private JobResponseDTO currentJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_job_details);
        init();
        jobRepository = new JobRepository(this);
        jobId = getIntent().getLongExtra("JOB_ID", 0);
        if (jobId != 0) {
            loadJobDetails();
        }
        btnApply.setOnClickListener(v -> applyJob());
        btnJobMatch.setOnClickListener(v -> calculateJobMatch());

    }

    private void init() {

        tvJobTitle = findViewById(R.id.tvJobTitle);
        tvCompany = findViewById(R.id.tvCompany);
        tvLocation = findViewById(R.id.tvLocation);
        tvEmployment = findViewById(R.id.tvEmployment);
        tvSalary = findViewById(R.id.tvSalary);
        tvDeadline = findViewById(R.id.tvDeadline);

        tvDescription = findViewById(R.id.tvDescription);
        tvResponsibilities = findViewById(R.id.tvResponsibilities);
        tvEducation = findViewById(R.id.tvEducation);
        tvExperience = findViewById(R.id.tvExperience);
        tvAdditional = findViewById(R.id.tvAdditional);
        tvBenefits = findViewById(R.id.tvBenefits);

        imgCompanyLogo = findViewById(R.id.imgCompanyLogo);

        btnApply = findViewById(R.id.btnApply);
        btnJobMatch = findViewById(R.id.btnJobMatch);

        jobRepository = new JobRepository(this);
        jobApplicationRepository = new JobApplicationRepository(this);
        sessionManager = new SessionManager(this);


    }

    private void loadJobDetails() {
        jobRepository.getJobById(jobId, new Callback<JobResponseDTO>() {
            @Override
            public void onResponse(Call<JobResponseDTO> call, Response<JobResponseDTO> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(JobDetailsActivity.this,
                            "Job not found",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                JobResponseDTO job = response.body();

                bindJob(job);
            }

            @Override
            public void onFailure(Call<JobResponseDTO> call, Throwable t) {

                Toast.makeText(JobDetailsActivity.this,
                        "Unable to connect to server",
                        Toast.LENGTH_SHORT).show();

                t.printStackTrace();
            }
        });
    }

    private void bindJob(JobResponseDTO job) {

        tvJobTitle.setText(job.getTitle());

        tvCompany.setText(job.getCompanyName());

        tvLocation.setText(
                job.getLocationPoliceStationName()
                        + ", "
                        + job.getLocationDistrictName());

        tvEmployment.setText(
                job.getEmploymentType()
                        + " | "
                        + job.getWorkPlaceType());

        if (Boolean.TRUE.equals(job.getIsNegotiable())) {

            tvSalary.setText("Salary: Negotiable");

        } else {

            tvSalary.setText(
                    "Salary: "
                            + job.getSalaryMin()
                            + " - "
                            + job.getSalaryMax());

        }

        tvDeadline.setText(
                "Deadline: " + job.getApplicationDeadline());

        tvDescription.setText(job.getJobDescription());

        tvResponsibilities.setText(job.getJobResponsibilities());

        tvEducation.setText(job.getEducationalRequirements());

        tvExperience.setText(job.getExperienceRequirements());

        tvAdditional.setText(job.getAdditionalRequirements());

        tvBenefits.setText(job.getBenefits());

        String logo = job.getCompanyLogo();

        if (logo != null && !logo.isEmpty()) {

            String imageUrl = ApiClient.BASE_URL
                    + "api/files/companyprofiles/"
                    + logo;

            Glide.with(this)
                    .load(ApiClient.getCompanyLogoUrl(job.getCompanyLogo()))
                    .placeholder(R.drawable.ic_company)
                    .error(R.drawable.ic_company)
                    .into(imgCompanyLogo);
        }

        currentJob = job;

        checkAlreadyApplied();

        btnApply.setOnClickListener(v -> applyJob());

    }


    private void applyJob() {

        LoginResponseDTO login = sessionManager.getUser();

        if (login == null) {
            Toast.makeText(this,
                    "Please login first",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        JobApplicationRequestDTO dto = new JobApplicationRequestDTO();

        dto.setJobId(currentJob.getId());
        dto.setUserProfileId(login.getProfileId());

        btnApply.setEnabled(false);

        jobApplicationRepository.applyJob(dto,
                new Callback<JobApplicationResponseDTO>() {

                    @Override
                    public void onResponse(Call<JobApplicationResponseDTO> call,
                                           Response<JobApplicationResponseDTO> response) {

                        btnApply.setEnabled(true);

                        if (!response.isSuccessful()) {

                            Toast.makeText(JobDetailsActivity.this,
                                    "Application failed",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(JobDetailsActivity.this,
                                "Application submitted successfully",
                                Toast.LENGTH_SHORT).show();

                        btnApply.setText("Applied");
                        btnApply.setEnabled(false);

                        checkAlreadyApplied();
                    }

                    @Override
                    public void onFailure(Call<JobApplicationResponseDTO> call,
                                          Throwable t) {

                        btnApply.setEnabled(true);

                        Toast.makeText(JobDetailsActivity.this,
                                "Unable to connect to server",
                                Toast.LENGTH_SHORT).show();

                        t.printStackTrace();
                    }
                });
    }


    private void checkAlreadyApplied() {

        LoginResponseDTO login = sessionManager.getUser();

        if (login == null) {
            return;
        }

        btnApply.setEnabled(false);
        btnApply.setText("Checking...");

        jobApplicationRepository.existsApplication(
                currentJob.getId(),
                login.getProfileId(),
                new Callback<Boolean>() {

                    @Override
                    public void onResponse(Call<Boolean> call,
                                           Response<Boolean> response) {

                        if (!response.isSuccessful() || response.body() == null) {
                            return;
                        }

                        boolean applied = response.body();

                        if (applied) {
                            btnApply.setText("Applied");
                            btnApply.setEnabled(false);
                        } else {
                            btnApply.setText("Apply Now");
                            btnApply.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call,
                                          Throwable t) {

                        Toast.makeText(JobDetailsActivity.this,
                                "Unable to verify application status",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void calculateJobMatch() {

        LoginResponseDTO login = sessionManager.getUser();

        if (login == null) {

            Toast.makeText(
                    this,
                    "Please login first",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        Long userProfileId = login.getProfileId();

        if (userProfileId == null) {

            Toast.makeText(
                    this,
                    "Please complete your profile first",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (currentJob == null) {

            Toast.makeText(
                    this,
                    "Job information is not available",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        btnJobMatch.setEnabled(false);
        btnJobMatch.setText("Calculating...");

        jobRepository.calculateJobMatch(
                currentJob.getId(),
                userProfileId,
                new Callback<ResumeScreeningResult>() {

                    @Override
                    public void onResponse(
                            Call<ResumeScreeningResult> call,
                            Response<ResumeScreeningResult> response) {

                        btnJobMatch.setEnabled(true);
                        btnJobMatch.setText("Check Job Match");

                        if (!response.isSuccessful()
                                || response.body() == null) {

                            Toast.makeText(
                                    JobDetailsActivity.this,
                                    "Unable to calculate job match",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        ResumeScreeningResult result = response.body();

                        showJobMatchDialog(result);
                    }

                    @Override
                    public void onFailure(
                            Call<ResumeScreeningResult> call,
                            Throwable t) {

                        btnJobMatch.setEnabled(true);
                        btnJobMatch.setText("Check Job Match");

                        Toast.makeText(
                                JobDetailsActivity.this,
                                "Unable to connect to server",
                                Toast.LENGTH_SHORT
                        ).show();

                        t.printStackTrace();
                    }
                }
        );
    }


    private void showJobMatchDialog(ResumeScreeningResult result) {

        String scoreText;

        if (result.getMatchScore() != null) {
            scoreText = result.getMatchScore() + "%";
        } else {
            scoreText = "N/A";
        }

        String feedback = result.getFeedback();

        if (feedback == null || feedback.trim().isEmpty()) {
            feedback = "No feedback available.";
        }

        String message =
                "Match Score\n"
                        + scoreText
                        + "\n\n"
                        + "Feedback\n"
                        + feedback;

        new MaterialAlertDialogBuilder(this)
                .setTitle("Job Match Result")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }


}