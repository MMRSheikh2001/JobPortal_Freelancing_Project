import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PortfolioRequestModel, PortfolioResponseModel } from '../../models/portfolio.model';
import { PortfolioService } from '../../services/portfolio.service';
import { StorageService } from '../../../../auth/services/storage.service';
import { FileResourceHandleService } from '../../../../services/file-resource-handle.service';

@Component({
  selector: 'app-portfolio',
  imports: [CommonModule, FormsModule],
  templateUrl: './portfolio.html',
  styleUrl: './portfolio.css',
})
export class Portfolio implements OnInit {




  // =====================================
  // IDs
  // =====================================

  profileId = 0;

  selectedPortfolioId = 0;

  isEdit = false;

  // =====================================
  // File
  // =====================================

  selectedFile?: File;

  // =====================================
  // List
  // =====================================

  portfolios: PortfolioResponseModel[] = [];

  // =====================================
  // Request Model
  // =====================================

  portfolio: PortfolioRequestModel = {

    title: '',

    description: '',

    projectUrl: '',

    technologies: '',

    userProfileId: 0

  };

  constructor(

    private service: PortfolioService,

    private storage: StorageService,

    private fileService: FileResourceHandleService,

    private cdr: ChangeDetectorRef

  ) { }

  ngOnInit(): void {

    this.profileId =
      this.storage.getProfileId() ?? 0;

    this.portfolio.userProfileId =
      this.profileId;

    this.loadPortfolios();

  }

  // =====================================
  // Load
  // =====================================

  loadPortfolios() {

    this.service
      .getByUserProfileId(this.profileId)
      .subscribe(data => {

        this.portfolios = data;

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
          this.selectedPortfolioId,
          this.portfolio,
          this.selectedFile
        )
        .subscribe(() => {

          alert("Portfolio Updated Successfully.");

          this.reset();

          this.loadPortfolios();

        });

    }
    else {

      this.service
        .save(
          this.portfolio,
          this.selectedFile
        )
        .subscribe(() => {

          alert("Portfolio Saved Successfully.");

          this.reset();

          this.loadPortfolios();

        });

    }

  }

  // =====================================
  // Edit
  // =====================================

  edit(data: PortfolioResponseModel) {

    this.selectedPortfolioId = data.id;

    this.portfolio = {

      title: data.title,

      description: data.description,

      projectUrl: data.projectUrl,

      technologies: data.technologies,

      userProfileId: this.profileId

    };

    this.selectedFile = undefined;

    this.isEdit = true;

  }

  // =====================================
  // Delete
  // =====================================

  delete(id: number) {

    if (!confirm("Delete this portfolio?")) {
      return;
    }

    this.service
      .delete(id)
      .subscribe(() => {

        alert("Portfolio Deleted Successfully.");

        this.loadPortfolios();

      });

  }

  // =====================================
  // Delete File
  // =====================================

  deleteFile(id: number) {

    if (!confirm("Delete portfolio file?")) {
      return;
    }

    this.service
      .deleteFile(id)
      .subscribe(() => {

        alert("Portfolio File Deleted Successfully.");

        this.loadPortfolios();

      });

  }

  // =====================================
  // Reset
  // =====================================

  reset() {

    this.portfolio = {

      title: '',

      description: '',

      projectUrl: '',

      technologies: '',

      userProfileId: this.profileId

    };

    this.selectedPortfolioId = 0;

    this.selectedFile = undefined;

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

    this.selectedFile = file;

  }

  // =====================================
  // Helpers
  // =====================================

  getPortfolio(fileName?: string): string {

    return this.fileService.getPortfolio(fileName);

  }

  hasFile(fileName?: string): boolean {

    return !!fileName;

  }


  getProjectUrl(url?: string): string {

    if (!url) {
      return '';
    }

    if (
      url.startsWith('http://') ||
      url.startsWith('https://')
    ) {
      return url;
    }

    return 'https://' + url;

  }

}


