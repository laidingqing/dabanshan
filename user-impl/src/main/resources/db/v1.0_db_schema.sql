USE users;

CREATE TABLE IF NOT EXISTS users(
  id              varchar, PRIMARY KEY (id)
  username        varchar,
  email           varchar,
  first_name      varchar,
  last_name       varchar,
  hashed_password varchar,
);

CREATE MATERIALIZED VIEW IF NOT EXISTS users_by_username AS
  SELECT * FROM users
  WHERE username IS NOT NULL
  PRIMARY KEY (username, userid);