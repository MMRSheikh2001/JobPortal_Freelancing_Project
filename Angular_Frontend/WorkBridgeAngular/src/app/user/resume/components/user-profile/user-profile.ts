import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UserProfileRequestModel, UserProfileResponseModel } from '../../models/user.profile.model';
import { CountryResponseModel } from '../../../../admin/address/models/country.model';
import { DivisionResponseModel } from '../../../../admin/address/models/division.model';
import { DistrictResponseModel } from '../../../../admin/address/models/district.model';
import { PoliceStationResponseModel } from '../../../../admin/address/models/police-station.model';
import { UserProfileService } from '../../services/user.profile.service';
import { StorageService } from '../../../../auth/services/storage.service';
import { FileResourceHandleService } from '../../../../services/file-resource-handle.service';
import { CountryService } from '../../../../admin/address/services/country.service';
import { DivisionService } from '../../../../admin/address/services/division.service';
import { DistrictService } from '../../../../admin/address/services/district.service';
import { PoliceStationService } from '../../../../admin/address/services/police-station.service';
import { ToastService } from '../../../../services/toast.service';

@Component({
  selector: 'app-user-profile',
  imports: [CommonModule, FormsModule],
  templateUrl: './user-profile.html',
  styleUrl: './user-profile.css',
})
export class UserProfile implements OnInit {





  // ======================================
  // User
  // ======================================

  profileId = 0;

  userId = 0;

  // ======================================
  // Image
  // ======================================

  selectedImage?: File;

  imagePreview = '';

  // ======================================
  // Response
  // ======================================

  profile?: UserProfileResponseModel;

  // ======================================
  // Request Model
  // ======================================

  userProfile: UserProfileRequestModel = {

    userId: 0,

    name: '',
    phone: '',

    headline: '',
    professionalSummary: '',
    bio: '',

    dateOfBirth: '',

    gender: undefined as any,
    nationality: '',
    religion: '',
    maritalStatus: '',

    fatherName: '',
    motherName: '',

    nidNumber: '',
    passportNumber: '',

    githubLink: '',
    linkedinLink: '',
    portfolioWebsite: '',

    expectedSalary: 0,
    currentSalary: 0,

    preferredJobType: undefined as any,
    preferredWorkplace: undefined as any,

    careerObjective: '',
    freelancerTitle: '',

    presentAddressId: undefined,
    presentAddressDetails: '',
    presentAddressPostCode: '',
    presentAddressPoliceStationId: 0,

    permanentAddressId: undefined,
    permanentAddressDetails: '',
    permanentAddressPostCode: '',
    permanentAddressPoliceStationId: 0

  };

  // ======================================
  // Dropdown Lists
  // ======================================

  countries: CountryResponseModel[] = [];

  presentDivisions: DivisionResponseModel[] = [];

  presentDistricts: DistrictResponseModel[] = [];

  presentPoliceStations: PoliceStationResponseModel[] = [];

  permanentDivisions: DivisionResponseModel[] = [];

  permanentDistricts: DistrictResponseModel[] = [];

  permanentPoliceStations: PoliceStationResponseModel[] = [];

  // ======================================
  // Temporary Present Address
  // ======================================

  presentCountryId = 0;

  presentDivisionId = 0;

  presentDistrictId = 0;

  // ======================================
  // Temporary Permanent Address
  // ======================================

  permanentCountryId = 0;

  permanentDivisionId = 0;

  permanentDistrictId = 0;

  // ======================================
  // Same Address
  // ======================================

  sameAsPresent = false;

  constructor(

    private service: UserProfileService,

    private storage: StorageService,

    private fileService: FileResourceHandleService,

    private countryService: CountryService,

    private divisionService: DivisionService,

    private districtService: DistrictService,

    private policeStationService: PoliceStationService,

    private cdr: ChangeDetectorRef,
    private toast: ToastService

  ) { }

  ngOnInit(): void {

    this.profileId = this.storage.getProfileId() ?? 0;

    this.userId = this.storage.getUserId() ?? 0;

    this.userProfile.userId = this.userId;

    this.loadCountries();

    this.loadProfile();

  }

  // ======================================
  // Load Countries
  // ======================================

  loadCountries() {

    this.countryService.getAll()
      .subscribe(data => {

        this.countries = data;

        this.cdr.markForCheck();

      });

  }

  // ======================================
  // Load Profile
  // ======================================

  loadProfile() {

    this.service.getById(this.profileId)
      .subscribe(data => {

        this.profile = data;

        this.userProfile = {

          userId: data.userId,

          name: data.name,
          phone: data.phone,

          headline: data.headline,
          professionalSummary: data.professionalSummary,
          bio: data.bio,

          dateOfBirth: data.dateOfBirth,

          gender: data.gender,
          nationality: data.nationality,
          religion: data.religion,
          maritalStatus: data.maritalStatus,

          fatherName: data.fatherName,
          motherName: data.motherName,

          nidNumber: data.nidNumber,
          passportNumber: data.passportNumber,

          githubLink: data.githubLink,
          linkedinLink: data.linkedinLink,
          portfolioWebsite: data.portfolioWebsite,

          expectedSalary: data.expectedSalary,
          currentSalary: data.currentSalary,

          preferredJobType: data.preferredJobType,
          preferredWorkplace: data.preferredWorkplace,

          careerObjective: data.careerObjective,
          freelancerTitle: data.freelancerTitle,

          presentAddressId: data.presentAddressId,
          presentAddressDetails: data.presentAddressDetails,
          presentAddressPostCode: data.presentAddressPostCode,
          presentAddressPoliceStationId: data.presentPoliceStationId,

          permanentAddressId: data.permanentAddressId,
          permanentAddressDetails: data.permanentAddressDetails,
          permanentAddressPostCode: data.permanentAddressPostCode,
          permanentAddressPoliceStationId: data.permanentPoliceStationId

        };

        this.presentCountryId = data.presentCountryId;
        this.presentDivisionId = data.presentDivisionId;
        this.presentDistrictId = data.presentDistrictId;

        this.permanentCountryId = data.permanentCountryId;
        this.permanentDivisionId = data.permanentDivisionId;
        this.permanentDistrictId = data.permanentDistrictId;

        this.imagePreview =
          this.fileService.getUserProfileImage(data.image);

        this.loadPresentDivisions();
        this.loadPermanentDivisions();

        this.cdr.markForCheck();

      });

  }

  // ======================================
  // Present Address
  // ======================================

  loadPresentDivisions() {

    if (!this.presentCountryId) {

      this.presentDivisions = [];
      this.presentDistricts = [];
      this.presentPoliceStations = [];

      return;

    }

    this.divisionService
      .getByCountryId(this.presentCountryId)
      .subscribe(data => {

        this.presentDivisions = data;

        if (this.presentDivisionId) {
          this.loadPresentDistricts();
        }

        this.cdr.markForCheck();

      });

  }

  loadPresentDistricts() {

    if (!this.presentDivisionId) {

      this.presentDistricts = [];
      this.presentPoliceStations = [];

      return;

    }

    this.districtService
      .getByDivisionId(this.presentDivisionId)
      .subscribe(data => {

        this.presentDistricts = data;

        if (this.presentDistrictId) {
          this.loadPresentPoliceStations();
        }

        this.cdr.markForCheck();

      });

  }

  loadPresentPoliceStations() {

    if (!this.presentDistrictId) {

      this.presentPoliceStations = [];

      return;

    }

    this.policeStationService
      .getByDistrictId(this.presentDistrictId)
      .subscribe(data => {

        this.presentPoliceStations = data;

        this.cdr.markForCheck();

      });

  }

  onPresentCountryChange() {

    this.presentDivisionId = 0;
    this.presentDistrictId = 0;

    this.userProfile.presentAddressPoliceStationId = 0;

    this.presentDistricts = [];
    this.presentPoliceStations = [];

    this.loadPresentDivisions();

  }

  onPresentDivisionChange() {

    this.presentDistrictId = 0;

    this.userProfile.presentAddressPoliceStationId = 0;

    this.presentPoliceStations = [];

    this.loadPresentDistricts();

  }

  onPresentDistrictChange() {

    this.userProfile.presentAddressPoliceStationId = 0;

    this.loadPresentPoliceStations();

  }

  // ======================================
  // Permanent Address
  // ======================================

  loadPermanentDivisions() {

    if (!this.permanentCountryId) {

      this.permanentDivisions = [];
      this.permanentDistricts = [];
      this.permanentPoliceStations = [];

      return;

    }

    this.divisionService
      .getByCountryId(this.permanentCountryId)
      .subscribe(data => {

        this.permanentDivisions = data;

        if (this.permanentDivisionId) {
          this.loadPermanentDistricts();
        }

        this.cdr.markForCheck();

      });

  }

  loadPermanentDistricts() {

    if (!this.permanentDivisionId) {

      this.permanentDistricts = [];
      this.permanentPoliceStations = [];

      return;

    }

    this.districtService
      .getByDivisionId(this.permanentDivisionId)
      .subscribe(data => {

        this.permanentDistricts = data;

        if (this.permanentDistrictId) {
          this.loadPermanentPoliceStations();
        }

        this.cdr.markForCheck();

      });

  }

  loadPermanentPoliceStations() {

    if (!this.permanentDistrictId) {

      this.permanentPoliceStations = [];

      return;

    }

    this.policeStationService
      .getByDistrictId(this.permanentDistrictId)
      .subscribe(data => {

        this.permanentPoliceStations = data;

        this.cdr.markForCheck();

      });

  }

  onPermanentCountryChange() {

    this.permanentDivisionId = 0;
    this.permanentDistrictId = 0;

    this.userProfile.permanentAddressPoliceStationId = 0;

    this.permanentDistricts = [];
    this.permanentPoliceStations = [];

    this.loadPermanentDivisions();

  }

  onPermanentDivisionChange() {

    this.permanentDistrictId = 0;

    this.userProfile.permanentAddressPoliceStationId = 0;

    this.permanentPoliceStations = [];

    this.loadPermanentDistricts();

  }

  onPermanentDistrictChange() {

    this.userProfile.permanentAddressPoliceStationId = 0;

    this.loadPermanentPoliceStations();

  }

  // ======================================
  // Same As Present Address
  // ======================================

  sameAddressChanged() {

    if (!this.sameAsPresent) {
      return;
    }

    this.userProfile.permanentAddressDetails =
      this.userProfile.presentAddressDetails;

    this.userProfile.permanentAddressPostCode =
      this.userProfile.presentAddressPostCode;

    this.permanentCountryId =
      this.presentCountryId;

    this.permanentDivisionId =
      this.presentDivisionId;

    this.permanentDistrictId =
      this.presentDistrictId;

    this.userProfile.permanentAddressPoliceStationId =
      this.userProfile.presentAddressPoliceStationId;

    this.loadPermanentDivisions();

  }

  // ======================================
  // Image
  // ======================================

  onImageSelected(event: any) {

    const file = event.target.files[0];

    if (!file) {
      return;
    }

    this.selectedImage = file;

    this.imagePreview =
      URL.createObjectURL(file);

  }

  // ======================================
  // Save
  // ======================================

  save() {

    this.service.update(

      this.profileId,

      this.userProfile,

      this.selectedImage

    ).subscribe(data => {

      this.profile = data;

      this.selectedImage = undefined;

      this.imagePreview =
        this.fileService.getUserProfileImage(data.image);

      this.loadProfile();

      this.toast.show(
        "Profile Updated Successfully."
      );
      this.reset();

    });

  }

  // ======================================
  // Delete Image
  // ======================================

  deleteImage() {

    if (!confirm("Delete profile image?")) {
      return;
    }

    this.service.deleteImage(this.profileId)

      .subscribe(() => {

        this.selectedImage = undefined;

        this.imagePreview =
          this.fileService.getUserProfileImage();

        this.loadProfile();

        alert("Profile Image Deleted.");

      });

  }

  // ======================================
  // Reset
  // ======================================

  reset() {

    this.loadProfile();

  }

  // ======================================
  // Helpers
  // ======================================

  getProfileImage(): string {

    return this.imagePreview;

  }

  hasImage(): boolean {

    return this.imagePreview !==
      'assets/images/default-user.png';

  }


}
