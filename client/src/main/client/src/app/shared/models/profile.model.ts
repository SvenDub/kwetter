import {Location} from './location.model';

export interface Profile {
  id: number;
  username: string;
  name: string;
  bio: string;
  location: Location;
  website: string;
  createdAt: Date;
}
