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
  checkingRefresh = false;

  constructor(private loginService: LoginService, private jwtHelper: JwtHelperService) {
  }

  ngOnInit() {
    this.loggedIn = !this.jwtHelper.isTokenExpired();
    if (!this.loggedIn && !this.jwtHelper.isTokenExpired(localStorage.getItem('refresh_token'))) {
      this.refreshToken();
    }
    this.loginService.onLogout$.subscribe(() => this.loggedIn = false);
  }

  login() {
    this.loginService.login(this.username, this.password)
      .subscribe(resp => {
        localStorage.setItem('access_token', resp.accessToken);
        localStorage.setItem('refresh_token', resp.refreshToken);
        this.loggedIn = !this.jwtHelper.isTokenExpired();
      });
  }

  refreshToken() {
    this.checkingRefresh = true;
    this.loginService.refresh(localStorage.getItem('refresh_token'))
      .subscribe(resp => {
        localStorage.setItem('access_token', resp.accessToken);
        localStorage.setItem('refresh_token', resp.refreshToken);
        this.checkingRefresh = false;
        this.loggedIn = !this.jwtHelper.isTokenExpired();
      });
  }
}
