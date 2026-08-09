package com.MMRSheikh2001.workbridgeandroid.masterdata.repository;

import android.content.Context;

import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.api.ApiService;
import com.MMRSheikh2001.workbridgeandroid.masterdata.response.DivisionResponseDTO;

import java.util.List;

import retrofit2.Callback;

public class DivisionRepository {

    private final ApiService apiService;

    public  DivisionRepository(Context context){
        apiService= ApiClient.getClient(context);
    }

    public void getAllDivisions(
            Callback<List<DivisionResponseDTO>> callback){

        apiService.getAllDivisions().enqueue(callback);
    }

    public void getDivisionsByCountryId(
            Long countryId,
            Callback<List<DivisionResponseDTO>> callback){

        apiService.getDivisionsByCountryId(countryId)
                .enqueue(callback);
    }


}
