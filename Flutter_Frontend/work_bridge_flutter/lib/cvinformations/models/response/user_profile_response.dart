import 'package:work_bridge_flutter/enums/gender_type.dart';
import 'package:work_bridge_flutter/enums/job_type.dart';
import 'package:work_bridge_flutter/enums/work_place_type.dart';

class UserProfileResponseDTO {
  final int? id;
  final int? userId;
  final String? userEmail;
  final String? name;
  final String? phone;
  final String? image;
  final String? headline;
  final String? professionalSummary;
  final String? bio;
  final DateTime? dateOfBirth;
  final GenderType? gender;
  final String? nationality;
  final String? religion;
  final String? maritalStatus;
  final String? fatherName;
  final String? motherName;
  final String? nidNumber;
  final String? passportNumber;
  final String? githubLink;
  final String? linkedinLink;
  final String? portfolioWebsite;
  final double? expectedSalary;
  final double? currentSalary;
  final JobType? preferredJobType;
  final WorkPlaceType? preferredWorkplace;
  final String? careerObjective;
  final String? freelancerTitle;

  // Present Address
  final int? presentAddressId;
  final String? presentAddressDetails;
  final String? presentAddressPostCode;
  final int? presentCountryId;
  final String? presentCountryName;
  final String? presentCountryCode;
  final int? presentDivisionId;
  final String? presentDivisionName;
  final int? presentDistrictId;
  final String? presentDistrictName;
  final int? presentPoliceStationId;
  final String? presentPoliceStationName;

  // Permanent Address
  final int? permanentAddressId;
  final String? permanentAddressDetails;
  final String? permanentAddressPostCode;
  final int? permanentCountryId;
  final String? permanentCountryName;
  final String? permanentCountryCode;
  final int? permanentDivisionId;
  final String? permanentDivisionName;
  final int? permanentDistrictId;
  final String? permanentDistrictName;
  final int? permanentPoliceStationId;
  final String? permanentPoliceStationName;

  final bool? profileCompleted;
  final DateTime? createdAt;
  final DateTime? updatedAt;

  const UserProfileResponseDTO({
    this.id,
    this.userId,
    this.userEmail,
    this.name,
    this.phone,
    this.image,
    this.headline,
    this.professionalSummary,
    this.bio,
    this.dateOfBirth,
    this.gender,
    this.nationality,
    this.religion,
    this.maritalStatus,
    this.fatherName,
    this.motherName,
    this.nidNumber,
    this.passportNumber,
    this.githubLink,
    this.linkedinLink,
    this.portfolioWebsite,
    this.expectedSalary,
    this.currentSalary,
    this.preferredJobType,
    this.preferredWorkplace,
    this.careerObjective,
    this.freelancerTitle,
    this.presentAddressId,
    this.presentAddressDetails,
    this.presentAddressPostCode,
    this.presentCountryId,
    this.presentCountryName,
    this.presentCountryCode,
    this.presentDivisionId,
    this.presentDivisionName,
    this.presentDistrictId,
    this.presentDistrictName,
    this.presentPoliceStationId,
    this.presentPoliceStationName,
    this.permanentAddressId,
    this.permanentAddressDetails,
    this.permanentAddressPostCode,
    this.permanentCountryId,
    this.permanentCountryName,
    this.permanentCountryCode,
    this.permanentDivisionId,
    this.permanentDivisionName,
    this.permanentDistrictId,
    this.permanentDistrictName,
    this.permanentPoliceStationId,
    this.permanentPoliceStationName,
    this.profileCompleted,
    this.createdAt,
    this.updatedAt,
  });

  /// Factory constructor to create [UserProfileResponseDTO] from a JSON map.
  factory UserProfileResponseDTO.fromJson(Map<String, dynamic> json) {
    return UserProfileResponseDTO(
      id: (json['id'] as num?)?.toInt(),
      userId: (json['userId'] as num?)?.toInt(),
      userEmail: json['userEmail'] as String?,
      name: json['name'] as String?,
      phone: json['phone'] as String?,
      image: json['image'] as String?,
      headline: json['headline'] as String?,
      professionalSummary: json['professionalSummary'] as String?,
      bio: json['bio'] as String?,
      dateOfBirth: json['dateOfBirth'] != null
          ? DateTime.tryParse(json['dateOfBirth'] as String)
          : null,
      gender: json['gender'] != null
          ? GenderType.fromJson(json['gender'] as String)
          : null,
      nationality: json['nationality'] as String?,
      religion: json['religion'] as String?,
      maritalStatus: json['maritalStatus'] as String?,
      fatherName: json['fatherName'] as String?,
      motherName: json['motherName'] as String?,
      nidNumber: json['nidNumber'] as String?,
      passportNumber: json['passportNumber'] as String?,
      githubLink: json['githubLink'] as String?,
      linkedinLink: json['linkedinLink'] as String?,
      portfolioWebsite: json['portfolioWebsite'] as String?,
      expectedSalary: (json['expectedSalary'] as num?)?.toDouble(),
      currentSalary: (json['currentSalary'] as num?)?.toDouble(),
      preferredJobType: json['preferredJobType'] != null
          ? JobType.fromJson(json['preferredJobType'] as String)
          : null,
      preferredWorkplace: json['preferredWorkplace'] != null
          ? WorkPlaceType.fromJson(json['preferredWorkplace'] as String)
          : null,
      careerObjective: json['careerObjective'] as String?,
      freelancerTitle: json['freelancerTitle'] as String?,
      presentAddressId: (json['presentAddressId'] as num?)?.toInt(),
      presentAddressDetails: json['presentAddressDetails'] as String?,
      presentAddressPostCode: json['presentAddressPostCode'] as String?,
      presentCountryId: (json['presentCountryId'] as num?)?.toInt(),
      presentCountryName: json['presentCountryName'] as String?,
      presentCountryCode: json['presentCountryCode'] as String?,
      presentDivisionId: (json['presentDivisionId'] as num?)?.toInt(),
      presentDivisionName: json['presentDivisionName'] as String?,
      presentDistrictId: (json['presentDistrictId'] as num?)?.toInt(),
      presentDistrictName: json['presentDistrictName'] as String?,
      presentPoliceStationId:
      (json['presentPoliceStationId'] as num?)?.toInt(),
      presentPoliceStationName: json['presentPoliceStationName'] as String?,
      permanentAddressId: (json['permanentAddressId'] as num?)?.toInt(),
      permanentAddressDetails: json['permanentAddressDetails'] as String?,
      permanentAddressPostCode: json['permanentAddressPostCode'] as String?,
      permanentCountryId: (json['permanentCountryId'] as num?)?.toInt(),
      permanentCountryName: json['permanentCountryName'] as String?,
      permanentCountryCode: json['permanentCountryCode'] as String?,
      permanentDivisionId: (json['permanentDivisionId'] as num?)?.toInt(),
      permanentDivisionName: json['permanentDivisionName'] as String?,
      permanentDistrictId: (json['permanentDistrictId'] as num?)?.toInt(),
      permanentDistrictName: json['permanentDistrictName'] as String?,
      permanentPoliceStationId:
      (json['permanentPoliceStationId'] as num?)?.toInt(),
      permanentPoliceStationName:
      json['permanentPoliceStationName'] as String?,
      profileCompleted: json['profileCompleted'] as bool?,
      createdAt: json['createdAt'] != null
          ? DateTime.tryParse(json['createdAt'] as String)
          : null,
      updatedAt: json['updatedAt'] != null
          ? DateTime.tryParse(json['updatedAt'] as String)
          : null,
    );
  }

  /// Converts this [UserProfileResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      if (userId != null) 'userId': userId,
      if (userEmail != null) 'userEmail': userEmail,
      if (name != null) 'name': name,
      if (phone != null) 'phone': phone,
      if (image != null) 'image': image,
      if (headline != null) 'headline': headline,
      if (professionalSummary != null)
        'professionalSummary': professionalSummary,
      if (bio != null) 'bio': bio,
      if (dateOfBirth != null)
        'dateOfBirth': dateOfBirth!.toIso8601String().split('T').first,
      if (gender != null) 'gender': gender!.toJson(),
      if (nationality != null) 'nationality': nationality,
      if (religion != null) 'religion': religion,
      if (maritalStatus != null) 'maritalStatus': maritalStatus,
      if (fatherName != null) 'fatherName': fatherName,
      if (motherName != null) 'motherName': motherName,
      if (nidNumber != null) 'nidNumber': nidNumber,
      if (passportNumber != null) 'passportNumber': passportNumber,
      if (githubLink != null) 'githubLink': githubLink,
      if (linkedinLink != null) 'linkedinLink': linkedinLink,
      if (portfolioWebsite != null) 'portfolioWebsite': portfolioWebsite,
      if (expectedSalary != null) 'expectedSalary': expectedSalary,
      if (currentSalary != null) 'currentSalary': currentSalary,
      if (preferredJobType != null)
        'preferredJobType': preferredJobType!.toJson(),
      if (preferredWorkplace != null)
        'preferredWorkplace': preferredWorkplace!.toJson(),
      if (careerObjective != null) 'careerObjective': careerObjective,
      if (freelancerTitle != null) 'freelancerTitle': freelancerTitle,
      if (presentAddressId != null) 'presentAddressId': presentAddressId,
      if (presentAddressDetails != null)
        'presentAddressDetails': presentAddressDetails,
      if (presentAddressPostCode != null)
        'presentAddressPostCode': presentAddressPostCode,
      if (presentCountryId != null) 'presentCountryId': presentCountryId,
      if (presentCountryName != null) 'presentCountryName': presentCountryName,
      if (presentCountryCode != null) 'presentCountryCode': presentCountryCode,
      if (presentDivisionId != null) 'presentDivisionId': presentDivisionId,
      if (presentDivisionName != null)
        'presentDivisionName': presentDivisionName,
      if (presentDistrictId != null) 'presentDistrictId': presentDistrictId,
      if (presentDistrictName != null)
        'presentDistrictName': presentDistrictName,
      if (presentPoliceStationId != null)
        'presentPoliceStationId': presentPoliceStationId,
      if (presentPoliceStationName != null)
        'presentPoliceStationName': presentPoliceStationName,
      if (permanentAddressId != null) 'permanentAddressId': permanentAddressId,
      if (permanentAddressDetails != null)
        'permanentAddressDetails': permanentAddressDetails,
      if (permanentAddressPostCode != null)
        'permanentAddressPostCode': permanentAddressPostCode,
      if (permanentCountryId != null) 'permanentCountryId': permanentCountryId,
      if (permanentCountryName != null)
        'permanentCountryName': permanentCountryName,
      if (permanentCountryCode != null)
        'permanentCountryCode': permanentCountryCode,
      if (permanentDivisionId != null)
        'permanentDivisionId': permanentDivisionId,
      if (permanentDivisionName != null)
        'permanentDivisionName': permanentDivisionName,
      if (permanentDistrictId != null)
        'permanentDistrictId': permanentDistrictId,
      if (permanentDistrictName != null)
        'permanentDistrictName': permanentDistrictName,
      if (permanentPoliceStationId != null)
        'permanentPoliceStationId': permanentPoliceStationId,
      if (permanentPoliceStationName != null)
        'permanentPoliceStationName': permanentPoliceStationName,
      if (profileCompleted != null) 'profileCompleted': profileCompleted,
      if (createdAt != null) 'createdAt': createdAt!.toIso8601String(),
      if (updatedAt != null) 'updatedAt': updatedAt!.toIso8601String(),
    };
  }

  /// Helper method to create a modified copy of this object.
  UserProfileResponseDTO copyWith({
    int? id,
    int? userId,
    String? userEmail,
    String? name,
    String? phone,
    String? image,
    String? headline,
    String? professionalSummary,
    String? bio,
    DateTime? dateOfBirth,
    GenderType? gender,
    String? nationality,
    String? religion,
    String? maritalStatus,
    String? fatherName,
    String? motherName,
    String? nidNumber,
    String? passportNumber,
    String? githubLink,
    String? linkedinLink,
    String? portfolioWebsite,
    double? expectedSalary,
    double? currentSalary,
    JobType? preferredJobType,
    WorkPlaceType? preferredWorkplace,
    String? careerObjective,
    String? freelancerTitle,
    int? presentAddressId,
    String? presentAddressDetails,
    String? presentAddressPostCode,
    int? presentCountryId,
    String? presentCountryName,
    String? presentCountryCode,
    int? presentDivisionId,
    String? presentDivisionName,
    int? presentDistrictId,
    String? presentDistrictName,
    int? presentPoliceStationId,
    String? presentPoliceStationName,
    int? permanentAddressId,
    String? permanentAddressDetails,
    String? permanentAddressPostCode,
    int? permanentCountryId,
    String? permanentCountryName,
    String? permanentCountryCode,
    int? permanentDivisionId,
    String? permanentDivisionName,
    int? permanentDistrictId,
    String? permanentDistrictName,
    int? permanentPoliceStationId,
    String? permanentPoliceStationName,
    bool? profileCompleted,
    DateTime? createdAt,
    DateTime? updatedAt,
  }) {
    return UserProfileResponseDTO(
      id: id ?? this.id,
      userId: userId ?? this.userId,
      userEmail: userEmail ?? this.userEmail,
      name: name ?? this.name,
      phone: phone ?? this.phone,
      image: image ?? this.image,
      headline: headline ?? this.headline,
      professionalSummary: professionalSummary ?? this.professionalSummary,
      bio: bio ?? this.bio,
      dateOfBirth: dateOfBirth ?? this.dateOfBirth,
      gender: gender ?? this.gender,
      nationality: nationality ?? this.nationality,
      religion: religion ?? this.religion,
      maritalStatus: maritalStatus ?? this.maritalStatus,
      fatherName: fatherName ?? this.fatherName,
      motherName: motherName ?? this.motherName,
      nidNumber: nidNumber ?? this.nidNumber,
      passportNumber: passportNumber ?? this.passportNumber,
      githubLink: githubLink ?? this.githubLink,
      linkedinLink: linkedinLink ?? this.linkedinLink,
      portfolioWebsite: portfolioWebsite ?? this.portfolioWebsite,
      expectedSalary: expectedSalary ?? this.expectedSalary,
      currentSalary: currentSalary ?? this.currentSalary,
      preferredJobType: preferredJobType ?? this.preferredJobType,
      preferredWorkplace: preferredWorkplace ?? this.preferredWorkplace,
      careerObjective: careerObjective ?? this.careerObjective,
      freelancerTitle: freelancerTitle ?? this.freelancerTitle,
      presentAddressId: presentAddressId ?? this.presentAddressId,
      presentAddressDetails:
      presentAddressDetails ?? this.presentAddressDetails,
      presentAddressPostCode:
      presentAddressPostCode ?? this.presentAddressPostCode,
      presentCountryId: presentCountryId ?? this.presentCountryId,
      presentCountryName: presentCountryName ?? this.presentCountryName,
      presentCountryCode: presentCountryCode ?? this.presentCountryCode,
      presentDivisionId: presentDivisionId ?? this.presentDivisionId,
      presentDivisionName: presentDivisionName ?? this.presentDivisionName,
      presentDistrictId: presentDistrictId ?? this.presentDistrictId,
      presentDistrictName: presentDistrictName ?? this.presentDistrictName,
      presentPoliceStationId:
      presentPoliceStationId ?? this.presentPoliceStationId,
      presentPoliceStationName:
      presentPoliceStationName ?? this.presentPoliceStationName,
      permanentAddressId: permanentAddressId ?? this.permanentAddressId,
      permanentAddressDetails:
      permanentAddressDetails ?? this.permanentAddressDetails,
      permanentAddressPostCode:
      permanentAddressPostCode ?? this.permanentAddressPostCode,
      permanentCountryId: permanentCountryId ?? this.permanentCountryId,
      permanentCountryName: permanentCountryName ?? this.permanentCountryName,
      permanentCountryCode: permanentCountryCode ?? this.permanentCountryCode,
      permanentDivisionId: permanentDivisionId ?? this.permanentDivisionId,
      permanentDivisionName:
      permanentDivisionName ?? this.permanentDivisionName,
      permanentDistrictId: permanentDistrictId ?? this.permanentDistrictId,
      permanentDistrictName:
      permanentDistrictName ?? this.permanentDistrictName,
      permanentPoliceStationId:
      permanentPoliceStationId ?? this.permanentPoliceStationId,
      permanentPoliceStationName:
      permanentPoliceStationName ?? this.permanentPoliceStationName,
      profileCompleted: profileCompleted ?? this.profileCompleted,
      createdAt: createdAt ?? this.createdAt,
      updatedAt: updatedAt ?? this.updatedAt,
    );
  }
}