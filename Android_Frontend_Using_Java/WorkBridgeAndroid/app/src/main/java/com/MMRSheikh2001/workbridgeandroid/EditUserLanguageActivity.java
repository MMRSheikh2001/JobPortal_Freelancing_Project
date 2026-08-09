package com.MMRSheikh2001.workbridgeandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.UserLanguageRepository;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.request.UserLanguageRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.UserLanguageResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.enums.LanguageProficiency;
import com.MMRSheikh2001.workbridgeandroid.masterdata.repository.LanguageRepository;
import com.MMRSheikh2001.workbridgeandroid.masterdata.response.LanguageResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.LoginResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserLanguageActivity extends AppCompatActivity {


    private MaterialToolbar toolbar;

    private AutoCompleteTextView actLanguage;
    private AutoCompleteTextView actProficiency;

    private MaterialButton btnSave;
    private MaterialButton btnCancel;

    private ProgressBar progressBar;

    private UserLanguageRepository userLanguageRepository;
    private LanguageRepository languageRepository;
    private SessionManager sessionManager;

    private Long profileId;
    private Long userLanguageId;

    private final List<LanguageResponseDTO> languageList =
            new ArrayList<>();

    private LanguageResponseDTO selectedLanguage;

    private LanguageProficiency selectedProficiency;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_language);


        init();

        LoginResponseDTO user = sessionManager.getUser();

        if (user == null) {
            finish();
            return;
        }

        profileId = user.getProfileId();

        userLanguageId =
                getIntent().getLongExtra(
                        "userLanguageId",
                        -1);

        toolbar.setNavigationOnClickListener(v -> finish());

        btnCancel.setOnClickListener(v -> finish());

        if (userLanguageId == -1) {

            toolbar.setTitle("Add Language");

        } else {

            toolbar.setTitle("Edit Language");

        }

        loadLanguages();

        loadProficiencyDropdown();

        btnSave.setOnClickListener(v -> saveUserLanguage());


    }


    private void init() {

        userLanguageRepository =
                new UserLanguageRepository(this);

        languageRepository =
                new LanguageRepository(this);

        sessionManager =
                new SessionManager(this);

        toolbar = findViewById(R.id.toolbar);

        actLanguage =
                findViewById(R.id.actLanguage);

        actProficiency =
                findViewById(R.id.actProficiency);

        btnSave =
                findViewById(R.id.btnSave);

        btnCancel =
                findViewById(R.id.btnCancel);

        progressBar =
                findViewById(R.id.progressBar);

    }

    private void loadLanguages() {

        progressBar.setVisibility(View.VISIBLE);

        languageRepository.getAllLanguages(
                new retrofit2.Callback<List<LanguageResponseDTO>>() {

                    @Override
                    public void onResponse(
                            retrofit2.Call<List<LanguageResponseDTO>> call,
                            retrofit2.Response<List<LanguageResponseDTO>> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()
                                && response.body() != null) {

                            languageList.clear();
                            languageList.addAll(response.body());

                            List<String> names = new ArrayList<>();

                            for (LanguageResponseDTO language : languageList) {
                                names.add(language.getName());
                            }

                            ArrayAdapter<String> adapter =
                                    new ArrayAdapter<>(
                                            EditUserLanguageActivity.this,
                                            android.R.layout.simple_dropdown_item_1line,
                                            names);

                            actLanguage.setAdapter(adapter);

                            actLanguage.setOnItemClickListener(
                                    (parent, view, position, id) ->
                                            selectedLanguage =
                                                    languageList.get(position));

                            // Important:
                            // Wait until languages are loaded before loading
                            // the existing UserLanguage for editing.

                            if (userLanguageId != -1) {
                                loadUserLanguage();
                            }

                        }

                    }

                    @Override
                    public void onFailure(
                            retrofit2.Call<List<LanguageResponseDTO>> call,
                            Throwable t) {

                        progressBar.setVisibility(View.GONE);

                    }

                });

    }


    private void loadProficiencyDropdown() {

        List<String> list = new ArrayList<>();

        for (LanguageProficiency proficiency
                : LanguageProficiency.values()) {

            list.add(proficiency.name());

        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        list);

        actProficiency.setAdapter(adapter);

        actProficiency.setOnItemClickListener(
                (parent, view, position, id) ->

                        selectedProficiency =
                                LanguageProficiency.values()[position]);

    }

    private void loadUserLanguage() {

        progressBar.setVisibility(View.VISIBLE);

        userLanguageRepository.getUserLanguageById(
                userLanguageId,
                new retrofit2.Callback<UserLanguageResponseDTO>() {

                    @Override
                    public void onResponse(
                            retrofit2.Call<UserLanguageResponseDTO> call,
                            retrofit2.Response<UserLanguageResponseDTO> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()
                                && response.body() != null) {

                            UserLanguageResponseDTO dto = response.body();

                            // Set Language Dropdown
                            actLanguage.setText(
                                    dto.getLanguageName(),
                                    false);

                            // Find the selected language object
                            for (LanguageResponseDTO language : languageList) {

                                if (language.getId()
                                        .equals(dto.getLanguageId())) {

                                    selectedLanguage = language;
                                    break;
                                }

                            }

                            // Set Proficiency Dropdown
                            if (dto.getProficiency() != null) {

                                selectedProficiency =
                                        dto.getProficiency();

                                actProficiency.setText(
                                        selectedProficiency.name(),
                                        false);

                            }

                        }

                    }

                    @Override
                    public void onFailure(
                            retrofit2.Call<UserLanguageResponseDTO> call,
                            Throwable t) {

                        progressBar.setVisibility(View.GONE);

                    }

                });

    }

    private void saveUserLanguage() {

        if (selectedLanguage == null) {
            actLanguage.setError("Select a language");
            return;
        }

        if (selectedProficiency == null) {
            actProficiency.setError("Select proficiency");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        UserLanguageRequestDTO request = new UserLanguageRequestDTO();

        request.setLanguageId(selectedLanguage.getId());
        request.setProficiency(selectedProficiency);
        request.setUserProfileId(profileId);

        if (userLanguageId == -1) {

            userLanguageRepository.saveUserLanguage(
                    request,
                    new Callback<UserLanguageResponseDTO>() {

                        @Override
                        public void onResponse(
                                Call<UserLanguageResponseDTO> call,
                                Response<UserLanguageResponseDTO> response) {

                            progressBar.setVisibility(View.GONE);

                            if (response.isSuccessful()) {

                                Toast.makeText(
                                        EditUserLanguageActivity.this,
                                        "Language saved successfully",
                                        Toast.LENGTH_SHORT
                                ).show();

                                finish();

                            } else {

                                Toast.makeText(
                                        EditUserLanguageActivity.this,
                                        "Failed to save language",
                                        Toast.LENGTH_SHORT
                                ).show();

                            }

                        }

                        @Override
                        public void onFailure(
                                Call<UserLanguageResponseDTO> call,
                                Throwable t) {

                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(
                                    EditUserLanguageActivity.this,
                                    t.getMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();

                        }

                    });

        } else {

            userLanguageRepository.updateUserLanguage(
                    userLanguageId,
                    request,
                    new Callback<UserLanguageResponseDTO>() {

                        @Override
                        public void onResponse(
                                Call<UserLanguageResponseDTO> call,
                                Response<UserLanguageResponseDTO> response) {

                            progressBar.setVisibility(View.GONE);

                            if (response.isSuccessful()) {

                                Toast.makeText(
                                        EditUserLanguageActivity.this,
                                        "Language updated successfully",
                                        Toast.LENGTH_SHORT
                                ).show();

                                finish();

                            } else {

                                Toast.makeText(
                                        EditUserLanguageActivity.this,
                                        "Failed to update language",
                                        Toast.LENGTH_SHORT
                                ).show();

                            }

                        }

                        @Override
                        public void onFailure(
                                Call<UserLanguageResponseDTO> call,
                                Throwable t) {

                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(
                                    EditUserLanguageActivity.this,
                                    t.getMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();

                        }

                    });

        }

    }


}