import {Component, OnInit} from '@angular/core';
import {UserService} from '../api/user.service';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {User} from '../shared/models/user.model';
import {Profile} from '../shared/models/profile.model';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit {

  username: string;
  user: User;
  profile: Profile;
  errorMessage: string;

  constructor(private route: ActivatedRoute, private router: Router, private userService: UserService) {
  }

  ngOnInit() {
    this.route.params.subscribe(
      (params: Params) => {
        if (params['username']) {
          this.username = params['username'];

          this.refresh();
        }
      }
    );
  }

  refresh() {
    this.userService.getMe()
      .subscribe(me => {
        this.user = me;
        this.profile = JSON.parse(JSON.stringify(me.profile));
      });
  }

  createProfile() {
    this.errorMessage = '';
    this.userService.updateProfile(this.profile)
      .subscribe(profile => this.router.navigate(['/u/' + profile.username]),
        () => {
          this.errorMessage = 'Could not create profile. Check that all fields are correct and the username is not taken.';
        });
  }
}
