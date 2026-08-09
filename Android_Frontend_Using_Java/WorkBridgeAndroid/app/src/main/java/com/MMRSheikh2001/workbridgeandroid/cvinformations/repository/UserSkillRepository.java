package com.MMRSheikh2001.workbridgeandroid.cvinformations.repository;

import android.content.Context;

import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.api.ApiService;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.request.UserSkillRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.UserSkillResponseDTO;

import java.util.List;

import retrofit2.Callback;

public class UserSkillRepository {


    private final ApiService apiService;

    public UserSkillRepository(Context context) {
        apiService = ApiClient.getClient(context);
    }


    // Save
    public void saveUserSkill(
            UserSkillRequestDTO request,
            Callback<UserSkillResponseDTO> callback) {

        apiService.saveUserSkill(request)
                .enqueue(callback);
    }

    // Get By Id
    public void getUserSkillById(
            Long id,
            Callback<UserSkillResponseDTO> callback) {

        apiService.getUserSkillById(id)
                .enqueue(callback);
    }

    // Update
    public void updateUserSkill(
            Long id,
            UserSkillRequestDTO request,
            Callback<UserSkillResponseDTO> callback) {

        apiService.updateUserSkill(id, request)
                .enqueue(callback);
    }

    // Delete
    public void deleteUserSkill(
            Long id,
            Callback<String> callback) {

        apiService.deleteUserSkill(id)
                .enqueue(callback);
    }

    // Get By User Profile
    public void getUserSkillsByUserProfileId(
            Long userProfileId,
            Callback<List<UserSkillResponseDTO>> callback) {

        apiService.getUserSkillsByUserProfileId(userProfileId)
                .enqueue(callback);
    }

    // Get By Skill
    public void getUserSkillsBySkillId(
            Long skillId,
            Callback<List<UserSkillResponseDTO>> callback) {

        apiService.getUserSkillsBySkillId(skillId)
                .enqueue(callback);
    }

    // Get By Skill Category
    public void getUserSkillsBySkillCategoryId(
            Long categoryId,
            Callback<List<UserSkillResponseDTO>> callback) {

        apiService.getUserSkillsBySkillCategoryId(categoryId)
                .enqueue(callback);
    }

    // Get By User Profile + Skill
    public void getUserSkillByUserProfileAndSkill(
            Long userProfileId,
            Long skillId,
            Callback<UserSkillResponseDTO> callback) {

        apiService.getUserSkillByUserProfileAndSkill(
                userProfileId,
                skillId
        ).enqueue(callback);
    }

    // Delete By User Profile + Skill
    public void deleteUserSkillByUserProfileAndSkill(
            Long userProfileId,
            Long skillId,
            Callback<String> callback) {

        apiService.deleteUserSkillByUserProfileAndSkill(
                userProfileId,
                skillId
        ).enqueue(callback);
    }

    // Count
    public void countUserSkillsByUserProfileId(
            Long userProfileId,
            Callback<Long> callback) {

        apiService.countUserSkillsByUserProfileId(userProfileId)
                .enqueue(callback);
    }


}
