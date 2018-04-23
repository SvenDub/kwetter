import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {LoginService} from './login.service';
import {JwtHelperService} from '@auth0/angular-jwt';
import * as moment from 'moment';

@Injectable()
export class JwtRefreshInterceptor implements HttpInterceptor {

  constructor(private loginService: LoginService, private jwtHelper: JwtHelperService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!req.url.startsWith('/api/auth/')) {
      const token = localStorage.getItem('access_token');
      if (!token || moment(this.jwtHelper.getTokenExpirationDate(token)).subtract(5, 'minutes').isBefore(new Date())) {
        console.log('Refreshing token');
        this.loginService.refresh(localStorage.getItem('refresh_token'))
          .subscribe(resp => {
            localStorage.setItem('access_token', resp.accessToken);
            localStorage.setItem('refresh_token', resp.refreshToken);
          });
      }
    }
    return next.handle(req);
  }

}
