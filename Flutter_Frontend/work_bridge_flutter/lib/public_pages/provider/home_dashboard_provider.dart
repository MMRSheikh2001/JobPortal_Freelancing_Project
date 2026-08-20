import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_riverpod/legacy.dart';
import 'package:work_bridge_flutter/auth/providers.dart';
import 'package:work_bridge_flutter/public_pages/data/dashboard_repository.dart';
import 'package:work_bridge_flutter/public_pages/entity/user_dashboard_reponse.dart';

final dashboardRepositoryProvider = Provider<DashboardRepository>((ref) {
  return DashboardRepository(ref.watch(apiClientProvider));
});

class DashboardController extends StateNotifier<AsyncValue<UserDashboardDTO?>> {
  DashboardController(this._ref) : super(const AsyncValue.loading());

  final Ref _ref;

  Future<void> loadUserDashboard(int userId) async {
    state = const AsyncValue.loading();

    try {
      final repository = _ref.read(dashboardRepositoryProvider);

      final dashboard = await repository.getUserDashboard(userId);

      state = AsyncValue.data(dashboard);
    } catch (e, st) {
      state = AsyncValue.error(e, st);
    }
  }

  Future<void> refresh(int userId) async {
    await loadUserDashboard(userId);
  }

  void clear() {
    state = const AsyncValue.data(null);
  }
}

final dashboardControllerProvider =
    StateNotifierProvider<DashboardController, AsyncValue<UserDashboardDTO?>>((
      ref,
    ) {
      return DashboardController(ref);
    });
