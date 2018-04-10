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
    return this.http.get<Tweet[]>('/api/me/timeline', {headers: {'X-API-KEY': this.loginService.apiKey}});
  }

  addTweet(tweet: Tweet) {
    return this.http.post<Tweet>('/api/tweets', tweet, {headers: {'X-API-KEY': this.loginService.apiKey}})
      .pipe(tap(value => this.tweetPlaced.next(value)));
  }

  getAutocomplete(query: string) {
    return this.http.get<User[]>('/api/me/autocomplete', {headers: {'X-API-KEY': this.loginService.apiKey}, params: {'q': query}});
  }

  getByHashtag(hashtag: string) {
    return this.http.get<Tweet[]>(`/api/tweets/hashtag/${hashtag}`, {headers: {'X-API-KEY': this.loginService.apiKey}});
  }

  getTrends() {
    return this.http.get<{ [p: string]: number }>('/api/me/trends', {headers: {'X-API-KEY': this.loginService.apiKey}});
  }

  getMentions() {
    return this.http.get<Tweet[]>('/api/me/mentions', {headers: {'X-API-KEY': this.loginService.apiKey}});
  }

  like(tweet: Tweet) {
    return this.http.post<Tweet>(`/api/tweets/${tweet.id}/like`, null, {headers: {'X-API-KEY': this.loginService.apiKey}});
  }

  flag(tweet: Tweet) {
    return this.http.post<Tweet>(`/api/tweets/${tweet.id}/flag`, null, {headers: {'X-API-KEY': this.loginService.apiKey}});
  }
}
