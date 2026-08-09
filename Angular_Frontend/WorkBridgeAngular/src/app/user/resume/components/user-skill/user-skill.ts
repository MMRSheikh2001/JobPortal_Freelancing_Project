import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ProficiencyLevel } from '../../../../enums/proficiency-level.enum';
import { CategoryResponseModel } from '../../../../admin/cvinformations/models/category.model';
import { SkillResponseModel } from '../../../../admin/cvinformations/models/skill.model';
import { UserSkillRequestModel, UserSkillResponseModel } from '../../models/user-skill.model';
import { UserSkillService } from '../../services/user.skill.service';
import { CategoryService } from '../../../../admin/cvinformations/services/category.service';
import { SkillService } from '../../../../admin/cvinformations/services/skill.service';
import { StorageService } from '../../../../auth/services/storage.service';

@Component({
  selector: 'app-user-skill',
  imports: [FormsModule, CommonModule],
  templateUrl: './user-skill.html',
  styleUrl: './user-skill.css',
})
export class UserSkill implements OnInit {




  // =====================================
  // IDs
  // =====================================

  profileId = 0;

  selectedUserSkillId = 0;

  selectedCategoryId = 0;

  isEdit = false;

  // =====================================
  // Dropdown
  // =====================================

  proficiencyLevels =
    Object.values(ProficiencyLevel);

  categories: CategoryResponseModel[] = [];

  skills: SkillResponseModel[] = [];

  // =====================================
  // List
  // =====================================

  userSkills: UserSkillResponseModel[] = [];

  // =====================================
  // Request Model
  // =====================================

  userSkill: UserSkillRequestModel = {

    proficiencyLevel:
      ProficiencyLevel.BEGINNER,

    yearsOfExperience: 0,

    userProfileId: 0,

    skillId: 0

  };

  constructor(

    private userSkillService: UserSkillService,

    private categoryService: CategoryService,

    private skillService: SkillService,

    private storage: StorageService,

    private cdr: ChangeDetectorRef

  ) { }

  ngOnInit(): void {

    this.profileId =
      this.storage.getProfileId() ?? 0;

    this.userSkill.userProfileId =
      this.profileId;

    this.loadCategories();

    this.loadUserSkills();

  }

  // =====================================
  // Load Categories
  // =====================================

  loadCategories() {

    this.categoryService
      .getAll()
      .subscribe(data => {

        this.categories = data;

        this.cdr.markForCheck();

      });

  }

  // =====================================
  // Load Skills By Category
  // =====================================

  loadSkills() {

    if (this.selectedCategoryId == 0) {

      this.skills = [];

      this.userSkill.skillId = 0;

      return;

    }

    this.skillService
      .getByCategoryId(this.selectedCategoryId)
      .subscribe(data => {

        this.skills = data;

        this.cdr.markForCheck();

      });

  }

  // =====================================
  // Load User Skills
  // =====================================

  loadUserSkills() {

    this.userSkillService
      .getByUserProfileId(this.profileId)
      .subscribe(data => {

        this.userSkills = data;

        this.cdr.markForCheck();

      });

  }

  // =====================================
  // Category Changed
  // =====================================

  categoryChanged() {

    this.userSkill.skillId = 0;

    this.loadSkills();

  }  // =====================================
  // Save
  // =====================================

  save() {

    if (this.isEdit) {

      this.userSkillService
        .update(
          this.selectedUserSkillId,
          this.userSkill
        )
        .subscribe(() => {

          alert("User Skill Updated Successfully.");

          this.reset();

          this.loadUserSkills();

        });

    }
    else {

      this.userSkillService
        .save(this.userSkill)
        .subscribe(() => {

          alert("User Skill Saved Successfully.");

          this.reset();

          this.loadUserSkills();

        });

    }

  }

  // =====================================
  // Edit
  // =====================================

  edit(data: UserSkillResponseModel) {

    this.selectedUserSkillId = data.id;

    this.selectedCategoryId = data.categoryId;

    this.loadSkills();

    this.userSkill = {

      proficiencyLevel: data.proficiencyLevel,

      yearsOfExperience: data.yearsOfExperience,

      userProfileId: this.profileId,

      skillId: data.skillId

    };

    this.isEdit = true;

  }

  // =====================================
  // Delete
  // =====================================

  delete(id: number) {

    if (!confirm("Delete this skill?")) {
      return;
    }

    this.userSkillService
      .delete(id)
      .subscribe(() => {

        alert("User Skill Deleted Successfully.");

        this.loadUserSkills();

      });

  }

  // =====================================
  // Reset
  // =====================================

  reset() {

    this.selectedCategoryId = 0;

    this.skills = [];

    this.userSkill = {

      proficiencyLevel: ProficiencyLevel.BEGINNER,

      yearsOfExperience: 0,

      userProfileId: this.profileId,

      skillId: 0

    };

    this.selectedUserSkillId = 0;

    this.isEdit = false;

  }


}
