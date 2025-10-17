Feature: PredictionApi

  Background:
    Given the following roles have been stored previously
      | role         |
      | ROLE_USER    |
      | ROLE_PREMIUM |
      | ROLE_ADMIN   |
    Given the following users have been stored previously
      | username   | password       | name        | email            | birthDate | roles      | creation_date | modification_date |
      | someUser   | somePassword   | someName    | some@email.com   | 12-3-1998 | ROLE_ADMIN | 123456        | 123456            |
      | normalUser | normalPassword | Normal Name | normal@email.com | 13-3-1998 | ROLE_USER  | 123456        | 123456            |
    Given the following matches have been stored previously
      | match_id | category_id | season | match_day_number | home_team    | away_team  | start_time | status | creation_date | modification_date |
      | 101      | PREMIER     | 2025   | 1                | Team Alpha   | Team Beta  | 1624005000 | ACTIVE | 1624000000    | 1624005000        |
      | 102      | PREMIER     | 2025   | 1                | Team Gamma   | Team Delta | 1624006000 | ACTIVE | 1624212000    | 1624001000        |
      | 103      | PREMIER     | 2025   | 2                | Team Epsilon | Team Zeta  | 1624007000 | ACTIVE | 1624298400    | 1624002000        |
    Given the following events have been stored previously
      | event_type | description        | creation_date | modification_date |
      | h2h        | Head to Head event | 1624000000    | 1624005000        |
      | Totals     | Total goals event  | 1624001000    | 1624006000        |
      | spreads    | Handicap event     | 1624002000    | 1624007000        |
    Given the following odds have been stored previously
      | odds_id | match_id | event_type | label    | value | active | creation_date | modification_date |
      | 301     | 101      | h2h        | Home Win | 1.5   | true   | 1624000000    | 1624005000        |
      | 302     | 101      | h2h        | Away Win | 2.5   | true   | 1624001000    | 1624006000        |
      | 303     | 102      | Totals     | Over 2.5 | 1.8   | true   | 1624002000    | 1624007000        |
    When an user with username normalUser and password normalPassword tries to log in
    Then received status code is 200

   # HEALTH
  Scenario: PredictionApi health OK by admin user but nor for normal user
    When try to get PredictionApi health
    Then received status code is 403
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200
    When try to get PredictionApi health
    Then received status code is 200

   # MATCHES
  Scenario: Get match by matchId successfully
    Then a match with categoryId PREMIER has been stored successfully
    When try to get a match by matchId with public endpoint
    Then received status code is 200
    Then a match with categoryId PREMIER returned in response

  Scenario: Get match by categoryId, season and matchDayNumber successfully
    Then a match with categoryId PREMIER has been stored successfully
    When try to get a match by categoryId, season and matchDayNumber with public endpoint
    Then received status code is 200
    Then a list of matches with categoryIds PREMIER are returned in response

   # EVENTS
  Scenario: Get event by eventType successfully
    Then an event with type h2h has been stored successfully
    When try to get an event by eventType with public endpoint
    Then received status code is 200
    Then an event with type h2h has been returned in response

    # ODDS
  Scenario: Get odds by oddsId successfully
    Then odds with label Over 2.5 have been stored successfully
    When try to get odds by oddsId with public endpoint
    Then received status code is 200
    Then odds with label Over 2.5 are returned in response

  Scenario: Get odds by eventType successfully
    Then odds with label Home Win have been stored successfully
    When try to get odds by eventType with public endpoint
    Then received status code is 200
    Then a list of odds with labels Home Win,Away Win are returned in response

  Scenario: Get odds by matchId successfully
    Then odds with label Home Win have been stored successfully
    When try to get odds by matchId with public endpoint
    Then received status code is 200
    Then a list of odds with labels Home Win,Away Win are returned in response

  # PLAYER BETS
  Scenario: Create new player bet for other user with error
    Given try to create new player bet with data with public endpoint
      | match_id | league_id | username | selections                    |
      | 101      | 123       | someUser | 301,h2h,5.00;303,Totals,10.00 |
    Then received status code is 403

  Scenario: Create new player bet successfully
    Given try to create new player bet with data with public endpoint
      | match_id | league_id | username   | selections                    |
      | 101      | 123       | normalUser | 301,h2h,5.00;303,Totals,10.00 |
    Then received status code is 201
    Then a player bet for username normalUser has been stored successfully

  Scenario: Get player bet by playerBetId successfully
    Given try to create new player bet with data with public endpoint
      | match_id | league_id | username   | selections                    |
      | 101      | 123       | normalUser | 301,h2h,5.00;303,Totals,10.00 |
    Then received status code is 201
    Then a player bet for username normalUser has been stored successfully
    When try to get a player bet by playerBetId with public endpoint
    Then received status code is 200
    Then a player bet with username normalUser is returned in response

  Scenario: Get player bets by matchId successfully
    Given try to create new player bet with data with public endpoint
      | match_id | league_id | username   | selections                    |
      | 101      | 123       | normalUser | 301,h2h,5.00;303,Totals,10.00 |
    Then received status code is 201
    Then a player bet for username normalUser has been stored successfully
    Given try to create new player bet with data with public endpoint
      | match_id | league_id | username   | selections                    |
      | 101      | 123       | normalUser | 301,h2h,7.00;303,Totals,12.00 |
    Then received status code is 201
    Then a player bet for username normalUser has been stored successfully
    When try to get player bets by matchId with public endpoint
    Then received status code is 200
    Then a list of player bets with usernames normalUser is returned in response

  Scenario: Get player bets by username successfully
    Given try to create new player bet with data with public endpoint
      | match_id | league_id | username   | selections                    |
      | 101      | 123       | normalUser | 301,h2h,5.00;303,Totals,10.00 |
    Then received status code is 201
    Then a player bet for username normalUser has been stored successfully
    Given try to create new player bet with data with public endpoint
      | match_id | league_id | username   | selections                    |
      | 101      | 123       | normalUser | 301,h2h,7.00;303,Totals,12.00 |
    Then received status code is 201
    Then a player bet for username normalUser has been stored successfully
    When try to get player bets by username with public endpoint
    Then received status code is 200
    Then a list of player bets with usernames normalUser is returned in response

  Scenario: Delete player bet by playerBetId successfully
    Given try to create new player bet with data with public endpoint
      | match_id | league_id | username   | selections                    |
      | 101      | 123       | normalUser | 301,h2h,5.00;303,Totals,10.00 |
    Then received status code is 201
    Then a player bet for username normalUser has been stored successfully
    When try to delete a player bet by playerBetId with public endpoint
    Then received status code is 200

  # TODO: PLAYER BET SELECTIONS ( NOT NECESSARY FOR NOW )