import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SkillService } from '../services/skill.service';
import { CategoryService } from '../services/category.service';
import { CategoryResponseModel } from '../models/category.model';
import { SkillRequestModel, SkillResponseModel } from '../models/skill.model';

@Component({
  selector: 'app-skill',
  imports: [CommonModule, FormsModule],
  templateUrl: './skill.html',
  styleUrl: './skill.css',
})
export class Skill implements OnInit {




  categories: CategoryResponseModel[] = [];

  skills: SkillResponseModel[] = [];

  skill: SkillRequestModel = {

    skillName: '',

    categoryId: 0

  };

  selectedSkillId = 0;

  isEdit = false;

  constructor(

    private skillService: SkillService,

    private categoryService: CategoryService,

    private cdr: ChangeDetectorRef

  ) { }

  ngOnInit(): void {

    this.loadCategories();

    this.loadSkills();

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
  // Load Skills
  // ==========================

  loadSkills() {

    this.skillService.getAll().subscribe({

      next: (data) => {

        this.skills = data;

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

  saveSkill(): void {

    if (this.isEdit) {

      this.skillService.update(

        this.selectedSkillId,

        this.skill

      ).subscribe({

        next: () => {

          alert("Updated Successfully");

          this.loadSkills();

          this.resetForm();

        },

        error: (err) => {

          console.log(err);

        }

      });

    }

    else {

      this.skillService.save(this.skill)

        .subscribe({

          next: () => {

            alert("Saved Successfully");

            this.loadSkills();

            this.resetForm();

          },

          error: (err) => {

            console.log(err);

          }

        });

    }

  }

  // ==========================
  // Edit
  // ==========================

  editSkill(item: SkillResponseModel): void {

    this.selectedSkillId = item.skillId;

    this.skill = {

      skillName: item.skillName,

      categoryId: item.categoryId

    };

    this.isEdit = true;

  }

  // ==========================
  // Delete
  // ==========================

  deleteSkill(id: number): void {

    if (confirm('Delete this Skill?')) {

      this.skillService.delete(id)

        .subscribe({

          next: () => {

            alert("Deleted Successfully");

            this.loadSkills();

          },

          error: (err) => {

            console.log(err);

          }

        });

    }

  }

  // ==========================
  // Reset
  // ==========================

  resetForm(): void {

    this.skill = {

      skillName: '',

      categoryId: 0

    };

    this.selectedSkillId = 0;

    this.isEdit = false;

  }


}
