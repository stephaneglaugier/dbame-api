drop  table if exists ballots;
drop  table if exists moderator_relay;
drop  table if exists permutation_moderator;


CREATE TABLE ballots (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    timestamp TIMESTAMP NOT NULL,
    randint INT NOT NULL,
    w VARCHAR(255) NOT NULL,
    s VARCHAR(255) NOT NULL,
    permutation BIGINT NOT NULL
);

CREATE TABLE moderator_relay (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    y VARCHAR(255) NOT NULL,
    maskedY VARCHAR(255) NOT NULL,
    w VARCHAR(255) NOT NULL,
    s VARCHAR(255) NOT NULL,
    permutation BIGINT NOT NULL,
    blindFactor VARCHAR(255) NOT NULL,
    cypherText VARCHAR(255) NOT NULL,
    ephemeralKey VARCHAR(255) NOT NULL
);

CREATE TABLE permutation_moderator (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    a BIGINT NOT NULL,
    b BIGINT NOT NULL
);
