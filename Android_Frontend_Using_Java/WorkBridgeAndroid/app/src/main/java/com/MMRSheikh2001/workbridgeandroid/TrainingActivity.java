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

import com.MMRSheikh2001.workbridgeandroid.adapter.TrainingAdapter;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.TrainingRepository;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.TrainingResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.LoginResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainingActivity extends AppCompatActivity {



    private MaterialToolbar toolbar;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayout layoutEmpty;
    private FloatingActionButton fabAddTraining;
    private TextView tvTrainingCount;

    private TrainingRepository repository;
    private SessionManager sessionManager;
    private TrainingAdapter adapter;

    private final List<TrainingResponseDTO> trainingList = new ArrayList<>();

    private Long profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);



        init();

        LoginResponseDTO user = sessionManager.getUser();

        if (user == null) {
            finish();
            return;
        }

        profileId = user.getProfileId();

        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TrainingAdapter(
                this,
                trainingList,
                training -> {

                    Intent intent =
                            new Intent(
                                    TrainingActivity.this,
                                    EditTrainingActivity.class);

                    intent.putExtra(
                            "trainingId",
                            training.getId());

                    startActivity(intent);

                });

        recyclerView.setAdapter(adapter);

        fabAddTraining.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            TrainingActivity.this,
                            EditTrainingActivity.class);

            startActivity(intent);

        });

        loadTrainings();


    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTrainings();
    }

    private void init() {

        repository = new TrainingRepository(this);
        sessionManager = new SessionManager(this);

        toolbar = findViewById(R.id.toolbar);

        recyclerView = findViewById(R.id.rvTraining);
        progressBar = findViewById(R.id.progressBar);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        fabAddTraining = findViewById(R.id.fabAddTraining);
        tvTrainingCount = findViewById(R.id.tvTrainingCount);
    }

    private void loadTrainings() {

        progressBar.setVisibility(View.VISIBLE);

        repository.getTrainingsByUserProfileId(
                profileId,
                new Callback<List<TrainingResponseDTO>>() {

                    @Override
                    public void onResponse(
                            Call<List<TrainingResponseDTO>> call,
                            Response<List<TrainingResponseDTO>> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()
                                && response.body() != null) {

                            trainingList.clear();
                            trainingList.addAll(response.body());

                            adapter.notifyDataSetChanged();

                            tvTrainingCount.setText(
                                    trainingList.size() + " Trainings");

                            if (trainingList.isEmpty()) {

                                layoutEmpty.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);

                            } else {

                                layoutEmpty.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);

                            }

                        } else {

                            Toast.makeText(
                                    TrainingActivity.this,
                                    "Failed to load trainings",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onFailure(
                            Call<List<TrainingResponseDTO>> call,
                            Throwable t) {

                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(
                                TrainingActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_SHORT).show();

                    }

                });

    }






}