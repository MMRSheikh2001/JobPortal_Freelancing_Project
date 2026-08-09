import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Gender } from '../../enums/gender.enum';
import { FileResourceHandleService } from '../../services/file-resource-handle.service';
import { ResumeResponseService } from '../../user/resume/services/resume-response.service';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ResumeResponseModel } from '../../user/resume/models/resume-response.model';

@Component({
  selector: 'app-admin-user-profile-preview',
  imports: [CommonModule],
  templateUrl: './admin-user-profile-preview.html',
  styleUrl: './admin-user-profile-preview.css',
})
export class AdminUserProfilePreview implements OnInit {






  // =====================================
  // IDs
  // =====================================

  profileId = 0;

  // =====================================
  // Resume
  // =====================================

  resume?: ResumeResponseModel;

  loading = true;

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private route: ActivatedRoute,

    private resumeService: ResumeResponseService,

    private fileService: FileResourceHandleService,

    private cdr: ChangeDetectorRef,
    private router: Router,

    

  ) { }

  // =====================================
  // Init
  // =====================================

  ngOnInit(): void {

    this.profileId = Number(
      this.route.snapshot.paramMap.get('userProfileId')
    );

    this.loadResume();

  }

  // =====================================
  // Load Resume
  // =====================================

  loadResume(): void {

    this.loading = true;

    this.resumeService
      .getResume(this.profileId)
      .subscribe({

        next: data => {

          this.resume = data;

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          alert('Failed to load profile.');

        }

      });

  }

  // =====================================
  // Profile Image
  // =====================================

  getProfileImage(fileName?: string): string {

    return this.fileService.getUserProfileImage(fileName);

  }

  // =====================================
  // Training File
  // =====================================

  getTraining(fileName?: string): string {

    return this.fileService.getTraining(fileName);

  }

  // =====================================
  // Portfolio File
  // =====================================

  getPortfolio(fileName?: string): string {

    return this.fileService.getPortfolio(fileName);

  }

  // =====================================
  // Gender
  // =====================================

  getGender(): string {

    if (!this.resume) {
      return '';
    }

    switch (this.resume.profile.gender) {

      case Gender.MALE:
        return 'Male';

      case Gender.FEMALE:
        return 'Female';

      default:
        return 'Other';

    }

  }

  // =====================================
  // Preferred Job Type
  // =====================================

  getJobType(): string {

    if (!this.resume) {
      return '';
    }

    return this.resume.profile.preferredJobType
      ?.replaceAll('_', ' ');

  }

  // =====================================
  // Preferred Workplace
  // =====================================

  getWorkPlace(): string {

    if (!this.resume) {
      return '';
    }

    return this.resume.profile.preferredWorkplace
      ?.replaceAll('_', ' ');

  }

  // =====================================
  // Present Address
  // =====================================

  getPresentAddress(): string {

    if (!this.resume) {
      return '';
    }

    const p = this.resume.profile;

    return `${p.presentAddressDetails},
${p.presentPoliceStationName},
${p.presentDistrictName},
${p.presentDivisionName},
${p.presentCountryName}`;

  }

  // =====================================
  // Permanent Address
  // =====================================

  getPermanentAddress(): string {

    if (!this.resume) {
      return '';
    }

    const p = this.resume.profile;

    return `${p.permanentAddressDetails},
${p.permanentPoliceStationName},
${p.permanentDistrictName},
${p.permanentDivisionName},
${p.permanentCountryName}`;

  }

  // =====================================
  // Open External Link
  // =====================================

  open(url?: string): void {

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

    window.open(link, '_blank');

  }


  //=====================================
  // View User Gigs
  //=====================================

  viewUserGigs(): void {

    this.router.navigate([
      '/admin/freelancer-gigs',
      this.profileId
    ]);

  }

}
