
CREATE TABLE IF NOT EXISTS stat (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  app VARCHAR(128) NOT NULL,
  uri VARCHAR(255) NOT NULL,
  ip VARCHAR(64) NOT NULL,
  timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  CONSTRAINT pk_stat PRIMARY KEY (id)
);