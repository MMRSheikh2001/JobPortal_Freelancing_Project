package com.MMRSheikh2001.workbridgeandroid.masterdata.repository;

import android.content.Context;

import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.api.ApiService;
import com.MMRSheikh2001.workbridgeandroid.masterdata.response.CategoryResponseDTO;

import java.util.List;

import retrofit2.Callback;

public class CategoryRepository {

    private final ApiService apiService;

    public  CategoryRepository(Context context){
        apiService= ApiClient.getClient(context);
    }

    public void getAllCategories(
            Callback<List<CategoryResponseDTO>> callback){

        apiService.getAllCategories().enqueue(callback);
    }

    public void getCategoryById(Long id,
            Callback<CategoryResponseDTO> callback){

        apiService.getCategoryById(id).enqueue(callback);
    }

}
