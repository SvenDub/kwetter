import {Component, EventEmitter, OnInit} from '@angular/core';
import {TweetService} from '../api/tweet.service';
import {Tweet} from '../shared/models/tweet.model';
import {User} from '../shared/models/user.model';
import {UserService} from '../api/user.service';
import {LoginService} from '../api/login.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  tweets: Tweet[];
  user: User;

  replyClicked = new EventEmitter<Tweet>();

  constructor(private tweetService: TweetService, private userService: UserService) {
  }

  ngOnInit() {
    this.tweetService.getTimeline()
      .subscribe(tweets => this.tweets = tweets);

    this.tweetService.tweetPlaced$
      .subscribe(tweet => this.tweets.unshift(tweet));

    this.userService.getMe()
      .subscribe(user => this.user = user);
  }

  onReplyClicked(tweet: Tweet) {
    this.replyClicked.emit(tweet);
  }

}
