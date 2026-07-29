import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ErrorAlertService {




  visible = false;

  message = '';

  show(message: string) {

    this.message = message;

    this.visible = true;

  }

  close() {

    this.visible = false;

  }





}
