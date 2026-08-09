package com.MMRSheikh2001.workbridgeandroid.masterdata.repository;

import android.content.Context;

import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.api.ApiService;
import com.MMRSheikh2001.workbridgeandroid.masterdata.response.SkillResponseDTO;

import java.util.List;

import retrofit2.Callback;

public class SkillRepository {

    private final ApiService apiService;

    public SkillRepository(Context context) {
        apiService = ApiClient.getClient(context);
    }


    public void getAllSkills(
            Callback<List<SkillResponseDTO>> callback) {

        apiService.getAllSkills().enqueue(callback);
    }

    public void getSkillById(Long id,
                             Callback<SkillResponseDTO> callback) {

        apiService.getSkillById(id).enqueue(callback);
    }

    public void getSkillsByCategoryId(
            Long categoryId,
            Callback<List<SkillResponseDTO>> callback) {

        apiService.getSkillsByCategoryId(categoryId)
                .enqueue(callback);
    }


}
