package com.MMRSheikh2001.workbridgeandroid.repository;

import android.content.Context;

import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.api.ApiService;
import com.MMRSheikh2001.workbridgeandroid.request.JobSearchRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.response.JobResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.ResumeScreeningResult;

import java.util.List;

import retrofit2.Callback;


public class JobRepository {

    private final ApiService apiService;

    public JobRepository(Context context) {
        apiService = ApiClient.getClient(context);
    }

    public void searchJobs(JobSearchRequestDTO dto,
                           Callback<List<JobResponseDTO>> callback
    ) {
        apiService.searchJobs(dto).enqueue(callback);

    }

    public void getJobById(Long id, Callback<JobResponseDTO> callback) {
        apiService.getJobById(id).enqueue(callback);
    }


    //==========================================
// AI Job Match
//==========================================

    public void calculateJobMatch(
            Long jobId,
            Long userProfileId,
            Callback<ResumeScreeningResult> callback) {

        apiService.calculateJobMatch(
                jobId,
                userProfileId
        ).enqueue(callback);
    }


}
