import {Injectable} from '@angular/core';
import {Subject} from 'rxjs/Subject';
import {Tweet} from '../shared/models/tweet.model';

@Injectable()
export class SseService {

  private tweetCreated = new Subject<Tweet>();
  tweetCreated$ = this.tweetCreated.asObservable();

  private socket: WebSocket;

  startListener() {
    if (this.socket) {
      this.socket.close();
    }

    const token = localStorage.getItem('access_token');

    this.socket = new WebSocket(`ws://${window.location.host}/ws/events?authToken=${token}`);
    this.socket.onmessage = e => {
      const event = JSON.parse(e.data);
      if (event.type === 'tweet.created') {
        this.tweetCreated.next(event.payload);
      }
    };
  }
}
