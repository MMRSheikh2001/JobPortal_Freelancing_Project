import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/providers.dart';

import 'package:work_bridge_flutter/enums/notification_type.dart';
import 'package:work_bridge_flutter/chat/models/response/notification_response.dart';
import 'package:work_bridge_flutter/utils/api_client.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

class NotificationsListScreen extends ConsumerStatefulWidget {
  const NotificationsListScreen({super.key});

  @override
  ConsumerState<NotificationsListScreen> createState() =>
      _NotificationsListScreenState();
}

class _NotificationsListScreenState
    extends ConsumerState<NotificationsListScreen> {
  List<NotificationResponseDTO> notifications = [];

  NotificationType? selectedType;

  bool loading = false;
  bool deletingAll = false;
  bool markingAll = false;

  int? userId;

  @override
  void initState() {
    super.initState();
    loadNotifications();
  }

  // =====================================================
  // LOAD NOTIFICATIONS
  // =====================================================

  Future<void> loadNotifications() async {
    setState(() {
      loading = true;
    });

    try {
      final storage = ref.read(storageServiceProvider);

      final user = await storage.getUser();

      if (user == null || user.userId == null) {
        if (!mounted) return;

        setState(() {
          notifications = [];
          loading = false;
        });

        showMessage('Unable to identify the logged-in user.', isError: true);

        return;
      }

      userId = user.userId;

      await loadNotificationsByFilter();
    } catch (e) {
      if (!mounted) return;

      setState(() {
        loading = false;
      });

      showMessage(apiErrorMessage(e), isError: true);
    }
  }

  // =====================================================
  // LOAD BY FILTER
  // =====================================================

  Future<void> loadNotificationsByFilter() async {
    if (userId == null) return;

    try {
      final repository = ref.read(notificationRepositoryProvider);

      List<NotificationResponseDTO> result;

      if (selectedType == null) {
        result = await repository.getUserNotifications(userId!);
      } else {
        result = await repository.getNotificationsByType(
          userId!,
          selectedType!,
        );
      }

      if (!mounted) return;

      setState(() {
        notifications = result;
        loading = false;
      });
    } catch (e) {
      if (!mounted) return;

      setState(() {
        loading = false;
      });

      showMessage(apiErrorMessage(e), isError: true);
    }
  }

  // =====================================================
  // FILTER
  // =====================================================

  Future<void> changeFilter(NotificationType? type) async {
    if (selectedType == type) return;

    setState(() {
      selectedType = type;
      loading = true;
    });

    await loadNotificationsByFilter();
  }

  // =====================================================
  // MARK SINGLE AS READ
  // =====================================================

  Future<void> markAsRead(NotificationResponseDTO notification) async {
    if (notification.id == null ||
        userId == null ||
        notification.isRead == true) {
      return;
    }

    try {
      final updated = await ref
          .read(notificationRepositoryProvider)
          .markNotificationAsRead(notification.id!, userId!);

      if (!mounted) return;

      setState(() {
        final index = notifications.indexWhere(
          (item) => item.id == notification.id,
        );

        if (index != -1) {
          notifications[index] = updated;
        }
      });
    } catch (e) {
      if (!mounted) return;

      showMessage(apiErrorMessage(e), isError: true);
    }
  }

  // =====================================================
  // MARK ALL AS READ
  // =====================================================

  Future<void> markAllAsRead() async {
    if (userId == null || notifications.isEmpty) return;

    final hasUnread = notifications.any(
      (notification) => notification.isRead != true,
    );

    if (!hasUnread) {
      showMessage('All notifications are already read.');
      return;
    }

    setState(() {
      markingAll = true;
    });

    try {
      await ref
          .read(notificationRepositoryProvider)
          .markAllNotificationsAsRead(userId!);

      if (!mounted) return;

      setState(() {
        notifications = notifications
            .map((notification) => notification.copyWith(isRead: true))
            .toList();

        markingAll = false;
      });

      showMessage('All notifications marked as read.');
    } catch (e) {
      if (!mounted) return;

      setState(() {
        markingAll = false;
      });

      showMessage(apiErrorMessage(e), isError: true);
    }
  }

  // =====================================================
  // DELETE SINGLE
  // =====================================================

  Future<void> deleteNotification(NotificationResponseDTO notification) async {
    if (notification.id == null || userId == null) return;

    try {
      await ref
          .read(notificationRepositoryProvider)
          .deleteNotification(notification.id!, userId!);

      if (!mounted) return;

      setState(() {
        notifications.removeWhere((item) => item.id == notification.id);
      });

      showMessage('Notification deleted.');
    } catch (e) {
      if (!mounted) return;

      showMessage(apiErrorMessage(e), isError: true);
    }
  }

  // =====================================================
  // DELETE ALL
  // =====================================================

  Future<void> deleteAllNotifications() async {
    if (userId == null || notifications.isEmpty) return;

    final confirmed = await showConfirmationDialog(
      title: 'Delete All Notifications',
      message: 'Are you sure you want to delete all your notifications?',
      confirmText: 'Delete All',
      isDanger: true,
    );

    if (!confirmed) return;

    setState(() {
      deletingAll = true;
    });

    try {
      await ref
          .read(notificationRepositoryProvider)
          .deleteAllNotifications(userId!);

      if (!mounted) return;

      setState(() {
        notifications.clear();
        deletingAll = false;
      });

      showMessage('All notifications deleted.');
    } catch (e) {
      if (!mounted) return;

      setState(() {
        deletingAll = false;
      });

      showMessage(apiErrorMessage(e), isError: true);
    }
  }

  // =====================================================
  // OPEN NOTIFICATION
  // =====================================================

  Future<void> openNotification(NotificationResponseDTO notification) async {
    // First mark it as read.
    await markAsRead(notification);

    // -------------------------------------------------
    // Navigation based on notification type can be
    // added here later.
    //
    // Example:
    //
    // if (notification.type == NotificationType.gigOrder &&
    //     notification.referenceId != null) {
    //
    //   Navigator.pushNamed(
    //     context,
    //     '/order-details',
    //     arguments: notification.referenceId,
    //   );
    // }
    // -------------------------------------------------
  }

  // =====================================================
  // HELPERS
  // =====================================================

  String notificationTypeText(NotificationType? type) {
    if (type == null) {
      return 'Notification';
    }

    switch (type) {
      case NotificationType.jobApplied:
        return 'Job Applied';

      case NotificationType.jobShortlisted:
        return 'Job Shortlisted';

      case NotificationType.jobRejected:
        return 'Job Rejected';

      case NotificationType.jobHired:
        return 'Job Hired';

      case NotificationType.gigApplication:
        return 'Gig Application';

      case NotificationType.gigOrder:
        return 'Gig Order';

      case NotificationType.gigCompleted:
        return 'Gig Completed';

      case NotificationType.depositSuccess:
        return 'Deposit Success';

      case NotificationType.withdrawApproved:
        return 'Withdraw Approved';

      case NotificationType.withdrawRejected:
        return 'Withdraw Rejected';

      case NotificationType.system:
        return 'System';

      case NotificationType.adminMessage:
        return 'Admin Message';
    }
  }

  IconData notificationIcon(NotificationType? type) {
    switch (type) {
      case NotificationType.jobApplied:
        return Icons.work_outline;

      case NotificationType.jobShortlisted:
        return Icons.check_circle_outline;

      case NotificationType.jobRejected:
        return Icons.cancel_outlined;

      case NotificationType.jobHired:
        return Icons.celebration_outlined;

      case NotificationType.gigApplication:
        return Icons.assignment_outlined;

      case NotificationType.gigOrder:
        return Icons.shopping_bag_outlined;

      case NotificationType.gigCompleted:
        return Icons.task_alt;

      case NotificationType.depositSuccess:
        return Icons.account_balance_wallet_outlined;

      case NotificationType.withdrawApproved:
        return Icons.check_circle_outline;

      case NotificationType.withdrawRejected:
        return Icons.money_off_csred_outlined;

      case NotificationType.system:
        return Icons.notifications_outlined;

      case NotificationType.adminMessage:
        return Icons.admin_panel_settings_outlined;

      case null:
        return Icons.notifications_none;
    }
  }

  String formatDate(DateTime? date) {
    if (date == null) return '';

    final local = date.toLocal();

    String twoDigits(int value) {
      return value.toString().padLeft(2, '0');
    }

    return '${twoDigits(local.day)}/'
        '${twoDigits(local.month)}/'
        '${local.year} '
        '${twoDigits(local.hour)}:'
        '${twoDigits(local.minute)}';
  }

  int get unreadCount {
    return notifications
        .where((notification) => notification.isRead != true)
        .length;
  }

  // =====================================================
  // MESSAGE
  // =====================================================

  void showMessage(String message, {bool isError = false}) {
    if (!mounted) return;

    ScaffoldMessenger.of(context)
      ..hideCurrentSnackBar()
      ..showSnackBar(
        SnackBar(
          content: Text(message),
          backgroundColor: isError ? Colors.red : Colors.green,
        ),
      );
  }

  // =====================================================
  // CONFIRMATION DIALOG
  // =====================================================

  Future<bool> showConfirmationDialog({
    required String title,
    required String message,
    required String confirmText,
    bool isDanger = false,
  }) async {
    final result = await showDialog<bool>(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: Text(title),
          content: Text(message),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.pop(context, false);
              },
              child: const Text('Cancel'),
            ),
            TextButton(
              onPressed: () {
                Navigator.pop(context, true);
              },
              child: Text(
                confirmText,
                style: TextStyle(color: isDanger ? Colors.red : null),
              ),
            ),
          ],
        );
      },
    );

    return result ?? false;
  }

  // =====================================================
  // NOTIFICATION TILE
  // =====================================================

  Widget buildNotificationTile(NotificationResponseDTO notification) {
    final bool unread = notification.isRead != true;

    return Card(
      margin: const EdgeInsets.only(bottom: 10),
      elevation: unread ? 2 : 0.5,
      color: unread ? Colors.blue.shade50 : null,
      child: InkWell(
        borderRadius: BorderRadius.circular(8),
        onTap: () => openNotification(notification),
        child: Padding(
          padding: const EdgeInsets.all(14),
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // -----------------------------------------
              // ICON
              // -----------------------------------------
              Container(
                width: 46,
                height: 46,
                decoration: BoxDecoration(
                  color: unread ? Colors.blue.shade100 : Colors.grey.shade200,
                  shape: BoxShape.circle,
                ),
                child: Icon(
                  notificationIcon(notification.type),
                  color: unread ? Colors.blue : Colors.grey.shade700,
                ),
              ),

              const SizedBox(width: 12),

              // -----------------------------------------
              // CONTENT
              // -----------------------------------------
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Expanded(
                          child: Text(
                            notification.title ?? 'Notification',
                            style: TextStyle(
                              fontSize: 16,
                              fontWeight: unread
                                  ? FontWeight.bold
                                  : FontWeight.w500,
                            ),
                          ),
                        ),

                        if (unread)
                          Container(
                            width: 9,
                            height: 9,
                            margin: const EdgeInsets.only(top: 5, left: 8),
                            decoration: const BoxDecoration(
                              color: Colors.blue,
                              shape: BoxShape.circle,
                            ),
                          ),
                      ],
                    ),

                    const SizedBox(height: 5),

                    Text(
                      notification.message ?? '',
                      style: TextStyle(
                        color: Colors.grey.shade700,
                        fontSize: 14,
                      ),
                    ),

                    const SizedBox(height: 8),

                    Row(
                      children: [
                        Text(
                          notificationTypeText(notification.type),
                          style: TextStyle(
                            fontSize: 12,
                            color: Colors.grey.shade600,
                            fontWeight: FontWeight.w500,
                          ),
                        ),
                        const SizedBox(width: 8),
                        Text(
                          '•',
                          style: TextStyle(color: Colors.grey.shade500),
                        ),
                        const SizedBox(width: 8),
                        Text(
                          formatDate(notification.createdAt),
                          style: TextStyle(
                            fontSize: 12,
                            color: Colors.grey.shade600,
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),

              // -----------------------------------------
              // MENU
              // -----------------------------------------
              PopupMenuButton<String>(
                onSelected: (value) async {
                  if (value == 'read') {
                    await markAsRead(notification);
                  } else if (value == 'delete') {
                    await deleteNotification(notification);
                  }
                },
                itemBuilder: (context) {
                  return [
                    if (unread)
                      const PopupMenuItem<String>(
                        value: 'read',
                        child: Row(
                          children: [
                            Icon(Icons.mark_email_read_outlined),
                            SizedBox(width: 10),
                            Text('Mark as read'),
                          ],
                        ),
                      ),
                    const PopupMenuItem<String>(
                      value: 'delete',
                      child: Row(
                        children: [
                          Icon(Icons.delete_outline, color: Colors.red),
                          SizedBox(width: 10),
                          Text('Delete', style: TextStyle(color: Colors.red)),
                        ],
                      ),
                    ),
                  ];
                },
              ),
            ],
          ),
        ),
      ),
    );
  }

  // =====================================================
  // FILTER DROPDOWN
  // =====================================================

  Widget buildFilter() {
    return SingleChildScrollView(
      scrollDirection: Axis.horizontal,
      child: Row(
        children: [
          ChoiceChip(
            label: const Text('All'),
            selected: selectedType == null,
            onSelected: (_) {
              changeFilter(null);
            },
          ),

          const SizedBox(width: 8),

          ...NotificationType.values.map((type) {
            return Padding(
              padding: const EdgeInsets.only(right: 8),
              child: ChoiceChip(
                label: Text(notificationTypeText(type)),
                selected: selectedType == type,
                onSelected: (_) {
                  changeFilter(type);
                },
              ),
            );
          }),
        ],
      ),
    );
  }

  // =====================================================
  // HEADER ACTIONS
  // =====================================================

  Widget buildHeaderActions() {
    return Row(
      mainAxisAlignment: MainAxisAlignment.end,
      children: [
        if (unreadCount > 0)
          TextButton.icon(
            onPressed: markingAll ? null : markAllAsRead,
            icon: markingAll
                ? const SizedBox(
                    width: 16,
                    height: 16,
                    child: CircularProgressIndicator(strokeWidth: 2),
                  )
                : const Icon(Icons.mark_email_read_outlined),
            label: const Text('Mark all as read'),
          ),

        if (notifications.isNotEmpty)
          TextButton.icon(
            onPressed: deletingAll ? null : deleteAllNotifications,
            icon: deletingAll
                ? const SizedBox(
                    width: 16,
                    height: 16,
                    child: CircularProgressIndicator(strokeWidth: 2),
                  )
                : const Icon(Icons.delete_outline, color: Colors.red),
            label: const Text(
              'Delete all',
              style: TextStyle(color: Colors.red),
            ),
          ),
      ],
    );
  }

  // =====================================================
  // BUILD
  // =====================================================

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Row(
          children: [
            const Text('Notifications'),
            if (unreadCount > 0) ...[
              const SizedBox(width: 8),
              Container(
                padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 3),
                decoration: BoxDecoration(
                  color: Colors.red,
                  borderRadius: BorderRadius.circular(12),
                ),
                child: Text(
                  '$unreadCount',
                  style: const TextStyle(
                    color: Colors.white,
                    fontSize: 12,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
            ],
          ],
        ),
      ),

      body: RefreshIndicator(
        onRefresh: loadNotifications,
        child: loading
            ? const Center(child: CircularProgressIndicator())
            : notifications.isEmpty
            ? ListView(
                physics: const AlwaysScrollableScrollPhysics(),
                children: [
                  const SizedBox(height: 120),
                  Icon(Icons.notifications_none, size: 70, color: Colors.grey),
                  const SizedBox(height: 15),
                  Center(
                    child: Text(
                      selectedType == null
                          ? 'No notifications.'
                          : 'No notifications of this type.',
                      style: TextStyle(
                        color: Colors.grey.shade600,
                        fontSize: 16,
                      ),
                    ),
                  ),
                ],
              )
            : ListView(
                physics: const AlwaysScrollableScrollPhysics(),
                padding: const EdgeInsets.all(16),
                children: [
                  // -------------------------------------
                  // FILTER
                  // -------------------------------------
                  const Text(
                    'Filter by type',
                    style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                  ),

                  const SizedBox(height: 10),

                  buildFilter(),

                  const SizedBox(height: 8),

                  // -------------------------------------
                  // ACTIONS
                  // -------------------------------------
                  buildHeaderActions(),

                  const SizedBox(height: 4),

                  // -------------------------------------
                  // NOTIFICATIONS
                  // -------------------------------------
                  ...notifications.map(buildNotificationTile),
                ],
              ),
      ),
    );
  }
}
