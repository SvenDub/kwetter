import {Component, OnInit, Output} from '@angular/core';
import {TweetService} from '../api/tweet.service';
import {Tweet} from '../shared/models/tweet.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  private tweets: Tweet[];

  constructor(private tweetService: TweetService) { }

  ngOnInit() {
    this.tweetService.getTimeline()
      .subscribe(tweets => this.tweets = tweets);
  }

}
