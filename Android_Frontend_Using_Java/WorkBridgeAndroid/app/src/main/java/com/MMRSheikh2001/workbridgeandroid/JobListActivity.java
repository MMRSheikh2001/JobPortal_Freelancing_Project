package com.MMRSheikh2001.workbridgeandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.MMRSheikh2001.workbridgeandroid.adapter.JobAdapter;
import com.MMRSheikh2001.workbridgeandroid.enums.EmploymentType;
import com.MMRSheikh2001.workbridgeandroid.enums.WorkPlaceType;
import com.MMRSheikh2001.workbridgeandroid.masterdata.repository.CategoryRepository;
import com.MMRSheikh2001.workbridgeandroid.masterdata.repository.CountryRepository;
import com.MMRSheikh2001.workbridgeandroid.masterdata.repository.DistrictRepository;
import com.MMRSheikh2001.workbridgeandroid.masterdata.repository.DivisionRepository;
import com.MMRSheikh2001.workbridgeandroid.masterdata.repository.PoliceStationRepository;
import com.MMRSheikh2001.workbridgeandroid.masterdata.response.CategoryResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.masterdata.response.CountryResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.masterdata.response.DistrictResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.masterdata.response.DivisionResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.masterdata.response.PoliceStationResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.repository.JobRepository;
import com.MMRSheikh2001.workbridgeandroid.request.JobSearchRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.response.JobResponseDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobListActivity extends AppCompatActivity {

    private TextInputEditText etKeyword;

    private Spinner spCategory;
    private Spinner spCountry;
    private Spinner spDivision;
    private Spinner spDistrict;
    private Spinner spPoliceStation;
    private Spinner spEmploymentType;
    private Spinner spWorkPlaceType;

    private MaterialButton btnSearch;

    private MaterialButton btnReset;

    private RecyclerView rvJobs;

    private CategoryRepository categoryRepository;
    private CountryRepository countryRepository;
    private DivisionRepository divisionRepository;
    private DistrictRepository districtRepository;
    private PoliceStationRepository policeStationRepository;

    private JobRepository jobRepository;

    private List<CategoryResponseDTO> categoryList;
    private List<CountryResponseDTO> countryList;
    private List<DivisionResponseDTO> divisionList;
    private List<DistrictResponseDTO> districtList;
    private List<PoliceStationResponseDTO> policeStationList;

    private List<JobResponseDTO> jobList = new ArrayList<>();

    private JobAdapter jobAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_job_list);
        init();

        loadCategories();

        loadCountries();

        loadEmploymentTypes();

        loadWorkPlaceTypes();



        setSpinnerListeners();

        btnSearch.setOnClickListener(v -> search());

        btnReset.setOnClickListener(v -> resetFilters());

    }

    private void init() {

        etKeyword = findViewById(R.id.etKeyword);

        spCategory = findViewById(R.id.spCategory);
        spCountry = findViewById(R.id.spCountry);
        spDivision = findViewById(R.id.spDivision);
        spDistrict = findViewById(R.id.spDistrict);
        spPoliceStation = findViewById(R.id.spPoliceStation);

        spEmploymentType = findViewById(R.id.spEmploymentType);
        spWorkPlaceType = findViewById(R.id.spWorkPlaceType);

        btnSearch = findViewById(R.id.btnSearch);
        btnReset = findViewById(R.id.btnReset);

        rvJobs = findViewById(R.id.rvJobs);

        rvJobs.setLayoutManager(new LinearLayoutManager(this));

        jobAdapter = new JobAdapter(
                this,
                jobList,
                job -> {

                    Intent intent=new Intent(JobListActivity.this
                    , JobDetailsActivity.class);
                    intent.putExtra("JOB_ID",job.getId());
                    startActivity(intent);


                });

        rvJobs.setAdapter(jobAdapter);

        categoryRepository = new CategoryRepository(this);
        countryRepository = new CountryRepository(this);
        divisionRepository = new DivisionRepository(this);
        districtRepository = new DistrictRepository(this);
        policeStationRepository = new PoliceStationRepository(this);

        jobRepository = new JobRepository(this);
    }

    private void loadCategories() {
        categoryRepository.getAllCategories(new Callback<List<CategoryResponseDTO>>() {
            @Override
            public void onResponse(Call<List<CategoryResponseDTO>> call, Response<List<CategoryResponseDTO>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    return;
                }
                categoryList = response.body();
                List<String> names = new ArrayList<>();
                names.add("All Categories");
                for (CategoryResponseDTO dto : categoryList) {
                    names.add(dto.getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(JobListActivity.this
                        , android.R.layout.simple_spinner_dropdown_item, names);
                spCategory.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<CategoryResponseDTO>> call, Throwable t) {
                Toast.makeText(JobListActivity.this, "Failed to load Categories"
                        , Toast.LENGTH_SHORT).show();

//                t.printStackTrace();
//
//                Toast.makeText(JobListActivity.this,
//                        t.getMessage(),
//                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private void loadCountries() {

        countryRepository.getAllCountries(new Callback<List<CountryResponseDTO>>() {
            @Override
            public void onResponse(Call<List<CountryResponseDTO>> call, Response<List<CountryResponseDTO>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    return;
                }
                countryList = response.body();
                List<String> names = new ArrayList<>();
                names.add("All Countries");
                for (CountryResponseDTO dto : countryList) {
                    names.add(dto.getCountryName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(JobListActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, names);
                spCountry.setAdapter(adapter);

                search();
            }

            @Override
            public void onFailure(Call<List<CountryResponseDTO>> call, Throwable t) {

                Toast.makeText(JobListActivity.this, "Failed to load Countries"
                        , Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadEmploymentTypes() {


        List<String> names = new ArrayList<>();

        names.add("All Employment Types");

        for (EmploymentType type : EmploymentType.values()) {
            names.add(type.name().replace("_", " "));
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        names);

        spEmploymentType.setAdapter(adapter);

    }

    private void loadWorkPlaceTypes() {


        List<String> names = new ArrayList<>();

        names.add("All Workplace Types");

        for (WorkPlaceType type : WorkPlaceType.values()) {
            names.add(type.name().replace("_", " "));
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        names);

        spWorkPlaceType.setAdapter(adapter);
    }

    private void setSpinnerListeners() {

        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clearDivision();
                clearDistrict();
                clearPoliceStation();

                if (position == 0) {
                    return;
                }
                CountryResponseDTO country = countryList.get(position - 1);
                loadDivisions(country.getCountryId());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view,
                                       int position,
                                       long id) {

                clearDistrict();
                clearPoliceStation();

                if (position == 0) {
                    return;
                }

                DivisionResponseDTO division = divisionList.get(position - 1);

                loadDistricts(division.getDivisionId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view,
                                       int position,
                                       long id) {

                clearPoliceStation();

                if (position == 0) {
                    return;
                }

                DistrictResponseDTO district = districtList.get(position - 1);

                loadPoliceStations(district.getDistrictId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void loadDivisions(Long countryId) {

        divisionRepository.getDivisionsByCountryId(countryId,
                new Callback<List<DivisionResponseDTO>>() {

                    @Override
                    public void onResponse(Call<List<DivisionResponseDTO>> call,
                                           Response<List<DivisionResponseDTO>> response) {

                        if (!response.isSuccessful() || response.body() == null)
                            return;

                        divisionList = response.body();

                        List<String> names = new ArrayList<>();
                        names.add("All Divisions");

                        for (DivisionResponseDTO dto : divisionList) {
                            names.add(dto.getDivisionName());
                        }

                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<>(
                                        JobListActivity.this,
                                        android.R.layout.simple_spinner_dropdown_item,
                                        names);

                        spDivision.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<List<DivisionResponseDTO>> call,
                                          Throwable t) {

                        Toast.makeText(JobListActivity.this,
                                "Failed to load divisions",
                                Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void loadDistricts(Long divisionId) {

        districtRepository.getDistrictsByDivisionId(divisionId,
                new Callback<List<DistrictResponseDTO>>() {

                    @Override
                    public void onResponse(Call<List<DistrictResponseDTO>> call,
                                           Response<List<DistrictResponseDTO>> response) {

                        if (!response.isSuccessful() || response.body() == null)
                            return;

                        districtList = response.body();

                        List<String> names = new ArrayList<>();
                        names.add("All Districts");

                        for (DistrictResponseDTO dto : districtList) {
                            names.add(dto.getDistrictName());
                        }

                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<>(
                                        JobListActivity.this,
                                        android.R.layout.simple_spinner_dropdown_item,
                                        names);

                        spDistrict.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<List<DistrictResponseDTO>> call,
                                          Throwable t) {

                        Toast.makeText(JobListActivity.this,
                                "Failed to load districts",
                                Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void loadPoliceStations(Long districtId) {

        policeStationRepository.getPoliceStationsByDistrictId(
                districtId,
                new Callback<List<PoliceStationResponseDTO>>() {

                    @Override
                    public void onResponse(
                            Call<List<PoliceStationResponseDTO>> call,
                            Response<List<PoliceStationResponseDTO>> response) {

                        if (!response.isSuccessful() || response.body() == null)
                            return;

                        policeStationList = response.body();

                        List<String> names = new ArrayList<>();
                        names.add("All Police Stations");

                        for (PoliceStationResponseDTO dto : policeStationList) {
                            names.add(dto.getPoliceStationName());
                        }

                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<>(
                                        JobListActivity.this,
                                        android.R.layout.simple_spinner_dropdown_item,
                                        names);

                        spPoliceStation.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(
                            Call<List<PoliceStationResponseDTO>> call,
                            Throwable t) {

                        Toast.makeText(JobListActivity.this,
                                "Failed to load police stations",
                                Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void clearDivision() {

        divisionList = new ArrayList<>();

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        new String[]{"All Divisions"});

        spDivision.setAdapter(adapter);
    }

    private void clearDistrict() {

        districtList = new ArrayList<>();

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        new String[]{"All Districts"});

        spDistrict.setAdapter(adapter);
    }

    private void clearPoliceStation() {

        policeStationList = new ArrayList<>();

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        new String[]{"All Police Stations"});

        spPoliceStation.setAdapter(adapter);
    }

    private void search() {

        JobSearchRequestDTO dto = new JobSearchRequestDTO();

        dto.setKeyword(etKeyword.getText().toString().trim());

        if (spCategory.getSelectedItemPosition() > 0) {
            dto.setCategoryId(
                    categoryList.get(spCategory.getSelectedItemPosition() - 1).getId());
        }

        if (spCountry.getSelectedItemPosition() > 0) {
            dto.setCountryId(
                    countryList.get(spCountry.getSelectedItemPosition() - 1).getCountryId());
        }

        if (spDivision.getSelectedItemPosition() > 0) {
            dto.setDivisionId(
                    divisionList.get(spDivision.getSelectedItemPosition() - 1).getDivisionId());
        }

        if (spDistrict.getSelectedItemPosition() > 0) {
            dto.setDistrictId(
                    districtList.get(spDistrict.getSelectedItemPosition() - 1).getDistrictId());
        }

        if (spPoliceStation.getSelectedItemPosition() > 0) {
            dto.setPoliceStationId(
                    policeStationList.get(spPoliceStation.getSelectedItemPosition() - 1).getPoliceStationId());
        }

        if (spEmploymentType.getSelectedItemPosition() > 0) {
            dto.setEmploymentType(
                    EmploymentType.values()[spEmploymentType.getSelectedItemPosition() - 1]);
        }

        if (spWorkPlaceType.getSelectedItemPosition() > 0) {
            dto.setWorkPlaceType(
                    WorkPlaceType.values()[spWorkPlaceType.getSelectedItemPosition() - 1]);
        }
        dto.setActive(true);

        jobRepository.searchJobs(dto, new Callback<List<JobResponseDTO>>() {
            @Override
            public void onResponse(Call<List<JobResponseDTO>> call, Response<List<JobResponseDTO>> response) {
                btnSearch.setEnabled(true);

                if (!response.isSuccessful()) {
                    Toast.makeText(JobListActivity.this,
                            "Search failed",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                jobList.clear();

                if (response.body() != null) {
                    jobList.addAll(response.body());
                }
                jobAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(Call<List<JobResponseDTO>> call, Throwable t) {
                btnSearch.setEnabled(true);

                Toast.makeText(JobListActivity.this,
                        "Unable to connect to server",
                        Toast.LENGTH_SHORT).show();

                t.printStackTrace();
            }
        });


    }

    private void resetFilters() {


        etKeyword.setText("");

        spCategory.setSelection(0);
        spCountry.setSelection(0);

        clearDivision();
        clearDistrict();
        clearPoliceStation();

        spEmploymentType.setSelection(0);
        spWorkPlaceType.setSelection(0);

        search();

        Toast.makeText(this,
                        "Filters reset",
                        Toast.LENGTH_SHORT)
                .show();
    }


}