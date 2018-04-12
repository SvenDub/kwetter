import {Component, OnInit} from '@angular/core';
import {LoginService} from '../api/login.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  username = 'SvenDub';
  query: string;

  constructor(private loginService: LoginService, private router: Router) {
  }

  ngOnInit() {
  }

  login() {
    this.loginService.apiKey = this.username;
  }

  onInput() {
    this.router.navigate(['/search/', {query: this.query}]);
  }

}
