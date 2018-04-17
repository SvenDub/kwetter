import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {Tweet} from '../shared/models/tweet.model';
import {TweetService} from '../api/tweet.service';
import {User} from '../shared/models/user.model';
import {UserService} from '../api/user.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  query: string;
  tweets: Tweet[];
  user: User;

  constructor(private route: ActivatedRoute, private tweetService: TweetService, private userService: UserService) {
  }

  ngOnInit() {
    this.route.params.subscribe(
      (params: Params) => {
        if (params['query']) {
          this.query = params['query'];

          this.refresh();
        }
      }
    );
  }

  refresh() {
    this.tweetService.search(this.query)
      .subscribe(tweets => this.tweets = tweets);
    this.userService.getMe()
      .subscribe(me => this.user = me);
  }

}
