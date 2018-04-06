import {Component, Input, OnInit} from '@angular/core';
import {Tweet} from '../shared/models/tweet.model';
import * as moment from 'moment';
import {User} from '../shared/models/user.model';

@Component({
  selector: 'app-tweet',
  templateUrl: './tweet.component.html',
  styleUrls: ['./tweet.component.css']
})
export class TweetComponent implements OnInit {

  @Input() tweet: Tweet;

  constructor() {
  }

  ngOnInit() {
  }

  private getWords() {
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

  private getRelativeDate() {
    return moment(this.tweet.date).fromNow();
  }

  private getAbsoluteDate() {
    return moment(this.tweet.date).format('lll');
  }

}
