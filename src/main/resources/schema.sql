CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name  VARCHAR(100)        NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS requests
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description        VARCHAR NOT NULL,
    request_creator_id BIGINT REFERENCES users (id),
    created            TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS items
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR      NOT NULL,
    available   BOOL DEFAULT FALSE,
    owner_id    BIGINT REFERENCES users (id),
    request_id  BIGINT REFERENCES requests(id)
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

CREATE TABLE IF NOT EXISTS comments
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text_comment VARCHAR(250),
    author_id    BIGINT REFERENCES users (id),
    item_id      BIGINT REFERENCES items (id),
    created      TIMESTAMP WITHOUT TIME ZONE
);