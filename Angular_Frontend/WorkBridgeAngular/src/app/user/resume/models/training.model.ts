import { TrainingType } from "../../../enums/training-type.enum";

export interface TrainingRequestModel {

    name: string;
    description: string;
    institution: string;
    startDate: string;
    endDate: string;
    duration: string;

    certificateVerificationUrl: string;
    certificateId: string;

    trainingType: TrainingType;

    userProfileId: number;


}

export interface TrainingResponseModel {

    id: number;

    name: string;
    description: string;
    institution: string;
    startDate: string;
    endDate: string;
    completed: boolean;

    duration: string;

    certificateFile: string;
    certificateVerificationUrl: string;
    certificateId: string;

    trainingType: TrainingType;

    createdAt: string;
    updatedAt: string;

    userProfileId: number;

    userName: string;

    userId: number;
    userEmail: string

}