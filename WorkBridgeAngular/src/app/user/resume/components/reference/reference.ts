import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReferenceRequestModel, ReferenceResponseModel } from '../../models/reference.model';
import { ReferenceService } from '../../services/reference.service';
import { StorageService } from '../../../../auth/services/storage.service';

@Component({
  selector: 'app-reference',
  imports: [FormsModule, CommonModule],
  templateUrl: './reference.html',
  styleUrl: './reference.css',
})
export class Reference implements OnInit {





  // =====================================
  // IDs
  // =====================================

  profileId = 0;

  selectedReferenceId = 0;

  isEdit = false;

  // =====================================
  // List
  // =====================================

  references: ReferenceResponseModel[] = [];

  // =====================================
  // Request Model
  // =====================================

  reference: ReferenceRequestModel = {

    name: '',

    organization: '',

    designation: '',

    phone: '',

    email: '',

    address: '',

    relation: '',

    userProfileId: 0

  };

  constructor(

    private service: ReferenceService,

    private storage: StorageService,

    private cdr: ChangeDetectorRef

  ) { }

  ngOnInit(): void {

    this.profileId =
      this.storage.getProfileId() ?? 0;

    this.reference.userProfileId =
      this.profileId;

    this.loadReferences();

  }

  // =====================================
  // Load
  // =====================================

  loadReferences() {

    this.service
      .getByUserProfileId(this.profileId)
      .subscribe(data => {

        this.references = data;

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
          this.selectedReferenceId,
          this.reference
        )
        .subscribe(() => {

          alert("Reference Updated Successfully.");

          this.reset();

          this.loadReferences();

        });

    }
    else {

      this.service
        .save(this.reference)
        .subscribe(() => {

          alert("Reference Saved Successfully.");

          this.reset();

          this.loadReferences();

        });

    }

  }

  // =====================================
  // Edit
  // =====================================

  edit(data: ReferenceResponseModel) {

    this.selectedReferenceId = data.id;

    this.reference = {

      name: data.name,

      organization: data.organization,

      designation: data.designation,

      phone: data.phone,

      email: data.email,

      address: data.address,

      relation: data.relation,

      userProfileId: this.profileId

    };

    this.isEdit = true;

  }

  // =====================================
  // Delete
  // =====================================

  delete(id: number) {

    if (!confirm("Delete this reference?")) {
      return;
    }

    this.service
      .delete(id)
      .subscribe(() => {

        alert("Reference Deleted Successfully.");

        this.loadReferences();

      });

  }

  // =====================================
  // Reset
  // =====================================

  reset() {

    this.reference = {

      name: '',

      organization: '',

      designation: '',

      phone: '',

      email: '',

      address: '',

      relation: '',

      userProfileId: this.profileId

    };

    this.selectedReferenceId = 0;

    this.isEdit = false;

  }

}



