import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/auth/request/user_request.dart';
import 'package:work_bridge_flutter/auth/response/login_response.dart';
import 'package:work_bridge_flutter/public_pages/entity/user_dashboard_reponse.dart';
import 'package:work_bridge_flutter/public_pages/provider/home_dashboard_provider.dart';
import 'package:work_bridge_flutter/router/app_router.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';

class HomePage extends ConsumerStatefulWidget {
  const HomePage({super.key});

  @override
  ConsumerState<HomePage> createState() => _HomePageState();
}

class _HomePageState extends ConsumerState<HomePage> {
  @override
  void initState() {
    super.initState();

    Future.microtask(() {
      final user = ref.read(currentUserProvider);

      if (user?.userId != null) {
        ref
            .read(dashboardControllerProvider.notifier)
            .loadUserDashboard(user!.userId!);
      }
    });
  }

  Future<void> _refreshDashboard() async {
    final user = ref.read(currentUserProvider);

    if (user?.userId != null) {
      await ref
          .read(dashboardControllerProvider.notifier)
          .refresh(user!.userId!);
    }
  }

  Future<void> _logout() async {
    final shouldLogout = await showDialog<bool>(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text('Logout'),
          content: const Text(
            'Are you sure you want to logout?',
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(context).pop(false),
              child: const Text('Cancel'),
            ),
            FilledButton(
              onPressed: () => Navigator.of(context).pop(true),
              child: const Text('Logout'),
            ),
          ],
        );
      },
    );

    if (shouldLogout != true) return;

    await ref.read(authControllerProvider.notifier).logout();

    if (!mounted) return;

    Navigator.of(context).pushNamedAndRemoveUntil(
      AppRouter.login,
          (route) => false,
    );
  }

  String? _getProfileImageUrl(
      LoginResponse user,
      UserDashboardDTO dashboard,
      ) {
    final filename = dashboard.profileImage;

    if (filename == null || filename.isEmpty) {
      return null;
    }

    if (user.role == UserRole.COMPANY) {
      return '${ApiConstants.companyProfileImageUrl}$filename';
    }

    return '${ApiConstants.userProfileImageUrl}$filename';
  }

  bool _isCompany(LoginResponse user) {
    return user.role == UserRole.COMPANY;
  }

  @override
  Widget build(BuildContext context) {
    final user = ref.watch(currentUserProvider);

    if (user == null || user.userId == null) {
      return const Scaffold(
        body: Center(
          child: CircularProgressIndicator(),
        ),
      );
    }

    final dashboardState = ref.watch(dashboardControllerProvider);

    return Scaffold(
      appBar: AppBar(
        title: const Text(
          'WorkBridge',
          style: TextStyle(
            fontWeight: FontWeight.bold,
          ),
        ),
        actions: [
          _NotificationButton(
            unreadCount: dashboardState.value?.unreadNotifications ?? 0,
            onPressed: () {
              Navigator.of(context).pushNamed(
                AppRouter.notifications,
              );
            },
          ),
          IconButton(
            tooltip: 'Logout',
            onPressed: _logout,
            icon: const Icon(Icons.logout),
          ),
        ],
      ),
      body: dashboardState.when(
        loading: () => const Center(
          child: CircularProgressIndicator(),
        ),
        error: (error, stackTrace) => _DashboardError(
          onRetry: () {
            ref
                .read(dashboardControllerProvider.notifier)
                .loadUserDashboard(user.userId!);
          },
        ),
        data: (dashboard) {
          if (dashboard == null) {
            return _DashboardError(
              message: 'Unable to load dashboard.',
              onRetry: () {
                ref
                    .read(dashboardControllerProvider.notifier)
                    .loadUserDashboard(user.userId!);
              },
            );
          }

          return RefreshIndicator(
            onRefresh: _refreshDashboard,
            child: _buildDashboard(
              context,
              user,
              dashboard,
            ),
          );
        },
      ),
    );
  }

  Widget _buildDashboard(
      BuildContext context,
      LoginResponse user,
      UserDashboardDTO dashboard,
      ) {
    final isCompany = _isCompany(user);
    final imageUrl = _getProfileImageUrl(user, dashboard);

    return SingleChildScrollView(
      physics: const AlwaysScrollableScrollPhysics(),
      padding: const EdgeInsets.all(16),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          _ProfileHeader(
            userName: dashboard.userName ?? user.displayName ?? 'User',
            imageUrl: imageUrl,
            profileCompletion: dashboard.profileCompletion,
            isCompany: isCompany,
            onProfilePressed: () {
              Navigator.of(context).pushNamed(
                AppRouter.profile,
              );
            },
          ),

          const SizedBox(height: 24),

          const Text(
            'Dashboard',
            style: TextStyle(
              fontSize: 22,
              fontWeight: FontWeight.bold,
            ),
          ),

          const SizedBox(height: 16),

          _StatisticsGrid(
            appliedJobs: dashboard.appliedJobs ?? 0,
            savedJobs: dashboard.savedJobs ?? 0,
            savedGigs: dashboard.savedGigs ?? 0,
            activeOrders: dashboard.activeOrders ?? 0,
            unreadMessages: dashboard.unreadMessages ?? 0,
            isCompany: isCompany,
          ),

          const SizedBox(height: 28),

          const Text(
            'Quick Access',
            style: TextStyle(
              fontSize: 20,
              fontWeight: FontWeight.bold,
            ),
          ),

          const SizedBox(height: 16),

          _buildQuickAccessGrid(
            context,
            user,
            dashboard,
          ),

          const SizedBox(height: 28),

          _RecentActivity(
            dashboard: dashboard,
            isCompany: isCompany,
          ),

          const SizedBox(height: 24),

          SizedBox(
            width: double.infinity,
            child: OutlinedButton.icon(
              onPressed: _logout,
              icon: const Icon(Icons.logout),
              label: const Text('Logout'),
            ),
          ),

          const SizedBox(height: 16),
        ],
      ),
    );
  }

  Widget _buildQuickAccessGrid(
      BuildContext context,
      LoginResponse user,
      UserDashboardDTO dashboard,
      ) {
    final isCompany = _isCompany(user);

    final items = <_DashboardMenuItem>[
      if (!isCompany)
        _DashboardMenuItem(
          title: 'Search Jobs',
          subtitle: 'Find suitable jobs',
          icon: Icons.work_outline,
          onTap: () {
            Navigator.of(context).pushNamed(
              AppRouter.jobs,
            );
          },
        ),

      _DashboardMenuItem(
        title: 'Search Gigs',
        subtitle: 'Find freelance services',
        icon: Icons.search,
        onTap: () {
          Navigator.of(context).pushNamed(
            AppRouter.gigs,
          );
        },
      ),

      if (!isCompany)
        _DashboardMenuItem(
          title: 'Applications',
          subtitle: 'View your applications',
          icon: Icons.description_outlined,
          onTap: () {
            Navigator.of(context).pushNamed(
              AppRouter.applications,
            );
          },
        ),

      _DashboardMenuItem(
        title: 'Gig Orders',
        subtitle: 'Manage your orders',
        icon: Icons.shopping_bag_outlined,
        onTap: () {
          Navigator.of(context).pushNamed(
            AppRouter.orders,
          );
        },
      ),

      _DashboardMenuItem(
        title: 'Wallet',
        subtitle: 'Manage your balance',
        icon: Icons.account_balance_wallet_outlined,
        onTap: () {
          Navigator.of(context).pushNamed(
            AppRouter.wallet,
          );
        },
      ),

      _DashboardMenuItem(
        title: 'Chat',
        subtitle: 'View your conversations',
        icon: Icons.chat_outlined,
        badge: dashboard.unreadMessages ?? 0,
        onTap: () {
          Navigator.of(context).pushNamed(
            AppRouter.chat,
          );
        },
      ),

      _DashboardMenuItem(
        title: 'Profile',
        subtitle: 'View your CV & profile',
        icon: Icons.person_outline,
        onTap: () {
          Navigator.of(context).pushNamed(
            AppRouter.profile,
          );
        },
      ),

      _DashboardMenuItem(
        title: 'Notifications',
        subtitle: 'View your notifications',
        icon: Icons.notifications_none,
        badge: dashboard.unreadNotifications ?? 0,
        onTap: () {
          Navigator.of(context).pushNamed(
            AppRouter.notifications,
          );
        },
      ),
    ];

    return GridView.builder(
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      itemCount: items.length,
      gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
        crossAxisCount: 2,
        crossAxisSpacing: 12,
        mainAxisSpacing: 12,
        childAspectRatio: 1.15,
      ),
      itemBuilder: (context, index) {
        return _DashboardMenuCard(
          item: items[index],
        );
      },
    );
  }
}


// ============================================================
// PROFILE HEADER
// ============================================================

class _ProfileHeader extends StatelessWidget {
  const _ProfileHeader({
    required this.userName,
    required this.imageUrl,
    required this.profileCompletion,
    required this.isCompany,
    required this.onProfilePressed,
  });

  final String userName;
  final String? imageUrl;
  final int? profileCompletion;
  final bool isCompany;
  final VoidCallback onProfilePressed;

  @override
  Widget build(BuildContext context) {
    final completion = (profileCompletion ?? 0).clamp(0, 100);

    return Card(
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Row(
          children: [
            GestureDetector(
              onTap: onProfilePressed,
              child: _ProfileImage(
                imageUrl: imageUrl,
                isCompany: isCompany,
              ),
            ),

            const SizedBox(width: 16),

            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'Welcome back,',
                    style: TextStyle(
                      color: Colors.grey.shade600,
                      fontSize: 14,
                    ),
                  ),

                  const SizedBox(height: 4),

                  Text(
                    userName,
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                    style: const TextStyle(
                      fontSize: 20,
                      fontWeight: FontWeight.bold,
                    ),
                  ),

                  const SizedBox(height: 8),

                  Text(
                    isCompany ? 'Company' : 'Job Seeker',
                    style: TextStyle(
                      color: Colors.grey.shade600,
                    ),
                  ),

                  if (!isCompany) ...[
                    const SizedBox(height: 10),

                    Row(
                      children: [
                        Expanded(
                          child: ClipRRect(
                            borderRadius: BorderRadius.circular(10),
                            child: LinearProgressIndicator(
                              value: completion / 100,
                              minHeight: 7,
                            ),
                          ),
                        ),

                        const SizedBox(width: 10),

                        Text(
                          '$completion%',
                          style: const TextStyle(
                            fontWeight: FontWeight.bold,
                            fontSize: 12,
                          ),
                        ),
                      ],
                    ),

                    const SizedBox(height: 3),

                    Text(
                      'Profile completion',
                      style: TextStyle(
                        color: Colors.grey.shade600,
                        fontSize: 11,
                      ),
                    ),
                  ],
                ],
              ),
            ),

            IconButton(
              tooltip: 'Profile',
              onPressed: onProfilePressed,
              icon: const Icon(
                Icons.arrow_forward_ios,
                size: 18,
              ),
            ),
          ],
        ),
      ),
    );
  }
}


// ============================================================
// PROFILE IMAGE
// ============================================================

class _ProfileImage extends StatelessWidget {
  const _ProfileImage({
    required this.imageUrl,
    required this.isCompany,
  });

  final String? imageUrl;
  final bool isCompany;

  @override
  Widget build(BuildContext context) {
    return CircleAvatar(
      radius: 36,
      backgroundColor: Colors.grey.shade200,
      backgroundImage:
      imageUrl != null ? NetworkImage(imageUrl!) : null,
      child: imageUrl == null
          ? Icon(
        isCompany
            ? Icons.business
            : Icons.person,
        size: 38,
        color: Colors.grey.shade600,
      )
          : null,
    );
  }
}


// ============================================================
// STATISTICS
// ============================================================

class _StatisticsGrid extends StatelessWidget {
  const _StatisticsGrid({
    required this.appliedJobs,
    required this.savedJobs,
    required this.savedGigs,
    required this.activeOrders,
    required this.unreadMessages,
    required this.isCompany,
  });

  final int appliedJobs;
  final int savedJobs;
  final int savedGigs;
  final int activeOrders;
  final int unreadMessages;
  final bool isCompany;

  @override
  Widget build(BuildContext context) {
    final items = <_StatisticItem>[
      if (!isCompany)
        _StatisticItem(
          label: 'Applied Jobs',
          value: appliedJobs,
          icon: Icons.work_history_outlined,
        ),

      _StatisticItem(
        label: 'Saved Jobs',
        value: savedJobs,
        icon: Icons.bookmark_border,
      ),

      _StatisticItem(
        label: 'Saved Gigs',
        value: savedGigs,
        icon: Icons.favorite_border,
      ),

      _StatisticItem(
        label: 'Active Orders',
        value: activeOrders,
        icon: Icons.shopping_bag_outlined,
      ),

      _StatisticItem(
        label: 'Unread Messages',
        value: unreadMessages,
        icon: Icons.mark_unread_chat_alt_outlined,
      ),
    ];

    return GridView.builder(
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      itemCount: items.length,
      gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
        crossAxisCount: 2,
        crossAxisSpacing: 12,
        mainAxisSpacing: 12,
        childAspectRatio: 1.5,
      ),
      itemBuilder: (context, index) {
        return _StatisticCard(
          item: items[index],
        );
      },
    );
  }
}

class _StatisticItem {
  const _StatisticItem({
    required this.label,
    required this.value,
    required this.icon,
  });

  final String label;
  final int value;
  final IconData icon;
}

class _StatisticCard extends StatelessWidget {
  const _StatisticCard({
    required this.item,
  });

  final _StatisticItem item;

  @override
  Widget build(BuildContext context) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: Row(
          children: [
            CircleAvatar(
              radius: 20,
              child: Icon(
                item.icon,
                size: 20,
              ),
            ),

            const SizedBox(width: 10),

            Expanded(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    '${item.value}',
                    style: const TextStyle(
                      fontSize: 21,
                      fontWeight: FontWeight.bold,
                    ),
                  ),

                  const SizedBox(height: 2),

                  Text(
                    item.label,
                    maxLines: 2,
                    overflow: TextOverflow.ellipsis,
                    style: TextStyle(
                      fontSize: 11,
                      color: Colors.grey.shade600,
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}


// ============================================================
// DASHBOARD MENU
// ============================================================

class _DashboardMenuItem {
  const _DashboardMenuItem({
    required this.title,
    required this.subtitle,
    required this.icon,
    required this.onTap,
    this.badge = 0,
  });

  final String title;
  final String subtitle;
  final IconData icon;
  final VoidCallback onTap;
  final int badge;
}

class _DashboardMenuCard extends StatelessWidget {
  const _DashboardMenuCard({
    required this.item,
  });

  final _DashboardMenuItem item;

  @override
  Widget build(BuildContext context) {
    return Card(
      clipBehavior: Clip.antiAlias,
      child: InkWell(
        onTap: item.onTap,
        child: Padding(
          padding: const EdgeInsets.all(14),
          child: Stack(
            children: [
              Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Icon(
                    item.icon,
                    size: 30,
                  ),

                  const SizedBox(height: 10),

                  Text(
                    item.title,
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                    style: const TextStyle(
                      fontSize: 15,
                      fontWeight: FontWeight.bold,
                    ),
                  ),

                  const SizedBox(height: 4),

                  Text(
                    item.subtitle,
                    maxLines: 2,
                    overflow: TextOverflow.ellipsis,
                    style: TextStyle(
                      fontSize: 11,
                      color: Colors.grey.shade600,
                    ),
                  ),
                ],
              ),

              if (item.badge > 0)
                Positioned(
                  top: 0,
                  right: 0,
                  child: _Badge(
                    count: item.badge,
                  ),
                ),
            ],
          ),
        ),
      ),
    );
  }
}


// ============================================================
// NOTIFICATION BUTTON
// ============================================================

class _NotificationButton extends StatelessWidget {
  const _NotificationButton({
    required this.unreadCount,
    required this.onPressed,
  });

  final int unreadCount;
  final VoidCallback onPressed;

  @override
  Widget build(BuildContext context) {
    return Stack(
      clipBehavior: Clip.none,
      children: [
        IconButton(
          tooltip: 'Notifications',
          onPressed: onPressed,
          icon: const Icon(
            Icons.notifications_none,
          ),
        ),

        if (unreadCount > 0)
          Positioned(
            right: 4,
            top: 4,
            child: _Badge(
              count: unreadCount,
            ),
          ),
      ],
    );
  }
}


// ============================================================
// BADGE
// ============================================================

class _Badge extends StatelessWidget {
  const _Badge({
    required this.count,
  });

  final int count;

  @override
  Widget build(BuildContext context) {
    final text = count > 99 ? '99+' : '$count';

    return Container(
      constraints: const BoxConstraints(
        minWidth: 18,
        minHeight: 18,
      ),
      padding: const EdgeInsets.symmetric(
        horizontal: 5,
        vertical: 2,
      ),
      decoration: BoxDecoration(
        color: Colors.red,
        borderRadius: BorderRadius.circular(20),
        border: Border.all(
          color: Theme.of(context).scaffoldBackgroundColor,
          width: 1.5,
        ),
      ),
      child: Text(
        text,
        textAlign: TextAlign.center,
        style: const TextStyle(
          color: Colors.white,
          fontSize: 10,
          fontWeight: FontWeight.bold,
        ),
      ),
    );
  }
}


// ============================================================
// RECENT ACTIVITY
// ============================================================

class _RecentActivity extends StatelessWidget {
  const _RecentActivity({
    required this.dashboard,
    required this.isCompany,
  });

  final UserDashboardDTO dashboard;
  final bool isCompany;

  @override
  Widget build(BuildContext context) {
    final applications = dashboard.recentApplications ?? [];
    final orders = dashboard.recentOrders ?? [];

    if (isCompany && orders.isEmpty) {
      return const SizedBox.shrink();
    }

    if (!isCompany &&
        applications.isEmpty &&
        orders.isEmpty) {
      return const SizedBox.shrink();
    }

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Text(
          'Recent Activity',
          style: TextStyle(
            fontSize: 20,
            fontWeight: FontWeight.bold,
          ),
        ),

        const SizedBox(height: 12),

        if (!isCompany)
          ...applications.take(3).map(
                (application) => Card(
              child: ListTile(
                leading: const CircleAvatar(
                  child: Icon(
                    Icons.description_outlined,
                  ),
                ),
                title: Text(
                  application.jobTitle ?? 'Job Application',
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                ),
                subtitle: Text(
                  application.companyName ?? 'Company',
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                ),
              ),
            ),
          ),

        ...orders.take(3).map(
              (order) => Card(
            child: ListTile(
              leading: const CircleAvatar(
                child: Icon(
                  Icons.shopping_bag_outlined,
                ),
              ),
              title: Text(
                order.gigTitle ?? 'Gig Order',
                maxLines: 1,
                overflow: TextOverflow.ellipsis,
              ),
              subtitle: Text(
                order.sellerName ?? 'Seller',
                maxLines: 1,
                overflow: TextOverflow.ellipsis,
              ),
            ),
          ),
        ),
      ],
    );
  }
}


// ============================================================
// ERROR
// ============================================================

class _DashboardError extends StatelessWidget {
  const _DashboardError({
    this.message = 'Failed to load dashboard.',
    required this.onRetry,
  });

  final String message;
  final VoidCallback onRetry;

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(24),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            const Icon(
              Icons.error_outline,
              size: 50,
            ),

            const SizedBox(height: 16),

            Text(
              message,
              textAlign: TextAlign.center,
            ),

            const SizedBox(height: 16),

            FilledButton.icon(
              onPressed: onRetry,
              icon: const Icon(Icons.refresh),
              label: const Text('Retry'),
            ),
          ],
        ),
      ),
    );
  }
}
