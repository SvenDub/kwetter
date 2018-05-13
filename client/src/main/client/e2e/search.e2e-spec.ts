import {browser, by, element} from 'protractor';

describe('search page', () => {
  it('should show tweets containing a search string', () => {
    browser.get('/');

    element(by.name('query')).sendKeys('inleveren');

    expect(browser.getCurrentUrl()).toBe(`${browser.baseUrl}/search;query=inleveren`);

    const tweets = element.all(by.css('app-tweet'));
    expect(tweets.count()).toBe(2);
    tweets.each(tweet => {
      expect(tweet.element(by.css('p.card-text')).getText()).toContain('inleveren');
    });
  });

  it('should show tweets from an author', () => {
    browser.get('/');

    element(by.name('query')).sendKeys('@admin');

    expect(browser.getCurrentUrl()).toBe(`${browser.baseUrl}/search;query=@admin`);

    const tweets = element.all(by.css('app-tweet'));
    expect(tweets.count()).toBe(1);
    tweets.each(tweet => {
      expect(tweet.all(by.css('h6.card-subtitle > a')).first().getText()).toBe('@admin');
    });
  });

  it('should not match partial usernames', () => {
    browser.get('/');

    element(by.name('query')).sendKeys('@admi');

    expect(browser.getCurrentUrl()).toBe(`${browser.baseUrl}/search;query=@admi`);

    const tweets = element.all(by.css('app-tweet'));
    expect(tweets.count()).toBe(0);
  });
});
