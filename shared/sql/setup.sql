CREATE TABLE junction (
    id serial8 NOT NULL PRIMARY KEY,
    name varchar(64) NOT NULL,
    longitude float8 NOT NULL,
    latitude float8 NOT NULL
);

INSERT INTO junction (name, longitude, latitude) VALUES
    ('МКАД - ш. Энтузиастов', 37.84270, 55.77692),
    ('МКАД - Каширское ш.', 37.72944, 55.59180),
    ('МКАД - Рязанский пр.', 37.83499, 55.70789);
