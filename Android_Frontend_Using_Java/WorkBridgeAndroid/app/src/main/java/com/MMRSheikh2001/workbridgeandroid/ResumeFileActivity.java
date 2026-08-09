package com.MMRSheikh2001.workbridgeandroid;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.ResumeFileRepository;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ResumeFileResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.LoginResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResumeFileActivity extends AppCompatActivity {



    private ResumeFileResponseDTO currentResume;

    private MaterialToolbar toolbar;

    private TextView tvFileName;
    private TextView tvUploadedAt;

    private MaterialButton btnUploadResume;
    private MaterialButton btnDeleteResume;
    private MaterialButton btnImportResume;

    private MaterialButton btnViewResume;
    private MaterialButton btnDownloadResume;

    private android.widget.ProgressBar progressBar;

    private ResumeFileRepository resumeRepository;
    private SessionManager sessionManager;

    private Long userProfileId;

    private Uri selectedPdfUri;

    private ActivityResultLauncher<String> pdfPickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_file);


        init();

        LoginResponseDTO user = sessionManager.getUser();

        if (user == null) {
            finish();
            return;
        }

        userProfileId = user.getProfileId();

        toolbar.setNavigationOnClickListener(v -> finish());

        registerPdfPicker();

        checkResumeExists();

        btnUploadResume.setOnClickListener(v ->
                pdfPickerLauncher.launch("application/pdf"));

        btnDeleteResume.setOnClickListener(v ->
                confirmDeleteResume());

        btnImportResume.setOnClickListener(v -> {

            Intent intent = new Intent(
                    ResumeFileActivity.this,
                    ResumePreviewActivity.class);

            startActivity(intent);

        });

        btnViewResume.setOnClickListener(v -> viewResume());

        btnDownloadResume.setOnClickListener(v -> downloadResume());

    }



    @Override
    protected void onResume() {
        super.onResume();

        checkResumeExists();
    }

    private void init() {

        resumeRepository =
                new ResumeFileRepository(this);

        sessionManager =
                new SessionManager(this);

        toolbar =
                findViewById(R.id.toolbar);

        tvFileName =
                findViewById(R.id.tvFileName);

        tvUploadedAt =
                findViewById(R.id.tvUploadedAt);

        btnUploadResume =
                findViewById(R.id.btnUploadResume);

        btnDeleteResume =
                findViewById(R.id.btnDeleteResume);

        btnViewResume =
                findViewById(R.id.btnViewResume);

        btnDownloadResume =
                findViewById(R.id.btnDownloadResume);

        btnImportResume =
                findViewById(R.id.btnImportResume);

        progressBar =
                findViewById(R.id.progressBar);

    }


    private void registerPdfPicker() {

        pdfPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {

                    if (uri != null) {

                        selectedPdfUri = uri;

                        uploadResume();

                    }

                });

    }


    private void uploadResume() {

        if (selectedPdfUri == null)
            return;

        progressBar.setVisibility(View.VISIBLE);

        try {

            File file = createTempFileFromUri(selectedPdfUri);

            RequestBody requestFile =
                    RequestBody.create(
                            file,
                            MediaType.parse("application/pdf"));

            MultipartBody.Part multipart =
                    MultipartBody.Part.createFormData(
                            "cv",
                            file.getName(),
                            requestFile);

            resumeRepository.uploadResume(
                    userProfileId,
                    multipart,
                    new Callback<ResumeFileResponseDTO>() {

                        @Override
                        public void onResponse(
                                Call<ResumeFileResponseDTO> call,
                                Response<ResumeFileResponseDTO> response) {

                            progressBar.setVisibility(View.GONE);

                            if (response.isSuccessful()) {

                                Toast.makeText(
                                        ResumeFileActivity.this,
                                        "Resume uploaded successfully",
                                        Toast.LENGTH_SHORT
                                ).show();

                                checkResumeExists();

                            } else {

                                Toast.makeText(
                                        ResumeFileActivity.this,
                                        "Upload failed",
                                        Toast.LENGTH_SHORT
                                ).show();

                            }

                        }

                        @Override
                        public void onFailure(
                                Call<ResumeFileResponseDTO> call,
                                Throwable t) {

                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(
                                    ResumeFileActivity.this,
                                    t.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();

                        }

                    });

        } catch (Exception e) {

            progressBar.setVisibility(View.GONE);

            Toast.makeText(
                    this,
                    e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();

        }

    }


    private File createTempFileFromUri(Uri uri)
            throws IOException {

        InputStream inputStream =
                getContentResolver().openInputStream(uri);

        String fileName = getFileName(uri);

        File file = new File(
                getCacheDir(),
                fileName);

        FileOutputStream outputStream =
                new FileOutputStream(file);

        byte[] buffer = new byte[4096];

        int read;

        while ((read = inputStream.read(buffer)) != -1) {

            outputStream.write(buffer, 0, read);

        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();

        return file;

    }


    private String getFileName(Uri uri) {

        String result = "resume.pdf";

        Cursor cursor =
                getContentResolver().query(
                        uri,
                        null,
                        null,
                        null,
                        null);

        if (cursor != null) {

            if (cursor.moveToFirst()) {

                int index = cursor.getColumnIndex(
                        OpenableColumns.DISPLAY_NAME);

                if (index >= 0) {

                    result = cursor.getString(index);

                }

            }

            cursor.close();

        }

        return result;

    }


    private void checkResumeExists() {

        progressBar.setVisibility(View.VISIBLE);

        resumeRepository.resumeFileExists(
                userProfileId,
                new Callback<Boolean>() {

                    @Override
                    public void onResponse(
                            Call<Boolean> call,
                            Response<Boolean> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()
                                && response.body() != null) {

                            boolean exists = response.body();

                            if (exists) {

                                loadResumeInfo();

                            } else {

                                tvFileName.setText("No resume uploaded");
                                tvUploadedAt.setText("");

                                btnViewResume.setEnabled(false);
                                btnDownloadResume.setEnabled(false);
                                btnDeleteResume.setEnabled(false);
                                btnImportResume.setEnabled(false);

                            }

                        }

                    }

                    @Override
                    public void onFailure(
                            Call<Boolean> call,
                            Throwable t) {

                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(
                                ResumeFileActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();

                    }

                });

    }


    private void loadResumeInfo() {

        progressBar.setVisibility(View.VISIBLE);

        resumeRepository.getResumeFileByUserProfileId(
                userProfileId,
                new Callback<ResumeFileResponseDTO>() {

                    @Override
                    public void onResponse(
                            Call<ResumeFileResponseDTO> call,
                            Response<ResumeFileResponseDTO> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()
                                && response.body() != null) {

                            currentResume = response.body();

                            ResumeFileResponseDTO resume = currentResume;

                            tvFileName.setText(
                                    resume.getFileName());

                            if (resume.getUploadedAt() != null) {

                                tvUploadedAt.setText(
                                        "Uploaded: "
                                                + resume.getUploadedAt());

                            } else {

                                tvUploadedAt.setText("");

                            }

                            btnViewResume.setEnabled(true);
                            btnDownloadResume.setEnabled(true);
                            btnDeleteResume.setEnabled(true);
                            btnImportResume.setEnabled(true);

                        }

                    }

                    @Override
                    public void onFailure(
                            Call<ResumeFileResponseDTO> call,
                            Throwable t) {

                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(
                                ResumeFileActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();

                    }

                });

    }


    private void confirmDeleteResume() {

        new AlertDialog.Builder(this)
                .setTitle("Delete Resume")
                .setMessage("Are you sure you want to delete your uploaded resume?")
                .setPositiveButton("Delete",
                        (dialog, which) -> deleteResume())
                .setNegativeButton("Cancel", null)
                .show();

    }

    private void deleteResume() {

        progressBar.setVisibility(View.VISIBLE);

        resumeRepository.deleteResumeFileByUserProfileId(
                userProfileId,
                new Callback<String>() {

                    @Override
                    public void onResponse(
                            Call<String> call,
                            Response<String> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()) {

                            Toast.makeText(
                                    ResumeFileActivity.this,
                                    "Resume deleted successfully",
                                    Toast.LENGTH_SHORT
                            ).show();

                            // Reset UI
                            tvFileName.setText("No resume uploaded");
                            tvUploadedAt.setText("");

                            btnDeleteResume.setEnabled(false);
                            btnImportResume.setEnabled(false);

                        } else {

                            Toast.makeText(
                                    ResumeFileActivity.this,
                                    "Failed to delete resume",
                                    Toast.LENGTH_SHORT
                            ).show();

                        }

                    }

                    @Override
                    public void onFailure(
                            Call<String> call,
                            Throwable t) {

                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(
                                ResumeFileActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();

                    }

                });

    }



    private void viewResume() {

        if (currentResume == null)
            return;

        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                        ApiClient.getResumeFile(
                                currentResume.getFileName()
                        )
                ));

        startActivity(intent);

    }


    private void downloadResume() {

        if (currentResume == null)
            return;

        DownloadManager.Request request =
                new DownloadManager.Request(
                        Uri.parse(
                                ApiClient.getResumeFile(
                                        currentResume.getFileName()
                                )));

        request.setTitle(currentResume.getFileName());

        request.setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                currentResume.getFileName());

        DownloadManager manager =
                (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        manager.enqueue(request);

    }




}