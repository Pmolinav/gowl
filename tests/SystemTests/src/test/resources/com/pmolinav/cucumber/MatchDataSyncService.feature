Feature: MatchDataSyncService

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
      | category_id | name       | description                        | sport    | country | icon_url             | is_active | creation_date | modification_date |
      | MLS         | MLS League | North American 1st Division League | FOOTBALL | EU      | http://example.com/1 | true      | 123456        | 123456            |
    Given the following match days have been stored previously
      | category_id | season | match_day_number | synced | results_checked |
      | MLS         | 2025   | 1                | false  | false           |
    Given the following categories mappings have been stored previously
      | category_id | external_sport_key |
      | MLS         | soccer_usa_mls     |
    Given the following events have been stored previously
      | event_type | description            | creation_date | modification_date |
      | h2h        | Head to Head event     | 1624000000    | 1624005000        |
      | h2h_lay    | Head to Head Lay event | 1624000000    | 1624005000        |
      | totals     | Total Goals event      | 1624001000    | 1624006000        |
      | spreads    | Handicap event         | 1624002000    | 1624007000        |
    When an user with username normalUser and password normalPassword tries to log in
    Then received status code is 200

   # SYNCHRONIZE MATCHES AND RESULTS
  Scenario: E2E test to synchronize matches and results from external API
    # 1. An user registers and creates a league for previously created category.
    Given try to create a new league with public endpoint with data
      | name            | description          | category_id | is_public | password | status | max_players | logo_url             | is_premium | owner_username | league_players      |
      | Friendly League | A League for Friends | MLS         | false     | fiends   | ACTIVE | 20          | http://example.com/1 | false      | normalUser     | normalUser,0,ACTIVE |
    Then received status code is 201
    Then a league with name Friendly League and status ACTIVE has been stored successfully
    Then a player with username normalUser has been associated to last league successfully
    # 2. Other user registers too and joins to the league.
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200
    Given try to create several league players with public endpoint with data
      | username | total_points | status |
      | someUser | 0            | ACTIVE |
    Then received status code is 201
    Then a player with username someUser has been associated to last league successfully
    # 3. Mock responses from External API to get matches and results calls.
    Given mock odds response from external API for external category mapping soccer_usa_mls
    Given mock results response from external API for external category mapping soccer_usa_mls
    # 4. Wait for the scheduled method (matches) to be executed.
    Then wait for 80 seconds
    # 5. Assert that the expected matches, events and odds are created successfully from JSON response.
    # MATCH 1
    Then a match with categoryId MLS with home team Chicago Fire and away team Charlotte FC has been stored successfully
    Then an event with type h2h has been stored successfully
    Then odds with label Charlotte FC and point N/A have been stored successfully for the last event type
    Then odds with label Chicago Fire and point N/A have been stored successfully for the last event type
    Then odds with label Draw and point N/A have been stored successfully for the last event type
    Then an event with type totals has been stored successfully
    Then odds with label Over and point 3.5 have been stored successfully for the last event type
    Then odds with label Under and point 3.5 have been stored successfully for the last event type
    Then an event with type spreads has been stored successfully
    Then odds with label Charlotte FC and point 0.25 have been stored successfully for the last event type
    Then odds with label Chicago Fire and point -0.25 have been stored successfully for the last event type
    # MATCH 2
    Then a match with categoryId MLS with home team FC Dallas and away team San Diego FC has been stored successfully
    Then an event with type h2h has been stored successfully
    Then odds with label San Diego FC and point N/A have been stored successfully for the last event type
    Then odds with label FC Dallas and point N/A have been stored successfully for the last event type
    Then odds with label Draw and point N/A have been stored successfully for the last event type
    Then an event with type totals has been stored successfully
    Then odds with label Over and point 3.5 have been stored successfully for the last event type
    Then odds with label Under and point 3.5 have been stored successfully for the last event type
    Then an event with type spreads has been stored successfully
    Then odds with label San Diego FC and point -0.25 have been stored successfully for the last event type
    Then odds with label FC Dallas and point 0.25 have been stored successfully for the last event type
    # MATCH 3
    Then a match with categoryId MLS with home team Houston Dynamo and away team St. Louis City SC has been stored successfully
    Then an event with type h2h has been stored successfully
    Then odds with label St. Louis City SC and point N/A have been stored successfully for the last event type
    Then odds with label Houston Dynamo and point N/A have been stored successfully for the last event type
    Then odds with label Draw and point N/A have been stored successfully for the last event type
    Then an event with type totals has been stored successfully
    Then odds with label Over and point 2.5 have been stored successfully for the last event type
    Then odds with label Under and point 2.5 have been stored successfully for the last event type
    Then an event with type spreads has been stored successfully
    Then odds with label Houston Dynamo and point -0.5 have been stored successfully for the last event type
    Then odds with label St. Louis City SC and point 0.5 have been stored successfully for the last event type
    # MATCH 4
    Then a match with categoryId MLS with home team Sporting Kansas City and away team Real Salt Lake has been stored successfully
    Then an event with type h2h has been stored successfully
    Then odds with label Real Salt Lake and point N/A have been stored successfully for the last event type
    Then odds with label Sporting Kansas City and point N/A have been stored successfully for the last event type
    Then odds with label Draw and point N/A have been stored successfully for the last event type
    Then an event with type totals has been stored successfully
    Then odds with label Over and point 2.5 have been stored successfully for the last event type
    Then odds with label Under and point 2.5 have been stored successfully for the last event type
    Then an event with type spreads has been stored successfully
    Then odds with label Sporting Kansas City and point 0 have been stored successfully for the last event type
    Then odds with label Real Salt Lake and point 0 have been stored successfully for the last event type
    # MATCH 5
    Then a match with categoryId MLS with home team Seattle Sounders FC and away team Austin FC has been stored successfully
    Then an event with type h2h has been stored successfully
    Then odds with label Austin FC and point N/A have been stored successfully for the last event type
    Then odds with label Seattle Sounders FC and point N/A have been stored successfully for the last event type
    Then odds with label Draw and point N/A have been stored successfully for the last event type
    Then an event with type totals has been stored successfully
    Then odds with label Over and point 2.5 have been stored successfully for the last event type
    Then odds with label Under and point 2.5 have been stored successfully for the last event type
    Then an event with type spreads has been stored successfully
    Then odds with label Seattle Sounders FC and point -0.75 have been stored successfully for the last event type
    Then odds with label Austin FC and point 0.75 have been stored successfully for the last event type
    # MATCH 6
    Then a match with categoryId MLS with home team San Jose Earthquakes and away team LA Galaxy has been stored successfully
    Then an event with type h2h has been stored successfully
    Then odds with label LA Galaxy and point N/A have been stored successfully for the last event type
    Then odds with label San Jose Earthquakes and point N/A have been stored successfully for the last event type
    Then odds with label Draw and point N/A have been stored successfully for the last event type
    Then an event with type totals has been stored successfully
    Then odds with label Over and point 3.5 have been stored successfully for the last event type
    Then odds with label Under and point 3.5 have been stored successfully for the last event type
    Then an event with type spreads has been stored successfully
    Then odds with label San Jose Earthquakes and point -1 have been stored successfully for the last event type
    Then odds with label LA Galaxy and point 1 have been stored successfully for the last event type
    # MATCH 7
    Then a match with categoryId MLS with home team Columbus Crew SC and away team Philadelphia Union has been stored successfully
    Then an event with type h2h has been stored successfully
    Then odds with label Philadelphia Union and point N/A have been stored successfully for the last event type
    Then odds with label Columbus Crew SC and point N/A have been stored successfully for the last event type
    Then odds with label Draw and point N/A have been stored successfully for the last event type
    Then an event with type totals has been stored successfully
    Then odds with label Over and point 3.5 have been stored successfully for the last event type
    Then odds with label Under and point 3.5 have been stored successfully for the last event type
    Then an event with type spreads has been stored successfully
    Then odds with label Columbus Crew SC and point -0.5 have been stored successfully for the last event type
    Then odds with label Philadelphia Union and point 0.5 have been stored successfully for the last event type
    # 6. Both players bet for several matches.
    Then a match with categoryId MLS with home team Chicago Fire and away team Charlotte FC has been stored successfully
    # WON
    When an user with username normalUser and password normalPassword tries to log in
    Then received status code is 200
    Given user normalUser tries to create new player bet with data with public endpoint
      | label        | point | event_type | expected |
      | Chicago Fire | N/A   | h2h        | WON      |
      | Under        | 3.5   | totals     | WON      |
    # LOST
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200
    Given user someUser tries to create new player bet with data with public endpoint
      | label        | point | event_type | expected |
      | Charlotte FC | N/A   | h2h        | LOST     |
      | Under        | 3.5   | totals     | WON      |
    Then a match with categoryId MLS with home team FC Dallas and away team San Diego FC has been stored successfully
    # LOST
    When an user with username normalUser and password normalPassword tries to log in
    Then received status code is 200
    Given user normalUser tries to create new player bet with data with public endpoint
      | label        | point | event_type | expected |
      | San Diego FC | N/A   | h2h        | LOST     |
    # WON
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200
    Given user someUser tries to create new player bet with data with public endpoint
      | label     | point | event_type | expected |
      | Draw      | N/A   | h2h        | WON      |
      | Over      | 3.5   | totals     | WON      |
      | FC Dallas | 0.25  | spreads    | WON      |
    Then a match with categoryId MLS with home team Houston Dynamo and away team St. Louis City SC has been stored successfully
    # WON
    When an user with username normalUser and password normalPassword tries to log in
    Then received status code is 200
    Given user normalUser tries to create new player bet with data with public endpoint
      | label          | point | event_type | expected |
      | Houston Dynamo | -0.5  | spreads    | WON      |
    # WON
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200
    Given user someUser tries to create new player bet with data with public endpoint
      | label          | point | event_type | expected |
      | Houston Dynamo | N/A   | h2h        | WON      |
      | Under          | 2.5   | totals     | WON      |
    Then a match with categoryId MLS with home team Sporting Kansas City and away team Real Salt Lake has been stored successfully
    # PUSH
    When an user with username normalUser and password normalPassword tries to log in
    Then received status code is 200
    Given user normalUser tries to create new player bet with data with public endpoint
      | label                | point | event_type | expected |
      | Sporting Kansas City | 0     | spreads    | PUSH     |
    # WON
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200
    Given user someUser tries to create new player bet with data with public endpoint
      | label          | point | event_type | expected |
      | Real Salt Lake | 0     | spreads    | PUSH     |
      | Under          | 2.5   | totals     | WON      |
    Then a match with categoryId MLS with home team Seattle Sounders FC and away team Austin FC has been stored successfully
    # WON
    When an user with username normalUser and password normalPassword tries to log in
    Then received status code is 200
    Given user normalUser tries to create new player bet with data with public endpoint
      | label               | point | event_type | expected |
      | Seattle Sounders FC | N/A   | h2h        | WON      |
      | Over                | 2.5   | totals     | WON      |
    # LOST
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200
    Given user someUser tries to create new player bet with data with public endpoint
      | label               | point | event_type | expected |
      | Seattle Sounders FC | N/A   | h2h        | WON      |
      | Under               | 2.5   | totals     | LOST     |
    Then a match with categoryId MLS with home team San Jose Earthquakes and away team LA Galaxy has been stored successfully
    # LOST
    When an user with username normalUser and password normalPassword tries to log in
    Then received status code is 200
    Given user normalUser tries to create new player bet with data with public endpoint
      | label                | point | event_type | expected |
      | San Jose Earthquakes | N/A   | h2h        | LOST     |
      | Under                | 3.5   | totals     | LOST     |
    # LOST
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200
    Given user someUser tries to create new player bet with data with public endpoint
      | label     | point | event_type | expected |
      | LA Galaxy | N/A   | h2h        | WON      |
      | Under     | 3.5   | totals     | LOST     |
    Then a match with categoryId MLS with home team Columbus Crew SC and away team Philadelphia Union has been stored successfully
    # WON
    When an user with username normalUser and password normalPassword tries to log in
    Then received status code is 200
    Given user normalUser tries to create new player bet with data with public endpoint
      | label            | point | event_type | expected |
      | Columbus Crew SC | -0.5  | spreads    | WON      |
    # WON
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200
    Given user someUser tries to create new player bet with data with public endpoint
      | label              | point | event_type | expected |
      | Philadelphia Union | 0.5   | spreads    | LOST     |
      | Under              | 3.5   | totals     | WON      |
    # 7. Wait for the scheduled method (results) to be executed.
    Then wait for 110 seconds
    # TODO: Why this part is not working now ?
    # 8. Assert that the different bets are successfully stored as WON, LOST or PUSH
    Then a player bet for username normalUser, home team Chicago Fire and away team Charlotte FC has status WON and selections
      | event_type | status |
      | h2h        | WON    |
      | totals     | WON    |
    Then a player bet for username someUser, home team Chicago Fire and away team Charlotte FC has status LOST and selections
      | event_type | status |
      | h2h        | LOST   |
      | totals     | WON    |
    Then a player bet for username normalUser, home team FC Dallas and away team San Diego FC has status LOST and selections
      | event_type | status |
      | h2h        | LOST   |
    Then a player bet for username someUser, home team FC Dallas and away team San Diego FC has status WON and selections
      | event_type | status |
      | h2h        | WON    |
      | totals     | WON    |
      | spreads    | WON    |
    Then a player bet for username normalUser, home team Houston Dynamo and away team St. Louis City SC has status WON and selections
      | event_type | status |
      | spreads    | WON    |
    Then a player bet for username someUser, home team Houston Dynamo and away team St. Louis City SC has status WON and selections
      | event_type | status |
      | h2h        | WON    |
      | totals     | WON    |
    Then a player bet for username normalUser, home team Sporting Kansas City and away team Real Salt Lake has status PUSH and selections
      | event_type | status |
      | spreads    | PUSH   |
    Then a player bet for username someUser, home team Sporting Kansas City and away team Real Salt Lake has status WON and selections
      | event_type | status |
      | spreads    | PUSH   |
      | totals     | WON    |
    Then a player bet for username normalUser, home team Seattle Sounders FC and away team Austin FC has status WON and selections
      | event_type | status |
      | h2h        | WON    |
      | totals     | WON    |
    Then a player bet for username someUser, home team Seattle Sounders FC and away team Austin FC has status LOST and selections
      | event_type | status |
      | h2h        | WON    |
      | totals     | LOST   |
    Then a player bet for username normalUser, home team San Jose Earthquakes and away team LA Galaxy has status LOST and selections
      | event_type | status |
      | h2h        | LOST   |
      | totals     | LOST   |
    Then a player bet for username someUser, home team San Jose Earthquakes and away team LA Galaxy has status LOST and selections
      | event_type | status |
      | h2h        | WON    |
      | totals     | LOST   |
    Then a player bet for username normalUser, home team Columbus Crew SC and away team Philadelphia Union has status WON and selections
      | event_type | status |
      | spreads    | WON    |
    Then a player bet for username someUser, home team Columbus Crew SC and away team Philadelphia Union has status LOST and selections
      | event_type | status |
      | spreads    | LOST   |
      | totals     | WON    |
    # 9. Assert that points are added OK to leaderboard.
    Then a match day with categoryId MLS, season 2025 and number 1 has been stored with results checked successfully
    Then the total points for user normalUser should be updated
    Then the total points for user someUser should be updated
