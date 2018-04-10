import {Injectable} from '@angular/core';

@Injectable()
export class LoginService {
  private _apiKey = 'SvenDub';

  get apiKey(): string {
    return this._apiKey;
  }

  set apiKey(value: string) {
    this._apiKey = value;
  }

}
