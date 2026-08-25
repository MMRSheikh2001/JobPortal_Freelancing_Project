import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/providers.dart';
import 'package:work_bridge_flutter/chat/data/notification_repository.dart';
import 'package:work_bridge_flutter/cvinformations/data/company_profile_repository.dart';
import 'package:work_bridge_flutter/cvinformations/data/cv_repository.dart';
import 'package:work_bridge_flutter/gig/data/gig_repository.dart';
import 'package:work_bridge_flutter/job/data/job_repository.dart';
import 'package:work_bridge_flutter/masterdata/data/master_data_repository.dart';
import 'package:work_bridge_flutter/masterdata/models/response/category_response.dart';

final jobRepositoryProvider = Provider<JobRepository>((ref) {
  return JobRepository(ref.watch(apiClientProvider));
});

final notificationRepositoryProvider = Provider<NotificationRepository>((ref) {
  return NotificationRepository(ref.watch(apiClientProvider));
});

final masterDataRepositoryProvider = Provider<MasterDataRepository>((ref) {
  return MasterDataRepository(ref.watch(apiClientProvider));
});

final cvRepositoryProvider = Provider<CvRepository>((ref) {
  return CvRepository(ref.watch(apiClientProvider));
});

final companyProfileRepositoryProvider = Provider<CompanyProfileRepository>((
  ref,
) {
  return CompanyProfileRepository(ref.watch(apiClientProvider));
});

final countriesProvider = FutureProvider((ref) {
  return ref.watch(masterDataRepositoryProvider).getAllCountries();
});

// providers.dart or main.dart
final navigatorKeyProvider = Provider<GlobalKey<NavigatorState>>((ref) {
  return GlobalKey<NavigatorState>();
});

final gigRepositoryProvider = Provider<GigRepository>((ref) {
  return GigRepository(ref.watch(apiClientProvider));
});

final gigCategoriesProvider = FutureProvider<List<CategoryResponseDTO>>((ref) {
  return ref.watch(masterDataRepositoryProvider).getAllCategories();
});
