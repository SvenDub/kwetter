import {Component, OnInit} from '@angular/core';
import {TweetService} from '../api/tweet.service';

@Component({
  selector: 'app-trends',
  templateUrl: './trends.component.html',
  styleUrls: ['./trends.component.css']
})
export class TrendsComponent implements OnInit {

  trends: [string, number][];

  constructor(private tweetService: TweetService) {
  }

  ngOnInit() {
    this.tweetService.getTrends()
      .subscribe(trends => {
        this.trends = Object.entries(trends)
          .sort((a, b) => b[1] - a[1]);
      });
  }

}
