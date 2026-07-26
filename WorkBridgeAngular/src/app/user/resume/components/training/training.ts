import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TrainingType } from '../../../../enums/training-type.enum';
import { TrainingRequestModel, TrainingResponseModel } from '../../models/training.model';
import { TrainingService } from '../../services/training.service';
import { StorageService } from '../../../../auth/services/storage.service';
import { FileResourceHandleService } from '../../../../services/file-resource-handle.service';

@Component({
  selector: 'app-training',
  imports: [FormsModule, CommonModule],
  templateUrl: './training.html',
  styleUrl: './training.css',
})
export class Training implements OnInit {





  // =====================================
  // IDs
  // =====================================

  profileId = 0;

  selectedTrainingId = 0;

  isEdit = false;

  trainingCompleted = false;

  // =====================================
  // File
  // =====================================

  selectedCertificate?: File;

  // =====================================
  // Dropdown
  // =====================================

  trainingTypes =
    Object.values(TrainingType);

  // =====================================
  // List
  // =====================================

  trainings: TrainingResponseModel[] = [];

  // =====================================
  // Request Model
  // =====================================

  training: TrainingRequestModel = {

    name: '',

    description: '',

    institution: '',

    startDate: '',

    endDate: '',

    duration: '',

    certificateVerificationUrl: '',

    certificateId: '',

    trainingType: TrainingType.Online,

    userProfileId: 0

  };

  constructor(

    private service: TrainingService,

    private storage: StorageService,

    private fileService: FileResourceHandleService,

    private cdr: ChangeDetectorRef

  ) { }

  ngOnInit(): void {

    this.profileId =
      this.storage.getProfileId() ?? 0;

    this.training.userProfileId =
      this.profileId;

    this.loadTrainings();

  }

  // =====================================
  // Load
  // =====================================

  loadTrainings() {

    this.service
      .getByUserProfileId(this.profileId)
      .subscribe(data => {

        this.trainings = data;

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
          this.selectedTrainingId,
          this.training,
          this.selectedCertificate
        )
        .subscribe(() => {

          alert("Training Updated Successfully.");

          this.reset();

          this.loadTrainings();

        });

    }
    else {

      this.service
        .save(
          this.training,
          this.selectedCertificate
        )
        .subscribe(() => {

          alert("Training Saved Successfully.");

          this.reset();

          this.loadTrainings();

        });

    }

  }

  // =====================================
  // Edit
  // =====================================

  edit(data: TrainingResponseModel) {

    this.selectedTrainingId = data.id;

    this.training = {

      name: data.name,

      description: data.description,

      institution: data.institution,

      startDate: data.startDate,

      endDate: data.endDate,

      duration: data.duration,

      certificateVerificationUrl:
        data.certificateVerificationUrl,

      certificateId:
        data.certificateId,

      trainingType:
        data.trainingType,

      userProfileId:
        this.profileId

    };

    this.selectedCertificate = undefined;

    this.trainingCompleted = data.completed;

    this.isEdit = true;

  }

  // =====================================
  // Delete
  // =====================================

  delete(id: number) {

    if (!confirm("Delete this training?")) {
      return;
    }

    this.service
      .delete(id)
      .subscribe(() => {

        alert("Training Deleted Successfully.");

        this.loadTrainings();

      });

  }

  // =====================================
  // Delete Certificate
  // =====================================

  deleteCertificate(id: number) {

    if (!confirm("Delete certificate file?")) {
      return;
    }

    this.service
      .deleteFile(id)
      .subscribe(() => {

        alert("Certificate Deleted Successfully.");

        this.loadTrainings();

      });

  }  // =====================================
  // Reset
  // =====================================

  reset() {

    this.training = {

      name: '',

      description: '',

      institution: '',

      startDate: '',

      endDate: '',

      duration: '',

      certificateVerificationUrl: '',

      certificateId: '',

      trainingType: TrainingType.Online,

      userProfileId: this.profileId

    };

    this.selectedTrainingId = 0;

    this.selectedCertificate = undefined;
    this.trainingCompleted = false;

    this.isEdit = false;

  }

  // =====================================
  // File Selected
  // =====================================

  onFileSelected(event: any) {

    const file = event.target.files[0];

    if (!file) {
      return;
    }

    this.selectedCertificate = file;

  }

  // =====================================
  // Helpers
  // =====================================

  getCertificate(fileName?: string): string {

    return this.fileService.getTraining(fileName);

  }

  hasCertificate(fileName?: string): boolean {

    return !!fileName;

  }

  // =====================================
  // Completed
  // =====================================

  completed(): boolean {

    return !!this.training.endDate;

  }

completedChanged(event: Event) {

  this.trainingCompleted =
    (event.target as HTMLInputElement).checked;

  if (!this.trainingCompleted) {

    this.training.endDate = '';

  }

}

}




