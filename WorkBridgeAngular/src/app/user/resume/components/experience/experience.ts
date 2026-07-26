import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { EmploymentType } from '../../../../enums/employment-type.enum';
import { ExperienceRequestModel, ExperienceResponseModel } from '../../models/experience.model';
import { ExperienceService } from '../../services/experience.service';
import { StorageService } from '../../../../auth/services/storage.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-experience',
  imports: [CommonModule, FormsModule],
  templateUrl: './experience.html',
  styleUrl: './experience.css',
})
export class Experience implements OnInit {




  // =====================================
  // IDs
  // =====================================

  profileId = 0;

  selectedExperienceId = 0;

  isEdit = false;
 currentlyWorking = false;

  // =====================================
  // Employment Types
  // =====================================

  employmentTypes =
    Object.values(EmploymentType);

  // =====================================
  // List
  // =====================================

  experiences: ExperienceResponseModel[] = [];

  // =====================================
  // Request Model
  // =====================================

  experience: ExperienceRequestModel = {

    companyName: '',

    position: '',

    responsibilities: '',

    achievements: '',

    startDate: '',

    endDate: '',

    employmentType: EmploymentType.Full_Time,

    userProfileId: 0

  };

  constructor(

    private service: ExperienceService,

    private storage: StorageService,

    private cdr: ChangeDetectorRef

  ) { }

  ngOnInit(): void {

    this.profileId =
      this.storage.getProfileId() ?? 0;

    this.experience.userProfileId =
      this.profileId;

    this.loadExperiences();

  }

  // =====================================
  // Load
  // =====================================

  loadExperiences() {

    this.service
      .getByUserProfileId(this.profileId)
      .subscribe(data => {

        this.experiences = data;

        this.cdr.markForCheck();

      });
  }


  // =====================================
  // Save
  // =====================================

  save() {

    if (this.isEdit) {

      this.service
        .update(
          this.selectedExperienceId,
          this.experience
        )
        .subscribe(() => {

          alert("Experience Updated Successfully.");

          this.reset();

          this.loadExperiences();

        });

    }
    else {

      this.service
        .save(this.experience)
        .subscribe(() => {

          alert("Experience Saved Successfully.");

          this.reset();

          this.loadExperiences();

        });

    }

  }

  // =====================================
  // Edit
  // =====================================

  edit(data: ExperienceResponseModel) {

    this.selectedExperienceId = data.id;

    this.experience = {

      companyName: data.companyName,

      position: data.position,

      responsibilities: data.responsibilities,

      achievements: data.achievements,

      startDate: data.startDate,

      endDate: data.endDate,

      employmentType: data.employmentType,

      userProfileId: this.profileId

    };

    this.currentlyWorking = !data.endDate;
    this.isEdit = true;

  }

  // =====================================
  // Delete
  // =====================================

  delete(id: number) {

    if (!confirm("Delete this experience?")) {
      return;
    }

    this.service
      .delete(id)
      .subscribe(() => {

        alert("Experience Deleted Successfully.");

        this.loadExperiences();

      });

  }

  // =====================================
  // Reset
  // =====================================

  reset() {

    this.experience = {

      companyName: '',

      position: '',

      responsibilities: '',

      achievements: '',

      startDate: '',

      endDate: '',

      employmentType: EmploymentType.Full_Time,

      userProfileId: this.profileId

    };

    this.selectedExperienceId = 0;

    this.isEdit = false;

    this.currentlyWorking = false;

  }

  // =====================================
  // Currently Working
  // =====================================

 // =====================================
// Working Changed
// =====================================

workingChanged(event: Event) {

  this.currentlyWorking =
    (event.target as HTMLInputElement).checked;

  if (this.currentlyWorking) {

    this.experience.endDate = '';

  }

}

  

}





