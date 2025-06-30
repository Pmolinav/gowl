Feature: PredictionsBOApi

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
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200
    Given the following matches have been stored previously
      | match_id | category_id | season | match_day_number | home_team    | away_team  | start_time | status | creation_date | modification_date |
      | 101      | PREMIER     | 2025   | 1                | Team Alpha   | Team Beta  | 1624005000 | ACTIVE | 1624000000    | 1624005000        |
      | 102      | PREMIER     | 2025   | 1                | Team Gamma   | Team Delta | 1624006000 | ACTIVE | 1624212000    | 1624001000        |
      | 103      | PREMIER     | 2025   | 2                | Team Epsilon | Team Zeta  | 1624007000 | ACTIVE | 1624298400    | 1624002000        |
    Given the following events have been stored previously
      | event_type | match_id | description        | creation_date | modification_date |
      | h2h        | 101      | Head to Head event | 1624000000    | 1624005000        |
      | totals     | 101      | Total Goals event  | 1624001000    | 1624006000        |
      | spreads    | 102      | Handicap event     | 1624002000    | 1624007000        |
    Given the following odds have been stored previously
      | odds_id | event_type | label    | value | active | creation_date | modification_date |
      | 301     | h2h        | Home Win | 1.5   | true   | 1624000000    | 1624005000        |
      | 302     | h2h        | Away Win | 2.5   | true   | 1624001000    | 1624006000        |
      | 303     | totals     | Over 2.5 | 1.8   | true   | 1624002000    | 1624007000        |

   # MATCHES
  Scenario: Try to create a new match by a non-admin player with error
    When an user with username normalUser and password normalPassword tries to log in
    Then received status code is 200
    Given try to create a new match with data
      | category_id | season | match_day_number | home_team | away_team | status    | start_time |
      | PREMIER     | 2025   | 1                | Team A    | Team B    | SCHEDULED | 1624125600 |
    Then received status code is 403

  Scenario: Create a new match bad request
    Given try to create a new match with data
      | category_id | season | match_day_number | status    | start_time |
      | PREMIER     | 2025   | 1                | SCHEDULED | 1624125600 |
    Then received status code is 400

  Scenario: Create a new match successfully
    Given try to create a new match with data
      | category_id | season | match_day_number | home_team | away_team | status    | start_time |
      | PREMIER2    | 2025   | 1                | Team A    | Team B    | SCHEDULED | 1624125600 |
    Then received status code is 201
    Then a match with categoryId PREMIER2 has been stored successfully

  Scenario: Get all matches successfully
    Given try to create a new match with data
      | category_id | season | match_day_number | home_team | away_team | status    | start_time |
      | PREMIER2    | 2025   | 1                | Team A    | Team B    | SCHEDULED | 1624125600 |
    Then received status code is 201
    When try to get all matches
    Then received status code is 200
    Then a list of matches with categoryIds PREMIER,PREMIER2 are returned in response

  Scenario: Get match by matchId successfully
    Given try to create a new match with data
      | category_id | season | match_day_number | home_team | away_team | status    | start_time |
      | PREMIER2    | 2025   | 1                | Team A    | Team B    | SCHEDULED | 1624125600 |
    Then received status code is 201
    Then a match with categoryId PREMIER2 has been stored successfully
    When try to get a match by matchId
    Then received status code is 200
    Then a match with categoryId PREMIER2 returned in response

  Scenario: Get match by categoryId, season and matchDayNumber successfully
    Given try to create a new match with data
      | category_id | season | match_day_number | home_team | away_team | status    | start_time |
      | PREMIER     | 2025   | 5                | Team A    | Team B    | SCHEDULED | 1624125600 |
    Then received status code is 201
    Then a match with categoryId PREMIER has been stored successfully
    When try to get a match by categoryId, season and matchDayNumber
    Then received status code is 200
    Then a list of matches with categoryIds PREMIER are returned in response

  Scenario: Update a new match successfully
    Given try to create a new match with data
      | category_id | season | match_day_number | home_team | away_team | status    | start_time |
      | PREMIER2    | 2025   | 1                | Team A    | Team B    | SCHEDULED | 1624125600 |
    Then received status code is 201
    Then a match with categoryId PREMIER2 has been stored successfully
    Given try to update a match with data
      | category_id | season | match_day_number | home_team | away_team | status    | start_time |
      | PREMIER2    | 2025   | 1                | Team B    | Team C    | SCHEDULED | 1624125600 |
    Then received status code is 200
    Then last stored match home team is Team B and away team is Team C

  Scenario: Delete match by categoryId successfully
    Given try to create a new match with data
      | category_id | season | match_day_number | home_team | away_team | status    | start_time |
      | PREMIER2    | 2025   | 1                | Team A    | Team B    | SCHEDULED | 1624125600 |
    Then received status code is 201
    Then a match with categoryId PREMIER2 has been stored successfully
    When try to delete a match by matchId
    Then received status code is 200

   # EVENTS
  Scenario: Try to create a new event by a non-admin player with error
    When an user with username normalUser and password normalPassword tries to log in
    Then received status code is 200
    Given try to create a new event with data
      | match_id | event_type | timestamp  | description |
      | 101      | Result     | 1624125650 | First goal  |
    Then received status code is 403

  Scenario: Create a new event bad request
    Given try to create a new event with data
      | match_id | timestamp  | description  |
      | 101      | 1624125650 | Missing type |
    Then received status code is 400

  Scenario: Create a new event successfully
    Given try to create a new event with data
      | match_id | event_type | timestamp  | description |
      | 101      | Result     | 1624125650 | First goal  |
    Then received status code is 201
    Then an event with type Result has been stored successfully

  Scenario: Get all events successfully
    Given try to create a new event with data
      | match_id | event_type | timestamp  | description |
      | 101      | Result     | 1624125650 | Result      |
    Then received status code is 201
    When try to get all events
    Then received status code is 200
    Then a list of events with names h2h,totals,spreads,Result are returned in response

  Scenario: Get event by eventType successfully
    Given try to create a new event with data
      | match_id | event_type | timestamp  | description |
      | 101      | Result     | 1624125650 | Result      |
    Then received status code is 201
    Then an event with type Result has been stored successfully
    When try to get an event by eventType
    Then received status code is 200
    Then an event with type Result has been returned in response

  Scenario: Get events by matchId successfully
    Given try to create a new event with data
      | match_id | event_type | name   | timestamp  | description |
      | 101      | Result     | Result | 1624125650 | First goal  |
    Then received status code is 201
    Then an event with type Result has been stored successfully
    When try to get events by matchId
    Then received status code is 200
    Then a list of events with names h2h,totals,Result are returned in response

  Scenario: Update an event successfully
    Given try to create a new event with data
      | match_id | event_type | timestamp  | description |
      | 101      | Result     | 1624125650 | Result      |
    Then received status code is 201
    Then an event with type Result has been stored successfully
    Given try to update an event with data
      | match_id | event_type  | timestamp  | description         |
      | 101      | Score First | 1624125655 | Team to Score First |
    Then received status code is 200
    Then an event with type Score First has been stored successfully

  Scenario: Delete event by eventType successfully
    Given try to create a new event with data
      | match_id | event_type | timestamp  | description |
      | 101      | Result     | 1624125650 | Result      |
    Then received status code is 201
    Then an event with type Result has been stored successfully
    When try to delete an event by eventType
    Then received status code is 200

    # ODDS
  Scenario: Try to create new odds by a non-admin player with error
    When an user with username normalUser and password normalPassword tries to log in
    Then received status code is 200
    Given try to create new odds with data
      | event_type | label     | value | active |
      | NewType    | Under 1.5 | 1.50  | true   |
    Then received status code is 403

  Scenario: Create new odds bad request
    Given try to create new odds with data
      | value | active |
      | 1.50  | true   |
    Then received status code is 400

  Scenario: Create new odds successfully
    Given try to create new odds with data
      | event_type | label     | value | active |
      | h2h        | Under 1.5 | 1.50  | true   |
    Then received status code is 201
    Then odds with label Under 1.5 have been stored successfully

  Scenario: Get all odds successfully
    Given try to create new odds with data
      | event_type | label     | value | active |
      | h2h        | Under 1.5 | 1.50  | true   |
    Then received status code is 201
    When try to get all odds
    Then received status code is 200
    Then a list of odds with labels Home Win,Away Win,Over 2.5,Under 1.5 are returned in response

  Scenario: Get odds by oddsId successfully
    Given try to create new odds with data
      | event_type | label     | value | active |
      | h2h        | Under 1.5 | 1.50  | true   |
    Then received status code is 201
    Then odds with label Under 1.5 have been stored successfully
    When try to get odds by oddsId
    Then received status code is 200
    Then odds with label Under 1.5 are returned in response

  Scenario: Get odds by eventType successfully
    Given try to create new odds with data
      | event_type | label     | value | active |
      | totals     | Under 1.5 | 1.50  | true   |
    Then received status code is 201
    Then odds with label Under 1.5 have been stored successfully
    When try to get odds by eventType
    Then received status code is 200
    Then a list of odds with labels Over 2.5,Under 1.5 are returned in response

  Scenario: Update odds successfully
    Given try to create new odds with data
      | event_type | label     | value | active |
      | h2h        | Under 1.5 | 1.50  | true   |
    Then received status code is 201
    Then odds with label Under 1.5 have been stored successfully
    Given try to update odds with data
      | event_type | label     | value | active |
      | h2h        | Under 3.5 | 2.10  | 0.40   |
    Then received status code is 200
    Then odds with label Under 3.5 have been stored successfully

  Scenario: Delete odds by oddsId successfully
    Given try to create new odds with data
      | event_type | label     | value | active |
      | h2h        | Under 1.5 | 1.50  | true   |
    Then received status code is 201
    Then odds with label Under 1.5 have been stored successfully
    When try to delete odds by oddsId
    Then received status code is 200

  # PLAYER BETS
  Scenario: Try to create new player bet by a non-admin player with error
    When an user with username normalUser and password normalPassword tries to log in
    Then received status code is 200
    Given try to create new player bet with data
      | match_id | league_id | username | selections                    |
      | 101      | 123       | someUser | 301,h2h,5.00;303,totals,10.00 |
    Then received status code is 403

  Scenario: Create new player bet bad request
    Given try to create new player bet with data
      | match_id | league_id | selections                    |
      | 101      | 123       | 301,h2h,5.00;303,totals,10.00 |
    Then received status code is 400

  Scenario: Create new player bet successfully
    Given try to create new player bet with data
      | match_id | league_id | username | selections                    |
      | 101      | 123       | someUser | 301,h2h,5.00;303,totals,10.00 |
    Then received status code is 201
    Then a player bet for username someUser has been stored successfully

  Scenario: Get all player bets successfully
    Given try to create new player bet with data
      | match_id | league_id | username | selections                    |
      | 101      | 123       | someUser | 301,h2h,5.00;303,totals,10.00 |
    Then received status code is 201
    Given try to create new player bet with data
      | match_id | league_id | username  | selections                    |
      | 101      | 123       | otherUser | 301,h2h,5.00;303,totals,10.00 |
    Then received status code is 201
    Then a player bet for username otherUser has been stored successfully
    When try to get all player bets
    Then received status code is 200
    Then a list of player bets with usernames someUser,otherUser is returned in response

  Scenario: Get player bet by playerBetId successfully
    Given try to create new player bet with data
      | match_id | league_id | username | selections                    |
      | 101      | 123       | someUser | 301,h2h,5.00;303,totals,10.00 |
    Then received status code is 201
    Then a player bet for username someUser has been stored successfully
    When try to get a player bet by playerBetId
    Then received status code is 200
    Then a player bet with username someUser is returned in response

  Scenario: Get player bets by matchId successfully
    Given try to create new player bet with data
      | match_id | league_id | username | selections                    |
      | 101      | 123       | someUser | 301,h2h,5.00;303,totals,10.00 |
    Then received status code is 201
    Then a player bet for username someUser has been stored successfully
    Given try to create new player bet with data
      | match_id | league_id | username  | selections                    |
      | 101      | 123       | otherUser | 301,h2h,5.00;303,totals,10.00 |
    Then received status code is 201
    Then a player bet for username otherUser has been stored successfully
    When try to get player bets by matchId
    Then received status code is 200
    Then a list of player bets with usernames someUser,otherUser is returned in response

  Scenario: Get player bets by username successfully
    Given try to create new player bet with data
      | match_id | league_id | username | selections                    |
      | 101      | 123       | someUser | 301,h2h,5.00;303,totals,10.00 |
    Then received status code is 201
    Then a player bet for username someUser has been stored successfully
    Given try to create new player bet with data
      | match_id | league_id | username | selections   |
      | 102      | 123       | someUser | 301,h2h,6.00 |
    Then received status code is 201
    Then a player bet for username someUser has been stored successfully
    When try to get player bets by username
    Then received status code is 200
    Then a list of player bets with usernames someUser is returned in response

  Scenario: Delete player bet by playerBetId successfully
    Given try to create new player bet with data
      | match_id | league_id | username | selections                    |
      | 101      | 123       | someUser | 301,h2h,5.00;303,totals,10.00 |
    Then received status code is 201
    Then a player bet for username someUser has been stored successfully
    When try to delete a player bet by playerBetId
    Then received status code is 200

  # TODO: PLAYER BET SELECTIONS ( IS NECESSARY ? )