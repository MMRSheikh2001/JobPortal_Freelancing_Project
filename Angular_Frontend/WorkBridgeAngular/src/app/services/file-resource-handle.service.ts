import { Injectable } from '@angular/core';
import { environment } from '../../enviroments/environment';

@Injectable({
  providedIn: 'root',
})
export class FileResourceHandleService {



  private api = environment.apiUrl + 'files/';

  getUserProfileImage(fileName?: string): string {

    if (!fileName) {
      return 'assets/images/default-user.png';
    }

    return `${this.api}userprofiles/${fileName}`;
  }

  getCompanyProfileImage(fileName?: string): string {

    if (!fileName) {
      return 'assets/images/default-company.png';
    }

    return `${this.api}companyprofiles/${fileName}`;
  }

  getResume(fileName?: string): string {

    if (!fileName) {
      return '';
    }

    return `${this.api}resumes/${fileName}`;
  }

  getPortfolio(fileName?: string): string {

    if (!fileName) {
      return '';
    }

    return `${this.api}portfolios/${fileName}`;
  }


  getTraining(fileName?: string): string {

    if (!fileName) {
      return '';
    }

    return `${this.api}trainings/${fileName}`;
  }


  getMessageAttachment(fileName?: string): string {

    if (!fileName) {
      return '';
    }

    return `${this.api}messages/${fileName}`;
  }

  getGigDelivery(fileName?: string): string {

    if (!fileName) {
      return '';
    }

    return `${this.api}gigdeliveries/${fileName}`;
  }

  getGigImage(fileName?: string): string {

    if (!fileName) {
      return '';
    }

    return `${this.api}gigs/${fileName}`;
  }

  getReportFile(fileName?: string): string {

    if (!fileName) {
      return '';
    }

    return `${this.api}reports/${fileName}`;
  }

}
