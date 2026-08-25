import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/gig/entity/request/review_request.dart';
import 'package:work_bridge_flutter/gig/entity/response/review_response.dart';
import 'package:work_bridge_flutter/gig/provider/gig_provider.dart';
import 'package:work_bridge_flutter/utils/api_client.dart';

class ReviewAddEditScreen extends ConsumerStatefulWidget {
  const ReviewAddEditScreen({super.key, required this.gigOrderId});

  final int gigOrderId;

  @override
  ConsumerState<ReviewAddEditScreen> createState() =>
      _ReviewAddEditScreenState();
}

class _ReviewAddEditScreenState extends ConsumerState<ReviewAddEditScreen> {
  ReviewResponseDTO? review;

  final TextEditingController commentController = TextEditingController();

  int rating = 0;

  bool loading = false;
  bool saving = false;
  bool deleting = false;

  bool get isEditMode => review != null;

  @override
  void initState() {
    super.initState();
    loadReview();
  }

  @override
  void dispose() {
    commentController.dispose();
    super.dispose();
  }

  // =====================================================
  // Load Review
  // =====================================================

  Future<void> loadReview() async {
    setState(() {
      loading = true;
    });

    try {
      final result = await ref
          .read(reviewRepositoryProvider)
          .getByGigOrderId(widget.gigOrderId);

      if (!mounted) return;

      setState(() {
        review = result;

        if (result != null) {
          rating = result.rating ?? 0;
          commentController.text = result.comment ?? '';
        }

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
  // Save Review
  // =====================================================

  Future<void> saveReview() async {
    if (rating < 1 || rating > 5) {
      showMessage('Please select a rating.', isError: true);
      return;
    }

    setState(() {
      saving = true;
    });

    final request = ReviewRequestDTO(
      rating: rating,
      comment: commentController.text.trim().isEmpty
          ? null
          : commentController.text.trim(),
      gigOrderId: widget.gigOrderId,
    );

    try {
      ReviewResponseDTO result;

      if (isEditMode) {
        result = await ref
            .read(reviewRepositoryProvider)
            .update(review!.id!, request);
      } else {
        result = await ref.read(reviewRepositoryProvider).create(request);
      }

      if (!mounted) return;

      setState(() {
        review = result;
        rating = result.rating ?? rating;
        commentController.text = result.comment ?? '';
      });

      showMessage(
        isEditMode
            ? 'Review updated successfully.'
            : 'Review submitted successfully.',
      );
    } catch (e) {
      if (!mounted) return;

      showMessage(apiErrorMessage(e), isError: true);
    } finally {
      if (mounted) {
        setState(() {
          saving = false;
        });
      }
    }
  }

  // =====================================================
  // Delete Review
  // =====================================================

  Future<void> deleteReview() async {
    if (review?.id == null) return;

    final confirmed = await showConfirmationDialog(
      title: 'Delete Review',
      message: 'Are you sure you want to delete this review?',
      confirmText: 'Delete',
      isDanger: true,
    );

    if (!confirmed) return;

    setState(() {
      deleting = true;
    });

    try {
      await ref.read(reviewRepositoryProvider).delete(review!.id!);

      if (!mounted) return;

      showMessage('Review deleted successfully.');

      Navigator.pop(context);
    } catch (e) {
      if (!mounted) return;

      showMessage(apiErrorMessage(e), isError: true);
    } finally {
      if (mounted) {
        setState(() {
          deleting = false;
        });
      }
    }
  }

  // =====================================================
  // Confirmation Dialog
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
              child: const Text('No'),
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
  // Message
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
  // Rating
  // =====================================================

  Widget buildRating() {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: List.generate(5, (index) {
        final starNumber = index + 1;

        return IconButton(
          onPressed: saving || deleting
              ? null
              : () {
                  setState(() {
                    rating = starNumber;
                  });
                },
          icon: Icon(
            starNumber <= rating ? Icons.star : Icons.star_border,
            size: 42,
            color: Colors.amber,
          ),
        );
      }),
    );
  }

  // =====================================================
  // Build
  // =====================================================

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(isEditMode ? 'Edit Review' : 'Write Review')),
      body: loading
          ? const Center(child: CircularProgressIndicator())
          : SingleChildScrollView(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // =====================================
                  // Review Information
                  // =====================================
                  Card(
                    child: Padding(
                      padding: const EdgeInsets.all(16),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            isEditMode ? 'Your Review' : 'Write a Review',
                            style: const TextStyle(
                              fontSize: 20,
                              fontWeight: FontWeight.bold,
                            ),
                          ),

                          const SizedBox(height: 20),

                          const Text(
                            'Rating',
                            style: TextStyle(
                              fontSize: 16,
                              fontWeight: FontWeight.bold,
                            ),
                          ),

                          const SizedBox(height: 8),

                          buildRating(),

                          if (rating > 0)
                            Center(
                              child: Text(
                                '$rating / 5',
                                style: const TextStyle(
                                  fontSize: 16,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),

                          const SizedBox(height: 24),

                          const Text(
                            'Comment',
                            style: TextStyle(
                              fontSize: 16,
                              fontWeight: FontWeight.bold,
                            ),
                          ),

                          const SizedBox(height: 8),

                          TextField(
                            controller: commentController,
                            enabled: !saving && !deleting,
                            maxLines: 5,
                            decoration: const InputDecoration(
                              hintText: 'Write your experience...',
                              border: OutlineInputBorder(),
                              alignLabelWithHint: true,
                            ),
                          ),

                          const SizedBox(height: 20),

                          // =================================
                          // Save
                          // =================================
                          SizedBox(
                            width: double.infinity,
                            child: ElevatedButton(
                              onPressed: saving || deleting ? null : saveReview,
                              child: saving
                                  ? const SizedBox(
                                      height: 20,
                                      width: 20,
                                      child: CircularProgressIndicator(
                                        strokeWidth: 2,
                                        color: Colors.white,
                                      ),
                                    )
                                  : Text(
                                      isEditMode
                                          ? 'Update Review'
                                          : 'Submit Review',
                                    ),
                            ),
                          ),

                          // =================================
                          // Delete
                          // =================================
                          if (isEditMode) ...[
                            const SizedBox(height: 10),

                            SizedBox(
                              width: double.infinity,
                              child: OutlinedButton(
                                onPressed: saving || deleting
                                    ? null
                                    : deleteReview,
                                style: OutlinedButton.styleFrom(
                                  foregroundColor: Colors.red,
                                  side: const BorderSide(color: Colors.red),
                                ),
                                child: deleting
                                    ? const SizedBox(
                                        height: 20,
                                        width: 20,
                                        child: CircularProgressIndicator(
                                          strokeWidth: 2,
                                          color: Colors.red,
                                        ),
                                      )
                                    : const Text('Delete Review'),
                              ),
                            ),
                          ],
                        ],
                      ),
                    ),
                  ),

                  // =====================================
                  // Existing Review Information
                  // =====================================
                  if (isEditMode && review?.createdAt != null)
                    Padding(
                      padding: const EdgeInsets.only(top: 12),
                      child: Center(
                        child: Text(
                          'Created: ${formatDate(review!.createdAt!)}',
                          style: TextStyle(color: Colors.grey.shade600),
                        ),
                      ),
                    ),
                ],
              ),
            ),
    );
  }

  // =====================================================
  // Date
  // =====================================================

  String formatDate(DateTime date) {
    final local = date.toLocal();

    String twoDigits(int value) {
      return value.toString().padLeft(2, '0');
    }

    return '${local.day}/'
        '${twoDigits(local.month)}/'
        '${local.year} '
        '${twoDigits(local.hour)}:'
        '${twoDigits(local.minute)}';
  }
}
