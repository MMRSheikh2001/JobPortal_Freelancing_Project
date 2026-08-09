package com.MMRSheikh2001.workbridgeandroid.cvinformations.repository;

import android.content.Context;

import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.api.ApiService;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.request.EducationRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.EducationResponseDTO;

import java.util.List;

import retrofit2.Callback;

public class EducationRepository {

    private final ApiService apiService;

    public EducationRepository(Context context) {
        apiService = ApiClient.getClient(context);
    }


    // Save
    public void saveEducation(
            EducationRequestDTO request,
            Callback<EducationResponseDTO> callback) {

        apiService.saveEducation(request)
                .enqueue(callback);
    }

    // Get By Id
    public void getEducationById(
            Long id,
            Callback<EducationResponseDTO> callback) {

        apiService.getEducationById(id)
                .enqueue(callback);
    }

    // Update
    public void updateEducation(
            Long id,
            EducationRequestDTO request,
            Callback<EducationResponseDTO> callback) {

        apiService.updateEducation(id, request)
                .enqueue(callback);
    }

    // Delete
    public void deleteEducation(
            Long id,
            Callback<String> callback) {

        apiService.deleteEducation(id)
                .enqueue(callback);
    }

    // Get All By User Profile
    public void getEducationsByUserProfileId(
            Long userProfileId,
            Callback<List<EducationResponseDTO>> callback) {

        apiService.getEducationsByUserProfileId(userProfileId)
                .enqueue(callback);
    }

    // Count By User Profile
    public void countEducationsByUserProfileId(
            Long userProfileId,
            Callback<Long> callback) {

        apiService.countEducationsByUserProfileId(userProfileId)
                .enqueue(callback);
    }


}
