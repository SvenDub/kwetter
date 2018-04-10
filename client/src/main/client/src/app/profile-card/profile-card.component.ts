import {Component, OnInit} from '@angular/core';
import {User} from '../shared/models/user.model';
import {UserService} from '../api/user.service';

@Component({
  selector: 'app-profile-card',
  templateUrl: './profile-card.component.html',
  styleUrls: ['./profile-card.component.css']
})
export class ProfileCardComponent implements OnInit {

  user: User;

  constructor(private userService: UserService) {
  }

  ngOnInit() {
    this.userService.getByUsername('SvenDub')
      .subscribe(value => this.user = value);
  }

}
