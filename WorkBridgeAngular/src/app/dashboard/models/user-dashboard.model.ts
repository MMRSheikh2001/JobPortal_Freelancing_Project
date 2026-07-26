import { JobResponseModel } from "../../company/company-profile/models/job.model";
import { GigOrderResponseDTO } from "../../gig/models/gig-order.model";
import { GigResponseModel } from "../../gig/models/gig.model";
import { JobApplicationResponseModel } from "../../jobapplication/models/job-application.model";

export interface UserDashboardDTO {
    userName: string;
    profileImage: string;
    profileCompletion: number;
    appliedJobs: number;
    savedJobs: number;
    savedGigs: number;
    activeOrders: number;
    unreadMessages: number;
    unreadNotifications: number;
    recentApplications: JobApplicationResponseModel[];
    recentOrders: GigOrderResponseDTO[];
    latestJobs: JobResponseModel[];
    popularGigs: GigResponseModel[];
}