import 'package:work_bridge_flutter/cvinformations/models/request/reference_request.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/education_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/experience_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/extracurricular_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/portfolio_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/training_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/user_language_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/user_profile_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/user_skill_response.dart';

class ResumeResponseDTO {
  final UserProfileResponseDTO? profile;
  final List<EducationResponseDTO>? educations;
  final List<ExperienceResponseDTO>? experiences;
  final List<UserSkillResponseDTO>? skills;
  final List<UserLanguageResponseDTO>? languages;
  final List<TrainingResponseDTO>? trainings;
  final List<PortfolioResponseDTO>? portfolios;
  final List<ReferenceResponseDTO>? references;
  final List<ExtracurricularResponseDTO>? extracurriculars;

  const ResumeResponseDTO({
    this.profile,
    this.educations,
    this.experiences,
    this.skills,
    this.languages,
    this.trainings,
    this.portfolios,
    this.references,
    this.extracurriculars,
  });

  /// Factory constructor to create [ResumeResponseDTO] from a JSON map.
  factory ResumeResponseDTO.fromJson(Map<String, dynamic> json) {
    return ResumeResponseDTO(
      profile: json['profile'] != null
          ? UserProfileResponseDTO.fromJson(
          json['profile'] as Map<String, dynamic>)
          : null,
      educations: (json['educations'] as List<dynamic>?)
          ?.map((e) => EducationResponseDTO.fromJson(e as Map<String, dynamic>))
          .toList(),
      experiences: (json['experiences'] as List<dynamic>?)
          ?.map(
              (e) => ExperienceResponseDTO.fromJson(e as Map<String, dynamic>))
          .toList(),
      skills: (json['skills'] as List<dynamic>?)
          ?.map((e) => UserSkillResponseDTO.fromJson(e as Map<String, dynamic>))
          .toList(),
      languages: (json['languages'] as List<dynamic>?)
          ?.map((e) =>
          UserLanguageResponseDTO.fromJson(e as Map<String, dynamic>))
          .toList(),
      trainings: (json['trainings'] as List<dynamic>?)
          ?.map((e) => TrainingResponseDTO.fromJson(e as Map<String, dynamic>))
          .toList(),
      portfolios: (json['portfolios'] as List<dynamic>?)
          ?.map((e) => PortfolioResponseDTO.fromJson(e as Map<String, dynamic>))
          .toList(),
      references: (json['references'] as List<dynamic>?)
          ?.map((e) => ReferenceResponseDTO.fromJson(e as Map<String, dynamic>))
          .toList(),
      extracurriculars: (json['extracurriculars'] as List<dynamic>?)
          ?.map((e) =>
          ExtracurricularResponseDTO.fromJson(e as Map<String, dynamic>))
          .toList(),
    );
  }

  /// Converts this [ResumeResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (profile != null) 'profile': profile!.toJson(),
      if (educations != null)
        'educations': educations!.map((e) => e.toJson()).toList(),
      if (experiences != null)
        'experiences': experiences!.map((e) => e.toJson()).toList(),
      if (skills != null) 'skills': skills!.map((e) => e.toJson()).toList(),
      if (languages != null)
        'languages': languages!.map((e) => e.toJson()).toList(),
      if (trainings != null)
        'trainings': trainings!.map((e) => e.toJson()).toList(),
      if (portfolios != null)
        'portfolios': portfolios!.map((e) => e.toJson()).toList(),
      if (references != null)
        'references': references!.map((e) => e.toJson()).toList(),
      if (extracurriculars != null)
        'extracurriculars':
        extracurriculars!.map((e) => e.toJson()).toList(),
    };
  }

  /// Helper method to create a modified copy of this object.
  ResumeResponseDTO copyWith({
    UserProfileResponseDTO? profile,
    List<EducationResponseDTO>? educations,
    List<ExperienceResponseDTO>? experiences,
    List<UserSkillResponseDTO>? skills,
    List<UserLanguageResponseDTO>? languages,
    List<TrainingResponseDTO>? trainings,
    List<PortfolioResponseDTO>? portfolios,
    List<ReferenceResponseDTO>? references,
    List<ExtracurricularResponseDTO>? extracurriculars,
  }) {
    return ResumeResponseDTO(
      profile: profile ?? this.profile,
      educations: educations ?? this.educations,
      experiences: experiences ?? this.experiences,
      skills: skills ?? this.skills,
      languages: languages ?? this.languages,
      trainings: trainings ?? this.trainings,
      portfolios: portfolios ?? this.portfolios,
      references: references ?? this.references,
      extracurriculars: extracurriculars ?? this.extracurriculars,
    );
  }
}