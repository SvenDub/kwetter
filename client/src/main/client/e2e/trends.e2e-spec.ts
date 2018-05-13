import {browser, by, element} from 'protractor';

describe('trends', () => {
  it('should show a list of trends', () => {
    browser.get('/');

    const trends = element.all(by.css('app-trends a'));
    expect(trends.count()).toBeGreaterThanOrEqual(4);
  });

  it('should link to the hashtag page', async () => {
    browser.get('/');

    const trends = element.all(by.css('app-trends a'));
    const trend = trends.first();
    let hashtag = await trend.getText();
    hashtag = hashtag.substr(1, hashtag.indexOf('\n') - 1);

    trend.click();

    expect(browser.getCurrentUrl()).toBe(`${browser.baseUrl}/hashtag/${hashtag}`);
  });
});
