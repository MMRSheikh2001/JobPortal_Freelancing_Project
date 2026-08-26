import 'dart:io';

import 'package:file_picker/file_picker.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/resume_file_response.dart';
import 'package:work_bridge_flutter/cvinformations/providers/user_profile_provider.dart';
import 'package:work_bridge_flutter/router/app_router.dart';
import 'package:work_bridge_flutter/utils/api_client.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

class ResumeFileScreen extends ConsumerStatefulWidget {
  const ResumeFileScreen({super.key});

  @override
  ConsumerState<ResumeFileScreen> createState() => _ResumeFileScreenState();
}

class _ResumeFileScreenState extends ConsumerState<ResumeFileScreen> {
  ResumeFileResponseDTO? _uploadedResume;
  bool _loading = true;
  String? _loadError;

  bool _uploading = false;
  bool _deleting = false;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) => _load());
  }

  Future<void> _load() async {
    setState(() {
      _loading = true;
      _loadError = null;
    });

    try {
      final profile = await ref.read(myUserProfileProvider.future);
      final profileId = profile?.id;

      if (profileId == null) {
        if (!mounted) return;
        setState(() {
          _loading = false;
          _loadError =
              'Profile not found. Please complete your personal information first.';
        });
        return;
      }

      final resume = await ref
          .read(cvRepositoryProvider)
          .getResumeFileByUserProfileId(profileId);
      if (!mounted) return;
      setState(() => _uploadedResume = resume);
    } catch (e) {
      final message = apiErrorMessage(e);
      final isNotFound =
          message.toLowerCase().contains('not found') || message.contains('404');
      if (!mounted) return;
      setState(() {
        _uploadedResume = null;
        _loadError = isNotFound ? null : message;
      });
    } finally {
      if (mounted) setState(() => _loading = false);
    }
  }

  Future<void> _uploadOrReplaceResume() async {
    try {
      final profile = await ref.read(myUserProfileProvider.future);
      final profileId = profile?.id;

      if (profileId == null) {
        if (!mounted) return;
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
              content: Text('Profile not found. Please save your info first.')),
        );
        return;
      }

      final pickedFile = await FilePicker.pickFile(
        type: FileType.custom,
        allowedExtensions: ['pdf', 'docx'],
      );

      if (pickedFile == null) return;

      setState(() => _uploading = true);

      final repo = ref.read(cvRepositoryProvider);
      ResumeFileResponseDTO uploaded;

      if (kIsWeb) {
        final bytes = await pickedFile.readAsBytes();
        uploaded = await repo.uploadResume(
          profileId,
          null,
          bytes: bytes,
          fileName: pickedFile.name,
        );
      } else {
        if (pickedFile.path != null) {
          uploaded = await repo.uploadResume(
            profileId,
            File(pickedFile.path!),
          );
        } else {
          // Fallback to bytes if path is null (e.g. cloud file on mobile)
          final bytes = await pickedFile.readAsBytes();
          uploaded = await repo.uploadResume(
            profileId,
            null,
            bytes: bytes,
            fileName: pickedFile.name,
          );
        }
      }

      if (!mounted) return;
      setState(() => _uploadedResume = uploaded);
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Resume uploaded.')),
      );
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
            content: Text('Could not upload resume: ${apiErrorMessage(e)}')),
      );
    } finally {
      if (mounted) setState(() => _uploading = false);
    }
  }

  Future<void> _viewUploadedResume() async {
    final fileName = _uploadedResume?.fileName;
    if (fileName == null) return;
    final url = Uri.parse('${ApiConstants.resumeFileUrl}$fileName');
    final opened = await launchUrl(url, mode: LaunchMode.externalApplication);
    if (!opened && mounted) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Could not open resume.')),
      );
    }
  }

  Future<void> _deleteUploadedResume() async {
    setState(() => _deleting = true);
    try {
      final profile = await ref.read(myUserProfileProvider.future);
      final profileId = profile?.id;

      if (profileId == null) throw Exception('Profile not found.');

      if (!mounted) return;
      final confirmed = await showDialog<bool>(
        context: context,
        builder: (ctx) => AlertDialog(
          title: const Text('Delete resume?'),
          content: const Text(
            'This removes your uploaded resume file. You can upload a new '
            'one at any time. This cannot be undone.',
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(ctx).pop(false),
              child: const Text('Cancel'),
            ),
            FilledButton(
              onPressed: () => Navigator.of(ctx).pop(true),
              style: FilledButton.styleFrom(backgroundColor: Colors.red),
              child: const Text('Delete'),
            ),
          ],
        ),
      );
      if (confirmed != true) {
        setState(() => _deleting = false);
        return;
      }

      await ref
          .read(cvRepositoryProvider)
          .deleteResumeFileByUserProfileId(profileId);
      if (!mounted) return;
      setState(() => _uploadedResume = null);
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Resume deleted.')),
      );
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
            content: Text('Could not delete resume: ${apiErrorMessage(e)}')),
      );
    } finally {
      if (mounted) setState(() => _deleting = false);
    }
  }

  void _goToImportResume() {
    final profileId = ref.read(myUserProfileProvider).value?.id;
    if (profileId == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please save your personal info first.')),
      );
      return;
    }
    Navigator.of(context).pushNamed(
      AppRouter.resumeFileImport,
      arguments: profileId,
    );
  }

  @override
  Widget build(BuildContext context) {
    final profileAsync = ref.watch(myUserProfileProvider);
    final profileId = profileAsync.value?.id;

    return Scaffold(
      appBar: AppBar(title: const Text('Resume')),
      body: _loading
          ? const Center(child: CircularProgressIndicator())
          : RefreshIndicator(
              onRefresh: _load,
              child: ListView(
                padding: const EdgeInsets.all(16),
                children: [
                  if (_loadError != null)
                    Padding(
                      padding: const EdgeInsets.only(bottom: 16),
                      child: Container(
                        width: double.infinity,
                        padding: const EdgeInsets.all(12),
                        decoration: BoxDecoration(
                          color: Colors.red.shade50,
                          borderRadius: BorderRadius.circular(8),
                        ),
                        child: Text(
                          'Failed to load resume: $_loadError',
                          style: const TextStyle(color: Colors.red),
                        ),
                      ),
                    ),

                  const Text(
                    'Uploaded Resume',
                    style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 4),
                  const Text(
                    'Your own resume file (PDF/DOCX).',
                    style: TextStyle(fontSize: 12, color: Colors.black54),
                  ),
                  const SizedBox(height: 10),
                  _buildUploadedResumeSection(),
                  const SizedBox(height: 28),

                  const Text(
                    'CV Generated from Your Profile',
                    style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 4),
                  const Text(
                    'Automatically built from your education, experience, '
                    'skills, and other profile sections.',
                    style: TextStyle(fontSize: 12, color: Colors.black54),
                  ),
                  const SizedBox(height: 10),

                  _GeneratedCvButton(profileId: profileId),

                  const SizedBox(height: 28),

                  const Divider(),
                  const SizedBox(height: 12),
                  const Text(
                    'Already have a resume?',
                    style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 4),
                  const Text(
                    'Import an existing resume file and we\'ll pull its '
                    'details into your profile automatically.',
                    style: TextStyle(fontSize: 12, color: Colors.black54),
                  ),
                  const SizedBox(height: 10),
                  FilledButton.tonalIcon(
                    onPressed: _goToImportResume,
                    icon: const Icon(Icons.auto_fix_high_outlined),
                    label: const Text('Import Resume'),
                  ),
                  const SizedBox(height: 16),
                ],
              ),
            ),
    );
  }

  Widget _buildUploadedResumeSection() {
    if (_uploadedResume != null) {
      return Container(
        padding: const EdgeInsets.all(12),
        decoration: BoxDecoration(
          color: Colors.grey.shade100,
          borderRadius: BorderRadius.circular(8),
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                const Icon(Icons.insert_drive_file_outlined),
                const SizedBox(width: 10),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        _uploadedResume!.fileName ?? 'resume',
                        overflow: TextOverflow.ellipsis,
                        style: const TextStyle(fontWeight: FontWeight.w600),
                      ),
                      if (_uploadedResume!.uploadedAt != null)
                        Text(
                          'Uploaded ${_formatDate(_uploadedResume!.uploadedAt!)}',
                          style: const TextStyle(
                            fontSize: 12,
                            color: Colors.black54,
                          ),
                        ),
                    ],
                  ),
                ),
              ],
            ),
            const SizedBox(height: 8),
            Wrap(
              spacing: 8,
              children: [
                OutlinedButton.icon(
                  onPressed: _viewUploadedResume,
                  icon: const Icon(Icons.visibility_outlined, size: 18),
                  label: const Text('View'),
                ),
                OutlinedButton.icon(
                  onPressed: _uploading ? null : _uploadOrReplaceResume,
                  icon: _uploading
                      ? const SizedBox(
                          height: 14,
                          width: 14,
                          child: CircularProgressIndicator(strokeWidth: 2),
                        )
                      : const Icon(Icons.upload_file, size: 18),
                  label: const Text('Replace'),
                ),
                OutlinedButton.icon(
                  onPressed: _deleting ? null : _deleteUploadedResume,
                  style: OutlinedButton.styleFrom(foregroundColor: Colors.red),
                  icon: _deleting
                      ? const SizedBox(
                          height: 14,
                          width: 14,
                          child: CircularProgressIndicator(strokeWidth: 2),
                        )
                      : const Icon(Icons.delete_outline, size: 18),
                  label: const Text('Delete'),
                ),
              ],
            ),
          ],
        ),
      );
    }

    return OutlinedButton.icon(
      onPressed: _uploading ? null : _uploadOrReplaceResume,
      icon: _uploading
          ? const SizedBox(
              height: 16,
              width: 16,
              child: CircularProgressIndicator(strokeWidth: 2),
            )
          : const Icon(Icons.upload_file),
      label: Text(_uploading ? 'Uploading...' : 'Upload Resume (PDF/DOCX)'),
    );
  }

  String _formatDate(DateTime d) => '${d.day}/${d.month}/${d.year}';
}

class _GeneratedCvButton extends ConsumerWidget {
  const _GeneratedCvButton({
    required this.profileId,
  });

  final int? profileId;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    if (profileId == null) {
      return const SizedBox.shrink();
    }

    return SizedBox(
      width: double.infinity,
      child: ElevatedButton.icon(
        icon: const Icon(Icons.picture_as_pdf),
        label: const Text('View Generated CV'),
        onPressed: () async {
          final url = Uri.parse(
            '${ApiConstants.baseUrl}${ApiConstants.resumePdf(profileId!)}',
          );

          try {
            if (await canLaunchUrl(url)) {
              await launchUrl(
                url,
                mode: LaunchMode.externalApplication,
              );
            } else {
              if (!context.mounted) return;
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(
                  content: Text('Could not open the CV PDF.'),
                ),
              );
            }
          } catch (e) {
            if (!context.mounted) return;

            ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(
                content: Text(
                  'Error opening CV.',
                ),
              ),
            );
          }
        },
      ),
    );
  }
}
