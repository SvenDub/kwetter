import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Tweet} from '../shared/models/tweet.model';
import {User} from '../shared/models/user.model';
import {TweetService} from '../api/tweet.service';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-tweet',
  templateUrl: './tweet.component.html',
  styleUrls: ['./tweet.component.css']
})
export class TweetComponent implements OnInit {

  @Input() tweet: Tweet;
  @Output() replyClicked = new EventEmitter<Tweet>();

  constructor(private tweetService: TweetService, private translateService: TranslateService) {
  }

  ngOnInit() {
  }

  getWordGroups() {
    const mentions = new Map<string, User>();
    Object.keys(this.tweet.mentions).forEach(key => {
      mentions.set(key, this.tweet.mentions[key]);
    });

    return this.tweet.content.split(/(#[a-zA-Z0-9_]+)|(@[a-zA-Z0-9_]+)/)
      .filter(value => value)
      .map(value => {
        if (value.startsWith('#') && this.tweet.hashtags.includes(value)) {
          return {
            value: value,
            link: value.substr(1),
            type: 'hashtag'
          };
        } else if (value.startsWith('@') && mentions.has(value.substr(1))) {
          return {
            value: value,
            link: mentions.get(value.substr(1)).profile.username,
            type: 'mention'
          };
        } else {
          return {
            value: value,
            link: null,
            type: 'text'
          };
        }
      });
  }

  getLikesCount() {
    // .size should exist on Set, but it doesn't. So we use .length which shouldn't exist but does. ¯\_(ツ)_/¯
    return (<object>this.tweet.likedBy)['length'];
  }

  like() {
    this.tweetService.like(this.tweet).subscribe(value => this.tweet = value);
  }

  flag() {
    this.tweetService.flag(this.tweet).subscribe(value => this.tweet = value);
  }

  getLocale() {
    return this.translateService.currentLang;
  }
}
