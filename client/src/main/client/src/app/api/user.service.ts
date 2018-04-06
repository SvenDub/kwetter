import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from '../shared/models/user.model';
import {Tweet} from '../shared/models/tweet.model';

@Injectable()
export class UserService {

  constructor(private http: HttpClient) {
  }

  getByUsername(username: string) {
    return this.http.get<User>(`/api/users/${username}`);
  }

  getTweets(username: string) {
    return this.http.get<Tweet[]>(`/api/users/${username}/tweets`);
  }
}
