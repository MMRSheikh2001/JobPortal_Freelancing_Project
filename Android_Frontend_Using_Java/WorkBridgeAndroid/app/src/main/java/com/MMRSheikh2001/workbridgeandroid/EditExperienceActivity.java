package com.MMRSheikh2001.workbridgeandroid;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.ExperienceRepository;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.request.ExperienceRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ExperienceResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.enums.EmploymentType;
import com.MMRSheikh2001.workbridgeandroid.response.LoginResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditExperienceActivity extends AppCompatActivity {


    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ISO_LOCAL_DATE;

    private MaterialToolbar toolbar;

    private TextInputEditText etCompanyName;
    private TextInputEditText etPosition;
    private TextInputEditText etResponsibilities;
    private TextInputEditText etAchievements;
    private TextInputEditText etStartDate;
    private TextInputEditText etEndDate;

    private AutoCompleteTextView actEmploymentType;

    private MaterialCheckBox cbCurrentlyWorking;

    private MaterialButton btnSave;
    private MaterialButton btnCancel;

    private ProgressBar progressBar;

    private ExperienceRepository repository;
    private SessionManager sessionManager;

    private Long experienceId;
    private Long profileId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_experience);


        repository = new ExperienceRepository(this);
        sessionManager = new SessionManager(this);

        bindViews();
        setupToolbar();
        setupDropdown();
        setupListeners();

        LoginResponseDTO user = sessionManager.getUser();
        if (user == null || user.getProfileId() == null) {
            finish();
            return;
        }
        profileId = user.getProfileId();

        if (getIntent().hasExtra("experienceId")) {
            experienceId = getIntent().getLongExtra("experienceId", -1);

            if (experienceId != -1) {
                loadExperience();
            }
        }

    }


    private void bindViews() {

        toolbar = findViewById(R.id.toolbar);

        etCompanyName = findViewById(R.id.etCompanyName);
        etPosition = findViewById(R.id.etPosition);
        etResponsibilities = findViewById(R.id.etResponsibilities);
        etAchievements = findViewById(R.id.etAchievements);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);

        actEmploymentType = findViewById(R.id.actEmploymentType);

        cbCurrentlyWorking = findViewById(R.id.cbCurrentlyWorking);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        progressBar = findViewById(R.id.progressBar);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupDropdown() {

        String[] list = new String[EmploymentType.values().length];

        for (int i = 0; i < EmploymentType.values().length; i++) {
            list[i] = EmploymentType.values()[i].name();
        }

        actEmploymentType.setAdapter(
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        list
                ));
    }

    private void setupListeners() {

        etStartDate.setOnClickListener(v -> showDatePicker(etStartDate));

        etEndDate.setOnClickListener(v -> {

            if (!cbCurrentlyWorking.isChecked()) {
                showDatePicker(etEndDate);
            }

        });

        cbCurrentlyWorking.setOnCheckedChangeListener((buttonView, checked) -> {

            etEndDate.setEnabled(!checked);

            if (checked) {
                etEndDate.setText("");
            }

        });

        btnCancel.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> saveExperience());
    }

    private void showDatePicker(TextInputEditText editText) {

        Calendar calendar = Calendar.getInstance();

        new DatePickerDialog(
                this,
                (view, year, month, day) -> {

                    LocalDate date =
                            LocalDate.of(year, month + 1, day);

                    editText.setText(date.format(DATE_FORMAT));

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void loadExperience() {

        progressBar.setVisibility(android.view.View.VISIBLE);

        repository.getExperienceById(
                experienceId,
                new Callback<ExperienceResponseDTO>() {

                    @Override
                    public void onResponse(Call<ExperienceResponseDTO> call,
                                           Response<ExperienceResponseDTO> response) {

                        progressBar.setVisibility(android.view.View.GONE);

                        if (response.isSuccessful()
                                && response.body() != null) {

                            populate(response.body());

                        }

                    }

                    @Override
                    public void onFailure(Call<ExperienceResponseDTO> call,
                                          Throwable t) {

                        progressBar.setVisibility(android.view.View.GONE);

                    }
                });
    }

    private void populate(ExperienceResponseDTO e) {

        etCompanyName.setText(e.getCompanyName());
        etPosition.setText(e.getPosition());
        etResponsibilities.setText(e.getResponsibilities());
        etAchievements.setText(e.getAchievements());

        if (e.getEmploymentType() != null) {
            actEmploymentType.setText(
                    e.getEmploymentType().name(),
                    false);
        }

        if (e.getStartDate() != null) {
            etStartDate.setText(e.getStartDate().toString());
        }

        if (e.getEndDate() != null) {
            etEndDate.setText(e.getEndDate().toString());
        }

        cbCurrentlyWorking.setChecked(
                Boolean.TRUE.equals(e.getCurrentlyWorking()));
    }

    private void saveExperience() {

        if (TextUtils.isEmpty(etCompanyName.getText())) {
            etCompanyName.setError("Required");
            return;
        }

        if (TextUtils.isEmpty(actEmploymentType.getText())) {
            actEmploymentType.setError("Required");
            return;
        }

        ExperienceRequestDTO request =
                new ExperienceRequestDTO();

        request.setCompanyName(
                etCompanyName.getText().toString().trim());

        request.setPosition(
                etPosition.getText().toString().trim());

        request.setResponsibilities(
                etResponsibilities.getText().toString().trim());

        request.setAchievements(
                etAchievements.getText().toString().trim());

        try {
            request.setEmploymentType(
                    EmploymentType.valueOf(
                            actEmploymentType.getText().toString().trim()));
        } catch (IllegalArgumentException e) {
            actEmploymentType.setError("Select a valid employment type");
            return;
        }

        if (!TextUtils.isEmpty(etStartDate.getText())) {
            request.setStartDate(
                    LocalDate.parse(
                            etStartDate.getText().toString()));
        }

        if (!cbCurrentlyWorking.isChecked()
                && !TextUtils.isEmpty(etEndDate.getText())) {

            request.setEndDate(
                    LocalDate.parse(
                            etEndDate.getText().toString()));
        }

        request.setUserProfileId(profileId);

        progressBar.setVisibility(android.view.View.VISIBLE);

        Callback<ExperienceResponseDTO> callback =
                new Callback<ExperienceResponseDTO>() {

                    @Override
                    public void onResponse(
                            Call<ExperienceResponseDTO> call,
                            Response<ExperienceResponseDTO> response) {

                        progressBar.setVisibility(android.view.View.GONE);

                        if (response.isSuccessful()) {
                            finish();
                        }

                    }

                    @Override
                    public void onFailure(
                            Call<ExperienceResponseDTO> call,
                            Throwable t) {

                        progressBar.setVisibility(android.view.View.GONE);

                    }
                };

        if (experienceId == null) {

            repository.saveExperience(request, callback);

        } else {

            repository.updateExperience(
                    experienceId,
                    request,
                    callback);
        }
    }


}