import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {LoginService} from '../api/login.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  query: string;

  constructor(private router: Router, private loginService: LoginService) {
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
}
