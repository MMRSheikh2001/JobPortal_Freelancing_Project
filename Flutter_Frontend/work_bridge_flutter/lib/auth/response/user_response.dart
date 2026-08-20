

import 'package:work_bridge_flutter/auth/request/user_request.dart';

class UserResponseDTO {
  final int? id;
  final String? email;
  final String? name;
  final UserRole? role;
  final bool? isVerified;
  final bool? isActive;
  final bool? isSuspended;
  final DateTime? createdAt;
  final DateTime? updatedAt;

  const UserResponseDTO({
    this.id,
    this.email,
    this.name,
    this.role,
    this.isVerified,
    this.isActive,
    this.isSuspended,
    this.createdAt,
    this.updatedAt,
  });


  factory UserResponseDTO.fromJson(Map<String, dynamic> json) {
    return UserResponseDTO(
      id: (json['id'] as num?)?.toInt(),
      email: json['email'] as String?,
      name: json['name'] as String?,
      role: json['role'] != null ? UserRole.fromJson(json['role'] as String) : null,
      isVerified: json['isVerified'] as bool?,
      isActive: json['isActive'] as bool?,
      isSuspended: json['isSuspended'] as bool?,
      createdAt: json['createdAt'] != null
          ? DateTime.tryParse(json['createdAt'] as String)
          : null,
      updatedAt: json['updatedAt'] != null
          ? DateTime.tryParse(json['updatedAt'] as String)
          : null,
    );
  }


  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      if (email != null) 'email': email,
      if (name != null) 'name': name,
      if (role != null) 'role': role!.toJson(),
      if (isVerified != null) 'isVerified': isVerified,
      if (isActive != null) 'isActive': isActive,
      if (isSuspended != null) 'isSuspended': isSuspended,
      if (createdAt != null) 'createdAt': createdAt!.toIso8601String(),
      if (updatedAt != null) 'updatedAt': updatedAt!.toIso8601String(),
    };
  }


  UserResponseDTO copyWith({
    int? id,
    String? email,
    String? name,
    UserRole? role,
    bool? isVerified,
    bool? isActive,
    bool? isSuspended,
    DateTime? createdAt,
    DateTime? updatedAt,
  }) {
    return UserResponseDTO(
      id: id ?? this.id,
      email: email ?? this.email,
      name: name ?? this.name,
      role: role ?? this.role,
      isVerified: isVerified ?? this.isVerified,
      isActive: isActive ?? this.isActive,
      isSuspended: isSuspended ?? this.isSuspended,
      createdAt: createdAt ?? this.createdAt,
      updatedAt: updatedAt ?? this.updatedAt,
    );
  }
}