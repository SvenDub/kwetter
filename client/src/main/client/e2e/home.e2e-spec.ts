import {AppPage} from './app.po';
import {browser, by, element, protractor} from 'protractor';

describe('homepage', () => {
  it('should show a list of tweets', () => {
    browser.get('/');

    expect(element.all(by.css('app-tweet')).count()).toBeGreaterThan(0);
  });

  it('should add a tweet', async () => {
    browser.get('/');

    const tweets = element.all(by.css('app-tweet'));
    const tweetCountBefore = await tweets.count();

    const tweetContent = `Let's add a new tweet! #e2e (${new Date().valueOf()})`;
    element(by.id('tweet')).sendKeys(tweetContent, protractor.Key.CONTROL, protractor.Key.ENTER);

    expect(tweets.count()).toBe(tweetCountBefore + 1);
    expect(tweets.all(by.css('p.card-text')).getText()).toContain(tweetContent);
  });
});
