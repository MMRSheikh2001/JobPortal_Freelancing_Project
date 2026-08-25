import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/providers.dart';

import 'package:work_bridge_flutter/chat/models/response/conversation_response.dart';
import 'package:work_bridge_flutter/chat/provider/conversation_provider.dart';
import 'package:work_bridge_flutter/enums/conversation_status.dart';
import 'package:work_bridge_flutter/utils/api_client.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';

class ChatListScreen extends ConsumerStatefulWidget {
  const ChatListScreen({super.key});

  @override
  ConsumerState<ChatListScreen> createState() => _ChatListScreenState();
}

class _ChatListScreenState extends ConsumerState<ChatListScreen> {
  int? _buyerId;
  bool _loadingUser = true;

  @override
  void initState() {
    super.initState();
    _loadUser();
  }

  // ============================================================
  // Load logged-in user
  // ============================================================

  Future<void> _loadUser() async {
    try {
      final storageService = ref.read(storageServiceProvider);
      final user = await storageService.getUser();

      if (!mounted) return;

      setState(() {
        _buyerId = user?.userId;
        _loadingUser = false;
      });
    } catch (_) {
      if (!mounted) return;

      setState(() {
        _loadingUser = false;
      });
    }
  }

  // ============================================================
  // Refresh
  // ============================================================

  Future<void> _refresh() async {
    if (_buyerId == null) return;

    ref.invalidate(buyerConversationsProvider(_buyerId!));

    await ref.read(
      buyerConversationsProvider(_buyerId!).future,
    );
  }

  // ============================================================
  // Navigation
  // ============================================================

  void _openChat(ConversationResponseDTO conversation) {
    if (conversation.id == null) return;

    Navigator.pushNamed(
      context,
      '/chat',
      arguments: conversation.id,
    );
  }

  // ============================================================
  // Build
  // ============================================================

  @override
  Widget build(BuildContext context) {
    if (_loadingUser) {
      return Scaffold(
        appBar: AppBar(
          title: const Text('Messages'),
        ),
        body: const Center(
          child: CircularProgressIndicator(),
        ),
      );
    }

    if (_buyerId == null) {
      return Scaffold(
        appBar: AppBar(
          title: const Text('Messages'),
        ),
        body: const Center(
          child: Text(
            'Unable to load your account information.',
          ),
        ),
      );
    }

    final conversationsAsync =
    ref.watch(buyerConversationsProvider(_buyerId!));

    return Scaffold(
      appBar: AppBar(
        title: const Text('Messages'),
      ),
      body: conversationsAsync.when(
        loading: () => const Center(
          child: CircularProgressIndicator(),
        ),
        error: (error, stackTrace) => _buildErrorState(
          error,
        ),
        data: (conversations) {
          if (conversations.isEmpty) {
            return _buildEmptyState();
          }

          return RefreshIndicator(
            onRefresh: _refresh,
            child: ListView.separated(
              physics: const AlwaysScrollableScrollPhysics(),
              padding: const EdgeInsets.symmetric(
                vertical: 8,
              ),
              itemCount: conversations.length,
              separatorBuilder: (_, __) => const Divider(
                height: 1,
                indent: 88,
              ),
              itemBuilder: (context, index) {
                final conversation = conversations[index];

                return _ConversationTile(
                  conversation: conversation,
                  onTap: () => _openChat(conversation),
                );
              },
            ),
          );
        },
      ),
    );
  }

  // ============================================================
  // Empty state
  // ============================================================

  Widget _buildEmptyState() {
    return RefreshIndicator(
      onRefresh: _refresh,
      child: ListView(
        physics: const AlwaysScrollableScrollPhysics(),
        children: const [
          SizedBox(height: 180),
          Icon(
            Icons.chat_bubble_outline,
            size: 64,
          ),
          SizedBox(height: 16),
          Center(
            child: Text(
              'No conversations yet',
              style: TextStyle(
                fontSize: 18,
                fontWeight: FontWeight.w600,
              ),
            ),
          ),
          SizedBox(height: 8),
          Center(
            child: Padding(
              padding: EdgeInsets.symmetric(horizontal: 32),
              child: Text(
                'Your conversations with sellers will appear here.',
                textAlign: TextAlign.center,
              ),
            ),
          ),
        ],
      ),
    );
  }

  // ============================================================
  // Error state
  // ============================================================

  Widget _buildErrorState(Object error) {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(24),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            const Icon(
              Icons.error_outline,
              size: 56,
            ),
            const SizedBox(height: 16),
            const Text(
              'Could not load conversations.',
              textAlign: TextAlign.center,
              style: TextStyle(
                fontSize: 17,
                fontWeight: FontWeight.w600,
              ),
            ),
            const SizedBox(height: 8),
            Text(
              apiErrorMessage(error),
              textAlign: TextAlign.center,
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                if (_buyerId != null) {
                  ref.invalidate(
                    buyerConversationsProvider(_buyerId!),
                  );
                }
              },
              child: const Text('Try Again'),
            ),
          ],
        ),
      ),
    );
  }
}

// ============================================================================
// Conversation Tile
// ============================================================================

class _ConversationTile extends ConsumerWidget {
  const _ConversationTile({
    required this.conversation,
    required this.onTap,
  });

  final ConversationResponseDTO conversation;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final conversationId = conversation.id;

    final unreadAsync = conversationId == null
        ? const AsyncValue<int>.data(0)
        : ref.watch(
      unreadMessagesCountProvider(conversationId),
    );

    final unreadCount = unreadAsync.value ?? 0;

    return InkWell(
      onTap: onTap,
      child: Padding(
        padding: const EdgeInsets.symmetric(
          horizontal: 16,
          vertical: 12,
        ),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            _buildAvatar(),

            const SizedBox(width: 14),

            Expanded(
              child: _buildConversationInformation(
                context,
                unreadCount,
              ),
            ),

            const SizedBox(width: 8),

            _buildTrailing(
              context,
              unreadCount,
            ),
          ],
        ),
      ),
    );
  }

  // ============================================================
  // Avatar
  // ============================================================

  Widget _buildAvatar() {
    final image = conversation.gigImage;

    if (image == null || image.isEmpty) {
      return const CircleAvatar(
        radius: 28,
        child: Icon(
          Icons.work_outline,
          size: 27,
        ),
      );
    }

    return CircleAvatar(
      radius: 28,
      backgroundImage: NetworkImage(
        '${ApiConstants.gigImageUrl}$image',
      ),
      onBackgroundImageError: (_, __) {},
      child: const Icon(
        Icons.work_outline,
      ),
    );
  }

  // ============================================================
  // Conversation information
  // ============================================================

  Widget _buildConversationInformation(
      BuildContext context,
      int unreadCount,
      ) {
    final sellerName = conversation.sellerName?.trim().isNotEmpty == true
        ? conversation.sellerName!
        : 'Seller';

    final gigTitle = conversation.gigTitle?.trim().isNotEmpty == true
        ? conversation.gigTitle!
        : 'Gig conversation';

    final isUnread = unreadCount > 0;

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          sellerName,
          maxLines: 1,
          overflow: TextOverflow.ellipsis,
          style: TextStyle(
            fontSize: 16,
            fontWeight:
            isUnread ? FontWeight.w700 : FontWeight.w600,
          ),
        ),

        const SizedBox(height: 3),

        Text(
          gigTitle,
          maxLines: 1,
          overflow: TextOverflow.ellipsis,
          style: TextStyle(
            fontSize: 13,
            fontWeight:
            isUnread ? FontWeight.w600 : FontWeight.normal,
          ),
        ),

        const SizedBox(height: 5),

        Row(
          children: [
            _StatusBadge(
              status: conversation.conversationStatus,
            ),

            const SizedBox(width: 7),

            if (conversation.status != null)
              Expanded(
                child: Text(
                  _formatOrderStatus(
                    conversation.status!.toJson(),
                  ),
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                  style: const TextStyle(
                    fontSize: 11,
                  ),
                ),
              ),
          ],
        ),
      ],
    );
  }

  // ============================================================
  // Trailing section
  // ============================================================

  Widget _buildTrailing(
      BuildContext context,
      int unreadCount,
      ) {
    final time = _formatTime(
      conversation.lastMessageAt ??
          conversation.createdAt,
    );

    return Column(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        Text(
          time,
          style: TextStyle(
            fontSize: 11,
            fontWeight:
            unreadCount > 0
                ? FontWeight.w600
                : FontWeight.normal,
          ),
        ),

        const SizedBox(height: 7),

        if (unreadCount > 0)
          Container(
            constraints: const BoxConstraints(
              minWidth: 22,
              minHeight: 22,
            ),
            padding: const EdgeInsets.symmetric(
              horizontal: 6,
            ),
            decoration: BoxDecoration(
              color: Theme.of(context).colorScheme.primary,
              borderRadius: BorderRadius.circular(12),
            ),
            alignment: Alignment.center,
            child: Text(
              unreadCount > 99
                  ? '99+'
                  : unreadCount.toString(),
              style: TextStyle(
                color: Theme.of(context)
                    .colorScheme
                    .onPrimary,
                fontSize: 11,
                fontWeight: FontWeight.bold,
              ),
            ),
          ),
      ],
    );
  }

  // ============================================================
  // Time formatting
  // ============================================================

  String _formatTime(DateTime? dateTime) {
    if (dateTime == null) return '';

    final now = DateTime.now();
    final local = dateTime.toLocal();

    final difference = now.difference(local);

    if (difference.inMinutes < 1) {
      return 'Now';
    }

    if (difference.inHours < 1) {
      return '${difference.inMinutes}m';
    }

    if (difference.inDays < 1) {
      final hour = local.hour > 12
          ? local.hour - 12
          : local.hour == 0
          ? 12
          : local.hour;

      final minute =
      local.minute.toString().padLeft(2, '0');

      final period =
      local.hour >= 12 ? 'PM' : 'AM';

      return '$hour:$minute $period';
    }

    if (difference.inDays < 7) {
      return _weekdayName(local.weekday);
    }

    return '${local.day}/${local.month}/${local.year}';
  }

  String _weekdayName(int weekday) {
    const days = [
      'Mon',
      'Tue',
      'Wed',
      'Thu',
      'Fri',
      'Sat',
      'Sun',
    ];

    return days[weekday - 1];
  }

  String _formatOrderStatus(String status) {
    return status
        .replaceAll('_', ' ')
        .toLowerCase()
        .split(' ')
        .map(
          (word) => word.isEmpty
          ? word
          : '${word[0].toUpperCase()}${word.substring(1)}',
    )
        .join(' ');
  }
}

// ============================================================================
// Conversation Status Badge
// ============================================================================

class _StatusBadge extends StatelessWidget {
  const _StatusBadge({
    required this.status,
  });

  final ConversationStatus? status;

  @override
  Widget build(BuildContext context) {
    final isActive = status == ConversationStatus.active;

    final text = isActive ? 'Active' : 'Closed';

    return Container(
      padding: const EdgeInsets.symmetric(
        horizontal: 6,
        vertical: 2,
      ),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(4),
        border: Border.all(
          color: Theme.of(context)
              .colorScheme
              .outline,
        ),
      ),
      child: Text(
        text,
        style: const TextStyle(
          fontSize: 10,
          fontWeight: FontWeight.w500,
        ),
      ),
    );
  }
}