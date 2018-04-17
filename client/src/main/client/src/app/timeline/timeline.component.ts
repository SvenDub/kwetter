import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Tweet} from '../shared/models/tweet.model';

@Component({
  selector: 'app-timeline',
  templateUrl: './timeline.component.html',
  styleUrls: ['./timeline.component.css']
})
export class TimelineComponent implements OnInit {

  @Input() tweets: Tweet[];
  @Output() replyClicked = new EventEmitter<Tweet>();

  constructor() {
  }

  ngOnInit() {
  }

}
