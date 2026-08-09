package com.MMRSheikh2001.workbridgeandroid;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.MMRSheikh2001.workbridgeandroid.adapter.AIQuestionAdapter;
import com.MMRSheikh2001.workbridgeandroid.repository.JobApplicationRepository;
import com.MMRSheikh2001.workbridgeandroid.response.AIInterviewSessionResponseDTO;
import com.google.android.material.button.MaterialButton;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AIInterviewActivity extends AppCompatActivity {


    private Long applicationId;

    private RecyclerView rvQuestions;

    private MaterialButton btnSubmit;

    private TextView tvTitle;
    private TextView tvInfo;

    private JobApplicationRepository repository;

    private AIQuestionAdapter adapter;

    private AIInterviewSessionResponseDTO session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_aiinterview);

        init();

        applicationId = getIntent().getLongExtra("APPLICATION_ID", 0);

        if (applicationId == 0) {
            finish();
            return;
        }

        loadInterview();

    }

    private void init() {

        tvTitle = findViewById(R.id.tvTitle);
        tvInfo = findViewById(R.id.tvInfo);

        rvQuestions = findViewById(R.id.rvQuestions);

        btnSubmit = findViewById(R.id.btnSubmit);

        rvQuestions.setLayoutManager(
                new LinearLayoutManager(this));

        repository = new JobApplicationRepository(this);



        btnSubmit.setOnClickListener(v -> submitInterview());

    }


    private void loadInterview() {

        repository.getInterviewByApplicationId(
                applicationId,
                new Callback<AIInterviewSessionResponseDTO>() {

                    @Override
                    public void onResponse(
                            Call<AIInterviewSessionResponseDTO> call,
                            Response<AIInterviewSessionResponseDTO> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            session = response.body();

                            showInterview(true);

                        } else {

                            startInterview();

                        }

                    }

                    @Override
                    public void onFailure(
                            Call<AIInterviewSessionResponseDTO> call,
                            Throwable t) {

                        startInterview();

                    }
                });

    }


    private void startInterview() {

        repository.startInterview(
                applicationId,
                new Callback<AIInterviewSessionResponseDTO>() {

                    @Override
                    public void onResponse(
                            Call<AIInterviewSessionResponseDTO> call,
                            Response<AIInterviewSessionResponseDTO> response) {

                        if (!response.isSuccessful()
                                || response.body() == null) {

                            Toast.makeText(
                                    AIInterviewActivity.this,
                                    "Unable to start interview",
                                    Toast.LENGTH_SHORT).show();

                            finish();
                            return;
                        }

                        session = response.body();

                        showInterview(false);

                    }

                    @Override
                    public void onFailure(
                            Call<AIInterviewSessionResponseDTO> call,
                            Throwable t) {

                        Toast.makeText(
                                AIInterviewActivity.this,
                                "Connection failed",
                                Toast.LENGTH_SHORT).show();

                    }
                });

    }


    private void showInterview(boolean readOnly) {

        adapter = new AIQuestionAdapter(
                this,
                session.getQuestions(),
                readOnly);

        rvQuestions.setAdapter(adapter);

        if (readOnly) {

            tvInfo.setText("Interview completed");

            btnSubmit.setVisibility(View.GONE);

        } else {

            tvInfo.setText("Answer every question carefully.");

            btnSubmit.setVisibility(View.VISIBLE);

        }

    }


    private void submitInterview() {

        btnSubmit.setEnabled(false);

        session.setQuestions(
                adapter.getQuestionList());

        repository.submitInterview(
                session,
                new Callback<AIInterviewSessionResponseDTO>() {

                    @Override
                    public void onResponse(
                            Call<AIInterviewSessionResponseDTO> call,
                            Response<AIInterviewSessionResponseDTO> response) {

                        btnSubmit.setEnabled(true);

                        if (!response.isSuccessful()
                                || response.body() == null) {

                            Toast.makeText(
                                    AIInterviewActivity.this,
                                    "Submission failed",
                                    Toast.LENGTH_SHORT).show();

                            return;
                        }

                        Toast.makeText(
                                AIInterviewActivity.this,
                                "Interview submitted",
                                Toast.LENGTH_SHORT).show();

                        session = response.body();

                        showInterview(true);

                    }

                    @Override
                    public void onFailure(
                            Call<AIInterviewSessionResponseDTO> call,
                            Throwable t) {

                        btnSubmit.setEnabled(true);

                        Toast.makeText(
                                AIInterviewActivity.this,
                                "Connection failed",
                                Toast.LENGTH_SHORT).show();

                    }
                });

    }


}