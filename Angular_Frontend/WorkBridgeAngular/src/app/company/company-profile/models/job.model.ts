import { EmploymentType } from "../../../enums/employment-type.enum";
import { WorkPlaceType } from "../../../enums/work-place-type.enum";









export interface JobRequestModel {
    title: string;
    jobDescription: string;
    jobResponsibilities: string;
    educationalRequirements: string;
    experienceRequirements: string;
    minExperience: number;
    maxExperience: number;
    additionalRequirements: string;
    benefits: string;
    salaryMin: number;
    salaryMax: number;
    isNegotiable: boolean;
    applicationDeadline: string;
    vacancy: number;
    employmentType: EmploymentType;
    workPlaceType: WorkPlaceType;
    companyProfileId: number;
    locationPoliceStationId: number;
    categoryId: number;
    aiScreeningEnabled: boolean;
    aiCvScreeningEnabled: boolean;
    aiInterviewEnabled: boolean;
    aiMatchThreshold: number;
    aiQuestionCount: number;
    aiShortlistCount: number;
    aiDeadlineDays: number;
}





export interface JobResponseModel {
    id: number;
    title: string;
    jobDescription: string;
    jobResponsibilities: string;
    educationalRequirements: string;
    experienceRequirements: string;
    minExperience: number;
    maxExperience: number;
    additionalRequirements: string;
    benefits: string;
    salaryMin: number;
    salaryMax: number;
    isNegotiable: boolean;
    applicationDeadline: string;
    isActive: boolean;
    vacancy: number;
    employmentType: EmploymentType;
    workPlaceType: WorkPlaceType;
    createdAt: string;
    updatedAt: string;
    companyProfileId: number;
    userId: number;
    userEmail: string;
    companyName: string;
    companyEmail: string;
    companyPhone: string;
    companyDescription: string;
    companyWebsite: string;
    companyLogo: string;
    locationCountryId: number;
    locationCountryName: string;
    locationCountryCode: string;
    locationDivisionId: number;
    locationDivisionName: string;
    locationDistrictId: number;
    locationDistrictName: string;
    locationPoliceStationId: number;
    locationPoliceStationName: string;
    categoryId: number;
    categoryName: string;
    aiScreeningEnabled: boolean;
    aiCvScreeningEnabled: boolean;
    aiInterviewEnabled: boolean;
    aiMatchThreshold: number;
    aiQuestionCount: number;
    aiShortlistCount: number;
    aiDeadlineDays: number;
}





export interface JobSearchRequestModel {
    keyword: string;
    categoryId: number;
    countryId: number;
    divisionId: number;
    districtId: number;
    policeStationId: number;
    employmentType: EmploymentType;
    workPlaceType: WorkPlaceType;
    minSalary: number;
    maxSalary: number;
    active: boolean;
}