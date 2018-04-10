import { Component, OnInit } from '@angular/core';
import moment = require('moment');

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.css']
})
export class AboutComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

  getYear() {
    return moment().year();
  }

}
