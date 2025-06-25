-- V1__init_schema.sql
-- Tables initialization.

-- League categories table.
CREATE TABLE league_category (
    category_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(256),
    sport VARCHAR(100) NOT NULL,
    country VARCHAR(2) NOT NULL,
    icon_url VARCHAR(256),
    is_active BOOLEAN NOT NULL DEFAULT FALSE,
    creation_date BIGINT NOT NULL,
    modification_date BIGINT
);

-- Match day tables by category, season and number.
CREATE TABLE match_day (
    category_id VARCHAR(50) NOT NULL,
    season INTEGER NOT NULL,
    match_day_number INTEGER NOT NULL,
    start_date BIGINT NOT NULL,
    end_date BIGINT NOT NULL,
    synced BOOLEAN NOT NULL,
    results_checked BOOLEAN NOT NULL,
    PRIMARY KEY (category_id, season, match_day_number),
    CONSTRAINT fk_matchday_category FOREIGN KEY (category_id)
        REFERENCES league_category(category_id)
        ON DELETE CASCADE
);

CREATE INDEX idx_matchday_category ON match_day(category_id);
CREATE INDEX idx_matchday_startdate_synced ON match_day(start_date, synced);
CREATE INDEX idx_match_day_enddate_results_checked ON match_day (end_date, results_checked);

-- Leagues table.
CREATE TABLE league (
    league_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(256),
    category_id VARCHAR(50) NOT NULL,
    is_public BOOLEAN NOT NULL,
    password VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    max_players INTEGER,
    logo_url VARCHAR(255),
    is_premium BOOLEAN NOT NULL,
    owner_username VARCHAR(100) NOT NULL,
    creation_date BIGINT NOT NULL,
    modification_date BIGINT,
    CONSTRAINT fk_league_category FOREIGN KEY (category_id)
        REFERENCES league_category(category_id)
        ON DELETE CASCADE
);

CREATE INDEX idx_league_category ON league(category_id);

-- Players per league table.
CREATE TABLE league_player (
    league_id BIGINT NOT NULL,
    username VARCHAR(100) NOT NULL,
    total_points INTEGER NOT NULL,
    player_status VARCHAR(255) NOT NULL,
    join_date BIGINT NOT NULL,
    PRIMARY KEY (league_id, username),
    CONSTRAINT fk_leagueplayer_league FOREIGN KEY (league_id)
        REFERENCES league (league_id)
        ON DELETE CASCADE
);

CREATE INDEX idx_leagueplayer_league ON league_player (league_id);

-- Players per league and theis points table.
CREATE TABLE league_player_points (
    category_id VARCHAR(50) NOT NULL,
    season INTEGER NOT NULL,
    match_day_number INTEGER NOT NULL,
    league_id BIGINT NOT NULL,
    username VARCHAR(100) NOT NULL,
    points INTEGER NOT NULL,
    PRIMARY KEY (category_id, season, match_day_number, league_id, username),
    CONSTRAINT fk_lpp_matchday FOREIGN KEY (category_id, season, match_day_number)
        REFERENCES match_day (category_id, season, match_day_number)
        ON DELETE CASCADE,
    CONSTRAINT fk_lpp_leagueplayer FOREIGN KEY (league_id, username)
        REFERENCES league_player (league_id, username)
        ON DELETE CASCADE
);

CREATE INDEX idx_lpp_league_user ON league_player_points (league_id, username);
CREATE INDEX idx_lpp_matchday ON league_player_points (category_id, season, match_day_number);
