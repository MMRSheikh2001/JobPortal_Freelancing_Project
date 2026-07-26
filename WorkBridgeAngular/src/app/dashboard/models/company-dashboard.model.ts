export interface CompanyDashboardDTO {
    companyName: string;
    profileImage: string;
    profileCompletion: number;
    totalJobs: number;
    activeJobs: number;
    inactiveJobs: number;
    totalApplications: number;
    appliedApplications: number;
    aiCompletedApplications: number;
    automaticQualifiedApplications: number;
    companyShortlistedApplications: number;
    hiredApplications: number;
    unreadMessages: number;
    unreadNotifications: number;
}