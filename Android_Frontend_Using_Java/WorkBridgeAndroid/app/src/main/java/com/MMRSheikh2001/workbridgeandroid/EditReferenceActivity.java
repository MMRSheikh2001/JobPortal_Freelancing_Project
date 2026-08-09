package com.MMRSheikh2001.workbridgeandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.ReferenceRepository;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.request.ReferenceRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ReferenceResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.LoginResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditReferenceActivity extends AppCompatActivity {


    private TextInputEditText etName;
    private TextInputEditText etOrganization;
    private TextInputEditText etDesignation;
    private TextInputEditText etPhone;
    private TextInputEditText etEmail;
    private TextInputEditText etAddress;
    private TextInputEditText etRelation;

    private MaterialButton btnSave;
    private MaterialButton btnCancel;

    private ProgressBar progressBar;

    private ReferenceRepository repository;
    private SessionManager sessionManager;

    private Long profileId;
    private Long referenceId;

    private boolean isEditMode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reference);


        init();

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        LoginResponseDTO user = sessionManager.getUser();

        if (user == null) {
            finish();
            return;
        }

        profileId = user.getProfileId();

        if (getIntent().hasExtra("referenceId")) {

            referenceId = getIntent().getLongExtra(
                    "referenceId",
                    -1);

            if (referenceId != -1) {

                isEditMode = true;

                loadReference();
            }
        }

        btnSave.setOnClickListener(v -> {

            if (isEditMode) {
                updateReference();
            } else {
                saveReference();
            }

        });

        btnCancel.setOnClickListener(v -> finish());

    }


    private void init() {

        repository = new ReferenceRepository(this);
        sessionManager = new SessionManager(this);

        etName = findViewById(R.id.etName);
        etOrganization = findViewById(R.id.etOrganization);
        etDesignation = findViewById(R.id.etDesignation);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etRelation = findViewById(R.id.etRelation);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        progressBar = findViewById(R.id.progressBar);
    }


    private void loadReference() {

        progressBar.setVisibility(View.VISIBLE);

        repository.getReferenceById(
                referenceId,
                new Callback<ReferenceResponseDTO>() {

                    @Override
                    public void onResponse(
                            Call<ReferenceResponseDTO> call,
                            Response<ReferenceResponseDTO> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()
                                && response.body() != null) {

                            populateForm(response.body());

                        }

                    }

                    @Override
                    public void onFailure(
                            Call<ReferenceResponseDTO> call,
                            Throwable t) {

                        progressBar.setVisibility(View.GONE);

                    }
                });

    }

    private void populateForm(ReferenceResponseDTO reference) {

        etName.setText(reference.getName());

        etOrganization.setText(reference.getOrganization());

        etDesignation.setText(reference.getDesignation());

        etPhone.setText(reference.getPhone());

        etEmail.setText(reference.getEmail());

        etAddress.setText(reference.getAddress());

        etRelation.setText(reference.getRelation());

    }


    private void saveReference() {

        if (etName.getText().toString().trim().isEmpty()) {
            etName.setError("Required");
            etName.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        repository.saveReference(
                buildRequest(),
                new Callback<ReferenceResponseDTO>() {

                    @Override
                    public void onResponse(
                            Call<ReferenceResponseDTO> call,
                            Response<ReferenceResponseDTO> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()) {

                            setResult(RESULT_OK);
                            finish();

                        }

                    }

                    @Override
                    public void onFailure(
                            Call<ReferenceResponseDTO> call,
                            Throwable t) {

                        progressBar.setVisibility(View.GONE);

                    }
                });

    }

    private void updateReference() {

        if (etName.getText().toString().trim().isEmpty()) {
            etName.setError("Required");
            etName.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        repository.updateReference(
                referenceId,
                buildRequest(),
                new Callback<ReferenceResponseDTO>() {

                    @Override
                    public void onResponse(
                            Call<ReferenceResponseDTO> call,
                            Response<ReferenceResponseDTO> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()) {

                            setResult(RESULT_OK);
                            finish();

                        }

                    }

                    @Override
                    public void onFailure(
                            Call<ReferenceResponseDTO> call,
                            Throwable t) {

                        progressBar.setVisibility(View.GONE);

                    }
                });

    }

    private ReferenceRequestDTO buildRequest() {

        ReferenceRequestDTO request = new ReferenceRequestDTO();

        request.setName(
                etName.getText().toString().trim());

        request.setOrganization(
                etOrganization.getText().toString().trim());

        request.setDesignation(
                etDesignation.getText().toString().trim());

        request.setPhone(
                etPhone.getText().toString().trim());

        request.setEmail(
                etEmail.getText().toString().trim());

        request.setAddress(
                etAddress.getText().toString().trim());

        request.setRelation(
                etRelation.getText().toString().trim());

        request.setUserProfileId(profileId);

        return request;
    }


}