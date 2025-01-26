-- Таблица пользователей
CREATE TABLE IF NOT EXISTS users
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    email
    VARCHAR
(
    255
) NOT NULL UNIQUE,
    name VARCHAR
(
    255
) NOT NULL
    );

-- Таблица запросов на добавление новых вещей
CREATE TABLE IF NOT EXISTS item_requests
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    description
    TEXT
    NOT
    NULL,
    requestor_id
    BIGINT
    NOT
    NULL
    REFERENCES
    users
(
    id
) ON DELETE CASCADE,
    created TIMESTAMP NOT NULL DEFAULT NOW
(
)
    );

-- Таблица вещей
CREATE TABLE IF NOT EXISTS items
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    owner_id
    BIGINT
    NOT
    NULL
    REFERENCES
    users
(
    id
) ON DELETE CASCADE,
    name VARCHAR
(
    255
) NOT NULL,
    description TEXT NOT NULL,
    is_available BOOLEAN NOT NULL,
    item_request_id BIGINT REFERENCES item_requests
(
    id
)
  ON DELETE SET NULL
    );

-- Таблица бронирования вещей
CREATE TABLE IF NOT EXISTS bookings
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    start_date
    TIMESTAMP
    NOT
    NULL,
    end_date
    TIMESTAMP
    NOT
    NULL,
    item_id
    BIGINT
    NOT
    NULL
    REFERENCES
    items
(
    id
) ON DELETE CASCADE,
    booker_id BIGINT NOT NULL REFERENCES users
(
    id
)
  ON DELETE CASCADE,
    status VARCHAR
(
    50
) NOT NULL CHECK
(
    status
    IN
(
    'WAITING',
    'APPROVED',
    'REJECTED',
    'CANCELLED'
))
    );

-- Таблица отзывов
CREATE TABLE IF NOT EXISTS comments
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    text
    TEXT
    NOT
    NULL,
    item_id
    BIGINT
    NOT
    NULL
    REFERENCES
    items
(
    id
) ON DELETE CASCADE,
    author_id BIGINT NOT NULL REFERENCES users
(
    id
)
  ON DELETE CASCADE,
    created TIMESTAMP NOT NULL DEFAULT NOW
(
)
    );
