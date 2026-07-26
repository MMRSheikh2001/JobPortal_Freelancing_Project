import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CompanyProfileResponseModel, CompanySearchRequestDTO } from '../../../company/company-profile/models/company-profile.model';
import { CountryResponseModel } from '../../../admin/address/models/country.model';
import { DivisionResponseModel } from '../../../admin/address/models/division.model';
import { DistrictResponseModel } from '../../../admin/address/models/district.model';
import { CompanyProfileService } from '../../../company/company-profile/services/company-profile.service';
import { CountryService } from '../../../admin/address/services/country.service';
import { DivisionService } from '../../../admin/address/services/division.service';
import { DistrictService } from '../../../admin/address/services/district.service';
import { ToastService } from '../../../services/toast.service';
import { Router } from '@angular/router';
import { FileResourceHandleService } from '../../../services/file-resource-handle.service';

@Component({
  selector: 'app-search-companies.component',
  imports: [CommonModule, FormsModule],
  templateUrl: './search-companies.component.html',
  styleUrl: './search-companies.component.css',
})
export class SearchCompaniesComponent implements OnInit {






  //=====================================
  // Properties
  //=====================================

  loading = false;

  companies: CompanyProfileResponseModel[] = [];

  countries: CountryResponseModel[] = [];

  divisions: DivisionResponseModel[] = [];

  districts: DistrictResponseModel[] = [];

  filter: CompanySearchRequestDTO = {

    keyword: '',

    industry: '',

    countryId: 0,

    divisionId: 0,

    districtId: 0

  };

  //=====================================
  // Constructor
  //=====================================

  constructor(

    private companyService: CompanyProfileService,

    private countryService: CountryService,

    private divisionService: DivisionService,

    private districtService: DistrictService,

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
  // Countries
  //=====================================

  loadCountries(): void {

    this.countryService.getAll().subscribe({

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

    this.divisions = [];

    this.districts = [];

    if (this.filter.countryId! > 0) {

      this.divisionService

        .getByCountryId(this.filter.countryId!)

        .subscribe({

          next: res => this.divisions = res

        });

    }

  }

  //=====================================
  // Division Changed
  //=====================================

  divisionChanged(): void {

    this.filter.districtId = 0;

    this.districts = [];

    if (this.filter.divisionId! > 0) {

      this.districtService

        .getByDivisionId(this.filter.divisionId!)

        .subscribe({

          next: res => this.districts = res

        });

    }

  }

  //=====================================
  // Search
  //=====================================

  search(): void {

    this.loading = true;

    this.companyService

      .filter(this.filter)

      .subscribe({

        next: res => {

          this.companies = res;

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(

            'Unable to load companies.',

            'danger'

          );

        }

      });

  }

  //=====================================
  // Reset
  //=====================================

  reset(): void {

    this.filter = {

      keyword: '',

      industry: '',

      countryId: 0,

      divisionId: 0,

      districtId: 0

    };

    this.divisions = [];

    this.districts = [];

    this.search();

  }

  //=====================================
  // View Company
  //=====================================

  openCompany(companyId: number): void {

    this.router.navigate([

      '/company-profile',

      companyId

    ]);

  }

  //=====================================
  // Image
  //=====================================

  getLogo(image?: string): string {

    return this.fileService.getCompanyProfileImage(image);

  }






}
