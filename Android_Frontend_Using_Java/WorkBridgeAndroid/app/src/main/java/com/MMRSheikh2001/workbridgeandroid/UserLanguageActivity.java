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

import com.MMRSheikh2001.workbridgeandroid.adapter.UserLanguageAdapter;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.UserLanguageRepository;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.UserLanguageResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.LoginResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLanguageActivity extends AppCompatActivity {



    private MaterialToolbar toolbar;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayout layoutEmpty;
    private FloatingActionButton fabAdd;
    private TextView tvLanguageCount;

    private UserLanguageAdapter adapter;

    private final List<UserLanguageResponseDTO> languageList =
            new ArrayList<>();

    private UserLanguageRepository repository;
    private SessionManager sessionManager;

    private Long profileId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_language);



        init();

        LoginResponseDTO user = sessionManager.getUser();

        if (user == null) {
            finish();
            return;
        }

        profileId = user.getProfileId();

        toolbar.setNavigationOnClickListener(v -> finish());

        loadLanguages();

        fabAdd.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            UserLanguageActivity.this,
                            EditUserLanguageActivity.class);

            startActivity(intent);

        });




    }


    @Override
    protected void onResume() {
        super.onResume();

        loadLanguages();
    }

    private void init() {

        repository = new UserLanguageRepository(this);
        sessionManager = new SessionManager(this);

        toolbar = findViewById(R.id.toolbar);

        recyclerView = findViewById(R.id.rvUserLanguage);
        progressBar = findViewById(R.id.progressBar);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        fabAdd = findViewById(R.id.fabAddUserLanguage);
        tvLanguageCount = findViewById(R.id.tvLanguageCount);

        adapter = new UserLanguageAdapter(
                this,
                languageList,
                userLanguage -> {

                    Intent intent =
                            new Intent(
                                    UserLanguageActivity.this,
                                    EditUserLanguageActivity.class);

                    intent.putExtra(
                            "userLanguageId",
                            userLanguage.getId());

                    startActivity(intent);

                });

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);

    }

    private void loadLanguages() {

        progressBar.setVisibility(View.VISIBLE);

        repository.getUserLanguagesByUserProfileId(
                profileId,
                new Callback<List<UserLanguageResponseDTO>>() {

                    @Override
                    public void onResponse(
                            Call<List<UserLanguageResponseDTO>> call,
                            Response<List<UserLanguageResponseDTO>> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()
                                && response.body() != null) {

                            languageList.clear();

                            languageList.addAll(response.body());

                            adapter.notifyDataSetChanged();

                            tvLanguageCount.setText(
                                    String.valueOf(languageList.size()));

                            if (languageList.isEmpty()) {

                                layoutEmpty.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);

                            } else {

                                layoutEmpty.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);

                            }

                        } else {

                            Toast.makeText(
                                            UserLanguageActivity.this,
                                            "Failed to load languages",
                                            Toast.LENGTH_SHORT)
                                    .show();

                        }

                    }

                    @Override
                    public void onFailure(
                            Call<List<UserLanguageResponseDTO>> call,
                            Throwable t) {

                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(
                                        UserLanguageActivity.this,
                                        t.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show();

                    }

                });

    }







}