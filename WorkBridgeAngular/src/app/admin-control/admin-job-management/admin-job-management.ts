import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { JobResponseModel } from '../../company/company-profile/models/job.model';
import { JobService } from '../../company/company-profile/services/job.service';
import { CategoryService } from '../../admin/cvinformations/services/category.service';
import { CountryService } from '../../admin/address/services/country.service';
import { DivisionService } from '../../admin/address/services/division.service';
import { DistrictService } from '../../admin/address/services/district.service';
import { ToastService } from '../../services/toast.service';
import { Router } from '@angular/router';
import { CategoryResponseModel } from '../../admin/cvinformations/models/category.model';
import { CountryResponseModel } from '../../admin/address/models/country.model';
import { DivisionResponseModel } from '../../admin/address/models/division.model';
import { DistrictResponseModel } from '../../admin/address/models/district.model';

@Component({
  selector: 'app-admin-job-management',
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-job-management.html',
  styleUrl: './admin-job-management.css',
})
export class AdminJobManagement implements OnInit {





  //-----------------------------------
  // Data
  //-----------------------------------

  loading = false;

  jobs: JobResponseModel[] = [];

  filteredJobs: JobResponseModel[] = [];

  categories: CategoryResponseModel[] = [];

  countries: CountryResponseModel[] = [];

  divisions: DivisionResponseModel[] = [];

  districts: DistrictResponseModel[] = [];

  //-----------------------------------
  // Filter
  //-----------------------------------

  keyword = '';

  categoryId = 0;

  countryId = 0;

  divisionId = 0;

  districtId = 0;

  active = 'ALL';

  //-----------------------------------
  // Constructor
  //-----------------------------------

  constructor(

    private jobService: JobService,

    private categoryService: CategoryService,

    private countryService: CountryService,

    private divisionService: DivisionService,

    private districtService: DistrictService,

    private toast: ToastService,

    private router: Router,

    private cdr: ChangeDetectorRef

  ) { }

  //-----------------------------------
  // Init
  //-----------------------------------

  ngOnInit(): void {

    this.loadCategories();

    this.loadCountries();

    this.loadJobs();

  }

  //-----------------------------------
  // Load Categories
  //-----------------------------------

  loadCategories(): void {

    this.categoryService
      .getAll()
      .subscribe(res => this.categories = res);

  }

  //-----------------------------------
  // Load Countries
  //-----------------------------------

  loadCountries(): void {

    this.countryService
      .getAll()
      .subscribe(res => this.countries = res);

  }

  //-----------------------------------
  // Country Changed
  //-----------------------------------

  countryChanged(): void {

    this.divisionId = 0;

    this.districtId = 0;

    this.divisions = [];

    this.districts = [];

    if (this.countryId != 0) {

      this.divisionService
        .getByCountryId(this.countryId)
        .subscribe(res => this.divisions = res);

    }

    this.applyFilter();

  }

  //-----------------------------------
  // Division Changed
  //-----------------------------------

  divisionChanged(): void {

    this.districtId = 0;

    this.districts = [];

    if (this.divisionId != 0) {

      this.districtService
        .getByDivisionId(this.divisionId)
        .subscribe(res => this.districts = res);

    }

    this.applyFilter();

  }

  //-----------------------------------
  // Load Jobs
  //-----------------------------------

  loadJobs(): void {

    this.loading = true;

    this.jobService
      .getAll()
      .subscribe({

        next: res => {

          this.jobs = res;

          this.applyFilter();

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load jobs.',
            'danger'
          );

        }

      });

  }

  //-----------------------------------
  // Filter
  //-----------------------------------

  applyFilter(): void {

    this.filteredJobs = this.jobs.filter(job => {

      const keywordMatch =

        this.keyword == '' ||

        job.title.toLowerCase().includes(this.keyword.toLowerCase()) ||

        job.companyName.toLowerCase().includes(this.keyword.toLowerCase());

      const categoryMatch =

        this.categoryId == 0 ||

        job.categoryId == this.categoryId;

      const countryMatch =

        this.countryId == 0 ||

        job.locationCountryId == this.countryId;

      const divisionMatch =

        this.divisionId == 0 ||

        job.locationDivisionId == this.divisionId;

      const districtMatch =

        this.districtId == 0 ||

        job.locationDistrictId == this.districtId;

      const statusMatch =

        this.active == 'ALL' ||

        (this.active == 'ACTIVE' && job.isActive) ||

        (this.active == 'INACTIVE' && !job.isActive);

      return keywordMatch &&
        categoryMatch &&
        countryMatch &&
        divisionMatch &&
        districtMatch &&
        statusMatch;

    });

  }

  //-----------------------------------
  // View Job
  //-----------------------------------

  viewJob(id: number): void {

    this.router.navigate([
      '/job-details',
      id
    ]);

  }

  //-----------------------------------
  // View Company
  //-----------------------------------

  viewCompany(companyProfileId: number): void {

    this.router.navigate([
      '/company-profile',
      companyProfileId
    ]);

  }

  //-----------------------------------
  // Toggle Status
  //-----------------------------------

  toggleStatus(job: JobResponseModel): void {

    this.jobService
      .toggleStatus(job.id)
      .subscribe({

        next: updated => {

          job.isActive = updated.isActive;

          this.toast.show(

            updated.isActive
              ? 'Job Activated.'
              : 'Job Deactivated.'

          );

          this.applyFilter();

        },

        error: () => {

          this.toast.show(
            'Unable to change status.',
            'danger'
          );

        }

      });

  }

  //-----------------------------------
  // Delete
  //-----------------------------------

  deleteJob(job: JobResponseModel): void {

    if (!confirm(`Delete "${job.title}"?`)) {

      return;

    }

    this.jobService
      .delete(job.id)
      .subscribe({

        next: () => {

          this.toast.show(
            'Job deleted successfully.'
          );

          this.jobs = this.jobs.filter(
            x => x.id != job.id
          );

          this.applyFilter();

        },

        error: () => {

          this.toast.show(
            'Unable to delete job.',
            'danger'
          );

        }

      });

  }





}
