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

  static String get userProfileImageUrl => '${fileUrl}userprofiles/';

  static String get companyProfileImageUrl => '${fileUrl}companyprofiles/';

  static String get resumeFileUrl => '${fileUrl}resumes/';

  static String get portfolioFileUrl => '${fileUrl}portfolios/';

  static String get trainingFileUrl => '${fileUrl}trainings/';

  static String get messageFileUrl => '${fileUrl}messages/';

  static String get gigDeliveryFileUrl => '${fileUrl}gigdeliveries/';

  static String get gigImageUrl => '${fileUrl}gigs/';

  // ── Auth ───────────────────────────────────────────────
  static const String login = 'auth/login';
  static const String forgotPassword = 'auth/forgot-password';
  static const String resetPassword = 'auth/reset-password';
  static const String verifyEmail = 'auth/verifyemail';
  static const String register = 'users/register';

  // Dashboard
  static const String userDashboard = 'dashboards/user';

  // =====================================================
  // Job
  // =====================================================

  static const String searchJobs = 'jobs/search';

  static const String getJobById = 'jobs/';

  static String getJobUrl(int id) => '$getJobById$id';

  // =====================================================
  // AI Job Match
  // =====================================================

  static String getAiJobMatchUrl(int jobId, int userProfileId) =>
      'ai/interview/$jobId/match/$userProfileId';

  // =====================================================
  // Company Profile
  // =====================================================

  static const String companyProfiles = 'companies/';

  static String companyProfileById(int id) => 'companies/$id';

  static String companyProfileByUserId(int userId) => 'companies/user/$userId';

  static String deletecompanyProfileImage(int id) => 'companies/$id/image';

  // =====================================================
  // User Profile
  // =====================================================

  static const String userProfiles = 'userprofiles/';

  static String userProfileById(int id) => 'userprofiles/$id';

  static String userProfileByUserId(int userId) => 'userprofiles/user/$userId';

  static String deleteUserProfileImage(int id) => 'userprofiles/$id/image';

  // =====================================================
  // Education
  // =====================================================

  static const String educations = 'educations/';

  static String educationById(int id) => 'educations/$id';

  static String educationsByUserProfile(int id) => 'educations/userprofile/$id';

  static String educationCount(int id) => 'educations/userprofile/count/$id';

  // =====================================================
  // Experience
  // =====================================================

  static const String experiences = 'experiences/';

  static String experienceById(int id) => 'experiences/$id';

  static String experiencesByUserProfile(int id) =>
      'experiences/userprofile/$id';

  static String experienceCount(int id) => 'experiences/userprofile/count/$id';

  // =====================================================
  // Extracurricular
  // =====================================================

  static const String extracurriculars = 'extracurriculars/';

  static String extracurricularById(int id) => 'extracurriculars/$id';

  static String extracurricularsByUserProfile(int id) =>
      'extracurriculars/userprofile/$id';

  static String extracurricularCount(int id) =>
      'extracurriculars/userprofile/count/$id';

  // =====================================================
  // Portfolio
  // =====================================================

  static const String portfolios = 'portfolios/';

  static String portfolioById(int id) => 'portfolios/$id';

  static String deletePortfolioFile(int id) => 'portfolios/$id/file';

  static String portfoliosByUserProfile(int id) => 'portfolios/userprofile/$id';

  static String portfolioCount(int id) => 'portfolios/count/userprofile/$id';

  // =====================================================
  // Reference
  // =====================================================

  static const String references = 'references/';

  static String referenceById(int id) => 'references/$id';

  static String referencesByUserProfile(int id) => 'references/userprofile/$id';

  static String referenceCount(int id) => 'references/userprofile/count/$id';

  // =====================================================
  // Training
  // =====================================================

  static const String trainings = 'trainings/';

  static String trainingById(int id) => 'trainings/$id';

  static String deleteTrainingFile(int id) => 'trainings/$id/file';

  static String trainingsByUserProfile(int id) => 'trainings/userprofile/$id';

  static String trainingCount(int id) => 'trainings/count/userprofile/$id';

  // =====================================================
  // User Language
  // =====================================================

  static const String userLanguages = 'userlanguages/';

  static String userLanguageById(int id) => 'userlanguages/$id';

  static String userLanguagesByUserProfile(int id) =>
      'userlanguages/userprofile/$id';

  static String userLanguagesByLanguage(int id) => 'userlanguages/language/$id';

  static String userLanguageByUserProfileAndLanguage(
    int userProfileId,
    int languageId,
  ) => 'userlanguages/userprofile/$userProfileId/language/$languageId';

  static String userLanguageCount(int userProfileId) =>
      'userlanguages/userprofile/count/$userProfileId';

  // =====================================================
  // User Skill
  // =====================================================

  static const String userSkills = 'userskills/';

  static String userSkillById(int id) => 'userskills/$id';

  static String userSkillsByUserProfile(int id) => 'userskills/userprofile/$id';

  static String userSkillsBySkill(int id) => 'userskills/skill/$id';

  static String userSkillsByCategory(int id) => 'userskills/skill/category/$id';

  static String userSkillByUserProfileAndSkill(
    int userProfileId,
    int skillId,
  ) => 'userskills/userprofile/$userProfileId/skill/$skillId';

  static String userSkillCount(int userProfileId) =>
      'userskills/userprofile/count/$userProfileId';

  // =====================================================
  // Resume
  // =====================================================

  static String resume(int userProfileId) => 'resume/$userProfileId';

  static String resumeHtml(int userProfileId) => 'resume/$userProfileId/html';

  static String resumePdf(int userProfileId) => 'resume/$userProfileId/pdf';

  // =====================================================
  // Uploaded Resume File
  // =====================================================

  static const String uploadedResume = 'resumes/uploadedfile/';

  static String uploadedResumeById(int id) => 'resumes/uploadedfile/$id';

  static String uploadedResumeByUserProfile(int id) =>
      'resumes/uploadedfile/user/$id';

  static String resumeFileExists(int id) => 'resumes/uploadedfile/exists/$id';

  // =====================================================
  // Resume Import
  // =====================================================

  static String resumeImport(int userProfileId) =>
      'resume-import/$userProfileId';

  static String saveResumeImport(int userProfileId) =>
      'resume-import/save/$userProfileId';

  // =====================================================
  // Job Application
  // =====================================================

  static const String jobApplications = 'jobapplications/';

  static String jobApplicationById(int id) => 'jobapplications/$id';

  static String applicationsByUserProfile(int id) =>
      'jobapplications/userprofile/$id';

  static String withdrawApplication(int applicationId, int userProfileId) =>
      'jobapplications/withdraw/'
      '$applicationId/userprofile/$userProfileId';

  static String applicationCount(int userProfileId) =>
      'jobapplications/count/userprofile/$userProfileId';

  static String applicationExists(int jobId, int userProfileId) =>
      'jobapplications/exist/job/'
      '$jobId/userprofile/$userProfileId';

  static String applicationByJobAndUser(int jobId, int userProfileId) =>
      'jobapplications/job/'
      '$jobId/userprofile/$userProfileId';

  // =====================================================
  // AI Interview
  // =====================================================

  static String startInterview(int applicationId) =>
      'ai/interview/start/$applicationId';

  static const String submitInterview = 'ai/interview/submit';

  static String interviewByApplication(int applicationId) =>
      'ai/interview/$applicationId';

  // =====================================================
  // Notifications
  // =====================================================

  static String userNotifications(int userId) => 'notifications/user/$userId';

  static String notificationById(int notificationId) =>
      'notifications/$notificationId';

  static String unreadNotifications(int userId) =>
      'notifications/user/$userId/unread';

  static String unreadNotificationCount(int userId) =>
      'notifications/user/$userId/count';

  static String markNotificationRead(int notificationId) =>
      'notifications/$notificationId/read';

  static String markAllNotificationsRead(int userId) =>
      'notifications/user/$userId/read-all';

  static String notificationsByType(int userId, String type) =>
      'notifications/user/$userId/type/$type';

  static String deleteNotification(int notificationId) =>
      'notifications/$notificationId/clear';

  static String deleteAllNotifications(int userId) =>
      'notifications/user/$userId/clear';

  static const String searchNotifications = 'notifications/search';

  // =====================================================
  // Language
  // =====================================================

  static const String languages = 'languages/';

  static String languageById(int id) => 'languages/$id';

  // =====================================================
  // Category
  // =====================================================

  static const String categories = 'categories/';

  static String categoryById(int id) => 'categories/$id';

  // =====================================================
  // Skill
  // =====================================================

  static const String skills = 'skills/';

  static String skillById(int id) => 'skills/$id';

  static String skillsByCategory(int id) => 'skills/category/$id';

  // =====================================================
  // Country
  // =====================================================

  static const String countries = 'countries/';

  // =====================================================
  // Division
  // =====================================================

  static const String divisions = 'divisions/';

  static String divisionsByCountry(int id) => 'divisions/country/$id';

  // =====================================================
  // District
  // =====================================================

  static const String districts = 'districts/';

  static String districtsByDivision(int id) => 'districts/division/$id';

  // =====================================================
  // Police Station
  // =====================================================

  static const String policeStations = 'policestations/';

  static String policeStationsByDistrict(int id) =>
      'policestations/district/$id';

  // =====================================================
  // Gig
  // =====================================================

  static const String searchGigs = 'gigs/search';

  static const String getGigById = 'gigs/';

  static String getGigUrl(int id) => '$getGigById$id';

  // =====================================================
  // Gig Order
  // =====================================================

  static const String gigOrders = 'gig-orders/';

  static String placeGigOrder(int gigId, int buyerId) =>
      'gig-orders?gigId=$gigId&buyerId=$buyerId';

  static String acceptGigQuote(int orderId) =>
      'gig-orders/$orderId/accept-quote';

  static String rejectGigQuote(int orderId) =>
      'gig-orders/$orderId/reject-quote';

  static String acceptGigDelivery(int orderId) =>
      'gig-orders/$orderId/accept-delivery';

  static String rejectGigDelivery(int orderId) =>
      'gig-orders/$orderId/reject-delivery';

  static String buyerCancelGigOrder(int orderId) =>
      'gig-orders/$orderId/buyer-cancel';

  static String buyerGigOrders(int buyerId) => 'gig-orders/buyer/$buyerId';

  static String buyerGigOrdersByStatus(int buyerId, String status) =>
      'gig-orders/buyer/$buyerId/status/$status';

  static String buyerGigOrderCountByStatus(int buyerId, String status) =>
      'gig-orders/buyer/$buyerId/status/$status/count';

  static String buyerGigOrderCount(int buyerId) =>
      'gig-orders/buyer/$buyerId/count';

  static String gigOrderExists(int gigId, int buyerId) =>
      'gig-orders/gig/$gigId/buyer/$buyerId/exist';

  static String activeGigOrder(int gigId, int buyerId) =>
      'gig-orders/gig/$gigId/buyer/$buyerId/active';

  // =====================================================
// Gig Order - Authenticated Users
// =====================================================

  static String gigOrderById(int orderId) =>
      'gig-orders/$orderId';

  // ── Conversations ─────────────────────────────────────
  static String conversationById(int id) => 'conversations/$id';
  static String buyerConversations(int buyerId) => 'conversations/buyer/$buyerId';
  static String countBuyerConversations(int buyerId) => 'conversations/buyer/$buyerId/count';

  // ── Messages ──────────────────────────────────────────
  static const String sendMessage = 'messages/';
  static String messageById(int id) => 'messages/$id';
  static String conversationMessages(int conversationId) => 'messages/conversation/$conversationId';
  static String senderMessages(int senderId) => 'messages/sender/$senderId';
  static String unreadMessages(int conversationId) => 'messages/conversation/$conversationId/unread';
  static String countUnreadMessages(int conversationId) => 'messages/conversation/$conversationId/unread/count';
  static String countUnreadMessagesForUser(int conversationId, int senderId) =>
      'messages/conversation/$conversationId/unread/count/$senderId';
  static String markConversationAsRead(int conversationId) =>
      'messages/conversation/$conversationId/read';
  static String latestMessage(int conversationId) =>
      'messages/conversation/$conversationId/latest';


// ── Wallet ─────────────────────────────────────────────
  static String walletById(int id) => 'wallets/$id';
  static String walletByUserId(int userId) => 'wallets/user/$userId';
  static String walletBalance(int userId) => 'wallets/user/$userId/balance';
  static String walletFrozenBalance(int userId) => 'wallets/user/$userId/frozen-balance';

  // ── Transactions ───────────────────────────────────────
  static String transactionById(int id) => 'transactions/$id';
  static String transactionsFromUser(int userId) => 'transactions/from/$userId';
  static String transactionsToUser(int userId) => 'transactions/to/$userId';
  static String userTransactionHistory(int userId) => 'transactions/history/$userId';


  // ── Withdraws ──────────────────────────────────────────
  static const String createWithdraw = 'withdraws/';
  static String userWithdraws(int userId) => 'withdraws/user/$userId';
  static String withdrawById(int withdrawId, int userId) =>
      'withdraws/$withdrawId/user/$userId';


  // ── Payments ───────────────────────────────────────────
  static String createDeposit(int userId) => 'payments/deposit/$userId';
  static String paymentById(int id) => 'payments/$id';
  static String paymentByGatewayTxnId(String gatewayTransactionId) =>
      'payments/gateway/$gatewayTransactionId';
  static String userPayments(int userId) => 'payments/user/$userId';








}
