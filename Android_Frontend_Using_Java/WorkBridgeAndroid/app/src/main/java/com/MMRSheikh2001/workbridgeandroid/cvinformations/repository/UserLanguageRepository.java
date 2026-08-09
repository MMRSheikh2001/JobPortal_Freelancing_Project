package com.MMRSheikh2001.workbridgeandroid.cvinformations.repository;

import android.content.Context;

import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.api.ApiService;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.request.UserLanguageRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.UserLanguageResponseDTO;

import java.util.List;

import retrofit2.Callback;

public class UserLanguageRepository {


    private final ApiService apiService;

    public UserLanguageRepository(Context context) {
        apiService = ApiClient.getClient(context);
    }


    // Save
    public void saveUserLanguage(
            UserLanguageRequestDTO request,
            Callback<UserLanguageResponseDTO> callback) {

        apiService.saveUserLanguage(request)
                .enqueue(callback);
    }

    // Get By Id
    public void getUserLanguageById(
            Long id,
            Callback<UserLanguageResponseDTO> callback) {

        apiService.getUserLanguageById(id)
                .enqueue(callback);
    }

    // Update
    public void updateUserLanguage(
            Long id,
            UserLanguageRequestDTO request,
            Callback<UserLanguageResponseDTO> callback) {

        apiService.updateUserLanguage(id, request)
                .enqueue(callback);
    }

    // Delete
    public void deleteUserLanguage(
            Long id,
            Callback<String> callback) {

        apiService.deleteUserLanguage(id)
                .enqueue(callback);
    }

    // Get By User Profile
    public void getUserLanguagesByUserProfileId(
            Long userProfileId,
            Callback<List<UserLanguageResponseDTO>> callback) {

        apiService.getUserLanguagesByUserProfileId(userProfileId)
                .enqueue(callback);
    }

    // Get By Language
    public void getUserLanguagesByLanguageId(
            Long languageId,
            Callback<List<UserLanguageResponseDTO>> callback) {

        apiService.getUserLanguagesByLanguageId(languageId)
                .enqueue(callback);
    }

    // Get By User Profile + Language
    public void getUserLanguageByUserProfileAndLanguage(
            Long userProfileId,
            Long languageId,
            Callback<UserLanguageResponseDTO> callback) {

        apiService.getUserLanguageByUserProfileAndLanguage(
                userProfileId,
                languageId
        ).enqueue(callback);
    }

    // Delete By User Profile + Language
    public void deleteUserLanguageByUserProfileAndLanguage(
            Long userProfileId,
            Long languageId,
            Callback<String> callback) {

        apiService.deleteUserLanguageByUserProfileAndLanguage(
                userProfileId,
                languageId
        ).enqueue(callback);
    }

    // Count
    public void countUserLanguagesByUserProfileId(
            Long userProfileId,
            Callback<Long> callback) {

        apiService.countUserLanguagesByUserProfileId(userProfileId)
                .enqueue(callback);
    }


}
