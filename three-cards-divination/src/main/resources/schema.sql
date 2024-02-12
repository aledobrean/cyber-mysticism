CREATE TABLE IF NOT EXISTS tarot_card (
    id SERIAL PRIMARY KEY,
    name varchar(255) UNIQUE,
    number integer,
    arcana varchar(255),
    img varchar(255) UNIQUE,
    fortune_telling varchar[]
);
