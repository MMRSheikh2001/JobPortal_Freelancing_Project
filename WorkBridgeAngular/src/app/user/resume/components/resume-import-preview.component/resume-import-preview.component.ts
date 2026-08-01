import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ResumeImportService } from '../../services/resume-import.service';
import { ResumeImportPreviewDTO } from '../../models/resume-preview.model';

@Component({
  selector: 'app-resume-import-preview.component',
  imports: [CommonModule],
  templateUrl: './resume-import-preview.component.html',
  styleUrl: './resume-import-preview.component.css',
})
export class ResumeImportPreviewComponent implements OnInit {






  // =====================================
  // IDs
  // =====================================

  userProfileId = 0;

  // =====================================
  // Preview
  // =====================================

  preview?: ResumeImportPreviewDTO;

  // =====================================
  // State
  // =====================================

  loading = true;

  importing = false;

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private route: ActivatedRoute,

    private router: Router
    ,

    private resumeImportService: ResumeImportService,

    private cdr: ChangeDetectorRef

  ) { }

  // =====================================
  // Init
  // =====================================

  ngOnInit(): void {

    this.userProfileId = Number(

      this.route.snapshot.paramMap.get('userProfileId')

    );

    this.loadPreview();

  }

  // =====================================
  // Load AI Preview
  // =====================================

  loadPreview(): void {

    this.loading = true;

    this.resumeImportService
      .getPreviewFromResume(this.userProfileId)
      .subscribe({

        next: data => {

          this.preview = data;

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          alert('Failed to generate AI preview.');

        }

      });

  }

  // =====================================
  // Import Information
  // =====================================

  importInformation(): void {

    if (!this.preview) {
      alert('Nothing to import.');
      return;
    }

    this.importing = true;

    this.resumeImportService
      .saveImportedResume(
        this.userProfileId,
        this.preview
      )
      .subscribe({

        next: () => {

          this.importing = false;

          alert('Resume imported successfully.');

          this.router.navigate([
            '/user/user-profile-preview'
          ]);

        },

        error: () => {

          this.importing = false;

          alert('Failed to import resume.');

        }

      });

  }

  // =====================================
  // Cancel
  // =====================================

  cancel(): void {

    this.router.navigate([
      '/user/resume-control'
    ]);

  }





}
