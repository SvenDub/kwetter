import {Component, OnInit} from '@angular/core';
import {TweetService} from '../api/tweet.service';
import {Tweet} from '../shared/models/tweet.model';

@Component({
  selector: 'app-add-tweet',
  templateUrl: './add-tweet.component.html',
  styleUrls: ['./add-tweet.component.css']
})
export class AddTweetComponent implements OnInit {

  content: string;
  errorMessage: string;

  constructor(private tweetService: TweetService) {
  }

  ngOnInit() {
  }

  tweet() {
    const tweet: Tweet = {
      content: this.content,
    };

    this.errorMessage = '';

    this.tweetService.addTweet(tweet)
      .subscribe(
        value => {
          this.content = '';
        },
        error => {
          this.errorMessage = 'Could not place tweet, please try again.';
        }
      );
  }

  isDisabled() {
    return !this.content || this.content.length > 140;
  }

}
