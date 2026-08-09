package com.MMRSheikh2001.workbridgeandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.MMRSheikh2001.workbridgeandroid.adapter.ExperienceAdapter;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.ExperienceRepository;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ExperienceResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.LoginResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExperienceActivity extends AppCompatActivity {




    private MaterialToolbar toolbar;
    private RecyclerView rvExperience;
    private ProgressBar progressBar;
    private LinearLayout layoutEmpty;
    private FloatingActionButton fabAddExperience;
    private TextView tvExperienceCount;

    private ExperienceAdapter adapter;

    private final List<ExperienceResponseDTO> experienceList = new ArrayList<>();

    private ExperienceRepository repository;
    private SessionManager sessionManager;

    private Long profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience);


        init();

        LoginResponseDTO user = sessionManager.getUser();

        if (user == null) {
            finish();
            return;
        }

        profileId = user.getProfileId();

        toolbar.setNavigationOnClickListener(v -> finish());

        fabAddExperience.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ExperienceActivity.this,
                    EditExperienceActivity.class);

            startActivity(intent);

        });

        loadExperiences();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadExperiences();
    }

    private void init() {

        repository = new ExperienceRepository(this);
        sessionManager = new SessionManager(this);

        toolbar = findViewById(R.id.toolbar);
        rvExperience = findViewById(R.id.rvExperience);
        progressBar = findViewById(R.id.progressBar);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        fabAddExperience = findViewById(R.id.fabAddExperience);
        tvExperienceCount = findViewById(R.id.tvExperienceCount);

        adapter = new ExperienceAdapter(
                this,
                experienceList,
                experience -> {

                    Intent intent = new Intent(
                            ExperienceActivity.this,
                            EditExperienceActivity.class);

                    intent.putExtra(
                            "experienceId",
                            experience.getId());

                    startActivity(intent);

                });

        rvExperience.setLayoutManager(
                new LinearLayoutManager(this));

        rvExperience.setAdapter(adapter);

    }

    private void loadExperiences() {

        progressBar.setVisibility(View.VISIBLE);

        repository.getExperiencesByUserProfileId(
                profileId,
                new Callback<List<ExperienceResponseDTO>>() {

                    @Override
                    public void onResponse(
                            Call<List<ExperienceResponseDTO>> call,
                            Response<List<ExperienceResponseDTO>> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()
                                && response.body() != null) {

                            experienceList.clear();
                            experienceList.addAll(response.body());

                            adapter.notifyDataSetChanged();

                            tvExperienceCount.setText(
                                    String.valueOf(experienceList.size()));

                            if (experienceList.isEmpty()) {

                                layoutEmpty.setVisibility(View.VISIBLE);
                                rvExperience.setVisibility(View.GONE);

                            } else {

                                layoutEmpty.setVisibility(View.GONE);
                                rvExperience.setVisibility(View.VISIBLE);

                            }

                        } else {

                            Toast.makeText(
                                    ExperienceActivity.this,
                                    "Failed to load experiences",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onFailure(
                            Call<List<ExperienceResponseDTO>> call,
                            Throwable t) {

                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(
                                ExperienceActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_SHORT).show();

                    }

                });

    }





}