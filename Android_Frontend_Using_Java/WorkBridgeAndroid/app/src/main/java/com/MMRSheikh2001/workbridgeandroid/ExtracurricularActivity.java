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

import com.MMRSheikh2001.workbridgeandroid.adapter.ExtracurricularAdapter;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.ExtracurricularRepository;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ExtracurricularResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.LoginResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExtracurricularActivity extends AppCompatActivity {



    private MaterialToolbar toolbar;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayout layoutEmpty;
    private TextView tvCount;
    private FloatingActionButton fabAdd;

    private ExtracurricularAdapter adapter;

    private final List<ExtracurricularResponseDTO> extracurricularList =
            new ArrayList<>();

    private ExtracurricularRepository repository;
    private SessionManager sessionManager;

    private Long profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extracurricular);

        init();

        LoginResponseDTO user = sessionManager.getUser();

        if (user == null) {
            finish();
            return;
        }

        profileId = user.getProfileId();

        toolbar.setNavigationOnClickListener(v -> finish());

        loadExtracurriculars();

        fabAdd.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ExtracurricularActivity.this,
                    EditExtracurricularActivity.class);

            startActivity(intent);

        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        loadExtracurriculars();
    }

    private void init() {

        repository = new ExtracurricularRepository(this);
        sessionManager = new SessionManager(this);

        toolbar = findViewById(R.id.toolbar);

        recyclerView = findViewById(R.id.rvExtracurricular);
        progressBar = findViewById(R.id.progressBar);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        tvCount = findViewById(R.id.tvCount);
        fabAdd = findViewById(R.id.fabAddExtracurricular);

        adapter = new ExtracurricularAdapter(
                this,
                extracurricularList,
                extracurricular -> {

                    Intent intent = new Intent(
                            ExtracurricularActivity.this,
                            EditExtracurricularActivity.class);

                    intent.putExtra(
                            "extracurricularId",
                            extracurricular.getId());

                    startActivity(intent);

                });

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);

    }

    private void loadExtracurriculars() {

        progressBar.setVisibility(View.VISIBLE);

        repository.getExtracurricularsByUserProfileId(
                profileId,
                new Callback<List<ExtracurricularResponseDTO>>() {

                    @Override
                    public void onResponse(
                            Call<List<ExtracurricularResponseDTO>> call,
                            Response<List<ExtracurricularResponseDTO>> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()
                                && response.body() != null) {

                            extracurricularList.clear();

                            extracurricularList.addAll(response.body());

                            adapter.notifyDataSetChanged();

                            tvCount.setText(
                                    "Total Activities : "
                                            + extracurricularList.size());

                            if (extracurricularList.isEmpty()) {

                                layoutEmpty.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);

                            } else {

                                layoutEmpty.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);

                            }

                        } else {

                            Toast.makeText(
                                    ExtracurricularActivity.this,
                                    "Failed to load extracurricular activities",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onFailure(
                            Call<List<ExtracurricularResponseDTO>> call,
                            Throwable t) {

                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(
                                ExtracurricularActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_SHORT).show();

                    }
                });

    }





}