import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LanguageProficiency } from '../../../../enums/language-proficiency';
import { UserLanguageRequestModel, UserLanguageResponseModel } from '../../models/user-language';
import { LanguageResponseModel } from '../../../../admin/cvinformations/models/language.model';
import { UserLanguageService } from '../../services/user.language.service';
import { LanguageService } from '../../../../admin/cvinformations/services/language.service';
import { StorageService } from '../../../../auth/services/storage.service';

@Component({
  selector: 'app-user-language',
  imports: [CommonModule, FormsModule],
  templateUrl: './user-language.html',
  styleUrl: './user-language.css',
})
export class UserLanguage implements OnInit {




  // =====================================
  // IDs
  // =====================================

  profileId = 0;

  selectedUserLanguageId = 0;

  isEdit = false;

  // =====================================
  // Dropdown
  // =====================================

  proficiencyLevels =
    Object.values(LanguageProficiency);

  languages: LanguageResponseModel[] = [];

  // =====================================
  // List
  // =====================================

  userLanguages: UserLanguageResponseModel[] = [];

  // =====================================
  // Request Model
  // =====================================

  userLanguage: UserLanguageRequestModel = {

    proficiency:
      LanguageProficiency.BEGINNER,

    languageId: 0,

    userProfileId: 0

  };

  constructor(

    private userLanguageService: UserLanguageService,

    private languageService: LanguageService,

    private storage: StorageService,

    private cdr: ChangeDetectorRef

  ) { }

  ngOnInit(): void {

    this.profileId =
      this.storage.getProfileId() ?? 0;

    this.userLanguage.userProfileId =
      this.profileId;

    this.loadLanguages();

    this.loadUserLanguages();

  }

  // =====================================
  // Load Languages
  // =====================================

  loadLanguages() {

    this.languageService
      .getAll()
      .subscribe(data => {

        this.languages = data;

        this.cdr.markForCheck();

      });

  }

  // =====================================
  // Load User Languages
  // =====================================

  loadUserLanguages() {

    this.userLanguageService
      .getByUserProfileId(this.profileId)
      .subscribe(data => {

        this.userLanguages = data;

        this.cdr.markForCheck();

      });

  }

  // =====================================
  // Save
  // =====================================

  save() {

    if (this.isEdit) {

      this.userLanguageService
        .update(
          this.selectedUserLanguageId,
          this.userLanguage
        )
        .subscribe(() => {

          alert("User Language Updated Successfully.");

          this.reset();

          this.loadUserLanguages();

        });

    }
    else {

      this.userLanguageService
        .save(this.userLanguage)
        .subscribe(() => {

          alert("User Language Saved Successfully.");

          this.reset();

          this.loadUserLanguages();

        });

    }

  }

  // =====================================
  // Edit
  // =====================================

  edit(data: UserLanguageResponseModel) {

    this.selectedUserLanguageId = data.id;

    this.userLanguage = {

      proficiency: data.proficiency,

      languageId: data.languageId,

      userProfileId: this.profileId

    };

    this.isEdit = true;

  }

  // =====================================
  // Delete
  // =====================================

  delete(id: number) {

    if (!confirm("Delete this language?")) {
      return;
    }

    this.userLanguageService
      .delete(id)
      .subscribe(() => {

        alert("User Language Deleted Successfully.");

        this.loadUserLanguages();

      });

  }

  // =====================================
  // Reset
  // =====================================

  reset() {

    this.userLanguage = {

      proficiency: LanguageProficiency.BEGINNER,

      languageId: 0,

      userProfileId: this.profileId

    };

    this.selectedUserLanguageId = 0;

    this.isEdit = false;

  }


}
