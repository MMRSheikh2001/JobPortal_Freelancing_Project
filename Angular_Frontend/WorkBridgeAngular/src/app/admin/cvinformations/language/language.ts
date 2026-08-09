import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LanguageRequestModel, LanguageResponseModel } from '../models/language.model';
import { LanguageService } from '../services/language.service';

@Component({
  selector: 'app-language',
  imports: [CommonModule, FormsModule],
  templateUrl: './language.html',
  styleUrl: './language.css',
})
export class Language implements OnInit {





  languages: LanguageResponseModel[] = [];

  language: LanguageRequestModel = {

    name: ''

  };

  selectedLanguageId = 0;

  isEdit = false;

  constructor(

    private languageService: LanguageService,

    private cdr: ChangeDetectorRef

  ) { }

  ngOnInit(): void {

    this.loadLanguages();

  }

  // ==========================
  // Load Languages
  // ==========================

  loadLanguages() {

    this.languageService.getAll().subscribe({

      next: (data) => {

        this.languages = data;

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

  saveLanguage(): void {

    if (this.isEdit) {

      this.languageService.update(

        this.selectedLanguageId,

        this.language

      ).subscribe({

        next: () => {

          alert("Updated Successfully");

          this.loadLanguages();

          this.resetForm();

        },

        error: (err) => {

          console.log(err);

        }

      });

    }

    else {

      this.languageService.save(this.language)

        .subscribe({

          next: () => {

            alert("Saved Successfully");

            this.loadLanguages();

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

  editLanguage(item: LanguageResponseModel): void {

    this.selectedLanguageId = item.id;

    this.language = {

      name: item.name

    };

    this.isEdit = true;

  }

  // ==========================
  // Delete
  // ==========================

  deleteLanguage(id: number): void {

    if (confirm('Delete this Language?')) {

      this.languageService.delete(id)

        .subscribe({

          next: () => {

            alert("Deleted Successfully");

            this.loadLanguages();

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

    this.language = {

      name: ''

    };

    this.selectedLanguageId = 0;

    this.isEdit = false;

  }


}
