package com.MMRSheikh2001.workbridgeandroid.cvinformations.repository;

import android.content.Context;

import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.api.ApiService;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.TrainingResponseDTO;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Callback;

public class TrainingRepository {


    private final ApiService apiService;

    public TrainingRepository(Context context) {
        apiService = ApiClient.getClient(context);
    }

    // Save
    public void saveTraining(
            RequestBody training,
            MultipartBody.Part file,
            Callback<TrainingResponseDTO> callback) {

        apiService.saveTraining(training, file)
                .enqueue(callback);
    }

    // Get By Id
    public void getTrainingById(
            Long id,
            Callback<TrainingResponseDTO> callback) {

        apiService.getTrainingById(id)
                .enqueue(callback);
    }

    // Update
    public void updateTraining(
            Long id,
            RequestBody training,
            MultipartBody.Part file,
            Callback<TrainingResponseDTO> callback) {

        apiService.updateTraining(id, training, file)
                .enqueue(callback);
    }

    // Delete
    public void deleteTraining(
            Long id,
            Callback<String> callback) {

        apiService.deleteTraining(id)
                .enqueue(callback);
    }

    // Delete File
    public void deleteTrainingFile(
            Long id,
            Callback<String> callback) {

        apiService.deleteTrainingFile(id)
                .enqueue(callback);
    }

    // Get By User Profile
    public void getTrainingsByUserProfileId(
            Long userProfileId,
            Callback<List<TrainingResponseDTO>> callback) {

        apiService.getTrainingsByUserProfileId(userProfileId)
                .enqueue(callback);
    }

    // Count
    public void countTrainingsByUserProfileId(
            Long userProfileId,
            Callback<Long> callback) {

        apiService.countTrainingsByUserProfileId(userProfileId)
                .enqueue(callback);
    }


}
