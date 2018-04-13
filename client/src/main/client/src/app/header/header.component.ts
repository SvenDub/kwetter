import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  query: string;

  constructor(private router: Router) {
  }

  ngOnInit() {
  }

  onInput() {
    this.router.navigate(['/search/', {query: this.query}]);
  }

  logout() {
    localStorage.removeItem('access_token');
    window.location.reload();
    return false;
  }
}
