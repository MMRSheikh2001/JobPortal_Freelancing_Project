import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/cvinformations/models/request/experience_request.dart';
import 'package:work_bridge_flutter/enums/employment_type.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

/// Add/Edit Experience form — same shape as AddEditEducationScreen.
/// [experienceId] null means "add new"; non-null means "edit existing"
/// and the form is pre-filled via getExperienceById.
class AddEditExperience extends ConsumerStatefulWidget {
  const AddEditExperience({super.key, this.experienceId});

  final int? experienceId;

  bool get isEditing => experienceId != null;

  @override
  ConsumerState<AddEditExperience> createState() => _AddEditExperienceState();
}

class _AddEditExperienceState extends ConsumerState<AddEditExperience> {
  final _formKey = GlobalKey<FormState>();

  final _companyNameCtrl = TextEditingController();
  final _positionCtrl = TextEditingController();
  final _responsibilitiesCtrl = TextEditingController();
  final _achievementsCtrl = TextEditingController();

  EmploymentType? _employmentType;
  DateTime? _startDate;
  DateTime? _endDate;
  bool _currentlyWorking = false;

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
      final experience = await ref
          .read(cvRepositoryProvider)
          .getExperienceById(widget.experienceId!);

      _companyNameCtrl.text = experience.companyName ?? '';
      _positionCtrl.text = experience.position ?? '';
      _responsibilitiesCtrl.text = experience.responsibilities ?? '';
      _achievementsCtrl.text = experience.achievements ?? '';
      _employmentType = experience.employmentType;
      _startDate = experience.startDate;
      _endDate = experience.endDate;
      _currentlyWorking = experience.currentlyWorking ?? false;
    } catch (e) {
      if (!mounted) return;
      setState(() => _errorMessage = 'Failed to load: $e');
    } finally {
      if (mounted) setState(() => _loading = false);
    }
  }

  @override
  void dispose() {
    _companyNameCtrl.dispose();
    _positionCtrl.dispose();
    _responsibilitiesCtrl.dispose();
    _achievementsCtrl.dispose();
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

  void _toggleCurrentlyWorking(bool value) {
    setState(() {
      _currentlyWorking = value;
      if (value) _endDate = null;
    });
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
      final request = ExperienceRequestDTO(
        companyName: _companyNameCtrl.text.trim().isEmpty
            ? null
            : _companyNameCtrl.text.trim(),
        position: _positionCtrl.text.trim().isEmpty
            ? null
            : _positionCtrl.text.trim(),
        responsibilities: _responsibilitiesCtrl.text.trim().isEmpty
            ? null
            : _responsibilitiesCtrl.text.trim(),
        achievements: _achievementsCtrl.text.trim().isEmpty
            ? null
            : _achievementsCtrl.text.trim(),
        startDate: _startDate,
        endDate: _currentlyWorking ? null : _endDate,
        employmentType: _employmentType,
        userProfileId: profileId,
      );

      final repo = ref.read(cvRepositoryProvider);
      if (widget.isEditing) {
        await repo.updateExperience(widget.experienceId!, request);
      } else {
        await repo.saveExperience(request);
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
        title: Text(widget.isEditing ? 'Edit Experience' : 'Add Experience'),
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
                      controller: _companyNameCtrl,
                      decoration: const InputDecoration(
                        labelText: 'Company Name',
                        prefixIcon: Icon(Icons.apartment_outlined),
                      ),
                      validator: (v) => (v == null || v.trim().isEmpty)
                          ? 'Company name is required.'
                          : null,
                    ),
                    const SizedBox(height: 14),

                    TextFormField(
                      controller: _positionCtrl,
                      decoration: const InputDecoration(
                        labelText: 'Position',
                        prefixIcon: Icon(Icons.work_outline),
                      ),
                      validator: (v) => (v == null || v.trim().isEmpty)
                          ? 'Position is required.'
                          : null,
                    ),
                    const SizedBox(height: 14),

                    DropdownButtonFormField<EmploymentType>(
                      value: _employmentType,
                      decoration: const InputDecoration(
                        labelText: 'Employment Type',
                        prefixIcon: Icon(Icons.badge_outlined),
                      ),
                      items: EmploymentType.values
                          .map(
                            (e) => DropdownMenuItem(
                              value: e,
                              child: Text(_employmentTypeLabel(e)),
                            ),
                          )
                          .toList(),
                      onChanged: (v) => setState(() => _employmentType = v),
                    ),
                    const SizedBox(height: 14),

                    TextFormField(
                      controller: _responsibilitiesCtrl,
                      maxLines: 3,
                      decoration: const InputDecoration(
                        labelText: 'Responsibilities',
                        alignLabelWithHint: true,
                        prefixIcon: Icon(Icons.checklist_outlined),
                      ),
                    ),
                    const SizedBox(height: 14),

                    TextFormField(
                      controller: _achievementsCtrl,
                      maxLines: 3,
                      decoration: const InputDecoration(
                        labelText: 'Achievements',
                        alignLabelWithHint: true,
                        prefixIcon: Icon(Icons.emoji_events_outlined),
                      ),
                    ),
                    const SizedBox(height: 20),

                    const Text(
                      'Duration',
                      style: TextStyle(
                        fontSize: 15,
                        fontWeight: FontWeight.bold,
                      ),
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
                      onTap: _currentlyWorking
                          ? null
                          : () => _pickDate(isStart: false),
                      child: InputDecorator(
                        decoration: InputDecoration(
                          labelText: 'End Date',
                          prefixIcon: const Icon(Icons.calendar_today_outlined),
                          enabled: !_currentlyWorking,
                        ),
                        child: Text(
                          _endDate != null
                              ? _formatDate(_endDate!)
                              : (_currentlyWorking ? 'N/A' : 'Select date'),
                          style: TextStyle(
                            color: (_endDate != null && !_currentlyWorking)
                                ? null
                                : Colors.black45,
                          ),
                        ),
                      ),
                    ),
                    const SizedBox(height: 8),
                    InkWell(
                      onTap: () => _toggleCurrentlyWorking(!_currentlyWorking),
                      child: Row(
                        children: [
                          Checkbox(
                            value: _currentlyWorking,
                            onChanged: (v) =>
                                _toggleCurrentlyWorking(v ?? false),
                          ),
                          const Text('I currently work here'),
                        ],
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
                          : Text(
                              widget.isEditing
                                  ? 'Save Changes'
                                  : 'Add Experience',
                            ),
                    ),
                    const SizedBox(height: 16),
                  ],
                ),
              ),
            ),
    );
  }

  String _formatDate(DateTime d) => '${d.day}/${d.month}/${d.year}';

  String _employmentTypeLabel(EmploymentType e) => switch (e) {
    EmploymentType.fullTime => 'Full Time',
    EmploymentType.partTime => 'Part Time',
    EmploymentType.contract => 'Contract',
    EmploymentType.internship => 'Internship',
    EmploymentType.freelance => 'Freelance',
  };
}
