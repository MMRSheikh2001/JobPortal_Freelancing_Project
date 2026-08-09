import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ResumeFileResponseModel } from '../../models/resume-file.model';
import { ResumeUploadedFileService } from '../../services/resume-uploaded-file.service';
import { ResumeResponseService } from '../../services/resume-response.service';
import { StorageService } from '../../../../auth/services/storage.service';
import { FileResourceHandleService } from '../../../../services/file-resource-handle.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-resume-upload',
  imports: [CommonModule],
  templateUrl: './resume-upload.html',
  styleUrl: './resume-upload.css',
})
export class ResumeUpload implements OnInit {






  // =====================================
  // IDs
  // =====================================

  profileId = 0;

  // =====================================
  // Resume
  // =====================================

  uploadedResume?: ResumeFileResponseModel;

  resumeExists = false;

  // =====================================
  // File
  // =====================================

  selectedFile?: File;

  // =====================================
  // State
  // =====================================

  loading = true;

  uploading = false;

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private uploadService: ResumeUploadedFileService,

    private resumeService: ResumeResponseService,

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

    this.loadResume();

  }

  // =====================================
  // Load Resume
  // =====================================

  loadResume() {

    this.loading = true;

    this.uploadService
      .existsByUserProfileId(this.profileId)
      .subscribe({

        next: exists => {

          this.resumeExists = exists;

          if (exists) {

            this.uploadService
              .getByUserProfileId(this.profileId)
              .subscribe({

                next: data => {

                  this.uploadedResume = data;

                  this.loading = false;

                  this.cdr.markForCheck();

                },

                error: () => {

                  this.loading = false;

                }

              });

          }
          else {

            this.uploadedResume = undefined;

            this.loading = false;

            this.cdr.markForCheck();

          }

        },

        error: () => {

          this.loading = false;

          alert('Failed to load resume.');

        }

      });

  }

  // =====================================
  // Upload Resume
  // =====================================

  uploadResume() {

    if (!this.selectedFile) {

      alert('Please select a resume.');

      return;

    }

    this.uploading = true;

    this.uploadService
      .uploadResume(
        this.profileId,
        this.selectedFile
      )
      .subscribe({

        next: () => {

          this.uploading = false;

          alert('Resume uploaded successfully.');

          this.selectedFile = undefined;

          this.loadResume();

        },

        error: () => {

          this.uploading = false;

          alert('Resume upload failed.');

        }

      });

  }

  // =====================================
  // Delete Resume
  // =====================================

  deleteResume() {

    if (!confirm('Delete uploaded resume?')) {
      return;
    }

    this.uploadService
      .deleteByUserProfileId(this.profileId)
      .subscribe(() => {

        alert('Resume deleted successfully.');

        this.uploadedResume = undefined;

        this.resumeExists = false;

        this.selectedFile = undefined;

        this.loadResume();

      });

  }

  // =====================================
  // File Selected
  // =====================================

  onFileSelected(event: any) {

    const file = event.target.files[0];

    if (!file) {
      return;
    }

    this.selectedFile = file;

  }

  // =====================================
  // View Uploaded Resume
  // =====================================

  openUploadedResume() {

    if (!this.uploadedResume) {
      return;
    }

    window.open(

      this.fileService.getResume(
        this.uploadedResume.fileName
      ),

      '_blank'

    );

  }

  // =====================================
  // Download Uploaded Resume
  // =====================================

  downloadUploadedResume() {

    this.openUploadedResume();

  }

  // =====================================
  // Angular Resume Preview
  // =====================================

  previewAngularResume() {

    this.router.navigate([
      '/user/resume-preview'
    ]);

  }

  // =====================================
  // Backend Resume PDF
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
  // Resume Import Preview
  // =====================================

  openResumeImportPreview() {

    this.router.navigate([
      '/user/resume-import-preview',
      this.profileId
    ]);

  }




}
