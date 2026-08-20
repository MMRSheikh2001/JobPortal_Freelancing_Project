import 'package:flutter/foundation.dart';

class ApiConstants {
  ApiConstants._();

  static String get host {
    if (kIsWeb) {
      // Flutter Web
      return 'localhost';
    }

    switch (defaultTargetPlatform) {
      case TargetPlatform.android:
        // Android Emulator
        return '10.0.2.2';

      case TargetPlatform.iOS:
        // iOS Simulator
        return 'localhost';

      default:
        // Windows / macOS / Linux
        return 'localhost';
    }
  }

  static String get baseUrl => 'http://$host:8090/api/';

  static String get fileUrl => 'http://$host:8090/api/files/';

  //Files

  static String get userProfileImageUrl =>
      '${fileUrl}userprofiles/';


  static String get companyProfileImageUrl =>
      '${fileUrl}companyprofiles/';

  // ── Auth ───────────────────────────────────────────────
  static const String login = 'auth/login';
  static const String forgotPassword = 'auth/forgot-password';
  static const String resetPassword = 'auth/reset-password';
  static const String verifyEmail = 'auth/verifyemail';
  static const String register='users/register';

  // Dashboard
  static const String userDashboard ='dashboards/user';

  //jobs

static const String searchJobs='jobs/search';
static const String getJobById='jobs/';
static const String aiJobMatch='ai/interview/';// "api/ai/interview/{jobId}/match/{userProfileId}"

//job application









}
