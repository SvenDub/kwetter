import {Profile} from './profile.model';

export class User {
  constructor(
    public id: number,
    public profile: Profile,
    public tweetsCount: number,
    public followersCount: number,
    public followingCount: number
  ) {
  }
}
