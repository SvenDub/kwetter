import {Component, Input, OnInit} from '@angular/core';
import {User} from '../shared/models/user.model';
import {UserService} from '../api/user.service';

@Component({
  selector: 'app-profile-card',
  templateUrl: './profile-card.component.html',
  styleUrls: ['./profile-card.component.css']
})
export class ProfileCardComponent implements OnInit {

  @Input() user: User;

  constructor(private userService: UserService) {
  }

  ngOnInit() {
  }

}
