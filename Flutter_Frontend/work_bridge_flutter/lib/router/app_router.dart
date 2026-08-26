import 'package:flutter/material.dart';
import 'package:work_bridge_flutter/auth/screen/auth_gate.dart';
import 'package:work_bridge_flutter/auth/screen/forgot_passsword_screen.dart';
import 'package:work_bridge_flutter/auth/screen/login_screen.dart';
import 'package:work_bridge_flutter/auth/screen/register_screen.dart';
import 'package:work_bridge_flutter/chat/screen/chat_list_screen.dart';
import 'package:work_bridge_flutter/chat/screen/chat_screen.dart';
import 'package:work_bridge_flutter/cvinformations/screens/add_edit_education_screen.dart';
import 'package:work_bridge_flutter/cvinformations/screens/add_edit_experience.dart';
import 'package:work_bridge_flutter/cvinformations/screens/add_edit_extracurricular.dart';
import 'package:work_bridge_flutter/cvinformations/screens/add_edit_portfolio.dart';
import 'package:work_bridge_flutter/cvinformations/screens/add_edit_reference.dart';
import 'package:work_bridge_flutter/cvinformations/screens/add_edit_training.dart';
import 'package:work_bridge_flutter/cvinformations/screens/educations_list_screen.dart';
import 'package:work_bridge_flutter/cvinformations/screens/experience_list_screen.dart';
import 'package:work_bridge_flutter/cvinformations/screens/extracurricular_list_screen.dart';
import 'package:work_bridge_flutter/cvinformations/screens/full_resume_screen.dart';
import 'package:work_bridge_flutter/cvinformations/screens/personal_info_screen.dart';
import 'package:work_bridge_flutter/cvinformations/screens/portfolio_list_screen.dart';
import 'package:work_bridge_flutter/cvinformations/screens/reference_list_screen.dart';
import 'package:work_bridge_flutter/cvinformations/screens/resume_file_import_screen.dart';
import 'package:work_bridge_flutter/cvinformations/screens/resume_file_screen.dart';
import 'package:work_bridge_flutter/cvinformations/screens/training_list_screen.dart';
import 'package:work_bridge_flutter/cvinformations/screens/user_language_screen.dart';
import 'package:work_bridge_flutter/cvinformations/screens/user_skill_screen.dart';
import 'package:work_bridge_flutter/gig/screen/gig_details.dart';
import 'package:work_bridge_flutter/gig/screen/gig_order_details.dart';
import 'package:work_bridge_flutter/gig/screen/gig_orders_list_screen.dart';
import 'package:work_bridge_flutter/gig/screen/gigs_search_screen.dart';
import 'package:work_bridge_flutter/gig/screen/review_add_edit_screen.dart';
import 'package:work_bridge_flutter/job/screen/ai_interview_screen.dart';
import 'package:work_bridge_flutter/job/screen/job_applications_list_screen.dart';
import 'package:work_bridge_flutter/job/screen/job_details.dart';
import 'package:work_bridge_flutter/job/screen/jobs_search_Screen.dart';
import 'package:work_bridge_flutter/public_pages/notifications_list_screen.dart';
import 'package:work_bridge_flutter/public_pages/profile_gate.dart';
import 'package:work_bridge_flutter/wallet/screen/deposit_screen.dart';
import 'package:work_bridge_flutter/wallet/screen/transaction_screen.dart';
import 'package:work_bridge_flutter/wallet/screen/wallet_screen.dart';
import 'package:work_bridge_flutter/wallet/screen/withdraw_screen.dart';

class AppRouter {
  static const String root = '/';
  static const String login = '/login';
  static const String register = '/register';
  static const String forgotPassword = '/forgot-password';

  static const String gigs = '/gigs';
  static const String gigDetails = '/gig-details';

  static const String jobs = '/jobs';
  static const String jobDetails = '/job-details';

  static const String applications = '/applications';
  static const String aiInterview = '/ai-interview';

  static const String orders = '/orders';
  static const String orderDetails = '/order-details';
  static const String review = '/buyer-review';

  static const String notifications = '/notifications';

  static const String wallet = '/wallet';
  static const String deposit = '/deposit';
  static const String withdraw = '/withdraw';
  static const String transaction = '/transaction';

  static const String chatList = '/chat-list';
  static const String chat = '/chat';

  static const String profile = '/profile';

  static const String personalInfo = '/personal-info';

  static const String educations = '/educations';
  static const String addEditEducation = '/education/add-edit';

  static const String experiences = '/experiences';
  static const String addEditExperience = '/experience/add-edit';

  static const String extracurriculars = '/extracurriculars';
  static const String addEditExtracurricular = '/extracurricular/add-edit';

  static const String fullResumeScreen = '/full-resume-screen';

  static const String portfolios = '/portfolios';
  static const String addEditPortfolio = '/portfolio/add-edit';

  static const String references = '/references';
  static const String addEditReference = '/reference/add-edit';

  static const String resumeFile = '/resume-file';
  static const String resumeFileImport = '/resume-file-import';

  static const String trainings = '/trainings';
  static const String addEditTraining = '/training/add-edit';

  static const String userLanguages = '/userLanguages';
  static const String userSkills = '/userSkills';

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

      case gigDetails:
        final gigId = settings.arguments as int;
        return MaterialPageRoute(builder: (_) => GigDetails(gigId: gigId));

      case orders:
        return MaterialPageRoute(builder: (_) => const GigOrdersListScreen());

      case orderDetails:
        final gigOrderId = settings.arguments as int;
        return MaterialPageRoute(
          builder: (_) => GigOrderDetailsScreen(gigOrderId: gigOrderId),
        );
      case review:
        final gigOrderId = settings.arguments as int;
        return MaterialPageRoute(
          builder: (_) => ReviewAddEditScreen(gigOrderId: gigOrderId),
        );

      case jobs:
        return MaterialPageRoute(builder: (_) => const JobsSearchScreen());

      case jobDetails:
        final jobId = settings.arguments as int;
        return MaterialPageRoute(
          builder: (_) => JobDetailsScreen(jobId: jobId),
        );

      case applications:
        return MaterialPageRoute(
          builder: (_) => const JobApplicationsListScreen(),
        );

      case aiInterview:
        final applicationId = settings.arguments as int;
        return MaterialPageRoute(
          builder: (_) => AiInterviewScreen(applicationId: applicationId),
        );

      case notifications:
        return MaterialPageRoute(
          builder: (_) => const NotificationsListScreen(),
        );

      case wallet:
        return MaterialPageRoute(builder: (_) => const WalletScreen());
      case deposit:
        return MaterialPageRoute(builder: (_) => const DepositScreen());
      case withdraw:
        return MaterialPageRoute(builder: (_) => const WithdrawScreen());
      case transaction:
        return MaterialPageRoute(builder: (_) => const TransactionScreen());

      case chatList:
        return MaterialPageRoute(builder: (_) => const ChatListScreen());

      case chat:
        final conversationId = settings.arguments as int;
        return MaterialPageRoute(
          builder: (_) => ChatScreen(conversationId: conversationId),
        );

      case profile:
        return MaterialPageRoute(builder: (_) => const ProfileGate());

      //CV Informations

      case personalInfo:
        return MaterialPageRoute(builder: (_) => const PersonalInfoScreen());
      case educations:
        return MaterialPageRoute(builder: (_) => const EducationsListScreen());

      case addEditEducation:
        final educationId = settings.arguments as int?;
        return MaterialPageRoute(
          builder: (_) => AddEditEducationScreen(educationId: educationId),
        );

      case experiences:
        return MaterialPageRoute(builder: (_) => const ExperienceListScreen());
      case addEditExperience:
        final experienceId = settings.arguments as int?;
        return MaterialPageRoute(
          builder: (_) => AddEditExperience(experienceId: experienceId),
        );

      case extracurriculars:
        return MaterialPageRoute(
          builder: (_) => const ExtracurricularListScreen(),
        );
      case addEditExtracurricular:
        final extracurricularId = settings.arguments as int?;
        return MaterialPageRoute(
          builder: (_) =>
              AddEditExtracurricular(extracurricularId: extracurricularId),
        );

      case fullResumeScreen:
        return MaterialPageRoute(builder: (_) => const FullResumeScreen());

      case portfolios:
        return MaterialPageRoute(builder: (_) => const PortfolioListScreen());
      case addEditPortfolio:
        final portfolioId = settings.arguments as int?;
        return MaterialPageRoute(
          builder: (_) => AddEditPortfolio(portfolioId: portfolioId),
        );

      case references:
        return MaterialPageRoute(builder: (_) => const ReferenceListScreen());
      case addEditReference:
        final referenceId = settings.arguments as int?;
        return MaterialPageRoute(
          builder: (_) => AddEditReference(referenceId: referenceId),
        );

      case resumeFile:
        return MaterialPageRoute(builder: (_) => const ResumeFileScreen());
      case resumeFileImport:
        final profileId = settings.arguments as int;
        return MaterialPageRoute(
          builder: (_) => ResumeFileImportScreen(userProfileId: profileId),
        );

      case trainings:
        return MaterialPageRoute(builder: (_) => const TrainingListScreen());
      case addEditTraining:
        final trainingId = settings.arguments as int?;
        return MaterialPageRoute(
          builder: (_) => AddEditTraining(trainingId: trainingId),
        );

      case userLanguages:
        return MaterialPageRoute(builder: (_) => const UserLanguageScreen());
      case userSkills:
        return MaterialPageRoute(builder: (_) => const UserSkillScreen());

      default:
        return MaterialPageRoute(
          builder: (_) => Scaffold(
            body: Center(child: Text('No route defined for ${settings.name}')),
          ),
        );
    }
  }
}
