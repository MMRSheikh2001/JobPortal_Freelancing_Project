export interface ReferenceRequestModel {

    name: string;
    organization: string;
    designation: string;

    phone: string;
    email: string;
    address: string;

    relation: string;
    userProfileId: number;


}

export interface ReferenceResponseModel {

    id: number;

    name: string;
    organization: string;
    designation: string;

    phone: string;
    email: string;
    address: string;

    relation: string;

    userProfileId: number;
    userName: string;

    userId: number;
    userEmail: string;




}