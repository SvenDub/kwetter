import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {TweetService} from '../api/tweet.service';
import {Tweet} from '../shared/models/tweet.model';
import {Subject} from 'rxjs/Subject';
import {debounceTime, distinctUntilChanged, switchMap} from 'rxjs/operators';
import {User} from '../shared/models/user.model';

@Component({
  selector: 'app-add-tweet',
  templateUrl: './add-tweet.component.html',
  styleUrls: ['./add-tweet.component.css']
})
export class AddTweetComponent implements OnInit {

  content: string;
  errorMessage: string;

  @ViewChild('submit') submit: ElementRef;
  @ViewChild('textarea') textarea: ElementRef;

  private autocompleteRequested: Subject<string>;
  autocompleteValues: User[];
  autocompleteSelection = 0;

  constructor(private tweetService: TweetService) {
    this.autocompleteRequested = new Subject<string>();
  }

  ngOnInit() {
    this.autocompleteRequested.pipe(
      debounceTime(400),
      distinctUntilChanged(),
      switchMap(value => this.tweetService.getAutocomplete(value))
    )
      .subscribe(value => {
        this.autocompleteValues = value;
        this.autocompleteSelection = Math.min(this.autocompleteSelection, this.autocompleteValues.length - 1);
      });
  }

  tweet() {
    const tweet: Tweet = {
      content: this.content,
    };

    this.errorMessage = '';

    this.tweetService.addTweet(tweet)
      .subscribe(
        value => {
          this.content = '';
        },
        error => {
          this.errorMessage = 'Could not place tweet, please try again.';
        }
      );
  }

  isDisabled() {
    return !this.content || this.content.length > 140;
  }

  onEnterKey(event: KeyboardEvent) {
    (<HTMLButtonElement>this.submit.nativeElement).click();
    event.preventDefault();
  }

  onKeyDown(event: KeyboardEvent) {
    if (this.autocompleteValues != null) {
      if (event.keyCode === 38) { // up
        this.autocompleteSelection = Math.max(this.autocompleteSelection - 1, 0);
        event.preventDefault();
      } else if (event.keyCode === 40) { // down
        this.autocompleteSelection = Math.min(this.autocompleteSelection + 1, this.autocompleteValues.length - 1);
        event.preventDefault();
      } else if (event.keyCode === 13) { // enter
        this.acceptSuggestion(this.autocompleteSelection);
        event.preventDefault();
      }
    }
  }

  onInput(event: Event) {
    const textarea = (<HTMLTextAreaElement>event.target);
    if (textarea.selectionStart === textarea.selectionEnd && textarea.selectionStart > 0) {
      const pattern = /@[a-zA-Z0-9_]*/g;

      let results: RegExpExecArray;
      while ((results = pattern.exec(this.content)) !== null) {
        if (results.index + results[0].length === textarea.selectionStart) {
          const query = results[0].substr(1);
          this.autocompleteRequested.next(query);
          return;
        }
      }

      this.autocompleteValues = null;
      this.autocompleteSelection = 0;
    }
  }

  acceptSuggestion(index: number) {
    const pattern = /@[a-zA-Z0-9_]*$/;
    this.content = this.content.replace(pattern, `@${this.autocompleteValues[index].profile.username} `);
    this.autocompleteValues = null;
    this.autocompleteSelection = 0;
    (<HTMLTextAreaElement>this.textarea.nativeElement).focus();
  }

}
