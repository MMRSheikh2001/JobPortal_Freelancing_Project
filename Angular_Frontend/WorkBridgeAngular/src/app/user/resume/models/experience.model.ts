import { EmploymentType } from "../../../enums/employment-type.enum";

export interface ExperienceRequestModel {

    companyName: string;
    position: string;
    responsibilities: string;
    achievements: string;

    startDate: string;
    endDate: string;
    employmentType: EmploymentType;
    userProfileId: number


}

export interface ExperienceResponseModel {
    id: number;

    companyName: string;
    position: string;
    responsibilities: string;
    achievements: string;

    startDate: string;
    endDate: string;
    employmentType: EmploymentType;

    currentlyWorking: boolean;

    createdAt: string;
    updatedAt: string;

    userProfileId: number;
    userId: number;

    userName: string;
    userEmail: string;




}