import {Component, EventEmitter, OnInit} from '@angular/core';
import {TweetService} from '../api/tweet.service';
import {Tweet} from '../shared/models/tweet.model';
import {UserService} from '../api/user.service';
import {SseService} from '../api/sse.service';
import {UserSecure} from '../shared/models/user-secure.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  tweets: Tweet[];
  user: UserSecure;

  replyClicked = new EventEmitter<Tweet>();

  constructor(private tweetService: TweetService, private userService: UserService, private sseService: SseService) {
  }

  ngOnInit() {
    this.tweetService.getTimeline()
      .subscribe(tweets => this.tweets = tweets);

    this.tweetService.tweetPlaced$
      .subscribe(tweet => this.tweets.unshift(tweet));

    this.userService.getMe()
      .subscribe(user => this.user = user);

    this.sseService.tweetCreated$
      .subscribe(tweet => {
        if (!this.tweets.find(value => value.id === tweet.id)) {
          this.tweets.unshift(tweet);
          this.tweets.sort((a, b) => new Date(b.date) - new Date(a.date));
        }
      });
  }

  onReplyClicked(tweet: Tweet) {
    this.replyClicked.emit(tweet);
  }

}
