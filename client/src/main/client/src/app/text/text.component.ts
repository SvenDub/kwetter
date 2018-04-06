import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'span[app-text]',
  templateUrl: './text.component.html',
  styleUrls: ['./text.component.css']
})
export class TextComponent implements OnInit {

  @Input() content: string;

  constructor() { }

  ngOnInit() {
  }

}
