import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/cvinformations/models/request/extracurricular_request.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

/// Add/Edit Extracurricular form — same shape as AddEditEducationScreen /
/// AddEditExperience. [extracurricularId] null means "add new"; non-null
/// means "edit existing" and the form is pre-filled via
/// getExtracurricularById.
class AddEditExtracurricular extends ConsumerStatefulWidget {
  const AddEditExtracurricular({super.key, this.extracurricularId});

  final int? extracurricularId;

  bool get isEditing => extracurricularId != null;

  @override
  ConsumerState<AddEditExtracurricular> createState() =>
      _AddEditExtracurricularState();
}

class _AddEditExtracurricularState
    extends ConsumerState<AddEditExtracurricular> {
  final _formKey = GlobalKey<FormState>();

  final _titleCtrl = TextEditingController();
  final _organizationCtrl = TextEditingController();
  final _roleCtrl = TextEditingController();
  final _descriptionCtrl = TextEditingController();

  bool _loading = false;
  bool _saving = false;
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
      final extracurricular = await ref
          .read(cvRepositoryProvider)
          .getExtracurricularById(widget.extracurricularId!);

      _titleCtrl.text = extracurricular.title ?? '';
      _organizationCtrl.text = extracurricular.organization ?? '';
      _roleCtrl.text = extracurricular.role ?? '';
      _descriptionCtrl.text = extracurricular.description ?? '';
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
    _organizationCtrl.dispose();
    _roleCtrl.dispose();
    _descriptionCtrl.dispose();
    super.dispose();
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
      final request = ExtracurricularRequestDTO(
        title: _titleCtrl.text.trim().isEmpty ? null : _titleCtrl.text.trim(),
        organization: _organizationCtrl.text.trim().isEmpty
            ? null
            : _organizationCtrl.text.trim(),
        role: _roleCtrl.text.trim().isEmpty ? null : _roleCtrl.text.trim(),
        description: _descriptionCtrl.text.trim().isEmpty
            ? null
            : _descriptionCtrl.text.trim(),
        userProfileId: profileId,
      );

      final repo = ref.read(cvRepositoryProvider);
      if (widget.isEditing) {
        await repo.updateExtracurricular(widget.extracurricularId!, request);
      } else {
        await repo.saveExtracurricular(request);
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
        title: Text(
          widget.isEditing ? 'Edit Extracurricular' : 'Add Extracurricular',
        ),
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
                  labelText: 'Title',
                  hintText: 'e.g. Debate Club Captain',
                  prefixIcon: Icon(Icons.star_outline),
                ),
                validator: (v) => (v == null || v.trim().isEmpty)
                    ? 'Title is required.'
                    : null,
              ),
              const SizedBox(height: 14),

              TextFormField(
                controller: _organizationCtrl,
                decoration: const InputDecoration(
                  labelText: 'Organization',
                  prefixIcon: Icon(Icons.groups_outlined),
                ),
              ),
              const SizedBox(height: 14),

              TextFormField(
                controller: _roleCtrl,
                decoration: const InputDecoration(
                  labelText: 'Role',
                  prefixIcon: Icon(Icons.badge_outlined),
                ),
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
                    : 'Add Extracurricular'),
              ),
              const SizedBox(height: 16),
            ],
          ),
        ),
      ),
    );
  }
}