import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CompanyProfileRequestModel, CompanyProfileResponseModel } from '../../models/company-profile.model';
import { CountryResponseModel } from '../../../../admin/address/models/country.model';
import { DivisionResponseModel } from '../../../../admin/address/models/division.model';
import { DistrictResponseModel } from '../../../../admin/address/models/district.model';
import { PoliceStationResponseModel } from '../../../../admin/address/models/police-station.model';
import { CompanyProfileService } from '../../services/company-profile.service';
import { StorageService } from '../../../../auth/services/storage.service';
import { FileResourceHandleService } from '../../../../services/file-resource-handle.service';
import { CountryService } from '../../../../admin/address/services/country.service';
import { DivisionService } from '../../../../admin/address/services/division.service';
import { DistrictService } from '../../../../admin/address/services/district.service';
import { PoliceStationService } from '../../../../admin/address/services/police-station.service';
import { ToastService } from '../../../../services/toast.service';

@Component({
  selector: 'app-company-profile',
  imports: [CommonModule, FormsModule],
  templateUrl: './company-profile.html',
  styleUrl: './company-profile.css',
})
export class CompanyProfile implements OnInit {





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

  profile?: CompanyProfileResponseModel;

  // ======================================
  // Request Model
  // ======================================

  companyProfile: CompanyProfileRequestModel = {

    userId: 0,

    name: '',

    phone: '',

    companyEmail: '',

    companyDescription: '',

    companyWebsite: '',

    industry: '',

    foundedYear: '',

    tradeLicenseNumber: '',

    locationId: 0,

    locationDetails: '',

    locationPostCode: '',

    locationPoliceStationId: 0

  };

  // ======================================
  // Dropdown Lists
  // ======================================

  countries: CountryResponseModel[] = [];

  divisions: DivisionResponseModel[] = [];

  districts: DistrictResponseModel[] = [];

  policeStations: PoliceStationResponseModel[] = [];

  // ======================================
  // Temporary Address
  // ======================================

  countryId = 0;

  divisionId = 0;

  districtId = 0;

  // ======================================
  // Constructor
  // ======================================

  constructor(

    private service: CompanyProfileService,

    private storage: StorageService,

    private fileService: FileResourceHandleService,

    private countryService: CountryService,

    private divisionService: DivisionService,

    private districtService: DistrictService,

    private policeStationService: PoliceStationService,

    private toast: ToastService,

    private cdr: ChangeDetectorRef

  ) { }

  // ======================================
  // Init
  // ======================================

  ngOnInit(): void {

    this.profileId =
      this.storage.getProfileId() ?? 0;

    this.userId =
      this.storage.getUserId() ?? 0;

    this.companyProfile.userId =
      this.userId;

    this.loadCountries();

    this.loadProfile();

  }

  // ======================================
  // Load Countries
  // ======================================

  loadCountries() {

    this.countryService
      .getAll()
      .subscribe(data => {

        this.countries = data;

        this.cdr.markForCheck();

      });

  }

  // ======================================
  // Load Profile
  // ======================================

  loadProfile() {

    this.service
      .getById(this.profileId)
      .subscribe(data => {

        this.profile = data;

        this.companyProfile = {

          userId: data.userId,

          name: data.name,

          phone: data.phone,

          companyEmail: data.companyEmail,

          companyDescription: data.companyDescription,

          companyWebsite: data.companyWebsite,

          industry: data.industry,

          foundedYear: data.foundedYear,

          tradeLicenseNumber: data.tradeLicenseNumber,

          locationId: data.locationId,

          locationDetails: data.locationDetails,

          locationPostCode: data.locationPostCode,

          locationPoliceStationId:
            data.locationPoliceStationId

        };

        this.countryId =
          data.locationCountryId;

        this.divisionId =
          data.locationDivisionId;

        this.districtId =
          data.locationDistrictId;

        this.imagePreview =
          this.fileService.getCompanyProfileImage(
            data.image
          );

        this.loadDivisions();

        this.cdr.markForCheck();

      });

  }

  // ======================================
  // Load Divisions
  // ======================================

  loadDivisions() {

    if (!this.countryId) {

      this.divisions = [];

      this.districts = [];

      this.policeStations = [];

      return;

    }

    this.divisionService
      .getByCountryId(this.countryId)
      .subscribe(data => {

        this.divisions = data;

        if (this.divisionId) {

          this.loadDistricts();

        }

        this.cdr.markForCheck();

      });

  }

  // ======================================
  // Load Districts
  // ======================================

  loadDistricts() {

    if (!this.divisionId) {

      this.districts = [];

      this.policeStations = [];

      return;

    }

    this.districtService
      .getByDivisionId(this.divisionId)
      .subscribe(data => {

        this.districts = data;

        if (this.districtId) {

          this.loadPoliceStations();

        }

        this.cdr.markForCheck();

      });

  }

  // ======================================
  // Load Police Stations
  // ======================================

  loadPoliceStations() {

    if (!this.districtId) {

      this.policeStations = [];

      return;

    }

    this.policeStationService
      .getByDistrictId(this.districtId)
      .subscribe(data => {

        this.policeStations = data;

        this.cdr.markForCheck();

      });

  }

  // ======================================
  // Country Changed
  // ======================================

  onCountryChange() {

    this.divisionId = 0;

    this.districtId = 0;

    this.companyProfile.locationPoliceStationId = 0;

    this.divisions = [];

    this.districts = [];

    this.policeStations = [];

    this.loadDivisions();

  }

  // ======================================
  // Division Changed
  // ======================================

  onDivisionChange() {

    this.districtId = 0;

    this.companyProfile.locationPoliceStationId = 0;

    this.districts = [];

    this.policeStations = [];

    this.loadDistricts();

  }

  // ======================================
  // District Changed
  // ======================================

  onDistrictChange() {

    this.companyProfile.locationPoliceStationId = 0;

    this.loadPoliceStations();

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

      this.companyProfile,

      this.selectedImage

    ).subscribe(data => {

      this.profile = data;

      this.selectedImage = undefined;

      this.imagePreview =
        this.fileService.getCompanyProfileImage(
          data.image
        );

      this.loadProfile();

      this.toast.show(
        "Company Profile Updated Successfully."
      );

      this.reset();

    });

  }

  // ======================================
  // Delete Image
  // ======================================

  deleteImage() {

    if (!confirm("Delete company logo?")) {
      return;
    }

    this.service
      .deleteImage(this.profileId)
      .subscribe(() => {

        this.selectedImage = undefined;

        this.imagePreview =
          this.fileService.getCompanyProfileImage();

        this.loadProfile();

        alert("Company Logo Deleted.");

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
      'assets/images/default-company.png';

  }

}




