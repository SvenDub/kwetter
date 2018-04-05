import {Component, Input, OnInit} from '@angular/core';
import {Tweet} from '../shared/models/tweet.model';
import * as moment from 'moment';

@Component({
  selector: 'app-tweet',
  templateUrl: './tweet.component.html',
  styleUrls: ['./tweet.component.css']
})
export class TweetComponent implements OnInit {

  @Input() tweet: Tweet;

  constructor() { }

  ngOnInit() {
  }

  private getRelativeDate() {
    return moment(this.tweet.date).fromNow();
  }

  private getAbsoluteDate() {
    return moment(this.tweet.date).format('lll');
  }

}
