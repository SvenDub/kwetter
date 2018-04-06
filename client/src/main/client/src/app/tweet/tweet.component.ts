import {Component, ComponentFactoryResolver, Input, OnInit, Query, TemplateRef, ViewChild} from '@angular/core';
import {Tweet} from '../shared/models/tweet.model';
import * as moment from 'moment';
import {TweetContentDirective} from '../tweet-content.directive';
import {AnchorComponent} from '../anchor/anchor.component';
import {TextComponent} from '../text/text.component';
import {User} from '../shared/models/user.model';

@Component({
  selector: 'app-tweet',
  templateUrl: './tweet.component.html',
  styleUrls: ['./tweet.component.css']
})
export class TweetComponent implements OnInit {

  @Input() tweet: Tweet;
  @ViewChild(TweetContentDirective) tweetContent: TweetContentDirective;

  constructor(private componentFactoryResolver: ComponentFactoryResolver) { }

  ngOnInit() {
    // Create factories for Anchor and Text components
    const anchorComponentFactory = this.componentFactoryResolver.resolveComponentFactory(AnchorComponent);
    const textComponentFactory = this.componentFactoryResolver.resolveComponentFactory(TextComponent);

    // Get the view ref for tweet body
    const viewContainerRef = this.tweetContent.viewContainerRef;
    viewContainerRef.clear();

    const mentions = new Map<string, User>();
    Object.keys(this.tweet.mentions).forEach(key => {
      mentions.set(key, this.tweet.mentions[key]);
    });

    /* Replace mentions and hashtags with Anchor components

       Why, you ask? Simple. Although it is possible to output html directly, I could not find a way
       to include the routerLink property on an element without dynamically creating a parent for it.
       This section (and the Anchor and Text components) look more like hacks than solid code, but
       it works, so there's that.
    */
    this.tweet.content.split(/(#[a-zA-Z0-9_]+)|(@[a-zA-Z0-9_]+)/)
      .filter(value => value)
      .forEach(value => {
        if (value.startsWith('#') && this.tweet.hashtags.includes(value)) {
          const componentRef = viewContainerRef.createComponent(anchorComponentFactory);
          const instance = componentRef.instance;
          instance.content = value;
          instance.link = [`/trend/${value.substr(1)}`];
        } else if (value.startsWith('@') && mentions.has(value.substr(1))) {
          const componentRef = viewContainerRef.createComponent(anchorComponentFactory);
          const instance = componentRef.instance;
          instance.content = value;
          instance.link = [`/u/${mentions.get(value.substr(1)).profile.username}`];
        } else {
          const componentRef = viewContainerRef.createComponent(textComponentFactory);
          const instance = componentRef.instance;
          instance.content = value;
        }
      });
  }

  private getRelativeDate() {
    return moment(this.tweet.date).fromNow();
  }

  private getAbsoluteDate() {
    return moment(this.tweet.date).format('lll');
  }

}
