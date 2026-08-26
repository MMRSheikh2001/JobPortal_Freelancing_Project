import 'dart:io';
import 'dart:typed_data';

import 'package:dio/dio.dart';
import 'package:work_bridge_flutter/cvinformations/models/request/education_request.dart';
import 'package:work_bridge_flutter/cvinformations/models/request/experience_request.dart';
import 'package:work_bridge_flutter/cvinformations/models/request/extracurricular_request.dart';
import 'package:work_bridge_flutter/cvinformations/models/request/portfolio_request.dart';
import 'package:work_bridge_flutter/cvinformations/models/request/reference_request.dart';
import 'package:work_bridge_flutter/cvinformations/models/request/training_request.dart';
import 'package:work_bridge_flutter/cvinformations/models/request/user_language_request.dart';
import 'package:work_bridge_flutter/cvinformations/models/request/user_profile_request.dart';
import 'package:work_bridge_flutter/cvinformations/models/request/user_skill_request.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/education_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/experience_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/extracurricular_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/portfolio_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/reference_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/resume_file_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/resume_import_preview.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/resume_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/training_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/user_language_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/user_profile_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/user_skill_response.dart';
import 'package:work_bridge_flutter/utils/api_client.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';

class CvRepository {
  CvRepository(this._apiClient);

  final ApiClient _apiClient;

  Dio get _dio => _apiClient.dio;

  // =====================================================
  // User Profile
  // =====================================================

  Future<UserProfileResponseDTO> saveUserProfile(
    UserProfileRequestDTO request,
    File? imageFile,
  ) async {
    final formData = FormData();

    // User profile JSON
    formData.files.add(
      MapEntry(
        'userprofile',
        MultipartFile.fromString(
          request.toJsonString(),
          contentType: DioMediaType('application', 'json'),
        ),
      ),
    );

    // Profile image - optional
    if (imageFile != null) {
      formData.files.add(
        MapEntry(
          'image',
          await MultipartFile.fromFile(
            imageFile.path,
            filename: imageFile.path.split(Platform.pathSeparator).last,
          ),
        ),
      );
    }

    final response = await _dio.post(ApiConstants.userProfiles, data: formData);

    return UserProfileResponseDTO.fromJson(response.data);
  }

  Future<UserProfileResponseDTO> getUserProfileById(int id) async {
    final response = await _dio.get(ApiConstants.userProfileById(id));

    return UserProfileResponseDTO.fromJson(response.data);
  }

  Future<UserProfileResponseDTO> getUserProfileByUserId(int userId) async {
    final response = await _dio.get(ApiConstants.userProfileByUserId(userId));

    return UserProfileResponseDTO.fromJson(response.data);
  }

  Future<UserProfileResponseDTO> updateUserProfile(
    int id,
    UserProfileRequestDTO request,
    File? imageFile,
  ) async {
    final formData = FormData();

    // User profile JSON
    formData.files.add(
      MapEntry(
        'userprofile',
        MultipartFile.fromString(
          request.toJsonString(),
          contentType: DioMediaType('application', 'json'),
        ),
      ),
    );

    // Profile image - optional
    if (imageFile != null) {
      formData.files.add(
        MapEntry(
          'image',
          await MultipartFile.fromFile(
            imageFile.path,
            filename: imageFile.path.split(Platform.pathSeparator).last,
          ),
        ),
      );
    }

    final response = await _dio.put(
      ApiConstants.userProfileById(id),
      data: formData,
    );

    return UserProfileResponseDTO.fromJson(response.data);
  }

  Future<String> deleteUserProfile(int id) async {
    final response = await _dio.delete(ApiConstants.userProfileById(id));

    return response.data.toString();
  }

  Future<String> deleteUserProfileImage(int id) async {
    final response = await _dio.delete(ApiConstants.deleteUserProfileImage(id));

    return response.data.toString();
  }

  // =====================================================
  // Education
  // =====================================================

  Future<EducationResponseDTO> saveEducation(
    EducationRequestDTO request,
  ) async {
    final response = await _dio.post(
      ApiConstants.educations,
      data: request.toJson(),
    );

    return EducationResponseDTO.fromJson(response.data);
  }

  Future<EducationResponseDTO> getEducationById(int id) async {
    final response = await _dio.get(ApiConstants.educationById(id));

    return EducationResponseDTO.fromJson(response.data);
  }

  Future<EducationResponseDTO> updateEducation(
    int id,
    EducationRequestDTO request,
  ) async {
    final response = await _dio.put(
      ApiConstants.educationById(id),
      data: request.toJson(),
    );

    return EducationResponseDTO.fromJson(response.data);
  }

  Future<String> deleteEducation(int id) async {
    final response = await _dio.delete(ApiConstants.educationById(id));

    return response.data.toString();
  }

  Future<List<EducationResponseDTO>> getEducationsByUserProfileId(
    int userProfileId,
  ) async {
    final response = await _dio.get(
      ApiConstants.educationsByUserProfile(userProfileId),
    );

    return (response.data as List)
        .map((json) => EducationResponseDTO.fromJson(json))
        .toList();
  }

  Future<int> countEducationsByUserProfileId(int userProfileId) async {
    final response = await _dio.get(ApiConstants.educationCount(userProfileId));

    return (response.data as num).toInt();
  }

  // =====================================================
  // Experience
  // =====================================================

  Future<ExperienceResponseDTO> saveExperience(
    ExperienceRequestDTO request,
  ) async {
    final response = await _dio.post(
      ApiConstants.experiences,
      data: request.toJson(),
    );

    return ExperienceResponseDTO.fromJson(response.data);
  }

  Future<ExperienceResponseDTO> getExperienceById(int id) async {
    final response = await _dio.get(ApiConstants.experienceById(id));

    return ExperienceResponseDTO.fromJson(response.data);
  }

  Future<ExperienceResponseDTO> updateExperience(
    int id,
    ExperienceRequestDTO request,
  ) async {
    final response = await _dio.put(
      ApiConstants.experienceById(id),
      data: request.toJson(),
    );

    return ExperienceResponseDTO.fromJson(response.data);
  }

  Future<String> deleteExperience(int id) async {
    final response = await _dio.delete(ApiConstants.experienceById(id));

    return response.data.toString();
  }

  Future<List<ExperienceResponseDTO>> getExperiencesByUserProfileId(
    int userProfileId,
  ) async {
    final response = await _dio.get(
      ApiConstants.experiencesByUserProfile(userProfileId),
    );

    return (response.data as List)
        .map((json) => ExperienceResponseDTO.fromJson(json))
        .toList();
  }

  Future<int> countExperiencesByUserProfileId(int userProfileId) async {
    final response = await _dio.get(
      ApiConstants.experienceCount(userProfileId),
    );

    return (response.data as num).toInt();
  }

  // =====================================================
  // Extracurricular
  // =====================================================

  Future<ExtracurricularResponseDTO> saveExtracurricular(
    ExtracurricularRequestDTO request,
  ) async {
    final response = await _dio.post(
      ApiConstants.extracurriculars,
      data: request.toJson(),
    );

    return ExtracurricularResponseDTO.fromJson(response.data);
  }

  Future<ExtracurricularResponseDTO> getExtracurricularById(int id) async {
    final response = await _dio.get(ApiConstants.extracurricularById(id));

    return ExtracurricularResponseDTO.fromJson(response.data);
  }

  Future<ExtracurricularResponseDTO> updateExtracurricular(
    int id,
    ExtracurricularRequestDTO request,
  ) async {
    final response = await _dio.put(
      ApiConstants.extracurricularById(id),
      data: request.toJson(),
    );

    return ExtracurricularResponseDTO.fromJson(response.data);
  }

  Future<String> deleteExtracurricular(int id) async {
    final response = await _dio.delete(ApiConstants.extracurricularById(id));

    return response.data.toString();
  }

  Future<List<ExtracurricularResponseDTO>> getExtracurricularsByUserProfileId(
    int userProfileId,
  ) async {
    final response = await _dio.get(
      ApiConstants.extracurricularsByUserProfile(userProfileId),
    );

    return (response.data as List)
        .map((json) => ExtracurricularResponseDTO.fromJson(json))
        .toList();
  }

  Future<int> countExtracurricularsByUserProfileId(int userProfileId) async {
    final response = await _dio.get(
      ApiConstants.extracurricularCount(userProfileId),
    );

    return (response.data as num).toInt();
  }

  // =====================================================
  // Portfolio
  // =====================================================

  Future<PortfolioResponseDTO> savePortfolio(
    PortfolioRequestDTO request,
    File? file, {
    Uint8List? bytes,
    String? fileName,
  }) async {
    final formData = FormData();

    // Portfolio JSON
    formData.files.add(
      MapEntry(
        'portfolio',
        MultipartFile.fromString(
          request.toJsonString(),
          contentType: DioMediaType('application', 'json'),
        ),
      ),
    );

    // Portfolio file - optional
    if (file != null) {
      formData.files.add(
        MapEntry(
          'file',
          await MultipartFile.fromFile(
            file.path,
            filename: file.path.split(Platform.pathSeparator).last,
          ),
        ),
      );
    } else if (bytes != null) {
      formData.files.add(
        MapEntry(
          'file',
          MultipartFile.fromBytes(
            bytes,
            filename: fileName ?? 'portfolio_file',
          ),
        ),
      );
    }

    final response = await _dio.post(ApiConstants.portfolios, data: formData);

    return PortfolioResponseDTO.fromJson(response.data);
  }

  Future<PortfolioResponseDTO> getPortfolioById(int id) async {
    final response = await _dio.get(ApiConstants.portfolioById(id));

    return PortfolioResponseDTO.fromJson(response.data);
  }

  Future<PortfolioResponseDTO> updatePortfolio(
    int id,
    PortfolioRequestDTO request,
    File? file, {
    Uint8List? bytes,
    String? fileName,
  }) async {
    final formData = FormData();

    // Portfolio JSON
    formData.files.add(
      MapEntry(
        'portfolio',
        MultipartFile.fromString(
          request.toJsonString(),
          contentType: DioMediaType('application', 'json'),
        ),
      ),
    );

    // Portfolio file - optional
    if (file != null) {
      formData.files.add(
        MapEntry(
          'file',
          await MultipartFile.fromFile(
            file.path,
            filename: file.path.split(Platform.pathSeparator).last,
          ),
        ),
      );
    } else if (bytes != null) {
      formData.files.add(
        MapEntry(
          'file',
          MultipartFile.fromBytes(
            bytes,
            filename: fileName ?? 'portfolio_file',
          ),
        ),
      );
    }

    final response = await _dio.put(
      ApiConstants.portfolioById(id),
      data: formData,
    );

    return PortfolioResponseDTO.fromJson(response.data);
  }

  Future<String> deletePortfolio(int id) async {
    final response = await _dio.delete(ApiConstants.portfolioById(id));

    return response.data.toString();
  }

  Future<String> deletePortfolioFile(int id) async {
    final response = await _dio.delete(ApiConstants.deletePortfolioFile(id));

    return response.data.toString();
  }

  Future<List<PortfolioResponseDTO>> getPortfoliosByUserProfileId(
    int userProfileId,
  ) async {
    final response = await _dio.get(
      ApiConstants.portfoliosByUserProfile(userProfileId),
    );

    return (response.data as List)
        .map((json) => PortfolioResponseDTO.fromJson(json))
        .toList();
  }

  Future<int> countPortfoliosByUserProfileId(int userProfileId) async {
    final response = await _dio.get(ApiConstants.portfolioCount(userProfileId));

    return (response.data as num).toInt();
  }

  // =====================================================
  // Reference
  // =====================================================

  Future<ReferenceResponseDTO> saveReference(
    ReferenceRequestDTO request,
  ) async {
    final response = await _dio.post(
      ApiConstants.references,
      data: request.toJson(),
    );

    return ReferenceResponseDTO.fromJson(response.data);
  }

  Future<ReferenceResponseDTO> getReferenceById(int id) async {
    final response = await _dio.get(ApiConstants.referenceById(id));

    return ReferenceResponseDTO.fromJson(response.data);
  }

  Future<ReferenceResponseDTO> updateReference(
    int id,
    ReferenceRequestDTO request,
  ) async {
    final response = await _dio.put(
      ApiConstants.referenceById(id),
      data: request.toJson(),
    );

    return ReferenceResponseDTO.fromJson(response.data);
  }

  Future<String> deleteReference(int id) async {
    final response = await _dio.delete(ApiConstants.referenceById(id));

    return response.data.toString();
  }

  Future<List<ReferenceResponseDTO>> getReferencesByUserProfileId(
    int userProfileId,
  ) async {
    final response = await _dio.get(
      ApiConstants.referencesByUserProfile(userProfileId),
    );

    return (response.data as List)
        .map((json) => ReferenceResponseDTO.fromJson(json))
        .toList();
  }

  Future<int> countReferencesByUserProfileId(int userProfileId) async {
    final response = await _dio.get(ApiConstants.referenceCount(userProfileId));

    return (response.data as num).toInt();
  }

  // =====================================================
  // Training
  // =====================================================

  Future<TrainingResponseDTO> saveTraining(
    TrainingRequestDTO request,
    File? file, {
    Uint8List? bytes,
    String? fileName,
  }) async {
    final formData = FormData.fromMap({
      'training': MultipartFile.fromString(
        request.toJsonString(),
        contentType: DioMediaType('application', 'json'),
      ),
    });

    if (file != null) {
      formData.files.add(
        MapEntry(
          'file',
          await MultipartFile.fromFile(
            file.path,
            filename: file.path.split(Platform.pathSeparator).last,
          ),
        ),
      );
    } else if (bytes != null) {
      formData.files.add(
        MapEntry(
          'file',
          MultipartFile.fromBytes(bytes, filename: fileName ?? 'certificate'),
        ),
      );
    }

    final response = await _dio.post(ApiConstants.trainings, data: formData);

    return TrainingResponseDTO.fromJson(response.data);
  }

  Future<TrainingResponseDTO> getTrainingById(int id) async {
    final response = await _dio.get(ApiConstants.trainingById(id));

    return TrainingResponseDTO.fromJson(response.data);
  }

  Future<TrainingResponseDTO> updateTraining(
    int id,
    TrainingRequestDTO request,
    File? file, {
    Uint8List? bytes,
    String? fileName,
  }) async {
    final formData = FormData.fromMap({
      'training': MultipartFile.fromString(
        request.toJsonString(),
        contentType: DioMediaType('application', 'json'),
      ),
    });

    if (file != null) {
      formData.files.add(
        MapEntry(
          'file',
          await MultipartFile.fromFile(
            file.path,
            filename: file.path.split(Platform.pathSeparator).last,
          ),
        ),
      );
    } else if (bytes != null) {
      formData.files.add(
        MapEntry(
          'file',
          MultipartFile.fromBytes(bytes, filename: fileName ?? 'certificate'),
        ),
      );
    }

    final response = await _dio.put(
      ApiConstants.trainingById(id),
      data: formData,
    );

    return TrainingResponseDTO.fromJson(response.data);
  }

  Future<String> deleteTraining(int id) async {
    final response = await _dio.delete(ApiConstants.trainingById(id));

    return response.data.toString();
  }

  Future<String> deleteTrainingFile(int id) async {
    final response = await _dio.delete(ApiConstants.deleteTrainingFile(id));

    return response.data.toString();
  }

  Future<List<TrainingResponseDTO>> getTrainingsByUserProfileId(
    int userProfileId,
  ) async {
    final response = await _dio.get(
      ApiConstants.trainingsByUserProfile(userProfileId),
    );

    return (response.data as List)
        .map((json) => TrainingResponseDTO.fromJson(json))
        .toList();
  }

  Future<int> countTrainingsByUserProfileId(int userProfileId) async {
    final response = await _dio.get(ApiConstants.trainingCount(userProfileId));

    return (response.data as num).toInt();
  }

  // =====================================================
  // User Language
  // =====================================================

  Future<UserLanguageResponseDTO> saveUserLanguage(
    UserLanguageRequestDTO request,
  ) async {
    final response = await _dio.post(
      ApiConstants.userLanguages,
      data: request.toJson(),
    );

    return UserLanguageResponseDTO.fromJson(response.data);
  }

  Future<UserLanguageResponseDTO> getUserLanguageById(int id) async {
    final response = await _dio.get(ApiConstants.userLanguageById(id));

    return UserLanguageResponseDTO.fromJson(response.data);
  }

  Future<UserLanguageResponseDTO> updateUserLanguage(
    int id,
    UserLanguageRequestDTO request,
  ) async {
    final response = await _dio.put(
      ApiConstants.userLanguageById(id),
      data: request.toJson(),
    );

    return UserLanguageResponseDTO.fromJson(response.data);
  }

  Future<String> deleteUserLanguage(int id) async {
    final response = await _dio.delete(ApiConstants.userLanguageById(id));

    return response.data.toString();
  }

  Future<List<UserLanguageResponseDTO>> getUserLanguagesByUserProfileId(
    int userProfileId,
  ) async {
    final response = await _dio.get(
      ApiConstants.userLanguagesByUserProfile(userProfileId),
    );

    return (response.data as List)
        .map((json) => UserLanguageResponseDTO.fromJson(json))
        .toList();
  }

  Future<List<UserLanguageResponseDTO>> getUserLanguagesByLanguageId(
    int languageId,
  ) async {
    final response = await _dio.get(
      ApiConstants.userLanguagesByLanguage(languageId),
    );

    return (response.data as List)
        .map((json) => UserLanguageResponseDTO.fromJson(json))
        .toList();
  }

  Future<UserLanguageResponseDTO> getUserLanguageByUserProfileAndLanguage(
    int userProfileId,
    int languageId,
  ) async {
    final response = await _dio.get(
      ApiConstants.userLanguageByUserProfileAndLanguage(
        userProfileId,
        languageId,
      ),
    );

    return UserLanguageResponseDTO.fromJson(response.data);
  }

  Future<String> deleteUserLanguageByUserProfileAndLanguage(
    int userProfileId,
    int languageId,
  ) async {
    final response = await _dio.delete(
      ApiConstants.userLanguageByUserProfileAndLanguage(
        userProfileId,
        languageId,
      ),
    );

    return response.data.toString();
  }

  Future<int> countUserLanguagesByUserProfileId(int userProfileId) async {
    final response = await _dio.get(
      ApiConstants.userLanguageCount(userProfileId),
    );

    return (response.data as num).toInt();
  }

  // =====================================================
  // User Skill
  // =====================================================

  Future<UserSkillResponseDTO> saveUserSkill(
    UserSkillRequestDTO request,
  ) async {
    final response = await _dio.post(
      ApiConstants.userSkills,
      data: request.toJson(),
    );

    return UserSkillResponseDTO.fromJson(response.data);
  }

  Future<UserSkillResponseDTO> getUserSkillById(int id) async {
    final response = await _dio.get(ApiConstants.userSkillById(id));

    return UserSkillResponseDTO.fromJson(response.data);
  }

  Future<UserSkillResponseDTO> updateUserSkill(
    int id,
    UserSkillRequestDTO request,
  ) async {
    final response = await _dio.put(
      ApiConstants.userSkillById(id),
      data: request.toJson(),
    );

    return UserSkillResponseDTO.fromJson(response.data);
  }

  Future<String> deleteUserSkill(int id) async {
    final response = await _dio.delete(ApiConstants.userSkillById(id));

    return response.data.toString();
  }

  Future<List<UserSkillResponseDTO>> getUserSkillsByUserProfileId(
    int userProfileId,
  ) async {
    final response = await _dio.get(
      ApiConstants.userSkillsByUserProfile(userProfileId),
    );

    return (response.data as List)
        .map((json) => UserSkillResponseDTO.fromJson(json))
        .toList();
  }

  Future<List<UserSkillResponseDTO>> getUserSkillsBySkillId(int skillId) async {
    final response = await _dio.get(ApiConstants.userSkillsBySkill(skillId));

    return (response.data as List)
        .map((json) => UserSkillResponseDTO.fromJson(json))
        .toList();
  }

  Future<List<UserSkillResponseDTO>> getUserSkillsBySkillCategoryId(
    int categoryId,
  ) async {
    final response = await _dio.get(
      ApiConstants.userSkillsByCategory(categoryId),
    );

    return (response.data as List)
        .map((json) => UserSkillResponseDTO.fromJson(json))
        .toList();
  }

  Future<UserSkillResponseDTO> getUserSkillByUserProfileAndSkill(
    int userProfileId,
    int skillId,
  ) async {
    final response = await _dio.get(
      ApiConstants.userSkillByUserProfileAndSkill(userProfileId, skillId),
    );

    return UserSkillResponseDTO.fromJson(response.data);
  }

  Future<String> deleteUserSkillByUserProfileAndSkill(
    int userProfileId,
    int skillId,
  ) async {
    final response = await _dio.delete(
      ApiConstants.userSkillByUserProfileAndSkill(userProfileId, skillId),
    );

    return response.data.toString();
  }

  Future<int> countUserSkillsByUserProfileId(int userProfileId) async {
    final response = await _dio.get(ApiConstants.userSkillCount(userProfileId));

    return (response.data as num).toInt();
  }

  // =====================================================
  // Resume
  // =====================================================

  Future<ResumeResponseDTO> getResume(int userProfileId) async {
    final response = await _dio.get(ApiConstants.resume(userProfileId));

    return ResumeResponseDTO.fromJson(response.data);
  }

  Future<String> getResumeHtml(int userProfileId) async {
    final response = await _dio.get(ApiConstants.resumeHtml(userProfileId));

    return response.data.toString();
  }

  Future<Uint8List> getResumePdf(int userProfileId) async {
    final response = await _dio.get<List<int>>(
      ApiConstants.resumePdf(userProfileId),
      options: Options(responseType: ResponseType.bytes),
    );

    return Uint8List.fromList(response.data ?? []);
  }

  // =====================================================
  // Uploaded Resume File

  Future<ResumeFileResponseDTO> uploadResume(
    int userProfileId,
    File? file, {
    Uint8List? bytes,
    String? fileName,
  }) async {
    final formData = FormData.fromMap({
      if (file != null)
        'cv': await MultipartFile.fromFile(
          file.path,
          filename: file.path.split(Platform.pathSeparator).last,
        )
      else if (bytes != null)
        'cv': MultipartFile.fromBytes(
          bytes,
          filename: fileName ?? 'resume.pdf',
        ),
    });

    final response = await _dio.post(
      ApiConstants.uploadedResume,
      data: formData,
      queryParameters: {'userProfileId': userProfileId},
    );

    return ResumeFileResponseDTO.fromJson(response.data);
  }

  Future<String> deleteResumeFile(int id) async {
    final response = await _dio.delete(ApiConstants.uploadedResumeById(id));

    return response.data.toString();
  }

  Future<ResumeFileResponseDTO> getResumeFileByUserProfileId(
    int userProfileId,
  ) async {
    final response = await _dio.get(
      ApiConstants.uploadedResumeByUserProfile(userProfileId),
    );

    return ResumeFileResponseDTO.fromJson(response.data);
  }

  Future<String> deleteResumeFileByUserProfileId(int userProfileId) async {
    final response = await _dio.delete(
      ApiConstants.uploadedResumeByUserProfile(userProfileId),
    );

    return response.data.toString();
  }

  Future<bool> resumeFileExists(int userProfileId) async {
    final response = await _dio.get(
      ApiConstants.resumeFileExists(userProfileId),
    );

    return response.data as bool;
  }

  // =====================================================
  // Resume Import
  // =====================================================

  //   Future<ResumeImportPreviewDTO> getResumeImportPreview(
  //     int userProfileId,
  //   ) async {
  //     final response = await _dio.get(ApiConstants.resumeImport(userProfileId));
  //
  //     print("============================================================================");
  //     print(response);
  //
  //     return ResumeImportPreviewDTO.fromJson(response.data);
  //   }
  //
    Future<void> saveImportedResume(
      int userProfileId,
      ResumeImportPreviewDTO preview,
    ) async {
      await _dio.post(
        ApiConstants.saveResumeImport(userProfileId),
        data: preview.toJson(),
      );
    }

  Future<ResumeImportPreviewDTO> getResumeImportPreview(
    int userProfileId,
  ) async {
    try {
      print('========== RESUME IMPORT START ==========');
      print('URL: ${ApiConstants.resumeImport(userProfileId)}');
      print('BASE URL: ${ApiConstants.baseUrl}');

      final response = await _dio.get(
        ApiConstants.resumeImport(userProfileId),
        options: Options(
          connectTimeout: const Duration(minutes: 10),
          receiveTimeout: const Duration(minutes: 10),
        ),
      );

      print('========== RESPONSE RECEIVED ==========');
      print('STATUS: ${response.statusCode}');
      print('DATA: ${response.data}');

      return ResumeImportPreviewDTO.fromJson(response.data);
    } on DioException catch (e) {
      print('========== DIO ERROR ==========');
      print('TYPE: ${e.type}');
      print('MESSAGE: ${e.message}');
      print('ERROR: ${e.error}');
      print('STATUS: ${e.response?.statusCode}');
      print('DATA: ${e.response?.data}');
      print('REQUEST URL: ${e.requestOptions.uri}');
      print('HEADERS: ${e.requestOptions.headers}');

      rethrow;
    } catch (e, stackTrace) {
      print('========== OTHER ERROR ==========');
      print(e);
      print(stackTrace);

      rethrow;
    }
  }
}
