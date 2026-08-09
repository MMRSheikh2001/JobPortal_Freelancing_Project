export interface SkillRequestModel{
    skillName:string;
    categoryId:number;
}

export interface SkillResponseModel{
    skillId:number;
    skillName:string;
    categoryId:number;
    categoryName:string;
    categoryDescription:string
}