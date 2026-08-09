import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { StorageService } from '../../../../auth/services/storage.service';
import { Router } from '@angular/router';
import { UserProfileService } from '../../services/user.profile.service';
import { EducationService } from '../../services/education.service';
import { ExperienceService } from '../../services/experience.service';
import { UserSkillService } from '../../services/user.skill.service';
import { UserLanguageService } from '../../services/user.language.service';
import { TrainingService } from '../../services/training.service';
import { PortfolioService } from '../../services/portfolio.service';
import { ReferenceService } from '../../services/reference.service';
import { ExtracurricularService } from '../../services/extracurricular.service';
import { ResumeUploadedFileService } from '../../services/resume-uploaded-file.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-resume-dashboard',
  imports: [CommonModule],
  templateUrl: './resume-dashboard.html',
  styleUrl: './resume-dashboard.css',
})
export class ResumeDashboard implements OnInit {




  // =====================================
  // IDs
  // =====================================

  profileId = 0;

  // =====================================
  // Loading
  // =====================================

  loading = true;

  // =====================================
  // Profile
  // =====================================

  profileCompleted = false;

  // =====================================
  // Counts
  // =====================================

  educationCount = 0;
  experienceCount = 0;
  skillCount = 0;
  languageCount = 0;
  trainingCount = 0;
  portfolioCount = 0;
  referenceCount = 0;
  extracurricularCount = 0;

  // =====================================
  // Resume
  // =====================================

  uploadedResume = false;

  // =====================================
  // Completion
  // =====================================

  completion = 0;

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private storage: StorageService,

    private router: Router,

    private cdr: ChangeDetectorRef,

    private userProfileService: UserProfileService,

    private educationService: EducationService,

    private experienceService: ExperienceService,

    private userSkillService: UserSkillService,

    private userLanguageService: UserLanguageService,

    private trainingService: TrainingService,

    private portfolioService: PortfolioService,

    private referenceService: ReferenceService,

    private extracurricularService: ExtracurricularService,

    private resumeUploadService: ResumeUploadedFileService

  ) { }

  // =====================================
  // Init
  // =====================================

  ngOnInit(): void {

    this.profileId =
      this.storage.getProfileId() ?? 0;

    this.loadDashboard();

  }

  // =====================================
  // Load Dashboard
  // =====================================

  loadDashboard() {

    this.loading = true;

    forkJoin({

      profile:
        this.userProfileService.getById(this.profileId),

      education:
        this.educationService.countByUserProfileId(this.profileId),

      experience:
        this.experienceService.countByUserProfileId(this.profileId),

      skills:
        this.userSkillService.countSkillsByUserProfileId(this.profileId),

      languages:
        this.userLanguageService.countLanguagesByUserProfileId(this.profileId),

      trainings:
        this.trainingService.countByUserProfileId(this.profileId),

      portfolios:
        this.portfolioService.countByUserProfileId(this.profileId),

      references:
        this.referenceService.countByUserProfileId(this.profileId),

      extracurriculars:
        this.extracurricularService.countByUserProfileId(this.profileId),

      uploadedResume:
        this.resumeUploadService.existsByUserProfileId(this.profileId)

    }).subscribe({

      next: result => {

        this.profileCompleted =
          result.profile.profileCompleted;

        this.educationCount =
          result.education;

        this.experienceCount =
          result.experience;

        this.skillCount =
          result.skills;

        this.languageCount =
          result.languages;

        this.trainingCount =
          result.trainings;

        this.portfolioCount =
          result.portfolios;

        this.referenceCount =
          result.references;

        this.extracurricularCount =
          result.extracurriculars;

        this.uploadedResume =
          result.uploadedResume;

        this.calculateCompletion();

        this.loading = false;

        this.cdr.markForCheck();

      },

      error: () => {

        this.loading = false;

        alert('Failed to load resume dashboard.');

      }

    });

  }

  // =====================================
  // Completion
  // =====================================

  calculateCompletion() {

    let score = 0;

    if (this.profileCompleted) score++;
    if (this.educationCount > 0) score++;
    if (this.experienceCount > 0) score++;
    if (this.skillCount > 0) score++;
    if (this.languageCount > 0) score++;
    if (this.trainingCount > 0) score++;
    if (this.portfolioCount > 0) score++;
    if (this.referenceCount > 0) score++;
    if (this.extracurricularCount > 0) score++;
    if (this.uploadedResume) score++;

    this.completion =
      Math.round((score / 10) * 100);

  }

  // =====================================
  // Navigation
  // =====================================

  go(url: string) {

    this.router.navigate([url]);

  }


}
