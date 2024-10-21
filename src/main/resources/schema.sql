-- Tabela de usuários
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(20) NOT NULL
);

-- Tabela de mídias
CREATE TABLE IF NOT EXISTS media (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    title VARCHAR(255) NOT NULL,
    release_date DATE NOT NULL,
    average_rating DECIMAL(4, 2) CHECK (average_rating >= 0 AND average_rating <= 10.0),
    type VARCHAR(20) NOT NULL
);

-- Tabela de junção para relacionar usuários e mídias
CREATE TABLE IF NOT EXISTS user_media (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id BIGINT NOT NULL,
    media_id BIGINT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_media FOREIGN KEY (media_id) REFERENCES media(id) ON DELETE CASCADE,
    UNIQUE(user_id, media_id)  -- Para garantir que a combinação de user e media seja única
);
