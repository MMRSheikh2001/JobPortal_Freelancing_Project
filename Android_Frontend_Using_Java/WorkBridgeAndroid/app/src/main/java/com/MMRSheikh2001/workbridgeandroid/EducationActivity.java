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

import com.MMRSheikh2001.workbridgeandroid.adapter.EducationAdapter;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.EducationRepository;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.EducationResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.LoginResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EducationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private TextView tvEducationCount;

    private FloatingActionButton fabAdd;

    LinearLayout layoutEmpty;

    private EducationAdapter adapter;

    private final List<EducationResponseDTO> educationList = new ArrayList<>();

    private EducationRepository repository;
    private SessionManager sessionManager;

    private Long profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);

        init();

        LoginResponseDTO user = sessionManager.getUser();

        if (user == null) {
            finish();
            return;
        }

        profileId = user.getProfileId();

        loadEducations();
        loadEducationCount();

        fabAdd.setOnClickListener(v -> {

            Intent intent =
                    new Intent(EducationActivity.this,
                            EducationEditActivity.class);

            startActivity(intent);

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEducations();
    }

    private void init() {

        repository = new EducationRepository(this);
        sessionManager = new SessionManager(this);

        recyclerView = findViewById(R.id.rvEducation);
        progressBar = findViewById(R.id.progressBar);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        fabAdd = findViewById(R.id.fabAddEducation);
        tvEducationCount=findViewById(R.id.tvEducationCount);

        adapter = new EducationAdapter(
                this,
                educationList,
                new EducationAdapter.OnEducationClickListener() {

                    @Override
                    public void onEdit(EducationResponseDTO education) {

                        Intent intent =
                                new Intent(EducationActivity.this,
                                        EducationEditActivity.class);

                        intent.putExtra("educationId", education.getId());

                        startActivity(intent);
                    }

                    @Override
                    public void onDelete() {

                        refreshList();

                    }
                });

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);

    }

    private void loadEducations() {

        progressBar.setVisibility(View.VISIBLE);

        repository.getEducationsByUserProfileId(
                profileId,
                new Callback<List<EducationResponseDTO>>() {

                    @Override
                    public void onResponse(
                            Call<List<EducationResponseDTO>> call,
                            Response<List<EducationResponseDTO>> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()
                                && response.body() != null) {

                            educationList.clear();

                            educationList.addAll(response.body());

                            adapter.notifyDataSetChanged();
                            loadEducationCount();

                            if (educationList.isEmpty()) {
                                layoutEmpty.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                layoutEmpty.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }

                        } else {

                            Toast.makeText(
                                    EducationActivity.this,
                                    "Failed to load education",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onFailure(
                            Call<List<EducationResponseDTO>> call,
                            Throwable t) {

                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(
                                EducationActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void loadEducationCount() {

        repository.countEducationsByUserProfileId(
                profileId,
                new Callback<Long>() {

                    @Override
                    public void onResponse(Call<Long> call,
                                           Response<Long> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            tvEducationCount.setText("Total Education Number :"+
                                    String.valueOf(response.body()));

                        }

                    }

                    @Override
                    public void onFailure(Call<Long> call,
                                          Throwable t) {

                    }
                });

    }



    private void refreshList() {

        loadEducations();

    }

}