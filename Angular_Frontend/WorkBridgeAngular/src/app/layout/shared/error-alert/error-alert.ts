import { Component } from '@angular/core';
import { ErrorAlertService } from '../../../services/error-alert.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-error-alert',
  imports: [CommonModule],
  templateUrl: './error-alert.html',
  styleUrl: './error-alert.css',
})
export class ErrorAlert {



  constructor(
    public alert: ErrorAlertService
  ) {}


}
