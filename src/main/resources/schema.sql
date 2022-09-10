CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name  VARCHAR(100)        NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS items
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR      NOT NULL,
    available   BOOL DEFAULT FALSE,
    owner_id    BIGINT REFERENCES users (id)
);


CREATE TABLE IF NOT EXISTS bookings
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start_booking TIMESTAMP WITHOUT TIME ZONE,
    end_booking   TIMESTAMP WITHOUT TIME ZONE,
    status        VARCHAR(30),
    booker_id     BIGINT REFERENCES users (id),
    item_id       BIGINT REFERENCES items (id)

);