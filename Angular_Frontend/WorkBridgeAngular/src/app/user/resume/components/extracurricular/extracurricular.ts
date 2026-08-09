import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ExtracurricularRequestModel, ExtracurricularResponseModel } from '../../models/extracurricular.model';
import { ExtracurricularService } from '../../services/extracurricular.service';
import { StorageService } from '../../../../auth/services/storage.service';

@Component({
  selector: 'app-extracurricular',
  imports: [CommonModule, FormsModule],
  templateUrl: './extracurricular.html',
  styleUrl: './extracurricular.css',
})
export class Extracurricular implements OnInit {





  // =====================================
  // IDs
  // =====================================

  profileId = 0;

  selectedExtracurricularId = 0;

  isEdit = false;

  // =====================================
  // List
  // =====================================

  extracurriculars: ExtracurricularResponseModel[] = [];

  // =====================================
  // Request Model
  // =====================================

  extracurricular: ExtracurricularRequestModel = {

    title: '',

    description: '',

    organization: '',

    role: '',

    userProfileId: 0

  };

  constructor(

    private service: ExtracurricularService,

    private storage: StorageService,

    private cdr: ChangeDetectorRef

  ) { }

  ngOnInit(): void {

    this.profileId =
      this.storage.getProfileId() ?? 0;

    this.extracurricular.userProfileId =
      this.profileId;

    this.loadExtracurriculars();

  }

  // =====================================
  // Load
  // =====================================

  loadExtracurriculars() {

    this.service
      .getByUserProfileId(this.profileId)
      .subscribe(data => {

        this.extracurriculars = data;

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
          this.selectedExtracurricularId,
          this.extracurricular
        )
        .subscribe(() => {

          alert("Extracurricular Updated Successfully.");

          this.reset();

          this.loadExtracurriculars();

        });

    }
    else {

      this.service
        .save(this.extracurricular)
        .subscribe(() => {

          alert("Extracurricular Saved Successfully.");

          this.reset();

          this.loadExtracurriculars();

        });

    }

  }

  // =====================================
  // Edit
  // =====================================

  edit(data: ExtracurricularResponseModel) {

    this.selectedExtracurricularId = data.id;

    this.extracurricular = {

      title: data.title,

      description: data.description,

      organization: data.organization,

      role: data.role,

      userProfileId: this.profileId

    };

    this.isEdit = true;

  }

  // =====================================
  // Delete
  // =====================================

  delete(id: number) {

    if (!confirm("Delete this extracurricular activity?")) {
      return;
    }

    this.service
      .delete(id)
      .subscribe(() => {

        alert("Extracurricular Deleted Successfully.");

        this.loadExtracurriculars();

      });

  }

  // =====================================
  // Reset
  // =====================================

  reset() {

    this.extracurricular = {

      title: '',

      description: '',

      organization: '',

      role: '',

      userProfileId: this.profileId

    };

    this.selectedExtracurricularId = 0;

    this.isEdit = false;

  }



}
