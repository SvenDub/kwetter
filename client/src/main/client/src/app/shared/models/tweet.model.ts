import {User} from './user.model';
import {Location} from './location.model';

export class Tweet {
  constructor(
    public id: number,
    public owner: User,
    public content: string,
    public likedBy: Set<User>,
    public date: Date,
    public location: Location,
    public mentions: Map<string, User>,
    public hashtags: string[]
  ) {
  }
}
