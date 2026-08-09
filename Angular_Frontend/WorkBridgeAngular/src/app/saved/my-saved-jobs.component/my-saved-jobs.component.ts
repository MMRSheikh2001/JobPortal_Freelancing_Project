import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SavedJobResponseDTO } from '../models/saved-job.model';
import { SavedJobService } from '../services/saved-job.service';
import { StorageService } from '../../auth/services/storage.service';
import { ToastService } from '../../services/toast.service';
import { Router } from '@angular/router';
import { FileResourceHandleService } from '../../services/file-resource-handle.service';

@Component({
  selector: 'app-my-saved-jobs.component',
  imports: [CommonModule, FormsModule],
  templateUrl: './my-saved-jobs.component.html',
  styleUrl: './my-saved-jobs.component.css',
})
export class MySavedJobsComponent implements OnInit {






  // =====================================
  // Properties
  // =====================================

  loading = false;

  userId = 0;

  search = '';

  savedJobs: SavedJobResponseDTO[] = [];

  filteredJobs: SavedJobResponseDTO[] = [];

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private savedJobService: SavedJobService,

    private storage: StorageService,

    private toast: ToastService,

    private router: Router,

    public fileService: FileResourceHandleService,

    private cdr: ChangeDetectorRef

  ) { }

  // =====================================
  // Init
  // =====================================

  ngOnInit(): void {

    this.userId =
      this.storage.getUserId() ?? 0;

    this.load();

  }

  // =====================================
  // Load
  // =====================================

  load(): void {

    this.loading = true;

    this.savedJobService
      .getSavedJobs(this.userId)
      .subscribe({

        next: res => {

          this.savedJobs = res;

          this.filteredJobs = [...res];

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load saved jobs.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Filter
  // =====================================

  filter(): void {

    const keyword =
      this.search.trim().toLowerCase();

    if (!keyword) {

      this.filteredJobs = [...this.savedJobs];

      return;

    }

    this.filteredJobs =
      this.savedJobs.filter(j =>

        j.jobTitle.toLowerCase().includes(keyword) ||

        j.companyName.toLowerCase().includes(keyword)

      );

  }

  // =====================================
  // Remove
  // =====================================

  remove(item: SavedJobResponseDTO): void {

    if (!confirm('Remove this saved job?')) {

      return;

    }

    this.savedJobService
      .unsaveJob(
        item.userId,
        item.jobId
      )
      .subscribe({

        next: () => {

          this.toast.show(
            'Removed from saved jobs.'
          );

          this.load();

        },

        error: () => {

          this.toast.show(
            'Unable to remove saved job.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // View Job
  // =====================================

  view(item: SavedJobResponseDTO): void {

    this.router.navigate([
      '/job-details',
      item.jobId
    ]);

  }




}
