import { Injectable } from '@angular/core';

export interface Toast {

  id: number;

  message: string;

  type: 'success' | 'danger' | 'warning' | 'info';

}

@Injectable({
  providedIn: 'root',
})
export class ToastService {



  toasts: Toast[] = [];

  private id = 1;

  show(

    message: string,

    type: 'success' | 'danger' | 'warning' | 'info' = 'success'

  ) {

    const toast: Toast = {

      id: this.id++,

      message,

      type

    };

    this.toasts.push(toast);

    setTimeout(() => {

      this.remove(toast.id);

    }, 3000);

  }

  remove(id: number) {

    this.toasts = this.toasts.filter(t => t.id !== id);

  }

}
