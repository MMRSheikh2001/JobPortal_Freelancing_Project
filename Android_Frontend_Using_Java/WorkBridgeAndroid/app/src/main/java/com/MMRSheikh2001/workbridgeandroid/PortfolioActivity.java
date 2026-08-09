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

import com.MMRSheikh2001.workbridgeandroid.adapter.PortfolioAdapter;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.PortfolioRepository;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.PortfolioResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.LoginResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PortfolioActivity extends AppCompatActivity {



    private MaterialToolbar toolbar;

    private RecyclerView rvPortfolio;
    private ProgressBar progressBar;
    private LinearLayout layoutEmpty;
    private TextView tvPortfolioCount;
    private FloatingActionButton fabAddPortfolio;

    private PortfolioAdapter adapter;

    private final List<PortfolioResponseDTO> portfolioList =
            new ArrayList<>();

    private PortfolioRepository repository;
    private SessionManager sessionManager;

    private Long profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_portfolio);



        init();

        LoginResponseDTO user = sessionManager.getUser();

        if (user == null) {
            finish();
            return;
        }

        profileId = user.getProfileId();

        toolbar.setNavigationOnClickListener(v -> finish());

        fabAddPortfolio.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            PortfolioActivity.this,
                            EditPortfolioActivity.class);

            startActivity(intent);

        });


    }



    @Override
    protected void onResume() {
        super.onResume();
        loadPortfolios();
    }

    private void init() {

        repository = new PortfolioRepository(this);
        sessionManager = new SessionManager(this);

        toolbar = findViewById(R.id.toolbar);

        rvPortfolio = findViewById(R.id.rvPortfolio);
        progressBar = findViewById(R.id.progressBar);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        tvPortfolioCount = findViewById(R.id.tvPortfolioCount);
        fabAddPortfolio = findViewById(R.id.fabAddPortfolio);

        adapter = new PortfolioAdapter(
                this,
                portfolioList,
                portfolio -> {

                    Intent intent =
                            new Intent(
                                    PortfolioActivity.this,
                                    EditPortfolioActivity.class);

                    intent.putExtra(
                            "portfolioId",
                            portfolio.getId());

                    startActivity(intent);

                });

        rvPortfolio.setLayoutManager(
                new LinearLayoutManager(this));

        rvPortfolio.setAdapter(adapter);

    }

    private void loadPortfolios() {

        progressBar.setVisibility(View.VISIBLE);

        repository.getPortfoliosByUserProfileId(
                profileId,
                new Callback<List<PortfolioResponseDTO>>() {

                    @Override
                    public void onResponse(
                            Call<List<PortfolioResponseDTO>> call,
                            Response<List<PortfolioResponseDTO>> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()
                                && response.body() != null) {

                            portfolioList.clear();

                            portfolioList.addAll(
                                    response.body());

                            adapter.notifyDataSetChanged();

                            tvPortfolioCount.setText(
                                    "Total Projects : "
                                            + portfolioList.size());

                            if (portfolioList.isEmpty()) {

                                layoutEmpty.setVisibility(
                                        View.VISIBLE);

                                rvPortfolio.setVisibility(
                                        View.GONE);

                            } else {

                                layoutEmpty.setVisibility(
                                        View.GONE);

                                rvPortfolio.setVisibility(
                                        View.VISIBLE);

                            }

                        } else {

                            Toast.makeText(
                                            PortfolioActivity.this,
                                            "Failed to load portfolio",
                                            Toast.LENGTH_SHORT)
                                    .show();

                        }

                    }

                    @Override
                    public void onFailure(
                            Call<List<PortfolioResponseDTO>> call,
                            Throwable t) {

                        progressBar.setVisibility(
                                View.GONE);

                        Toast.makeText(
                                        PortfolioActivity.this,
                                        t.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show();

                    }

                });

    }


}