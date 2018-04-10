import {Component, OnInit} from '@angular/core';
import {Tweet} from '../shared/models/tweet.model';
import {TweetService} from '../api/tweet.service';
import {ActivatedRoute, Params} from '@angular/router';

@Component({
  selector: 'app-hashtag',
  templateUrl: './hashtag.component.html',
  styleUrls: ['./hashtag.component.css']
})
export class HashtagComponent implements OnInit {

  tweets: Tweet[];
  hashtag: string;

  constructor(private route: ActivatedRoute, private tweetService: TweetService) {
  }

  ngOnInit() {
    this.route.params.subscribe(
      (params: Params) => {
        if (params['hashtag']) {
          this.hashtag = params['hashtag'];

          this.tweetService.getByHashtag(this.hashtag)
            .subscribe(tweets => this.tweets = tweets);
        }
      }
    );
  }

}
