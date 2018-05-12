import {browser, by, element} from 'protractor';

export class Utils {

  login(username: string, password: string) {
    element(by.id('username')).sendKeys(username);
    element(by.id('password')).sendKeys(password, '\n');
  }

  logout() {
    this.getLogoutButton().click();
  }

  clearCredentials() {
    browser.executeScript('localStorage.clear()');
  }

  getLogoutButton() {
    return element(by.cssContainingText('.nav-link', 'Logout'));
  }

  getPageTitle() {
    return browser.getTitle();
  }
}
