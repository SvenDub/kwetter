import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Tweet} from '../shared/models/tweet.model';
import {Subject} from 'rxjs/Subject';
import {tap} from 'rxjs/operators';
import {User} from '../shared/models/user.model';
import 'rxjs/add/operator/map';
import {LoginService} from './login.service';

@Injectable()
export class TweetService {

  private tweetPlaced = new Subject<Tweet>();
  tweetPlaced$ = this.tweetPlaced.asObservable();

  constructor(private http: HttpClient, private loginService: LoginService) {
  }

  getTimeline() {
    return this.http.get<Tweet[]>('/api/me/timeline');
  }

  addTweet(tweet: Tweet) {
    return this.http.post<Tweet>('/api/tweets', tweet)
      .pipe(tap(value => this.tweetPlaced.next(value)));
  }

  getAutocomplete(query: string) {
    return this.http.get<User[]>('/api/me/autocomplete', {params: {'q': query}});
  }

  getByHashtag(hashtag: string) {
    return this.http.get<Tweet[]>(`/api/tweets/hashtag/${hashtag}`);
  }

  getTrends() {
    return this.http.get<{ [p: string]: number }>('/api/me/trends');
  }

  getMentions() {
    return this.http.get<Tweet[]>('/api/me/mentions');
  }

  like(tweet: Tweet) {
    return this.http.post<Tweet>(`/api/tweets/${tweet.id}/like`, null);
  }

  flag(tweet: Tweet) {
    return this.http.post<Tweet>(`/api/tweets/${tweet.id}/flag`, null);
  }

  search(query: string) {
    return this.http.get<Tweet[]>(`/api/tweets/search`, {params: {'query': query}});
  }
}
