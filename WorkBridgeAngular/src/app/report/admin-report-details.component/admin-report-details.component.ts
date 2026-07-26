import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReportStatus } from '../../enums/report-status.enum';
import { UserRole } from '../../enums/user-role.enum';
import { FileResourceHandleService } from '../../services/file-resource-handle.service';
import { ToastService } from '../../services/toast.service';
import { ReportService } from '../services/report.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ReportResponseDTO } from '../models/report.model';

@Component({
  selector: 'app-admin-report-details.component',
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-report-details.component.html',
  styleUrl: './admin-report-details.component.css',
})
export class AdminReportDetailsComponent implements OnInit {





  //-----------------------------------
  // Data
  //-----------------------------------

  loading = false;

  reportId = 0;

  report?: ReportResponseDTO;

  adminReply = '';

  //-----------------------------------
  // Constructor
  //-----------------------------------

  constructor(

    private route: ActivatedRoute,

    private router: Router,

    private reportService: ReportService,

    private toast: ToastService,

    public fileService: FileResourceHandleService,

    private cdr: ChangeDetectorRef

  ) { }

  //-----------------------------------
  // Init
  //-----------------------------------

  ngOnInit(): void {

    this.reportId = Number(

      this.route.snapshot.paramMap.get(
        'reportId'
      )

    );

    this.loadReport();

  }

  //-----------------------------------
  // Load Report
  //-----------------------------------

  loadReport(): void {

    this.loading = true;

    this.reportService
      .getById(this.reportId)
      .subscribe({

        next: (data) => {

          this.report = data;

          this.adminReply =
            data.adminReply ?? '';

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load report.',
            'danger'
          );

        }

      });

  }

  //-----------------------------------
  // Resolve
  //-----------------------------------

  resolve(): void {

    if (!this.adminReply.trim()) {

      this.toast.show(
        'Admin reply is required.',
        'warning'
      );

      return;

    }

    this.reportService
      .resolveReport(
        this.reportId,
        this.adminReply
      )
      .subscribe({

        next: (data) => {

          this.report = data;

          this.toast.show(
            'Report resolved successfully.'
          );

          this.cdr.markForCheck();

        },

        error: () => {

          this.toast.show(
            'Unable to resolve report.',
            'danger'
          );

        }

      });

  }

  //-----------------------------------
  // Reject
  //-----------------------------------

  reject(): void {

    if (!this.adminReply.trim()) {

      this.toast.show(
        'Admin reply is required.',
        'warning'
      );

      return;

    }

    this.reportService
      .rejectReport(
        this.reportId,
        this.adminReply
      )
      .subscribe({

        next: (data) => {

          this.report = data;

          this.toast.show(
            'Report rejected successfully.'
          );

          this.cdr.markForCheck();

        },

        error: () => {

          this.toast.show(
            'Unable to reject report.',
            'danger'
          );

        }

      });

  }

  //-----------------------------------
  // View Profile
  //-----------------------------------

  viewProfile(): void {

    if (!this.report) return;

    if (this.report.userRole === UserRole.USER) {

      this.router.navigate([
        '/admin/user-profile-review',
        this.report.profileId
      ]);

      return;

    }

    this.router.navigate([
      '/company-profile',
      this.report.profileId
    ]);

  }

  //-----------------------------------
  // Attachment
  //-----------------------------------

  getAttachment(): string {

    return this.fileService.getReportFile(
      this.report?.attachmentUrl
    );

  }

  //-----------------------------------
  // Status Badge
  //-----------------------------------

  getStatusClass(
    status?: ReportStatus
  ): string {

    switch (status) {

      case ReportStatus.OPEN:
        return 'bg-warning text-dark';

      case ReportStatus.RESOLVED:
        return 'bg-success';

      case ReportStatus.REJECTED:
        return 'bg-danger';

      default:
        return 'bg-secondary';

    }

  }




}
