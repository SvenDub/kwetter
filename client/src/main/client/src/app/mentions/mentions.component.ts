import {Component, OnInit} from '@angular/core';
import {TweetService} from '../api/tweet.service';
import {Tweet} from '../shared/models/tweet.model';

@Component({
  selector: 'app-mentions',
  templateUrl: './mentions.component.html',
  styleUrls: ['./mentions.component.css']
})
export class MentionsComponent implements OnInit {

  mentions: Tweet[];

  constructor(private tweetService: TweetService) {
  }

  ngOnInit() {
    this.tweetService.getMentions()
      .subscribe(mentions => this.mentions = mentions);
  }

}
