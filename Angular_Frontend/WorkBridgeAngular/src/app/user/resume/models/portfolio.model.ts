export interface PortfolioRequestModel{
    title:string;
    description:string;
    projectUrl:string;
    technologies:string;
    userProfileId:number;
}

export interface PortfolioResponseModel{

    id:number;
     title:string;
    description:string;
    projectUrl:string;

    fileUrl:string;

    technologies:string;

    createdAt:string;
    updatedAt:string;

    userProfileId:number;
    userName:string;

    userId:number;
    userEmail:string;



}