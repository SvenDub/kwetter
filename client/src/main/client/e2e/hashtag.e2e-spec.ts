import {browser, by, element} from 'protractor';

describe('hashtag page', () => {
  it('should show tweets containing a specific hashtag', () => {
    browser.get('/hashtag/twitter');

    const tweets = element.all(by.css('app-tweet'));
    expect(tweets.count()).toBe(2);
    tweets.each(tweet => {
      expect(tweet.element(by.css('p.card-text')).getText()).toContain('#twitter');
    });
  });

  it('should not match partial hashtags', () => {
    browser.get('/hashtag/twitte');

    const tweets = element.all(by.css('app-tweet'));
    expect(tweets.count()).toBe(0);
  });
});
