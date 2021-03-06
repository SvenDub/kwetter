import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './home/home.component';
import {ProfileComponent} from './profile/profile.component';
import {HashtagComponent} from './hashtag/hashtag.component';
import {NotFoundComponent} from './not-found/not-found.component';
import {MentionsComponent} from './mentions/mentions.component';
import {EditProfileComponent} from './edit-profile/edit-profile.component';
import {SearchComponent} from './search/search.component';
import {TokenListComponent} from './token-list/token-list.component';

const routes: Routes = [
  {path: '', component: HomeComponent, pathMatch: 'full'},
  {path: 'u/:username', component: ProfileComponent},
  {path: 'u/:username/edit', component: EditProfileComponent},
  {path: 'u/:username/tokens', component: TokenListComponent},
  {path: 'hashtag/:hashtag', component: HashtagComponent},
  {path: 'mentions', component: MentionsComponent},
  {path: 'search', component: SearchComponent},
  {path: '**', component: NotFoundComponent}
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule {
}
