import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { GigOrderResponseDTO } from '../../models/gig-order.model';
import { ConversationResponseModel } from '../../../conversation/models/conversation.model';
import { MessageResponseModel } from '../../../conversation/models/message.model';
import { ActivatedRoute } from '@angular/router';
import { ConversationService } from '../../../conversation/services/conversation.service';
import { GigOrderService } from '../../services/gig-order.service';
import { MessageService } from '../../../conversation/services/message.service';
import { ToastService } from '../../../services/toast.service';
import { FileResourceHandleService } from '../../../services/file-resource-handle.service';

@Component({
  selector: 'app-admin-gig-order-details',
  imports: [CommonModule],
  templateUrl: './admin-gig-order-details.html',
  styleUrl: './admin-gig-order-details.css',
})
export class AdminGigOrderDetails implements OnInit {





  gigOrderId = 0;

  loading = false;
  saving = false;

  order!: GigOrderResponseDTO;

  conversation!: ConversationResponseModel;

  messages: MessageResponseModel[] = [];

  constructor(
    private route: ActivatedRoute,
    private gigOrderService: GigOrderService,
    private conversationService: ConversationService,
    private messageService: MessageService,
    private toast: ToastService,
    public fileService: FileResourceHandleService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {

    this.route.paramMap.subscribe(params => {

      this.gigOrderId = Number(
        params.get('gigOrderId')
      );

      this.loadOrder();

    });

  }

  // =====================================
  // Load Order
  // =====================================

  loadOrder(): void {

    this.loading = true;

    this.gigOrderService
      .getById(this.gigOrderId)
      .subscribe({

        next: res => {

          this.order = res;

          this.loadConversation();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load order.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Load Conversation
  // =====================================

  loadConversation(): void {

    this.conversationService
      .getByGigOrderId(this.order.id)
      .subscribe({

        next: res => {

          this.conversation = res;

          this.loadMessages();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load conversation.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Load Messages
  // =====================================

  loadMessages(): void {

    this.messageService
      .getConversationMessages(this.conversation.id)
      .subscribe({

        next: res => {

          this.messages = res;

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load messages.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Release Payment
  // =====================================

  releasePayment(): void {

    if (!confirm(
      'Release payment to seller?'
    )) {
      return;
    }

    this.saving = true;

    this.gigOrderService
      .releasePayment(this.order.id)
      .subscribe({

        next: () => {

          this.toast.show(
            'Payment released.',
            'success'
          );

          this.saving = false;

          this.loadOrder();

        },

        error: () => {

          this.saving = false;

          this.toast.show(
            'Unable to release payment.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Refund Buyer
  // =====================================

  refundBuyer(): void {

    if (!confirm(
      'Refund buyer?'
    )) {
      return;
    }

    this.saving = true;

    this.gigOrderService
      .refundBuyer(this.order.id)
      .subscribe({

        next: () => {

          this.toast.show(
            'Buyer refunded.',
            'success'
          );

          this.saving = false;

          this.loadOrder();

        },

        error: () => {

          this.saving = false;

          this.toast.show(
            'Unable to refund buyer.',
            'danger'
          );

        }

      });

  }



}
