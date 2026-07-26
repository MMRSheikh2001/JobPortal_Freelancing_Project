import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReportStatus } from '../../enums/report-status.enum';
import { ReportFilterRequestDTO, ReportResponseDTO } from '../models/report.model';
import { UserRole } from '../../enums/user-role.enum';
import { ReportType } from '../../enums/report-type.enum';
import { ReportService } from '../services/report.service';
import { ToastService } from '../../services/toast.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-report-management.component',
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-report-management.component.html',
  styleUrl: './admin-report-management.component.css',
})
export class AdminReportManagementComponent implements OnInit {




  //-----------------------------------
  // Data
  //-----------------------------------

  loading = false;

  reports: ReportResponseDTO[] = [];

  //-----------------------------------
  // Filter
  //-----------------------------------

  filter: ReportFilterRequestDTO = {

    keyword: '',

    userId: 0,

    userRole: undefined,

    type: undefined,

    status: undefined,

    createdFrom: '',

    createdTo: ''

  };

  reportTypes = Object.values(
    ReportType
  );

  reportStatuses = Object.values(
    ReportStatus
  );

  userRoles = Object.values(
    UserRole
  );

  //-----------------------------------
  // Constructor
  //-----------------------------------

  constructor(

    private reportService: ReportService,

    private toast: ToastService,

    private router: Router,

    private cdr: ChangeDetectorRef

  ) { }

  //-----------------------------------
  // Init
  //-----------------------------------

  ngOnInit(): void {

    this.search();

  }

  //-----------------------------------
  // Search
  //-----------------------------------

  search(): void {

    this.loading = true;

    this.reportService
      .search(this.filter)
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
  // Clear Filter
  //-----------------------------------

  clearFilter(): void {

    this.filter = {

      keyword: '',

      userId: 0,

      userRole: undefined,

      type: undefined,

      status: undefined,

      createdFrom: '',

      createdTo: ''

    };

    this.search();

  }

  //-----------------------------------
  // View Details
  //-----------------------------------

  viewDetails(
    reportId: number
  ): void {

    this.router.navigate([
      '/admin/report-details',
      reportId
    ]);

  }

  //-----------------------------------
  // View User
  //-----------------------------------

  viewUser(
    report: ReportResponseDTO
  ): void {

    if (report.userRole === UserRole.USER) {

      this.router.navigate([
        '/admin/user-profile-review',
        report.profileId
      ]);

      return;

    }

    this.router.navigate([
      '/company-profile',
      report.profileId
    ]);

  }

  //-----------------------------------
  // Badge
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



}
