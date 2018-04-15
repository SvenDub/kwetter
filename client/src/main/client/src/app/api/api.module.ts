import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TweetService} from './tweet.service';
import {SharedModule} from '../shared/shared.module';
import {HttpClientModule} from '@angular/common/http';
import {UserService} from './user.service';
import {LoginService} from './login.service';
import {httpInterceptorProviders} from './http-interceptors';

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
    SharedModule
  ],
  providers: [
    LoginService,
    TweetService,
    UserService,
    httpInterceptorProviders
  ],
  declarations: []
})
export class ApiModule {
}
