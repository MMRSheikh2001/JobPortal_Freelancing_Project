package com.MMRSheikh2001.workbridgeandroid.cvinformations.repository;

import android.content.Context;

import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.api.ApiService;
import com.MMRSheikh2001.workbridgeandroid.response.UserProfileResponseDTO;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Callback;

public class UserProfileRepository {


    private final ApiService apiService;

    public UserProfileRepository(Context context) {
        apiService = ApiClient.getClient(context);
    }


    // Save
    public void saveUserProfile(
            RequestBody userProfile,
            MultipartBody.Part image,
            Callback<UserProfileResponseDTO> callback) {

        apiService.saveUserProfile(
                userProfile,
                image
        ).enqueue(callback);
    }

    // Get By Id
    public void getUserProfileById(
            Long id,
            Callback<UserProfileResponseDTO> callback) {

        apiService.getUserProfileById(id)
                .enqueue(callback);
    }

    // Get By User Id
    public void getUserProfileByUserId(
            Long userId,
            Callback<UserProfileResponseDTO> callback) {

        apiService.getUserProfileByUserId(userId)
                .enqueue(callback);
    }

    // Update
    public void updateUserProfile(
            Long id,
            RequestBody userProfile,
            MultipartBody.Part image,
            Callback<UserProfileResponseDTO> callback) {

        apiService.updateUserProfile(
                id,
                userProfile,
                image
        ).enqueue(callback);
    }

    // Delete
    public void deleteUserProfile(
            Long id,
            Callback<String> callback) {

        apiService.deleteUserProfile(id)
                .enqueue(callback);
    }

    // Delete Image
    public void deleteUserProfileImage(
            Long id,
            Callback<String> callback) {

        apiService.deleteUserProfileImage(id)
                .enqueue(callback);
    }


}
