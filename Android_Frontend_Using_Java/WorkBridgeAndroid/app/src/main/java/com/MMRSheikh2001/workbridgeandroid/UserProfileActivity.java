package com.MMRSheikh2001.workbridgeandroid;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.api.ApiService;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.UserProfileRepository;
import com.MMRSheikh2001.workbridgeandroid.enums.GenderType;
import com.MMRSheikh2001.workbridgeandroid.enums.JobType;
import com.MMRSheikh2001.workbridgeandroid.enums.WorkPlaceType;
import com.MMRSheikh2001.workbridgeandroid.masterdata.repository.CountryRepository;
import com.MMRSheikh2001.workbridgeandroid.masterdata.repository.DistrictRepository;
import com.MMRSheikh2001.workbridgeandroid.masterdata.repository.DivisionRepository;
import com.MMRSheikh2001.workbridgeandroid.masterdata.repository.PoliceStationRepository;
import com.MMRSheikh2001.workbridgeandroid.masterdata.response.CountryResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.masterdata.response.DistrictResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.masterdata.response.DivisionResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.masterdata.response.PoliceStationResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.request.UserProfileRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.response.LoginResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.UserProfileResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.Strictness;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This screen only ever UPDATES an existing UserProfile.
 * A profile row is already created (with just the name saved) the moment the
 * user registers, so we never POST /api/userprofiles here - we always
 * GET the existing profile by id, prefill the form, and PUT the changes back.
 */
public class UserProfileActivity extends AppCompatActivity {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    // ---- Dependencies ----
    private UserProfileRepository userProfileRepository;
    private ApiService apiService;
    private SessionManager sessionManager;
    private Gson gson;

    // ---- State ----
    private Long profileId;
    private UserProfileResponseDTO currentProfile;
    private Uri selectedImageUri;

    private final List<PoliceStationResponseDTO> policeStations = new ArrayList<>();
    private final Map<String, Long> presentPoliceStationMap = new LinkedHashMap<>();
    private final Map<String, Long> permanentPoliceStationMap = new LinkedHashMap<>();
    private Long pendingPresentPoliceStationId;
    private Long pendingPermanentPoliceStationId;
    private boolean policeStationsLoaded = false;

    // ---- Views ----
    private ShapeableImageView ivProfilePhoto;
    private FloatingActionButton fabEditPhoto;
    private TextView tvHeaderName;
    private TextView tvUserEmail;
    private Chip chipProfileStatus;

    private TextInputEditText etName, etPhone, etHeadline, etFreelancerTitle, etDateOfBirth;
    private AutoCompleteTextView actvGender, actvMaritalStatus;
    private TextInputEditText etNationality, etReligion;

    private TextInputEditText etProfessionalSummary, etBio, etCareerObjective;
    private TextInputEditText etFatherName, etMotherName, etNidNumber, etPassportNumber;

    private AutoCompleteTextView actvPreferredJobType, actvPreferredWorkplace;
    private TextInputEditText etExpectedSalary, etCurrentSalary;

    private TextInputEditText etGithubLink, etLinkedinLink, etPortfolioWebsite;

    private TextInputEditText etPresentAddressDetails, etPresentAddressPostCode;


    private MaterialCheckBox cbSameAsPresentAddress;
    private TextInputEditText etPermanentAddressDetails, etPermanentAddressPostCode;


    private MaterialButton btnCancel, btnSaveProfile;
    private ProgressBar progressLoading;


    private AutoCompleteTextView actvPresentCountry;
    private AutoCompleteTextView actvPresentDivision;
    private AutoCompleteTextView actvPresentDistrict;
    private AutoCompleteTextView actvPresentPoliceStation;

    private AutoCompleteTextView actvPermanentCountry;
    private AutoCompleteTextView actvPermanentDivision;
    private AutoCompleteTextView actvPermanentDistrict;
    private AutoCompleteTextView actvPermanentPoliceStation;

    CountryRepository countryRepository;
    DivisionRepository divisionRepository;
    DistrictRepository districtRepository;
    PoliceStationRepository policeStationRepository;

    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    Glide.with(this)
                            .load(uri)
                            .placeholder(R.drawable.workbridge_logo)
                            .circleCrop()
                            .into(ivProfilePhoto);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userProfileRepository = new UserProfileRepository(this);
        apiService = ApiClient.getClient(this);
        sessionManager = new SessionManager(this);
        gson = buildGson();

        bindViews();
        setupDropdowns();
        setupListeners();

        LoginResponseDTO user = sessionManager.getUser();
        if (user == null || user.getProfileId() == null) {
            Toast.makeText(this, "No profile found. Please log in again.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        profileId = user.getProfileId();

        countryRepository = new CountryRepository(this);
        divisionRepository = new DivisionRepository(this);
        districtRepository = new DistrictRepository(this);
        policeStationRepository = new PoliceStationRepository(this);

        loadCountries();
        loadProfile();
    }

    // =====================================================================
    // View binding / setup
    // =====================================================================

    private void bindViews() {
        ivProfilePhoto = findViewById(R.id.ivProfilePhoto);
        fabEditPhoto = findViewById(R.id.fabEditPhoto);
        tvHeaderName = findViewById(R.id.tvHeaderName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        chipProfileStatus = findViewById(R.id.chipProfileStatus);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etHeadline = findViewById(R.id.etHeadline);
        etFreelancerTitle = findViewById(R.id.etFreelancerTitle);
        etDateOfBirth = findViewById(R.id.etDateOfBirth);
        actvGender = findViewById(R.id.actvGender);
        actvMaritalStatus = findViewById(R.id.actvMaritalStatus);
        etNationality = findViewById(R.id.etNationality);
        etReligion = findViewById(R.id.etReligion);

        etProfessionalSummary = findViewById(R.id.etProfessionalSummary);
        etBio = findViewById(R.id.etBio);
        etCareerObjective = findViewById(R.id.etCareerObjective);

        etFatherName = findViewById(R.id.etFatherName);
        etMotherName = findViewById(R.id.etMotherName);
        etNidNumber = findViewById(R.id.etNidNumber);
        etPassportNumber = findViewById(R.id.etPassportNumber);

        actvPreferredJobType = findViewById(R.id.actvPreferredJobType);
        actvPreferredWorkplace = findViewById(R.id.actvPreferredWorkplace);
        etExpectedSalary = findViewById(R.id.etExpectedSalary);
        etCurrentSalary = findViewById(R.id.etCurrentSalary);

        etGithubLink = findViewById(R.id.etGithubLink);
        etLinkedinLink = findViewById(R.id.etLinkedinLink);
        etPortfolioWebsite = findViewById(R.id.etPortfolioWebsite);

        etPresentAddressDetails = findViewById(R.id.etPresentAddressDetails);
        etPresentAddressPostCode = findViewById(R.id.etPresentAddressPostCode);


        cbSameAsPresentAddress = findViewById(R.id.cbSameAsPresentAddress);
        etPermanentAddressDetails = findViewById(R.id.etPermanentAddressDetails);
        etPermanentAddressPostCode = findViewById(R.id.etPermanentAddressPostCode);


        btnCancel = findViewById(R.id.btnCancel);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        progressLoading = findViewById(R.id.progressLoading);

        actvPresentCountry = findViewById(R.id.actvPresentCountry);
        actvPresentDivision = findViewById(R.id.actvPresentDivision);
        actvPresentDistrict = findViewById(R.id.actvPresentDistrict);
        actvPresentPoliceStation = findViewById(R.id.actvPresentPoliceStation);

        actvPermanentCountry = findViewById(R.id.actvPermanentCountry);
        actvPermanentDivision = findViewById(R.id.actvPermanentDivision);
        actvPermanentDistrict = findViewById(R.id.actvPermanentDistrict);
        actvPermanentPoliceStation = findViewById(R.id.actvPermanentPoliceStation);
    }

    private void setupDropdowns() {
        actvGender.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                new String[]{GenderType.MALE.name(), GenderType.FEMALE.name(), GenderType.OTHER.name()}));

        actvMaritalStatus.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                new String[]{"Single", "Married", "Divorced", "Widowed"}));

        String[] jobTypes = new String[JobType.values().length];
        for (int i = 0; i < JobType.values().length; i++) jobTypes[i] = JobType.values()[i].name();
        actvPreferredJobType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, jobTypes));

        String[] workplaceTypes = new String[WorkPlaceType.values().length];
        for (int i = 0; i < WorkPlaceType.values().length; i++)
            workplaceTypes[i] = WorkPlaceType.values()[i].name();
        actvPreferredWorkplace.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, workplaceTypes));
    }

    private void setupListeners() {
        etDateOfBirth.setOnClickListener(v -> showDatePicker());

        View.OnClickListener pickPhoto = v -> imagePickerLauncher.launch("image/*");
        fabEditPhoto.setOnClickListener(pickPhoto);
        ivProfilePhoto.setOnClickListener(pickPhoto);

        cbSameAsPresentAddress.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etPermanentAddressDetails.setText(text(etPresentAddressDetails));
                etPermanentAddressPostCode.setText(text(etPresentAddressPostCode));
                actvPermanentPoliceStation.setText(text(actvPresentPoliceStation), false);
            }
            setPermanentFieldsEnabled(!isChecked);
        });

        btnCancel.setOnClickListener(v -> finish());
        btnSaveProfile.setOnClickListener(v -> saveProfile());
    }

    private void setPermanentFieldsEnabled(boolean enabled) {
        etPermanentAddressDetails.setEnabled(enabled);
        etPermanentAddressPostCode.setEnabled(enabled);
        actvPermanentPoliceStation.setEnabled(enabled);
    }

    // =====================================================================
    // Loading data
    // =====================================================================


    private void loadProfile() {
        setLoading(true);
        userProfileRepository.getUserProfileById(profileId, new Callback<UserProfileResponseDTO>() {
            @Override
            public void onResponse(Call<UserProfileResponseDTO> call, Response<UserProfileResponseDTO> response) {
                setLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    currentProfile = response.body();
                    populateForm(currentProfile);
                } else {
                    Toast.makeText(UserProfileActivity.this,
                            "Failed to load profile (code " + response.code() + ")", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponseDTO> call, Throwable t) {
                setLoading(false);
                Toast.makeText(UserProfileActivity.this,
                        "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void populateForm(UserProfileResponseDTO p) {
        tvHeaderName.setText(TextUtils.isEmpty(p.getName()) ? "Your Name" : p.getName());
        tvUserEmail.setText(p.getUserEmail());

        boolean completed = Boolean.TRUE.equals(p.getProfileCompleted());
        chipProfileStatus.setText(completed ? "Profile Complete" : "Profile Incomplete");
        chipProfileStatus.setChipIconTintResource(completed ? R.color.teal_700 : R.color.purple_700);

        if (!TextUtils.isEmpty(p.getImage())) {
            Glide.with(this)
                    .load(ApiClient.getUserProfileImage(p.getImage()))
                    .placeholder(R.drawable.workbridge_logo)
                    .error(R.drawable.workbridge_logo)
                    .circleCrop()
                    .into(ivProfilePhoto);
        }

        etName.setText(p.getName());
        etPhone.setText(p.getPhone());
        etHeadline.setText(p.getHeadline());
        etFreelancerTitle.setText(p.getFreelancerTitle());

        if (p.getDateOfBirth() != null) {
            etDateOfBirth.setText(p.getDateOfBirth().format(DATE_FORMAT));
        }
        if (p.getGender() != null) {
            actvGender.setText(p.getGender().name(), false);
        }
        actvMaritalStatus.setText(p.getMaritalStatus(), false);
        etNationality.setText(p.getNationality());
        etReligion.setText(p.getReligion());

        etProfessionalSummary.setText(p.getProfessionalSummary());
        etBio.setText(p.getBio());
        etCareerObjective.setText(p.getCareerObjective());

        etFatherName.setText(p.getFatherName());
        etMotherName.setText(p.getMotherName());
        etNidNumber.setText(p.getNidNumber());
        etPassportNumber.setText(p.getPassportNumber());

        if (p.getPreferredJobType() != null) {
            actvPreferredJobType.setText(p.getPreferredJobType().name(), false);
        }
        if (p.getPreferredWorkplace() != null) {
            actvPreferredWorkplace.setText(p.getPreferredWorkplace().name(), false);
        }
        if (p.getExpectedSalary() != null)
            etExpectedSalary.setText(p.getExpectedSalary().toPlainString());
        if (p.getCurrentSalary() != null)
            etCurrentSalary.setText(p.getCurrentSalary().toPlainString());

        etGithubLink.setText(p.getGithubLink());
        etLinkedinLink.setText(p.getLinkedinLink());
        etPortfolioWebsite.setText(p.getPortfolioWebsite());

        etPresentAddressDetails.setText(p.getPresentAddressDetails());
        etPresentAddressPostCode.setText(p.getPresentAddressPostCode());

        boolean sameAddress = p.getPermanentAddressId() != null
                && p.getPermanentAddressId().equals(p.getPresentAddressId());
        cbSameAsPresentAddress.setChecked(sameAddress);
        setPermanentFieldsEnabled(!sameAddress);

        etPermanentAddressDetails.setText(p.getPermanentAddressDetails());
        etPermanentAddressPostCode.setText(p.getPermanentAddressPostCode());

        loadPresentAddress(p);
        loadPermanentAddress(p);
        trySelectPoliceStations();
    }

    private void trySelectPoliceStations() {
        if (!policeStationsLoaded || currentProfile == null) return;

        if (pendingPresentPoliceStationId != null) {
            for (PoliceStationResponseDTO ps : policeStations) {
                if (pendingPresentPoliceStationId.equals(ps.getPoliceStationId())) {
                    actvPresentPoliceStation.setText(ps.getPoliceStationName(), false);
                    break;
                }
            }
        }
        if (pendingPermanentPoliceStationId != null) {
            for (PoliceStationResponseDTO ps : policeStations) {
                if (pendingPermanentPoliceStationId.equals(ps.getPoliceStationId())) {
                    actvPermanentPoliceStation.setText(ps.getPoliceStationName(), false);
                    break;
                }
            }
        }
    }

    // =====================================================================
    // Saving (update only)
    // =====================================================================

    private void saveProfile() {
        if (TextUtils.isEmpty(text(etName))) {
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }

        UserProfileRequestDTO req = new UserProfileRequestDTO();

        LoginResponseDTO user = sessionManager.getUser();
        req.setUserId(user != null ? user.getUserId() : null);

        req.setName(text(etName));
        req.setPhone(text(etPhone));
        req.setHeadline(text(etHeadline));
        req.setFreelancerTitle(text(etFreelancerTitle));
        req.setProfessionalSummary(text(etProfessionalSummary));
        req.setBio(text(etBio));
        req.setCareerObjective(text(etCareerObjective));

        String dob = text(etDateOfBirth);
        if (!dob.isEmpty()) {
            try {
                req.setDateOfBirth(LocalDate.parse(dob, DATE_FORMAT));
            } catch (Exception ignored) {
            }
        }

        String genderText = text(actvGender);
        if (!genderText.isEmpty()) {
            try {
                req.setGender(GenderType.valueOf(genderText));
            } catch (IllegalArgumentException ignored) {
            }
        }
        req.setNationality(text(etNationality));
        req.setReligion(text(etReligion));
        req.setMaritalStatus(text(actvMaritalStatus));

        req.setFatherName(text(etFatherName));
        req.setMotherName(text(etMotherName));
        req.setNidNumber(text(etNidNumber));
        req.setPassportNumber(text(etPassportNumber));

        req.setGithubLink(text(etGithubLink));
        req.setLinkedinLink(text(etLinkedinLink));
        req.setPortfolioWebsite(text(etPortfolioWebsite));

        String expectedSalary = text(etExpectedSalary);
        if (!expectedSalary.isEmpty()) {
            try {
                req.setExpectedSalary(new BigDecimal(expectedSalary));
            } catch (NumberFormatException ignored) {
            }
        }
        String currentSalary = text(etCurrentSalary);
        if (!currentSalary.isEmpty()) {
            try {
                req.setCurrentSalary(new BigDecimal(currentSalary));
            } catch (NumberFormatException ignored) {
            }
        }

        String jobTypeText = text(actvPreferredJobType);
        if (!jobTypeText.isEmpty()) {
            try {
                req.setPreferredJobType(JobType.valueOf(jobTypeText));
            } catch (IllegalArgumentException ignored) {
            }
        }
        String workplaceText = text(actvPreferredWorkplace);
        if (!workplaceText.isEmpty()) {
            try {
                req.setPreferredWorkplace(WorkPlaceType.valueOf(workplaceText));
            } catch (IllegalArgumentException ignored) {
            }
        }

        if (currentProfile != null) {
            req.setPresentAddressId(currentProfile.getPresentAddressId());
            req.setPermanentAddressId(currentProfile.getPermanentAddressId());
        }
        req.setPresentAddressDetails(text(etPresentAddressDetails));
        req.setPresentAddressPostCode(text(etPresentAddressPostCode));
        req.setPresentAddressPoliceStationId(presentPoliceStationMap.get(text(actvPresentPoliceStation)));

        req.setPermanentAddressDetails(text(etPermanentAddressDetails));
        req.setPermanentAddressPostCode(text(etPermanentAddressPostCode));
        req.setPermanentAddressPoliceStationId(permanentPoliceStationMap.get(text(actvPermanentPoliceStation)));

        submitUpdate(req);
    }

    private void submitUpdate(UserProfileRequestDTO req) {
        String json = gson.toJson(req);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        MultipartBody.Part imagePart = null;
        if (selectedImageUri != null) {
            try {
                imagePart = buildImagePart(selectedImageUri);
            } catch (IOException e) {
                Toast.makeText(this, "Could not read selected image", Toast.LENGTH_SHORT).show();
            }
        }

        setLoading(true);
        userProfileRepository.updateUserProfile(profileId, body, imagePart, new Callback<UserProfileResponseDTO>() {
            @Override
            public void onResponse(Call<UserProfileResponseDTO> call, Response<UserProfileResponseDTO> response) {
                setLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    currentProfile = response.body();
                    Toast.makeText(UserProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(UserProfileActivity.this,
                            "Update failed (code " + response.code() + ")", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponseDTO> call, Throwable t) {
                setLoading(false);
                Toast.makeText(UserProfileActivity.this,
                        "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private MultipartBody.Part buildImagePart(Uri uri) throws IOException {
        String fileName = queryFileName(uri);
        File tempFile = new File(getCacheDir(), fileName);

        try (InputStream in = getContentResolver().openInputStream(uri);
             FileOutputStream out = new FileOutputStream(tempFile)) {
            if (in == null) throw new IOException("Unable to open selected image");
            byte[] buffer = new byte[4096];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        }

        RequestBody requestFile = RequestBody.create(tempFile, MediaType.parse("image/*"));
        return MultipartBody.Part.createFormData("image", tempFile.getName(), requestFile);
    }

    private String queryFileName(Uri uri) {
        String name = "profile_image.jpg";
        try (android.database.Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex >= 0) {
                    name = cursor.getString(nameIndex);
                }
            }
        } catch (Exception ignored) {
        }
        return name;
    }

    // =====================================================================
    // Helpers
    // =====================================================================

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        String existing = text(etDateOfBirth);
        if (!existing.isEmpty()) {
            try {
                LocalDate parsed = LocalDate.parse(existing, DATE_FORMAT);
                calendar.set(parsed.getYear(), parsed.getMonthValue() - 1, parsed.getDayOfMonth());
            } catch (Exception ignored) {
            }
        }

        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    LocalDate picked = LocalDate.of(year, month + 1, dayOfMonth);
                    etDateOfBirth.setText(picked.format(DATE_FORMAT));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();
    }

    private void setLoading(boolean loading) {
        progressLoading.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnSaveProfile.setEnabled(!loading);
        btnCancel.setEnabled(!loading);
    }

    private String text(@Nullable TextInputEditText editText) {
        return editText == null || editText.getText() == null ? "" : editText.getText().toString().trim();
    }

    private String text(@Nullable AutoCompleteTextView editText) {
        return editText == null || editText.getText() == null ? "" : editText.getText().toString().trim();
    }

    private Gson buildGson() {
        return new GsonBuilder()
                .setStrictness(Strictness.LENIENT)
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                        src == null ? null : new JsonPrimitive(src.format(DATE_FORMAT)))
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) ->
                        LocalDate.parse(json.getAsString(), DATE_FORMAT))
                .create();
    }

    private void loadCountries() {

        countryRepository.getAllCountries(new Callback<List<CountryResponseDTO>>() {
            @Override
            public void onResponse(Call<List<CountryResponseDTO>> call,
                                   Response<List<CountryResponseDTO>> response) {

                if (!response.isSuccessful() || response.body() == null)
                    return;

                List<CountryResponseDTO> countries = response.body();

                List<String> names = new ArrayList<>();

                for (CountryResponseDTO c : countries) {
                    names.add(c.getCountryName());
                }

                ArrayAdapter<String> adapter =
                        new ArrayAdapter<>(UserProfileActivity.this,
                                android.R.layout.simple_list_item_1,
                                names);

                actvPresentCountry.setAdapter(adapter);
                actvPermanentCountry.setAdapter(adapter);

                actvPresentCountry.setOnItemClickListener((p, v, pos, id) -> {
                    loadPresentDivisions(countries.get(pos).getCountryId());
                });

                actvPermanentCountry.setOnItemClickListener((p, v, pos, id) -> {
                    loadPermanentDivisions(countries.get(pos).getCountryId());
                });

            }

            @Override
            public void onFailure(Call<List<CountryResponseDTO>> call,
                                  Throwable t) {

            }
        });

    }

    private void loadPresentDivisions(Long countryId) {

        divisionRepository.getDivisionsByCountryId(countryId, new Callback<List<DivisionResponseDTO>>() {

            @Override
            public void onResponse(Call<List<DivisionResponseDTO>> call,
                                   Response<List<DivisionResponseDTO>> response) {

                if (!response.isSuccessful() || response.body() == null)
                    return;

                List<DivisionResponseDTO> list = response.body();

                List<String> names = new ArrayList<>();

                for (DivisionResponseDTO d : list)
                    names.add(d.getDivisionName());

                actvPresentDivision.setAdapter(
                        new ArrayAdapter<>(UserProfileActivity.this,
                                android.R.layout.simple_list_item_1,
                                names));

                actvPresentDivision.setOnItemClickListener((p, v, pos, id) ->
                        loadPresentDistricts(list.get(pos).getDivisionId()));

            }

            @Override
            public void onFailure(Call<List<DivisionResponseDTO>> call,
                                  Throwable t) {

            }

        });

    }

    private void loadPresentDistricts(Long divisionId) {

        districtRepository.getDistrictsByDivisionId(divisionId, new Callback<List<DistrictResponseDTO>>() {

            @Override
            public void onResponse(Call<List<DistrictResponseDTO>> call,
                                   Response<List<DistrictResponseDTO>> response) {

                if (!response.isSuccessful() || response.body() == null)
                    return;

                List<DistrictResponseDTO> list = response.body();

                List<String> names = new ArrayList<>();

                for (DistrictResponseDTO d : list)
                    names.add(d.getDistrictName());

                actvPresentDistrict.setAdapter(
                        new ArrayAdapter<>(UserProfileActivity.this,
                                android.R.layout.simple_list_item_1,
                                names));

                actvPresentDistrict.setOnItemClickListener((p, v, pos, id) ->
                        loadPresentPoliceStations(list.get(pos).getDistrictId()));

            }

            @Override
            public void onFailure(Call<List<DistrictResponseDTO>> call,
                                  Throwable t) {

            }

        });

    }


    private void loadPresentPoliceStations(Long districtId) {

        policeStationRepository.getPoliceStationsByDistrictId(districtId,
                new Callback<List<PoliceStationResponseDTO>>() {

                    @Override
                    public void onResponse(Call<List<PoliceStationResponseDTO>> call,
                                           Response<List<PoliceStationResponseDTO>> response) {

                        if (!response.isSuccessful() || response.body() == null)
                            return;

                        presentPoliceStationMap.clear();

                        List<String> names = new ArrayList<>();

                        for (PoliceStationResponseDTO ps : response.body()) {

                            names.add(ps.getPoliceStationName());

                            presentPoliceStationMap.put(
                                    ps.getPoliceStationName(),
                                    ps.getPoliceStationId());

                        }

                        actvPresentPoliceStation.setAdapter(
                                new ArrayAdapter<>(UserProfileActivity.this,
                                        android.R.layout.simple_list_item_1,
                                        names));

                    }

                    @Override
                    public void onFailure(Call<List<PoliceStationResponseDTO>> call,
                                          Throwable t) {

                    }

                });

    }

    private void loadPermanentCountries() {

        countryRepository.getAllCountries(new Callback<List<CountryResponseDTO>>() {

            @Override
            public void onResponse(Call<List<CountryResponseDTO>> call,
                                   Response<List<CountryResponseDTO>> response) {

                if (!response.isSuccessful() || response.body() == null)
                    return;

                List<CountryResponseDTO> countries = response.body();

                List<String> names = new ArrayList<>();

                for (CountryResponseDTO country : countries) {
                    names.add(country.getCountryName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        UserProfileActivity.this,
                        android.R.layout.simple_list_item_1,
                        names);

                actvPermanentCountry.setAdapter(adapter);

                actvPermanentCountry.setOnItemClickListener((parent, view, position, id) -> {

                    CountryResponseDTO selected = countries.get(position);

                    loadPermanentDivisions(selected.getCountryId());

                });

            }

            @Override
            public void onFailure(Call<List<CountryResponseDTO>> call,
                                  Throwable t) {

            }

        });

    }

    private void loadPermanentDivisions(Long countryId) {

        divisionRepository.getDivisionsByCountryId(
                countryId,
                new Callback<List<DivisionResponseDTO>>() {

                    @Override
                    public void onResponse(Call<List<DivisionResponseDTO>> call,
                                           Response<List<DivisionResponseDTO>> response) {

                        if (!response.isSuccessful() || response.body() == null)
                            return;

                        List<DivisionResponseDTO> divisions = response.body();

                        List<String> names = new ArrayList<>();

                        for (DivisionResponseDTO division : divisions) {
                            names.add(division.getDivisionName());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                UserProfileActivity.this,
                                android.R.layout.simple_list_item_1,
                                names);

                        actvPermanentDivision.setAdapter(adapter);

                        actvPermanentDivision.setOnItemClickListener((parent, view, position, id) -> {

                            DivisionResponseDTO selected = divisions.get(position);

                            loadPermanentDistricts(selected.getDivisionId());

                        });

                    }

                    @Override
                    public void onFailure(Call<List<DivisionResponseDTO>> call,
                                          Throwable t) {

                    }

                });

    }


    private void loadPermanentDistricts(Long divisionId) {

        districtRepository.getDistrictsByDivisionId(
                divisionId,
                new Callback<List<DistrictResponseDTO>>() {

                    @Override
                    public void onResponse(Call<List<DistrictResponseDTO>> call,
                                           Response<List<DistrictResponseDTO>> response) {

                        if (!response.isSuccessful() || response.body() == null)
                            return;

                        List<DistrictResponseDTO> districts = response.body();

                        List<String> names = new ArrayList<>();

                        for (DistrictResponseDTO district : districts) {
                            names.add(district.getDistrictName());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                UserProfileActivity.this,
                                android.R.layout.simple_list_item_1,
                                names);

                        actvPermanentDistrict.setAdapter(adapter);

                        actvPermanentDistrict.setOnItemClickListener((parent, view, position, id) -> {

                            DistrictResponseDTO selected = districts.get(position);

                            loadPermanentPoliceStations(selected.getDistrictId());

                        });

                    }

                    @Override
                    public void onFailure(Call<List<DistrictResponseDTO>> call,
                                          Throwable t) {

                    }

                });

    }


    private void loadPermanentPoliceStations(Long districtId) {

        policeStationRepository.getPoliceStationsByDistrictId(
                districtId,
                new Callback<List<PoliceStationResponseDTO>>() {

                    @Override
                    public void onResponse(Call<List<PoliceStationResponseDTO>> call,
                                           Response<List<PoliceStationResponseDTO>> response) {

                        if (!response.isSuccessful() || response.body() == null)
                            return;

                        List<PoliceStationResponseDTO> stations = response.body();

                        permanentPoliceStationMap.clear();

                        List<String> names = new ArrayList<>();

                        for (PoliceStationResponseDTO ps : stations) {

                            names.add(ps.getPoliceStationName());

                            permanentPoliceStationMap.put(
                                    ps.getPoliceStationName(),
                                    ps.getPoliceStationId());

                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                UserProfileActivity.this,
                                android.R.layout.simple_list_item_1,
                                names);

                        actvPermanentPoliceStation.setAdapter(adapter);

                    }

                    @Override
                    public void onFailure(Call<List<PoliceStationResponseDTO>> call,
                                          Throwable t) {

                    }

                });

    }

    private void loadPresentAddress(UserProfileResponseDTO p) {

        countryRepository.getAllCountries(new Callback<List<CountryResponseDTO>>() {

            @Override
            public void onResponse(Call<List<CountryResponseDTO>> call,
                                   Response<List<CountryResponseDTO>> response) {

                if (!response.isSuccessful() || response.body() == null) return;

                List<String> names = new ArrayList<>();

                for (CountryResponseDTO c : response.body()) {
                    names.add(c.getCountryName());
                }

                actvPresentCountry.setAdapter(
                        new ArrayAdapter<>(
                                UserProfileActivity.this,
                                android.R.layout.simple_list_item_1,
                                names));

                actvPresentCountry.setText(p.getPresentCountryName(), false);

                loadPresentDivisions(p);
            }

            @Override
            public void onFailure(Call<List<CountryResponseDTO>> call, Throwable t) {

            }
        });
    }


    private void loadPresentDivisions(UserProfileResponseDTO p) {

        divisionRepository.getDivisionsByCountryId(
                p.getPresentCountryId(),
                new Callback<List<DivisionResponseDTO>>() {

                    @Override
                    public void onResponse(Call<List<DivisionResponseDTO>> call,
                                           Response<List<DivisionResponseDTO>> response) {

                        if (!response.isSuccessful() || response.body() == null) return;

                        List<String> names = new ArrayList<>();

                        for (DivisionResponseDTO d : response.body()) {
                            names.add(d.getDivisionName());
                        }

                        actvPresentDivision.setAdapter(
                                new ArrayAdapter<>(
                                        UserProfileActivity.this,
                                        android.R.layout.simple_list_item_1,
                                        names));

                        actvPresentDivision.setText(
                                p.getPresentDivisionName(),
                                false);

                        loadPresentDistricts(p);
                    }

                    @Override
                    public void onFailure(Call<List<DivisionResponseDTO>> call, Throwable t) {

                    }
                });
    }

    private void loadPresentDistricts(UserProfileResponseDTO p) {

        districtRepository.getDistrictsByDivisionId(
                p.getPresentDivisionId(),
                new Callback<List<DistrictResponseDTO>>() {

                    @Override
                    public void onResponse(Call<List<DistrictResponseDTO>> call,
                                           Response<List<DistrictResponseDTO>> response) {

                        if (!response.isSuccessful() || response.body() == null) return;

                        List<String> names = new ArrayList<>();

                        for (DistrictResponseDTO d : response.body()) {
                            names.add(d.getDistrictName());
                        }

                        actvPresentDistrict.setAdapter(
                                new ArrayAdapter<>(
                                        UserProfileActivity.this,
                                        android.R.layout.simple_list_item_1,
                                        names));

                        actvPresentDistrict.setText(
                                p.getPresentDistrictName(),
                                false);

                        loadPresentPoliceStations(p);
                    }

                    @Override
                    public void onFailure(Call<List<DistrictResponseDTO>> call,
                                          Throwable t) {

                    }
                });
    }


    private void loadPresentPoliceStations(UserProfileResponseDTO p) {

        policeStationRepository.getPoliceStationsByDistrictId(
                p.getPresentDistrictId(),
                new Callback<List<PoliceStationResponseDTO>>() {

                    @Override
                    public void onResponse(Call<List<PoliceStationResponseDTO>> call,
                                           Response<List<PoliceStationResponseDTO>> response) {

                        if (!response.isSuccessful() || response.body() == null) return;

                        presentPoliceStationMap.clear();

                        List<String> names = new ArrayList<>();

                        for (PoliceStationResponseDTO ps : response.body()) {

                            names.add(ps.getPoliceStationName());

                            presentPoliceStationMap.put(
                                    ps.getPoliceStationName(),
                                    ps.getPoliceStationId());
                        }

                        actvPresentPoliceStation.setAdapter(
                                new ArrayAdapter<>(
                                        UserProfileActivity.this,
                                        android.R.layout.simple_list_item_1,
                                        names));

                        actvPresentPoliceStation.setText(
                                p.getPresentPoliceStationName(),
                                false);
                    }

                    @Override
                    public void onFailure(Call<List<PoliceStationResponseDTO>> call,
                                          Throwable t) {

                    }
                });
    }


    private void loadPermanentAddress(UserProfileResponseDTO p) {

        countryRepository.getAllCountries(new Callback<List<CountryResponseDTO>>() {

            @Override
            public void onResponse(Call<List<CountryResponseDTO>> call,
                                   Response<List<CountryResponseDTO>> response) {

                if (!response.isSuccessful() || response.body() == null) return;

                List<String> names = new ArrayList<>();

                for (CountryResponseDTO c : response.body()) {
                    names.add(c.getCountryName());
                }

                actvPermanentCountry.setAdapter(
                        new ArrayAdapter<>(
                                UserProfileActivity.this,
                                android.R.layout.simple_list_item_1,
                                names));

                actvPermanentCountry.setText(
                        p.getPermanentCountryName(),
                        false);

                loadPermanentDivisions(p);
            }

            @Override
            public void onFailure(Call<List<CountryResponseDTO>> call,
                                  Throwable t) {

            }
        });
    }

    private void loadPermanentDivisions(UserProfileResponseDTO p) {

        divisionRepository.getDivisionsByCountryId(
                p.getPermanentCountryId(),
                new Callback<List<DivisionResponseDTO>>() {

                    @Override
                    public void onResponse(Call<List<DivisionResponseDTO>> call,
                                           Response<List<DivisionResponseDTO>> response) {

                        if (!response.isSuccessful() || response.body() == null)
                            return;

                        List<String> names = new ArrayList<>();

                        for (DivisionResponseDTO division : response.body()) {
                            names.add(division.getDivisionName());
                        }

                        actvPermanentDivision.setAdapter(
                                new ArrayAdapter<>(
                                        UserProfileActivity.this,
                                        android.R.layout.simple_list_item_1,
                                        names));

                        actvPermanentDivision.setText(
                                p.getPermanentDivisionName(),
                                false);

                        loadPermanentDistricts(p);
                    }

                    @Override
                    public void onFailure(Call<List<DivisionResponseDTO>> call,
                                          Throwable t) {

                    }
                });
    }


    private void loadPermanentDistricts(UserProfileResponseDTO p) {

        districtRepository.getDistrictsByDivisionId(
                p.getPermanentDivisionId(),
                new Callback<List<DistrictResponseDTO>>() {

                    @Override
                    public void onResponse(Call<List<DistrictResponseDTO>> call,
                                           Response<List<DistrictResponseDTO>> response) {

                        if (!response.isSuccessful() || response.body() == null)
                            return;

                        List<String> names = new ArrayList<>();

                        for (DistrictResponseDTO district : response.body()) {
                            names.add(district.getDistrictName());
                        }

                        actvPermanentDistrict.setAdapter(
                                new ArrayAdapter<>(
                                        UserProfileActivity.this,
                                        android.R.layout.simple_list_item_1,
                                        names));

                        actvPermanentDistrict.setText(
                                p.getPermanentDistrictName(),
                                false);

                        loadPermanentPoliceStations(p);
                    }

                    @Override
                    public void onFailure(Call<List<DistrictResponseDTO>> call,
                                          Throwable t) {

                    }
                });
    }


    private void loadPermanentPoliceStations(UserProfileResponseDTO p) {

        policeStationRepository.getPoliceStationsByDistrictId(
                p.getPermanentDistrictId(),
                new Callback<List<PoliceStationResponseDTO>>() {

                    @Override
                    public void onResponse(Call<List<PoliceStationResponseDTO>> call,
                                           Response<List<PoliceStationResponseDTO>> response) {

                        if (!response.isSuccessful() || response.body() == null)
                            return;

                        permanentPoliceStationMap.clear();

                        List<String> names = new ArrayList<>();

                        for (PoliceStationResponseDTO ps : response.body()) {

                            names.add(ps.getPoliceStationName());

                            permanentPoliceStationMap.put(
                                    ps.getPoliceStationName(),
                                    ps.getPoliceStationId());
                        }

                        actvPermanentPoliceStation.setAdapter(
                                new ArrayAdapter<>(
                                        UserProfileActivity.this,
                                        android.R.layout.simple_list_item_1,
                                        names));

                        actvPermanentPoliceStation.setText(
                                p.getPermanentPoliceStationName(),
                                false);
                    }

                    @Override
                    public void onFailure(Call<List<PoliceStationResponseDTO>> call,
                                          Throwable t) {

                    }
                });
    }


}
