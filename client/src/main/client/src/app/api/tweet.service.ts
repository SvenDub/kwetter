import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Tweet} from '../shared/models/tweet.model';
import {Subject} from 'rxjs/Subject';
import {tap} from 'rxjs/operators';
import {User} from '../shared/models/user.model';
import 'rxjs/add/operator/map';

@Injectable()
export class TweetService {

  private tweetPlaced = new Subject<Tweet>();
  tweetPlaced$ = this.tweetPlaced.asObservable();

  constructor(private http: HttpClient) {
  }

  getTimeline() {
    return this.http.get<Tweet[]>('/api/me/timeline', {headers: {'X-API-KEY': 'SvenDub'}});
  }

  addTweet(tweet: Tweet) {
    return this.http.post<Tweet>('/api/tweets', tweet, {headers: {'X-API-KEY': 'SvenDub'}})
      .pipe(tap(value => this.tweetPlaced.next(value)));
  }

  getAutocomplete(query: string) {
    return this.http.get<User[]>('/api/me/autocomplete', {headers: {'X-API-KEY': 'SvenDub'}, params: {'q': query}});
  }

  getByHashtag(hashtag: string) {
    return this.http.get<Tweet[]>(`/api/tweets/hashtag/${hashtag}`);
  }

  getTrends() {
    return this.http.get<{ [p: string]: number }>('/api/me/trends', {headers: {'X-API-KEY': 'SvenDub'}});
  }

  getMentions() {
    return this.http.get<Tweet[]>('/api/me/mentions', {headers: {'X-API-KEY': 'SvenDub'}});
  }

  like(tweet: Tweet) {
    return this.http.post<Tweet>(`/api/tweets/${tweet.id}/like`, null, {headers: {'X-API-KEY': 'SvenDub'}});
  }

  flag(tweet: Tweet) {
    return this.http.post<Tweet>(`/api/tweets/${tweet.id}/flag`, null, {headers: {'X-API-KEY': 'SvenDub'}});
  }
}
