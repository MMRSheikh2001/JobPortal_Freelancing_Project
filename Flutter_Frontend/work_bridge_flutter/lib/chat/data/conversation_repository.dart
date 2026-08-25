import 'dart:io';
import 'dart:typed_data';
import 'package:dio/dio.dart';
import 'package:work_bridge_flutter/chat/models/request/message_request.dart';
import 'package:work_bridge_flutter/chat/models/response/conversation_response.dart';
import 'package:work_bridge_flutter/chat/models/response/message_response.dart';
import 'package:work_bridge_flutter/utils/api_client.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';

class ConversationRepository {
  ConversationRepository(this._apiClient);

  final ApiClient _apiClient;
  Dio get _dio => _apiClient.dio;

  // ===========================================================================
  // CONVERSATIONS
  // ===========================================================================

  Future<ConversationResponseDTO> getConversationById(int id) async {
    final response = await _dio.get(ApiConstants.conversationById(id));
    return ConversationResponseDTO.fromJson(response.data);
  }

  Future<List<ConversationResponseDTO>> getBuyerConversations(int buyerId) async {
    final response = await _dio.get(ApiConstants.buyerConversations(buyerId));
    return (response.data as List)
        .map((json) => ConversationResponseDTO.fromJson(json))
        .toList();
  }

  Future<int> countBuyerConversations(int buyerId) async {
    final response = await _dio.get(ApiConstants.countBuyerConversations(buyerId));
    return (response.data as num).toInt();
  }

  // ===========================================================================
  // MESSAGES
  // ===========================================================================

  /// Sends a message. Uses `MultipartFile` if an attachment (File or bytes) is present.
  Future<MessageResponseDTO> sendMessage({
    required MessageRequestDTO message,
    required int senderId,
    File? attachment,
    Uint8List? bytes,
    String? fileName,
  }) async {
    final formData = FormData.fromMap({
      'message': MultipartFile.fromString(
        message.toJsonString(),
        contentType: DioMediaType('application', 'json'),
        filename: '',
      ),
    });

    if (attachment != null) {
      formData.files.add(
        MapEntry(
          'attachment',
          await MultipartFile.fromFile(
            attachment.path,
            filename: attachment.path.split('/').last,
          ),
        ),
      );
    } else if (bytes != null) {
      formData.files.add(
        MapEntry(
          'attachment',
          MultipartFile.fromBytes(
            bytes,
            filename: fileName ?? 'attachment',
          ),
        ),
      );
    }

    final response = await _dio.post(
      ApiConstants.sendMessage,
      data: formData,
      queryParameters: {'senderId': senderId},
    );

    return MessageResponseDTO.fromJson(response.data);
  }

  Future<MessageResponseDTO> getMessageById(int id) async {
    final response = await _dio.get(ApiConstants.messageById(id));
    return MessageResponseDTO.fromJson(response.data);
  }

  Future<List<MessageResponseDTO>> getConversationMessages(int conversationId) async {
    final response = await _dio.get(ApiConstants.conversationMessages(conversationId));
    return (response.data as List)
        .map((json) => MessageResponseDTO.fromJson(json))
        .toList();
  }

  Future<List<MessageResponseDTO>> getSenderMessages(int senderId) async {
    final response = await _dio.get(ApiConstants.senderMessages(senderId));
    return (response.data as List)
        .map((json) => MessageResponseDTO.fromJson(json))
        .toList();
  }

  Future<List<MessageResponseDTO>> getUnreadMessages(int conversationId) async {
    final response = await _dio.get(ApiConstants.unreadMessages(conversationId));
    return (response.data as List)
        .map((json) => MessageResponseDTO.fromJson(json))
        .toList();
  }

  Future<int> countUnreadMessages(int conversationId) async {
    final response = await _dio.get(ApiConstants.countUnreadMessages(conversationId));
    return (response.data as num).toInt();
  }

  Future<int> countUnreadMessagesForUser({
    required int conversationId,
    required int senderId,
  }) async {
    final response = await _dio.get(
      ApiConstants.countUnreadMessagesForUser(conversationId, senderId),
    );
    return (response.data as num).toInt();
  }

  Future<String> markConversationAsRead({
    required int conversationId,
    required int readerId,
  }) async {
    final response = await _dio.put(
      ApiConstants.markConversationAsRead(conversationId),
      queryParameters: {'readerId': readerId},
    );
    return response.data.toString();
  }

  Future<MessageResponseDTO> getLatestMessage(int conversationId) async {
    final response = await _dio.get(ApiConstants.latestMessage(conversationId));
    return MessageResponseDTO.fromJson(response.data);
  }
}