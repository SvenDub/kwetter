import { AppPage } from './app.po';
import {browser, by, element, protractor} from 'protractor';
import {Utils} from './utils';

describe('client App', () => {

});

describe('login page', () => {
  let utils: Utils;

  beforeEach(() => {
    utils = new Utils();
  });

  afterEach(() => {
    utils.clearCredentials();
  });

  it('should show login form', () => {
    browser.get('/');

    expect(element(by.id('username')).isPresent()).toBe(true);
    expect(element(by.id('password')).isPresent()).toBe(true);
  });

  it('should show signup form', () => {
    browser.get('/');

    expect(element(by.id('sign_up_email')).isPresent()).toBe(true);
    expect(element(by.id('sign_up_name')).isPresent()).toBe(true);
    expect(element(by.id('sign_up_username')).isPresent()).toBe(true);
    expect(element(by.id('sign_up_password')).isPresent()).toBe(true);
  });

  it('should login', () => {
    browser.get('/');

    utils.login('SvenDub', 'password');

    expect(element(by.id('username')).isPresent()).toBe(false);
    expect(element(by.id('password')).isPresent()).toBe(false);
    expect(utils.getLogoutButton().isPresent()).toBe(true);
  });

  it('should logout', () => {
    browser.get('/');

    utils.login('SvenDub', 'password');

    utils.logout();

    expect(element(by.id('username')).isPresent()).toBe(true);
    expect(element(by.id('password')).isPresent()).toBe(true);
    expect(utils.getLogoutButton().isPresent()).toBe(false);
  });
});
