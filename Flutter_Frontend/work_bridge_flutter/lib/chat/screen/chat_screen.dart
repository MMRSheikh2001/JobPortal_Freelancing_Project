import 'dart:io';

import 'package:file_picker/file_picker.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/chat/models/request/message_request.dart';
import 'package:work_bridge_flutter/chat/models/response/conversation_response.dart';
import 'package:work_bridge_flutter/chat/models/response/message_response.dart';
import 'package:work_bridge_flutter/chat/provider/conversation_provider.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';

class ChatScreen extends ConsumerStatefulWidget {
  final int conversationId;

  const ChatScreen({super.key, required this.conversationId});

  @override
  ConsumerState<ChatScreen> createState() => _ChatScreenState();
}

class _ChatScreenState extends ConsumerState<ChatScreen> {
  final TextEditingController _messageController = TextEditingController();
  final ScrollController _scrollController = ScrollController();

  File? _pickedFile;
  Uint8List? _pickedFileBytes;
  String? _pickedFileName;

  bool _sending = false;
  bool _markingAsRead = false;

  @override
  void initState() {
    super.initState();

    WidgetsBinding.instance.addPostFrameCallback((_) {
      _markMessagesAsRead();
    });
  }

  @override
  void dispose() {
    _messageController.dispose();
    _scrollController.dispose();
    super.dispose();
  }

  // ===========================================================================
  // MARK MESSAGES AS READ
  // ===========================================================================

  Future<void> _markMessagesAsRead() async {
    if (_markingAsRead) return;

    _markingAsRead = true;

    try {
      final userId = ref.read(currentUserProvider)?.userId;

      if (userId == null) return;

      await ref
          .read(conversationRepositoryProvider)
          .markConversationAsRead(
            conversationId: widget.conversationId,
            readerId: userId,
          );

      ref.invalidate(conversationMessagesProvider(widget.conversationId));
      ref.invalidate(unreadMessagesCountProvider(widget.conversationId));
    } catch (_) {
      // Do not interrupt the chat screen if marking messages as read fails.
    } finally {
      _markingAsRead = false;
    }
  }

  // ===========================================================================
  // PICK ATTACHMENT
  // ===========================================================================
  //
  // Future<void> _pickAttachment() async {
  //   try {
  //     final result = await FilePicker.pickFile(type: FileType.any);
  //
  //     if (result == null) {
  //       return;
  //     }
  //
  //     final path = result?.path;
  //     print("===========================================================jjhfvj====================");
  //     print(path);
  //
  //     if (path == null || path.isEmpty) {
  //       if (!mounted) return;
  //
  //       ScaffoldMessenger.of(context).showSnackBar(
  //         const SnackBar(content: Text('Could not access the selected file.')),
  //       );
  //
  //       return;
  //     }
  //
  //     setState(() {
  //       _selectedFile = File(path);
  //     });
  //
  //
  //   } catch (e) {
  //     if (!mounted) return;
  //
  //     ScaffoldMessenger.of(
  //       context,
  //     ).showSnackBar(SnackBar(content: Text('Could not select file: $e')));
  //   }
  // }


  Future<void> _pickAttachment() async {
    try {
      final result = await FilePicker.pickFile(type: FileType.any);

      // 1. User canceled the picker
      if (result == null) {
        return;
      }


      // 4. Update State Cross-Platform
      if (kIsWeb) {
        // On Web, use file.bytes instead of File(path)
        final Uint8List fileBytes = await result.readAsBytes();
        setState(() {
          _pickedFile = null;
          _pickedFileBytes = fileBytes;
          _pickedFileName = result.name;
        });
      } else {
        // On Mobile / Desktop, file.path is guaranteed to be non-null
        if (result.path != null) {
          setState(() {
            _pickedFile = File(result.path!);
            _pickedFileBytes = null;
            _pickedFileName = result.name;
          });
        }
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Error picking file: $e')),
        );
      }
    }
  }

  // ===========================================================================
  // REMOVE SELECTED ATTACHMENT
  // ===========================================================================

  void _removeSelectedFile() {
    setState(() {
      _pickedFile = null;
      _pickedFileBytes = null;
      _pickedFileName = null;
    });
  }

  // ===========================================================================
  // SEND MESSAGE
  // ===========================================================================

  Future<void> _sendMessage() async {
    final text = _messageController.text.trim();

    if (text.isEmpty && _pickedFile == null && _pickedFileBytes == null) {
      return;
    }

    if (_sending) return;

    final userId = ref.read(currentUserProvider)?.userId;

    if (userId == null) {
      if (!mounted) return;

      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Unable to identify the current user.')),
      );

      return;
    }

    setState(() {
      _sending = true;
    });

    try {
      final request = MessageRequestDTO(
        messageText: text.isEmpty ? null : text,
        conversationId: widget.conversationId,
      );

      await ref.read(conversationRepositoryProvider).sendMessage(
            message: request,
            senderId: userId,
            attachment: _pickedFile,
            bytes: _pickedFileBytes,
            fileName: _pickedFileName,
          );

      _messageController.clear();

      setState(() {
        _pickedFile = null;
        _pickedFileBytes = null;
        _pickedFileName = null;
      });

      ref.invalidate(conversationMessagesProvider(widget.conversationId));
      ref.invalidate(latestMessageProvider(widget.conversationId));
      ref.invalidate(unreadMessagesCountProvider(widget.conversationId));

      // Scrolling is handled by the provider listener in build().
    } catch (e) {
      if (!mounted) return;

      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('Failed to send message: $e')));
    } finally {
      if (mounted) {
        setState(() {
          _sending = false;
        });
      }
    }
  }

  // ===========================================================================
  // SCROLL TO BOTTOM
  // ===========================================================================

  void _scrollToBottom() {
    if (!_scrollController.hasClients) return;

    // Avoid redundant animations and potential loops
    final position = _scrollController.position;
    if (position.pixels < position.maxScrollExtent) {
      _scrollController.animateTo(
        position.maxScrollExtent,
        duration: const Duration(milliseconds: 250),
        curve: Curves.easeOut,
      );
    }
  }

  // ===========================================================================
  // BUILD
  // ===========================================================================

  @override
  Widget build(BuildContext context) {
    // Watch user identity once here
    final currentUserId = ref.watch(currentUserProvider)?.userId;

    final conversationAsync = ref.watch(
      conversationProvider(widget.conversationId),
    );

    final messagesAsync = ref.watch(
      conversationMessagesProvider(widget.conversationId),
    );

    // Auto-scroll when new messages arrive
    ref.listen(conversationMessagesProvider(widget.conversationId), (
      previous,
      next,
    ) {
      next.whenData((messages) {
        if (messages.isNotEmpty) {
          WidgetsBinding.instance.addPostFrameCallback((_) {
            if (mounted) {
              _scrollToBottom();
            }
          });
        }
      });
    });

    return Scaffold(
      appBar: _buildAppBar(conversationAsync, currentUserId),
      body: Column(
        children: [
          Expanded(
            child: messagesAsync.when(
              loading: () => const Center(child: CircularProgressIndicator()),
              error: (error, stack) => _buildError(error),
              data: (messages) {
                if (messages.isEmpty) {
                  return _buildEmptyMessages();
                }

                return _buildMessagesList(messages, currentUserId);
              },
            ),
          ),
          if (_pickedFile != null || _pickedFileBytes != null)
            _buildSelectedFilePreview(),
          _buildMessageInput(conversationAsync),
        ],
      ),
    );
  }

  // ===========================================================================
  // APP BAR
  // ===========================================================================

  PreferredSizeWidget _buildAppBar(
    AsyncValue<ConversationResponseDTO> conversationAsync,
    int? currentUserId,
  ) {
    return AppBar(
      titleSpacing: 0,
      title: conversationAsync.when(
        loading: () => const Text('Chat'),
        error: (_, __) => const Text('Chat'),
        data: (conversation) {
          return Row(
            children: [
              _buildConversationAvatar(conversation),
              const SizedBox(width: 10),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      _getOtherUserName(conversation, currentUserId),
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                      style: const TextStyle(
                        fontSize: 16,
                        fontWeight: FontWeight.w600,
                      ),
                    ),
                    if (conversation.gigTitle != null)
                      Text(
                        conversation.gigTitle!,
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                        style: TextStyle(
                          fontSize: 12,
                          color: Colors.grey.shade600,
                        ),
                      ),
                  ],
                ),
              ),
            ],
          );
        },
      ),
    );
  }

  Widget _buildConversationAvatar(ConversationResponseDTO conversation) {
    final image = conversation.gigImage;

    if (image != null && image.isNotEmpty) {
      return CircleAvatar(
        radius: 20,
        backgroundImage: NetworkImage('${ApiConstants.gigImageUrl}$image'),
      );
    }

    return const CircleAvatar(radius: 20, child: Icon(Icons.person));
  }

  String _getOtherUserName(
    ConversationResponseDTO conversation,
    int? currentUserId,
  ) {
    // If current user is the buyer, show seller's name.
    // Otherwise show buyer's name.
    if (currentUserId == conversation.buyerId) {
      return conversation.sellerName ?? 'Seller';
    }
    return conversation.buyerName ?? 'Buyer';
  }

  // ===========================================================================
  // MESSAGES LIST
  // ===========================================================================

  Widget _buildMessagesList(
    List<MessageResponseDTO> messages,
    int? currentUserId,
  ) {
    return ListView.builder(
      controller: _scrollController,
      padding: const EdgeInsets.fromLTRB(12, 16, 12, 16),
      itemCount: messages.length,
      itemBuilder: (context, index) {
        final message = messages[index];

        return _buildMessageBubble(message, currentUserId);
      },
    );
  }

  // ===========================================================================
  // MESSAGE BUBBLE
  // ===========================================================================

  Widget _buildMessageBubble(MessageResponseDTO message, int? currentUserId) {
    final isMine =
        currentUserId != null &&
        message.senderId != null &&
        currentUserId == message.senderId;

    return Align(
      alignment: isMine ? Alignment.centerRight : Alignment.centerLeft,
      child: Container(
        constraints: BoxConstraints(
          maxWidth: MediaQuery.of(context).size.width * 0.78,
        ),
        margin: const EdgeInsets.only(bottom: 10),
        padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 9),
        decoration: BoxDecoration(
          color: isMine ? Colors.blue : Colors.grey.shade200,
          borderRadius: BorderRadius.only(
            topLeft: const Radius.circular(14),
            topRight: const Radius.circular(14),
            bottomLeft: Radius.circular(isMine ? 14 : 3),
            bottomRight: Radius.circular(isMine ? 3 : 14),
          ),
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            if (message.attachment != null && message.attachment!.isNotEmpty)
              _buildAttachment(message, isMine),
            if (message.messageText != null &&
                message.messageText!.trim().isNotEmpty)
              Padding(
                padding: EdgeInsets.only(
                  top: message.attachment != null ? 8 : 0,
                ),
                child: Text(
                  message.messageText!,
                  style: TextStyle(
                    color: isMine ? Colors.white : Colors.black87,
                    fontSize: 15,
                  ),
                ),
              ),
            const SizedBox(height: 5),
            Row(
              mainAxisSize: MainAxisSize.min,
              children: [
                Text(
                  _formatMessageTime(message.sentAt),
                  style: TextStyle(
                    fontSize: 10,
                    color: isMine ? Colors.white70 : Colors.grey.shade600,
                  ),
                ),
                if (isMine) ...[
                  const SizedBox(width: 4),
                  Icon(
                    message.isRead == true ? Icons.done_all : Icons.done,
                    size: 14,
                    color: message.isRead == true
                        ? Colors.white
                        : Colors.white70,
                  ),
                ],
              ],
            ),
          ],
        ),
      ),
    );
  }

  // ===========================================================================
  // ATTACHMENT
  // ===========================================================================

  Widget _buildAttachment(MessageResponseDTO message, bool isMine) {
    final attachment = message.attachment!;

    return InkWell(
      onTap: () => _openAttachment(attachment),
      borderRadius: BorderRadius.circular(8),
      child: Container(
        padding: const EdgeInsets.all(10),
        decoration: BoxDecoration(
          color: isMine ? Colors.white.withValues(alpha: 0.12) : Colors.white,
          borderRadius: BorderRadius.circular(8),
        ),
        child: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(Icons.attach_file, color: isMine ? Colors.white : Colors.blue),
            const SizedBox(width: 8),
            Flexible(
              child: Text(
                attachment,
                maxLines: 2,
                overflow: TextOverflow.ellipsis,
                style: TextStyle(
                  color: isMine ? Colors.white : Colors.black87,
                  fontSize: 13,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Future<void> _openAttachment(String attachment) async {
    final url = Uri.parse('${ApiConstants.messageFileUrl}$attachment');

    try {
      final launched = await launchUrl(
        url,
        mode: LaunchMode.externalApplication,
      );

      if (!launched && mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Could not open attachment.')),
        );
      }
    } catch (e) {
      if (!mounted) return;

      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('Could not open attachment: $e')));
    }
  }

  // ===========================================================================
  // SELECTED FILE
  // ===========================================================================

  Widget _buildSelectedFilePreview() {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
      decoration: BoxDecoration(
        color: Colors.grey.shade100,
        border: Border(top: BorderSide(color: Colors.grey.shade300)),
      ),
      child: Row(
        children: [
          const Icon(Icons.attach_file, color: Colors.blue),
          const SizedBox(width: 8),
          Expanded(
            child: Text(
              _pickedFileName ?? 'Selected file',
              maxLines: 1,
              overflow: TextOverflow.ellipsis,
            ),
          ),
          IconButton(
            onPressed: _removeSelectedFile,
            icon: const Icon(Icons.close),
            tooltip: 'Remove attachment',
          ),
        ],
      ),
    );
  }

  // ===========================================================================
  // MESSAGE INPUT
  // ===========================================================================

  Widget _buildMessageInput(
    AsyncValue<ConversationResponseDTO> conversationAsync,
  ) {
    final isClosed = conversationAsync.maybeWhen(
      data: (conversation) =>
          conversation.conversationStatus != null &&
          conversation.conversationStatus!.name == 'closed',
      orElse: () => false,
    );

    if (isClosed) {
      return Container(
        width: double.infinity,
        padding: const EdgeInsets.all(14),
        decoration: BoxDecoration(
          color: Colors.grey.shade100,
          border: Border(top: BorderSide(color: Colors.grey.shade300)),
        ),
        child: const SafeArea(
          top: false,
          child: Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(Icons.lock_outline, size: 18, color: Colors.grey),
              SizedBox(width: 8),
              Text(
                'This conversation is closed.',
                style: TextStyle(
                  color: Colors.grey,
                  fontWeight: FontWeight.w500,
                ),
              ),
            ],
          ),
        ),
      );
    }

    return SafeArea(
      top: false,
      child: Container(
        padding: const EdgeInsets.fromLTRB(8, 8, 8, 8),
        decoration: BoxDecoration(
          color: Theme.of(context).scaffoldBackgroundColor,
          border: Border(top: BorderSide(color: Colors.grey.shade300)),
        ),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.end,
          children: [
            IconButton(
              onPressed: _sending ? null : _pickAttachment,
              icon: const Icon(Icons.attach_file),
              tooltip: 'Attach file',
            ),
            Expanded(
              child: TextField(
                controller: _messageController,
                minLines: 1,
                maxLines: 5,
                textInputAction: TextInputAction.newline,
                decoration: InputDecoration(
                  hintText: 'Write a message...',
                  filled: true,
                  fillColor: Colors.grey.shade100,
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(22),
                    borderSide: BorderSide.none,
                  ),
                  contentPadding: const EdgeInsets.symmetric(
                    horizontal: 16,
                    vertical: 10,
                  ),
                ),
                onSubmitted: (_) {
                  // Do not send automatically on keyboard submit.
                  // New line is allowed.
                },
              ),
            ),
            const SizedBox(width: 4),
            SizedBox(
              width: 46,
              height: 46,
              child: Material(
                color: _sending ? Colors.grey : Colors.blue,
                shape: const CircleBorder(),
                child: InkWell(
                  customBorder: const CircleBorder(),
                  onTap: _sending ? null : _sendMessage,
                  child: _sending
                      ? const Padding(
                          padding: EdgeInsets.all(13),
                          child: CircularProgressIndicator(
                            strokeWidth: 2,
                            color: Colors.white,
                          ),
                        )
                      : const Icon(Icons.send, color: Colors.white, size: 20),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  // ===========================================================================
  // EMPTY / ERROR
  // ===========================================================================

  Widget _buildEmptyMessages() {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(24),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(
              Icons.chat_bubble_outline,
              size: 60,
              color: Colors.grey.shade400,
            ),
            const SizedBox(height: 16),
            Text(
              'No messages yet',
              style: TextStyle(
                fontSize: 18,
                fontWeight: FontWeight.w600,
                color: Colors.grey.shade700,
              ),
            ),
            const SizedBox(height: 6),
            Text(
              'Start the conversation by sending a message.',
              textAlign: TextAlign.center,
              style: TextStyle(color: Colors.grey.shade600),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildError(Object error) {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(24),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(Icons.error_outline, size: 50, color: Colors.red),
            const SizedBox(height: 12),
            const Text(
              'Could not load messages.',
              style: TextStyle(fontSize: 17, fontWeight: FontWeight.w600),
            ),
            const SizedBox(height: 8),
            Text(
              error.toString(),
              textAlign: TextAlign.center,
              style: TextStyle(color: Colors.grey.shade600),
            ),
            const SizedBox(height: 16),
            ElevatedButton.icon(
              onPressed: () {
                ref.invalidate(
                  conversationMessagesProvider(widget.conversationId),
                );

                ref.invalidate(conversationProvider(widget.conversationId));
              },
              icon: const Icon(Icons.refresh),
              label: const Text('Retry'),
            ),
          ],
        ),
      ),
    );
  }

  // ===========================================================================
  // TIME
  // ===========================================================================

  String _formatMessageTime(DateTime? dateTime) {
    if (dateTime == null) return '';

    final local = dateTime.toLocal();

    final hour = local.hour == 0
        ? 12
        : local.hour > 12
        ? local.hour - 12
        : local.hour;

    final minute = local.minute.toString().padLeft(2, '0');

    final period = local.hour >= 12 ? 'PM' : 'AM';

    return '$hour:$minute $period';
  }
}
