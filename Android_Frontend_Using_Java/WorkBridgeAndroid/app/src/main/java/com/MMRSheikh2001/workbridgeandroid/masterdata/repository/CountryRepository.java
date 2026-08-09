package com.MMRSheikh2001.workbridgeandroid.masterdata.repository;

import android.content.Context;

import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.api.ApiService;
import com.MMRSheikh2001.workbridgeandroid.masterdata.response.CountryResponseDTO;

import java.util.List;

import retrofit2.Callback;

public class CountryRepository {

    private final ApiService apiService;

    public  CountryRepository(Context context){
        apiService= ApiClient.getClient(context);
    }


    public void getAllCountries(
            Callback<List<CountryResponseDTO>> callback){

        apiService.getAllCountries().enqueue(callback);
    }


}
