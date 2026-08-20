import 'package:work_bridge_flutter/enums/language_proficiency.dart';

class UserLanguageRequestDTO {
  final LanguageProficiency? proficiency;
  final int? languageId;
  final int? userProfileId;

  const UserLanguageRequestDTO({
    this.proficiency,
    this.languageId,
    this.userProfileId,
  });

  /// Factory constructor to create [UserLanguageRequestDTO] from a JSON map.
  factory UserLanguageRequestDTO.fromJson(Map<String, dynamic> json) {
    return UserLanguageRequestDTO(
      proficiency: json['proficiency'] != null
          ? LanguageProficiency.fromJson(json['proficiency'] as String)
          : null,
      languageId: (json['languageId'] as num?)?.toInt(),
      userProfileId: (json['userProfileId'] as num?)?.toInt(),
    );
  }

  /// Converts this [UserLanguageRequestDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (proficiency != null) 'proficiency': proficiency!.toJson(),
      if (languageId != null) 'languageId': languageId,
      if (userProfileId != null) 'userProfileId': userProfileId,
    };
  }

  /// Helper method to create a modified copy of this object.
  UserLanguageRequestDTO copyWith({
    LanguageProficiency? proficiency,
    int? languageId,
    int? userProfileId,
  }) {
    return UserLanguageRequestDTO(
      proficiency: proficiency ?? this.proficiency,
      languageId: languageId ?? this.languageId,
      userProfileId: userProfileId ?? this.userProfileId,
    );
  }
}