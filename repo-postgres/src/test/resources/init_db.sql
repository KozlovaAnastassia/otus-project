DROP TABLE IF EXISTS public.memes;

CREATE TABLE public.memes (
                              id VARCHAR(255) PRIMARY KEY,
                              title VARCHAR(255),
                              tags TEXT,
                              image TEXT,
                              image_url TEXT,
                              created_at VARCHAR(255),
                              author_id VARCHAR(255),
                              lock VARCHAR(255) NOT NULL,
                              visibility VARCHAR(50) NOT NULL
);