import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Tweet} from '../shared/models/tweet.model';

@Injectable()
export class TweetService {

  constructor(private http: HttpClient) {
  }

  getTimeline() {
    return this.http.get<Tweet[]>('/api/me/timeline', {headers: {'X-API-KEY': 'SvenDub'}});
  }
}
