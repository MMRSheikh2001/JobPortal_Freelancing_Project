import { EducationResponseModel } from "./education.model";
import { ExperienceResponseModel } from "./experience.model";
import { ExtracurricularResponseModel } from "./extracurricular.model";
import { PortfolioResponseModel } from "./portfolio.model";
import { ReferenceResponseModel } from "./reference.model";
import { TrainingResponseModel } from "./training.model";
import { UserLanguageResponseModel } from "./user-language";
import { UserSkillResponseModel } from "./user-skill.model";
import { UserProfileResponseModel } from "./user.profile.model";


export interface ResumeImportPreviewDTO {
    profile: UserProfileResponseModel;
    educations: EducationResponseModel[];
    experiences: ExperienceResponseModel[];
    skills: UserSkillResponseModel[];
    languages: UserLanguageResponseModel[];
    trainings: TrainingResponseModel[];
    portfolios: PortfolioResponseModel[];
    references: ReferenceResponseModel[];
    extracurriculars: ExtracurricularResponseModel[];
}