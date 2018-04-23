import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params} from '@angular/router';
import {User} from '../shared/models/user.model';
import {UserService} from '../api/user.service';
import {Location} from '../shared/models/location.model';
import {Tweet} from '../shared/models/tweet.model';
import {UserSecure} from '../shared/models/user-secure.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  username: string;
  user: User;
  tweets: Tweet[];
  likes: Tweet[];
  me: UserSecure;
  following: User[];
  followers: User[];

  constructor(private route: ActivatedRoute, private userService: UserService) {
  }

  ngOnInit() {
    this.route.params.subscribe(
      (params: Params) => {
        if (params['username']) {
          this.username = params['username'];

          this.refresh();
        }
      }
    );
  }

  refresh() {
    this.userService.getByUsername(this.username)
      .subscribe(user => this.user = user);
    this.userService.getTweets(this.username)
      .subscribe(tweets => this.tweets = tweets);
    this.userService.getMe()
      .subscribe(me => this.me = me);
    this.userService.getFollowing(this.username)
      .subscribe(following => this.following = following);
    this.userService.getFollowers(this.username)
      .subscribe(followers => this.followers = followers);
    this.userService.getLikes(this.username)
      .subscribe(likes => this.likes = likes);
  }

  getLocationQuery(location: Location) {
    if (location.latitude && location.longitude) {
      return `https://maps.google.com/maps/place/${location.latitude},${location.longitude}`;
    } else {
      return null;
    }
  }

  follow() {
    this.userService.follow(this.username)
      .subscribe(() => this.refresh());
  }

  unfollow() {
    this.userService.unfollow(this.username)
      .subscribe(() => this.refresh());
  }

  isFollowing() {
    return this.me && this.user && this.me.following.some(value => value.id === this.user.id);
  }

  isMe() {
    return this.me && this.user && this.me.id === this.user.id;
  }

}
