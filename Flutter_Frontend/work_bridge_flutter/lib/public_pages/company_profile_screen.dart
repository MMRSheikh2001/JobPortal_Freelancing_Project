import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:image_picker/image_picker.dart';
import 'package:work_bridge_flutter/auth/providers.dart';
import 'package:work_bridge_flutter/cvinformations/models/request/company_profile_request.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/company_profile_response.dart';
import 'package:work_bridge_flutter/cvinformations/providers/company_profile_provider.dart';
import 'package:work_bridge_flutter/masterdata/widget/location_cascade.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

class CompanyProfileScreen extends ConsumerStatefulWidget {
  const CompanyProfileScreen({super.key});

  @override
  ConsumerState<CompanyProfileScreen> createState() =>
      _CompanyProfileScreenState();
}

class _CompanyProfileScreenState extends ConsumerState<CompanyProfileScreen> {
  final _formKey = GlobalKey<FormState>();

  final _nameCtrl = TextEditingController();
  final _phoneCtrl = TextEditingController();
  final _emailCtrl = TextEditingController();
  final _descriptionCtrl = TextEditingController();
  final _websiteCtrl = TextEditingController();
  final _industryCtrl = TextEditingController();
  final _foundedYearCtrl = TextEditingController();
  final _tradeLicenseCtrl = TextEditingController();
  final _locationDetailsCtrl = TextEditingController();
  final _postCodeCtrl = TextEditingController();

  LocationSelection _location = LocationSelection.empty;

  // Address row id on the backend. Null on a brand-new company profile
  // (no address saved yet) — the first save omits `locationId`, so the
  // backend creates a fresh Address row. That row's id comes back in the
  // save response and gets captured into this field immediately (see
  // _save()), so a *second* save in the same session correctly sends
  // `locationId` and updates that same row instead of creating another
  // orphaned Address every time.
  int? _locationId;

  File? _pickedImage;
  String? _existingImageUrl;

  bool _seeded = false;
  bool _saving = false;
  String? _errorMessage;

  void _seedFrom(CompanyProfileResponseDTO profile) {
    if (_seeded) return; // don't overwrite what the user is actively typing
    _seeded = true;
    _nameCtrl.text = profile.name ?? '';
    _phoneCtrl.text = profile.phone ?? '';
    _emailCtrl.text = profile.companyEmail ?? '';
    _descriptionCtrl.text = profile.companyDescription ?? '';
    _websiteCtrl.text = profile.companyWebsite ?? '';
    _industryCtrl.text = profile.industry ?? '';
    _foundedYearCtrl.text = profile.foundedYear ?? '';
    _tradeLicenseCtrl.text = profile.tradeLicenseNumber ?? '';
    _locationDetailsCtrl.text = profile.locationDetails ?? '';
    _postCodeCtrl.text = profile.locationPostCode ?? '';
    _existingImageUrl = (profile.image != null && profile.image!.isNotEmpty)
        ? '${ApiConstants.companyProfileImageUrl}${profile.image}'
        : null;
    _locationId = profile.locationId;
    _location = LocationSelection(
      countryId: profile.locationCountryId,
      countryName: profile.locationCountryName,
      divisionId: profile.locationDivisionId,
      divisionName: profile.locationDivisionName,
      districtId: profile.locationDistrictId,
      districtName: profile.locationDistrictName,
      policeStationId: profile.locationPoliceStationId,
      policeStationName: profile.locationPoliceStationName,
    );
  }

  @override
  void dispose() {
    _nameCtrl.dispose();
    _phoneCtrl.dispose();
    _emailCtrl.dispose();
    _descriptionCtrl.dispose();
    _websiteCtrl.dispose();
    _industryCtrl.dispose();
    _foundedYearCtrl.dispose();
    _tradeLicenseCtrl.dispose();
    _locationDetailsCtrl.dispose();
    _postCodeCtrl.dispose();
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

  Future<void> _save(int profileId) async {
    if (!_formKey.currentState!.validate()) return;

    // The backend crashes (500) if locationPoliceStationId is null on
    // save, and there's no server-side default/skip for it, so the
    // address section is enforced as required here rather than optional.


    if (_location.policeStationId == null) {
      setState(() {
        _errorMessage =
            'Please complete the Company Address section (down to Police '
            'Station) before saving.';
      });
      return;
    }

    setState(() {
      _saving = true;
      _errorMessage = null;
    });


    final storageService = ref.read(storageServiceProvider); // Or your locator/DI method
    final user = await storageService.getUser();

    if (user == null || user.userId == null) {
// Handle unauthenticated state or missing ID
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('User session expired. Please log in again.')),
      );
      return;
    }

    try {
      final request = CompanyProfileRequestDTO(


        userId: user.userId!,
        name: _nameCtrl.text.trim(),
        phone: _phoneCtrl.text.trim().isEmpty ? null : _phoneCtrl.text.trim(),
        companyEmail: _emailCtrl.text.trim().isEmpty
            ? null
            : _emailCtrl.text.trim(),
        companyDescription: _descriptionCtrl.text.trim().isEmpty
            ? null
            : _descriptionCtrl.text.trim(),
        companyWebsite: _websiteCtrl.text.trim().isEmpty
            ? null
            : _websiteCtrl.text.trim(),
        industry: _industryCtrl.text.trim().isEmpty
            ? null
            : _industryCtrl.text.trim(),
        foundedYear: _foundedYearCtrl.text.trim().isEmpty
            ? null
            : _foundedYearCtrl.text.trim(),
        tradeLicenseNumber: _tradeLicenseCtrl.text.trim().isEmpty
            ? null
            : _tradeLicenseCtrl.text.trim(),
        locationDetails: _locationDetailsCtrl.text.trim().isEmpty
            ? null
            : _locationDetailsCtrl.text.trim(),
        locationPostCode: _postCodeCtrl.text.trim().isEmpty
            ? null
            : _postCodeCtrl.text.trim(),
        locationPoliceStationId: _location.policeStationId,
        // Null on first save (no address exists yet) -> backend creates
        // a new Address row. Non-null on every save after that -> backend
        // updates that same row instead of creating a new one.
        locationId: _locationId,
      );


      final updated = await ref
          .read(companyProfileRepositoryProvider)
          .updateCompanyProfile(profileId, request, _pickedImage);



      // Capture what the backend actually persisted — most importantly
      // the Address id it just created (if this was the first save), so
      // a second save in the same session sends it back and updates the
      // same row instead of creating another one.
      _locationId = updated.locationId;
      _existingImageUrl = (updated.image != null && updated.image!.isNotEmpty)
          ? '${ApiConstants.companyProfileImageUrl}${updated.image}'
          : null;
      _pickedImage = null; // now uploaded; stop showing the local file

      ref.invalidate(myCompanyProfileProvider);
      if (!mounted) return;

      ScaffoldMessenger.of(
        context,
      ).showSnackBar(const SnackBar(content: Text('Company profile updated.')));
    } catch (e) {
      setState(() => _errorMessage = e.toString());
    } finally {
      if (mounted) setState(() => _saving = false);
    }
  }

  Future<void> _removeImage(int profileId) async {
    setState(() => _saving = true);
    try {
      await ref
          .read(companyProfileRepositoryProvider)
          .deleteCompanyProfileImage(profileId);
      ref.invalidate(myCompanyProfileProvider);
      if (!mounted) return;
      setState(() {
        _pickedImage = null;
        _existingImageUrl = null;
      });
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('Could not remove logo: $e')));
    } finally {
      if (mounted) setState(() => _saving = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    final profileAsync = ref.watch(myCompanyProfileProvider);

    return Scaffold(
      appBar: AppBar(title: const Text('Company Profile')),
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
                  onPressed: () => ref.invalidate(myCompanyProfileProvider),
                  child: const Text('Retry'),
                ),
              ],
            ),
          ),
        ),
        data: (profile) {
          if (profile?.id == null) {
            return const Center(
              child: Padding(
                padding: EdgeInsets.all(24),
                child: Text(
                  'No company profile found for this account. '
                  'Please contact support.',
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
                                  Icons.apartment,
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
                  if (_pickedImage != null || _existingImageUrl != null)
                    Center(
                      child: TextButton.icon(
                        onPressed: _saving
                            ? null
                            : () {
                                if (_pickedImage != null) {
                                  setState(() => _pickedImage = null);
                                } else {
                                  _removeImage(profile.id!);
                                }
                              },
                        icon: const Icon(
                          Icons.delete_outline,
                          size: 18,
                          color: Colors.red,
                        ),
                        label: const Text(
                          'Remove logo',
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

                  TextFormField(
                    controller: _nameCtrl,
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
                      labelText: 'Company Email',
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
                    controller: _descriptionCtrl,
                    maxLines: 4,
                    decoration: const InputDecoration(
                      labelText: 'Company Description',
                      alignLabelWithHint: true,
                      prefixIcon: Icon(Icons.description_outlined),
                    ),
                  ),
                  const SizedBox(height: 14),

                  TextFormField(
                    controller: _websiteCtrl,
                    keyboardType: TextInputType.url,
                    decoration: const InputDecoration(
                      labelText: 'Website',
                      prefixIcon: Icon(Icons.language_outlined),
                      hintText: 'https://example.com',
                    ),
                  ),
                  const SizedBox(height: 14),

                  Row(
                    children: [
                      Expanded(
                        child: TextFormField(
                          controller: _industryCtrl,
                          decoration: const InputDecoration(
                            labelText: 'Industry',
                            prefixIcon: Icon(Icons.factory_outlined),
                          ),
                        ),
                      ),
                      const SizedBox(width: 12),
                      Expanded(
                        child: TextFormField(
                          controller: _foundedYearCtrl,
                          keyboardType: TextInputType.number,
                          decoration: const InputDecoration(
                            labelText: 'Founded Year',
                            prefixIcon: Icon(Icons.event_outlined),
                          ),
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 14),

                  TextFormField(
                    controller: _tradeLicenseCtrl,
                    decoration: const InputDecoration(
                      labelText: 'Trade License Number',
                      prefixIcon: Icon(Icons.badge_outlined),
                    ),
                  ),
                  const SizedBox(height: 20),

                  const Text(
                    'Company Address *',
                    style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                  ),
                  const Text(
                    'Required — select down to Police Station.',
                    style: TextStyle(fontSize: 12, color: Colors.black54),
                  ),
                  const SizedBox(height: 10),
                  LocationCascade(
                    initialSelection: _location,
                    onChanged: (loc) => setState(() => _location = loc),
                  ),
                  const SizedBox(height: 14),

                  TextFormField(
                    controller: _locationDetailsCtrl,
                    maxLines: 2,
                    decoration: const InputDecoration(
                      labelText: 'Address Details',
                      alignLabelWithHint: true,
                      hintText: 'House/road/area',
                      prefixIcon: Icon(Icons.home_outlined),
                    ),
                  ),
                  const SizedBox(height: 14),

                  TextFormField(
                    controller: _postCodeCtrl,
                    decoration: const InputDecoration(
                      labelText: 'Post Code',
                      prefixIcon: Icon(Icons.local_post_office_outlined),
                    ),
                  ),
                  const SizedBox(height: 28),

                  FilledButton(
                    onPressed: _saving ? null : () => _save(profile.id!),
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
}
