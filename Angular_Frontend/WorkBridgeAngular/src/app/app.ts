import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Toast } from './layout/shared/toast/toast';
import { ErrorAlert } from './layout/shared/error-alert/error-alert';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet,Toast,ErrorAlert],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('WorkBridgeAngular');
}
