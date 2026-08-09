package com.MMRSheikh2001.workbridgeandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.UserSkillRepository;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.request.UserSkillRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.UserSkillResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.enums.ProficiencyLevel;
import com.MMRSheikh2001.workbridgeandroid.masterdata.repository.CategoryRepository;
import com.MMRSheikh2001.workbridgeandroid.masterdata.repository.SkillRepository;
import com.MMRSheikh2001.workbridgeandroid.masterdata.response.CategoryResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.masterdata.response.SkillResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.LoginResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserSkillActivity extends AppCompatActivity {



    private MaterialToolbar toolbar;

    private MaterialAutoCompleteTextView actCategory;
    private MaterialAutoCompleteTextView actSkill;
    private MaterialAutoCompleteTextView actProficiency;

    private TextInputEditText etYearsOfExperience;

    private MaterialButton btnSave;
    private MaterialButton btnCancel;

    private ProgressBar progressBar;

    private CategoryRepository categoryRepository;
    private SkillRepository skillRepository;
    private UserSkillRepository userSkillRepository;
    private SessionManager sessionManager;

    private Long profileId;
    private Long userSkillId;

    private final List<CategoryResponseDTO> categoryList =
            new ArrayList<>();

    private final List<SkillResponseDTO> skillList =
            new ArrayList<>();

    private CategoryResponseDTO selectedCategory;
    private SkillResponseDTO selectedSkill;
    private ProficiencyLevel selectedProficiency;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_skill);



        init();

        LoginResponseDTO user = sessionManager.getUser();

        if (user == null) {
            finish();
            return;
        }

        profileId = user.getProfileId();

        userSkillId = getIntent().getLongExtra(
                "userSkillId",
                -1L);

        toolbar.setNavigationOnClickListener(v -> finish());

        btnCancel.setOnClickListener(v -> finish());

        if (userSkillId == -1) {

            toolbar.setTitle("Add Skill");

        } else {

            toolbar.setTitle("Edit Skill");

        }

        loadCategories();

        loadProficiencyDropdown();

        btnSave.setOnClickListener(v -> saveUserSkill());



    }



    private void init() {

        categoryRepository =
                new CategoryRepository(this);

        skillRepository =
                new SkillRepository(this);

        userSkillRepository =
                new UserSkillRepository(this);

        sessionManager =
                new SessionManager(this);

        toolbar =
                findViewById(R.id.toolbar);

        actCategory =
                findViewById(R.id.actCategory);

        actSkill =
                findViewById(R.id.actSkill);

        actProficiency =
                findViewById(R.id.actProficiency);

        etYearsOfExperience =
                findViewById(R.id.etYearsOfExperience);

        btnSave =
                findViewById(R.id.btnSave);

        btnCancel =
                findViewById(R.id.btnCancel);

        progressBar =
                findViewById(R.id.progressBar);

    }



    private void loadCategories() {

        progressBar.setVisibility(View.VISIBLE);

        categoryRepository.getAllCategories(
                new retrofit2.Callback<List<CategoryResponseDTO>>() {

                    @Override
                    public void onResponse(
                            retrofit2.Call<List<CategoryResponseDTO>> call,
                            retrofit2.Response<List<CategoryResponseDTO>> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()
                                && response.body() != null) {

                            categoryList.clear();
                            categoryList.addAll(response.body());

                            List<String> categoryNames = new ArrayList<>();

                            for (CategoryResponseDTO category : categoryList) {
                                categoryNames.add(category.getName());
                            }

                            ArrayAdapter<String> adapter =
                                    new ArrayAdapter<>(
                                            EditUserSkillActivity.this,
                                            android.R.layout.simple_dropdown_item_1line,
                                            categoryNames);

                            actCategory.setAdapter(adapter);

                            actCategory.setOnItemClickListener(
                                    (parent, view, position, id) -> {

                                        selectedCategory =
                                                categoryList.get(position);

                                        // Clear previous skill selection
                                        selectedSkill = null;
                                        actSkill.setText("", false);

                                        // Load skills of selected category
                                        loadSkills(
                                                selectedCategory.getId(),
                                                null);

                                    });

                            // Edit mode
                            if (userSkillId != -1) {
                                loadUserSkill();
                            }

                        }

                    }

                    @Override
                    public void onFailure(
                            retrofit2.Call<List<CategoryResponseDTO>> call,
                            Throwable t) {

                        progressBar.setVisibility(View.GONE);

                    }

                });

    }

    private void loadProficiencyDropdown() {

        List<String> list = new ArrayList<>();

        for (ProficiencyLevel level
                : ProficiencyLevel.values()) {

            list.add(level.name());

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
                                ProficiencyLevel.values()[position]);

    }


    private void loadSkills(
            Long categoryId,
            Long selectedSkillId) {

        progressBar.setVisibility(View.VISIBLE);

        skillRepository.getSkillsByCategoryId(
                categoryId,
                new retrofit2.Callback<List<SkillResponseDTO>>() {

                    @Override
                    public void onResponse(
                            retrofit2.Call<List<SkillResponseDTO>> call,
                            retrofit2.Response<List<SkillResponseDTO>> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()
                                && response.body() != null) {

                            skillList.clear();
                            skillList.addAll(response.body());

                            List<String> skillNames =
                                    new ArrayList<>();

                            for (SkillResponseDTO skill : skillList) {
                                skillNames.add(skill.getSkillName());
                            }

                            ArrayAdapter<String> adapter =
                                    new ArrayAdapter<>(
                                            EditUserSkillActivity.this,
                                            android.R.layout.simple_dropdown_item_1line,
                                            skillNames);

                            actSkill.setAdapter(adapter);

                            actSkill.setOnItemClickListener(
                                    (parent, view, position, id) ->
                                            selectedSkill =
                                                    skillList.get(position));

                            // ---------- Edit Mode ----------
                            if (selectedSkillId != null) {

                                for (SkillResponseDTO skill : skillList) {

                                    if (skill.getSkillId()
                                            .equals(selectedSkillId)) {

                                        selectedSkill = skill;

                                        actSkill.setText(
                                                skill.getSkillName(),
                                                false);

                                        break;
                                    }

                                }

                            }

                        }

                    }

                    @Override
                    public void onFailure(
                            retrofit2.Call<List<SkillResponseDTO>> call,
                            Throwable t) {

                        progressBar.setVisibility(View.GONE);

                    }

                });

    }

    private void loadUserSkill() {

        progressBar.setVisibility(View.VISIBLE);

        userSkillRepository.getUserSkillById(
                userSkillId,
                new retrofit2.Callback<UserSkillResponseDTO>() {

                    @Override
                    public void onResponse(
                            retrofit2.Call<UserSkillResponseDTO> call,
                            retrofit2.Response<UserSkillResponseDTO> response) {

                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()
                                && response.body() != null) {

                            UserSkillResponseDTO dto = response.body();

                            // ---------- Category ----------

                            actCategory.setText(
                                    dto.getCategoryName(),
                                    false);

                            for (CategoryResponseDTO category : categoryList) {

                                if (category.getId()
                                        .equals(dto.getCategoryId())) {

                                    selectedCategory = category;
                                    break;
                                }

                            }

                            // ---------- Skill ----------
                            // This loads the skills for the selected category
                            // and automatically selects the saved skill.

                            loadSkills(
                                    dto.getCategoryId(),
                                    dto.getSkillId());

                            // ---------- Proficiency ----------

                            if (dto.getProficiencyLevel() != null) {

                                selectedProficiency =
                                        dto.getProficiencyLevel();

                                actProficiency.setText(
                                        selectedProficiency.name(),
                                        false);

                            }

                            // ---------- Years of Experience ----------

                            if (dto.getYearsOfExperience() != null) {

                                etYearsOfExperience.setText(
                                        String.valueOf(
                                                dto.getYearsOfExperience()));

                            }

                        }

                    }

                    @Override
                    public void onFailure(
                            retrofit2.Call<UserSkillResponseDTO> call,
                            Throwable t) {

                        progressBar.setVisibility(View.GONE);

                    }

                });

    }



    private void saveUserSkill() {

        if (selectedCategory == null) {
            actCategory.setError("Select a category");
            return;
        }

        if (selectedSkill == null) {
            actSkill.setError("Select a skill");
            return;
        }

        if (selectedProficiency == null) {
            actProficiency.setError("Select proficiency");
            return;
        }

        UserSkillRequestDTO request = new UserSkillRequestDTO();

        request.setSkillId(selectedSkill.getSkillId());
        request.setUserProfileId(profileId);
        request.setProficiencyLevel(selectedProficiency);

        String years = etYearsOfExperience.getText()
                .toString()
                .trim();

        if (!years.isEmpty()) {
            request.setYearsOfExperience(
                    Integer.parseInt(years));
        }

        progressBar.setVisibility(View.VISIBLE);

        if (userSkillId == -1) {

            userSkillRepository.saveUserSkill(
                    request,
                    new Callback<UserSkillResponseDTO>() {

                        @Override
                        public void onResponse(
                                Call<UserSkillResponseDTO> call,
                                Response<UserSkillResponseDTO> response) {

                            progressBar.setVisibility(View.GONE);

                            if (response.isSuccessful()) {

                                Toast.makeText(
                                        EditUserSkillActivity.this,
                                        "Skill saved successfully",
                                        Toast.LENGTH_SHORT
                                ).show();

                                finish();

                            } else {

                                Toast.makeText(
                                        EditUserSkillActivity.this,
                                        "Failed to save skill",
                                        Toast.LENGTH_SHORT
                                ).show();

                            }

                        }

                        @Override
                        public void onFailure(
                                Call<UserSkillResponseDTO> call,
                                Throwable t) {

                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(
                                    EditUserSkillActivity.this,
                                    t.getMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();

                        }

                    });

        } else {

            userSkillRepository.updateUserSkill(
                    userSkillId,
                    request,
                    new Callback<UserSkillResponseDTO>() {

                        @Override
                        public void onResponse(
                                Call<UserSkillResponseDTO> call,
                                Response<UserSkillResponseDTO> response) {

                            progressBar.setVisibility(View.GONE);

                            if (response.isSuccessful()) {

                                Toast.makeText(
                                        EditUserSkillActivity.this,
                                        "Skill updated successfully",
                                        Toast.LENGTH_SHORT
                                ).show();

                                finish();

                            } else {

                                Toast.makeText(
                                        EditUserSkillActivity.this,
                                        "Failed to update skill",
                                        Toast.LENGTH_SHORT
                                ).show();

                            }

                        }

                        @Override
                        public void onFailure(
                                Call<UserSkillResponseDTO> call,
                                Throwable t) {

                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(
                                    EditUserSkillActivity.this,
                                    t.getMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();

                        }

                    });

        }

    }








}