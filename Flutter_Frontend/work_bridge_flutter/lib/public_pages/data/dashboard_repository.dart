
import 'package:dio/dio.dart';
import 'package:work_bridge_flutter/public_pages/entity/user_dashboard_reponse.dart';
import 'package:work_bridge_flutter/utils/api_client.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';

class DashboardRepository {
  DashboardRepository(this._apiClient);

  final ApiClient _apiClient;

  Dio get _dio => _apiClient.dio;

  Future<UserDashboardDTO> getUserDashboard(int userId) async {
    final response = await _dio.get(
      '${ApiConstants.userDashboard}/$userId',
    );

    return UserDashboardDTO.fromJson(
      response.data as Map<String, dynamic>,
    );
  }
}