import { Routes } from '@angular/router';
import { Register } from './auth/register/register';
import { Login } from './auth/login/login';
import { ForgotPassword } from './auth/forgot-password/forgot-password';
import { VerifyEmail } from './auth/verify-email/verify-email';

import { UserDashboard } from './user/user-dashboard/user-dashboard';
import { authGuard } from './auth/guards/auth-guard';
import { roleGuard } from './auth/guards/role-guard';
import { CompanyDashboard } from './company/company-dashboard/company-dashboard';
import { AdminDashboard } from './admin/admin-dashboard/admin-dashboard';
import { ResetPassword } from './auth/reset-password/reset-password';
import { Country } from './admin/address/country/country';
import { Division } from './admin/address/division/division';
import { District } from './admin/address/district/district';
import { Policestation } from './admin/address/policestation/policestation';
import { Category } from './admin/cvinformations/category/category';
import { Skill } from './admin/cvinformations/skill/skill';
import { Language } from './admin/cvinformations/language/language';
import { UserProfile } from './user/resume/components/user-profile/user-profile';
import { UserSkill } from './user/resume/components/user-skill/user-skill';
import { UserLanguage } from './user/resume/components/user-language/user-language';
import { Education } from './user/resume/components/education/education';
import { Experience } from './user/resume/components/experience/experience';
import { Extracurricular } from './user/resume/components/extracurricular/extracurricular';
import { Portfolio } from './user/resume/components/portfolio/portfolio';
import { Reference } from './user/resume/components/reference/reference';
import { ResumePreview } from './user/resume/components/resume-preview/resume-preview';
import { ResumeUpload } from './user/resume/components/resume-upload/resume-upload';
import { Training } from './user/resume/components/training/training';
import { FullUserProfilePreview } from './user/resume/components/full-user-profile-preview/full-user-profile-preview';
import { ResumeDashboard } from './user/resume/components/resume-dashboard/resume-dashboard';
import { CompanyProfile } from './company/company-profile/components/company-profile/company-profile';
import { CompanyProfilePreview } from './company/company-profile/components/company-profile-preview/company-profile-preview';
import { ManageJobs } from './company/company-profile/components/manage-jobs/manage-jobs';
import { JobList } from './company/company-profile/components/job-list/job-list';
import { JobDetails } from './company/company-profile/components/job-details/job-details';
import { MyApplications } from './jobapplication/components/my-applications/my-applications';
import { AiInterview } from './jobapplication/components/ai-interview/ai-interview';
import { ApplicationDetails } from './jobapplication/components/application-details/application-details';
import { CompanyJobApplications } from './jobapplication/components/company-job-applications/company-job-applications';
import { AiEvaluation } from './jobapplication/components/ai-evaluation/ai-evaluation';
import { Home } from './layout/shared/home/home';
import { UserLayout } from './layout/user/user-layout/user-layout';
import { CompanyLayout } from './layout/company/company-layout/company-layout';
import { AdminLayout } from './layout/admin/admin-layout/admin-layout';
import { GuestLayout } from './layout/guest/guest-layout/guest-layout';
import { PublicJobList } from './layout/shared/public-job-list/public-job-list';
import { CreateEditGig } from './gig/components/create-edit-gig/create-edit-gig';
import { FreelancerGigOrders } from './gig/components/freelancer-gig-orders/freelancer-gig-orders';
import { GigDetails } from './gig/components/gig-details/gig-details';
import { MyGigs } from './gig/components/my-gigs/my-gigs';
import { BuyerGigOrders } from './gig/components/buyer-gig-orders/buyer-gig-orders';
import { BuyerGigDetails } from './gig/components/buyer-gig-details/buyer-gig-details';
import { FreelancerGigDetails } from './gig/components/freelancer-gig-details/freelancer-gig-details';
import { ConversationList } from './conversation/components/conversation-list/conversation-list';
import { ConversationChat } from './conversation/components/conversation-chat/conversation-chat';
import { AdminGigOrderDetails } from './gig/components/admin-gig-order-details/admin-gig-order-details';
import { GigOrderDisputeList } from './gig/components/gig-order-dispute-list/gig-order-dispute-list';
import { PublicGigList } from './layout/shared/public-gig-list/public-gig-list';
import { BuyerReview } from './gig/components/buyer-review/buyer-review';
import { PublicGigReviews } from './gig/components/public-gig-reviews/public-gig-reviews';
import { SellerProfileView } from './gig/components/seller-profile-view/seller-profile-view';
import { WalletHomeComponent } from './wallet/components/wallet-home.component/wallet-home.component';
import { TransactionHistoryComponent } from './wallet/components/transaction-history.component/transaction-history.component';
import { DepositComponent } from './wallet/components/deposit.component/deposit.component';
import { PaymentSuccess } from './wallet/components/payment-success/payment-success';
import { PaymentFailure } from './wallet/components/payment-failure/payment-failure';
import { PaymentCancel } from './wallet/components/payment-cancel/payment-cancel';
import { UserWithdraw } from './wallet/components/user-withdraw/user-withdraw';
import { AdminWithdraw } from './wallet/components/admin-withdraw/admin-withdraw';
import { AdminTransactionHistoryComponent } from './wallet/components/admin-transaction-history.component/admin-transaction-history.component';
import { NotificationsComponent } from './notification/notifications.component/notifications.component';
import { NotificationDetailsComponent } from './notification/notification-details.component/notification-details.component';
import { MySavedGigsComponent } from './saved/my-saved-gigs.component/my-saved-gigs.component';
import { MySavedJobsComponent } from './saved/my-saved-jobs.component/my-saved-jobs.component';
import { FreelancerDashboard } from './dashboard/freelancer-dashboard/freelancer-dashboard';
import { SearchCompaniesComponent } from './layout/shared/search-companies.component/search-companies.component';
import { CompanyPublicPage } from './company/company-public-page/company-public-page';
import { AdminUserManagement } from './admin-control/admin-user-management/admin-user-management';
import { AdminCompanyManagement } from './admin-control/admin-company-management/admin-company-management';
import { AdminUserProfileManagement } from './admin-control/admin-user-profile-management/admin-user-profile-management';
import { AdminUserProfilePreview } from './admin-control/admin-user-profile-preview/admin-user-profile-preview';
import { FreelancerAllGigs } from './admin-control/freelancer-all-gigs/freelancer-all-gigs';
import { AdminGigManagement } from './admin-control/admin-gig-management/admin-gig-management';
import { AdminJobManagement } from './admin-control/admin-job-management/admin-job-management';
import { AdminJobApplicationManagement } from './admin-control/admin-job-application-management/admin-job-application-management';
import { AdminGigOrderManagement } from './admin-control/admin-gig-order-management/admin-gig-order-management';
import { UserReportComponent } from './report/user-report.component/user-report.component';
import { AdminReportManagementComponent } from './report/admin-report-management.component/admin-report-management.component';
import { AdminReportDetailsComponent } from './report/admin-report-details.component/admin-report-details.component';

export const routes: Routes = [



    {
        path: 'company/resume-preview/:userProfileId',
        component: ResumePreview,
        canActivate: [
            authGuard
        ]
    },
    {
        path: 'user-report',
        component: UserReportComponent,
        canActivate: [
            authGuard
        ]
    },

    {
        path: '',
        component: GuestLayout,
        children: [

            { path: '', component: Home },

            { path: 'login', component: Login },

            { path: 'register', component: Register },

            { path: 'forgot-password', component: ForgotPassword },

            { path: 'reset-password', component: ResetPassword },

            { path: 'verify-email', component: VerifyEmail },
            {
                path: 'job-details/:id', component: JobDetails
            },
            { path: 'job-list', component: PublicJobList },
            {
                path: 'gig-details/:id', component: GigDetails
            },
            {
                path: 'seller-profile-view/:userProfileId', component: SellerProfileView
            },

            {
                path: 'gig-reviews/:id', component: PublicGigReviews
            }
            ,
            { path: 'gig-list', component: PublicGigList },

            { path: 'companies', component: SearchCompaniesComponent },

            { path: 'company-profile/:companyId', component: CompanyPublicPage },


        ]
    },

    {
        path: 'user',
        component: UserLayout,
        canActivate: [
            authGuard,
            roleGuard(['USER'])
        ],
        children: [

            { path: 'dashboard', component: UserDashboard },
            { path: 'freelancer-dashboard', component: FreelancerDashboard },

            { path: 'user-profile', component: UserProfile },

            { path: 'user-skill', component: UserSkill },

            { path: 'user-language', component: UserLanguage },

            { path: 'education', component: Education },

            { path: 'experience', component: Experience },

            { path: 'extracurricular', component: Extracurricular },

            { path: 'portfolio', component: Portfolio },

            { path: 'reference', component: Reference },

            { path: 'user-profile-preview', component: FullUserProfilePreview },

            { path: 'resume-dashboard', component: ResumeDashboard },

            { path: 'resume-preview', component: ResumePreview },

            { path: 'resume-control', component: ResumeUpload },

            { path: 'training', component: Training },

            { path: 'my-applications', component: MyApplications },
            { path: 'saved-jobs', component: MySavedJobsComponent },

            { path: 'ai-interview/:applicationId', component: AiInterview },

            { path: 'ai-evaluation/:applicationId', component: AiEvaluation },
            { path: 'manage-gig', component: CreateEditGig },
            { path: 'freelancer-gigs', component: MyGigs },
            { path: 'manage-gig/:id', component: CreateEditGig },
            { path: 'freelancer-gig-orders/:gigId', component: FreelancerGigOrders },
            { path: 'buyer-gig-orders', component: BuyerGigOrders },
            { path: 'buyer-gig-details/:gigOrderId', component: BuyerGigDetails },
            { path: 'freelancer-gig-details/:gigOrderId', component: FreelancerGigDetails },
            { path: 'saved-gigs', component: MySavedGigsComponent },

            {
                path: 'conversation-list',
                component: ConversationList,
                children: [
                    {
                        path: ':conversationId',
                        component: ConversationChat
                    }
                ]
            },
            {
                path: 'buyer-review/:gigOrderId',
                component: BuyerReview
            },
            { path: 'wallet', component: WalletHomeComponent },

            { path: 'wallet/transactions', component: TransactionHistoryComponent },

            { path: 'wallet/deposit', component: DepositComponent },

            { path: 'payment/success', component: PaymentSuccess },
            { path: 'payment/failure', component: PaymentFailure },
            { path: 'payment/cancel', component: PaymentCancel },

            { path: 'wallet/withdraw', component: UserWithdraw },
            { path: 'notifications', component: NotificationsComponent },
            { path: 'notification-details/:notificationId', component: NotificationDetailsComponent },



        ]
    }, {
        path: 'company',
        component: CompanyLayout,
        canActivate: [
            authGuard,
            roleGuard(['COMPANY'])
        ],
        children: [

            { path: 'dashboard', component: CompanyDashboard },

            { path: 'company-profile', component: CompanyProfile },

            { path: 'manage-jobs', component: ManageJobs },

            { path: 'manage-jobs/:id', component: ManageJobs },

            { path: 'job-list', component: JobList },

            {
                path: 'job-applications/:jobId',
                component: CompanyJobApplications
            },

            {
                path: 'application-details/:applicationId',
                component: ApplicationDetails
            },

            {
                path: 'resume-preview/:userProfileId',
                component: ResumePreview
            },

            {
                path: 'company-profile-preview',
                component: CompanyProfilePreview
            },
            { path: 'buyer-gig-orders', component: BuyerGigOrders },
            { path: 'buyer-gig-details/:gigOrderId', component: BuyerGigDetails },
            {
                path: 'conversation-list',
                component: ConversationList,
                children: [
                    {
                        path: ':conversationId',
                        component: ConversationChat
                    }
                ]
            },
            {
                path: 'buyer-review/:gigOrderId',
                component: BuyerReview
            },
            { path: 'saved-gigs', component: MySavedGigsComponent },
            { path: 'wallet', component: WalletHomeComponent },

            { path: 'wallet/transactions', component: TransactionHistoryComponent },
            { path: 'wallet/deposit', component: DepositComponent },

            { path: 'payment/success', component: PaymentSuccess },
            { path: 'payment/failure', component: PaymentFailure },
            { path: 'payment/cancel', component: PaymentCancel },
            { path: 'wallet/withdraw', component: UserWithdraw },
            { path: 'notifications', component: NotificationsComponent },
            { path: 'notification-details/:notificationId', component: NotificationDetailsComponent },


        ]
    }, {
        path: 'admin',
        component: AdminLayout,
        canActivate: [
            authGuard,
            roleGuard(['ADMIN'])
        ],
        children: [

            { path: 'dashboard', component: AdminDashboard },

            { path: 'country', component: Country },

            { path: 'division', component: Division },

            { path: 'district', component: District },

            { path: 'police-station', component: Policestation },

            { path: 'category', component: Category },

            { path: 'skill', component: Skill },

            { path: 'language', component: Language },
            { path: 'gig-order-details/:gigOrderId', component: AdminGigOrderDetails },

            { path: 'gig-order-disputes', component: GigOrderDisputeList },

            { path: 'wallet', component: WalletHomeComponent },

            { path: 'wallet/transactions', component: TransactionHistoryComponent },
            { path: 'wallet/withdraw', component: AdminWithdraw },
            { path: 'wallet/all-transactions', component: AdminTransactionHistoryComponent },
            { path: 'notifications', component: NotificationsComponent },
            { path: 'notification-details/:notificationId', component: NotificationDetailsComponent },

            { path: 'user-management', component: AdminUserManagement },

            { path: 'company-management', component: AdminCompanyManagement },

            { path: 'user-profile-management', component: AdminUserProfileManagement },
            { path: 'user-profile-review/:userProfileId', component: AdminUserProfilePreview },
            { path: 'freelancer-gigs/:userProfileId', component: FreelancerAllGigs },

            { path: 'gigs-management', component: AdminGigManagement },
            { path: 'jobs-management', component: AdminJobManagement },
            { path: 'job-applications-management', component: AdminJobApplicationManagement },
            { path: 'gig-orders-management', component: AdminGigOrderManagement },
            { path: 'reports-management', component: AdminReportManagementComponent },
            { path: 'report-details/:reportId', component: AdminReportDetailsComponent },




        ]
    }


];
