import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ConversationResponseModel } from '../../models/conversation.model';
import { MessageResponseModel } from '../../models/message.model';
import { ActivatedRoute } from '@angular/router';
import { ConversationService } from '../../services/conversation.service';
import { MessageService } from '../../services/message.service';
import { ToastService } from '../../../services/toast.service';
import { FileResourceHandleService } from '../../../services/file-resource-handle.service';
import { StorageService } from '../../../auth/services/storage.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-conversation-chat',
  imports: [CommonModule, FormsModule],
  templateUrl: './conversation-chat.html',
  styleUrl: './conversation-chat.css',
})
export class ConversationChat implements OnInit {


  //Properties 

  conversationId = 0;

  conversation!: ConversationResponseModel;

  messages: MessageResponseModel[] = [];

  loading = false;

  currentProfileId = 0;

  messageText = '';

  selectedFile?: File;

  sending = false;

  loggedInUserId = 0;

  //Contructors

  constructor(

    private route: ActivatedRoute,

    private conversationService: ConversationService,

    private messageService: MessageService,

    private toast: ToastService,

    public fileService: FileResourceHandleService,

    private cdr: ChangeDetectorRef,
    private storage: StorageService

  ) { }



  ngOnInit(): void {

    this.route.paramMap.subscribe(param => {

      this.conversationId =
        Number(param.get('conversationId'));

      this.currentProfileId =
        this.storage.getProfileId() ?? 0;

      this.loggedInUserId =
        this.storage.getUserId() ?? 0;

      this.loadConversation();

      this.loadMessages();

    });

  }

  loadConversation(): void {

    this.loading = true;

    this.conversationService
      .getById(this.conversationId)
      .subscribe({

        next: data => {

          this.conversation = data;

          this.loading = false;

          this.cdr.markForCheck();

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

  loadMessages(): void {

    this.messageService
      .getConversationMessages(this.conversationId)
      .subscribe({

        next: data => {

          this.messages = data;

          // Mark messages as read
          this.messageService
            .markConversationAsRead(
              this.conversationId,
              this.loggedInUserId
            )
            .subscribe();

          this.cdr.markForCheck();

          this.scrollToBottom();

        },

        error: () => {

          this.toast.show(
            'Unable to load messages.',
            'danger'
          );

        }

      });

  }

  onFileSelected(event: Event): void {

    const input =
      event.target as HTMLInputElement;

    if (input.files && input.files.length > 0) {

      this.selectedFile = input.files[0];

    }

  }


  sendMessage(): void {

    if (
      !this.messageText.trim()
      &&
      !this.selectedFile
    ) {
      return;
    }

    this.sending = true;

    this.messageService
      .sendMessage(
        {
          messageText: this.messageText,
          conversationId: this.conversationId
        },
        this.storage.getUserId() ?? 0,
        this.selectedFile
      )
      .subscribe({

        next: () => {

          this.messageText = '';

          this.selectedFile = undefined;

          this.loadMessages();

          this.sending = false;

        },

        error: () => {

          this.toast.show(
            'Unable to send message.',
            'danger'
          );

          this.sending = false;

        }

      });

  }


  scrollToBottom(): void {

    setTimeout(() => {

      const body =
        document.querySelector('.chat-body');

      if (body) {

        body.scrollTop =
          body.scrollHeight;

      }

    });

  }




}
