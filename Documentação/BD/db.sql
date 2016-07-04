DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email TEXT
);

DROP TABLE IF EXISTS requests CASCADE;
CREATE TABLE requests (
    id SERIAL PRIMARY KEY,
    creator INTEGER NOT NULL REFERENCES users(id),
    timeout INTEGER NOT NULL DEFAULT 1000,
    minDaysBetweenSameTopicExams INTEGER NOT NULL DEFAULT 14,
	minDaysBetweenSameYearExams INTEGER NOT NULL DEFAULT 2,
    difficultyPenalty FLOAT NOT NULL DEFAULT 1.0,
    startingDate DATE NOT NULL,
    normalSeasonDuration INTEGER NOT NULL DEFAULT 21,
	appealSeasonDuration INTEGER NOT NULL DEFAULT 14,
    spreadPenalty FLOAT NOT NULL DEFAULT 10.0,
    enqueueingTime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    startTime TIMESTAMP,
    endTime TIMESTAMP
);

DROP TABLE IF EXISTS professors CASCADE;
CREATE TABLE professors (
    id SERIAL PRIMARY KEY,
    creator INTEGER NOT NULL REFERENCES users(id),
    name TEXT NOT NULL,
	acronym TEXT NOT NULL,
    cod TEXT NOT NULL
);

DROP TABLE IF EXISTS topics CASCADE;
CREATE TABLE topics (
    id SERIAL PRIMARY KEY,
    creator INTEGER REFERENCES users(id),
    name TEXT NOT NULL,
	acronym TEXT NOT NULL,
	code TEXT NOT NULL,
	year INTEGER NOT NULL,
	difficulty INTEGER NOT NULL DEFAULT 2,
    regent INTEGER NOT NULL REFERENCES professors(id)
);

DROP TABLE IF EXISTS exams CASCADE;
CREATE TABLE exams (
    id SERIAL PRIMARY KEY,
    creator INTEGER REFERENCES users(id),
	topic INTEGER REFERENCES topics(id),
    normal BOOLEAN NOT NULL DEFAULT TRUE,
	pc BOOLEAN NOT NULL DEFAULT FALSE,
    day DATE,
    time INT
);

DROP TABLE IF EXISTS students CASCADE;
CREATE TABLE students (
    id SERIAL PRIMARY KEY,
    creator INTEGER NOT NULL REFERENCES users(id),
    name TEXT NOT NULL,
    cod TEXT NOT NULL,
    entryYear INTEGER,
    currentYear INTEGER
);

DROP TABLE IF EXISTS studentTopic CASCADE;
CREATE TABLE studentTopic (
	student INTEGER REFERENCES students(id),
	topic INTEGER REFERENCES topics(id),
	PRIMARY KEY (student, topic)
);

DROP TABLE IF EXISTS rooms CASCADE;
CREATE TABLE rooms (
    id SERIAL PRIMARY KEY,
    creator INTEGER NOT NULL REFERENCES users(id),
    cod TEXT NOT NULL,
	capacity INTEGER NOT NULL,
	pc BOOLEAN NOT NULL
);

DROP TABLE IF EXISTS topicAuxiliars CASCADE;
CREATE TABLE topicAuxiliars (
    id SERIAL PRIMARY KEY,
    topic INTEGER NOT NULL REFERENCES topics(id),
    auxiliar INTEGER NOT NULL REFERENCES professors(id)
);

DROP TABLE IF EXISTS professorUnavailable CASCADE;
CREATE TABLE professorUnavailable (
    id SERIAL PRIMARY KEY,
    creator INTEGER NOT NULL REFERENCES users(id),
	professor INTEGER NOT NULL REFERENCES professors(id),
	day DATE NOT NULL,
	time INTEGER NOT NULL,
	UNIQUE(professor, day, time)
);

DROP TABLE IF EXISTS roomPeriodUnavailable CASCADE;
CREATE TABLE roomPeriodUnavailable (
    id SERIAL PRIMARY KEY,
    creator INTEGER NOT NULL REFERENCES users(id),
    room INTEGER NOT NULL REFERENCES rooms(id),
    day DATE NOT NULL,
    time INT NOT NULL
);

INSERT INTO users (email) VALUES ('up201304143@fe.up.pt');
INSERT INTO requests (creator, startingDate) VALUES (1, '2014-11-22');
INSERT INTO professors (creator, name, acronym, cod) VALUES (1, 'António Augusto de Sousa', 'AAS', 209500);
INSERT INTO topics (creator, name, acronym, code, year, difficulty, regent) VALUES (1, 'Computação Gráfica', 'CGRA', 'EIC0019', 2, 1, 1);
INSERT INTO exams (creator, topic, normal, pc) VALUES (1, 1, TRUE, FALSE);
INSERT INTO exams (creator, topic, normal, pc) VALUES (1, 1, FALSE, FALSE);
INSERT INTO rooms (creator, cod, capacity, pc) VALUES (1, 'B116', 69, FALSE);
INSERT INTO rooms (creator, cod, capacity, pc) VALUES (1, 'B120', 69, FALSE);
INSERT INTO rooms (creator, cod, capacity, pc) VALUES (1, 'B232C', 44, FALSE);
INSERT INTO students (creator, name, cod) VALUES (1, 'Test student', '201301234');
INSERT INTO studentTopic (student, topic) VALUES (1, 1);

SELECT * FROM requests;