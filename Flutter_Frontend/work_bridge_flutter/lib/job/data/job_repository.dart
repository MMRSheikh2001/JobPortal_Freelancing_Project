import 'package:dio/dio.dart';
import 'package:work_bridge_flutter/job/entity/request/job_application_request.dart';
import 'package:work_bridge_flutter/job/entity/request/job_search_request.dart';
import 'package:work_bridge_flutter/job/entity/response/ai_interview_session_response.dart';
import 'package:work_bridge_flutter/job/entity/response/job_application_response.dart';
import 'package:work_bridge_flutter/job/entity/response/job_response.dart';
import 'package:work_bridge_flutter/job/entity/response/resume_screening_result.dart';
import 'package:work_bridge_flutter/utils/api_client.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';

class JobRepository {
  JobRepository(this._apiClient);

  final ApiClient _apiClient;

  Dio get _dio => _apiClient.dio;

  // =====================================================
  // Jobs
  // =====================================================

  Future<List<JobResponseDTO>> searchJobs(JobSearchRequestDTO request) async {
    final response = await _dio.post(
      ApiConstants.searchJobs,
      data: request.toJson(),
    );

    return (response.data as List)
        .map((json) => JobResponseDTO.fromJson(json))
        .toList();
  }

  Future<JobResponseDTO> getJobById(int id) async {
    final response = await _dio.get(ApiConstants.getJobUrl(id));

    return JobResponseDTO.fromJson(response.data);
  }

  // =====================================================
  // Job Application
  // =====================================================

  Future<JobApplicationResponseDTO> applyJob(
    JobApplicationRequestDTO request,
  ) async {
    final response = await _dio.post(
      ApiConstants.jobApplications,
      data: request.toJson(),
    );

    return JobApplicationResponseDTO.fromJson(response.data);
  }

  Future<JobApplicationResponseDTO> getJobApplicationById(int id) async {
    final response = await _dio.get(ApiConstants.jobApplicationById(id));

    return JobApplicationResponseDTO.fromJson(response.data);
  }

  Future<List<JobApplicationResponseDTO>> getApplicationsByUserProfileId(
    int userProfileId,
  ) async {
    final response = await _dio.get(
      ApiConstants.applicationsByUserProfile(userProfileId),
    );

    return (response.data as List)
        .map((json) => JobApplicationResponseDTO.fromJson(json))
        .toList();
  }

  Future<JobApplicationResponseDTO> withdrawApplication(
    int applicationId,
    int userProfileId,
  ) async {
    final response = await _dio.patch(
      ApiConstants.withdrawApplication(applicationId, userProfileId),
    );

    return JobApplicationResponseDTO.fromJson(response.data);
  }

  Future<int> countApplications(int userProfileId) async {
    final response = await _dio.get(
      ApiConstants.applicationCount(userProfileId),
    );

    return (response.data as num).toInt();
  }

  Future<bool> existsApplication(int jobId, int userProfileId) async {
    final response = await _dio.get(
      ApiConstants.applicationExists(jobId, userProfileId),
    );

    return response.data as bool;
  }

  Future<JobApplicationResponseDTO> getApplicationByJobAndUser(
    int jobId,
    int userProfileId,
  ) async {
    final response = await _dio.get(
      ApiConstants.applicationByJobAndUser(jobId, userProfileId),
    );

    return JobApplicationResponseDTO.fromJson(response.data);
  }

  // =====================================================
  // AI Interview
  // =====================================================

  Future<AIInterviewSessionResponseDTO> startInterview(
    int applicationId,
  ) async {
    final response = await _dio.post(
      ApiConstants.startInterview(applicationId),
    );

    return AIInterviewSessionResponseDTO.fromJson(response.data);
  }

  Future<AIInterviewSessionResponseDTO> submitInterview(
    AIInterviewSessionResponseDTO data,
  ) async {
    final response = await _dio.post(
      ApiConstants.submitInterview,
      data: data.toJson(),
    );

    return AIInterviewSessionResponseDTO.fromJson(response.data);
  }

  Future<AIInterviewSessionResponseDTO> getInterviewByApplicationId(
    int applicationId,
  ) async {
    final response = await _dio.get(
      ApiConstants.interviewByApplication(applicationId),
    );

    return AIInterviewSessionResponseDTO.fromJson(response.data);
  }

  // =====================================================
  // AI Job Match
  // =====================================================

  Future<ResumeScreeningResult> calculateJobMatch(
    int jobId,
    int userProfileId,
  ) async {
    final response = await _dio.get(
      ApiConstants.getAiJobMatchUrl(jobId, userProfileId),
    );

    return ResumeScreeningResult.fromJson(response.data);
  }
}
