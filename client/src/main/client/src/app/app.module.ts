import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AngularFontAwesomeModule} from 'angular-font-awesome';
import {ApiModule} from './api/api.module';
import {AppComponent} from './app.component';
import {AppRoutingModule} from './app-routing.module';
import {HeaderComponent} from './header/header.component';
import {HomeComponent} from './home/home.component';
import {TimelineComponent} from './timeline/timeline.component';
import {SharedModule} from './shared/shared.module';
import {FormsModule} from '@angular/forms';
import {TweetComponent} from './tweet/tweet.component';
import {ProfileComponent} from './profile/profile.component';
import {AddTweetComponent} from './add-tweet/add-tweet.component';
import {AboutComponent} from './about/about.component';
import {HashtagComponent} from './hashtag/hashtag.component';
import {NotFoundComponent} from './not-found/not-found.component';
import {TrendsComponent} from './trends/trends.component';
import {ProfileCardComponent} from './profile-card/profile-card.component';
import {MentionsComponent} from './mentions/mentions.component';
import {UserListComponent} from './user-list/user-list.component';
import {EditProfileComponent} from './edit-profile/edit-profile.component';
import {SearchComponent} from './search/search.component';
import {JwtModule} from '@auth0/angular-jwt';
import {TokenListComponent} from './token-list/token-list.component';
import {MomentModule} from 'ngx-moment';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {HttpClient} from '@angular/common/http';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ToastModule} from 'ng2-toastr';

export function getAccessToken() {
  return localStorage.getItem('access_token');
}

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    HomeComponent,
    TimelineComponent,
    TweetComponent,
    ProfileComponent,
    AddTweetComponent,
    AboutComponent,
    HashtagComponent,
    NotFoundComponent,
    TrendsComponent,
    ProfileCardComponent,
    MentionsComponent,
    UserListComponent,
    EditProfileComponent,
    SearchComponent,
    TokenListComponent,
  ],
  imports: [
    AngularFontAwesomeModule,
    ApiModule,
    AppRoutingModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    JwtModule.forRoot({
      config: {
        tokenGetter: getAccessToken,
        whitelistedDomains: new Array(new RegExp('^null$')),
        blacklistedRoutes: [
          '/api/auth/login',
          '/api/auth/refresh'
        ]
      }
    }),
    MomentModule,
    SharedModule,
    ToastModule.forRoot(),
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient],
      }
    }),
  ],
  providers: [],
  bootstrap: [
    AppComponent,
  ]
})
export class AppModule {
}
