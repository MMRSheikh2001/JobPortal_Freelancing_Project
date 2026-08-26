import 'package:dio/dio.dart';
import 'package:work_bridge_flutter/services/storage_service.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';
import 'package:work_bridge_flutter/utils/navigation_service.dart';
import 'package:work_bridge_flutter/router/app_router.dart';

class ApiClient {
  final StorageService _storageService;
  late final Dio dio;

  bool _loggingOut = false;

  ApiClient(this._storageService) {
    dio = Dio(
      BaseOptions(
        baseUrl: ApiConstants.baseUrl,
        connectTimeout: const Duration(seconds: 2000),
        receiveTimeout: const Duration(seconds: 2000),
      ),
    );

    dio.interceptors.add(
      InterceptorsWrapper(
        // ================================================================
        // ATTACH JWT
        // ================================================================
        onRequest: (options, handler) async {
          final token = await _storageService.getToken();

          if (token != null && token.isNotEmpty) {
            options.headers['Authorization'] = 'Bearer $token';
          }

          handler.next(options);
        },

        // ================================================================
        // HANDLE JWT EXPIRATION
        // ================================================================
        onError: (error, handler) async {
          if (error.response?.statusCode == 401) {
            await _handleUnauthorized();
          }

          handler.next(error);
        },
      ),
    );
  }

  // ================================================================
  // LOGOUT AFTER 401
  // ================================================================

  Future<void> _handleUnauthorized() async {
    if (_loggingOut) return;

    _loggingOut = true;

    try {
      await _storageService.clearSession();

      final navigator = NavigationService.navigatorKey.currentState;

      if (navigator != null) {
        navigator.pushNamedAndRemoveUntil(AppRouter.login, (route) => false);
      }
    } finally {
      _loggingOut = false;
    }
  }
}

/// Normalizes Dio/backend errors into a readable message.
String apiErrorMessage(Object error) {
  if (error is DioException) {
    final status = error.response?.statusCode;
    final data = error.response?.data;

    if (data is String && data.isNotEmpty) return data;

    if (data is Map && data['message'] is String) {
      return data['message'] as String;
    }

    switch (status) {
      case 401:
        return 'Your session has expired. Please log in again.';

      case 403:
        return 'Your account is not verified or has been disabled.';

      case 404:
        return 'Not found.';

      case null:
        return 'Could not reach the server. Check your connection / API URL.';

      default:
        return 'Something went wrong. Please try again.';
    }
  }

  return 'Something went wrong. Please try again.';
}
