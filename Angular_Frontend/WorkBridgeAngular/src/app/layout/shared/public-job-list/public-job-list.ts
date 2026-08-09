import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { JobResponseModel, JobSearchRequestModel } from '../../../company/company-profile/models/job.model';
import { CountryResponseModel } from '../../../admin/address/models/country.model';
import { DivisionResponseModel } from '../../../admin/address/models/division.model';
import { DistrictResponseModel } from '../../../admin/address/models/district.model';
import { PoliceStationResponseModel } from '../../../admin/address/models/police-station.model';
import { CategoryResponseModel } from '../../../admin/cvinformations/models/category.model';
import { JobService } from '../../../company/company-profile/services/job.service';
import { CategoryService } from '../../../admin/cvinformations/services/category.service';
import { CountryService } from '../../../admin/address/services/country.service';
import { DivisionService } from '../../../admin/address/services/division.service';
import { DistrictService } from '../../../admin/address/services/district.service';
import { PoliceStationService } from '../../../admin/address/services/police-station.service';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { WorkPlaceType } from '../../../enums/work-place-type.enum';
import { EmploymentType } from '../../../enums/employment-type.enum';

@Component({
  selector: 'app-public-job-list',
  imports: [CommonModule, FormsModule,  RouterModule],
  templateUrl: './public-job-list.html',
  styleUrl: './public-job-list.css',
})
export class PublicJobList implements OnInit {



  EmploymentType = EmploymentType;
  WorkPlaceType = WorkPlaceType;
  // ==========================
  // Lists
  // ==========================

  jobs: JobResponseModel[] = [];

  countries: CountryResponseModel[] = [];
  divisions: DivisionResponseModel[] = [];
  districts: DistrictResponseModel[] = [];
  policeStations: PoliceStationResponseModel[] = [];
  categories: CategoryResponseModel[] = [];

  // ==========================
  // Search Model
  // ==========================

  searchModel: JobSearchRequestModel = {

    keyword: '',

    categoryId: 0,

    countryId: 0,

    divisionId: 0,

    districtId: 0,

    policeStationId: 0,

    employmentType: null as any,

    workPlaceType: null as any,

    minSalary: 0,

    maxSalary: 0,

    active: true

  };

  // ==========================
  // Constructor
  // ==========================

  constructor(

    private jobService: JobService,

    private categoryService: CategoryService,

    private countryService: CountryService,

    private divisionService: DivisionService,

    private districtService: DistrictService,

    private policeStationService: PoliceStationService,
    private cdr: ChangeDetectorRef

  ) { }

  // ==========================
  // Init
  // ==========================

  ngOnInit(): void {

    this.loadJobs();

    this.loadCountries();

    this.loadCategories();

  }

  // ==========================
  // Load Jobs
  // ==========================

  loadJobs(): void {

    this.jobService.getActiveJobs().subscribe({

      next: (res) => {

        this.jobs = res;
        this.cdr.markForCheck();

      }

    });

  }

  // ==========================
  // Load Countries
  // ==========================

  loadCountries(): void {

    this.countryService.getAll().subscribe({

      next: (res) => {

        this.countries = res;
        this.cdr.markForCheck();

      }

    });

  }

  // ==========================
  // Load Categories
  // ==========================

  loadCategories(): void {

    this.categoryService.getAll().subscribe({

      next: (res) => {

        this.categories = res;
        this.cdr.markForCheck();

      }

    });

  }

  // ==========================
  // Country Changed
  // ==========================

  onCountryChange(): void {

    this.searchModel.divisionId = 0;
    this.searchModel.districtId = 0;
    this.searchModel.policeStationId = 0;

    this.divisions = [];
    this.districts = [];
    this.policeStations = [];

    if (!this.searchModel.countryId) {
      return;
    }

    this.divisionService
      .getByCountryId(this.searchModel.countryId)
      .subscribe({

        next: (res) => {

          this.divisions = res;

        }

      });

  }

  // ==========================
  // Division Changed
  // ==========================

  onDivisionChange(): void {

    this.searchModel.districtId = 0;
    this.searchModel.policeStationId = 0;

    this.districts = [];
    this.policeStations = [];

    if (!this.searchModel.divisionId) {
      return;
    }

    this.districtService
      .getByDivisionId(this.searchModel.divisionId)
      .subscribe({

        next: (res) => {

          this.districts = res;

        }

      });

  }

  // ==========================
  // District Changed
  // ==========================

  onDistrictChange(): void {

    this.searchModel.policeStationId = 0;

    this.policeStations = [];

    if (!this.searchModel.districtId) {
      return;
    }

    this.policeStationService
      .getByDistrictId(this.searchModel.districtId)
      .subscribe({

        next: (res) => {

          this.policeStations = res;

        }

      });

  }



  // ==========================
  // Search Jobs
  // ==========================

  searchJobs(): void {

    this.jobService.search(this.searchModel).subscribe({

      next: (res) => {

        this.jobs = res;
        this.cdr.markForCheck();

      }

    });

  }

  // ==========================
  // Reset Search
  // ==========================

  resetSearch(): void {

    this.searchModel = {

      keyword: '',

      categoryId: 0,

      countryId: 0,

      divisionId: 0,

      districtId: 0,

      policeStationId: 0,

      employmentType: null as any,

      workPlaceType: null as any,

      minSalary: 0,

      maxSalary: 0,

      active: true

    };

    this.divisions = [];
    this.districts = [];
    this.policeStations = [];

    this.loadJobs();

  }

}



