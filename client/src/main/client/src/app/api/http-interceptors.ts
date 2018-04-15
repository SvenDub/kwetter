import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {JwtRefreshInterceptor} from './jwt-refresh.interceptor';

export const httpInterceptorProviders = [
  {provide: HTTP_INTERCEPTORS, useClass: JwtRefreshInterceptor, multi: true},
];
