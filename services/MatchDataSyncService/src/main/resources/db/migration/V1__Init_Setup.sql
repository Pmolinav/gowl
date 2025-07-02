-- V1__init_schema.sql
-- Tables initialization.

-- Match table.
CREATE TABLE match (
    match_id BIGSERIAL PRIMARY KEY,
    category_id VARCHAR(50) NOT NULL,
    match_day_number INTEGER NOT NULL,
    season INTEGER NOT NULL,
    home_team VARCHAR(100) NOT NULL,
    away_team VARCHAR(100) NOT NULL,
    start_time BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    external_id VARCHAR(100),
    creation_date BIGINT DEFAULT EXTRACT(EPOCH FROM NOW()) * 1000,
    modification_date BIGINT DEFAULT EXTRACT(EPOCH FROM NOW()) * 1000
);

CREATE INDEX idx_category_season_day ON match (category_id, season, match_day_number);

-- Event table.
CREATE TABLE event (
    event_type VARCHAR(100) PRIMARY KEY,
    description VARCHAR(255),
    creation_date BIGINT DEFAULT EXTRACT(EPOCH FROM NOW()) * 1000,
    modification_date BIGINT DEFAULT EXTRACT(EPOCH FROM NOW()) * 1000
);

-- Odds table.
CREATE TABLE odds (
    odds_id BIGSERIAL PRIMARY KEY,
    event_type VARCHAR(100) NOT NULL,
    match_id BIGINT NOT NULL,
    label VARCHAR(50) NOT NULL,
    value DECIMAL(10,2) NOT NULL,
    point DECIMAL(6,2),
    provider VARCHAR(50),
    active BOOLEAN DEFAULT TRUE,
    creation_date BIGINT DEFAULT EXTRACT(EPOCH FROM NOW()) * 1000,
    modification_date BIGINT DEFAULT EXTRACT(EPOCH FROM NOW()) * 1000,
    CONSTRAINT fk_odds_event FOREIGN KEY (event_type)
        REFERENCES event (event_type) ON DELETE CASCADE,
    CONSTRAINT fk_event_match FOREIGN KEY (match_id)
        REFERENCES match (match_id) ON DELETE CASCADE
);

CREATE INDEX idx_odds_event_type ON odds (event_type);
CREATE INDEX idx_odds_match_id ON odds (match_id);

-- Player bet table.
CREATE TABLE player_bet (
    bet_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    match_id BIGINT NOT NULL,
    league_id BIGINT NOT NULL,
    total_stake DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    creation_date BIGINT DEFAULT EXTRACT(EPOCH FROM NOW()) * 1000,
    CONSTRAINT fk_player_bet_match FOREIGN KEY (match_id)
        REFERENCES match (match_id) ON DELETE CASCADE
);

CREATE INDEX idx_player_bet_username ON player_bet (username);
CREATE INDEX idx_player_bet_match_id ON player_bet (match_id);

-- Player bet selection table.
CREATE TABLE player_bet_selection (
    selection_id BIGSERIAL PRIMARY KEY,
    bet_id BIGINT NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    odds_id BIGINT NOT NULL,
    stake DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    creation_date BIGINT DEFAULT EXTRACT(EPOCH FROM NOW()) * 1000,
    CONSTRAINT fk_player_bet_selection_bet FOREIGN KEY (bet_id)
        REFERENCES player_bet (bet_id) ON DELETE CASCADE,
    CONSTRAINT fk_player_bet_selection_odds FOREIGN KEY (odds_id)
        REFERENCES odds (odds_id) ON DELETE CASCADE,
    CONSTRAINT fk_player_bet_selection_event FOREIGN KEY (event_type)
        REFERENCES event (event_type) ON DELETE CASCADE
);

CREATE INDEX idx_player_bet_selection_bet_id ON player_bet_selection (bet_id);
CREATE INDEX idx_player_bet_selection_odds_id ON player_bet_selection (odds_id);
