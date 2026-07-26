import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CompanyProfileResponseModel } from '../../models/company-profile.model';
import { CompanyProfileService } from '../../services/company-profile.service';
import { StorageService } from '../../../../auth/services/storage.service';
import { FileResourceHandleService } from '../../../../services/file-resource-handle.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-company-profile-preview',
  imports: [CommonModule],
  templateUrl: './company-profile-preview.html',
  styleUrl: './company-profile-preview.css',
})
export class CompanyProfilePreview implements OnInit {





  // =====================================
  // IDs
  // =====================================

  profileId = 0;

  // =====================================
  // Company Profile
  // =====================================

  profile?: CompanyProfileResponseModel;

  loading = true;

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private companyProfileService: CompanyProfileService,

    private storage: StorageService,

    private fileService: FileResourceHandleService,

    private router: Router,

    private cdr: ChangeDetectorRef

  ) { }

  // =====================================
  // Init
  // =====================================

  ngOnInit(): void {

    this.profileId =
      this.storage.getProfileId() ?? 0;

    this.loadProfile();

  }

  // =====================================
  // Load Profile
  // =====================================

  loadProfile() {

    this.loading = true;

    this.companyProfileService
      .getById(this.profileId)
      .subscribe({

        next: data => {

          this.profile = data;

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          alert("Failed to load company profile.");

        }

      });

  }

  // =====================================
  // Company Logo
  // =====================================

  getCompanyImage(fileName?: string): string {

    return this.fileService
      .getCompanyProfileImage(fileName);

  }

  // =====================================
  // Full Address
  // =====================================

  getAddress(): string {

    if (!this.profile) {

      return '';

    }

    return `${this.profile.locationDetails},
${this.profile.locationPoliceStationName},
${this.profile.locationDistrictName},
${this.profile.locationDivisionName},
${this.profile.locationCountryName}`;

  }

  // =====================================
  // Website
  // =====================================

  open(url?: string) {

    if (!url) {

      return;

    }

    let link = url;

    if (
      !link.startsWith('http://') &&
      !link.startsWith('https://')
    ) {

      link = 'https://' + link;

    }

    window.open(
      link,
      '_blank'
    );

  }

  // =====================================
  // Edit Profile
  // =====================================

  editProfile() {

    this.router.navigate([
      '/company/company-profile'
    ]);

  }



}
