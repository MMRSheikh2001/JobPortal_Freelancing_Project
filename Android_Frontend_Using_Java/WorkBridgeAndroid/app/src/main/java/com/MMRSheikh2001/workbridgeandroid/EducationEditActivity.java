package com.MMRSheikh2001.workbridgeandroid;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.EducationRepository;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.request.EducationRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.EducationResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.enums.EducationLevel;
import com.MMRSheikh2001.workbridgeandroid.enums.ResultType;
import com.MMRSheikh2001.workbridgeandroid.response.LoginResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EducationEditActivity extends AppCompatActivity {


    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ISO_LOCAL_DATE;

    private EducationRepository repository;
    private SessionManager sessionManager;

    private Long profileId;
    private Long educationId;

    // Views
    private AutoCompleteTextView actvEducationLevel;
    private AutoCompleteTextView actvResultType;

    private TextInputEditText etInstitution;
    private TextInputEditText etBoard;
    private TextInputEditText etFieldOfStudy;

    private TextInputEditText etResult;
    private TextInputEditText etOutOf;
    private TextInputEditText etGradeDivision;

    private TextInputEditText etStartDate;
    private TextInputEditText etEndDate;

    private MaterialCheckBox cbCurrentlyStudying;

    private MaterialButton btnSave;
    private MaterialButton btnCancel;

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_education_edit);

        repository = new EducationRepository(this);
        sessionManager = new SessionManager(this);

        LoginResponseDTO user = sessionManager.getUser();

        if (user == null) {
            finish();
            return;
        }

        profileId = user.getProfileId();

        educationId = getIntent().getLongExtra("educationId", -1);

        init();

        loadDropdowns();

        setupListeners();

        if (educationId != -1) {
            loadEducation();
        }
    }


    // =======================================================
// Initialization
// =======================================================

    private void init() {

        actvEducationLevel = findViewById(R.id.actEducationLevel);
        actvResultType = findViewById(R.id.actResultType);

        etInstitution = findViewById(R.id.etInstitution);
        etBoard = findViewById(R.id.etBoard);
        etFieldOfStudy = findViewById(R.id.etFieldOfStudy);

        etResult = findViewById(R.id.etResult);
        etOutOf = findViewById(R.id.etOutOf);
        etGradeDivision = findViewById(R.id.etGradeDivision);

        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);

        cbCurrentlyStudying = findViewById(R.id.cbCurrentlyStudying);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        progressBar = findViewById(R.id.progressBar);

    }


// =======================================================
// Dropdowns
// =======================================================

    private void loadDropdowns() {

        String[] educationLevels =
                new String[EducationLevel.values().length];

        for (int i = 0; i < EducationLevel.values().length; i++) {

            educationLevels[i] =
                    EducationLevel.values()[i].name();

        }

        actvEducationLevel.setAdapter(
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        educationLevels
                )
        );


        String[] resultTypes =
                new String[ResultType.values().length];

        for (int i = 0; i < ResultType.values().length; i++) {

            resultTypes[i] =
                    ResultType.values()[i].name();

        }

        actvResultType.setAdapter(
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        resultTypes
                )
        );

    }


// =======================================================
// Listeners
// =======================================================

    private void setupListeners() {

        btnCancel.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> saveEducation());

        etStartDate.setOnClickListener(v ->
                showStartDatePicker());

        etEndDate.setOnClickListener(v ->
                showEndDatePicker());


        cbCurrentlyStudying.setOnCheckedChangeListener(
                (buttonView, checked) -> {

                    etEndDate.setEnabled(!checked);

                    if (checked) {
                        etEndDate.setText("");
                    }

                });


        actvResultType.setOnItemClickListener(
                (parent, view, position, id) -> {

                    ResultType type =
                            ResultType.valueOf(
                                    actvResultType.getText().toString());

                    LinearLayout layoutCgpa =
                            findViewById(R.id.layoutCgpa);

                    LinearLayout layoutGradeDivision =
                            findViewById(R.id.layoutGradeDivision);

                    if (type == ResultType.CGPA
                            || type == ResultType.GPA
                            || type == ResultType.PERCENTAGE) {

                        layoutCgpa.setVisibility(View.VISIBLE);
                        layoutGradeDivision.setVisibility(View.GONE);

                    } else {

                        layoutCgpa.setVisibility(View.GONE);
                        layoutGradeDivision.setVisibility(View.VISIBLE);

                    }

                });

    }

    // =======================================================
// Date Pickers
// =======================================================

    private void showStartDatePicker() {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {

                    LocalDate date =
                            LocalDate.of(year, month + 1, dayOfMonth);

                    etStartDate.setText(date.toString());

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        dialog.show();

    }

    private void showEndDatePicker() {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {

                    LocalDate date =
                            LocalDate.of(year, month + 1, dayOfMonth);

                    etEndDate.setText(date.toString());

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        dialog.show();

    }


// =======================================================
// Load Existing Education
// =======================================================

    private void loadEducation() {

        progressBar.setVisibility(View.VISIBLE);

        repository.getEducationById(
                educationId,
                new Callback<EducationResponseDTO>() {

                    @Override
                    public void onResponse(
                            Call<EducationResponseDTO> call,
                            Response<EducationResponseDTO> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()
                                && response.body() != null) {

                            populateForm(response.body());

                        } else {

                            Toast.makeText(
                                    EducationEditActivity.this,
                                    "Failed to load education",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onFailure(
                            Call<EducationResponseDTO> call,
                            Throwable t) {

                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(
                                EducationEditActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_SHORT).show();

                    }

                });

    }


// =======================================================
// Populate Form
// =======================================================

    private void populateForm(EducationResponseDTO education) {

        if (education.getEducationLevel() != null) {

            actvEducationLevel.setText(
                    education.getEducationLevel().name(),
                    false);

        }

        etInstitution.setText(education.getInstitution());
        etBoard.setText(education.getBoard());
        etFieldOfStudy.setText(education.getFieldOfStudy());

        if (education.getResultType() != null) {

            actvResultType.setText(
                    education.getResultType().name(),
                    false);

            LinearLayout layoutCgpa =
                    findViewById(R.id.layoutCgpa);

            LinearLayout layoutGradeDivision =
                    findViewById(R.id.layoutGradeDivision);

            if (education.getResultType() == ResultType.CGPA
                    || education.getResultType() == ResultType.GPA
                    || education.getResultType() == ResultType.PERCENTAGE) {

                layoutCgpa.setVisibility(View.VISIBLE);
                layoutGradeDivision.setVisibility(View.GONE);

                if (education.getResult() != null) {
                    etResult.setText(String.valueOf(education.getResult()));
                }

                if (education.getOutOf() != null) {
                    etOutOf.setText(String.valueOf(education.getOutOf()));
                }

            } else {

                layoutCgpa.setVisibility(View.GONE);
                layoutGradeDivision.setVisibility(View.VISIBLE);

                etGradeDivision.setText(
                        education.getGradeOrDivision());

            }

        }

        if (education.getStartDate() != null) {

            etStartDate.setText(
                    education.getStartDate().toString());

        }

        if (Boolean.TRUE.equals(
                education.getCurrentlyStudying())) {

            cbCurrentlyStudying.setChecked(true);

            etEndDate.setText("");

            etEndDate.setEnabled(false);

        } else {

            cbCurrentlyStudying.setChecked(false);

            etEndDate.setEnabled(true);

            if (education.getEndDate() != null) {

                etEndDate.setText(
                        education.getEndDate().toString());

            }

        }

    }
    // =======================================================
// Save / Update
// =======================================================

    private void saveEducation() {

        if (text(actvEducationLevel).isEmpty()) {
            actvEducationLevel.setError("Required");
            return;
        }

        if (text(etInstitution).isEmpty()) {
            etInstitution.setError("Required");
            return;
        }

        EducationRequestDTO request = new EducationRequestDTO();

        request.setUserProfileId(profileId);

        request.setEducationLevel(
                EducationLevel.valueOf(
                        text(actvEducationLevel)));

        request.setInstitution(text(etInstitution));
        request.setBoard(text(etBoard));
        request.setFieldOfStudy(text(etFieldOfStudy));

        if (text(actvResultType).isEmpty()) {
            actvResultType.setError("Required");
            return;
        }

        request.setResultType(
                ResultType.valueOf(
                        text(actvResultType)));

        if (request.getResultType() == ResultType.CGPA
                || request.getResultType() == ResultType.GPA
                || request.getResultType() == ResultType.PERCENTAGE) {

            if (!text(etResult).isEmpty()) {
                request.setResult(
                        Double.parseDouble(text(etResult)));
            }

            if (!text(etOutOf).isEmpty()) {
                request.setOutOf(
                        Double.parseDouble(text(etOutOf)));
            }

        } else {

            request.setGradeOrDivision(
                    text(etGradeDivision));

        }

        if (!text(etStartDate).isEmpty()) {
            request.setStartDate(
                    String.valueOf(LocalDate.parse(
                            text(etStartDate),
                            DATE_FORMAT)));
        }

        if (!cbCurrentlyStudying.isChecked()
                && !text(etEndDate).isEmpty()) {

            request.setEndDate(
                    String.valueOf(LocalDate.parse(
                            text(etEndDate),
                            DATE_FORMAT)));

        }

        if (educationId == -1) {

            submitSave(request);

        } else {

            submitUpdate(request);

        }

    }


// =======================================================
// Save New
// =======================================================

    private void submitSave(EducationRequestDTO request) {

        setLoading(true);

        repository.saveEducation(
                request,
                new Callback<EducationResponseDTO>() {

                    @Override
                    public void onResponse(
                            Call<EducationResponseDTO> call,
                            Response<EducationResponseDTO> response) {

                        setLoading(false);

                        if (response.isSuccessful()) {

                            Toast.makeText(
                                    EducationEditActivity.this,
                                    "Education saved",
                                    Toast.LENGTH_SHORT).show();

                            finish();

                        } else {

                            Toast.makeText(
                                    EducationEditActivity.this,
                                    "Failed",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onFailure(
                            Call<EducationResponseDTO> call,
                            Throwable t) {

                        setLoading(false);

                        Toast.makeText(
                                EducationEditActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_SHORT).show();

                    }

                });

    }


// =======================================================
// Update
// =======================================================

    private void submitUpdate(EducationRequestDTO request) {

        setLoading(true);

        repository.updateEducation(
                educationId,
                request,
                new Callback<EducationResponseDTO>() {

                    @Override
                    public void onResponse(
                            Call<EducationResponseDTO> call,
                            Response<EducationResponseDTO> response) {

                        setLoading(false);

                        if (response.isSuccessful()) {

                            Toast.makeText(
                                    EducationEditActivity.this,
                                    "Education updated",
                                    Toast.LENGTH_SHORT).show();

                            finish();

                        } else {

                            Toast.makeText(
                                    EducationEditActivity.this,
                                    "Failed",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onFailure(
                            Call<EducationResponseDTO> call,
                            Throwable t) {

                        setLoading(false);

                        Toast.makeText(
                                EducationEditActivity.this,
                                t.getMessage(),
                                Toast.LENGTH_SHORT).show();

                    }

                });

    }


// =======================================================
// Helpers
// =======================================================

    private String text(TextView view) {

        if (view.getText() == null)
            return "";

        return view.getText().toString().trim();

    }

    private void setLoading(boolean loading) {

        progressBar.setVisibility(
                loading ? View.VISIBLE : View.GONE);

        btnSave.setEnabled(!loading);

    }


}