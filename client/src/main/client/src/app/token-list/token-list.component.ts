import {Component, OnInit} from '@angular/core';
import {LoginService} from '../api/login.service';
import {User} from '../shared/models/user.model';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {UserService} from '../api/user.service';
import {Location} from '../shared/models/location.model';
import {Token} from '../shared/models/token.model';

@Component({
  selector: 'app-token-list',
  templateUrl: './token-list.component.html',
  styleUrls: ['./token-list.component.css']
})
export class TokenListComponent implements OnInit {

  username: string;
  user: User;
  following: User[];
  followers: User[];
  tokens: Token[];

  constructor(private route: ActivatedRoute, private router: Router, private userService: UserService, private loginService: LoginService) {
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
    this.userService.getMe()
      .subscribe(me => {
        this.user = me;
      });
    this.userService.getFollowing(this.username)
      .subscribe(following => this.following = following);
    this.userService.getFollowers(this.username)
      .subscribe(followers => this.followers = followers);
    this.loginService.getTokens()
      .subscribe(tokens => this.tokens = tokens);
  }

  getLocationQuery(location: Location) {
    if (location.latitude && location.longitude) {
      return `https://maps.google.com/maps/place/${location.latitude},${location.longitude}`;
    } else {
      return null;
    }
  }

  revoke(token: Token) {
    this.loginService.deleteToken(token)
      .subscribe(() => this.refresh());
  }
}
