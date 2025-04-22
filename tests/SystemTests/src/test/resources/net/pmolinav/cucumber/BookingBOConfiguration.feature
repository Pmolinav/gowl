Feature: BookingBOConfiguration

  Background:
    Given the following users have been stored previously
      | username | password     | name     | email          | role  | creation_date | modification_date |
      | someUser | somePassword | someName | some@email.com | ADMIN | 123456        | 123456            |
    When an user with username someUser and password somePassword tries to log in
    Then received status code is 200
    # Create new user previously to store a valid userId
    When try to create a new user with data
      | username | password    | name     | email          | role  |
      | newUser  | newPassword | someName | some@email.com | ADMIN |
    Then received status code is 201
    Then an user with username newUser has been stored successfully
    # Create new activity previously to store a valid activity name
    When try to create a new activity with data
      | name | description                          | price |
      | GYM  | Some description of the GYM activity | 520   |
    Then received status code is 201
    Then an activity with name GYM has been stored successfully

  Scenario: Create a new booking unauthorized
    Given invalid auth token
    When try to create a new booking with data
      | status   |
      | ACCEPTED |
    Then received status code is 401

  Scenario: Create a new booking successfully
    When try to create a new booking with data
      | status |
      | OPEN   |
    Then received status code is 201
    Then a booking with status OPEN has been stored successfully
    Then an entity Booking with id N/A has been stored into historical by user Admin and with type CREATE

  Scenario: Create a new booking request
    When try to create a new booking with data
      | status   | start_time |
      | FINISHED | 1234       |
    Then received status code is 400

  Scenario: Get all bookings successfully
    When try to create a new booking with data
      | status   |
      | FINISHED |
      | OPEN     |
    Then received status code is 201
    Then a booking with status OPEN has been stored successfully
    Then a booking with status FINISHED has been stored successfully
    When try to get all bookings
    Then received status code is 200
    Then a list of bookings with statuses FINISHED,OPEN are returned in response

  Scenario: Get booking by bookingId successfully
    When try to create a new booking with data
      | status |
      | OPEN   |
    Then received status code is 201
    Then a booking with status OPEN has been stored successfully
    When try to get a booking by bookingId
    Then received status code is 200
    Then a booking with status OPEN is returned in response

  Scenario: Delete booking by bookingId successfully
    When try to create a new booking with data
      | status    |
      | CANCELLED |
    Then received status code is 201
    Then a booking with status CANCELLED has been stored successfully
    When try to delete a booking by bookingId
    Then received status code is 200
    Then an entity Booking with id N/A has been stored into historical by user Admin and with type CREATE
    Then an entity Booking with id N/A has been stored into historical by user Admin and with type DELETE