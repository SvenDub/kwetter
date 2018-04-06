import {Profile} from './profile.model';

export class User {
    id: number;
    profile: Profile;
    tweetsCount: number;
    followersCount: number;
    followingCount: number;
}
