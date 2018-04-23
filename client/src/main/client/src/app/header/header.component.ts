import {Component, Input, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {LoginService} from '../api/login.service';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  @Input() loggedIn: boolean;
  query: string;

  flagIcons: Map<string, string> = new Map<string, string>([
    ['nl', 'nl'],
    ['en', 'gb']
  ]);

  constructor(private router: Router, private loginService: LoginService, private translate: TranslateService) {
  }

  ngOnInit() {
  }

  onInput() {
    this.router.navigate(['/search/', {query: this.query}]);
  }

  logout() {
    this.loginService.logout();
    return false;
  }

  setLanguage(lang: string) {
    this.translate.use(lang);
  }

  getLanguage() {
    return this.translate.currentLang;
  }
}
