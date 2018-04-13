import {Component, OnInit} from '@angular/core';
import {LoginService} from '../api/login.service';
import {Router} from '@angular/router';
import {JwtHelperService} from '@auth0/angular-jwt';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  username: string;
  password: string;
  query: string;
  loggedIn: boolean;

  constructor(private loginService: LoginService, private router: Router, private jwtHelper: JwtHelperService) {
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

  onInput() {
    this.router.navigate(['/search/', {query: this.query}]);
  }

}
