package com.MMRSheikh2001.workbridgeandroid.cvinformations.repository;

import android.content.Context;

import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.api.ApiService;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.request.ExperienceRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ExperienceResponseDTO;

import java.util.List;

import retrofit2.Callback;

public class ExperienceRepository {

    private final ApiService apiService;

    public ExperienceRepository(Context context) {
        apiService = ApiClient.getClient(context);
    }

    // Save
    public void saveExperience(
            ExperienceRequestDTO request,
            Callback<ExperienceResponseDTO> callback) {

        apiService.saveExperience(request)
                .enqueue(callback);
    }

    // Get By Id
    public void getExperienceById(
            Long id,
            Callback<ExperienceResponseDTO> callback) {

        apiService.getExperienceById(id)
                .enqueue(callback);
    }

    // Update
    public void updateExperience(
            Long id,
            ExperienceRequestDTO request,
            Callback<ExperienceResponseDTO> callback) {

        apiService.updateExperience(id, request)
                .enqueue(callback);
    }

    // Delete
    public void deleteExperience(
            Long id,
            Callback<String> callback) {

        apiService.deleteExperience(id)
                .enqueue(callback);
    }

    // Get All By User Profile
    public void getExperiencesByUserProfileId(
            Long userProfileId,
            Callback<List<ExperienceResponseDTO>> callback) {

        apiService.getExperiencesByUserProfileId(userProfileId)
                .enqueue(callback);
    }

    // Count By User Profile
    public void countExperiencesByUserProfileId(
            Long userProfileId,
            Callback<Long> callback) {

        apiService.countExperiencesByUserProfileId(userProfileId)
                .enqueue(callback);
    }
}