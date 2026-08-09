import { ProficiencyLevel } from "../../../enums/proficiency-level.enum";

export interface UserSkillRequestModel {

    proficiencyLevel: ProficiencyLevel;
    yearsOfExperience: number;
    userProfileId: number;
    skillId: number


}

export interface UserSkillResponseModel {

    id: number;
    proficiencyLevel: ProficiencyLevel;
    yearsOfExperience: number;
    createdAt: string;

    userProfileId: number;
    userFullName: string;
    userHeadline: string;

    userId: number;
    userEmail: string;

    skillId: number;
    skillName: string;

    categoryId: number;
    categoryName: string;
    categoryDescription: string;



}