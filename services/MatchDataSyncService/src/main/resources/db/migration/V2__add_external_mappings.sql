-- V2__add_external_mappings.sql
-- Tables initialization.

-- Table to map internal categoryId and external sport key.
CREATE TABLE external_category_mapping (
    category_id VARCHAR(50) PRIMARY KEY,
    external_sport_key VARCHAR(100) NOT NULL
);

-- Table to map internal odds label and external market key.
CREATE TABLE external_odds_mapping (
    odds_label VARCHAR(50) PRIMARY KEY,
    external_market_key VARCHAR(100) NOT NULL
);
