import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UserProfileResponseModel } from '../../user/resume/models/user.profile.model';
import { CountryResponseModel } from '../../admin/address/models/country.model';
import { DivisionResponseModel } from '../../admin/address/models/division.model';
import { DistrictResponseModel } from '../../admin/address/models/district.model';
import { PoliceStationResponseModel } from '../../admin/address/models/police-station.model';
import { Gender } from '../../enums/gender.enum';
import { JobType } from '../../enums/job-type.enum';
import { WorkPlaceType } from '../../enums/work-place-type.enum';
import { UserProfileService } from '../../user/resume/services/user.profile.service';
import { CountryService } from '../../admin/address/services/country.service';
import { DivisionService } from '../../admin/address/services/division.service';
import { DistrictService } from '../../admin/address/services/district.service';
import { PoliceStationService } from '../../admin/address/services/police-station.service';
import { ToastService } from '../../services/toast.service';
import { Router } from '@angular/router';
import { FileResourceHandleService } from '../../services/file-resource-handle.service';

@Component({
  selector: 'app-admin-user-profile-management',
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-user-profile-management.html',
  styleUrl: './admin-user-profile-management.css',
})
export class AdminUserProfileManagement implements OnInit {





  //=====================================
  // Properties
  //=====================================

  loading = false;

  userProfiles: UserProfileResponseModel[] = [];

  countries: CountryResponseModel[] = [];

  divisions: DivisionResponseModel[] = [];

  districts: DistrictResponseModel[] = [];

  policeStations: PoliceStationResponseModel[] = [];

  genders = Object.values(Gender);

  jobTypes = Object.values(JobType);

  workPlaceTypes = Object.values(WorkPlaceType);

  filter = {

    keyword: '',

    countryId: 0,

    divisionId: 0,

    districtId: 0,

    policeStationId: 0,

    gender: undefined as Gender | undefined,

    jobType: undefined as JobType | undefined,

    workPlaceType: undefined as WorkPlaceType | undefined

  };

  //=====================================
  // Constructor
  //=====================================

  constructor(

    private userProfileService: UserProfileService,

    private countryService: CountryService,

    private divisionService: DivisionService,

    private districtService: DistrictService,

    private policeStationService: PoliceStationService,

    private toast: ToastService,

    private router: Router,

    public fileService: FileResourceHandleService,

    private cdr: ChangeDetectorRef

  ) { }

  //=====================================
  // Init
  //=====================================

  ngOnInit(): void {

    this.loadCountries();

    this.search();

  }

  //=====================================
  // Load Countries
  //=====================================

  loadCountries(): void {

    this.countryService
      .getAll()
      .subscribe({

        next: res => {

          this.countries = res;

        }

      });

  }

  //=====================================
  // Country Changed
  //=====================================

  countryChanged(): void {

    this.filter.divisionId = 0;

    this.filter.districtId = 0;

    this.filter.policeStationId = 0;

    this.divisions = [];

    this.districts = [];

    this.policeStations = [];

    if ((this.filter.countryId ?? 0) > 0) {

      this.divisionService
        .getByCountryId(this.filter.countryId!)
        .subscribe({

          next: res => {

            this.divisions = res;

          }

        });

    }

  }

  //=====================================
  // Division Changed
  //=====================================

  divisionChanged(): void {

    this.filter.districtId = 0;

    this.filter.policeStationId = 0;

    this.districts = [];

    this.policeStations = [];

    if ((this.filter.divisionId ?? 0) > 0) {

      this.districtService
        .getByDivisionId(this.filter.divisionId!)
        .subscribe({

          next: res => {

            this.districts = res;

          }

        });

    }

  }

  //=====================================
  // District Changed
  //=====================================

  districtChanged(): void {

    this.filter.policeStationId = 0;

    this.policeStations = [];

    if ((this.filter.districtId ?? 0) > 0) {

      this.policeStationService
        .getByDistrictId(this.filter.districtId!)
        .subscribe({

          next: res => {

            this.policeStations = res;

          }

        });

    }

  }

  //=====================================
  // Search
  //=====================================

  search(): void {

    this.loading = true;

    this.userProfileService
      .filterUsers(

        this.filter.keyword,

        this.filter.countryId || undefined,

        this.filter.divisionId || undefined,

        this.filter.districtId || undefined,

        this.filter.policeStationId || undefined,

        this.filter.jobType,

        this.filter.workPlaceType,

        this.filter.gender

      )
      .subscribe({

        next: res => {

          this.userProfiles = res;

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load user profiles.',
            'danger'
          );

        }

      });

  }


  //=====================================
  // Reset Filters
  //=====================================

  reset(): void {

    this.filter = {

      keyword: '',

      countryId: 0,

      divisionId: 0,

      districtId: 0,

      policeStationId: 0,

      gender: undefined,

      jobType: undefined,

      workPlaceType: undefined

    };

    this.divisions = [];

    this.districts = [];

    this.policeStations = [];

    this.search();

  }

  //=====================================
  // View Profile
  //=====================================

  viewProfile(
    userProfileId: number
  ): void {

    this.router.navigate([
      '/admin/user-profile-review',
      userProfileId
    ]);

  }

  //=====================================
  // Delete User Profile
  //=====================================

  delete(
    profile: UserProfileResponseModel
  ): void {

    if (!confirm(`Delete ${profile.name}'s profile?`)) {

      return;

    }

    this.userProfileService
      .delete(profile.id)
      .subscribe({

        next: () => {

          this.toast.show(
            'User profile deleted successfully.',
            'success'
          );

          this.userProfiles =
            this.userProfiles.filter(
              x => x.id !== profile.id
            );

          this.cdr.markForCheck();

        },

        error: () => {

          this.toast.show(
            'Unable to delete profile.',
            'danger'
          );

        }

      });

  }

  //=====================================
  // Profile Image
  //=====================================

  getProfileImage(
    image?: string
  ): string {

    return this.fileService.getUserProfileImage(
      image
    );

  }

  //=====================================
  // Helpers
  //=====================================

  getGenderBadge(
    gender: Gender
  ): string {

    switch (gender) {

      case Gender.MALE:
        return 'bg-primary';

      case Gender.FEMALE:
        return 'bg-danger';

      default:
        return 'bg-secondary';

    }

  }

  getJobTypeBadge(
    jobType: JobType
  ): string {

    switch (jobType) {

      case JobType.FULL_TIME:
        return 'bg-success';

      case JobType.PART_TIME:
        return 'bg-warning';

      case JobType.INTERNSHIP:
        return 'bg-info';

      case JobType.CONTRACT:
        return 'bg-primary';

      case JobType.FREELANCE:
        return 'bg-dark';

      case JobType.REMOTE:
        return 'bg-secondary';

      case JobType.TEMPORARY:
        return 'bg-warning';

      case JobType.VOLUNTEER:
        return 'bg-success';

      default:
        return 'bg-secondary';

    }

  }

  getWorkplaceBadge(
    workplace: WorkPlaceType
  ): string {

    switch (workplace) {

      case WorkPlaceType.ONSITE:
        return 'bg-primary';

      case WorkPlaceType.REMOTE:
        return 'bg-success';

      case WorkPlaceType.HYBRID:
        return 'bg-warning';

      default:
        return 'bg-secondary';

    }

  }





}
