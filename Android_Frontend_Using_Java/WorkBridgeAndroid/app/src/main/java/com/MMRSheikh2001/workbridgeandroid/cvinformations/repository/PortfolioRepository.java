package com.MMRSheikh2001.workbridgeandroid.cvinformations.repository;

import android.content.Context;

import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.api.ApiService;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.PortfolioResponseDTO;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Callback;

public class PortfolioRepository {

    private final ApiService apiService;

    public PortfolioRepository(Context context) {
        apiService = ApiClient.getClient(context);
    }


    // Save
    public void savePortfolio(
            RequestBody portfolio,
            MultipartBody.Part file,
            Callback<PortfolioResponseDTO> callback) {

        apiService.savePortfolio(portfolio, file)
                .enqueue(callback);
    }

    // Get By Id
    public void getPortfolioById(
            Long id,
            Callback<PortfolioResponseDTO> callback) {

        apiService.getPortfolioById(id)
                .enqueue(callback);
    }

    // Update
    public void updatePortfolio(
            Long id,
            RequestBody portfolio,
            MultipartBody.Part file,
            Callback<PortfolioResponseDTO> callback) {

        apiService.updatePortfolio(id, portfolio, file)
                .enqueue(callback);
    }

    // Delete
    public void deletePortfolio(
            Long id,
            Callback<String> callback) {

        apiService.deletePortfolio(id)
                .enqueue(callback);
    }

    // Delete File
    public void deletePortfolioFile(
            Long id,
            Callback<String> callback) {

        apiService.deletePortfolioFile(id)
                .enqueue(callback);
    }

    // Get By User Profile
    public void getPortfoliosByUserProfileId(
            Long userProfileId,
            Callback<List<PortfolioResponseDTO>> callback) {

        apiService.getPortfoliosByUserProfileId(userProfileId)
                .enqueue(callback);
    }

    // Count
    public void countPortfoliosByUserProfileId(
            Long userProfileId,
            Callback<Long> callback) {

        apiService.countPortfoliosByUserProfileId(userProfileId)
                .enqueue(callback);
    }


}
