import 'package:flutter/material.dart';
import 'package:work_bridge_flutter/auth/screen/auth_gate.dart';
import 'package:work_bridge_flutter/auth/screen/forgot_passsword_screen.dart';
import 'package:work_bridge_flutter/auth/screen/login_screen.dart';
import 'package:work_bridge_flutter/auth/screen/register_screen.dart';
import 'package:work_bridge_flutter/chat/screen/chat_list_screen.dart';
import 'package:work_bridge_flutter/gig/screen/gig_orders_list_screen.dart';
import 'package:work_bridge_flutter/gig/screen/gigs_search_screen.dart';
import 'package:work_bridge_flutter/job/screen/job_applications_list_screen.dart';
import 'package:work_bridge_flutter/job/screen/jobs_search_Screen.dart';
import 'package:work_bridge_flutter/public_pages/notifications_list_screen.dart';
import 'package:work_bridge_flutter/public_pages/profile_gate.dart';
import 'package:work_bridge_flutter/wallet/screen/wallet_screen.dart';

class AppRouter {
  static const String root = '/';
  static const String login = '/login';
  static const String register = '/register';
  static const String forgotPassword = '/forgot-password';

  static const String gigs='/gigs';
  static const String jobs='/jobs';

  static const String applications='/applications';
  static const String orders='/orders';

  static const String notifications='/notifications';
  static const String wallet='/wallet';
  static const String chat='/chat';

  static const String profile='/profile';





  static Route<dynamic> onGenerateRoute(RouteSettings settings) {
    switch (settings.name) {
      case root:
        // This is where your RoleRedirectScreen or Home would go
        return MaterialPageRoute(builder: (_) => const AuthGate());
      case login:
        return MaterialPageRoute(builder: (_) => const LoginScreen());

      // Add cases for /register and /forgot-password here
      case register:
        return MaterialPageRoute(builder: (_) => const RegisterScreen());
      case forgotPassword:
        return MaterialPageRoute(builder: (_) => const ForgotPasswordScreen());

      // navigation from home page

      case gigs:
        return MaterialPageRoute(builder: (_) => const GigsSearchScreen());

      case jobs:
        return MaterialPageRoute(builder: (_) => const JobsSearchScreen());

      case applications:
        return MaterialPageRoute(builder: (_) => const JobApplicationsListScreen());

      case orders:
        return MaterialPageRoute(builder: (_) => const GigOrdersListScreen());

      case notifications:
        return MaterialPageRoute(builder: (_) => const NotificationsListScreen());

      case wallet:
        return MaterialPageRoute(builder: (_) => const WalletScreen());

      case chat:
        return MaterialPageRoute(builder: (_) => const ChatListScreen());

      case profile:
        return MaterialPageRoute(builder: (_) => const ProfileGate());

      default:
        return MaterialPageRoute(
          builder: (_) => Scaffold(
            body: Center(child: Text('No route defined for ${settings.name}')),
          ),
        );
    }
  }
}
