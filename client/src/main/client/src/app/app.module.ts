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
import { AboutComponent } from './about/about.component';
import { HashtagComponent } from './hashtag/hashtag.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { TrendsComponent } from './trends/trends.component';
import { ProfileCardComponent } from './profile-card/profile-card.component';
import { MentionsComponent } from './mentions/mentions.component';
import { UserListComponent } from './user-list/user-list.component';
import { EditProfileComponent } from './edit-profile/edit-profile.component';
import { SearchComponent } from './search/search.component';

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
    SearchComponent
  ],
  imports: [
    AngularFontAwesomeModule,
    ApiModule,
    AppRoutingModule,
    BrowserModule,
    FormsModule,
    SharedModule
  ],
  providers: [],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule {
}
