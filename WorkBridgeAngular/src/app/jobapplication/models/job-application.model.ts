import { ApplicationStatus } from "../../enums/application-status.enum";


export interface JobApplicationRequestModel {
  jobId: number;
  userProfileId: number;
}



export interface JobApplicationResponseModel {
  id: number;
  status: ApplicationStatus;
  appliedAt: string;
  aiDeadlineDate: string;

  companyNotes: string;
  jobId: number;
  jobTitle: string;
  jobDescription: string;
  companyProfileId: number;
  companyName: string;
  companyUserId: number;
  companyUserEmail: string;

  companyLogo: string;

  userProfileId: number;
  userName: string;
  userImage: string;
  userId: number;
  userEmail: string;
  aiMatchScore: number;
  aiMatchFeedback: string;
  aiInterviewScore: number;
  aiFinalScore: number;
  aiInterviewCompleted: boolean;
  aiCompletedAt: string;     // LocalDateTime mapped to string
  aiShortlisted: boolean;
  aiScreeningEnabled: boolean;
  aiCvScreeningEnabled: boolean;
  aiInterviewEnabled: boolean;
  aiMatchThreshold: number;
  aiQuestionCount: number;
}

export interface InterviewQuestion {
  question?: string;
  answer?: string;
  score?: number;
}



export interface AIInterviewSessionResponseDTO {

  applicationId?: number;
  startedAt?: string;
  completedAt?: string;
  totalScore?: number;
  completed?: boolean;
  questions?: InterviewQuestion[];
}

export interface JobApplicationFilterRequestDTO {
  keyword?: string;
  companyProfileId?: number;
  jobId?: number;
  userProfileId?: number;
  categoryId?: number;
  status?: ApplicationStatus;
  aiCompleted?: boolean;
  aiShortlisted?: boolean;
  appliedFrom?: string;
  appliedTo?: string;
}