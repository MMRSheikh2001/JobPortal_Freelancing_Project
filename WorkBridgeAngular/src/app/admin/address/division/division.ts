import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CountryResponseModel } from '../models/country.model';
import { DivisionRequestModel, DivisionResponseModel } from '../models/division.model';
import { DivisionService } from '../services/division.service';
import { CountryService } from '../services/country.service';

@Component({
  selector: 'app-division',
  imports: [CommonModule, FormsModule],
  templateUrl: './division.html',
  styleUrl: './division.css',
})
export class Division implements OnInit {




  countries: CountryResponseModel[] = [];

  divisions: DivisionResponseModel[] = [];

  division: DivisionRequestModel = {

    divisionName: '',

    countryId: 0

  };

  selectedDivisionId = 0;

  isEdit = false;

  constructor(

    private divisionService: DivisionService,

    private countryService: CountryService,

    private cdr: ChangeDetectorRef

  ) { }

  ngOnInit(): void {

    this.loadCountries();

    this.loadDivisions();

  }

  // ==========================
  // Load Countries
  // ==========================

  loadCountries() {

    this.countryService.getAll().subscribe({

      next: (data) => {

        this.countries = data;

        this.cdr.markForCheck();

        console.log(data);

      },

      error: (err) => {

        console.log(err);

      }

    });

  }

  // ==========================
  // Load Divisions
  // ==========================

  loadDivisions() {

    this.divisionService.getAll().subscribe({

      next: (data) => {

        this.divisions = data;

        this.cdr.markForCheck();

        console.log(data);

      },

      error: (err) => {

        console.log(err);

      }

    });

  }

  // ==========================
  // Save / Update
  // ==========================

  saveDivision(): void {

    if (this.isEdit) {

      this.divisionService.update(

        this.selectedDivisionId,

        this.division

      ).subscribe({

        next: () => {

          alert('Updated Successfully');

          this.loadDivisions();

          this.resetForm();

        }

      });

    }

    else {

      this.divisionService.save(this.division)

        .subscribe({

          next: () => {

            alert('Saved Successfully');

            this.loadDivisions();

            this.resetForm();

          }

        });

    }

  }

  // ==========================
  // Edit
  // ==========================

  editDivision(item: DivisionResponseModel): void {

    this.selectedDivisionId = item.divisionId;

    this.division = {

      divisionName: item.divisionName,

      countryId: item.countryId

    };

    this.isEdit = true;

  }

  // ==========================
  // Delete
  // ==========================

  deleteDivision(id: number): void {

    if (confirm('Delete this division?')) {

      this.divisionService.delete(id)

        .subscribe({

          next: () => {

            alert('Deleted Successfully');

            this.loadDivisions();

          }

        });

    }

  }

  // ==========================
  // Reset
  // ==========================

  resetForm(): void {

    this.division = {

      divisionName: '',

      countryId: 0

    };

    this.selectedDivisionId = 0;

    this.isEdit = false;

  }


}
