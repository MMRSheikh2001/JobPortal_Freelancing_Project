import 'package:work_bridge_flutter/gig/entity/response/gig_order_response.dart';
import 'package:work_bridge_flutter/gig/entity/response/gig_response.dart';
import 'package:work_bridge_flutter/job/entity/response/job_application_response.dart';
import 'package:work_bridge_flutter/job/entity/response/job_response.dart';

class UserDashboardDTO {
  final String? userName;
  final String? profileImage;
  final int? profileCompletion;

  final int? appliedJobs;
  final int? savedJobs;
  final int? savedGigs;
  final int? activeOrders;

  final int? unreadMessages;
  final int? unreadNotifications;

  final List<JobApplicationResponseDTO>? recentApplications;
  final List<GigOrderResponseDTO>? recentOrders;
  final List<JobResponseDTO>? latestJobs;
  final List<GigResponseDTO>? popularGigs;

  const UserDashboardDTO({
    this.userName,
    this.profileImage,
    this.profileCompletion,
    this.appliedJobs,
    this.savedJobs,
    this.savedGigs,
    this.activeOrders,
    this.unreadMessages,
    this.unreadNotifications,
    this.recentApplications,
    this.recentOrders,
    this.latestJobs,
    this.popularGigs,
  });

  /// Factory constructor to create [UserDashboardDTO] from a JSON map.
  factory UserDashboardDTO.fromJson(Map<String, dynamic> json) {
    return UserDashboardDTO(
      userName: json['userName'] as String?,
      profileImage: json['profileImage'] as String?,
      profileCompletion: (json['profileCompletion'] as num?)?.toInt(),
      appliedJobs: (json['appliedJobs'] as num?)?.toInt(),
      savedJobs: (json['savedJobs'] as num?)?.toInt(),
      savedGigs: (json['savedGigs'] as num?)?.toInt(),
      activeOrders: (json['activeOrders'] as num?)?.toInt(),
      unreadMessages: (json['unreadMessages'] as num?)?.toInt(),
      unreadNotifications: (json['unreadNotifications'] as num?)?.toInt(),
      recentApplications: (json['recentApplications'] as List<dynamic>?)
          ?.map(
            (e) =>
                JobApplicationResponseDTO.fromJson(e as Map<String, dynamic>),
          )
          .toList(),
      recentOrders: (json['recentOrders'] as List<dynamic>?)
          ?.map((e) => GigOrderResponseDTO.fromJson(e as Map<String, dynamic>))
          .toList(),
      latestJobs: (json['latestJobs'] as List<dynamic>?)
          ?.map((e) => JobResponseDTO.fromJson(e as Map<String, dynamic>))
          .toList(),
      popularGigs: (json['popularGigs'] as List<dynamic>?)
          ?.map((e) => GigResponseDTO.fromJson(e as Map<String, dynamic>))
          .toList(),
    );
  }

  /// Converts this [UserDashboardDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (userName != null) 'userName': userName,
      if (profileImage != null) 'profileImage': profileImage,
      if (profileCompletion != null) 'profileCompletion': profileCompletion,
      if (appliedJobs != null) 'appliedJobs': appliedJobs,
      if (savedJobs != null) 'savedJobs': savedJobs,
      if (savedGigs != null) 'savedGigs': savedGigs,
      if (activeOrders != null) 'activeOrders': activeOrders,
      if (unreadMessages != null) 'unreadMessages': unreadMessages,
      if (unreadNotifications != null)
        'unreadNotifications': unreadNotifications,
      if (recentApplications != null)
        'recentApplications': recentApplications!
            .map((e) => e.toJson())
            .toList(),
      if (recentOrders != null)
        'recentOrders': recentOrders!.map((e) => e.toJson()).toList(),
      if (latestJobs != null)
        'latestJobs': latestJobs!.map((e) => e.toJson()).toList(),
      if (popularGigs != null)
        'popularGigs': popularGigs!.map((e) => e.toJson()).toList(),
    };
  }

  /// Helper method to create a modified copy of this object.
  UserDashboardDTO copyWith({
    String? userName,
    String? profileImage,
    int? profileCompletion,
    int? appliedJobs,
    int? savedJobs,
    int? savedGigs,
    int? activeOrders,
    int? unreadMessages,
    int? unreadNotifications,
    List<JobApplicationResponseDTO>? recentApplications,
    List<GigOrderResponseDTO>? recentOrders,
    List<JobResponseDTO>? latestJobs,
    List<GigResponseDTO>? popularGigs,
  }) {
    return UserDashboardDTO(
      userName: userName ?? this.userName,
      profileImage: profileImage ?? this.profileImage,
      profileCompletion: profileCompletion ?? this.profileCompletion,
      appliedJobs: appliedJobs ?? this.appliedJobs,
      savedJobs: savedJobs ?? this.savedJobs,
      savedGigs: savedGigs ?? this.savedGigs,
      activeOrders: activeOrders ?? this.activeOrders,
      unreadMessages: unreadMessages ?? this.unreadMessages,
      unreadNotifications: unreadNotifications ?? this.unreadNotifications,
      recentApplications: recentApplications ?? this.recentApplications,
      recentOrders: recentOrders ?? this.recentOrders,
      latestJobs: latestJobs ?? this.latestJobs,
      popularGigs: popularGigs ?? this.popularGigs,
    );
  }
}
