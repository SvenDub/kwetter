import {Component, Input, OnInit} from '@angular/core';
import {Tweet} from '../shared/models/tweet.model';

@Component({
  selector: 'app-timeline',
  templateUrl: './timeline.component.html',
  styleUrls: ['./timeline.component.css']
})
export class TimelineComponent implements OnInit {

  @Input() tweets: Tweet[];

  constructor() {
  }

  ngOnInit() {
  }

}
