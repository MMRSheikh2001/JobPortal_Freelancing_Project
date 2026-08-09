package com.MMRSheikh2001.workbridgeandroid.masterdata.repository;

import android.content.Context;

import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.api.ApiService;
import com.MMRSheikh2001.workbridgeandroid.masterdata.response.DistrictResponseDTO;

import java.util.List;

import retrofit2.Callback;

public class DistrictRepository {

    private final ApiService apiService;

    public  DistrictRepository(Context context){
        apiService= ApiClient.getClient(context);
    }

    public void getAllDistricts(
            Callback<List<DistrictResponseDTO>> callback){

        apiService.getAllDistricts().enqueue(callback);
    }

    public void getDistrictsByDivisionId(
            Long divisionId,
            Callback<List<DistrictResponseDTO>> callback){

        apiService.getDistrictsByDivisionId(divisionId)
                .enqueue(callback);
    }



}
