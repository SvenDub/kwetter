import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class LoginService {

  constructor(private http: HttpClient) {
  }

  login(username: string, password: string) {
    return this.http.post('/api/auth/login', null, {
      headers: {
        'Authorization': 'Basic ' + btoa(`${username}:${password}`)
      },
      observe: 'response'
    });
  }

}
