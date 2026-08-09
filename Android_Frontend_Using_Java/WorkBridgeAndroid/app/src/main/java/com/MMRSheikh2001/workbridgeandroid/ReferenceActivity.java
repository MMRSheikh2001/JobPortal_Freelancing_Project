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

import com.MMRSheikh2001.workbridgeandroid.adapter.ReferenceAdapter;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.ReferenceRepository;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ReferenceResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.LoginResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReferenceActivity extends AppCompatActivity {



    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayout layoutEmpty;
    private FloatingActionButton fabAdd;
    private TextView tvReferenceCount;

    private ReferenceAdapter adapter;
    private final List<ReferenceResponseDTO> referenceList = new ArrayList<>();

    private ReferenceRepository repository;
    private SessionManager sessionManager;

    private Long profileId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference);



        init();

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        LoginResponseDTO user = sessionManager.getUser();

        if (user == null) {
            finish();
            return;
        }

        profileId = user.getProfileId();

        loadReferences();

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(
                    ReferenceActivity.this,
                    EditReferenceActivity.class);

            startActivity(intent);
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        loadReferences();
    }

    private void init() {

        repository = new ReferenceRepository(this);
        sessionManager = new SessionManager(this);

        recyclerView = findViewById(R.id.rvReference);
        progressBar = findViewById(R.id.progressBar);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        fabAdd = findViewById(R.id.fabAddReference);
        tvReferenceCount = findViewById(R.id.tvReferenceCount);

        adapter = new ReferenceAdapter(
                this,
                referenceList,
                reference -> {

                    Intent intent = new Intent(
                            ReferenceActivity.this,
                            EditReferenceActivity.class);

                    intent.putExtra(
                            "referenceId",
                            reference.getId());

                    startActivity(intent);
                });

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);
    }

    private void loadReferences() {

        progressBar.setVisibility(View.VISIBLE);

        repository.getReferencesByUserProfileId(
                profileId,
                new Callback<List<ReferenceResponseDTO>>() {

                    @Override
                    public void onResponse(
                            Call<List<ReferenceResponseDTO>> call,
                            Response<List<ReferenceResponseDTO>> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()
                                && response.body() != null) {

                            referenceList.clear();
                            referenceList.addAll(response.body());

                            adapter.notifyDataSetChanged();

                            tvReferenceCount.setText(
                                    referenceList.size() + " References");

                            if (referenceList.isEmpty()) {

                                layoutEmpty.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);

                            } else {

                                layoutEmpty.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }

                        } else {

                            Toast.makeText(
                                    ReferenceActivity.this,
                                    "Failed to load references",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<List<ReferenceResponseDTO>> call,
                            Throwable t) {

                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(
                                ReferenceActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }




}