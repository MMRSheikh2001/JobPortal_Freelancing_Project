package com.MMRSheikh2001.workbridgeandroid.repository;

import android.content.Context;

import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.api.ApiService;
import com.MMRSheikh2001.workbridgeandroid.request.JobApplicationRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.response.AIInterviewSessionResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.JobApplicationResponseDTO;

import java.util.List;

import retrofit2.Callback;

public class JobApplicationRepository {

    private final ApiService apiService;

    public JobApplicationRepository(Context context) {
        apiService = ApiClient.getClient(context);
    }


    // Apply Job
    public void applyJob(
            JobApplicationRequestDTO request,
            Callback<JobApplicationResponseDTO> callback) {

        apiService.applyJob(request)
                .enqueue(callback);
    }

    // Find Application By Id
    public void getJobApplicationById(
            Long applicationId,
            Callback<JobApplicationResponseDTO> callback) {

        apiService.getJobApplicationById(applicationId)
                .enqueue(callback);
    }

    // Find All Applications of User
    public void getApplicationsByUserProfileId(
            Long userProfileId,
            Callback<List<JobApplicationResponseDTO>> callback) {

        apiService.getApplicationsByUserProfileId(userProfileId)
                .enqueue(callback);
    }

    // Withdraw Application
    public void withdrawApplication(
            Long applicationId,
            Long userProfileId,
            Callback<JobApplicationResponseDTO> callback) {

        apiService.withdrawApplication(
                        applicationId,
                        userProfileId)
                .enqueue(callback);
    }

    // Count Applications
    public void countApplicationsByUserProfileId(
            Long userProfileId,
            Callback<Long> callback) {

        apiService.countApplicationsByUserProfileId(userProfileId)
                .enqueue(callback);
    }

    // Check Already Applied
    public void existsApplication(
            Long jobId,
            Long userProfileId,
            Callback<Boolean> callback) {

        apiService.existsApplication(
                        jobId,
                        userProfileId)
                .enqueue(callback);
    }

    // Find Application By Job + User
    public void getApplicationByJobAndUser(
            Long jobId,
            Long userProfileId,
            Callback<JobApplicationResponseDTO> callback) {

        apiService.getApplicationByJobAndUser(
                        jobId,
                        userProfileId)
                .enqueue(callback);
    }



    //AI interview


    public void startInterview(
            Long applicationId,
            Callback<AIInterviewSessionResponseDTO> callback) {

        apiService.startInterview(applicationId)
                .enqueue(callback);
    }

    public void submitInterview(
            AIInterviewSessionResponseDTO dto,
            Callback<AIInterviewSessionResponseDTO> callback) {

        apiService.submitInterview(dto)
                .enqueue(callback);
    }

    public void getInterviewByApplicationId(
            Long applicationId,
            Callback<AIInterviewSessionResponseDTO> callback) {

        apiService.getInterviewByApplicationId(applicationId)
                .enqueue(callback);
    }

}
