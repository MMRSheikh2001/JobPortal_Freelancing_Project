package com.MMRSheikh2001.workbridgeandroid.masterdata.repository;

import android.content.Context;

import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.api.ApiService;
import com.MMRSheikh2001.workbridgeandroid.masterdata.response.PoliceStationResponseDTO;

import java.util.List;

import retrofit2.Callback;

public class PoliceStationRepository {

    private final ApiService apiService;

    public  PoliceStationRepository(Context context){
        apiService= ApiClient.getClient(context);
    }


    public void getAllPoliceStations(
            Callback<List<PoliceStationResponseDTO>> callback){

        apiService.getAllPoliceStations().enqueue(callback);
    }

    public void getPoliceStationsByDistrictId(
            Long districtId,
            Callback<List<PoliceStationResponseDTO>> callback){

        apiService.getPoliceStationsByDistrictId(districtId)
                .enqueue(callback);
    }


}
