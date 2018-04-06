import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'span[app-anchor]',
  templateUrl: './anchor.component.html',
  styleUrls: ['./anchor.component.css']
})
export class AnchorComponent implements OnInit {

  @Input() link: Array;
  @Input() content: string;

  constructor() { }

  ngOnInit() {
  }

}
