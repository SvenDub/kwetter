import {Location} from './location.model';

export class Profile {
  id: number;
  username: string;
  name: string;
  bio: string;
  location: Location;
  website: string;
  createdAt: Date;
}
