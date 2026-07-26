import { LanguageProficiency } from "../../../enums/language-proficiency";

export interface UserLanguageRequestModel {

    proficiency: LanguageProficiency;
    languageId: number;
    userProfileId: number


}

export interface UserLanguageResponseModel {

    id: number;

    proficiency: LanguageProficiency;



    languageId: number;
    languageName: string;

    userProfileId: number;
    userName: string;
    userEmail: string


}