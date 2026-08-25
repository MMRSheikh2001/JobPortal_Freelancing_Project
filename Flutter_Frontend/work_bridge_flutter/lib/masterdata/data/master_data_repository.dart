

import 'package:dio/dio.dart';
import 'package:work_bridge_flutter/masterdata/models/response/category_response.dart';
import 'package:work_bridge_flutter/masterdata/models/response/country_response.dart';
import 'package:work_bridge_flutter/masterdata/models/response/district_response.dart';
import 'package:work_bridge_flutter/masterdata/models/response/division_response.dart';
import 'package:work_bridge_flutter/masterdata/models/response/language_response.dart';
import 'package:work_bridge_flutter/masterdata/models/response/policestation_response.dart';
import 'package:work_bridge_flutter/masterdata/models/response/skill_response.dart';
import 'package:work_bridge_flutter/utils/api_client.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';

class MasterDataRepository {
  MasterDataRepository(this._apiClient);

  final ApiClient _apiClient;

  Dio get _dio => _apiClient.dio;

  // =====================================================
  // Language
  // =====================================================

  Future<List<LanguageResponseDTO>> getAllLanguages() async {
    final response = await _dio.get(
      ApiConstants.languages,
    );

    return (response.data as List)
        .map((json) => LanguageResponseDTO.fromJson(json))
        .toList();
  }

  Future<LanguageResponseDTO> getLanguageById(int id) async {
    final response = await _dio.get(
      ApiConstants.languageById(id),
    );

    return LanguageResponseDTO.fromJson(response.data);
  }

  // =====================================================
  // Category
  // =====================================================

  Future<List<CategoryResponseDTO>> getAllCategories() async {
    final response = await _dio.get(
      ApiConstants.categories,
    );

    return (response.data as List)
        .map((json) => CategoryResponseDTO.fromJson(json))
        .toList();
  }

  Future<CategoryResponseDTO> getCategoryById(int id) async {
    final response = await _dio.get(
      ApiConstants.categoryById(id),
    );

    return CategoryResponseDTO.fromJson(response.data);
  }

  // =====================================================
  // Skill
  // =====================================================

  Future<List<SkillResponseDTO>> getAllSkills() async {
    final response = await _dio.get(
      ApiConstants.skills,
    );

    return (response.data as List)
        .map((json) => SkillResponseDTO.fromJson(json))
        .toList();
  }

  Future<SkillResponseDTO> getSkillById(int id) async {
    final response = await _dio.get(
      ApiConstants.skillById(id),
    );

    return SkillResponseDTO.fromJson(response.data);
  }

  Future<List<SkillResponseDTO>> getSkillsByCategoryId(
      int categoryId,
      ) async {
    final response = await _dio.get(
      ApiConstants.skillsByCategory(categoryId),
    );

    return (response.data as List)
        .map((json) => SkillResponseDTO.fromJson(json))
        .toList();
  }

  // =====================================================
  // Country
  // =====================================================

  Future<List<CountryResponseDTO>> getAllCountries() async {
    final response = await _dio.get(
      ApiConstants.countries,
    );

    return (response.data as List)
        .map((json) => CountryResponseDTO.fromJson(json))
        .toList();
  }

  // =====================================================
  // Division
  // =====================================================

  Future<List<DivisionResponseDTO>> getAllDivisions() async {
    final response = await _dio.get(
      ApiConstants.divisions,
    );

    return (response.data as List)
        .map((json) => DivisionResponseDTO.fromJson(json))
        .toList();
  }

  Future<List<DivisionResponseDTO>> getDivisionsByCountryId(
      int countryId,
      ) async {
    final response = await _dio.get(
      ApiConstants.divisionsByCountry(countryId),
    );

    return (response.data as List)
        .map((json) => DivisionResponseDTO.fromJson(json))
        .toList();
  }

  // =====================================================
  // District
  // =====================================================

  Future<List<DistrictResponseDTO>> getAllDistricts() async {
    final response = await _dio.get(
      ApiConstants.districts,
    );

    return (response.data as List)
        .map((json) => DistrictResponseDTO.fromJson(json))
        .toList();
  }

  Future<List<DistrictResponseDTO>> getDistrictsByDivisionId(
      int divisionId,
      ) async {
    final response = await _dio.get(
      ApiConstants.districtsByDivision(divisionId),
    );

    return (response.data as List)
        .map((json) => DistrictResponseDTO.fromJson(json))
        .toList();
  }

  // =====================================================
  // Police Station
  // =====================================================

  Future<List<PoliceStationResponseDTO>>
  getAllPoliceStations() async {
    final response = await _dio.get(
      ApiConstants.policeStations,
    );

    return (response.data as List)
        .map(
          (json) =>
          PoliceStationResponseDTO.fromJson(json),
    )
        .toList();
  }

  Future<List<PoliceStationResponseDTO>>
  getPoliceStationsByDistrictId(
      int districtId,
      ) async {
    final response = await _dio.get(
      ApiConstants.policeStationsByDistrict(districtId),
    );

    return (response.data as List)
        .map(
          (json) =>
          PoliceStationResponseDTO.fromJson(json),
    )
        .toList();
  }
}