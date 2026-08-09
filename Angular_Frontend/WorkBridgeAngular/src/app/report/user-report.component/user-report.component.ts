import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReportStatus } from '../../enums/report-status.enum';
import { FileResourceHandleService } from '../../services/file-resource-handle.service';
import { ToastService } from '../../services/toast.service';
import { StorageService } from '../../auth/services/storage.service';
import { ReportService } from '../services/report.service';
import { ReportType } from '../../enums/report-type.enum';
import { ReportResponseDTO } from '../models/report.model';

@Component({
  selector: 'app-user-report.component',
  imports: [CommonModule, FormsModule],
  templateUrl: './user-report.component.html',
  styleUrl: './user-report.component.css',
})
export class UserReportComponent implements OnInit {







  //-----------------------------------
  // Data
  //-----------------------------------

  loading = false;

  reports: ReportResponseDTO[] = [];

  userId = 0;


  @ViewChild('fileInput')
  fileInput!: ElementRef<HTMLInputElement>;
  //-----------------------------------
  // Form
  //-----------------------------------

  subject = '';

  description = '';

  type: ReportType = ReportType.OTHER;

  attachment?: File;

  //-----------------------------------
  // Enums
  //-----------------------------------

  reportTypes = Object.values(ReportType);

  //-----------------------------------
  // Constructor
  //-----------------------------------

  constructor(
    private reportService: ReportService,
    private storage: StorageService,
    private toast: ToastService,
    public fileService: FileResourceHandleService,
    private cdr: ChangeDetectorRef
  ) { }

  //-----------------------------------
  // Init
  //-----------------------------------

  ngOnInit(): void {

    this.userId =
      this.storage.getUserId() ?? 0;

    this.loadReports();

  }

  //-----------------------------------
  // Load Reports
  //-----------------------------------

  loadReports(): void {

    this.loading = true;

    this.reportService
      .getByUserId(this.userId)
      .subscribe({

        next: (data) => {

          this.reports = data;

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load reports.',
            'danger'
          );

        }

      });

  }

  //-----------------------------------
  // File Selected
  //-----------------------------------

  onFileSelected(event: any): void {

    if (event.target.files.length > 0) {

      this.attachment =
        event.target.files[0];

    }

  }

  //-----------------------------------
  // Submit
  //-----------------------------------

  submit(): void {

    if (!this.subject.trim()) {

      this.toast.show(
        'Subject is required.',
        'warning'
      );

      return;

    }

    if (!this.description.trim()) {

      this.toast.show(
        'Description is required.',
        'warning'
      );

      return;

    }

    this.reportService
      .createReport(
        this.userId,
        this.subject,
        this.description,
        this.type,
        this.attachment
      )
      .subscribe({

        next: (report) => {

          this.toast.show(
            'Report submitted successfully.'
          );

          this.resetForm();


          this.reports.unshift(report);

          this.cdr.markForCheck();

        },

        error: () => {

          this.toast.show(
            'Unable to submit report.',
            'danger'
          );

        }

      });

  }

  //-----------------------------------
  // Attachment
  //-----------------------------------

  getAttachment(
    fileName?: string
  ): string {

    return this.fileService.getReportFile(
      fileName
    );

  }

  //-----------------------------------
  // Status Badge
  //-----------------------------------

  getStatusClass(
    status: ReportStatus
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



  resetForm(): void {

    this.subject = '';

    this.description = '';

    this.type = ReportType.OTHER;

    this.attachment = undefined;

    if (this.fileInput) {
      this.fileInput.nativeElement.value = '';
    }

  }



}
