import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/cvinformations/models/request/reference_request.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

/// Add/Edit Reference form — same shape as AddEditEducationScreen /
/// AddEditExperience / AddEditExtracurricular. [referenceId] null means
/// "add new"; non-null means "edit existing" and the form is pre-filled
/// via getReferenceById.
class AddEditReference extends ConsumerStatefulWidget {
  const AddEditReference({super.key, this.referenceId});

  final int? referenceId;

  bool get isEditing => referenceId != null;

  @override
  ConsumerState<AddEditReference> createState() => _AddEditReferenceState();
}

class _AddEditReferenceState extends ConsumerState<AddEditReference> {
  final _formKey = GlobalKey<FormState>();

  final _nameCtrl = TextEditingController();
  final _organizationCtrl = TextEditingController();
  final _designationCtrl = TextEditingController();
  final _phoneCtrl = TextEditingController();
  final _emailCtrl = TextEditingController();
  final _addressCtrl = TextEditingController();
  final _relationCtrl = TextEditingController();

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
      final reference = await ref
          .read(cvRepositoryProvider)
          .getReferenceById(widget.referenceId!);

      _nameCtrl.text = reference.name ?? '';
      _organizationCtrl.text = reference.organization ?? '';
      _designationCtrl.text = reference.designation ?? '';
      _phoneCtrl.text = reference.phone ?? '';
      _emailCtrl.text = reference.email ?? '';
      _addressCtrl.text = reference.address ?? '';
      _relationCtrl.text = reference.relation ?? '';
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
    _organizationCtrl.dispose();
    _designationCtrl.dispose();
    _phoneCtrl.dispose();
    _emailCtrl.dispose();
    _addressCtrl.dispose();
    _relationCtrl.dispose();
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
      final request = ReferenceRequestDTO(
        name: _nameCtrl.text.trim().isEmpty ? null : _nameCtrl.text.trim(),
        organization: _organizationCtrl.text.trim().isEmpty
            ? null
            : _organizationCtrl.text.trim(),
        designation: _designationCtrl.text.trim().isEmpty
            ? null
            : _designationCtrl.text.trim(),
        phone: _phoneCtrl.text.trim().isEmpty ? null : _phoneCtrl.text.trim(),
        email: _emailCtrl.text.trim().isEmpty ? null : _emailCtrl.text.trim(),
        address:
        _addressCtrl.text.trim().isEmpty ? null : _addressCtrl.text.trim(),
        relation: _relationCtrl.text.trim().isEmpty
            ? null
            : _relationCtrl.text.trim(),
        userProfileId: profileId,
      );

      final repo = ref.read(cvRepositoryProvider);
      if (widget.isEditing) {
        await repo.updateReference(widget.referenceId!, request);
      } else {
        await repo.saveReference(request);
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
        title: Text(widget.isEditing ? 'Edit Reference' : 'Add Reference'),
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
                  labelText: 'Name',
                  prefixIcon: Icon(Icons.person_outline),
                ),
                validator: (v) => (v == null || v.trim().isEmpty)
                    ? 'Name is required.'
                    : null,
              ),
              const SizedBox(height: 14),

              TextFormField(
                controller: _designationCtrl,
                decoration: const InputDecoration(
                  labelText: 'Designation',
                  prefixIcon: Icon(Icons.badge_outlined),
                ),
              ),
              const SizedBox(height: 14),

              TextFormField(
                controller: _organizationCtrl,
                decoration: const InputDecoration(
                  labelText: 'Organization',
                  prefixIcon: Icon(Icons.apartment_outlined),
                ),
              ),
              const SizedBox(height: 14),

              TextFormField(
                controller: _relationCtrl,
                decoration: const InputDecoration(
                  labelText: 'Relation',
                  hintText: 'e.g. Former Manager',
                  prefixIcon: Icon(Icons.groups_outlined),
                ),
              ),
              const SizedBox(height: 14),

              TextFormField(
                controller: _phoneCtrl,
                keyboardType: TextInputType.phone,
                decoration: const InputDecoration(
                  labelText: 'Phone',
                  prefixIcon: Icon(Icons.phone_outlined),
                ),
              ),
              const SizedBox(height: 14),

              TextFormField(
                controller: _emailCtrl,
                keyboardType: TextInputType.emailAddress,
                decoration: const InputDecoration(
                  labelText: 'Email',
                  prefixIcon: Icon(Icons.email_outlined),
                ),
                validator: (v) {
                  if (v == null || v.trim().isEmpty) return null;
                  if (!v.contains('@')) return 'Enter a valid email.';
                  return null;
                },
              ),
              const SizedBox(height: 14),

              TextFormField(
                controller: _addressCtrl,
                maxLines: 2,
                decoration: const InputDecoration(
                  labelText: 'Address',
                  alignLabelWithHint: true,
                  prefixIcon: Icon(Icons.location_on_outlined),
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
                    : 'Add Reference'),
              ),
              const SizedBox(height: 16),
            ],
          ),
        ),
      ),
    );
  }
}