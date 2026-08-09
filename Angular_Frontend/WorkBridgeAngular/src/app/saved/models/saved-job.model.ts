export interface SavedJobResponseDTO {
    id: number;
    createdAt: string;
    userId: number;
    userName: string;
    jobId: number;
    jobTitle: string;
    jobDescription: string;
    companyName: string;
    companyLogo: string;
}