
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/public_pages/company_profile_screen.dart';
import 'package:work_bridge_flutter/public_pages/user_profile_screen.dart';

import '../auth/request/user_request.dart';

class ProfileGate extends ConsumerWidget {
  const ProfileGate({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final user = ref.watch(currentUserProvider);

    if (user == null) {
      return const Scaffold(
        body: Center(child: Text('Please sign in to view your profile.')),
      );
    }

    switch (user.role) {
      case UserRole.COMPANY:
        return const CompanyProfileScreen();
      case UserRole.USER:
      case UserRole.ADMIN:
      case null:
        return const UserProfileScreen();
    }
  }
}