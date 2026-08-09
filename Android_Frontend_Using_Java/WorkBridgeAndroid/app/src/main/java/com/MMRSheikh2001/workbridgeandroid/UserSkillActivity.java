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

import com.MMRSheikh2001.workbridgeandroid.adapter.UserSkillAdapter;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.UserSkillRepository;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.UserSkillResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.LoginResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSkillActivity extends AppCompatActivity {


    private MaterialToolbar toolbar;

    private RecyclerView rvUserSkill;
    private ProgressBar progressBar;
    private LinearLayout layoutEmpty;
    private FloatingActionButton fabAddUserSkill;
    private TextView tvSkillCount;

    private UserSkillAdapter adapter;

    private final List<UserSkillResponseDTO> skillList =
            new ArrayList<>();

    private UserSkillRepository repository;
    private SessionManager sessionManager;

    private Long profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_skill);


        init();

        LoginResponseDTO user = sessionManager.getUser();

        if (user == null) {
            finish();
            return;
        }

        profileId = user.getProfileId();

        toolbar.setNavigationOnClickListener(v -> finish());

        loadSkills();

        fabAddUserSkill.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            UserSkillActivity.this,
                            EditUserSkillActivity.class);

            startActivity(intent);

        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        loadSkills();
    }

    private void init() {

        repository = new UserSkillRepository(this);
        sessionManager = new SessionManager(this);

        toolbar = findViewById(R.id.toolbar);

        rvUserSkill = findViewById(R.id.rvUserSkill);
        progressBar = findViewById(R.id.progressBar);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        fabAddUserSkill = findViewById(R.id.fabAddUserSkill);
        tvSkillCount = findViewById(R.id.tvSkillCount);

        adapter = new UserSkillAdapter(
                this,
                skillList,
                userSkill -> {

                    Intent intent =
                            new Intent(
                                    UserSkillActivity.this,
                                    EditUserSkillActivity.class);

                    intent.putExtra(
                            "userSkillId",
                            userSkill.getId());

                    startActivity(intent);

                });

        rvUserSkill.setLayoutManager(
                new LinearLayoutManager(this));

        rvUserSkill.setAdapter(adapter);

    }

    private void loadSkills() {

        progressBar.setVisibility(View.VISIBLE);

        repository.getUserSkillsByUserProfileId(
                profileId,
                new Callback<List<UserSkillResponseDTO>>() {

                    @Override
                    public void onResponse(
                            Call<List<UserSkillResponseDTO>> call,
                            Response<List<UserSkillResponseDTO>> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()
                                && response.body() != null) {

                            skillList.clear();
                            skillList.addAll(response.body());

                            adapter.notifyDataSetChanged();

                            tvSkillCount.setText(
                                    String.valueOf(skillList.size()));

                            if (skillList.isEmpty()) {

                                layoutEmpty.setVisibility(View.VISIBLE);
                                rvUserSkill.setVisibility(View.GONE);

                            } else {

                                layoutEmpty.setVisibility(View.GONE);
                                rvUserSkill.setVisibility(View.VISIBLE);

                            }

                        } else {

                            Toast.makeText(
                                    UserSkillActivity.this,
                                    "Failed to load skills",
                                    Toast.LENGTH_SHORT
                            ).show();

                        }

                    }

                    @Override
                    public void onFailure(
                            Call<List<UserSkillResponseDTO>> call,
                            Throwable t) {

                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(
                                UserSkillActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();

                    }

                });

    }
}