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
import {TweetContentDirective} from './tweet-content.directive';
import {AnchorComponent} from './anchor/anchor.component';
import {TextComponent} from './text/text.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    HomeComponent,
    TimelineComponent,
    TweetComponent,
    ProfileComponent,
    TweetContentDirective,
    AnchorComponent,
    TextComponent
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
  ],
  entryComponents: [
    AnchorComponent,
    TextComponent
  ]
})
export class AppModule {
}
