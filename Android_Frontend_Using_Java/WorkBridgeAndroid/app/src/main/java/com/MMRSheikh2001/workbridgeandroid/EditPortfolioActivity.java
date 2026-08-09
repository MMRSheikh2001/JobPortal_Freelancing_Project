package com.MMRSheikh2001.workbridgeandroid;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.PortfolioRepository;
import com.MMRSheikh2001.workbridgeandroid.response.LoginResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditPortfolioActivity extends AppCompatActivity {




    private MaterialToolbar toolbar;

    private TextInputEditText etTitle;
    private TextInputEditText etDescription;
    private TextInputEditText etProjectUrl;
    private TextInputEditText etTechnologies;

    private TextView tvSelectedFile;

    private MaterialButton btnChooseFile;
    private MaterialButton btnSave;
    private MaterialButton btnCancel;

    private ProgressBar progressBar;

    private PortfolioRepository repository;
    private SessionManager sessionManager;

    private Long profileId;
    private Long portfolioId;

    private Uri selectedFileUri;

    private final Gson gson = new Gson();

    private ActivityResultLauncher<String> filePickerLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_portfolio);

        init();

        LoginResponseDTO user = sessionManager.getUser();

        if (user == null) {
            finish();
            return;
        }

        profileId = user.getProfileId();

        portfolioId = getIntent().getLongExtra(
                "portfolioId",
                -1);

        toolbar.setNavigationOnClickListener(v -> finish());

        btnCancel.setOnClickListener(v -> finish());

        if (portfolioId == -1) {
            toolbar.setTitle("Add Portfolio");
        } else {
            toolbar.setTitle("Edit Portfolio");
            loadPortfolio();
        }

        filePickerLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.GetContent(),
                        uri -> {

                            if (uri != null) {

                                selectedFileUri = uri;

                                tvSelectedFile.setText(
                                        getFileName(uri));

                            }

                        });

        btnChooseFile.setOnClickListener(v -> chooseFile());

        btnSave.setOnClickListener(v -> savePortfolio());

    }



    private void init() {

        repository = new PortfolioRepository(this);
        sessionManager = new SessionManager(this);

        toolbar = findViewById(R.id.toolbar);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etProjectUrl = findViewById(R.id.etProjectUrl);
        etTechnologies = findViewById(R.id.etTechnologies);

        tvSelectedFile = findViewById(R.id.tvSelectedFile);

        btnChooseFile = findViewById(R.id.btnChooseFile);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        progressBar = findViewById(R.id.progressBar);

    }

    private void loadPortfolio() {

        progressBar.setVisibility(View.VISIBLE);

        repository.getPortfolioById(
                portfolioId,
                new retrofit2.Callback<com.MMRSheikh2001.workbridgeandroid.cvinformations.response.PortfolioResponseDTO>() {

                    @Override
                    public void onResponse(
                            retrofit2.Call<com.MMRSheikh2001.workbridgeandroid.cvinformations.response.PortfolioResponseDTO> call,
                            retrofit2.Response<com.MMRSheikh2001.workbridgeandroid.cvinformations.response.PortfolioResponseDTO> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()
                                && response.body() != null) {

                            com.MMRSheikh2001.workbridgeandroid.cvinformations.response.PortfolioResponseDTO portfolio =
                                    response.body();

                            etTitle.setText(portfolio.getTitle());

                            etDescription.setText(
                                    portfolio.getDescription());

                            etProjectUrl.setText(
                                    portfolio.getProjectUrl());

                            etTechnologies.setText(
                                    portfolio.getTechnologies());

                            if (portfolio.getFileUrl() != null
                                    && !portfolio.getFileUrl().isEmpty()) {

                                tvSelectedFile.setText(
                                        "Current File Available");

                            } else {

                                tvSelectedFile.setText(
                                        "No file selected");

                            }

                        } else {

                            Toast.makeText(
                                            EditPortfolioActivity.this,
                                            "Failed to load portfolio",
                                            Toast.LENGTH_SHORT)
                                    .show();

                            finish();

                        }

                    }

                    @Override
                    public void onFailure(
                            retrofit2.Call<com.MMRSheikh2001.workbridgeandroid.cvinformations.response.PortfolioResponseDTO> call,
                            Throwable t) {

                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(
                                        EditPortfolioActivity.this,
                                        t.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show();

                    }

                });

    }

    private void chooseFile() {

        filePickerLauncher.launch("*/*");

    }




    private void savePortfolio() {

        String title = etTitle.getText().toString().trim();

        if (title.isEmpty()) {
            etTitle.setError("Required");
            etTitle.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        com.MMRSheikh2001.workbridgeandroid.cvinformations.request.PortfolioRequestDTO
                request =
                new com.MMRSheikh2001.workbridgeandroid.cvinformations.request.PortfolioRequestDTO();

        request.setTitle(title);

        request.setDescription(
                etDescription.getText().toString().trim());

        request.setProjectUrl(
                etProjectUrl.getText().toString().trim());

        request.setTechnologies(
                etTechnologies.getText().toString().trim());

        request.setUserProfileId(profileId);

        okhttp3.RequestBody requestBody =
                okhttp3.RequestBody.create(
                        gson.toJson(request),
                        okhttp3.MediaType.parse("application/json"));

        okhttp3.MultipartBody.Part filePart = null;

        if (selectedFileUri != null) {
            filePart = createFilePart(selectedFileUri);
        }

        retrofit2.Callback<
                com.MMRSheikh2001.workbridgeandroid.cvinformations.response.PortfolioResponseDTO>
                callback =
                new retrofit2.Callback<
                        com.MMRSheikh2001.workbridgeandroid.cvinformations.response.PortfolioResponseDTO>() {

                    @Override
                    public void onResponse(
                            retrofit2.Call<com.MMRSheikh2001.workbridgeandroid.cvinformations.response.PortfolioResponseDTO> call,
                            retrofit2.Response<com.MMRSheikh2001.workbridgeandroid.cvinformations.response.PortfolioResponseDTO> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()) {

                            Toast.makeText(
                                            EditPortfolioActivity.this,
                                            portfolioId == -1
                                                    ? "Portfolio Saved"
                                                    : "Portfolio Updated",
                                            Toast.LENGTH_SHORT)
                                    .show();

                            finish();

                        } else {

                            Toast.makeText(
                                            EditPortfolioActivity.this,
                                            "Operation Failed",
                                            Toast.LENGTH_SHORT)
                                    .show();

                        }

                    }

                    @Override
                    public void onFailure(
                            retrofit2.Call<com.MMRSheikh2001.workbridgeandroid.cvinformations.response.PortfolioResponseDTO> call,
                            Throwable t) {

                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(
                                        EditPortfolioActivity.this,
                                        t.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show();

                    }

                };

        if (portfolioId == -1) {

            repository.savePortfolio(
                    requestBody,
                    filePart,
                    callback);

        } else {

            repository.updatePortfolio(
                    portfolioId,
                    requestBody,
                    filePart,
                    callback);

        }

    }


    private MultipartBody.Part createFilePart(Uri uri) {

        try {

            InputStream inputStream =
                    getContentResolver().openInputStream(uri);

            String fileName = getFileName(uri);

            File tempFile =
                    new File(getCacheDir(), fileName);

            FileOutputStream outputStream =
                    new FileOutputStream(tempFile);

            byte[] buffer = new byte[4096];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            RequestBody requestFile =
                    RequestBody.create(
                            tempFile,
                            MediaType.parse("*/*"));

            return MultipartBody.Part.createFormData(
                    "file",
                    tempFile.getName(),
                    requestFile);

        } catch (IOException e) {

            e.printStackTrace();
            return null;

        }

    }

    private String getFileName(Uri uri) {

        String result = null;

        if ("content".equals(uri.getScheme())) {

            Cursor cursor =
                    getContentResolver().query(
                            uri,
                            null,
                            null,
                            null,
                            null);

            if (cursor != null) {

                if (cursor.moveToFirst()) {

                    int index =
                            cursor.getColumnIndex(
                                    OpenableColumns.DISPLAY_NAME);

                    if (index >= 0) {
                        result = cursor.getString(index);
                    }

                }

                cursor.close();

            }

        }

        if (result == null) {

            result = uri.getPath();

            if (result != null) {

                int cut = result.lastIndexOf('/');

                if (cut != -1) {
                    result = result.substring(cut + 1);
                }

            }

        }

        return result;

    }




}