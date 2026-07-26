import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ConversationResponseModel } from '../../models/conversation.model';
import { ConversationService } from '../../services/conversation.service';
import { StorageService } from '../../../auth/services/storage.service';
import { ToastService } from '../../../services/toast.service';
import { forkJoin } from 'rxjs';
import { FileResourceHandleService } from '../../../services/file-resource-handle.service';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-conversation-list',
  imports: [CommonModule, RouterOutlet],
  templateUrl: './conversation-list.html',
  styleUrl: './conversation-list.css',
})
export class ConversationList implements OnInit {



  // =====================================
  // Properties
  // =====================================

  buyerId = 0;

  sellerUserProfileId = 0;

  role: string | null = null;

  conversations: ConversationResponseModel[] = [];

  loading = false;

  selectedConversationId = 0;

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private conversationService: ConversationService,

    private storage: StorageService,

    private toast: ToastService,

    private cdr: ChangeDetectorRef,
    public fileService: FileResourceHandleService,
    private router: Router,
    private route: ActivatedRoute

  ) { }

  // =====================================
  // Init
  // =====================================

  ngOnInit(): void {

    this.buyerId =
      this.storage.getUserId() ?? 0;

    this.role =
      this.storage.getRole();

    // Only USER can be a seller
    if (this.role === 'USER') {

      this.sellerUserProfileId =
        this.storage.getProfileId() ?? 0;

    }

    this.loadConversations();

    this.route.firstChild?.paramMap.subscribe(param => {

      this.selectedConversationId =
        Number(param.get('conversationId'));

    });

  }

  // =====================================
  // Load Conversations
  // =====================================

  loadConversations(): void {

    this.loading = true;

    // USER -> Buyer + Seller conversations
    if (this.role === 'USER') {

      forkJoin({

        buyer:
          this.conversationService.getBuyerConversations(
            this.buyerId
          ),

        seller:
          this.conversationService.getSellerConversations(
            this.sellerUserProfileId
          )

      }).subscribe({

        next: ({ buyer, seller }) => {

          const map = new Map<number, ConversationResponseModel>();

          [...buyer, ...seller].forEach(c => {

            map.set(c.id, c);

          });

          this.conversations =
            Array.from(map.values())
              .sort((a, b) =>
                new Date(b.lastMessageAt).getTime()
                -
                new Date(a.lastMessageAt).getTime()
              );
          if (
            this.conversations.length > 0 &&
            this.selectedConversationId === 0
          ) {

            this.openConversation(
              this.conversations[0].id
            );

          }

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load conversations.',
            'danger'
          );

          this.cdr.markForCheck();

        }

      });

      return;

    }

    // COMPANY -> Buyer conversations only

    this.conversationService
      .getBuyerConversations(this.buyerId)
      .subscribe({

        next: (data) => {

          this.conversations =
            data.sort((a, b) =>
              new Date(b.lastMessageAt).getTime()
              -
              new Date(a.lastMessageAt).getTime()
            );

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load conversations.',
            'danger'
          );

          this.cdr.markForCheck();

        }

      });

  }


  openConversation(id: number): void {

    this.selectedConversationId = id;

    this.router.navigate(
      [id],
      {
        relativeTo: this.route
      }
    );

  }


  getStatusClass(status: string): string {

    switch (status) {

      case 'ORDER_PLACED':
        return 'bg-primary';

      case 'QUOTED':
        return 'bg-warning text-dark';

      case 'QUOTE_ACCEPTED':
        return 'bg-info text-dark';

      case 'QUOTE_REJECTED':
      case 'BUYER_REJECTED':
      case 'REFUNDED':
        return 'bg-danger';

      case 'DELIVERED':
        return 'bg-secondary';

      case 'BUYER_ACCEPTED':
      case 'PAYMENT_RELEASED':
        return 'bg-success';

      case 'BUYER_CANCELLED':
      case 'SELLER_CANCELLED':
        return 'bg-dark';

      case 'SELLER_DISPUTED':
        return 'bg-warning';

      default:
        return 'bg-secondary';

    }

  }




}
