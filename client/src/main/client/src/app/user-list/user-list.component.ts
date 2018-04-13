import {Component, Input, OnInit} from '@angular/core';
import {User} from '../shared/models/user.model';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  @Input() title: string;
  @Input() users: User[];
  @Input() count: number = 0;

  constructor() { }

  ngOnInit() {
  }

}
