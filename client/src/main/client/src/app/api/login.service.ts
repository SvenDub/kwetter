import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {TokenResponse} from '../shared/models/token-response.model';
import {Subject} from 'rxjs/Subject';
import {Token} from '../shared/models/token.model';

@Injectable()
export class LoginService {

  private onLogout = new Subject();
  onLogout$ = this.onLogout.asObservable();

  constructor(private http: HttpClient) {
  }

  login(username: string, password: string) {
    return this.http.post<TokenResponse>('/api/auth/login', null, {
      headers: {
        'Authorization': 'Basic ' + btoa(`${username}:${password}`)
      }
    });
  }

  refresh(refreshToken: string) {
    return this.http.post<TokenResponse>('/api/auth/refresh', null, {
      headers: {
        'Authorization': `Bearer ${refreshToken}`
      }
    });
  }

  logout() {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    this.onLogout.next();
  }

  getTokens() {
    return this.http.get<Token[]>('/api/me/tokens');
  }

  deleteToken(token: Token) {
    return this.http.delete(`/api/me/tokens/${token.id}`);
  }
}
