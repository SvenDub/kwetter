import {Component, OnInit} from '@angular/core';
import {JwtHelperService} from '@auth0/angular-jwt';
import {LoginService} from './api/login.service';
import {HttpErrorResponse} from '@angular/common/http';
import {Router} from '@angular/router';
import {LangChangeEvent, TranslateService} from '@ngx-translate/core';
import {Title} from '@angular/platform-browser';
import {SseService} from './api/sse.service';

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

  signUpEmail: string;
  signUpName: string;
  signUpUsername: string;
  signUpPassword: string;

  errorMessage: string;

  constructor(private loginService: LoginService, private jwtHelper: JwtHelperService, private router: Router,
              private translate: TranslateService, private title: Title, private sseService: SseService) {
    this.translate.setDefaultLang('en');

    const lang = localStorage.getItem('lang');
    if (lang) {
      this.translate.use(lang);
    } else {
      this.translate.use(this.translate.getBrowserLang());
    }

    this.translate.get('BRAND').subscribe(value => this.title.setTitle(value));

    this.translate.onLangChange.subscribe((params: LangChangeEvent) => {
      localStorage.setItem('lang', params.lang);
      this.translate.get('BRAND').subscribe(value => this.title.setTitle(value));
    });
  }

  ngOnInit() {
    this.errorMessage = null;
    this.loggedIn = !this.jwtHelper.isTokenExpired();

    this.loginService.onLogout$.subscribe(() => this.loggedIn = false);

    if (this.loggedIn) {
      this.sseService.startListener();
    } else if (!this.jwtHelper.isTokenExpired(localStorage.getItem('refresh_token'))) {
      this.refreshToken();
    } else {
      this.loginService.logout();
    }
  }

  login() {
    this.errorMessage = null;
    this.loginService.login(this.username, this.password)
      .subscribe(resp => {
        localStorage.setItem('access_token', resp.accessToken);
        localStorage.setItem('refresh_token', resp.refreshToken);
        this.loggedIn = !this.jwtHelper.isTokenExpired();

        if (this.loggedIn) {
          this.sseService.startListener();
        }
      });
  }

  refreshToken() {
    this.errorMessage = null;
    this.checkingRefresh = true;
    this.loginService.refresh(localStorage.getItem('refresh_token'))
      .subscribe(resp => {
        localStorage.setItem('access_token', resp.accessToken);
        localStorage.setItem('refresh_token', resp.refreshToken);
        this.checkingRefresh = false;
        this.loggedIn = !this.jwtHelper.isTokenExpired();
      }, () => {
        console.log('Can\'t log in with invalid token. Logging out.');
        this.loginService.logout();
      });
  }

  signUp() {
    this.errorMessage = null;
    this.loginService.signUp(this.signUpEmail, this.signUpName, this.signUpUsername, this.signUpPassword)
      .subscribe(resp => {
        localStorage.setItem('access_token', resp.accessToken);
        localStorage.setItem('refresh_token', resp.refreshToken);

        this.loggedIn = !this.jwtHelper.isTokenExpired();
        this.router.navigate(['/u/' + this.signUpUsername + '/edit']);

        this.sseService.startListener();
      }, (err: HttpErrorResponse) => {
        console.log('Invalid sign up parameters.', err);
        this.errorMessage = `Can't create account: ${err.error.message ? err.error.message : err.message}`;
      });
  }
}
