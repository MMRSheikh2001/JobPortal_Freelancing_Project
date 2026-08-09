package com.MMRSheikh2001.workbridgeandroid.cvinformations.repository;

import android.content.Context;

import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.api.ApiService;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.request.ExtracurricularRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ExtracurricularResponseDTO;

import java.util.List;

import retrofit2.Callback;

public class ExtracurricularRepository {

    private final ApiService apiService;

    public ExtracurricularRepository(Context context) {
        apiService = ApiClient.getClient(context);
    }


    // Save
    public void saveExtracurricular(
            ExtracurricularRequestDTO request,
            Callback<ExtracurricularResponseDTO> callback) {

        apiService.saveExtracurricular(request)
                .enqueue(callback);
    }

    // Get By Id
    public void getExtracurricularById(
            Long id,
            Callback<ExtracurricularResponseDTO> callback) {

        apiService.getExtracurricularById(id)
                .enqueue(callback);
    }

    // Update
    public void updateExtracurricular(
            Long id,
            ExtracurricularRequestDTO request,
            Callback<ExtracurricularResponseDTO> callback) {

        apiService.updateExtracurricular(id, request)
                .enqueue(callback);
    }

    // Delete
    public void deleteExtracurricular(
            Long id,
            Callback<String> callback) {

        apiService.deleteExtracurricular(id)
                .enqueue(callback);
    }

    // Get All By User Profile
    public void getExtracurricularsByUserProfileId(
            Long userProfileId,
            Callback<List<ExtracurricularResponseDTO>> callback) {

        apiService.getExtracurricularsByUserProfileId(userProfileId)
                .enqueue(callback);
    }

    // Count By User Profile
    public void countExtracurricularsByUserProfileId(
            Long userProfileId,
            Callback<Long> callback) {

        apiService.countExtracurricularsByUserProfileId(userProfileId)
                .enqueue(callback);
    }


}
