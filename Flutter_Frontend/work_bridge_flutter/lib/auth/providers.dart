import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:work_bridge_flutter/services/storage_service.dart';
import 'package:work_bridge_flutter/utils/api_client.dart';

/// Central place for cross-cutting singletons, mirroring how Angular's DI
/// container provided StorageService / HttpClient app-wide.

final secureStorageProvider = Provider<FlutterSecureStorage>((ref) {
  return const FlutterSecureStorage();
});

final storageServiceProvider = Provider<StorageService>((ref) {
  return StorageService(ref.watch(secureStorageProvider));
});

final apiClientProvider = Provider<ApiClient>((ref) {
  return ApiClient(ref.watch(storageServiceProvider));
});
