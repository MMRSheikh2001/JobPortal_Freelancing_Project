import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CategoryRequestModel, CategoryResponseModel } from '../models/category.model';
import { CategoryService } from '../services/category.service';

@Component({
  selector: 'app-category',
  imports: [CommonModule, FormsModule],
  templateUrl: './category.html',
  styleUrl: './category.css',
})
export class Category implements OnInit {




  categories: CategoryResponseModel[] = [];

  category: CategoryRequestModel = {

    name: '',
    description: ''

  };

  selectedCategoryId = 0;

  isEdit = false;

  constructor(

    private categoryService: CategoryService,

    private cdr: ChangeDetectorRef

  ) { }

  ngOnInit(): void {

    this.loadCategories();

  }

  // ==========================
  // Load Categories
  // ==========================

  loadCategories() {

    this.categoryService.getAll().subscribe({

      next: (data) => {

        this.categories = data;

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

  saveCategory(): void {

    if (this.isEdit) {

      this.categoryService.update(

        this.selectedCategoryId,

        this.category

      ).subscribe({

        next: () => {

          alert("Updated Successfully");

          this.loadCategories();

          this.resetForm();

        }

      });

    }

    else {

      this.categoryService.save(this.category)

        .subscribe({

          next: () => {

            alert("Saved Successfully");

            this.loadCategories();

            this.resetForm();

          }

        });

    }

  }

  // ==========================
  // Edit
  // ==========================

  editCategory(item: CategoryResponseModel): void {

    this.selectedCategoryId = item.id;

    this.category = {

      name: item.name,

      description: item.description

    };

    this.isEdit = true;

  }

  // ==========================
  // Delete
  // ==========================

  deleteCategory(id: number): void {

    if (confirm('Delete this Category?')) {

      this.categoryService.delete(id)

        .subscribe({

          next: () => {

            alert("Deleted Successfully");

            this.loadCategories();

          }

        });

    }

  }

  // ==========================
  // Reset
  // ==========================

  resetForm(): void {

    this.category = {

      name: '',

      description: ''

    };

    this.selectedCategoryId = 0;

    this.isEdit = false;

  }


}
