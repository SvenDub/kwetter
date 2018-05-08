import {Injectable} from '@angular/core';
import {Subject} from 'rxjs/Subject';
import {Tweet} from '../shared/models/tweet.model';

declare var EventSource;

@Injectable()
export class SseService {

  private tweetCreated = new Subject<Tweet>();
  tweetCreated$ = this.tweetCreated.asObservable();

  startListener() {
    const eventSource = new EventSource('/api/events');
    eventSource.addEventListener('tweet_created', event => {
      this.tweetCreated.next(JSON.parse(event.data));
    });
  }
}
