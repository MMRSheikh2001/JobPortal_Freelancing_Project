import 'dart:io';

import 'package:file_picker/file_picker.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/cvinformations/models/request/portfolio_request.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

/// Add/Edit Portfolio form — same structure as AddEditTraining (which
/// works): a project file upload/view/remove section alongside the
/// regular fields. [portfolioId] null means "add new"; non-null means
/// "edit existing" and the form is pre-filled via getPortfolioById.
class AddEditPortfolio extends ConsumerStatefulWidget {
  const AddEditPortfolio({super.key, this.portfolioId});

  final int? portfolioId;

  bool get isEditing => portfolioId != null;

  @override
  ConsumerState<AddEditPortfolio> createState() => _AddEditPortfolioState();
}

class _AddEditPortfolioState extends ConsumerState<AddEditPortfolio> {
  final _formKey = GlobalKey<FormState>();

  final _titleCtrl = TextEditingController();
  final _descriptionCtrl = TextEditingController();
  final _projectUrlCtrl = TextEditingController();
  final _technologiesCtrl = TextEditingController();

  // File: three possible states —
  // 1. _pickedFile / _pickedFileBytes set: a new file was just picked,
  //    not uploaded yet.
  // 2. _existingFileUrl set (and _pickedFile null): a file is already
  //    saved on the backend; "View" opens it, "Replace" picks a new
  //    one, "Remove" deletes it via deletePortfolioFile.
  // 3. Both null: no file yet.
  File? _pickedFile;
  Uint8List? _pickedFileBytes;
  String? _pickedFileName;
  String? _existingFileUrl;

  bool _loading = false;
  bool _saving = false;
  bool _removingFile = false;
  String? _errorMessage;

  @override
  void initState() {
    super.initState();
    if (widget.isEditing) {
      _loadExisting();
    }
  }

  Future<void> _loadExisting() async {
    setState(() => _loading = true);
    try {
      final portfolio = await ref
          .read(cvRepositoryProvider)
          .getPortfolioById(widget.portfolioId!);

      _titleCtrl.text = portfolio.title ?? '';
      _descriptionCtrl.text = portfolio.description ?? '';
      _projectUrlCtrl.text = portfolio.projectUrl ?? '';
      _technologiesCtrl.text = portfolio.technologies ?? '';
      _existingFileUrl = portfolio.fileUrl;
    } catch (e) {
      if (!mounted) return;
      setState(() => _errorMessage = 'Failed to load: $e');
    } finally {
      if (mounted) setState(() => _loading = false);
    }
  }

  @override
  void dispose() {
    _titleCtrl.dispose();
    _descriptionCtrl.dispose();
    _projectUrlCtrl.dispose();
    _technologiesCtrl.dispose();
    super.dispose();
  }

  Future<void> _pickProjectFile() async {
    try {
      final file = await FilePicker.pickFile(
        type: FileType.custom,
        allowedExtensions: ['pdf', 'jpg', 'jpeg', 'png'],
      );

      // User canceled the picker
      if (file == null) {
        return;
      }

      // Validate extension (fallback safety check)
      final allowedExts = ['pdf', 'jpg', 'jpeg', 'png'];
      final name = file.name.toLowerCase();
      final extension = name.contains('.') ? name.split('.').last : null;

      if (extension == null || !allowedExts.contains(extension)) {
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(
              content: Text(
                'Invalid file type. Please upload a PDF, JPG, or PNG.',
              ),
            ),
          );
        }
        return;
      }

      // Update state cross-platform
      if (kIsWeb) {
        // On Web, use file.bytes instead of File(path)
        final Uint8List fileBytes = await file.readAsBytes();
        setState(() {
          _pickedFile = null;
          _pickedFileBytes = fileBytes;
          _pickedFileName = file.name;
        });
      } else {
        // On Mobile / Desktop, file.path is guaranteed to be non-null
        if (file.path != null) {
          setState(() {
            _pickedFile = File(file.path!);
            _pickedFileBytes = null;
            _pickedFileName = file.name;
          });
        }
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(
          context,
        ).showSnackBar(SnackBar(content: Text('Error picking file: $e')));
      }
    }
  }

  Future<void> _viewFile() async {
    if (_existingFileUrl == null) return;
    final url = Uri.parse('${ApiConstants.portfolioFileUrl}$_existingFileUrl');
    final opened = await launchUrl(url, mode: LaunchMode.externalApplication);
    if (!opened && mounted) {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(const SnackBar(content: Text('Could not open file.')));
    }
  }

  Future<void> _removeExistingFile() async {
    if (widget.portfolioId == null) return;

    final confirmed = await showDialog<bool>(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Remove file?'),
        content: const Text(
          'This deletes the uploaded file from your portfolio entry. '
          'This cannot be undone.',
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(ctx).pop(false),
            child: const Text('Cancel'),
          ),
          FilledButton(
            onPressed: () => Navigator.of(ctx).pop(true),
            style: FilledButton.styleFrom(backgroundColor: Colors.red),
            child: const Text('Remove'),
          ),
        ],
      ),
    );
    if (confirmed != true) return;

    setState(() => _removingFile = true);
    try {
      await ref
          .read(cvRepositoryProvider)
          .deletePortfolioFile(widget.portfolioId!);
      if (!mounted) return;
      setState(() => _existingFileUrl = null);
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('Could not remove file: $e')));
    } finally {
      if (mounted) setState(() => _removingFile = false);
    }
  }

  Future<void> _save() async {
    if (!_formKey.currentState!.validate()) return;

    final profileId = ref.read(currentUserProvider)?.profileId;
    if (profileId == null) {
      setState(() => _errorMessage = 'Could not determine your profile.');
      return;
    }

    setState(() {
      _saving = true;
      _errorMessage = null;
    });

    try {
      final request = PortfolioRequestDTO(
        title: _titleCtrl.text.trim().isEmpty ? null : _titleCtrl.text.trim(),
        description: _descriptionCtrl.text.trim().isEmpty
            ? null
            : _descriptionCtrl.text.trim(),
        projectUrl: _projectUrlCtrl.text.trim().isEmpty
            ? null
            : _projectUrlCtrl.text.trim(),
        technologies: _technologiesCtrl.text.trim().isEmpty
            ? null
            : _technologiesCtrl.text.trim(),
        userProfileId: profileId,
      );

      final repo = ref.read(cvRepositoryProvider);
      if (widget.isEditing) {
        await repo.updatePortfolio(
          widget.portfolioId!,
          request,
          _pickedFile,
          bytes: _pickedFileBytes,
          fileName: _pickedFileName,
        );
      } else {
        await repo.savePortfolio(
          request,
          _pickedFile,
          bytes: _pickedFileBytes,
          fileName: _pickedFileName,
        );
      }

      if (!mounted) return;
      Navigator.of(context).pop(true); // true = list should refresh
    } catch (e) {
      setState(() => _errorMessage = e.toString());
    } finally {
      if (mounted) setState(() => _saving = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.isEditing ? 'Edit Portfolio' : 'Add Portfolio'),
      ),
      body: _loading
          ? const Center(child: CircularProgressIndicator())
          : SingleChildScrollView(
              padding: const EdgeInsets.all(16),
              child: Form(
                key: _formKey,
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    if (_errorMessage != null)
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
                            _errorMessage!,
                            style: const TextStyle(color: Colors.red),
                          ),
                        ),
                      ),

                    TextFormField(
                      controller: _titleCtrl,
                      decoration: const InputDecoration(
                        labelText: 'Project Title',
                        prefixIcon: Icon(Icons.folder_outlined),
                      ),
                      validator: (v) => (v == null || v.trim().isEmpty)
                          ? 'Project title is required.'
                          : null,
                    ),
                    const SizedBox(height: 14),

                    TextFormField(
                      controller: _descriptionCtrl,
                      maxLines: 4,
                      decoration: const InputDecoration(
                        labelText: 'Description',
                        alignLabelWithHint: true,
                        prefixIcon: Icon(Icons.description_outlined),
                      ),
                    ),
                    const SizedBox(height: 14),

                    TextFormField(
                      controller: _projectUrlCtrl,
                      keyboardType: TextInputType.url,
                      decoration: const InputDecoration(
                        labelText: 'Project URL',
                        hintText: 'https://github.com/you/project',
                        prefixIcon: Icon(Icons.link_outlined),
                      ),
                    ),
                    const SizedBox(height: 14),

                    TextFormField(
                      controller: _technologiesCtrl,
                      decoration: const InputDecoration(
                        labelText: 'Technologies',
                        hintText: 'e.g. Flutter, Spring Boot, MySQL',
                        prefixIcon: Icon(Icons.build_outlined),
                      ),
                    ),
                    const SizedBox(height: 20),

                    const Text(
                      'Project File',
                      style: TextStyle(
                        fontSize: 15,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    const SizedBox(height: 10),
                    _buildFileSection(),
                    const SizedBox(height: 28),

                    FilledButton(
                      onPressed: _saving ? null : _save,
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
                          : Text(
                              widget.isEditing
                                  ? 'Save Changes'
                                  : 'Add Portfolio',
                            ),
                    ),
                    const SizedBox(height: 16),
                  ],
                ),
              ),
            ),
    );
  }

  Widget _buildFileSection() {
    if (_pickedFile != null || _pickedFileBytes != null) {
      return Container(
        padding: const EdgeInsets.all(12),
        decoration: BoxDecoration(
          color: Colors.blue.shade50,
          borderRadius: BorderRadius.circular(8),
        ),
        child: Row(
          children: [
            const Icon(Icons.insert_drive_file_outlined, color: Colors.blue),
            const SizedBox(width: 10),
            Expanded(
              child: Text(
                _pickedFileName ?? 'Selected file',
                overflow: TextOverflow.ellipsis,
              ),
            ),
            IconButton(
              icon: const Icon(Icons.close, size: 20),
              onPressed: () => setState(() {
                _pickedFile = null;
                _pickedFileBytes = null;
                _pickedFileName = null;
              }),
            ),
          ],
        ),
      );
    }

    if (_existingFileUrl != null) {
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
                  child: Text(
                    _existingFileUrl!,
                    overflow: TextOverflow.ellipsis,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 8),
            Wrap(
              spacing: 8,
              children: [
                OutlinedButton.icon(
                  onPressed: _viewFile,
                  icon: const Icon(Icons.visibility_outlined, size: 18),
                  label: const Text('View'),
                ),
                OutlinedButton.icon(
                  onPressed: _pickProjectFile,
                  icon: const Icon(Icons.upload_file, size: 18),
                  label: const Text('Replace'),
                ),
                OutlinedButton.icon(
                  onPressed: _removingFile ? null : _removeExistingFile,
                  style: OutlinedButton.styleFrom(foregroundColor: Colors.red),
                  icon: _removingFile
                      ? const SizedBox(
                          height: 14,
                          width: 14,
                          child: CircularProgressIndicator(strokeWidth: 2),
                        )
                      : const Icon(Icons.delete_outline, size: 18),
                  label: const Text('Remove'),
                ),
              ],
            ),
          ],
        ),
      );
    }

    return OutlinedButton.icon(
      onPressed: _pickProjectFile,
      icon: const Icon(Icons.upload_file),
      label: const Text('Upload Project File (PDF/Image)'),
    );
  }
}
