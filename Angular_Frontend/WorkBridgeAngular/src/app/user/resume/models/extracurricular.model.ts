export interface ExtracurricularRequestModel {

    title: string;
    description: string;
    organization: string;
    role: string;
    userProfileId: number;
}

export interface ExtracurricularResponseModel {

    id: number;

    title: string;
    description: string;
    organization: string;
    role: string;
    userProfileId: number;

    userId: number;
    userName: string;
    userEmail: string;


}