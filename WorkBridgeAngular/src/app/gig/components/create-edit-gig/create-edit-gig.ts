import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CategoryResponseModel } from '../../../admin/cvinformations/models/category.model';
import { GigRequestModel, GigResponseModel } from '../../models/gig.model';
import { GigService } from '../../services/gig.service';
import { CategoryService } from '../../../admin/cvinformations/services/category.service';
import { StorageService } from '../../../auth/services/storage.service';
import { FileResourceHandleService } from '../../../services/file-resource-handle.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-create-edit-gig',
  imports: [CommonModule, FormsModule],
  templateUrl: './create-edit-gig.html',
  styleUrl: './create-edit-gig.css',
})
export class CreateEditGig implements OnInit {





  // ==========================
  // Variables
  // ==========================

  gigId = 0;

  editMode = false;

  selectedImage?: File;

  imagePreview = '';

  categories: CategoryResponseModel[] = [];

  originalGig!: GigRequestModel;

  originalImagePreview = '';

  // ==========================
  // Model
  // ==========================

  gig: GigRequestModel = {

    title: '',

    shortDescription: '',

    description: '',

    startingPrice: 0,

    deliveryDays: 1,

    revisions: 0,

    categoryId: 0,

    userProfileId: 0

  };

  // ==========================
  // Constructor
  // ==========================

  constructor(

    private gigService: GigService,

    private categoryService: CategoryService,

    private storageService: StorageService,

    private fileService: FileResourceHandleService,

    private route: ActivatedRoute,

    private router: Router,
    private cdr: ChangeDetectorRef

  ) { }

  // ==========================
  // Init
  // ==========================

  ngOnInit(): void {

    const profileId = this.storageService.getProfileId();

    if (profileId) {

      this.gig.userProfileId = profileId;

    }

    this.loadCategories();

    const id = this.route.snapshot.paramMap.get('id');

    if (id) {

      this.editMode = true;

      this.gigId = Number(id);

      this.loadGig();

    }

  }

  // ==========================
  // Load Categories
  // ==========================

  loadCategories(): void {

    this.categoryService.getAll().subscribe({

      next: res => {

        this.categories = res;
        this.cdr.markForCheck();

      }

    });

  }

  // ==========================
  // Load Gig
  // ==========================

  loadGig(): void {

    this.gigService.getById(this.gigId).subscribe({

      next: (res: GigResponseModel) => {

        this.gig = {

          title: res.title,

          shortDescription: res.shortDescription,

          description: res.description,

          startingPrice: res.startingPrice,

          deliveryDays: res.deliveryDays,

          revisions: res.revisions,

          categoryId: res.categoryId,

          userProfileId: res.userProfileId

        };

        this.originalGig = { ...this.gig };
        this.cdr.markForCheck();

        this.imagePreview =
          this.fileService.getGigImage(res.gigImage);
        this.originalImagePreview = this.imagePreview;

      }

    });

  }

  // ==========================
  // Select Image
  // ==========================

  onImageSelected(event: any): void {

    const file = event.target.files[0];

    if (!file) {

      return;

    }

    this.selectedImage = file;

    const reader = new FileReader();

    reader.onload = () => {

      this.imagePreview = reader.result as string;
      this.cdr.markForCheck();

    };

    reader.readAsDataURL(file);

  }

  // ==========================
  // Save
  // ==========================

  save(): void {

    if (this.editMode) {

      this.update();

      return;

    }

    this.gigService
      .save(this.gig, this.selectedImage)
      .subscribe({

        next: () => {

          alert('Gig Created Successfully');

          this.router.navigate(['/user/dashboard']);
          this.cdr.markForCheck();

        }

      });

  }

  // ==========================
  // Update
  // ==========================

  update(): void {

    this.gigService
      .update(
        this.gigId,
        this.gig,
        this.selectedImage
      )
      .subscribe({

        next: () => {

          alert('Gig Updated Successfully');

          this.router.navigate(['/user/dashboard']);
          this.cdr.markForCheck();

        }

      });

  }

  resetForm(): void {

    if (this.editMode) {

      this.gig = { ...this.originalGig };

      this.selectedImage = undefined;

      this.imagePreview = this.originalImagePreview;

    } else {

      this.gig = {

        title: '',

        shortDescription: '',

        description: '',

        startingPrice: 0,

        deliveryDays: 1,

        revisions: 0,

        categoryId: 0,

        userProfileId: this.storageService.getProfileId()!

      };

      this.selectedImage = undefined;

      this.imagePreview = '';

    }

    this.cdr.markForCheck();

  }


}
