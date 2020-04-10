CREATE TABLE junction (
    id serial8 NOT NULL PRIMARY KEY,
    name varchar(64) NOT NULL,
    longitude float8 NOT NULL,
    latitude float8 NOT NULL
);

CREATE TABLE route (
    id serial8 NOT NULL PRIMARY KEY,
    junction_id integer NOT NULL REFERENCES junction(id)
);

CREATE TABLE point (
    id serial8 NOT NULL PRIMARY KEY,
    longitude float8 NOT NULL,
    latitude float8 NOT NULL,
    route_id integer NOT NULL REFERENCES route(id)
);

INSERT INTO junction (name, longitude, latitude) VALUES
    ('МКАД - ш. Энтузиастов', 37.84270, 55.77692),
    ('МКАД - Каширское ш.', 37.72944, 55.59180),
    ('МКАД - Рязанский пр.', 37.83499, 55.70789);

INSERT INTO route (junction_id) VALUES (1), (1), (2), (3), (3), (3), (3);
