// Repository Provider
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/providers.dart';
import 'package:work_bridge_flutter/chat/data/conversation_repository.dart';
import 'package:work_bridge_flutter/chat/models/response/conversation_response.dart';
import 'package:work_bridge_flutter/chat/models/response/message_response.dart';

final conversationRepositoryProvider = Provider<ConversationRepository>((ref) {
  final apiClient = ref.watch(apiClientProvider);
  return ConversationRepository(apiClient);
});

// Conversations Providers
final buyerConversationsProvider = FutureProvider.family
    .autoDispose<List<ConversationResponseDTO>, int>((ref, buyerId) async {
      return ref
          .watch(conversationRepositoryProvider)
          .getBuyerConversations(buyerId);
    });

final buyerConversationsCountProvider = FutureProvider.family
    .autoDispose<int, int>((ref, buyerId) async {
      return ref
          .watch(conversationRepositoryProvider)
          .countBuyerConversations(buyerId);
    });

// Messages Providers
final conversationMessagesProvider = FutureProvider.family
    .autoDispose<List<MessageResponseDTO>, int>((ref, conversationId) async {
      return ref
          .watch(conversationRepositoryProvider)
          .getConversationMessages(conversationId);
    });

final unreadMessagesCountProvider = FutureProvider.family.autoDispose<int, int>(
  (ref, conversationId) async {
    return ref
        .watch(conversationRepositoryProvider)
        .countUnreadMessages(conversationId);
  },
);

final latestMessageProvider = FutureProvider.family
    .autoDispose<MessageResponseDTO, int>((ref, conversationId) async {
      return ref
          .watch(conversationRepositoryProvider)
          .getLatestMessage(conversationId);
    });

final conversationProvider = FutureProvider.family
    .autoDispose<ConversationResponseDTO, int>((ref, conversationId) async {
  return ref
      .watch(conversationRepositoryProvider)
      .getConversationById(conversationId);
});