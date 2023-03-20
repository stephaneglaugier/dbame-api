drop  table if exists ballots;
drop  table if exists permutation_moderator;

CREATE TABLE ballots (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    timestamp TIMESTAMP NOT NULL,
    randint INT NOT NULL,
    w VARCHAR(512) NOT NULL,
    s VARCHAR(512) NOT NULL,
    permutation BIGINT NOT NULL
);

CREATE TABLE permutation_moderator (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    permutation BIGINT NOT NULL
);
