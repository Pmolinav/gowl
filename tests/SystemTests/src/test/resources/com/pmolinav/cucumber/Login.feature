Feature: Login

#  Example user: INSERT INTO public.users
# (user_id, creation_date, email, modification_date, "name", "password", username)
# VALUES(1, '2024-05-19 13:37:03.884', 'some@user.com', '2024-05-19 13:37:03.884', 'Some User',
# '$2a$10$pn85ACcwW6v74Kkt3pnPau7A4lv8N2d.fvwXuLsYanv07PzlXTu9S', 'someUser');

  Background:
    Given the following roles have been stored previously
      | role         |
      | ROLE_USER    |
      | ROLE_PREMIUM |
      | ROLE_ADMIN   |
    Given the following users have been stored previously
      | username | password     | name     | email          | roles      | creation_date | modification_date |
      | someUser | somePassword | someName | some@email.com | ROLE_ADMIN | 123456        | 123456            |

  Scenario: An user logs in successfully
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200

  Scenario: Unauthorized login
    When an user with username otherUser and password otherPassword tries to log in
    Then received status code is 401