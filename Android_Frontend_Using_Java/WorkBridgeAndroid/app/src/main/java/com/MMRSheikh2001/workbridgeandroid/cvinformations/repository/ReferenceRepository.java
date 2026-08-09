package com.MMRSheikh2001.workbridgeandroid.cvinformations.repository;

import android.content.Context;

import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.api.ApiService;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.request.ReferenceRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ReferenceResponseDTO;

import java.util.List;

import retrofit2.Callback;

public class ReferenceRepository {

    private final ApiService apiService;

    public ReferenceRepository(Context context) {
        apiService = ApiClient.getClient(context);
    }


    // Save
    public void saveReference(
            ReferenceRequestDTO request,
            Callback<ReferenceResponseDTO> callback) {

        apiService.saveReference(request)
                .enqueue(callback);
    }

    // Get By Id
    public void getReferenceById(
            Long id,
            Callback<ReferenceResponseDTO> callback) {

        apiService.getReferenceById(id)
                .enqueue(callback);
    }

    // Update
    public void updateReference(
            Long id,
            ReferenceRequestDTO request,
            Callback<ReferenceResponseDTO> callback) {

        apiService.updateReference(id, request)
                .enqueue(callback);
    }

    // Delete
    public void deleteReference(
            Long id,
            Callback<String> callback) {

        apiService.deleteReference(id)
                .enqueue(callback);
    }

    // Get All By User Profile
    public void getReferencesByUserProfileId(
            Long userProfileId,
            Callback<List<ReferenceResponseDTO>> callback) {

        apiService.getReferencesByUserProfileId(userProfileId)
                .enqueue(callback);
    }

    // Count By User Profile
    public void countReferencesByUserProfileId(
            Long userProfileId,
            Callback<Long> callback) {

        apiService.countReferencesByUserProfileId(userProfileId)
                .enqueue(callback);
    }


}
