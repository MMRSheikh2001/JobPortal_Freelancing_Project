package com.MMRSheikh2001.workbridgeandroid.cvinformations.repository;

import android.content.Context;

import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.api.ApiService;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ResumeImportPreviewDTO;

import retrofit2.Call;
import retrofit2.Callback;

public class ResumeImportRepository {


    private final ApiService apiService;

    public ResumeImportRepository(Context context) {
        apiService = ApiClient.getClient(context);
    }

    // Get AI Preview
    public void getResumeImportPreview(
            Long userProfileId,
            Callback<ResumeImportPreviewDTO> callback) {

        Call<ResumeImportPreviewDTO> call =
                apiService.getResumeImportPreview(userProfileId);

        call.enqueue(callback);
    }

    // Save Imported Data
    public void saveImportedResume(
            Long userProfileId,
            ResumeImportPreviewDTO preview,
            Callback<Void> callback) {

        Call<Void> call =
                apiService.saveImportedResume(userProfileId, preview);

        call.enqueue(callback);
    }

}
