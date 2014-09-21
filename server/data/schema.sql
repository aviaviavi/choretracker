CREATE TABLE user (
    phone_number    INTEGER NOT NULL PRIMARY KEY,
    user_name       TEXT NOT NULL UNIQUE,
    group_name      TEXT
);

CREATE TABLE chore (
    chore_name        TEXT NOT NULL,
    description       TEXT,
    group_name        TEXT NOT NULL,
    user_name         TEXT NOT NULL,
    day_of_week       INTEGER NOT NULL,
    repeat            INTEGER NOT NULL
);
