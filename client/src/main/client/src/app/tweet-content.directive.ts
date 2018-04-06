import {Directive, ViewContainerRef} from '@angular/core';

@Directive({
  selector: '[appTweetContent]'
})
export class TweetContentDirective {

  constructor(public viewContainerRef: ViewContainerRef) { }

}
