import {Component, OnInit} from '@angular/core';
import {TweetService} from '../api/tweet.service';
import {Tweet} from '../shared/models/tweet.model';
import {UserService} from '../api/user.service';
import {User} from '../shared/models/user.model';

@Component({
  selector: 'app-mentions',
  templateUrl: './mentions.component.html',
  styleUrls: ['./mentions.component.css']
})
export class MentionsComponent implements OnInit {

  mentions: Tweet[];
  user: User;

  constructor(private tweetService: TweetService, private userService: UserService) {
  }

  ngOnInit() {
    this.tweetService.getMentions()
      .subscribe(mentions => this.mentions = mentions);

    this.userService.getMe()
      .subscribe(user => this.user = user);
  }

}
