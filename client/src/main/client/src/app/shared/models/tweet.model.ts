import {User} from './user.model';
import {Location} from './location.model';

export interface Tweet {
id: number;
    owner: User;
    content: string;
    likedBy: Set<User>;
    date: Date;
    location: Location;
    mentions: any;
    hashtags: string[];
}
