DROP TABLE IF EXISTS locations, users, categories, compilations, events, participation_requests CASCADE;

CREATE TABLE IF NOT EXISTS locations (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
lat FLOAT,
uri FLOAT
);

CREATE TABLE IF NOT EXISTS users (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
email VARCHAR(50),
name VARCHAR(50),

CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
name VARCHAR(50),

CONSTRAINT uq_category_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS compilations (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
title VARCHAR(50),
is_pinned BOOLEAN
);

CREATE TABLE IF NOT EXISTS events (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
annotation VARCHAR(300),
category_id BIGINT,
confirmed_requests INT,
created_on TIMESTAMP,
description VARCHAR(500),
event_date TIMESTAMP,
initiator_id BIGINT,
location_id BIGINT,
is_paid BOOLEAN,
participant_limit INT,
published_on TIMESTAMP,
is_request_moderation BOOLEAN,
state VARCHAR(20),
title VARCHAR(50),
compilation_id BIGINT,

CONSTRAINT fk_events_to_categories FOREIGN KEY(category_id) REFERENCES categories(id),
CONSTRAINT fk_events_to_users FOREIGN KEY(initiator_id) REFERENCES users(id),
CONSTRAINT fk_events_to_locations FOREIGN KEY(location_id) REFERENCES locations(id),
CONSTRAINT fk_events_to_compilations FOREIGN KEY(compilation_id) REFERENCES compilations(id)
);

CREATE TABLE IF NOT EXISTS participation_requests (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
created TIMESTAMP,
event_id BIGINT,
requester_id BIGINT,
status VARCHAR(20),

CONSTRAINT fk_participation_requests_to_events FOREIGN KEY(event_id) REFERENCES events(id),
CONSTRAINT fk_participation_requests_to_users FOREIGN KEY(requester_id) REFERENCES users(id)
);