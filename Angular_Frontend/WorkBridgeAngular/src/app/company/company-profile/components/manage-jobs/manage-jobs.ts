import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { JobRequestModel } from '../../models/job.model';
import { EmploymentType } from '../../../../enums/employment-type.enum';
import { WorkPlaceType } from '../../../../enums/work-place-type.enum';
import { CategoryResponseModel } from '../../../../admin/cvinformations/models/category.model';
import { CountryResponseModel } from '../../../../admin/address/models/country.model';
import { DivisionResponseModel } from '../../../../admin/address/models/division.model';
import { DistrictResponseModel } from '../../../../admin/address/models/district.model';
import { PoliceStationResponseModel } from '../../../../admin/address/models/police-station.model';
import { JobService } from '../../services/job.service';
import { CategoryService } from '../../../../admin/cvinformations/services/category.service';
import { CountryService } from '../../../../admin/address/services/country.service';
import { DivisionService } from '../../../../admin/address/services/division.service';
import { DistrictService } from '../../../../admin/address/services/district.service';
import { PoliceStationService } from '../../../../admin/address/services/police-station.service';
import { StorageService } from '../../../../auth/services/storage.service';
import { ToastService } from '../../../../services/toast.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-manage-jobs',
  imports: [CommonModule, FormsModule],
  templateUrl: './manage-jobs.html',
  styleUrl: './manage-jobs.css',
})
export class ManageJobs implements OnInit {





  // =====================================
  // IDs
  // =====================================

  companyProfileId = 0;

  jobId = 0;

  editMode = false;

  today = new Date().toISOString().split('T')[0];

  originalJob?: JobRequestModel;

  // =====================================
  // Model
  // =====================================
  job: JobRequestModel = this.createEmptyJob();

  // =====================================
  // Dropdown Lists
  // =====================================

  categories: CategoryResponseModel[] = [];

  countries: CountryResponseModel[] = [];

  divisions: DivisionResponseModel[] = [];

  districts: DistrictResponseModel[] = [];

  policeStations: PoliceStationResponseModel[] = [];

  // =====================================
  // Selected Address
  // =====================================

  countryId = 0;

  divisionId = 0;

  districtId = 0;

  // =====================================
  // Enums
  // =====================================

  employmentTypes = Object.values(EmploymentType);

  workPlaceTypes = Object.values(WorkPlaceType);

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private jobService: JobService,

    private categoryService: CategoryService,

    private countryService: CountryService,

    private divisionService: DivisionService,

    private districtService: DistrictService,

    private policeStationService: PoliceStationService,

    private storage: StorageService,

    private toast: ToastService,

    private route: ActivatedRoute,

    private router: Router,

    private cdr: ChangeDetectorRef

  ) { }

  // =====================================
  // Init
  // =====================================

  ngOnInit(): void {

    this.companyProfileId =
      this.storage.getProfileId() ?? 0;

    this.job.companyProfileId =
      this.companyProfileId;

    this.loadCategories();

    this.loadCountries();

    const id =
      Number(this.route.snapshot.paramMap.get('id'));

    if (id) {

      this.editMode = true;

      this.jobId = id;

      this.loadJob();

    }

  }

  // =====================================
  // Load Job
  // =====================================

  loadJob() {

    this.jobService
      .getById(this.jobId)
      .subscribe(data => {

        this.job = {

          title: data.title,

          jobDescription: data.jobDescription,

          jobResponsibilities: data.jobResponsibilities,

          educationalRequirements:
            data.educationalRequirements,

          experienceRequirements:
            data.experienceRequirements,

          minExperience: data.minExperience,

          maxExperience: data.maxExperience,

          additionalRequirements:
            data.additionalRequirements,

          benefits: data.benefits,

          salaryMin: data.salaryMin,

          salaryMax: data.salaryMax,

          isNegotiable: data.isNegotiable,

          applicationDeadline:
            data.applicationDeadline,

          vacancy: data.vacancy,

          employmentType:
            data.employmentType,

          workPlaceType:
            data.workPlaceType,

          companyProfileId:
            data.companyProfileId,

          locationPoliceStationId:
            data.locationPoliceStationId,

          categoryId:
            data.categoryId,

          aiScreeningEnabled:
            data.aiScreeningEnabled,

          aiCvScreeningEnabled:
            data.aiCvScreeningEnabled,

          aiInterviewEnabled:
            data.aiInterviewEnabled,

          aiMatchThreshold:
            data.aiMatchThreshold,

          aiQuestionCount:
            data.aiQuestionCount,

          aiShortlistCount:
            data.aiShortlistCount,

          aiDeadlineDays:
            data.aiDeadlineDays

        };
        this.originalJob = JSON.parse(JSON.stringify(this.job));

        this.countryId =
          data.locationCountryId;

        this.divisionId =
          data.locationDivisionId;

        this.districtId =
          data.locationDistrictId;

        this.loadDivisions();

        this.cdr.markForCheck();

      });

  }

  // =====================================
  // Save
  // =====================================

  save() {

    if (this.editMode) {

      if (this.job.maxExperience < this.job.minExperience) {
        this.toast.show('Maximum experience cannot be less than minimum experience.');
        return;
      }

      if (
        !this.job.isNegotiable &&
        this.job.salaryMax < this.job.salaryMin
      ) {
        this.toast.show('Maximum salary cannot be less than minimum salary.');
        return;
      }
      this.job.title = this.job.title.trim();

      if (!this.job.title) {
        this.toast.show('Job title is required.');
        return;
      }

      this.jobService
        .update(this.jobId, this.job)
        .subscribe(() => {

          this.toast.show(
            'Job Updated Successfully.'
          );

          this.router.navigate([
            '/company/dashboard'
          ]);

        });

    } else {

      if (this.job.maxExperience < this.job.minExperience) {
        this.toast.show('Maximum experience cannot be less than minimum experience.');
        return;
      }

      if (
        !this.job.isNegotiable &&
        this.job.salaryMax < this.job.salaryMin
      ) {
        this.toast.show('Maximum salary cannot be less than minimum salary.');
        return;
      }
      this.job.title = this.job.title.trim();

      if (!this.job.title) {
        this.toast.show('Job title is required.');
        return;
      }

      this.jobService
        .save(this.job)
        .subscribe(() => {

          this.toast.show(
            'Job Created Successfully.'
          );

          this.router.navigate([
            '/company/dashboard'
          ]);

        });

    }

  }

  // =====================================
  // Categories
  // =====================================

  loadCategories() {

    this.categoryService
      .getAll()
      .subscribe(data => {

        this.categories = data;
        this.cdr.markForCheck();

      });

  }

  // =====================================
  // Countries
  // =====================================

  loadCountries() {

    this.countryService
      .getAll()
      .subscribe(data => {

        this.countries = data;
        this.cdr.markForCheck();

      });

  }

  // =====================================
  // Divisions
  // =====================================

  loadDivisions() {

    if (!this.countryId) {

      this.divisions = [];

      return;

    }

    this.divisionService
      .getByCountryId(this.countryId)
      .subscribe(data => {

        this.divisions = data;
        this.cdr.markForCheck();

        if (this.divisionId) {

          this.loadDistricts();
          this.cdr.markForCheck();

        }

      });

  }

  // =====================================
  // Districts
  // =====================================

  loadDistricts() {

    if (!this.divisionId) {

      this.districts = [];

      return;

    }

    this.districtService
      .getByDivisionId(this.divisionId)
      .subscribe(data => {

        this.districts = data;
        this.cdr.markForCheck();

        if (this.districtId) {

          this.loadPoliceStations();
          this.cdr.markForCheck();

        }

      });

  }

  // =====================================
  // Police Stations
  // =====================================

  loadPoliceStations() {

    if (!this.districtId) {

      this.policeStations = [];

      return;

    }

    this.policeStationService
      .getByDistrictId(this.districtId)
      .subscribe(data => {

        this.policeStations = data;
        this.cdr.markForCheck();

      });

  }

  // =====================================
  // Dropdown Events
  // =====================================

  onCountryChange() {

    this.divisionId = 0;

    this.districtId = 0;

    this.job.locationPoliceStationId = 0;

    this.loadDivisions();

  }

  onDivisionChange() {

    this.districtId = 0;

    this.job.locationPoliceStationId = 0;

    this.loadDistricts();

  }

  onDistrictChange() {

    this.job.locationPoliceStationId = 0;

    this.loadPoliceStations();

  }


  private createEmptyJob(): JobRequestModel {

    return {

      title: '',

      jobDescription: '',

      jobResponsibilities: '',

      educationalRequirements: '',

      experienceRequirements: '',

      minExperience: 0,

      maxExperience: 0,

      additionalRequirements: '',

      benefits: '',

      salaryMin: 0,

      salaryMax: 0,

      isNegotiable: false,

      applicationDeadline: '',

      vacancy: 1,

      employmentType: EmploymentType.Full_Time,

      workPlaceType: WorkPlaceType.ONSITE,

      companyProfileId: this.companyProfileId,

      locationPoliceStationId: 0,

      categoryId: 0,

      aiScreeningEnabled: false,

      aiCvScreeningEnabled: false,

      aiInterviewEnabled: false,

      aiMatchThreshold: 70,

      aiQuestionCount: 10,

      aiShortlistCount: 10,

      aiDeadlineDays: 7

    };

  }
  resetForm() {

    if (this.editMode) {

      if (this.originalJob) {

        this.job = JSON.parse(JSON.stringify(this.originalJob));

        // Reload the job to restore address dropdowns correctly
        this.loadJob();

      }

    } else {

      this.job = this.createEmptyJob();

      this.countryId = 0;
      this.divisionId = 0;
      this.districtId = 0;

      this.divisions = [];
      this.districts = [];
      this.policeStations = [];

    }

  }

}
