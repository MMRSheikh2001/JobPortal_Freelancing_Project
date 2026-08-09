package com.MMRSheikh2001.workbridgeandroid;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.ExtracurricularRepository;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.request.ExtracurricularRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ExtracurricularResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.LoginResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditExtracurricularActivity extends AppCompatActivity {



    private MaterialToolbar toolbar;

    private TextInputEditText etTitle;
    private TextInputEditText etOrganization;
    private TextInputEditText etRole;
    private TextInputEditText etDescription;

    private MaterialButton btnSave;
    private MaterialButton btnCancel;

    private ProgressBar progressBar;

    private ExtracurricularRepository repository;
    private SessionManager sessionManager;

    private Long profileId;
    private Long extracurricularId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_extracurricular);

        init();

        LoginResponseDTO user = sessionManager.getUser();

        if (user == null) {
            finish();
            return;
        }

        profileId = user.getProfileId();

        extracurricularId = getIntent().getLongExtra(
                "extracurricularId",
                -1);

        toolbar.setNavigationOnClickListener(v -> finish());

        btnCancel.setOnClickListener(v -> finish());

        if (extracurricularId != -1) {
            toolbar.setTitle("Edit Extracurricular");
            loadExtracurricular();
        } else {
            toolbar.setTitle("Add Extracurricular");
        }

        btnSave.setOnClickListener(v -> saveExtracurricular());

    }



    private void init() {

        repository = new ExtracurricularRepository(this);
        sessionManager = new SessionManager(this);

        toolbar = findViewById(R.id.toolbar);

        etTitle = findViewById(R.id.etTitle);
        etOrganization = findViewById(R.id.etOrganization);
        etRole = findViewById(R.id.etRole);
        etDescription = findViewById(R.id.etDescription);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        progressBar = findViewById(R.id.progressBar);
    }

    private void loadExtracurricular() {

        progressBar.setVisibility(View.VISIBLE);

        repository.getExtracurricularById(
                extracurricularId,
                new Callback<ExtracurricularResponseDTO>() {

                    @Override
                    public void onResponse(
                            Call<ExtracurricularResponseDTO> call,
                            Response<ExtracurricularResponseDTO> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()
                                && response.body() != null) {

                            ExtracurricularResponseDTO item =
                                    response.body();

                            etTitle.setText(item.getTitle());
                            etOrganization.setText(item.getOrganization());
                            etRole.setText(item.getRole());
                            etDescription.setText(item.getDescription());

                        } else {

                            Toast.makeText(
                                    EditExtracurricularActivity.this,
                                    "Failed to load",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onFailure(
                            Call<ExtracurricularResponseDTO> call,
                            Throwable t) {

                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(
                                EditExtracurricularActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void saveExtracurricular() {

        if (TextUtils.isEmpty(etTitle.getText())) {
            etTitle.setError("Required");
            return;
        }

        ExtracurricularRequestDTO request =
                new ExtracurricularRequestDTO();

        request.setTitle(
                etTitle.getText().toString().trim());

        request.setOrganization(
                etOrganization.getText().toString().trim());

        request.setRole(
                etRole.getText().toString().trim());

        request.setDescription(
                etDescription.getText().toString().trim());

        request.setUserProfileId(profileId);

        progressBar.setVisibility(View.VISIBLE);

        Callback<ExtracurricularResponseDTO> callback =
                new Callback<ExtracurricularResponseDTO>() {

                    @Override
                    public void onResponse(
                            Call<ExtracurricularResponseDTO> call,
                            Response<ExtracurricularResponseDTO> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()) {

                            Toast.makeText(
                                    EditExtracurricularActivity.this,
                                    extracurricularId == -1
                                            ? "Saved Successfully"
                                            : "Updated Successfully",
                                    Toast.LENGTH_SHORT).show();

                            finish();

                        } else {

                            Toast.makeText(
                                    EditExtracurricularActivity.this,
                                    "Operation Failed",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onFailure(
                            Call<ExtracurricularResponseDTO> call,
                            Throwable t) {

                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(
                                EditExtracurricularActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_SHORT).show();

                    }
                };

        if (extracurricularId == -1) {

            repository.saveExtracurricular(
                    request,
                    callback);

        } else {

            repository.updateExtracurricular(
                    extracurricularId,
                    request,
                    callback);

        }

    }




}