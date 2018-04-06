import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params} from '@angular/router';
import {User} from '../shared/models/user.model';
import {UserService} from '../api/user.service';
import {Location} from '../shared/models/location.model';
import {Tweet} from '../shared/models/tweet.model';
import {TweetService} from '../api/tweet.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  private username: string;
  private user: User;
  private tweets: Tweet[];

  constructor(private route: ActivatedRoute, private userService: UserService, private tweetService: TweetService) {
  }

  ngOnInit() {
    this.route.params.subscribe(
      (params: Params) => {
        if (params['username']) {
          this.username = params['username'];

          this.userService.getByUsername(this.username)
            .subscribe(user => this.user = user);
          this.userService.getTweets(this.username)
            .subscribe(tweets => this.tweets = tweets);
        }
      }
    );
  }

  getLocationQuery(location: Location) {
    if (location.latitude && location.longitude) {
      return `https://maps.google.com/maps/place/${location.latitude},${location.longitude}`;
    } else {
      return null;
    }
  }

}
