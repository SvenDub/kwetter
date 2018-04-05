import {Location} from './location.model';

export class Profile {
  constructor(
    public id: number,
    public username: string,
    public name: string,
    public bio: string,
    public location: Location,
    public website: string,
    public createdAt: Date
  ) {
  }
}
