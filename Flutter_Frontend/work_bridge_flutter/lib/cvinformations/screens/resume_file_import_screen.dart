import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/resume_import_preview.dart';
import 'package:work_bridge_flutter/utils/api_client.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

/// Shows the profile data WorkBridge's AI extracted from the user's
/// uploaded resume (backend: ResumeImportController.getJSONFromResume,
/// which runs Gemini over the resume text) and lets the user choose to
/// commit it to their profile or discard it.
class ResumeFileImportScreen extends ConsumerStatefulWidget {
  const ResumeFileImportScreen({
    super.key,
    required this.userProfileId,
    this.initialPreview,
  });

  final int userProfileId;
  final ResumeImportPreviewDTO? initialPreview;

  @override
  ConsumerState<ResumeFileImportScreen> createState() =>
      _ResumeFileImportScreenState();
}

class _ResumeFileImportScreenState
    extends ConsumerState<ResumeFileImportScreen> {
  ResumeImportPreviewDTO? _preview;
  bool _loading = true;
  String? _loadError;
  bool _saving = false;

  @override
  void initState() {
    super.initState();
    if (widget.initialPreview != null) {
      _preview = widget.initialPreview;
      _loading = false;
    } else {
      WidgetsBinding.instance.addPostFrameCallback((_) => _load());
    }
  }

  Future<void> _load() async {
    setState(() {
      _loading = true;
      _loadError = null;
    });

    try {
      final preview = await ref
          .read(cvRepositoryProvider)
          .getResumeImportPreview(widget.userProfileId);
      if (!mounted) return;
      setState(() => _preview = preview);
    } catch (e) {
      if (!mounted) return;
      setState(() => _loadError = apiErrorMessage(e));
    } finally {
      if (mounted) setState(() => _loading = false);
    }
  }

  Future<void> _confirmAndSave() async {
    final preview = _preview;
    if (preview == null) return;

    final confirmed = await showDialog<bool>(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Save to your profile?'),
        content: const Text(
          'This adds the extracted education, experience, and other '
          'sections above to your profile. You can review or edit any of '
          'it afterward from your Profile Center.',
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(ctx).pop(false),
            child: const Text('Cancel'),
          ),
          FilledButton(
            onPressed: () => Navigator.of(ctx).pop(true),
            child: const Text('Save'),
          ),
        ],
      ),
    );
    if (confirmed != true) return;

    setState(() => _saving = true);
    try {
      await ref
          .read(cvRepositoryProvider)
          .saveImportedResume(widget.userProfileId, preview);
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Resume imported into your profile.')),
      );
      Navigator.of(context).pop(true); // true = caller should refresh
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Could not save: ${apiErrorMessage(e)}')),
      );
    } finally {
      if (mounted) setState(() => _saving = false);
    }
  }

  void _discard() {
    Navigator.of(context).pop(false);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Review Imported Resume')),
      body: _loading
          ? const _LoadingState()
          : _loadError != null
              ? _ErrorState(message: _loadError!, onRetry: _load)
              : _preview == null
                  ? const Center(child: Text('Nothing to review.'))
                  : _buildPreview(_preview!),
    );
  }

  Widget _buildPreview(ResumeImportPreviewDTO preview) {
    return Column(
      children: [
        Expanded(
          child: ListView(
            padding: const EdgeInsets.fromLTRB(16, 16, 16, 8),
            children: [
              Container(
                padding: const EdgeInsets.all(12),
                decoration: BoxDecoration(
                  color: Colors.blue.shade50,
                  borderRadius: BorderRadius.circular(8),
                ),
                child: const Row(
                  children: [
                    Icon(Icons.smart_toy_outlined, color: Colors.blue),
                    SizedBox(width: 10),
                    Expanded(
                      child: Text(
                        'Here\'s what our AI found in your resume. Review it '
                        'below, then choose to save it to your profile or '
                        'discard it.',
                        style: TextStyle(fontSize: 13),
                      ),
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 20),

              if (preview.profile != null) ...[
                const _SectionHeader(title: 'Profile'),
                _ProfileSummaryCard(profile: preview.profile!),
                const SizedBox(height: 20),
              ],

              _ListSection(
                title: 'Education',
                items: preview.educations,
                itemBuilder: (e) => _SimpleEntryCard(
                  title: e.institution ?? 'Untitled',
                  subtitle: [
                    e.fieldOfStudy,
                    _formatEnum(e.educationLevel),
                  ].whereType<String>().where((s) => s.isNotEmpty).join(' • '),
                ),
              ),

              _ListSection(
                title: 'Experience',
                items: preview.experiences,
                itemBuilder: (e) => _SimpleEntryCard(
                  title: e.position ?? 'Untitled',
                  subtitle: e.companyName ?? '',
                ),
              ),

              _ListSection(
                title: 'Training',
                items: preview.trainings,
                itemBuilder: (t) => _SimpleEntryCard(
                  title: t.name ?? 'Untitled',
                  subtitle: t.institution ?? '',
                ),
              ),

              _ListSection(
                title: 'Portfolio',
                items: preview.portfolios,
                itemBuilder: (p) => _SimpleEntryCard(
                  title: p.title ?? 'Untitled',
                  subtitle: p.technologies ?? '',
                ),
              ),

              _ListSection(
                title: 'References',
                items: preview.references,
                itemBuilder: (r) => _SimpleEntryCard(
                  title: r.name ?? 'Untitled',
                  subtitle: [
                    r.designation,
                    r.organization,
                  ].whereType<String>().where((s) => s.isNotEmpty).join(' • '),
                ),
              ),

              _ListSection(
                title: 'Extracurricular',
                items: preview.extracurriculars,
                itemBuilder: (e) => _SimpleEntryCard(
                  title: e.title ?? 'Untitled',
                  subtitle: e.organization ?? '',
                ),
              ),

              const SizedBox(height: 8),
            ],
          ),
        ),

        // ── Action bar ─────────────────────────────────
        Container(
          padding: EdgeInsets.fromLTRB(
            16,
            12,
            16,
            12 + MediaQuery.of(context).padding.bottom,
          ),
          decoration: BoxDecoration(
            color: Theme.of(context).scaffoldBackgroundColor,
            boxShadow: [
              BoxShadow(
                color: Colors.black.withValues(alpha: 0.08),
                blurRadius: 8,
                offset: const Offset(0, -2),
              ),
            ],
          ),
          child: Row(
            children: [
              Expanded(
                child: OutlinedButton(
                  onPressed: _saving ? null : _discard,
                  style: OutlinedButton.styleFrom(
                    foregroundColor: Colors.red,
                    side: const BorderSide(color: Colors.red),
                    minimumSize: const Size.fromHeight(48),
                  ),
                  child: const Text('Discard'),
                ),
              ),
              const SizedBox(width: 12),
              Expanded(
                child: FilledButton(
                  onPressed: _saving ? null : _confirmAndSave,
                  style: FilledButton.styleFrom(
                    minimumSize: const Size.fromHeight(48),
                  ),
                  child: _saving
                      ? const SizedBox(
                          height: 20,
                          width: 20,
                          child: CircularProgressIndicator(
                            strokeWidth: 2,
                            color: Colors.white,
                          ),
                        )
                      : const Text('Save to Profile'),
                ),
              ),
            ],
          ),
        ),
      ],
    );
  }

  /// Safely formats enum values for display to avoid 'method not found'
  /// crashes if the AI returns unexpected data or the DTO mapping has issues.
  String? _formatEnum(dynamic value) {
    if (value == null) return null;
    try {
      // Handles both real enums (using .name) and strings
      return value.toString().split('.').last.toUpperCase();
    } catch (_) {
      return value.toString();
    }
  }
}

class _LoadingState extends StatelessWidget {
  const _LoadingState();

  @override
  Widget build(BuildContext context) {
    return const Center(
      child: Padding(
        padding: EdgeInsets.all(24),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            CircularProgressIndicator(),
            SizedBox(height: 16),
            Text(
              'AI is analyzing your resume...\nThis can take a moment.',
              textAlign: TextAlign.center,
              style: TextStyle(color: Colors.black54),
            ),
          ],
        ),
      ),
    );
  }
}

class _ErrorState extends StatelessWidget {
  const _ErrorState({required this.message, required this.onRetry});

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
            const Icon(Icons.error_outline, size: 40, color: Colors.red),
            const SizedBox(height: 12),
            Text(
              'Could not analyze your resume: $message',
              textAlign: TextAlign.center,
            ),
            const SizedBox(height: 12),
            OutlinedButton(onPressed: onRetry, child: const Text('Retry')),
          ],
        ),
      ),
    );
  }
}

class _SectionHeader extends StatelessWidget {
  const _SectionHeader({required this.title});

  final String title;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 8),
      child: Text(
        title,
        style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
      ),
    );
  }
}

class _ListSection extends StatelessWidget {
  const _ListSection({
    required this.title,
    required this.items,
    required this.itemBuilder,
  });

  final String title;
  final List<dynamic>? items;
  final Widget Function(dynamic item) itemBuilder;

  @override
  Widget build(BuildContext context) {
    if (items == null || items!.isEmpty) return const SizedBox.shrink();

    return Padding(
      padding: const EdgeInsets.only(bottom: 20),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          _SectionHeader(title: '$title (${items!.length})'),
          ...items!.map(itemBuilder),
        ],
      ),
    );
  }
}

class _SimpleEntryCard extends StatelessWidget {
  const _SimpleEntryCard({required this.title, required this.subtitle});

  final String title;
  final String subtitle;

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.only(bottom: 8),
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              title,
              style: const TextStyle(fontWeight: FontWeight.w600),
            ),
            if (subtitle.isNotEmpty) ...[
              const SizedBox(height: 2),
              Text(
                subtitle,
                style: const TextStyle(fontSize: 13, color: Colors.black54),
              ),
            ],
          ],
        ),
      ),
    );
  }
}

class _ProfileSummaryCard extends StatelessWidget {
  const _ProfileSummaryCard({required this.profile});

  final dynamic profile;

  @override
  Widget build(BuildContext context) {
    final fields = <String, String?>{
      'Name': profile.name,
      'Headline': profile.headline,
      'Phone': profile.phone,
      'Nationality': profile.nationality,
      'Summary': profile.professionalSummary,
    }..removeWhere((_, v) => v == null || v.toString().trim().isEmpty);

    if (fields.isEmpty) {
      return const _SimpleEntryCard(
        title: 'No profile details extracted',
        subtitle: '',
      );
    }

    return Card(
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: fields.entries
              .map(
                (e) => Padding(
                  padding: const EdgeInsets.only(bottom: 6),
                  child: RichText(
                    text: TextSpan(
                      style: DefaultTextStyle.of(context).style,
                      children: [
                        TextSpan(
                          text: '${e.key}: ',
                          style: const TextStyle(fontWeight: FontWeight.w600),
                        ),
                        TextSpan(text: e.value.toString()),
                      ],
                    ),
                  ),
                ),
              )
              .toList(),
        ),
      ),
    );
  }
}
