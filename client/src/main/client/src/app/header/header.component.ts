import {Component, OnInit} from '@angular/core';
import {LoginService} from '../api/login.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  username = 'SvenDub';

  constructor(private loginService: LoginService) {
  }

  ngOnInit() {
  }

  login() {
    this.loginService.apiKey = this.username;
  }

}
