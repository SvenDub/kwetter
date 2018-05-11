import {Injectable} from '@angular/core';
import {Subject} from 'rxjs/Subject';
import {Tweet} from '../shared/models/tweet.model';

@Injectable()
export class SseService {

  private tweetCreated = new Subject<Tweet>();
  tweetCreated$ = this.tweetCreated.asObservable();

  startListener() {
    const token = localStorage.getItem('access_token');

    const socket = new WebSocket(`ws://${window.location.host}/ws/events?authToken=${token}`);
    socket.onmessage = e => {
      const event = JSON.parse(e.data);
      if (event.type === 'tweet.created') {
        this.tweetCreated.next(event.payload);
      }
    };
  }
}
