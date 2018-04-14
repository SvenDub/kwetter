import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {TokenResponse} from '../shared/models/token-response.model';

@Injectable()
export class LoginService {

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
  }
}
