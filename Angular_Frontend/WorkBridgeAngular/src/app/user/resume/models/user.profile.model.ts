import { Gender } from "../../../enums/gender.enum";
import { JobType } from "../../../enums/job-type.enum";
import { WorkPlaceType } from "../../../enums/work-place-type.enum";

export interface UserProfileRequestModel {

    userId: number;
    name: string;
    phone: string;

    headline: string;
    professionalSummary: string;
    bio: string;

    dateOfBirth: String;
    gender: Gender;
    nationality: string;
    religion: string;
    maritalStatus: string;

    fatherName: string;
    motherName: string;

    nidNumber: string;
    passportNumber: string;

    githubLink: string;
    linkedinLink: string;
    portfolioWebsite: string;

    expectedSalary: number;
    currentSalary: number;

    preferredJobType: JobType;
    preferredWorkplace: WorkPlaceType;

    careerObjective: string;
    freelancerTitle: string;

    presentAddressId?: number;
    presentAddressDetails: string;
    presentAddressPostCode: string;
    presentAddressPoliceStationId: number;

    permanentAddressId?: number;
    permanentAddressDetails: string;
    permanentAddressPostCode: string;
    permanentAddressPoliceStationId: number;


}

export interface UserProfileResponseModel {

    id: number;
    userId: number;
    userEmail: string;

    name: string;
    phone: string;
    image: string;

    headline: string;
    professionalSummary: string;
    bio: string;

    dateOfBirth: String;
    gender: Gender;
    nationality: string;
    religion: string;
    maritalStatus: string;

    fatherName: string;
    motherName: string;



    nidNumber: string;
    passportNumber: string;

    githubLink: string;
    linkedinLink: string;
    portfolioWebsite: string;

    expectedSalary: number;
    currentSalary: number;

    preferredJobType: JobType;
    preferredWorkplace: WorkPlaceType;

    careerObjective: string;
    freelancerTitle: string;

    presentAddressId: number;
    presentAddressDetails: string;
    presentAddressPostCode: string;

    presentCountryId: number;
    presentCountryName: string;
    presentCountryCode: string;

    presentDivisionId: number;
    presentDivisionName: string;

    presentDistrictId: number;
    presentDistrictName: string;

    presentPoliceStationId: number;
    presentPoliceStationName: string

    permanentAddressId: number;
    permanentAddressDetails: string;
    permanentAddressPostCode: string;

    permanentCountryId: number;
    permanentCountryName: string;
    permanentCountryCode: string;

    permanentDivisionId: number;
    permanentDivisionName: string;

    permanentDistrictId: number;
    permanentDistrictName: string;

    permanentPoliceStationId: number;
    permanentPoliceStationName: string



    profileCompleted: boolean;
    createdAt: string;
    updatedAt: string;

}