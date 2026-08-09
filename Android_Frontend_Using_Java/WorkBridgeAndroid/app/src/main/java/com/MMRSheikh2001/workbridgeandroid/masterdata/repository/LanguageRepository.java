package com.MMRSheikh2001.workbridgeandroid.masterdata.repository;

import android.content.Context;

import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.api.ApiService;
import com.MMRSheikh2001.workbridgeandroid.masterdata.response.LanguageResponseDTO;

import java.util.List;

import retrofit2.Callback;

public class LanguageRepository {


    private final ApiService apiService;

    public LanguageRepository(Context context) {
        apiService = ApiClient.getClient(context);
    }

    // Get All Languages
    public void getAllLanguages(
            Callback<List<LanguageResponseDTO>> callback) {

        apiService.getAllLanguages()
                .enqueue(callback);
    }

    // Get Language By Id
    public void getLanguageById(
            Long id,
            Callback<LanguageResponseDTO> callback) {

        apiService.getLanguageById(id)
                .enqueue(callback);
    }


}
