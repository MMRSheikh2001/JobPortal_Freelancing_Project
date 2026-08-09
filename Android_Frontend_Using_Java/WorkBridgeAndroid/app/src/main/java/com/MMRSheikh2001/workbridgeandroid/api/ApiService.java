package com.MMRSheikh2001.workbridgeandroid.api;

import com.MMRSheikh2001.workbridgeandroid.cvinformations.request.EducationRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.request.ExperienceRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.request.ExtracurricularRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.request.ReferenceRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.request.UserLanguageRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.request.UserSkillRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.EducationResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ExperienceResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ExtracurricularResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.PortfolioResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ReferenceResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ResumeFileResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ResumeImportPreviewDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ResumeResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.TrainingResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.UserLanguageResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.UserSkillResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.enums.NotificationType;
import com.MMRSheikh2001.workbridgeandroid.masterdata.response.CategoryResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.masterdata.response.CountryResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.masterdata.response.DistrictResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.masterdata.response.DivisionResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.masterdata.response.LanguageResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.masterdata.response.PoliceStationResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.masterdata.response.SkillResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.request.ForgotPasswordRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.request.JobApplicationRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.request.JobSearchRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.request.LoginRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.request.NotificationFilterDTO;
import com.MMRSheikh2001.workbridgeandroid.request.UserRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.response.AIInterviewSessionResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.JobApplicationResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.JobResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.LoginResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.NotificationResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.ResumeScreeningResult;
import com.MMRSheikh2001.workbridgeandroid.response.UserDashboardDTO;
import com.MMRSheikh2001.workbridgeandroid.response.UserProfileResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.UserResponseDTO;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface ApiService {


    //Register
    @POST("/api/users/register")
    Call<UserResponseDTO> register(@Body UserRequestDTO request);

    //Login
    @POST("api/auth/login")
    Call<LoginResponseDTO> login(@Body LoginRequestDTO request);

    //Forgot password
    @POST("api/auth/forgot-password")
    Call<String> forgotPassword(@Body ForgotPasswordRequestDTO request);


    //Dashboard
    @GET("api/dashboards/user/{userId}")
    Call<UserDashboardDTO> getUserDashboard(
            @Path("userId") Long userId
    );


    //=======
    //CV Informations
    //==========

    //UserProfile


    // Create Profile
    @Multipart
    @POST("api/userprofiles/")
    Call<UserProfileResponseDTO> saveUserProfile(
            @Part("userprofile") RequestBody userProfile,
            @Part MultipartBody.Part image
    );

    // Get By Id
    @GET("api/userprofiles/{id}")
    Call<UserProfileResponseDTO> getUserProfileById(
            @Path("id") Long id
    );

    // Get By User Id
    @GET("api/userprofiles/user/{userId}")
    Call<UserProfileResponseDTO> getUserProfileByUserId(
            @Path("userId") Long userId
    );

    // Update
    @Multipart
    @PUT("api/userprofiles/{id}")
    Call<UserProfileResponseDTO> updateUserProfile(
            @Path("id") Long id,
            @Part("userprofile") RequestBody userProfile,
            @Part MultipartBody.Part image
    );

    // Delete Profile
    @DELETE("api/userprofiles/{id}")
    Call<String> deleteUserProfile(
            @Path("id") Long id
    );

    // Delete Profile Image
    @DELETE("api/userprofiles/{id}/image")
    Call<String> deleteUserProfileImage(
            @Path("id") Long id
    );


    //Education


    // Save
    @POST("api/educations/")
    Call<EducationResponseDTO> saveEducation(
            @Body EducationRequestDTO request);

    // Get By Id
    @GET("api/educations/{id}")
    Call<EducationResponseDTO> getEducationById(
            @Path("id") Long id);

    // Update
    @PUT("api/educations/{id}")
    Call<EducationResponseDTO> updateEducation(
            @Path("id") Long id,
            @Body EducationRequestDTO request);

    // Delete
    @DELETE("api/educations/{id}")
    Call<String> deleteEducation(
            @Path("id") Long id);

    // Get All By User Profile
    @GET("api/educations/userprofile/{id}")
    Call<List<EducationResponseDTO>> getEducationsByUserProfileId(
            @Path("id") Long userProfileId);

    // Count By User Profile
    @GET("api/educations/userprofile/count/{id}")
    Call<Long> countEducationsByUserProfileId(
            @Path("id") Long userProfileId);

    //Experience


    // Save
    @POST("api/experiences/")
    Call<ExperienceResponseDTO> saveExperience(
            @Body ExperienceRequestDTO request);

    // Get By Id
    @GET("api/experiences/{id}")
    Call<ExperienceResponseDTO> getExperienceById(
            @Path("id") Long id);

    // Update
    @PUT("api/experiences/{id}")
    Call<ExperienceResponseDTO> updateExperience(
            @Path("id") Long id,
            @Body ExperienceRequestDTO request);

    // Delete
    @DELETE("api/experiences/{id}")
    Call<String> deleteExperience(
            @Path("id") Long id);

    // Get All By User Profile
    @GET("api/experiences/userprofile/{id}")
    Call<List<ExperienceResponseDTO>> getExperiencesByUserProfileId(
            @Path("id") Long userProfileId);

    // Count By User Profile
    @GET("api/experiences/userprofile/count/{id}")
    Call<Long> countExperiencesByUserProfileId(
            @Path("id") Long userProfileId);

    //Extracurricular


    // Save
    @POST("api/extracurriculars/")
    Call<ExtracurricularResponseDTO> saveExtracurricular(
            @Body ExtracurricularRequestDTO request);

    // Get By Id
    @GET("api/extracurriculars/{id}")
    Call<ExtracurricularResponseDTO> getExtracurricularById(
            @Path("id") Long id);

    // Update
    @PUT("api/extracurriculars/{id}")
    Call<ExtracurricularResponseDTO> updateExtracurricular(
            @Path("id") Long id,
            @Body ExtracurricularRequestDTO request);

    // Delete
    @DELETE("api/extracurriculars/{id}")
    Call<String> deleteExtracurricular(
            @Path("id") Long id);

    // Get All By User Profile
    @GET("api/extracurriculars/userprofile/{id}")
    Call<List<ExtracurricularResponseDTO>> getExtracurricularsByUserProfileId(
            @Path("id") Long userProfileId);

    // Count By User Profile
    @GET("api/extracurriculars/userprofile/count/{id}")
    Call<Long> countExtracurricularsByUserProfileId(
            @Path("id") Long userProfileId);


    //Portfolio


    // Save
    @Multipart
    @POST("api/portfolios/")
    Call<PortfolioResponseDTO> savePortfolio(
            @Part("portfolio") RequestBody portfolio,
            @Part MultipartBody.Part file);

    // Get By Id
    @GET("api/portfolios/{id}")
    Call<PortfolioResponseDTO> getPortfolioById(
            @Path("id") Long id);

    // Update
    @Multipart
    @PUT("api/portfolios/{id}")
    Call<PortfolioResponseDTO> updatePortfolio(
            @Path("id") Long id,
            @Part("portfolio") RequestBody portfolio,
            @Part MultipartBody.Part file);

    // Delete
    @DELETE("api/portfolios/{id}")
    Call<String> deletePortfolio(
            @Path("id") Long id);

    // Delete File
    @DELETE("api/portfolios/{id}/file")
    Call<String> deletePortfolioFile(
            @Path("id") Long id);

    // Get By User Profile
    @GET("api/portfolios/userprofile/{userProfileId}")
    Call<List<PortfolioResponseDTO>> getPortfoliosByUserProfileId(
            @Path("userProfileId") Long userProfileId);

    // Count
    @GET("api/portfolios/count/userprofile/{userProfileId}")
    Call<Long> countPortfoliosByUserProfileId(
            @Path("userProfileId") Long userProfileId);


    //Reference


    // Save
    @POST("api/references/")
    Call<ReferenceResponseDTO> saveReference(
            @Body ReferenceRequestDTO request);

    // Get By Id
    @GET("api/references/{id}")
    Call<ReferenceResponseDTO> getReferenceById(
            @Path("id") Long id);

    // Update
    @PUT("api/references/{id}")
    Call<ReferenceResponseDTO> updateReference(
            @Path("id") Long id,
            @Body ReferenceRequestDTO request);

    // Delete
    @DELETE("api/references/{id}")
    Call<String> deleteReference(
            @Path("id") Long id);

    // Get All By User Profile
    @GET("api/references/userprofile/{id}")
    Call<List<ReferenceResponseDTO>> getReferencesByUserProfileId(
            @Path("id") Long userProfileId);

    // Count By User Profile
    @GET("api/references/userprofile/count/{id}")
    Call<Long> countReferencesByUserProfileId(
            @Path("id") Long userProfileId);


    //Training


    // Save
    @Multipart
    @POST("api/trainings/")
    Call<TrainingResponseDTO> saveTraining(
            @Part("training") RequestBody training,
            @Part MultipartBody.Part file);

    // Get By Id
    @GET("api/trainings/{id}")
    Call<TrainingResponseDTO> getTrainingById(
            @Path("id") Long id);

    // Update
    @Multipart
    @PUT("api/trainings/{id}")
    Call<TrainingResponseDTO> updateTraining(
            @Path("id") Long id,
            @Part("training") RequestBody training,
            @Part MultipartBody.Part file);

    // Delete
    @DELETE("api/trainings/{id}")
    Call<String> deleteTraining(
            @Path("id") Long id);

    // Delete File
    @DELETE("api/trainings/{id}/file")
    Call<String> deleteTrainingFile(
            @Path("id") Long id);

    // Get By User Profile
    @GET("api/trainings/userprofile/{userProfileId}")
    Call<List<TrainingResponseDTO>> getTrainingsByUserProfileId(
            @Path("userProfileId") Long userProfileId);

    // Count
    @GET("api/trainings/count/userprofile/{userProfileId}")
    Call<Long> countTrainingsByUserProfileId(
            @Path("userProfileId") Long userProfileId);


    //User Language


    // Save
    @POST("api/userlanguages/")
    Call<UserLanguageResponseDTO> saveUserLanguage(
            @Body UserLanguageRequestDTO request);

    // Get By Id
    @GET("api/userlanguages/{id}")
    Call<UserLanguageResponseDTO> getUserLanguageById(
            @Path("id") Long id);

    // Update
    @PUT("api/userlanguages/{id}")
    Call<UserLanguageResponseDTO> updateUserLanguage(
            @Path("id") Long id,
            @Body UserLanguageRequestDTO request);

    // Delete
    @DELETE("api/userlanguages/{id}")
    Call<String> deleteUserLanguage(
            @Path("id") Long id);

    // Get By User Profile
    @GET("api/userlanguages/userprofile/{id}")
    Call<List<UserLanguageResponseDTO>> getUserLanguagesByUserProfileId(
            @Path("id") Long userProfileId);

    // Get By Language
    @GET("api/userlanguages/language/{id}")
    Call<List<UserLanguageResponseDTO>> getUserLanguagesByLanguageId(
            @Path("id") Long languageId);

    // Get By User Profile + Language
    @GET("api/userlanguages/userprofile/{userProfileId}/language/{languageId}")
    Call<UserLanguageResponseDTO> getUserLanguageByUserProfileAndLanguage(
            @Path("userProfileId") Long userProfileId,
            @Path("languageId") Long languageId);

    // Delete By User Profile + Language
    @DELETE("api/userlanguages/userprofile/{userProfileId}/language/{languageId}")
    Call<String> deleteUserLanguageByUserProfileAndLanguage(
            @Path("userProfileId") Long userProfileId,
            @Path("languageId") Long languageId);

    // Count
    @GET("api/userlanguages/userprofile/count/{userProfileId}")
    Call<Long> countUserLanguagesByUserProfileId(
            @Path("userProfileId") Long userProfileId);


    //User Skill

    // ==========================
// User Skill
// ==========================

    // Save
    @POST("api/userskills/")
    Call<UserSkillResponseDTO> saveUserSkill(
            @Body UserSkillRequestDTO request);

    // Get By Id
    @GET("api/userskills/{id}")
    Call<UserSkillResponseDTO> getUserSkillById(
            @Path("id") Long id);

    // Update
    @PUT("api/userskills/{id}")
    Call<UserSkillResponseDTO> updateUserSkill(
            @Path("id") Long id,
            @Body UserSkillRequestDTO request);

    // Delete
    @DELETE("api/userskills/{id}")
    Call<String> deleteUserSkill(
            @Path("id") Long id);

    // Get By User Profile
    @GET("api/userskills/userprofile/{id}")
    Call<List<UserSkillResponseDTO>> getUserSkillsByUserProfileId(
            @Path("id") Long userProfileId);

    // Get By Skill
    @GET("api/userskills/skill/{id}")
    Call<List<UserSkillResponseDTO>> getUserSkillsBySkillId(
            @Path("id") Long skillId);

    // Get By Skill Category
    @GET("api/userskills/skill/category/{id}")
    Call<List<UserSkillResponseDTO>> getUserSkillsBySkillCategoryId(
            @Path("id") Long categoryId);

    // Get By User Profile + Skill
    @GET("api/userskills/userprofile/{userProfileId}/skill/{skillId}")
    Call<UserSkillResponseDTO> getUserSkillByUserProfileAndSkill(
            @Path("userProfileId") Long userProfileId,
            @Path("skillId") Long skillId);

    // Delete By User Profile + Skill
    @DELETE("api/userskills/userprofile/{userProfileId}/skill/{skillId}")
    Call<String> deleteUserSkillByUserProfileAndSkill(
            @Path("userProfileId") Long userProfileId,
            @Path("skillId") Long skillId);

    // Count
    @GET("api/userskills/userprofile/count/{userProfileId}")
    Call<Long> countUserSkillsByUserProfileId(
            @Path("userProfileId") Long userProfileId);


    //Resume


    // Resume JSON
    @GET("api/resume/{userProfileId}")
    Call<ResumeResponseDTO> getResume(
            @Path("userProfileId") Long userProfileId);

    // Resume HTML
    @GET("api/resume/{userProfileId}/html")
    Call<ResponseBody> getResumeHtml(
            @Path("userProfileId") Long userProfileId);

    // Resume PDF
    @Streaming
    @GET("api/resume/{userProfileId}/pdf")
    Call<ResponseBody> getResumePdf(
            @Path("userProfileId") Long userProfileId);


    //Resume file


    // Upload Resume
    @Multipart
    @POST("api/resumes/uploadedfile/")
    Call<ResumeFileResponseDTO> uploadResume(
            @Query("userProfileId") Long userProfileId,
            @Part MultipartBody.Part cv);

    // Delete By Resume Id
    @DELETE("api/resumes/uploadedfile/{id}")
    Call<String> deleteResumeFile(
            @Path("id") Long id);

    // Get By User Profile
    @GET("api/resumes/uploadedfile/user/{userProfileId}")
    Call<ResumeFileResponseDTO> getResumeFileByUserProfileId(
            @Path("userProfileId") Long userProfileId);

    // Delete By User Profile
    @DELETE("api/resumes/uploadedfile/user/{userProfileId}")
    Call<String> deleteResumeFileByUserProfileId(
            @Path("userProfileId") Long userProfileId);

    // Exists
    @GET("api/resumes/uploadedfile/exists/{userProfileId}")
    Call<Boolean> resumeFileExists(
            @Path("userProfileId") Long userProfileId);

    // ==========================
// Resume Import (AI)
// ==========================

    // Get AI Preview (Nothing saved)
    @GET("api/resume-import/{userProfileId}")
    Call<ResumeImportPreviewDTO> getResumeImportPreview(
            @Path("userProfileId") Long userProfileId);

    // Save Imported Resume Data
    @POST("api/resume-import/save/{userProfileId}")
    Call<Void> saveImportedResume(
            @Path("userProfileId") Long userProfileId,
            @Body ResumeImportPreviewDTO preview);


    // ==========================
    // Job
    // ==========================

    @POST("api/jobs/search")
    Call<List<JobResponseDTO>> searchJobs(@Body JobSearchRequestDTO request);

    @GET("api/jobs/{id}")
    Call<JobResponseDTO> getJobById(@Path("id") Long id);


// Job Application
// ==========================

    // Apply Job
    @POST("api/jobapplications/")
    Call<JobApplicationResponseDTO> applyJob(
            @Body JobApplicationRequestDTO request);

    // Find By Id
    @GET("api/jobapplications/{id}")
    Call<JobApplicationResponseDTO> getJobApplicationById(
            @Path("id") Long id);

    // Find By User Profile
    @GET("api/jobapplications/userprofile/{userProfileId}")
    Call<List<JobApplicationResponseDTO>> getApplicationsByUserProfileId(
            @Path("userProfileId") Long userProfileId);

    // Withdraw Application
    @PATCH("api/jobapplications/withdraw/{applicationId}/userprofile/{userProfileId}")
    Call<JobApplicationResponseDTO> withdrawApplication(
            @Path("applicationId") Long applicationId,
            @Path("userProfileId") Long userProfileId);

    // Count Applications
    @GET("api/jobapplications/count/userprofile/{userProfileId}")
    Call<Long> countApplicationsByUserProfileId(
            @Path("userProfileId") Long userProfileId);

    // Check Already Applied
    @GET("api/jobapplications/exist/job/{jobId}/userprofile/{userProfileId}")
    Call<Boolean> existsApplication(
            @Path("jobId") Long jobId,
            @Path("userProfileId") Long userProfileId);

    // Find Application By Job + User
    @GET("api/jobapplications/job/{jobId}/userprofile/{userProfileId}")
    Call<JobApplicationResponseDTO> getApplicationByJobAndUser(
            @Path("jobId") Long jobId,
            @Path("userProfileId") Long userProfileId);


    // ==========================
// AI Interview
// ==========================

    @POST("api/ai/interview/start/{applicationId}")
    Call<AIInterviewSessionResponseDTO> startInterview(
            @Path("applicationId") Long applicationId);

    @POST("api/ai/interview/submit")
    Call<AIInterviewSessionResponseDTO> submitInterview(
            @Body AIInterviewSessionResponseDTO dto);

    @GET("api/ai/interview/{applicationId}")
    Call<AIInterviewSessionResponseDTO> getInterviewByApplicationId(
            @Path("applicationId") Long applicationId);


    //==========================================
// AI Job Match
//==========================================

    @GET("api/ai/interview/{jobId}/match/{userProfileId}")
    Call<ResumeScreeningResult> calculateJobMatch(
            @Path("jobId") Long jobId,
            @Path("userProfileId") Long userProfileId
    );


    //====================================================
// Notification
//====================================================

    // User notifications
    @GET("api/notifications/user/{userId}")
    Call<List<NotificationResponseDTO>> getUserNotifications(
            @Path("userId") Long userId);

    // By Id
    @GET("api/notifications/{notificationId}")
    Call<NotificationResponseDTO> getNotificationById(
            @Path("notificationId") Long notificationId);

    // Unread notifications
    @GET("api/notifications/user/{userId}/unread")
    Call<List<NotificationResponseDTO>> getUnreadNotifications(
            @Path("userId") Long userId);

    // Unread count
    @GET("api/notifications/user/{userId}/count")
    Call<Long> getUnreadNotificationCount(
            @Path("userId") Long userId);

    // Mark one as read
    @PUT("api/notifications/{notificationId}/read")
    Call<NotificationResponseDTO> markNotificationAsRead(
            @Path("notificationId") Long notificationId,
            @Query("userId") Long userId);

    // Mark all as read
    @PUT("api/notifications/user/{userId}/read-all")
    Call<Void> markAllNotificationsAsRead(
            @Path("userId") Long userId);

    // Filter by type
    @GET("api/notifications/user/{userId}/type/{type}")
    Call<List<NotificationResponseDTO>> getNotificationsByType(
            @Path("userId") Long userId,
            @Path("type") NotificationType type);

    // Delete one
    @DELETE("api/notifications/{notificationId}/clear")
    Call<Void> deleteNotification(
            @Path("notificationId") Long notificationId,
            @Query("userId") Long userId);

    // Delete all
    @DELETE("api/notifications/user/{userId}/clear")
    Call<Void> deleteAllNotifications(
            @Path("userId") Long userId);

    // Search (Admin)
    @POST("api/notifications/search")
    Call<List<NotificationResponseDTO>> searchNotifications(
            @Body NotificationFilterDTO filter);


    // ==============================
// Language
// ==============================

    @GET("api/languages/")
    Call<List<LanguageResponseDTO>> getAllLanguages();

    @GET("api/languages/{id}")
    Call<LanguageResponseDTO> getLanguageById(
            @Path("id") Long id);


    // Category
    @GET("api/categories/")
    Call<List<CategoryResponseDTO>> getAllCategories();

    @GET("api/categories/{id}")
    Call<CategoryResponseDTO> getCategoryById(
            @Path("id") Long id
    );

    // Skill
    @GET("api/skills/")
    Call<List<SkillResponseDTO>> getAllSkills();

    @GET("api/skills/{id}")
    Call<SkillResponseDTO> getSkillById(
            @Path("id") Long id
    );

    @GET("api/skills/category/{id}")
    Call<List<SkillResponseDTO>> getSkillsByCategoryId(@Path("id") Long id);

    // Country
    @GET("api/countries/")
    Call<List<CountryResponseDTO>> getAllCountries();

    // Division
    @GET("api/divisions/")
    Call<List<DivisionResponseDTO>> getAllDivisions();

    @GET("api/divisions/country/{id}")
    Call<List<DivisionResponseDTO>> getDivisionsByCountryId(@Path("id") Long id);

    // District
    @GET("api/districts/")
    Call<List<DistrictResponseDTO>> getAllDistricts();

    @GET("api/districts/division/{id}")
    Call<List<DistrictResponseDTO>> getDistrictsByDivisionId(@Path("id") Long id);

    // Police Station
    @GET("api/policestations/")
    Call<List<PoliceStationResponseDTO>> getAllPoliceStations();

    @GET("api/policestations/district/{id}")
    Call<List<PoliceStationResponseDTO>> getPoliceStationsByDistrictId(@Path("id") Long id);


}
