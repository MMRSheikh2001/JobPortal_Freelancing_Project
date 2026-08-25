import 'package:dio/dio.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/user_profile_response.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

/// The signed-in job seeker's own profile, fetched by their userId.
/// Returns null if they haven't created a profile yet (backend 404).
/// Any other failure (network, auth, 500, etc.) is rethrown so it
/// surfaces as a real error instead of being silently mistaken for
/// "no profile yet" — mirrors myCompanyProfileProvider.
final myUserProfileProvider = FutureProvider<UserProfileResponseDTO?>((
  ref,
) async {
  final userId = ref.watch(currentUserProvider)?.userId;
  if (userId == null) return null;

  try {
    return await ref.watch(cvRepositoryProvider).getUserProfileByUserId(userId);
  } on DioException catch (e) {
    if (e.response?.statusCode == 404) return null;
    rethrow;
  }
});
