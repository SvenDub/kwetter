import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from '../shared/models/user.model';
import {Tweet} from '../shared/models/tweet.model';
import {LoginService} from './login.service';

@Injectable()
export class UserService {

  constructor(private http: HttpClient, private loginService: LoginService) {
  }

  getByUsername(username: string) {
    return this.http.get<User>(`/api/users/${username}`, {headers: {'X-API-KEY': this.loginService.apiKey}});
  }

  getTweets(username: string) {
    return this.http.get<Tweet[]>(`/api/users/${username}/tweets`, {headers: {'X-API-KEY': this.loginService.apiKey}});
  }

  getMe() {
    return this.http.get<User>(`/api/me`, {headers: {'X-API-KEY': this.loginService.apiKey}});
  }
}
