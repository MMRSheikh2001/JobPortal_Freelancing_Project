import 'package:work_bridge_flutter/enums/employment_type.dart';
import 'package:work_bridge_flutter/enums/work_place_type.dart';

class JobSearchRequestDTO {
  final String? keyword;
  final int? categoryId;
  final int? countryId;
  final int? divisionId;
  final int? districtId;
  final int? policeStationId;
  final EmploymentType? employmentType;
  final WorkPlaceType? workPlaceType;
  final double? minSalary;
  final double? maxSalary;
  final bool? active;

  const JobSearchRequestDTO({
    this.keyword,
    this.categoryId,
    this.countryId,
    this.divisionId,
    this.districtId,
    this.policeStationId,
    this.employmentType,
    this.workPlaceType,
    this.minSalary,
    this.maxSalary,
    this.active,
  });

  /// Factory constructor to create [JobSearchRequestDTO] from a JSON map.
  factory JobSearchRequestDTO.fromJson(Map<String, dynamic> json) {
    return JobSearchRequestDTO(
      keyword: json['keyword'] as String?,
      categoryId: (json['categoryId'] as num?)?.toInt(),
      countryId: (json['countryId'] as num?)?.toInt(),
      divisionId: (json['divisionId'] as num?)?.toInt(),
      districtId: (json['districtId'] as num?)?.toInt(),
      policeStationId: (json['policeStationId'] as num?)?.toInt(),
      employmentType: json['employmentType'] != null
          ? EmploymentType.fromJson(json['employmentType'] as String)
          : null,
      workPlaceType: json['workPlaceType'] != null
          ? WorkPlaceType.fromJson(json['workPlaceType'] as String)
          : null,
      minSalary: (json['minSalary'] as num?)?.toDouble(),
      maxSalary: (json['maxSalary'] as num?)?.toDouble(),
      active: json['active'] as bool?,
    );
  }

  /// Converts this [JobSearchRequestDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (keyword != null) 'keyword': keyword,
      if (categoryId != null) 'categoryId': categoryId,
      if (countryId != null) 'countryId': countryId,
      if (divisionId != null) 'divisionId': divisionId,
      if (districtId != null) 'districtId': districtId,
      if (policeStationId != null) 'policeStationId': policeStationId,
      if (employmentType != null) 'employmentType': employmentType!.toJson(),
      if (workPlaceType != null) 'workPlaceType': workPlaceType!.toJson(),
      if (minSalary != null) 'minSalary': minSalary,
      if (maxSalary != null) 'maxSalary': maxSalary,
      if (active != null) 'active': active,
    };
  }

  /// Helper method to create a modified copy of this object.
  JobSearchRequestDTO copyWith({
    String? keyword,
    int? categoryId,
    int? countryId,
    int? divisionId,
    int? districtId,
    int? policeStationId,
    EmploymentType? employmentType,
    WorkPlaceType? workPlaceType,
    double? minSalary,
    double? maxSalary,
    bool? active,
  }) {
    return JobSearchRequestDTO(
      keyword: keyword ?? this.keyword,
      categoryId: categoryId ?? this.categoryId,
      countryId: countryId ?? this.countryId,
      divisionId: divisionId ?? this.divisionId,
      districtId: districtId ?? this.districtId,
      policeStationId: policeStationId ?? this.policeStationId,
      employmentType: employmentType ?? this.employmentType,
      workPlaceType: workPlaceType ?? this.workPlaceType,
      minSalary: minSalary ?? this.minSalary,
      maxSalary: maxSalary ?? this.maxSalary,
      active: active ?? this.active,
    );
  }
}