
CREATE TABLE IF NOT EXISTS motos_costosas (
                                    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
                                    marca VARCHAR(100) NOT NULL,
                                    modelo VARCHAR(100) NOT NULL,
                                    top_speed INTEGER NOT NULL  -- en km/h
);
