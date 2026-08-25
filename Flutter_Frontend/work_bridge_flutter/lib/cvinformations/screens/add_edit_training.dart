import 'dart:io';

import 'package:file_picker/file_picker.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/cvinformations/models/request/training_request.dart';
import 'package:work_bridge_flutter/enums/training_type.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

/// Add/Edit Training form — same shape as AddEditEducationScreen /
/// AddEditExperience / AddEditReference, plus a certificate file
/// upload/view/remove section. [trainingId] null means "add new";
/// non-null means "edit existing" and the form is pre-filled via
/// getTrainingById.
class AddEditTraining extends ConsumerStatefulWidget {
  const AddEditTraining({super.key, this.trainingId});

  final int? trainingId;

  bool get isEditing => trainingId != null;

  @override
  ConsumerState<AddEditTraining> createState() => _AddEditTrainingState();
}

class _AddEditTrainingState extends ConsumerState<AddEditTraining> {
  final _formKey = GlobalKey<FormState>();

  final _nameCtrl = TextEditingController();
  final _descriptionCtrl = TextEditingController();
  final _institutionCtrl = TextEditingController();
  final _durationCtrl = TextEditingController();
  final _certificateVerificationUrlCtrl = TextEditingController();
  final _certificateIdCtrl = TextEditingController();

  TrainingType? _trainingType;
  DateTime? _startDate;
  DateTime? _endDate;

  // Certificate file: three possible states —
  // 1. _pickedFile / _pickedFileBytes set: a new file was just picked, not uploaded yet.
  // 2. _existingCertificateFile set (and _pickedFile null): a file is
  //    already saved on the backend; "View" opens it, "Replace" picks a
  //    new one, "Remove" deletes it via deleteTrainingFile.
  // 3. Both null: no certificate yet.
  File? _pickedFile;
  Uint8List? _pickedFileBytes;
  String? _pickedFileName;
  String? _existingCertificateFile;

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
      final training = await ref
          .read(cvRepositoryProvider)
          .getTrainingById(widget.trainingId!);

      _nameCtrl.text = training.name ?? '';
      _descriptionCtrl.text = training.description ?? '';
      _institutionCtrl.text = training.institution ?? '';
      _durationCtrl.text = training.duration ?? '';
      _certificateVerificationUrlCtrl.text =
          training.certificateVerificationUrl ?? '';
      _certificateIdCtrl.text = training.certificateId ?? '';
      _trainingType = training.trainingType;
      _startDate = training.startDate;
      _endDate = training.endDate;
      _existingCertificateFile = training.certificateFile;
    } catch (e) {
      if (!mounted) return;
      setState(() => _errorMessage = 'Failed to load: $e');
    } finally {
      if (mounted) setState(() => _loading = false);
    }
  }

  @override
  void dispose() {
    _nameCtrl.dispose();
    _descriptionCtrl.dispose();
    _institutionCtrl.dispose();
    _durationCtrl.dispose();
    _certificateVerificationUrlCtrl.dispose();
    _certificateIdCtrl.dispose();
    super.dispose();
  }

  Future<void> _pickDate({required bool isStart}) async {
    final now = DateTime.now();
    final initial = (isStart ? _startDate : _endDate) ?? now;
    final picked = await showDatePicker(
      context: context,
      initialDate: initial,
      firstDate: DateTime(1970),
      lastDate: DateTime(now.year + 10),
    );
    if (picked != null) {
      setState(() {
        if (isStart) {
          _startDate = picked;
        } else {
          _endDate = picked;
        }
      });
    }
  }

  Future<void> _pickCertificateFile() async {
    try {
      final file = await FilePicker.pickFile(
        type: FileType.custom,
        allowedExtensions: ['pdf', 'jpg', 'jpeg', 'png'],
      );

      // 1. User canceled the picker
      if (file == null) {
        return;
      }

      // 2. Validate Extension (Fallback safety check)
      final allowedExts = ['pdf', 'jpg', 'jpeg', 'png'];
      final name = file.name.toLowerCase();
      final extension = name.contains('.') ? name.split('.').last : null;

      if (extension == null || !allowedExts.contains(extension)) {
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(
                content:
                    Text('Invalid file type. Please upload a PDF, JPG, or PNG.')),
          );
        }
        return;
      }

      // 4. Update State Cross-Platform
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
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Error picking file: $e')),
        );
      }
    }
  }

  Future<void> _viewCertificate() async {
    if (_existingCertificateFile == null) return;
    final url = Uri.parse(
      '${ApiConstants.trainingFileUrl}$_existingCertificateFile',
    );
    final opened = await launchUrl(url, mode: LaunchMode.externalApplication);
    if (!opened && mounted) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Could not open certificate.')),
      );
    }
  }

  Future<void> _removeExistingCertificate() async {
    if (widget.trainingId == null) return;

    final confirmed = await showDialog<bool>(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Remove certificate?'),
        content: const Text(
          'This deletes the uploaded certificate file from your training '
              'record. This cannot be undone.',
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
          .deleteTrainingFile(widget.trainingId!);
      if (!mounted) return;
      setState(() => _existingCertificateFile = null);
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Could not remove certificate: $e')),
      );
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
      final request = TrainingRequestDTO(
        name: _nameCtrl.text.trim().isEmpty ? null : _nameCtrl.text.trim(),
        description: _descriptionCtrl.text.trim().isEmpty
            ? null
            : _descriptionCtrl.text.trim(),
        institution: _institutionCtrl.text.trim().isEmpty
            ? null
            : _institutionCtrl.text.trim(),
        startDate: _startDate,
        endDate: _endDate,
        duration:
        _durationCtrl.text.trim().isEmpty ? null : _durationCtrl.text.trim(),
        certificateVerificationUrl:
        _certificateVerificationUrlCtrl.text.trim().isEmpty
            ? null
            : _certificateVerificationUrlCtrl.text.trim(),
        certificateId: _certificateIdCtrl.text.trim().isEmpty
            ? null
            : _certificateIdCtrl.text.trim(),
        trainingType: _trainingType,
        userProfileId: profileId,
      );

      final repo = ref.read(cvRepositoryProvider);
      if (widget.isEditing) {
        await repo.updateTraining(
          widget.trainingId!,
          request,
          _pickedFile,
          bytes: _pickedFileBytes,
          fileName: _pickedFileName,
        );
      } else {
        await repo.saveTraining(
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
        title: Text(widget.isEditing ? 'Edit Training' : 'Add Training'),
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
                controller: _nameCtrl,
                decoration: const InputDecoration(
                  labelText: 'Training Name',
                  prefixIcon: Icon(Icons.school_outlined),
                ),
                validator: (v) => (v == null || v.trim().isEmpty)
                    ? 'Training name is required.'
                    : null,
              ),
              const SizedBox(height: 14),

              TextFormField(
                controller: _institutionCtrl,
                decoration: const InputDecoration(
                  labelText: 'Institution',
                  prefixIcon: Icon(Icons.account_balance_outlined),
                ),
              ),
              const SizedBox(height: 14),

              DropdownButtonFormField<TrainingType>(
                initialValue: _trainingType,
                decoration: const InputDecoration(
                  labelText: 'Training Type',
                  prefixIcon: Icon(Icons.category_outlined),
                ),
                items: TrainingType.values
                    .map((t) => DropdownMenuItem(
                  value: t,
                  child: Text(t.value),
                ))
                    .toList(),
                onChanged: (v) => setState(() => _trainingType = v),
              ),
              const SizedBox(height: 14),

              TextFormField(
                controller: _descriptionCtrl,
                maxLines: 3,
                decoration: const InputDecoration(
                  labelText: 'Description',
                  alignLabelWithHint: true,
                  prefixIcon: Icon(Icons.description_outlined),
                ),
              ),
              const SizedBox(height: 14),

              TextFormField(
                controller: _durationCtrl,
                decoration: const InputDecoration(
                  labelText: 'Duration',
                  hintText: 'e.g. 3 months',
                  prefixIcon: Icon(Icons.timelapse_outlined),
                ),
              ),
              const SizedBox(height: 20),

              const Text(
                'Duration Dates',
                style: TextStyle(fontSize: 15, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 10),
              InkWell(
                onTap: () => _pickDate(isStart: true),
                child: InputDecorator(
                  decoration: const InputDecoration(
                    labelText: 'Start Date',
                    prefixIcon: Icon(Icons.calendar_today_outlined),
                  ),
                  child: Text(
                    _startDate != null
                        ? _formatDate(_startDate!)
                        : 'Select date',
                    style: TextStyle(
                      color: _startDate != null ? null : Colors.black45,
                    ),
                  ),
                ),
              ),
              const SizedBox(height: 14),
              InkWell(
                onTap: () => _pickDate(isStart: false),
                child: InputDecorator(
                  decoration: const InputDecoration(
                    labelText: 'End Date',
                    prefixIcon: Icon(Icons.calendar_today_outlined),
                  ),
                  child: Text(
                    _endDate != null
                        ? _formatDate(_endDate!)
                        : 'Select date',
                    style: TextStyle(
                      color: _endDate != null ? null : Colors.black45,
                    ),
                  ),
                ),
              ),
              const SizedBox(height: 20),

              const Text(
                'Certificate',
                style: TextStyle(fontSize: 15, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 10),
              TextFormField(
                controller: _certificateIdCtrl,
                decoration: const InputDecoration(
                  labelText: 'Certificate ID',
                  prefixIcon: Icon(Icons.confirmation_number_outlined),
                ),
              ),
              const SizedBox(height: 14),
              TextFormField(
                controller: _certificateVerificationUrlCtrl,
                keyboardType: TextInputType.url,
                decoration: const InputDecoration(
                  labelText: 'Certificate Verification URL',
                  prefixIcon: Icon(Icons.verified_outlined),
                ),
              ),
              const SizedBox(height: 14),

              // ── Certificate file ───────────────────
              _buildCertificateFileSection(),
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
                    : Text(widget.isEditing
                    ? 'Save Changes'
                    : 'Add Training'),
              ),
              const SizedBox(height: 16),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildCertificateFileSection() {
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

    if (_existingCertificateFile != null) {
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
                    _existingCertificateFile!,
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
                  onPressed: _viewCertificate,
                  icon: const Icon(Icons.visibility_outlined, size: 18),
                  label: const Text('View'),
                ),
                OutlinedButton.icon(
                  onPressed: _pickCertificateFile,
                  icon: const Icon(Icons.upload_file, size: 18),
                  label: const Text('Replace'),
                ),
                OutlinedButton.icon(
                  onPressed: _removingFile ? null : _removeExistingCertificate,
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
      onPressed: _pickCertificateFile,
      icon: const Icon(Icons.upload_file),
      label: const Text('Upload Certificate (PDF/Image)'),
    );
  }

  String _formatDate(DateTime d) => '${d.day}/${d.month}/${d.year}';
}