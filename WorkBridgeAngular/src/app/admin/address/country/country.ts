import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CountryService } from '../services/country.service';
import { CountryRequestModel, CountryResponseModel } from '../models/country.model';

@Component({
  selector: 'app-country',
  imports: [CommonModule, FormsModule],
  templateUrl: './country.html',
  styleUrl: './country.css',
})
export class Country implements OnInit {




  countries: CountryResponseModel[] = [];

  country: CountryRequestModel = {

    countryName: '',
    countryCode: ''

  };

  selectedCountryId = 0;

  isEdit = false;

  constructor(
    private service: CountryService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {

    this.loadCountries();

  }

  loadCountries() {

    this.service.getAll()
      .subscribe(data => {

        this.countries = data;

        this.cdr.markForCheck();

        console.log(data);

      });

  }

  save() {

    if (this.isEdit) {

      this.service.update(
        this.selectedCountryId,
        this.country
      )
        .subscribe(() => {

          alert("Updated Successfully");

          this.reset();

          this.loadCountries();

        });

    } else {

      this.service.save(this.country)
        .subscribe(() => {

          alert("Saved Successfully");

          this.reset();

          this.loadCountries();

        });

    }

  }

  edit(c: CountryResponseModel) {

    this.selectedCountryId = c.countryId;

    this.country = {

      countryName: c.countryName,
      countryCode: c.countryCode

    };

    this.isEdit = true;

  }

  delete(id: number) {

    if (confirm("Delete this country?")) {

      this.service.delete(id)
        .subscribe(() => {

          alert("Deleted Successfully");

          this.loadCountries();

        });

    }

  }

  reset() {

    this.country = {

      countryName: '',
      countryCode: ''

    };

    this.selectedCountryId = 0;

    this.isEdit = false;

  }


}
