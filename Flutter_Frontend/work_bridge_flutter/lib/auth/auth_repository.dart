
import 'package:dio/dio.dart';
import 'package:work_bridge_flutter/auth/request/forgot_password_request.dart';
import 'package:work_bridge_flutter/auth/request/login_request.dart';
import 'package:work_bridge_flutter/auth/request/user_request.dart';
import 'package:work_bridge_flutter/auth/response/login_response.dart';
import 'package:work_bridge_flutter/auth/response/user_response.dart';
import 'package:work_bridge_flutter/services/storage_service.dart';
import 'package:work_bridge_flutter/utils/api_client.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';

class AuthRepository{

  AuthRepository(this._apiClient, this._storageService);

  final ApiClient _apiClient;
  final StorageService _storageService;

  Dio get _dio => _apiClient.dio;

  Future<UserResponseDTO> register(UserRequestDTO dto) async {
    final res = await _dio.post(ApiConstants.register,data: dto.toJson());
    final registerRes=UserResponseDTO.fromJson(res.data as Map<String,dynamic>);

    return registerRes;
  }

  Future<LoginResponse> login(LoginRequest dto) async {
    final res = await _dio.post(ApiConstants.login, data: dto.toJson());
    final loginRes = LoginResponse.fromJson(res.data as Map<String, dynamic>);
    await _storageService.saveSession(loginRes);
    return loginRes;
  }

  Future<void> logout() => _storageService.clearSession();


  Future<String> forgotPassword(ForgotPasswordRequest dto) async {
    final res = await _dio.post(
      ApiConstants.forgotPassword,
      data: dto.toJson(),
      options: Options(responseType: ResponseType.plain),
    );
    return res.data.toString();
  }



  Future<String> verifyEmail(String token) async {
    final res = await _dio.get(
      ApiConstants.verifyEmail,
      queryParameters: {'token': token},
      options: Options(responseType: ResponseType.plain),
    );
    return res.data.toString();
  }


}