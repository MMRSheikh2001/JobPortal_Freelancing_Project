import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ResumeResponseModel } from '../../models/resume-response.model';
import { ResumeResponseService } from '../../services/resume-response.service';
import { StorageService } from '../../../../auth/services/storage.service';
import { FileResourceHandleService } from '../../../../services/file-resource-handle.service';
import { Gender } from '../../../../enums/gender.enum';

@Component({
  selector: 'app-resume-preview',
  imports: [CommonModule],
  templateUrl: './resume-preview.html',
  styleUrl: './resume-preview.css',
})
export class ResumePreview implements OnInit {





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

    private resumeService: ResumeResponseService,

    private storage: StorageService,

    private fileService: FileResourceHandleService,

    private cdr: ChangeDetectorRef

  ) { }

  // =====================================
  // Init
  // =====================================

  ngOnInit(): void {

    this.profileId =
      this.storage.getProfileId() ?? 0;

    this.loadResume();

  }

  // =====================================
  // Load Resume
  // =====================================

  loadResume() {

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

          alert('Failed to load resume.');

        }

      });

  }

  // =====================================
  // Download Backend PDF
  // =====================================

  downloadBackendPdf() {

    this.resumeService
      .downloadPdf(this.profileId)
      .subscribe(blob => {

        const url =
          window.URL.createObjectURL(blob);

        const a =
          document.createElement('a');

        a.href = url;

        a.download = 'Resume.pdf';

        a.click();

        window.URL.revokeObjectURL(url);

      });

  }

  // =====================================
  // Print Angular Resume
  // =====================================

  printResume() {

    window.print();

  }

  // =====================================
  // Profile Image
  // =====================================

  getProfileImage(fileName?: string): string {

    return this.fileService.getUserProfileImage(fileName);

  }

  // =====================================
  // Training Certificate
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

    window.open(link, '_blank');

  }

  // =====================================
  // Result Formatter
  // =====================================

  getEducationResult(result: any): string {

    if (result == null) {
      return '';
    }

    return String(result);

  }




}
