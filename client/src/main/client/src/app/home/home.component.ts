import {Component, EventEmitter, OnInit} from '@angular/core';
import {TweetService} from '../api/tweet.service';
import {Tweet} from '../shared/models/tweet.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  tweets: Tweet[];

  replyClicked = new EventEmitter<Tweet>();

  constructor(private tweetService: TweetService) {
  }

  ngOnInit() {
    this.tweetService.getTimeline()
      .subscribe(tweets => this.tweets = tweets);

    this.tweetService.tweetPlaced$
      .subscribe(tweet => this.tweets.unshift(tweet));
  }

  onReplyClicked(tweet: Tweet) {
    this.replyClicked.emit(tweet);
  }

}
