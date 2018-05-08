import {Component, EventEmitter, OnInit} from '@angular/core';
import {TweetService} from '../api/tweet.service';
import {Tweet} from '../shared/models/tweet.model';
import {User} from '../shared/models/user.model';
import {UserService} from '../api/user.service';
import {LoginService} from '../api/login.service';
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
        if (!this.tweets.find(value => value.id === tweet.id) && this.user.following.find(value => value.id === tweet.owner.id)) {
          this.tweets.unshift(tweet);
          this.tweets.sort((a, b) => b.date.getUTCMilliseconds() - a.date.getUTCMilliseconds());
        }
      });
  }

  onReplyClicked(tweet: Tweet) {
    this.replyClicked.emit(tweet);
  }

}
