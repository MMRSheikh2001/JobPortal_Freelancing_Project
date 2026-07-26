import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { EducationLevel } from '../../../../enums/education-level.enum';
import { ResultType } from '../../../../enums/result-type.enum';
import { EducationRequestModel, EducationResponseModel } from '../../models/education.model';
import { EducationService } from '../../services/education.service';
import { StorageService } from '../../../../auth/services/storage.service';

@Component({
  selector: 'app-education',
  imports: [CommonModule, FormsModule],
  templateUrl: './education.html',
  styleUrl: './education.css',
})
export class Education implements OnInit {




  // ======================================
  // IDs
  // ======================================

  profileId = 0;

  selectedEducationId = 0;

  isEdit = false;

  isCurrentlyStudying = false;

  // ======================================
  // Dropdown Enums
  // ======================================

  educationLevels = Object.values(EducationLevel);

  resultTypes = Object.values(ResultType);

  // ======================================
  // List
  // ======================================

  educations: EducationResponseModel[] = [];

  // ======================================
  // Request Model
  // ======================================

  education: EducationRequestModel = {

    educationLevel: EducationLevel.SSC,

    board: '',

    institution: '',

    fieldOfStudy: '',

    resultType: ResultType.GPA,

    result: 0,

    outOf: 5,

    gradeOrDivision: '',

    startDate: '',

    endDate: '',

    userProfileId: 0

  };

  constructor(

    private service: EducationService,

    private storage: StorageService,

    private cdr: ChangeDetectorRef

  ) { }

  ngOnInit(): void {

    this.profileId =
      this.storage.getProfileId() ?? 0;

    this.education.userProfileId =
      this.profileId;

    this.loadEducations();

  }

  // ======================================
  // Load
  // ======================================

  loadEducations() {

    this.service
      .getByUserProfileId(this.profileId)
      .subscribe(data => {

        this.educations = data;

        this.cdr.markForCheck();

      });

  }

  // ======================================
  // Save
  // ======================================

  save() {

    if (this.isCurrentlyStudying) {

      this.education.endDate = '';

    }

    if (this.isEdit) {

      this.service
        .update(
          this.selectedEducationId,
          this.education
        )
        .subscribe(() => {

          alert("Education Updated Successfully.");

          this.reset();

          this.loadEducations();

        });

    }
    else {

      this.service
        .save(this.education)
        .subscribe(() => {

          alert("Education Saved Successfully.");

          this.reset();

          this.loadEducations();

        });

    }

  }

  // ======================================
  // Edit
  // ======================================

  edit(data: EducationResponseModel) {

    this.selectedEducationId = data.id;

    this.education = {

      educationLevel: data.educationLevel,

      board: data.board,

      institution: data.institution,

      fieldOfStudy: data.fieldOfStudy,

      resultType: data.resultType,

      result: data.result,

      outOf: data.outOf,

      gradeOrDivision: data.gradeOrDivision,

      startDate: data.startDate,

      endDate: data.endDate,

      userProfileId: this.profileId

    };

    this.isEdit = true;
    this.isCurrentlyStudying = data.currentlyStudying;

  }

  // ======================================
  // Delete
  // ======================================

  delete(id: number) {

    if (!confirm("Delete this education?")) {
      return;
    }

    this.service
      .delete(id)
      .subscribe(() => {

        alert("Education Deleted Successfully.");

        this.loadEducations();

      });

  }

  // ======================================
  // Reset
  // ======================================

  reset() {

    this.education = {

      educationLevel: EducationLevel.SSC,

      board: '',

      institution: '',

      fieldOfStudy: '',

      resultType: ResultType.GPA,

      result: 0,

      outOf: 5,

      gradeOrDivision: '',

      startDate: '',

      endDate: '',

      userProfileId: this.profileId

    };

    this.selectedEducationId = 0;

    this.isEdit = false;
    this.isCurrentlyStudying = false;

  }

  // ======================================
  // Currently Studying
  // ======================================

  currentlyStudying(): boolean {

    return this.isCurrentlyStudying;

  }

  // ======================================
  // Result Type Changed
  // ======================================

  resultTypeChanged() {

    switch (this.education.resultType) {

      case ResultType.GPA:

        this.education.outOf = 5;

        this.education.gradeOrDivision = '';

        break;

      case ResultType.CGPA:

        this.education.outOf = 4;

        this.education.gradeOrDivision = '';

        break;

      case ResultType.PERCENTAGE:

        this.education.outOf = 100;

        this.education.gradeOrDivision = '';

        break;

      case ResultType.DIVISION:

      case ResultType.GRADE:

        this.education.result = 0;

        this.education.outOf = 0;

        break;

    }

  }

}




