import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from '../shared/models/user.model';
import {Tweet} from '../shared/models/tweet.model';
import {LoginService} from './login.service';
import {UserSecure} from '../shared/models/user-secure.model';
import {Profile} from '../shared/models/profile.model';

@Injectable()
export class UserService {

  constructor(private http: HttpClient, private loginService: LoginService) {
  }

  getByUsername(username: string) {
    return this.http.get<User>(`/api/users/${username}`);
  }

  getTweets(username: string) {
    return this.http.get<Tweet[]>(`/api/users/${username}/tweets`);
  }

  getMe() {
    return this.http.get<UserSecure>(`/api/me`);
  }

  getFollowing(username: string) {
    return this.http.get<User[]>(`/api/users/${username}/following`);
  }

  getFollowers(username: string) {
    return this.http.get<User[]>(`/api/users/${username}/followers`);
  }

  getLikes(username: string) {
    return this.http.get<Tweet[]>(`/api/users/${username}/likes`);
  }

  follow(username: string) {
    return this.http.post(`/api/users/${username}/follow`, null);
  }

  unfollow(username: string) {
    return this.http.post(`/api/users/${username}/unfollow`, null);
  }

  updateProfile(profile: Profile) {
    return this.http.post<Profile>(`/api/me/profile`, profile);
  }
}
