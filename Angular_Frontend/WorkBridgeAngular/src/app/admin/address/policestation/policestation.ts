import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PoliceStationRequestModel, PoliceStationResponseModel } from '../models/police-station.model';
import { DistrictResponseModel } from '../models/district.model';
import { PoliceStationService } from '../services/police-station.service';
import { DistrictService } from '../services/district.service';

@Component({
  selector: 'app-policestation',
  imports: [CommonModule, FormsModule],
  templateUrl: './policestation.html',
  styleUrl: './policestation.css',
})
export class Policestation implements OnInit {




  policeStations: PoliceStationResponseModel[] = [];

  districts: DistrictResponseModel[] = [];

  policeStation: PoliceStationRequestModel = {

    policeStationName: '',

    districtId: 0

  };

  selectedPoliceStationId = 0;

  isEdit = false;

  constructor(

    private policeStationService: PoliceStationService,

    private districtService: DistrictService,

    private cdr: ChangeDetectorRef

  ) { }

  ngOnInit(): void {

    this.loadPoliceStations();

    this.loadDistricts();

  }

  // ==========================
  // Load Police Stations
  // ==========================

  loadPoliceStations() {

    this.policeStationService.getAll().subscribe({

      next: (data) => {

        this.policeStations = data;

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

  loadDistricts() {

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

  savePoliceStation(): void {

    if (this.isEdit) {

      this.policeStationService.update(

        this.selectedPoliceStationId,

        this.policeStation

      ).subscribe({

        next: () => {

          alert("Updated Successfully");

          this.loadPoliceStations();

          this.resetForm();

        }

      });

    }

    else {

      this.policeStationService.save(this.policeStation)

        .subscribe({

          next: () => {

            alert("Saved Successfully");

            this.loadPoliceStations();

            this.resetForm();

          }

        });

    }

  }

  // ==========================
  // Edit
  // ==========================

  editPoliceStation(item: PoliceStationResponseModel): void {

    this.selectedPoliceStationId = item.policeStationId;

    this.policeStation = {

      policeStationName: item.policeStationName,

      districtId: item.districtId

    };

    this.isEdit = true;

  }

  // ==========================
  // Delete
  // ==========================

  deletePoliceStation(id: number): void {

    if (confirm('Delete this Police Station?')) {

      this.policeStationService.delete(id)

        .subscribe({

          next: () => {

            alert("Deleted Successfully");

            this.loadPoliceStations();

          }

        });

    }

  }

  // ==========================
  // Reset
  // ==========================

  resetForm(): void {

    this.policeStation = {

      policeStationName: '',

      districtId: 0

    };

    this.selectedPoliceStationId = 0;

    this.isEdit = false;

  }

}
