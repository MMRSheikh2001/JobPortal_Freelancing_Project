import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SavedGigResponseDTO } from '../models/saved-gig.model';
import { SavedGigService } from '../services/saved-gig.service';
import { StorageService } from '../../auth/services/storage.service';
import { ToastService } from '../../services/toast.service';
import { Router } from '@angular/router';
import { FileResourceHandleService } from '../../services/file-resource-handle.service';

@Component({
  selector: 'app-my-saved-gigs.component',
  imports: [CommonModule, FormsModule],
  templateUrl: './my-saved-gigs.component.html',
  styleUrl: './my-saved-gigs.component.css',
})
export class MySavedGigsComponent implements OnInit {






  loading = false;

  userId = 0;

  search = '';

  savedGigs: SavedGigResponseDTO[] = [];

  filteredGigs: SavedGigResponseDTO[] = [];

  constructor(
    private savedGigService: SavedGigService,
    private storage: StorageService,
    private toast: ToastService,
    private router: Router,
    public fileService: FileResourceHandleService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {

    this.userId =
      this.storage.getUserId() ?? 0;

    this.load();

  }

  load(): void {

    this.loading = true;

    this.savedGigService
      .getSavedGigs(this.userId)
      .subscribe({

        next: res => {

          this.savedGigs = res;

          this.filteredGigs = [...res];

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load saved gigs.',
            'danger'
          );

        }

      });

  }

  filter(): void {

    const keyword =
      this.search.trim().toLowerCase();

    if (!keyword) {

      this.filteredGigs = [...this.savedGigs];

      return;

    }

    this.filteredGigs =
      this.savedGigs.filter(g =>

        g.gigTitle.toLowerCase().includes(keyword) ||

        g.freelancerName.toLowerCase().includes(keyword)

      );

  }

  remove(item: SavedGigResponseDTO): void {

    if (!confirm('Remove this saved gig?')) {

      return;

    }

    this.savedGigService
      .unsaveGig(item.userId, item.gigId)
      .subscribe({

        next: () => {

          this.toast.show(
            'Removed from saved gigs.'
          );

          this.load();

        }

      });

  }

  view(item: SavedGigResponseDTO): void {

    this.router.navigate([
      '/gig-details',
      item.gigId
    ]);

  }



}
