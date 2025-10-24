Feature: LeaguesBOApi

  Background:
    Given the following roles have been stored previously
      | role         |
      | ROLE_USER    |
      | ROLE_PREMIUM |
      | ROLE_ADMIN   |
    Given the following users have been stored previously
      | username   | password       | name        | email            | birth_date | roles      | creation_date | modification_date |
      | someUser   | somePassword   | someName    | some@email.com   | 12-3-1998  | ROLE_ADMIN | 123456        | 123456            |
      | normalUser | normalPassword | Normal Name | normal@email.com | 13-3-1998  | ROLE_USER  | 123456        | 123456            |
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200
    Given the following categories have been stored previously
      | category_id | name                | description             | sport    | country | icon_url             | is_active | creation_date | modification_date |
      | PREMIER     | Premier League      | England First Division  | FOOTBALL | EN      | http://example.com/1 | true      | 123456        | 123456            |
      | PREMIER2    | League Championship | England Second Division | FOOTBALL | EN      | http://example.com/2 | true      | 123456        | 123456            |
    Given the following match days have been stored previously
      | category_id | season | match_day_number | start_date | end_date |
      | PREMIER     | 2025   | 1                | 123456     | 1234567  |
      | PREMIER     | 2025   | 2                | 1234567    | 12345678 |
      | PREMIER2    | 2025   | 1                | 123456     | 1234567  |
      | PREMIER2    | 2025   | 2                | 1234567    | 12345678 |

   # LEAGUE CATEGORIES
  Scenario: Try to create a new league category by a non-admin player with error
    When an user with username normalUser and password normalPassword tries to log in
    Then received status code is 200
    Given try to create a new league category with data
      | category_id | name    | description            | sport    | country | icon_url             | is_active |
      | LA_LIGA     | La Liga | Spanish First Division | FOOTBALL | ES      | http://example.com/1 | true      |
    Then received status code is 403

  Scenario: Create a new league category bad request
    Given try to create a new league category with data
      | category_id | name    | description            | is_active |
      | LA_LIGA     | La Liga | Spanish First Division | true      |
    Then received status code is 400

  Scenario: Create a new league category successfully
    Given try to create a new league category with data
      | category_id | name    | description            | sport    | country | icon_url             | is_active |
      | LA_LIGA     | La Liga | Spanish First Division | FOOTBALL | ES      | http://example.com/1 | true      |
    Then received status code is 201
    Then a league category with categoryId LA_LIGA has been stored successfully

  Scenario: Get all league categories successfully
    Given try to create a new league category with data
      | category_id | name    | description            | sport    | country | icon_url             | is_active |
      | LA_LIGA     | La Liga | Spanish First Division | FOOTBALL | ES      | http://example.com/1 | true      |
    Then received status code is 201
    When try to get all league categories
    Then received status code is 200
    Then a list of league categories with IDs PREMIER,PREMIER2,LA_LIGA are returned in response

  Scenario: Get league category by categoryId successfully
    Given try to create a new league category with data
      | category_id | name    | description            | sport    | country | icon_url             | is_active |
      | LA_LIGA     | La Liga | Spanish First Division | FOOTBALL | ES      | http://example.com/1 | true      |
    Then received status code is 201
    Then a league category with categoryId LA_LIGA has been stored successfully
    When try to get a league category by categoryId
    Then received status code is 200
    Then a league category with categoryId LA_LIGA has been stored successfully

  Scenario: Delete league category by categoryId successfully
    Given try to create a new league category with data
      | category_id | name    | description            | sport    | country | icon_url             | is_active |
      | LA_LIGA     | La Liga | Spanish First Division | FOOTBALL | ES      | http://example.com/1 | true      |
    Then received status code is 201
    Then a league category with categoryId LA_LIGA has been stored successfully
    When try to delete a league category by categoryId
    Then received status code is 200

   # MATCH DAYS
  Scenario: Try to create a new match day by a non-admin player with error
    When an user with username normalUser and password normalPassword tries to log in
    Then received status code is 200
    Given try to create a new match day with data
      | category_id | season | match_day_number | start_date | end_date  |
      | PREMIER     | 2025   | 5                | 12345678   | 123456789 |
    Then received status code is 403

  Scenario: Create a new match day bad request
    Given try to create a new match day with data
      | season | match_day_number | start_date | end_date  |
      | 2025   | 5                | 12345678   | 123456789 |
    Then received status code is 400

  Scenario: Create a new match day successfully
    Given try to create a new match day with data
      | category_id | season | match_day_number | start_date | end_date  |
      | PREMIER     | 2025   | 5                | 12345678   | 123456789 |
    Then received status code is 201
    Then a match day with categoryId PREMIER, season 2025 and number 5 has been stored successfully

  Scenario: Create several match days successfully
    Given try to create several new match days with data
      | category_id | season | match_day_number | start_date | end_date   |
      | PREMIER2    | 2025   | 3                | 12345678   | 123456789  |
      | PREMIER2    | 2025   | 4                | 123456789  | 1234567890 |
    Then received status code is 201
    Then a match day with categoryId PREMIER2, season 2025 and number 3 has been stored successfully
    Then a match day with categoryId PREMIER2, season 2025 and number 4 has been stored successfully

  Scenario: Get all match days successfully
    Given try to create a new match day with data
      | category_id | season | match_day_number | start_date | end_date  |
      | PREMIER     | 2025   | 3                | 12345678   | 123456789 |
    Then received status code is 201
    Then a match day with categoryId PREMIER, season 2025 and number 3 has been stored successfully
    When try to get all match days
    Then received status code is 200
    Then a list of match days with season 2025 and numbers 1,2,3 are returned in response

  Scenario: Get match days by categoryId
    Given try to create a new match day with data
      | category_id | season | match_day_number | start_date | end_date  |
      | PREMIER2    | 2025   | 11               | 12345678   | 123456789 |
    Then received status code is 201
    Then a match day with categoryId PREMIER2, season 2025 and number 11 has been stored successfully
    When try to get match days by categoryId
    Then received status code is 200
    Then a list of match days with season 2025 and numbers 1,2,11 are returned in response

  Scenario: Get match days by categoryId and season
    Given try to create a new match day with data
      | category_id | season | match_day_number | start_date | end_date  |
      | PREMIER     | 2025   | 3                | 12345678   | 123456789 |
    Then received status code is 201
    Then a match day with categoryId PREMIER, season 2025 and number 3 has been stored successfully
    When try to get match days by categoryId and season
    Then received status code is 200
    Then a list of match days with season 2025 and numbers 1,2,3 are returned in response

  Scenario: Delete match days by categoryId successfully
    Given try to create a new match day with data
      | category_id | season | match_day_number | start_date | end_date  |
      | PREMIER     | 2025   | 3                | 12345678   | 123456789 |
    Then received status code is 201
    Then a match day with categoryId PREMIER, season 2025 and number 3 has been stored successfully
    When try to delete match days by categoryId
    Then received status code is 200

  Scenario: Delete match days by categoryId and season successfully
    Given try to create a new match day with data
      | category_id | season | match_day_number | start_date | end_date  |
      | PREMIER     | 2025   | 3                | 12345678   | 123456789 |
    Then received status code is 201
    Then a match day with categoryId PREMIER, season 2025 and number 3 has been stored successfully
    When try to delete match days by categoryId and season
    Then received status code is 200

  Scenario: Delete match days by categoryId, season and number successfully
    Given try to create a new match day with data
      | category_id | season | match_day_number | start_date | end_date  |
      | PREMIER     | 2025   | 3                | 12345678   | 123456789 |
    Then received status code is 201
    Then a match day with categoryId PREMIER, season 2025 and number 3 has been stored successfully
    When try to delete match days by categoryId, season and number
    Then received status code is 200

   # LEAGUES
  Scenario: Try to create a new league by a non-admin player with error
    When an user with username normalUser and password normalPassword tries to log in
    Then received status code is 200
    Given try to create a new league with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | someUser       | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 403

  Scenario: Create a new league bad request
    Given try to create a new league with data
      | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | someUser       | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 400

  Scenario: Create a new league successfully
    Given try to create a new league with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | someUser       | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully
#    Then an entity User with id N/A has been stored into historical by user Admin and with type CREATE

  Scenario: Get all leagues successfully
    Given try to create a new league with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | someUser       | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully
    Given try to create a new league with data
      | name              | description                 | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players    |
      | Friendly League 2 | A Second League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | someUser       | someUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    When try to get all leagues
    Then received status code is 200
    Then a list of leagues with names Friendly League,Friendly League 2 are returned in response

  Scenario: Get league by leagueId successfully
    Given try to create a new league with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | someUser       | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully
    When try to get a league by leagueId
    Then received status code is 200
    Then a league with name Friendly League is returned in response

  Scenario: Get league by name successfully
    Given try to create a new league with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | someUser       | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully
    When try to get a league by name
    Then received status code is 200
    Then a league with name Friendly League is returned in response

  Scenario: Close league by leagueId successfully
    Given try to create a new league with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | someUser       | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully
    When try to close a league by leagueId
    Then received status code is 200
    Then a league with name Friendly League and status CLOSED has been stored successfully

  Scenario: Close league by name successfully
    Given try to create a new league with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | someUser       | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully
    When try to close a league by name
    Then received status code is 200
    Then a league with name Friendly League and status CLOSED has been stored successfully

  Scenario: Delete league by leagueId successfully
    Given try to create a new league with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | someUser       | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully
    When try to delete a league by leagueId
    Then received status code is 200

  Scenario: Delete league by name successfully
    Given try to create a new league with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | someUser       | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully
    When try to delete a league by name
    Then received status code is 200

   # LEAGUE PLAYERS
  Scenario: Try to create new league players by a non-admin player with error
    Given try to create a new league with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players      |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username normalUser has been associated to last league successfully
    When an user with username normalUser and password normalPassword tries to log in
    Then received status code is 200
    Given try to create several league players with data
      | username | total_points | status |
      | someUser | 20           | ACTIVE |
    Then received status code is 403

  Scenario: Create new league players bad request
    Given try to create a new league with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players      |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username normalUser has been associated to last league successfully
    Given try to create several league players with data
      | status | total_points |
      | ACTIVE | 33           |
    Then received status code is 400

  Scenario: Create new league players successfully
    Given try to create a new league with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players      |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username normalUser has been associated to last league successfully
    Given try to create several league players with data
      | username  | total_points | status |
      | someUser  | 10           | ACTIVE |
      | otherUser | 15           | ACTIVE |
    Then received status code is 201
    Then a player with username someUser has been associated to last league successfully
    Then a player with username otherUser has been associated to last league successfully

  Scenario: Get league players by leagueId successfully
    Given try to create a new league with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | someUser       | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully
    When try to get league players by leagueId
    Then received status code is 200
    Then a list of league players with usernames someUser,normalUser are returned in response

  Scenario: Get league player by leagueId and username successfully
    Given try to create a new league with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | someUser       | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully
    When try to get a league player by leagueId and username
    Then received status code is 200
    Then a league player with username normalUser is returned in response

  Scenario: Get leagues by username successfully
    Given try to create a new league with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | someUser       | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully
    When try to get leagues by player username
    Then received status code is 200
    Then a list of leagues with names Friendly League are returned in response

  Scenario: Add points to player successfully
    Given try to create a new league with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | someUser       | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username normalUser has been associated to last league successfully
    Then a player with username someUser has been associated to last league successfully
    When try to add 20 points to player in league
    Then received status code is 200
    Then a player with username someUser has been associated to last league successfully
    Then last player has 20 points

  Scenario: Delete league players by leagueId successfully
    Given try to create a new league with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | someUser       | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully
    When try to delete league players by leagueId
    Then received status code is 200

  Scenario: Delete league player by leagueId and username successfully
    Given try to create a new league with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | someUser       | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully
    When try to delete a league player by leagueId and username
    Then received status code is 200

   # LEAGUE PLAYER POINTS
  Scenario: Try to create new league player points by a non-admin player with error
    Given try to create a new league with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players      |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username normalUser has been associated to last league successfully
    Given try to create several league players with data
      | username  | total_points | status |
      | someUser  | 10           | ACTIVE |
      | otherUser | 15           | ACTIVE |
    Then received status code is 201
    Then a player with username someUser has been associated to last league successfully
    Then a player with username otherUser has been associated to last league successfully
    When an user with username normalUser and password normalPassword tries to log in
    Then received status code is 200
    Given try to create new league player points with data
      | category_id | season | match_day_number | username   | points |
      | PREMIER     | 2025   | 1                | normalUser | 18     |
    Then received status code is 403

  Scenario: Create new league player points bad request
    Given try to create a new league with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players      |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username normalUser has been associated to last league successfully
    Given try to create several league players with data
      | username  | total_points | status |
      | someUser  | 10           | ACTIVE |
      | otherUser | 15           | ACTIVE |
    Then received status code is 201
    Then a player with username otherUser has been associated to last league successfully
    Then a player with username someUser has been associated to last league successfully
    Given try to create new league player points with data
      | season | match_day_number | username | points |
      | 2025   | 1                | someUser | 22     |
    Then received status code is 400

  Scenario: Create new league player points successfully
    Given try to create a new league with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | someUser       | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username normalUser has been associated to last league successfully
    Then a player with username someUser has been associated to last league successfully
    Given try to create new league player points with data
      | category_id | season | match_day_number | username | points |
      | PREMIER     | 2025   | 1                | someUser | 22     |
    Then received status code is 201

  Scenario: Get league player points by leagueId and username successfully
    Given try to create a new league with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | someUser       | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username normalUser has been associated to last league successfully
    Then a player with username someUser has been associated to last league successfully
    Given try to create new league player points with data
      | category_id | season | match_day_number | username | points |
      | PREMIER     | 2025   | 1                | someUser | 22     |
    Then received status code is 201
    When try to get league player points by leagueId and username
    Then received status code is 200
    Then a list of league player points with usernames someUser are returned in response

  Scenario: Get league player points by categoryId, season and number successfully
    Given try to create a new league category with data
      | category_id | name    | description            | sport    | country | icon_url             | is_active |
      | LA_LIGA     | La Liga | Spanish First Division | FOOTBALL | ES      | http://example.com/1 | true      |
    Then received status code is 201
    Then a league category with categoryId LA_LIGA has been stored successfully
    Given try to create a new match day with data
      | category_id | season | match_day_number | start_date | end_date   |
      | LA_LIGA     | 2025   | 11               | 123456789  | 1234567890 |
    Then received status code is 201
    Then a match day with categoryId LA_LIGA, season 2025 and number 11 has been stored successfully
    Given try to create a new league with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | LA_LIGA     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | someUser       | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username normalUser has been associated to last league successfully
    Then a player with username someUser has been associated to last league successfully
    Given try to create new league player points with data
      | category_id | season | match_day_number | username | points |
      | LA_LIGA     | 2025   | 11               | someUser | 22     |
    Then received status code is 201
    When try to get league player points by categoryId, season and number
    Then received status code is 200
    Then a list of league player points with usernames someUser are returned in response

  Scenario: Delete league player points by leagueId and username successfully
    Given try to create a new league with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | someUser       | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username normalUser has been associated to last league successfully
    Then a player with username someUser has been associated to last league successfully
    Given try to create new league player points with data
      | category_id | season | match_day_number | username | points |
      | PREMIER     | 2025   | 1                | someUser | 22     |
    Then received status code is 201
    When try to delete league player points by leagueId and username
    Then received status code is 200

  Scenario: Delete league player points by categoryId, season and number successfully
    Given try to create a new league category with data
      | category_id | name    | description            | sport    | country | icon_url             | is_active |
      | LA_LIGA     | La Liga | Spanish First Division | FOOTBALL | ES      | http://example.com/1 | true      |
    Then received status code is 201
    Then a league category with categoryId LA_LIGA has been stored successfully
    Given try to create a new match day with data
      | category_id | season | match_day_number | start_date | end_date   |
      | LA_LIGA     | 2025   | 11               | 123456789  | 1234567890 |
    Then received status code is 201
    Then a match day with categoryId LA_LIGA, season 2025 and number 11 has been stored successfully
    Given try to create a new league with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | LA_LIGA     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | someUser       | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username normalUser has been associated to last league successfully
    Then a player with username someUser has been associated to last league successfully
    Given try to create new league player points with data
      | category_id | season | match_day_number | username | points |
      | LA_LIGA     | 2025   | 11               | someUser | 22     |
    Then received status code is 201
    When try to delete league player points by categoryId, season and number
    Then received status code is 200
