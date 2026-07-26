import { ReportStatus } from "../../enums/report-status.enum";
import { ReportType } from "../../enums/report-type.enum";
import { UserRole } from "../../enums/user-role.enum";

export interface ReportResponseDTO {
    id: number;
    type: ReportType;
    subject: string;
    description: string;
    attachmentUrl: string;
    status: ReportStatus;
    adminReply: string;
    createdAt: string;
    resolvedAt: string;
    userId: number;
    userName: string;
    profileId: number;
    userRole: UserRole;
    userEmail: string;
}

export interface ReportFilterRequestDTO {
    keyword?: string;
    userId?: number;
    userRole?: UserRole;
    type?: ReportType;
    status?: ReportStatus;
    createdFrom?: string;
    createdTo?: string;
}