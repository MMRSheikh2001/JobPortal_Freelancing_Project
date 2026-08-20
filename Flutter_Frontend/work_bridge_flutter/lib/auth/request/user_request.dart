enum UserRole {
  USER,
  COMPANY,
  ADMIN;

  String toJson() => name.toUpperCase();

  static UserRole? fromJson(String? role) {
    if (role == null) return null;
    return UserRole.values.firstWhere(
      (e) => e.name.toLowerCase() == role.toLowerCase(),
      orElse: () => UserRole.USER,
    );
  }
}

class UserRequestDTO {
  final String? email;
  final String? password;
  final UserRole? role;
  final String? fullName;

  const UserRequestDTO({this.email, this.password, this.role, this.fullName});

  /// Factory constructor to create [UserRequestDTO] from a JSON map.
  factory UserRequestDTO.fromJson(Map<String, dynamic> json) {
    return UserRequestDTO(
      email: json['email'] as String?,
      password: json['password'] as String?,
      role: json['role'] != null
          ? UserRole.fromJson(json['role'] as String)
          : null,
      fullName: json['fullName'] as String?,
    );
  }

  /// Converts this [UserRequestDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (email != null) 'email': email,
      if (password != null) 'password': password,
      if (role != null) 'role': role!.toJson(),
      if (fullName != null) 'fullName': fullName,
    };
  }

  /// Helper method to create a modified copy of this object.
  UserRequestDTO copyWith({
    String? email,
    String? password,
    UserRole? role,
    String? fullName,
  }) {
    return UserRequestDTO(
      email: email ?? this.email,
      password: password ?? this.password,
      role: role ?? this.role,
      fullName: fullName ?? this.fullName,
    );
  }
}
