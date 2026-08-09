package com.MMRSheikh2001.workbridgeandroid.cvinformations.repository;

import android.content.Context;

import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.api.ApiService;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ResumeFileResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ResumeResponseDTO;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Callback;

public class ResumeRepository {


    private final ApiService apiService;

    public ResumeRepository(Context context) {
        apiService = ApiClient.getClient(context);
    }

    // Upload Resume
    public void uploadResume(
            Long userProfileId,
            MultipartBody.Part cv,
            Callback<ResumeFileResponseDTO> callback) {

        apiService.uploadResume(userProfileId, cv)
                .enqueue(callback);
    }

    // Delete By Resume Id
    public void deleteResumeFile(
            Long id,
            Callback<String> callback) {

        apiService.deleteResumeFile(id)
                .enqueue(callback);
    }

    // Get By User Profile
    public void getResumeFileByUserProfileId(
            Long userProfileId,
            Callback<ResumeFileResponseDTO> callback) {

        apiService.getResumeFileByUserProfileId(userProfileId)
                .enqueue(callback);
    }

    // Delete By User Profile
    public void deleteResumeFileByUserProfileId(
            Long userProfileId,
            Callback<String> callback) {

        apiService.deleteResumeFileByUserProfileId(userProfileId)
                .enqueue(callback);
    }

    // Exists
    public void resumeFileExists(
            Long userProfileId,
            Callback<Boolean> callback) {

        apiService.resumeFileExists(userProfileId)
                .enqueue(callback);
    }




    //======================================================
    // Resume JSON
    //======================================================

    public void getResume(
            Long userProfileId,
            Callback<ResumeResponseDTO> callback) {

        apiService.getResume(userProfileId)
                .enqueue(callback);
    }

    //======================================================
    // Resume HTML
    //======================================================

    public void getResumeHtml(
            Long userProfileId,
            Callback<ResponseBody> callback) {

        apiService.getResumeHtml(userProfileId)
                .enqueue(callback);
    }

    //======================================================
    // Resume PDF
    //======================================================

    public void downloadResumePdf(
            Long userProfileId,
            Callback<ResponseBody> callback) {

        apiService.getResumePdf(userProfileId)
                .enqueue(callback);
    }


}
