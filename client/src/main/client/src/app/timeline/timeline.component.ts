import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Tweet} from '../shared/models/tweet.model';
import {animate, animateChild, query, stagger, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-timeline',
  templateUrl: './timeline.component.html',
  styleUrls: ['./timeline.component.css'],
  animations: [
    trigger('fade', [
      transition(':enter', [style({opacity: 0, height: 0}), animate('.6s ease')])
    ]),
    trigger('stagger', [
      transition(':enter', [
        query(':enter', stagger('.1s', [animateChild()]))
      ])
    ])
  ]
})
export class TimelineComponent implements OnInit {

  @Input() tweets: Tweet[];
  @Output() replyClicked = new EventEmitter<Tweet>();

  constructor() {
  }

  ngOnInit() {
  }

}
