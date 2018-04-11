import {User} from './user.model';
import {Profile} from './profile.model';

export class UserSecure extends User {
  email: string;
  following: User[];
  profiles: Profile[];
}
