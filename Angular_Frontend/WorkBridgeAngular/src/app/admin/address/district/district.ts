import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DivisionService } from '../services/division.service';
import { DistrictService } from '../services/district.service';
import { DivisionResponseModel } from '../models/division.model';
import { DistrictRequestModel, DistrictResponseModel } from '../models/district.model';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-district',
  imports: [FormsModule, CommonModule],
  templateUrl: './district.html',
  styleUrl: './district.css',
})
export class District implements OnInit {




  divisions: DivisionResponseModel[] = [];
  districts: DistrictResponseModel[] = [];

  district: DistrictRequestModel = {

    districtName: '',
    divisionId: 0

  };

  selectedDistrictId = 0;

  isEdit = false;

  constructor(

    private divisionService: DivisionService,

    private districtService: DistrictService,

    private cdr: ChangeDetectorRef

  ) { }

  ngOnInit(): void {

    this.loadDivision();

    this.loadDistrict();

  }

  // ==========================
  // Load Divisions
  // ==========================

  loadDivision() {

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
  // Load Districts
  // ==========================

  loadDistrict() {

    this.districtService.getAll().subscribe({

      next: (data) => {

        this.districts = data;

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

  saveDistrict(): void {

    if (this.isEdit) {

      this.districtService.update(

        this.selectedDistrictId,

        this.district

      ).subscribe({

        next: () => {

          alert("Updated Successfully");

          this.loadDistrict();

          this.resetForm();

        }

      });

    }

    else {

      this.districtService.save(this.district)

        .subscribe({

          next: () => {

            alert("Saved Successfully");

            this.loadDistrict();

            this.resetForm();

          }

        });

    }

  }

  // ==========================
  // Edit
  // ==========================

  editDistrict(item: DistrictResponseModel): void {

    this.selectedDistrictId = item.districtId;

    this.district = {

      districtName: item.districtName,

      divisionId: item.divisionId

    };

    this.isEdit = true;

  }

  // ==========================
  // Delete
  // ==========================

  deleteDistrict(id: number): void {

    if (confirm("Delete this District?")) {

      this.districtService.delete(id)

        .subscribe({

          next: () => {

            alert("Deleted Successfully");

            this.loadDistrict();

          }

        });

    }

  }

  // ==========================
  // Reset
  // ==========================

  resetForm(): void {

    this.district = {

      districtName: '',

      divisionId: 0

    };

    this.selectedDistrictId = 0;

    this.isEdit = false;

  }


}
