import { EducationLevel } from "../../../enums/education-level.enum";
import { ResultType } from "../../../enums/result-type.enum";

export interface EducationRequestModel {

    educationLevel: EducationLevel;
    board: string;
    institution: string;
    fieldOfStudy: string;

    resultType: ResultType;
    result?: number;
    outOf?: number;
    gradeOrDivision: string;

    startDate: string;
    endDate: string;

    userProfileId: number;

}

export interface EducationResponseModel {

    id: number;
    educationLevel: EducationLevel;
    board: string;
    institution: string;
    fieldOfStudy: string;

    resultType: ResultType;
    result: number;
    outOf: number;
    gradeOrDivision: string;

    startDate: string;
    endDate: string;

    currentlyStudying: boolean;

    createdAt: string;
    userProfileId: number;
    userId: number;

    userName: string;
    userEmail: string;


}