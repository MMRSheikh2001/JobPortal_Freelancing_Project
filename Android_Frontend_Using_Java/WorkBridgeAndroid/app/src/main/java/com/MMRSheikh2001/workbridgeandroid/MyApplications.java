package com.MMRSheikh2001.workbridgeandroid;

import android.os.Bundle;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.MMRSheikh2001.workbridgeandroid.adapter.ApplicationAdapter;
import com.MMRSheikh2001.workbridgeandroid.repository.JobApplicationRepository;
import com.MMRSheikh2001.workbridgeandroid.response.JobApplicationResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.LoginResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyApplications extends AppCompatActivity {


    private RecyclerView rvApplications;

    private ApplicationAdapter applicationAdapter;

    private List<JobApplicationResponseDTO> applicationList;

    private JobApplicationRepository jobApplicationRepository;

    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_applications);

        init();

        loadApplications();

    }

    private void init() {

        rvApplications = findViewById(R.id.rvApplications);

        applicationList = new ArrayList<>();

        applicationAdapter = new ApplicationAdapter(
                this,
                applicationList);

        rvApplications.setLayoutManager(
                new LinearLayoutManager(this));

        rvApplications.setAdapter(applicationAdapter);

        jobApplicationRepository =
                new JobApplicationRepository(this);

        sessionManager = new SessionManager(this);
    }

    private void loadApplications() {

        LoginResponseDTO user = sessionManager.getUser();
        if (user == null || user.getProfileId() == null) {
            Toast.makeText(this, "No session found. Please log in again.", Toast.LENGTH_LONG).show();
            return;
        }
        Long userProfileId = user.getProfileId();

        jobApplicationRepository.getApplicationsByUserProfileId(
                userProfileId,
                new Callback<List<JobApplicationResponseDTO>>() {

                    @Override
                    public void onResponse(
                            Call<List<JobApplicationResponseDTO>> call,
                            Response<List<JobApplicationResponseDTO>> response) {

                        if (!response.isSuccessful()
                                || response.body() == null) {

                            Toast.makeText(
                                    MyApplications.this,
                                    "Failed to load applications",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        applicationList.clear();

                        applicationList.addAll(response.body());

                        applicationAdapter.notifyDataSetChanged();

                        if (applicationList.isEmpty()) {

                            Toast.makeText(
                                    MyApplications.this,
                                    "No applications found",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<List<JobApplicationResponseDTO>> call,
                            Throwable t) {

                        Toast.makeText(
                                MyApplications.this,
                                "Unable to connect to server",
                                Toast.LENGTH_SHORT
                        ).show();

                        t.printStackTrace();
                    }
                });
    }


}