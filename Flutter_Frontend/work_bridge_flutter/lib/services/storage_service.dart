import 'dart:convert';

import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:work_bridge_flutter/auth/request/user_request.dart';
import 'package:work_bridge_flutter/auth/response/login_response.dart';

class StorageKeys {
  StorageKeys._();

  static const token = 'wb_token';
  static const user = 'wb_user';
}

class StorageService {
  StorageService(this._storage);

  final FlutterSecureStorage _storage;

  // ── Write ────────────────────────────────────────────
  Future<void> saveSession(LoginResponse data) async {
    await _storage.write(key: StorageKeys.token, value: data.token);
    await _storage.write(
      key: StorageKeys.user,
      value: jsonEncode(data.toJson()),
    );
  }

  // ── Read ─────────────────────────────────────────────
  Future<String?> getToken() => _storage.read(key: StorageKeys.token);

  Future<LoginResponse?> getUser() async {
    final raw = await _storage.read(key: StorageKeys.user);
    if (raw == null) return null;
    try {
      return LoginResponse.fromJson(jsonDecode(raw) as Map<String, dynamic>);
    } catch (_) {
      return null;
    }
  }

  Future<UserRole?> getRole() async => (await getUser())?.role;

  Future<bool> isLoggedIn() async => (await getToken()) != null;

  // ── Clear ────────────────────────────────────────────
  Future<void> clearSession() async {
    await _storage.delete(key: StorageKeys.token);
    await _storage.delete(key: StorageKeys.user);
  }

  // ── Generic (mirrors saveData/getData/removeData) ────
  Future<void> saveData(String key, Map<String, dynamic> data) =>
      _storage.write(key: key, value: jsonEncode(data));

  Future<Map<String, dynamic>?> getData(String key) async {
    final raw = await _storage.read(key: key);
    if (raw == null) return null;
    try {
      return jsonDecode(raw) as Map<String, dynamic>;
    } catch (_) {
      return null;
    }
  }

  Future<void> removeData(String key) => _storage.delete(key: key);
}
