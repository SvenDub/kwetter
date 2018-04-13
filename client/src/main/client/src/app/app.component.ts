import {Component, OnInit} from '@angular/core';
import {JwtHelperService} from '@auth0/angular-jwt';
import {LoginService} from './api/login.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  username: string;
  password: string;
  loggedIn: boolean;

  constructor(private loginService: LoginService, private jwtHelper: JwtHelperService) {
  }

  ngOnInit() {
    this.loggedIn = !this.jwtHelper.isTokenExpired();
  }

  login() {
    this.loginService.login(this.username, this.password)
      .subscribe(resp => {
        const authorizationHeader = resp.headers.get('Authorization');
        const token = authorizationHeader.substr('Bearer'.length).trim();

        localStorage.setItem('access_token', token);
        window.location.reload();
      });
  }
}
