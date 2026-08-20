import 'package:work_bridge_flutter/enums/proficiency_level.dart';

class UserSkillRequestDTO {
  final ProficiencyLevel? proficiencyLevel;
  final int? yearsOfExperience;
  final int? userProfileId;
  final int? skillId;

  const UserSkillRequestDTO({
    this.proficiencyLevel,
    this.yearsOfExperience,
    this.userProfileId,
    this.skillId,
  });

  /// Factory constructor to create [UserSkillRequestDTO] from a JSON map.
  factory UserSkillRequestDTO.fromJson(Map<String, dynamic> json) {
    return UserSkillRequestDTO(
      proficiencyLevel: json['proficiencyLevel'] != null
          ? ProficiencyLevel.fromJson(json['proficiencyLevel'] as String)
          : null,
      yearsOfExperience: (json['yearsOfExperience'] as num?)?.toInt(),
      userProfileId: (json['userProfileId'] as num?)?.toInt(),
      skillId: (json['skillId'] as num?)?.toInt(),
    );
  }

  /// Converts this [UserSkillRequestDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (proficiencyLevel != null) 'proficiencyLevel': proficiencyLevel!.toJson(),
      if (yearsOfExperience != null) 'yearsOfExperience': yearsOfExperience,
      if (userProfileId != null) 'userProfileId': userProfileId,
      if (skillId != null) 'skillId': skillId,
    };
  }

  /// Helper method to create a modified copy of this object.
  UserSkillRequestDTO copyWith({
    ProficiencyLevel? proficiencyLevel,
    int? yearsOfExperience,
    int? userProfileId,
    int? skillId,
  }) {
    return UserSkillRequestDTO(
      proficiencyLevel: proficiencyLevel ?? this.proficiencyLevel,
      yearsOfExperience: yearsOfExperience ?? this.yearsOfExperience,
      userProfileId: userProfileId ?? this.userProfileId,
      skillId: skillId ?? this.skillId,
    );
  }
}