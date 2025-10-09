Feature: LeagueApi

  Background:
    Given the following roles have been stored previously
      | role         |
      | ROLE_USER    |
      | ROLE_PREMIUM |
      | ROLE_ADMIN   |
    Given the following users have been stored previously
      | username   | password       | name        | email            | roles      | creation_date | modification_date |
      | someUser   | somePassword   | someName    | some@email.com   | ROLE_ADMIN | 123456        | 123456            |
      | normalUser | normalPassword | Normal Name | normal@email.com | ROLE_USER  | 123456        | 123456            |
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
    When an user with username normalUser and password normalPassword tries to log in
    Then received status code is 200

   # HEALTH
  Scenario: LeagueApi health OK by admin user but nor for normal user
    When try to get LeagueApi health
    Then received status code is 403
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200
    When try to get LeagueApi health
    Then received status code is 200

   # LEAGUE CATEGORIES
  Scenario: Get all league categories successfully from LeagueApi
    When try to get all league categories with public endpoint
    Then received status code is 200
    Then a list of league categories with IDs PREMIER,PREMIER2 are returned in response

  Scenario: Get league category by categoryId successfully from LeagueApi
    When try to get a league category by categoryId PREMIER with public endpoint
    Then received status code is 200
    Then a league category with categoryId PREMIER has been stored successfully

   # MATCH DAYS
  Scenario: Get match days by categoryId and season from LeagueApi
    When try to get match days by categoryId PREMIER and season 2025 with public endpoint
    Then received status code is 200
    Then a list of simple match days with values 1,2 are returned in response

   # LEAGUES
  Scenario: Create a new league with LeagueApi bad request
    Given try to create a new league with public endpoint with data
      | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 400

  Scenario: Create a new league with LeagueApi successfully
    Given try to create a new league with public endpoint with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully

  Scenario: Try to create a new league with LeagueApi for other players with error
    Given try to create a new league with public endpoint with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players    |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | someUser       | someUser,0,ACTIVE |
    Then received status code is 403

  Scenario: Get league by leagueId successfully from LeagueApi
    Given try to create a new league with public endpoint with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully
    When try to get a league by leagueId with public endpoint
    Then received status code is 200
    Then a league with name Friendly League is returned in response

  Scenario: Try to get league by leagueId from LeagueApi with other player and error
    Given try to create a new league with public endpoint with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players      |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username normalUser has been associated to last league successfully
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200
    When try to get a league by leagueId with public endpoint
    Then received status code is 403

  Scenario: Get league by name successfully from LeagueApi
    Given try to create a new league with public endpoint with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully
    When try to get a league by name Friendly League with public endpoint
    Then received status code is 200
    Then a league with name Friendly League is returned in response

  Scenario: Get leagues by username with another user from LeagueApi with error
    Given try to create a new league with public endpoint with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully
    When try to get leagues by username someUser with public endpoint
    Then received status code is 403

  Scenario: Get leagues by username simplified successfully from LeagueApi
    Given try to create a new league with public endpoint with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Given try to create a new league with public endpoint with data
      | name              | description            | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League 2 | A League for Friends 2 | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully
    When try to get leagues by username normalUser with public endpoint
    Then received status code is 200
    Then a list of leagues with names Friendly League,Friendly League 2 are returned in response

  Scenario: Close league by leagueId successfully from LeagueApi
    Given try to create a new league with public endpoint with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully
    When try to close a league by leagueId with public endpoint
    Then received status code is 200
    Then a league with name Friendly League and status CLOSED has been stored successfully

  Scenario: Try to close league by leagueId from LeagueApi by no owner user with error
    Given try to create a new league with public endpoint with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200
    When try to close a league by leagueId with public endpoint
    Then received status code is 403

  Scenario: Close league by name successfully from LeagueApi
    Given try to create a new league with public endpoint with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully
    When try to close a league by name Friendly League with public endpoint
    Then received status code is 200
    Then a league with name Friendly League and status CLOSED has been stored successfully

  Scenario: Try to close league by name from LeagueApi by another user with error
    Given try to create a new league with public endpoint with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players      |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username normalUser has been associated to last league successfully
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200
    When try to close a league by name Friendly League with public endpoint
    Then received status code is 403

   # LEAGUE PLAYERS
  Scenario: Create new league player with LeagueApi bad request
    Given try to create a new league with public endpoint with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players      |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username normalUser has been associated to last league successfully
    Given try to create several league players with public endpoint with data
      | username   | status |
      | normalUser | ACTIVE |
    Then received status code is 400

  Scenario: Try to create several league players with LeagueApi with error
    Given try to create a new league with public endpoint with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players      |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username normalUser has been associated to last league successfully
    Given try to create several league players with data
      | username  | total_points | status |
      | someUser  | 10           | ACTIVE |
      | otherUser | 15           | ACTIVE |
    Then received status code is 403

  Scenario: Create new league player with LeagueApi successfully
    Given try to create a new league with public endpoint with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players      |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username normalUser has been associated to last league successfully
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200
    Given try to create several league players with public endpoint with data
      | username | total_points | status |
      | someUser | 10           | ACTIVE |
    Then received status code is 201
    Then a player with username someUser has been associated to last league successfully

  Scenario: Try to create another league player with LeagueApi with error
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200
    Given try to create a new league with public endpoint with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players    |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | someUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Given try to create several league players with public endpoint with data
      | username   | total_points | status |
      | normalUser | 10           | ACTIVE |
    Then received status code is 403

  Scenario: Get league players by leagueId successfully from LeagueApi
    Given try to create a new league with public endpoint with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully
    When try to get league players by leagueId with public endpoint
    Then received status code is 200
    Then a list of league players with usernames someUser,normalUser are returned in response

  Scenario: Get league player by leagueId and username successfully from LeagueApi
    Given try to create a new league with public endpoint with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully
    When try to get a league player by leagueId and username normalUser with public endpoint
    Then received status code is 200
    Then a league player with username normalUser is returned in response

  Scenario: Try to get league player by leagueId and other username from LeagueApi with error
    Given try to create a new league with public endpoint with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players      |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username normalUser has been associated to last league successfully
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200
    When try to get a league player by leagueId and username normalUser with public endpoint
    Then received status code is 403

  Scenario: Get leagues by username successfully from LeagueApi
    Given try to create a new league with public endpoint with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully
    When try to get leagues by player username normalUser with public endpoint
    Then received status code is 200
    Then a list of leagues with names Friendly League are returned in response

  Scenario: Try to get leagues by username from LeagueApi with error
    Given try to create a new league with public endpoint with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200
    When try to get leagues by player username normalUser with public endpoint
    Then received status code is 403

  Scenario: Delete league player by leagueId and username successfully from LeagueApi
    Given try to create a new league with public endpoint with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully
    When try to delete a league player by leagueId and username normalUser with public endpoint
    Then received status code is 200

  Scenario: Try to delete another league player by leagueId and username from LeagueApi with error
    Given try to create a new league with public endpoint with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username someUser has been associated to last league successfully
    Then a player with username normalUser has been associated to last league successfully
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200
    When try to delete a league player by leagueId and username normalUser with public endpoint
    Then received status code is 403

   # LEAGUE PLAYER POINTS

  Scenario: Get league player points by leagueId and username successfully from LeagueApi
    Given try to create a new league with public endpoint with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username normalUser has been associated to last league successfully
    Then a player with username someUser has been associated to last league successfully
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200
    Given try to create new league player points with data
      | category_id | season | match_day_number | username   | points |
      | PREMIER     | 2025   | 1                | normalUser | 22     |
    Then received status code is 201
    When an user with username normalUser and password normalPassword tries to log in
    Then received status code is 200
    When try to get league player points by leagueId and username normalUser with public endpoint
    Then received status code is 200
    Then a list of league player points with usernames normalUser are returned in response

  Scenario: Get league player points by categoryId, season and number successfully from LeagueApi
    Given try to create a new league with public endpoint with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players                        |
      | Friendly League | A League for Friends | PREMIER     | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | someUser,0,ACTIVE;normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username normalUser has been associated to last league successfully
    Then a player with username someUser has been associated to last league successfully
    Then a league category with categoryId PREMIER has been stored successfully
    Then a match day with categoryId PREMIER, season 2025 and number 1 has been stored successfully
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200
    Given try to create new league player points with data
      | category_id | season | match_day_number | username   | points |
      | PREMIER     | 2025   | 1                | normalUser | 22     |
    Then received status code is 201
    When an user with username normalUser and password normalPassword tries to log in
    Then received status code is 200
    When try to get league player points by categoryId PREMIER, season 2025 and number 1 with public endpoint
    Then received status code is 200
    Then a list of league player points with usernames normalUser are returned in response
