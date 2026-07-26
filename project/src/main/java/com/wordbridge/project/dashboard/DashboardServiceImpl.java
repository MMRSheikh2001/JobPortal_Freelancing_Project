package com.wordbridge.project.dashboard;

import com.wordbridge.project.conversation.ConversationService;
import com.wordbridge.project.dto.responsedto.CompanyProfileResponseDTO;
import com.wordbridge.project.dto.responsedto.UserProfileResponseDTO;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.ApplicationStatus;
import com.wordbridge.project.enums.GigOrderStatus;
import com.wordbridge.project.enums.ReportStatus;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.gig.GigService;
import com.wordbridge.project.gigorder.GigOrderService;
import com.wordbridge.project.job.JobRepository;
import com.wordbridge.project.job.JobService;
import com.wordbridge.project.jobapplication.JobApplicationRepository;
import com.wordbridge.project.jobapplication.JobApplicationService;
import com.wordbridge.project.message.MessageService;
import com.wordbridge.project.notification.NotificationService;
import com.wordbridge.project.report.ReportService;
import com.wordbridge.project.repository.CompanyProfileRepository;
import com.wordbridge.project.repository.UserRepository;
import com.wordbridge.project.saved.SavedGigService;
import com.wordbridge.project.saved.SavedJobService;
import com.wordbridge.project.service.CompanyProfileService;
import com.wordbridge.project.service.UserProfileService;
import com.wordbridge.project.wallet.WalletService;
import com.wordbridge.project.withdraw.WithdrawRepository;
import com.wordbridge.project.withdraw.WithdrawStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final UserRepository userRepository;

    private final JobRepository jobRepository;
    private final CompanyProfileRepository companyProfileRepository;

    private final JobApplicationRepository jobApplicationRepository;


    private final UserProfileService userProfileService;
    private final CompanyProfileService companyProfileService;

    private final JobApplicationService jobApplicationService;

    private final SavedJobService savedJobService;

    private final SavedGigService savedGigService;

    private final GigOrderService gigOrderService;

    private final NotificationService notificationService;

    private final ConversationService conversationService;
    private final MessageService messageService;

    private final JobService jobService;

    private final GigService gigService;
    private final WalletService walletService;
    private final WithdrawRepository withdrawRepository;
    private final ReportService reportService;


    @Override
    public CompanyDashboardDTO getCompanyDashboard(Long companyProfileId) {
        CompanyDashboardDTO dto = new CompanyDashboardDTO();

        CompanyProfileResponseDTO companyProfile = companyProfileService.findById(companyProfileId);

        dto.setCompanyName(companyProfile.getName());
        dto.setProfileImage(companyProfile.getImage());
        dto.setProfileCompletion(
                companyProfileService.getProfileCompletionPercentage(companyProfile.getUserId())
        );

        dto.setTotalJobs(
                jobRepository.countByCompanyProfileId(companyProfileId));

        dto.setActiveJobs(
                jobRepository.countByCompanyProfileIdAndIsActiveTrue(companyProfileId));

        dto.setInactiveJobs(
                jobRepository.countByCompanyProfileIdAndIsActiveFalse(companyProfileId));

        dto.setTotalApplications(
                jobApplicationRepository.countByJobCompanyProfileId(companyProfileId));

        dto.setAppliedApplications(
                jobApplicationRepository.countByJobCompanyProfileIdAndStatus(
                        companyProfileId,
                        ApplicationStatus.APPLIED));

        dto.setAiCompletedApplications(
                jobApplicationRepository.countByJobCompanyProfileIdAndStatus(
                        companyProfileId,
                        ApplicationStatus.AI_COMPLETED));

        dto.setAutomaticQualifiedApplications(
                jobApplicationRepository.countByJobCompanyProfileIdAndStatus(
                        companyProfileId,
                        ApplicationStatus.AUTOMATIC_QUALIFIED));

        dto.setCompanyShortlistedApplications(
                jobApplicationRepository.countByJobCompanyProfileIdAndStatus(
                        companyProfileId,
                        ApplicationStatus.COMPANY_SHORTLISTED));

        dto.setHiredApplications(
                jobApplicationRepository.countByJobCompanyProfileIdAndStatus(
                        companyProfileId,
                        ApplicationStatus.HIRED));

        dto.setUnreadMessages(
                messageService.countUnreadMessagesByUserId(companyProfile.getUserId())
        );

        dto.setUnreadNotifications(notificationService.getUnreadCount(companyProfile.getUserId()));

        return dto;
    }

    @Override
    public UserDashboardDTO getUserDashboard(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No user found"));

        UserDashboardDTO dto = new UserDashboardDTO();
        if (user.getRole() == UserRole.USER) {
            dto.setUserName(user.getUserProfile().getName());
            dto.setProfileImage(user.getUserProfile().getImage());
            dto.setProfileCompletion(
                    userProfileService.getProfileCompletionPercentage(userId)
            );
            dto.setAppliedJobs(jobApplicationService.countByUserProfileId(user.getUserProfile().getId()));
        } else if (user.getRole() == UserRole.COMPANY) {
            dto.setUserName(user.getCompanyProfile().getName());
            dto.setProfileImage(user.getCompanyProfile().getImage());
            dto.setProfileCompletion(
                    companyProfileService.getProfileCompletionPercentage(userId)
            );
        } else {
            dto.setUserName("ADMIN");
            dto.setProfileCompletion(100);
        }

        dto.setSavedJobs(savedJobService.countByUserId(userId));
        dto.setSavedGigs(savedGigService.countByUserId(userId));

        dto.setActiveOrders(gigOrderService.countActiveByBuyerId(userId));

        dto.setUnreadMessages(
                messageService.countUnreadMessagesByUserId(userId)
        );

        dto.setUnreadNotifications(notificationService.getUnreadCount(userId));

        dto.setRecentApplications(
                jobApplicationService.getRecentApplicationsByUserId(userId)
        );
        dto.setRecentOrders(
                gigOrderService.getRecentOrdersByUserId(userId)
        );

        dto.setLatestJobs(jobService.findTop10ByIsActiveTrueOrderByCreatedAtDesc());
        dto.setPopularGigs(gigService.getPopularGigs());


        return dto;
    }

    @Override
    public AdminDashboardDTO getAdminDashboard(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No user found"));
        if (user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only admin can get data");
        }


        AdminDashboardDTO dto = new AdminDashboardDTO();

        dto.setTotalUsers(userRepository.count());
        dto.setTotalJobSeekers(userProfileService.countAllJobSeeker());
        dto.setTotalCompanies(companyProfileService.count());

        dto.setTotalFreelancers(gigService.countFreelancers());
        dto.setTotalClients(gigService.countClients());
        dto.setTotalActiveFreelancers(gigService.countActiveFreelancers());
        dto.setTotalActiveClients(gigOrderService.countActiveClients());

        dto.setTotalJobs(jobRepository.count());
        dto.setActiveJobs(jobRepository.countByIsActiveTrue());
        dto.setInactiveJobs(jobRepository.countByIsActiveFalse());
        dto.setTotalJobApplications(jobApplicationRepository.count());
        dto.setTotalHiredCandidates(jobApplicationService.countAllHiredCandidates());

        dto.setTotalGigs(gigService.countTotalGig());
        dto.setActiveGigs(gigService.countActiveGigs());
        dto.setInactiveGigs(gigService.countInactiveGigs());
        dto.setTotalOrders(gigOrderService.countTotalOrders());
        dto.setCompletedOrders(gigOrderService.countCompletedOrders());
        dto.setCancelledOrders(gigOrderService.countCancelledOrders());

        dto.setTotalPlatformRevenue(walletService.getTotalPlatformMoney());
        dto.setPendingWithdrawals(withdrawRepository.sumAmountByStatus(WithdrawStatus.PENDING));
        dto.setCompletedWithdrawals(withdrawRepository.sumAmountByStatus(WithdrawStatus.APPROVED));

        dto.setTotalAIInterviews(jobApplicationRepository.countByStatus(ApplicationStatus.AI_COMPLETED));
        dto.setTotalAIMatches(jobApplicationRepository.countApplicationsWithAIMatchScore());
        dto.setTotalAIQualifiedCandidates(jobApplicationRepository.countByStatus(ApplicationStatus.AUTOMATIC_QUALIFIED));

        dto.setPendingReports(reportService.countByStatus(ReportStatus.OPEN));
        dto.setPendingDisputes(gigOrderService.countByStatus(GigOrderStatus.SELLER_DISPUTED));
        dto.setBlockedUsers(userRepository.countByIsSuspendedTrue());
        dto.setUnreadNotifications(notificationService.getUnreadCount(userId));


        return dto;
    }

    @Override
    public FreelancerDashboardDTO getFreelancerDashboard(Long userProfileId) {
        UserProfileResponseDTO userProfile = userProfileService.findById(userProfileId);

        FreelancerDashboardDTO dto = new FreelancerDashboardDTO();
        dto.setUserName(userProfile.getName());
        dto.setProfileImage(userProfile.getImage());
        dto.setProfileCompletion(
                userProfileService.getProfileCompletionPercentage(userProfile.getUserId())
        );

        dto.setTotalGigs(gigService.countByUserProfileId(userProfileId));
        dto.setActiveGigs(gigService.countByUserProfileIdAndIsActiveTrue(userProfileId));
        dto.setInactiveGigs(gigService.countByUserProfileIdAndIsActiveFalse(userProfileId));

        dto.setTotalOrders(gigOrderService.countByGigUserProfileId(userProfileId));
        dto.setPendingOrders(gigOrderService.countSellerPendingOrders(userProfileId));
        dto.setInProgressOrders(gigOrderService.countSellerInProgressOrders(userProfileId));
        dto.setDeliveredOrders(gigOrderService.countSellerDeliveredOrders(userProfileId));
        dto.setCompletedOrders(gigOrderService.countSellerCompletedOrders(userProfileId));
        dto.setCancelledOrders(gigOrderService.countSellerCancelledOrders(userProfileId));
        dto.setDisputedOrders(gigOrderService.countSellerDisputedOrders(userProfileId));

        dto.setWalletBalance(walletService.getBalance(userProfile.getUserId()));
        dto.setLifetimeEarnings(gigOrderService.getLifetimeEarnings(userProfileId));
        dto.setPendingWithdrawals(withdrawRepository.getPendingWithdrawals(userProfile.getUserId()));

        dto.setAverageRating(gigService.getFreelancerAverageRating(userProfileId));
        dto.setTotalReviews(gigService.getTotalReviews(userProfileId));

        dto.setActiveConversations(conversationService.countActiveConversations(userProfileId));
        dto.setUnreadMessages(messageService.countUnreadMessagesByUserId(userProfile.getUserId()));
        dto.setUnreadNotifications(notificationService.getUnreadCount(userProfile.getUserId()));

        dto.setRecentOrders(
                gigOrderService.getRecentOrdersByFreelancerId(
                        userProfileId
                )
        );

        dto.setMyPopularGigs(gigService.getPopularGigsByFreelancer(userProfileId));


        return dto;
    }

    @Override
    public HomeStatisticsDTO getHomeStatistics() {
        HomeStatisticsDTO dto = new HomeStatisticsDTO();

        dto.setTotalUsers(
                userRepository.count());

        dto.setTotalCompanies(
                companyProfileRepository.count());

        dto.setTotalFreelancers(
                gigService.countFreelancers());

        dto.setTotalJobs(
                jobRepository.count());

        dto.setActiveJobs(
                jobRepository.countByIsActiveTrue());

        dto.setTotalGigs(
                gigService.countTotalGig());

        dto.setActiveGigs(
                gigService.countActiveGigs());

        dto.setCompletedOrders(
                gigOrderService.countCompletedOrders());

        return dto;
    }


}
