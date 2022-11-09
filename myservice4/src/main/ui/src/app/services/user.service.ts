import { Injectable } from '@angular/core';
import { User } from '../model/user';
import { Observable } from 'rxjs';
import { AppConstants } from '../shared/constants/app.constants';
import { ApiService } from '../shared/services/api.service';

@Injectable({
  providedIn: 'root'
})

export class UserService {

  constructor(private api: ApiService) { }

  url = 'users';

  getUsers(): Observable<any>{
    return this.api.httpGet(`${AppConstants.URLS.users}/${this.url}`);
  }

  addUser(user: User) {
    return this.api.httpPost(`${AppConstants.URLS.users}/${this.url}`, user);
  }

  removeUser(userId: string) {
    return this.api.httpDelete(`${AppConstants.URLS.users}/${this.url}/${userId}`);
  }
}
