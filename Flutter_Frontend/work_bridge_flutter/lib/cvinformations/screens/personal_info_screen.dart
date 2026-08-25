import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:image_picker/image_picker.dart';
import 'package:work_bridge_flutter/cvinformations/models/request/user_profile_request.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/user_profile_response.dart';
import 'package:work_bridge_flutter/cvinformations/providers/user_profile_provider.dart';
import 'package:work_bridge_flutter/enums/gender_type.dart';
import 'package:work_bridge_flutter/enums/job_type.dart';
import 'package:work_bridge_flutter/enums/work_place_type.dart';
import 'package:work_bridge_flutter/masterdata/widget/location_cascade.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

/// Job seeker's "Personal Information" edit screen — mirrors
/// WorkBridgeAndroid's UserProfileActivity. Same structure/conventions as
/// CompanyProfileScreen: seed-once-from-response, multipart save with
/// optional image, required-address guard (backend crashes on a null
/// police station id with no server-side default), and locationId
/// captured straight from the save response so a second save in the same
/// session updates the existing Address row instead of creating another.
class PersonalInfoScreen extends ConsumerStatefulWidget {
  const PersonalInfoScreen({super.key});

  @override
  ConsumerState<PersonalInfoScreen> createState() => _PersonalInfoScreenState();
}

class _PersonalInfoScreenState extends ConsumerState<PersonalInfoScreen> {
  final _formKey = GlobalKey<FormState>();

  final _nameCtrl = TextEditingController();
  final _phoneCtrl = TextEditingController();
  final _headlineCtrl = TextEditingController();
  final _professionalSummaryCtrl = TextEditingController();
  final _bioCtrl = TextEditingController();
  final _nationalityCtrl = TextEditingController();
  final _religionCtrl = TextEditingController();
  final _maritalStatusCtrl = TextEditingController();
  final _fatherNameCtrl = TextEditingController();
  final _motherNameCtrl = TextEditingController();
  final _nidNumberCtrl = TextEditingController();
  final _passportNumberCtrl = TextEditingController();
  final _githubLinkCtrl = TextEditingController();
  final _linkedinLinkCtrl = TextEditingController();
  final _portfolioWebsiteCtrl = TextEditingController();
  final _expectedSalaryCtrl = TextEditingController();
  final _currentSalaryCtrl = TextEditingController();
  final _careerObjectiveCtrl = TextEditingController();
  final _freelancerTitleCtrl = TextEditingController();

  final _presentDetailsCtrl = TextEditingController();
  final _presentPostCodeCtrl = TextEditingController();
  final _permanentDetailsCtrl = TextEditingController();
  final _permanentPostCodeCtrl = TextEditingController();

  DateTime? _dateOfBirth;
  GenderType? _gender;
  JobType? _preferredJobType;
  WorkPlaceType? _preferredWorkplace;

  LocationSelection _presentLocation = LocationSelection.empty;
  LocationSelection _permanentLocation = LocationSelection.empty;

  // Address row ids on the backend — null on first save, then captured
  // from the save response so a second save updates the same Address
  // row instead of creating a new one every time. See CompanyProfileScreen
  // for the full explanation of why this matters.
  int? _presentAddressId;
  int? _permanentAddressId;

  bool _sameAsPresent = false;

  File? _pickedImage;
  String? _existingImageUrl;

  bool _seeded = false;
  bool _saving = false;
  String? _errorMessage;

  void _seedFrom(UserProfileResponseDTO profile) {
    if (_seeded) return; // don't overwrite what the user is actively typing
    _seeded = true;

    _nameCtrl.text = profile.name ?? '';
    _phoneCtrl.text = profile.phone ?? '';
    _headlineCtrl.text = profile.headline ?? '';
    _professionalSummaryCtrl.text = profile.professionalSummary ?? '';
    _bioCtrl.text = profile.bio ?? '';
    _nationalityCtrl.text = profile.nationality ?? '';
    _religionCtrl.text = profile.religion ?? '';
    _maritalStatusCtrl.text = profile.maritalStatus ?? '';
    _fatherNameCtrl.text = profile.fatherName ?? '';
    _motherNameCtrl.text = profile.motherName ?? '';
    _nidNumberCtrl.text = profile.nidNumber ?? '';
    _passportNumberCtrl.text = profile.passportNumber ?? '';
    _githubLinkCtrl.text = profile.githubLink ?? '';
    _linkedinLinkCtrl.text = profile.linkedinLink ?? '';
    _portfolioWebsiteCtrl.text = profile.portfolioWebsite ?? '';
    _expectedSalaryCtrl.text = profile.expectedSalary?.toStringAsFixed(0) ?? '';
    _currentSalaryCtrl.text = profile.currentSalary?.toStringAsFixed(0) ?? '';
    _careerObjectiveCtrl.text = profile.careerObjective ?? '';
    _freelancerTitleCtrl.text = profile.freelancerTitle ?? '';

    _dateOfBirth = profile.dateOfBirth;
    _gender = profile.gender;
    _preferredJobType = profile.preferredJobType;
    _preferredWorkplace = profile.preferredWorkplace;

    _existingImageUrl = (profile.image != null && profile.image!.isNotEmpty)
        ? '${ApiConstants.userProfileImageUrl}${profile.image}'
        : null;

    _presentAddressId = profile.presentAddressId;
    _presentDetailsCtrl.text = profile.presentAddressDetails ?? '';
    _presentPostCodeCtrl.text = profile.presentAddressPostCode ?? '';
    _presentLocation = LocationSelection(
      countryId: profile.presentCountryId,
      countryName: profile.presentCountryName,
      divisionId: profile.presentDivisionId,
      divisionName: profile.presentDivisionName,
      districtId: profile.presentDistrictId,
      districtName: profile.presentDistrictName,
      policeStationId: profile.presentPoliceStationId,
      policeStationName: profile.presentPoliceStationName,
    );

    _permanentAddressId = profile.permanentAddressId;
    _permanentDetailsCtrl.text = profile.permanentAddressDetails ?? '';
    _permanentPostCodeCtrl.text = profile.permanentAddressPostCode ?? '';
    _permanentLocation = LocationSelection(
      countryId: profile.permanentCountryId,
      countryName: profile.permanentCountryName,
      divisionId: profile.permanentDivisionId,
      divisionName: profile.permanentDivisionName,
      districtId: profile.permanentDistrictId,
      districtName: profile.permanentDistrictName,
      policeStationId: profile.permanentPoliceStationId,
      policeStationName: profile.permanentPoliceStationName,
    );

    // Infer the checkbox from existing data: if present and permanent
    // already point at the same police station with matching details,
    // treat it as "same as present" so the toggle reflects reality
    // instead of always starting unticked.
    _sameAsPresent =
        profile.presentPoliceStationId != null &&
        profile.presentPoliceStationId == profile.permanentPoliceStationId &&
        profile.presentAddressDetails == profile.permanentAddressDetails &&
        profile.presentAddressPostCode == profile.permanentAddressPostCode;
  }

  @override
  void dispose() {
    _nameCtrl.dispose();
    _phoneCtrl.dispose();
    _headlineCtrl.dispose();
    _professionalSummaryCtrl.dispose();
    _bioCtrl.dispose();
    _nationalityCtrl.dispose();
    _religionCtrl.dispose();
    _maritalStatusCtrl.dispose();
    _fatherNameCtrl.dispose();
    _motherNameCtrl.dispose();
    _nidNumberCtrl.dispose();
    _passportNumberCtrl.dispose();
    _githubLinkCtrl.dispose();
    _linkedinLinkCtrl.dispose();
    _portfolioWebsiteCtrl.dispose();
    _expectedSalaryCtrl.dispose();
    _currentSalaryCtrl.dispose();
    _careerObjectiveCtrl.dispose();
    _freelancerTitleCtrl.dispose();
    _presentDetailsCtrl.dispose();
    _presentPostCodeCtrl.dispose();
    _permanentDetailsCtrl.dispose();
    _permanentPostCodeCtrl.dispose();
    super.dispose();
  }

  Future<void> _pickImage() async {
    final picked = await ImagePicker().pickImage(
      source: ImageSource.gallery,
      maxWidth: 1024,
      imageQuality: 85,
    );
    if (picked != null) {
      setState(() => _pickedImage = File(picked.path));
    }
  }

  Future<void> _pickDateOfBirth() async {
    final now = DateTime.now();
    final picked = await showDatePicker(
      context: context,
      initialDate: _dateOfBirth ?? DateTime(now.year - 20),
      firstDate: DateTime(now.year - 100),
      lastDate: now,
    );
    if (picked != null) {
      setState(() => _dateOfBirth = picked);
    }
  }

  void _onPresentLocationChanged(LocationSelection loc) {
    setState(() {
      _presentLocation = loc;
      if (_sameAsPresent) {
        _permanentLocation = loc;
      }
    });
  }

  void _toggleSameAsPresent(bool value) {
    setState(() {
      _sameAsPresent = value;
      if (value) {
        _permanentLocation = _presentLocation;
        _permanentDetailsCtrl.text = _presentDetailsCtrl.text;
        _permanentPostCodeCtrl.text = _presentPostCodeCtrl.text;
      }
    });
  }

  Future<void> _save(int profileId, int userId) async {
    if (!_formKey.currentState!.validate()) return;

    // The backend crashes (500) if either address's police station id is
    // null on save, with no server-side default/skip for it — both
    // present AND permanent address are enforced as required here.
    if (_presentLocation.policeStationId == null) {
      setState(() {
        _errorMessage =
            'Please complete your Present Address (down to Police Station) '
            'before saving.';
      });
      return;
    }
    if (!_sameAsPresent && _permanentLocation.policeStationId == null) {
      setState(() {
        _errorMessage =
            'Please complete your Permanent Address (down to Police '
            'Station), or tick "Same as present address".';
      });
      return;
    }

    setState(() {
      _saving = true;
      _errorMessage = null;
    });

    try {
      final permanentDetails = _sameAsPresent
          ? _presentDetailsCtrl.text.trim()
          : _permanentDetailsCtrl.text.trim();
      final permanentPostCode = _sameAsPresent
          ? _presentPostCodeCtrl.text.trim()
          : _permanentPostCodeCtrl.text.trim();
      final permanentLocation = _sameAsPresent
          ? _presentLocation
          : _permanentLocation;

      final request = UserProfileRequestDTO(
        userId: userId,
        name: _nameCtrl.text.trim().isEmpty ? null : _nameCtrl.text.trim(),
        phone: _phoneCtrl.text.trim().isEmpty ? null : _phoneCtrl.text.trim(),
        headline: _headlineCtrl.text.trim().isEmpty
            ? null
            : _headlineCtrl.text.trim(),
        professionalSummary: _professionalSummaryCtrl.text.trim().isEmpty
            ? null
            : _professionalSummaryCtrl.text.trim(),
        bio: _bioCtrl.text.trim().isEmpty ? null : _bioCtrl.text.trim(),
        dateOfBirth: _dateOfBirth,
        gender: _gender,
        nationality: _nationalityCtrl.text.trim().isEmpty
            ? null
            : _nationalityCtrl.text.trim(),
        religion: _religionCtrl.text.trim().isEmpty
            ? null
            : _religionCtrl.text.trim(),
        maritalStatus: _maritalStatusCtrl.text.trim().isEmpty
            ? null
            : _maritalStatusCtrl.text.trim(),
        fatherName: _fatherNameCtrl.text.trim().isEmpty
            ? null
            : _fatherNameCtrl.text.trim(),
        motherName: _motherNameCtrl.text.trim().isEmpty
            ? null
            : _motherNameCtrl.text.trim(),
        nidNumber: _nidNumberCtrl.text.trim().isEmpty
            ? null
            : _nidNumberCtrl.text.trim(),
        passportNumber: _passportNumberCtrl.text.trim().isEmpty
            ? null
            : _passportNumberCtrl.text.trim(),
        githubLink: _githubLinkCtrl.text.trim().isEmpty
            ? null
            : _githubLinkCtrl.text.trim(),
        linkedinLink: _linkedinLinkCtrl.text.trim().isEmpty
            ? null
            : _linkedinLinkCtrl.text.trim(),
        portfolioWebsite: _portfolioWebsiteCtrl.text.trim().isEmpty
            ? null
            : _portfolioWebsiteCtrl.text.trim(),
        expectedSalary: double.tryParse(_expectedSalaryCtrl.text.trim()),
        currentSalary: double.tryParse(_currentSalaryCtrl.text.trim()),
        preferredJobType: _preferredJobType,
        preferredWorkplace: _preferredWorkplace,
        careerObjective: _careerObjectiveCtrl.text.trim().isEmpty
            ? null
            : _careerObjectiveCtrl.text.trim(),
        freelancerTitle: _freelancerTitleCtrl.text.trim().isEmpty
            ? null
            : _freelancerTitleCtrl.text.trim(),
        presentAddressId: _presentAddressId,
        presentAddressDetails: _presentDetailsCtrl.text.trim().isEmpty
            ? null
            : _presentDetailsCtrl.text.trim(),
        presentAddressPostCode: _presentPostCodeCtrl.text.trim().isEmpty
            ? null
            : _presentPostCodeCtrl.text.trim(),
        presentAddressPoliceStationId: _presentLocation.policeStationId,
        // Null on first save (no permanent address yet) -> backend
        // creates a new Address row. Non-null after that -> updates the
        // same row. When "same as present" is ticked, we still send the
        // permanent-specific id (which may differ from the present one
        // if they were previously separate rows) so it keeps updating
        // its own row rather than colliding with the present address row.
        permanentAddressId: _permanentAddressId,
        permanentAddressDetails: permanentDetails.isEmpty
            ? null
            : permanentDetails,
        permanentAddressPostCode: permanentPostCode.isEmpty
            ? null
            : permanentPostCode,
        permanentAddressPoliceStationId: permanentLocation.policeStationId,
      );

      final updated = await ref
          .read(cvRepositoryProvider)
          .updateUserProfile(profileId, request, _pickedImage);

      // Capture what the backend actually persisted — most importantly
      // both Address ids it just created (if this was the first save),
      // so a second save in the same session updates those same rows
      // instead of creating new ones.
      _presentAddressId = updated.presentAddressId;
      _permanentAddressId = updated.permanentAddressId;
      _existingImageUrl = (updated.image != null && updated.image!.isNotEmpty)
          ? '${ApiConstants.userProfileImageUrl}${updated.image}'
          : null;
      _pickedImage = null; // now uploaded; stop showing the local file

      ref.invalidate(myUserProfileProvider);
      if (!mounted) return;

      ScaffoldMessenger.of(
        context,
      ).showSnackBar(const SnackBar(content: Text('Profile updated.')));
    } catch (e) {
      setState(() => _errorMessage = e.toString());
    } finally {
      if (mounted) setState(() => _saving = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    final profileAsync = ref.watch(myUserProfileProvider);

    return Scaffold(
      appBar: AppBar(title: const Text('Personal Information')),
      body: profileAsync.when(
        loading: () => const Center(child: CircularProgressIndicator()),
        error: (e, _) => Center(
          child: Padding(
            padding: const EdgeInsets.all(24),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                const Icon(Icons.error_outline, size: 40, color: Colors.red),
                const SizedBox(height: 12),
                Text('Failed to load profile: $e', textAlign: TextAlign.center),
                const SizedBox(height: 12),
                OutlinedButton(
                  onPressed: () => ref.invalidate(myUserProfileProvider),
                  child: const Text('Retry'),
                ),
              ],
            ),
          ),
        ),
        data: (profile) {
          if (profile?.id == null || profile?.userId == null) {
            // Shouldn't normally happen — a UserProfile is created
            // automatically at registration — but fail safely rather
            // than crash if it's somehow missing.
            return const Center(
              child: Padding(
                padding: EdgeInsets.all(24),
                child: Text(
                  'No profile found for this account. Please contact '
                  'support.',
                  textAlign: TextAlign.center,
                ),
              ),
            );
          }

          _seedFrom(profile!);

          return SingleChildScrollView(
            padding: const EdgeInsets.all(16),
            child: Form(
              key: _formKey,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Center(
                    child: Stack(
                      children: [
                        CircleAvatar(
                          radius: 48,
                          backgroundColor: Colors.blue.shade50,
                          backgroundImage: _pickedImage != null
                              ? FileImage(_pickedImage!)
                              : (_existingImageUrl != null
                                        ? NetworkImage(_existingImageUrl!)
                                        : null)
                                    as ImageProvider?,
                          child:
                              _pickedImage == null && _existingImageUrl == null
                              ? const Icon(
                                  Icons.person,
                                  size: 40,
                                  color: Colors.blue,
                                )
                              : null,
                        ),
                        Positioned(
                          bottom: 0,
                          right: 0,
                          child: InkWell(
                            onTap: _saving ? null : _pickImage,
                            child: Container(
                              padding: const EdgeInsets.all(6),
                              decoration: const BoxDecoration(
                                color: Colors.blue,
                                shape: BoxShape.circle,
                              ),
                              child: const Icon(
                                Icons.camera_alt,
                                size: 16,
                                color: Colors.white,
                              ),
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                  if (_pickedImage != null)
                    Center(
                      child: TextButton.icon(
                        onPressed: _saving
                            ? null
                            : () => setState(() => _pickedImage = null),
                        icon: const Icon(
                          Icons.close,
                          size: 18,
                          color: Colors.red,
                        ),
                        label: const Text(
                          'Remove selected photo',
                          style: TextStyle(color: Colors.red),
                        ),
                      ),
                    ),
                  const SizedBox(height: 12),

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

                  // ── Basic info ──────────────────────────
                  TextFormField(
                    controller: _nameCtrl,
                    decoration: const InputDecoration(
                      labelText: 'Full Name',
                      prefixIcon: Icon(Icons.person_outline),
                    ),
                    validator: (v) => (v == null || v.trim().isEmpty)
                        ? 'Full name is required.'
                        : null,
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
                    controller: _headlineCtrl,
                    decoration: const InputDecoration(
                      labelText: 'Headline',
                      hintText: 'e.g. Flutter Developer | Ex-XYZ',
                      prefixIcon: Icon(Icons.title_outlined),
                    ),
                  ),
                  const SizedBox(height: 14),

                  TextFormField(
                    controller: _professionalSummaryCtrl,
                    maxLines: 3,
                    decoration: const InputDecoration(
                      labelText: 'Professional Summary',
                      alignLabelWithHint: true,
                      prefixIcon: Icon(Icons.summarize_outlined),
                    ),
                  ),
                  const SizedBox(height: 14),

                  TextFormField(
                    controller: _bioCtrl,
                    maxLines: 3,
                    decoration: const InputDecoration(
                      labelText: 'Bio',
                      alignLabelWithHint: true,
                      prefixIcon: Icon(Icons.description_outlined),
                    ),
                  ),
                  const SizedBox(height: 14),

                  InkWell(
                    onTap: _pickDateOfBirth,
                    child: InputDecorator(
                      decoration: const InputDecoration(
                        labelText: 'Date of Birth',
                        prefixIcon: Icon(Icons.cake_outlined),
                      ),
                      child: Text(
                        _dateOfBirth != null
                            ? '${_dateOfBirth!.day}/${_dateOfBirth!.month}/${_dateOfBirth!.year}'
                            : 'Select date',
                        style: TextStyle(
                          color: _dateOfBirth != null ? null : Colors.black45,
                        ),
                      ),
                    ),
                  ),
                  const SizedBox(height: 14),

                  DropdownButtonFormField<GenderType>(
                    value: _gender,
                    decoration: const InputDecoration(
                      labelText: 'Gender',
                      prefixIcon: Icon(Icons.wc_outlined),
                    ),
                    items: GenderType.values
                        .map(
                          (g) => DropdownMenuItem(
                            value: g,
                            child: Text(_genderLabel(g)),
                          ),
                        )
                        .toList(),
                    onChanged: (v) => setState(() => _gender = v),
                  ),
                  const SizedBox(height: 20),

                  // ── Identity ────────────────────────────
                  const Text(
                    'Identity',
                    style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 10),
                  TextFormField(
                    controller: _nationalityCtrl,
                    decoration: const InputDecoration(labelText: 'Nationality'),
                  ),
                  const SizedBox(height: 14),
                  TextFormField(
                    controller: _religionCtrl,
                    decoration: const InputDecoration(labelText: 'Religion'),
                  ),
                  const SizedBox(height: 14),
                  TextFormField(
                    controller: _maritalStatusCtrl,
                    decoration: const InputDecoration(
                      labelText: 'Marital Status',
                    ),
                  ),
                  const SizedBox(height: 14),
                  Row(
                    children: [
                      Expanded(
                        child: TextFormField(
                          controller: _fatherNameCtrl,
                          decoration: const InputDecoration(
                            labelText: "Father's Name",
                          ),
                        ),
                      ),
                      const SizedBox(width: 12),
                      Expanded(
                        child: TextFormField(
                          controller: _motherNameCtrl,
                          decoration: const InputDecoration(
                            labelText: "Mother's Name",
                          ),
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 14),
                  TextFormField(
                    controller: _nidNumberCtrl,
                    decoration: const InputDecoration(labelText: 'NID Number'),
                  ),
                  const SizedBox(height: 14),
                  TextFormField(
                    controller: _passportNumberCtrl,
                    decoration: const InputDecoration(
                      labelText: 'Passport Number',
                    ),
                  ),
                  const SizedBox(height: 20),

                  // ── Career ──────────────────────────────
                  const Text(
                    'Career',
                    style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 10),
                  TextFormField(
                    controller: _careerObjectiveCtrl,
                    maxLines: 3,
                    decoration: const InputDecoration(
                      labelText: 'Career Objective',
                      alignLabelWithHint: true,
                    ),
                  ),
                  const SizedBox(height: 14),
                  TextFormField(
                    controller: _freelancerTitleCtrl,
                    decoration: const InputDecoration(
                      labelText: 'Freelancer Title',
                    ),
                  ),
                  const SizedBox(height: 14),
                  Row(
                    children: [
                      Expanded(
                        child: TextFormField(
                          controller: _expectedSalaryCtrl,
                          keyboardType: TextInputType.number,
                          decoration: const InputDecoration(
                            labelText: 'Expected Salary',
                            prefixText: '৳ ',
                          ),
                        ),
                      ),
                      const SizedBox(width: 12),
                      Expanded(
                        child: TextFormField(
                          controller: _currentSalaryCtrl,
                          keyboardType: TextInputType.number,
                          decoration: const InputDecoration(
                            labelText: 'Current Salary',
                            prefixText: '৳ ',
                          ),
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 14),
                  DropdownButtonFormField<JobType>(
                    value: _preferredJobType,
                    decoration: const InputDecoration(
                      labelText: 'Preferred Job Type',
                    ),
                    items: JobType.values
                        .map(
                          (t) => DropdownMenuItem(
                            value: t,
                            child: Text(_jobTypeLabel(t)),
                          ),
                        )
                        .toList(),
                    onChanged: (v) => setState(() => _preferredJobType = v),
                  ),
                  const SizedBox(height: 14),
                  DropdownButtonFormField<WorkPlaceType>(
                    value: _preferredWorkplace,
                    decoration: const InputDecoration(
                      labelText: 'Preferred Workplace',
                    ),
                    items: WorkPlaceType.values
                        .map(
                          (t) => DropdownMenuItem(
                            value: t,
                            child: Text(_workplaceLabel(t)),
                          ),
                        )
                        .toList(),
                    onChanged: (v) => setState(() => _preferredWorkplace = v),
                  ),
                  const SizedBox(height: 14),
                  TextFormField(
                    controller: _githubLinkCtrl,
                    decoration: const InputDecoration(labelText: 'GitHub'),
                  ),
                  const SizedBox(height: 14),
                  TextFormField(
                    controller: _linkedinLinkCtrl,
                    decoration: const InputDecoration(labelText: 'LinkedIn'),
                  ),
                  const SizedBox(height: 14),
                  TextFormField(
                    controller: _portfolioWebsiteCtrl,
                    decoration: const InputDecoration(
                      labelText: 'Portfolio Website',
                    ),
                  ),
                  const SizedBox(height: 20),

                  // ── Present address ─────────────────────
                  const Text(
                    'Present Address *',
                    style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                  ),
                  const Text(
                    'Required — select down to Police Station.',
                    style: TextStyle(fontSize: 12, color: Colors.black54),
                  ),
                  const SizedBox(height: 10),
                  LocationCascade(
                    initialSelection: _presentLocation,
                    onChanged: _onPresentLocationChanged,
                  ),
                  const SizedBox(height: 14),
                  TextFormField(
                    controller: _presentDetailsCtrl,
                    maxLines: 2,
                    onChanged: (v) {
                      if (_sameAsPresent) {
                        setState(() => _permanentDetailsCtrl.text = v);
                      }
                    },
                    decoration: const InputDecoration(
                      labelText: 'Address Details',
                      alignLabelWithHint: true,
                      hintText: 'House/road/area',
                    ),
                  ),
                  const SizedBox(height: 14),
                  TextFormField(
                    controller: _presentPostCodeCtrl,
                    onChanged: (v) {
                      if (_sameAsPresent) {
                        setState(() => _permanentPostCodeCtrl.text = v);
                      }
                    },
                    decoration: const InputDecoration(labelText: 'Post Code'),
                  ),
                  const SizedBox(height: 16),

                  // ── Same as present toggle ──────────────
                  InkWell(
                    onTap: () => _toggleSameAsPresent(!_sameAsPresent),
                    child: Row(
                      children: [
                        Checkbox(
                          value: _sameAsPresent,
                          onChanged: (v) => _toggleSameAsPresent(v ?? false),
                        ),
                        const Expanded(
                          child: Text(
                            'Permanent address is the same as '
                            'present address',
                          ),
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(height: 12),

                  // ── Permanent address ────────────────────
                  Text(
                    'Permanent Address *',
                    style: TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.bold,
                      color: _sameAsPresent ? Colors.black38 : Colors.black,
                    ),
                  ),
                  const Text(
                    'Required — select down to Police Station.',
                    style: TextStyle(fontSize: 12, color: Colors.black54),
                  ),
                  const SizedBox(height: 10),
                  IgnorePointer(
                    ignoring: _sameAsPresent,
                    child: Opacity(
                      opacity: _sameAsPresent ? 0.5 : 1,
                      child: LocationCascade(
                        initialSelection: _permanentLocation,
                        onChanged: (loc) =>
                            setState(() => _permanentLocation = loc),
                      ),
                    ),
                  ),
                  const SizedBox(height: 14),
                  Opacity(
                    opacity: _sameAsPresent ? 0.5 : 1,
                    child: TextFormField(
                      controller: _permanentDetailsCtrl,
                      enabled: !_sameAsPresent,
                      maxLines: 2,
                      decoration: const InputDecoration(
                        labelText: 'Address Details',
                        alignLabelWithHint: true,
                        hintText: 'House/road/area',
                      ),
                    ),
                  ),
                  const SizedBox(height: 14),
                  Opacity(
                    opacity: _sameAsPresent ? 0.5 : 1,
                    child: TextFormField(
                      controller: _permanentPostCodeCtrl,
                      enabled: !_sameAsPresent,
                      decoration: const InputDecoration(labelText: 'Post Code'),
                    ),
                  ),
                  const SizedBox(height: 28),

                  FilledButton(
                    onPressed: _saving
                        ? null
                        : () => _save(profile.id!, profile.userId!),
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
                        : const Text('Save Changes'),
                  ),
                  const SizedBox(height: 16),
                ],
              ),
            ),
          );
        },
      ),
    );
  }

  String _genderLabel(GenderType g) => switch (g) {
    GenderType.male => 'Male',
    GenderType.female => 'Female',
    GenderType.other => 'Other',
  };

  String _jobTypeLabel(JobType t) => switch (t) {
    JobType.fullTime => 'Full Time',
    JobType.partTime => 'Part Time',
    JobType.contract => 'Contract',
    JobType.internship => 'Internship',
    JobType.freelance => 'Freelance',
    JobType.remote => 'Remote',
    JobType.temporary => 'Temporary',
    JobType.volunteer => 'Volunteer',
  };

  String _workplaceLabel(WorkPlaceType t) => switch (t) {
    WorkPlaceType.onsite => 'On-site',
    WorkPlaceType.remote => 'Remote',
    WorkPlaceType.hybrid => 'Hybrid',
  };
}
