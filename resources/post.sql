CREATE TABLE IF NOT EXISTS post (
   id SERIAL PRIMARY KEY,
   name TEXT,
   text TEXT,
   link TEXT,
   created TIMESTAMP,
   UNIQUE(link)
);
