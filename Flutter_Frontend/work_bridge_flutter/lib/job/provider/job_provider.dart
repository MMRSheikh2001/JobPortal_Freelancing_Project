
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_riverpod/legacy.dart';
import 'package:work_bridge_flutter/job/entity/request/job_search_request.dart';
import 'package:work_bridge_flutter/job/entity/response/job_response.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

/// Current job search filter. The search screen updates this
/// (`ref.read(jobSearchFilterProvider.notifier).state = ...`) whenever any
/// filter control changes — jobSearchResultsProvider watches it and
/// automatically re-runs the search, so there's no manual "refetch"
/// call needed anywhere in the UI.
final jobSearchFilterProvider = StateProvider<JobSearchRequestDTO>((ref) {
  return const JobSearchRequestDTO(active: true);
});

/// Auto re-fetches whenever jobSearchFilterProvider changes.
final jobSearchResultsProvider = FutureProvider<List<JobResponseDTO>>((ref) {
  final filter = ref.watch(jobSearchFilterProvider);
  return ref.watch(jobRepositoryProvider).searchJobs(filter);
});