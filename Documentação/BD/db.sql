SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email TEXT
);

DROP TABLE IF EXISTS requests CASCADE;
CREATE TABLE requests (
    id SERIAL PRIMARY KEY,
	creator BIGINT UNSIGNED NOT NULL REFERENCES users(id) ON DELETE CASCADE,
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
    creator BIGINT UNSIGNED NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name TEXT NOT NULL,
	acronym TEXT NOT NULL,
    cod TEXT NOT NULL
);

DROP TABLE IF EXISTS topics CASCADE;
CREATE TABLE topics (
    id SERIAL PRIMARY KEY,
    creator BIGINT UNSIGNED NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name TEXT NOT NULL,
	acronym TEXT NOT NULL,
	code TEXT NOT NULL,
	year INTEGER NOT NULL,
	difficulty INTEGER NOT NULL DEFAULT 2
);

DROP TABLE IF EXISTS topicRegent CASCADE;
CREATE TABLE topicRegent (
	id SERIAL PRIMARY KEY,
	topic BIGINT UNSIGNED NOT NULL REFERENCES topics(id) ON DELETE CASCADE,
	regent BIGINT UNSIGNED NOT NULL REFERENCES professors(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS exams CASCADE;
CREATE TABLE exams (
    id SERIAL PRIMARY KEY,
    creator BIGINT UNSIGNED NOT NULL REFERENCES users(id) ON DELETE CASCADE,
	topic BIGINT UNSIGNED NOT NULL REFERENCES topics(id) ON DELETE CASCADE,
    normal BOOLEAN NOT NULL DEFAULT TRUE,
	pc BOOLEAN NOT NULL DEFAULT FALSE,
    day DATE,
    time INT
);

DROP TABLE IF EXISTS students CASCADE;
CREATE TABLE students (
    id SERIAL PRIMARY KEY,
    creator BIGINT UNSIGNED NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name TEXT NOT NULL,
    cod TEXT NOT NULL,
    entryYear INTEGER,
    currentYear INTEGER
);

DROP TABLE IF EXISTS studentTopic CASCADE;
CREATE TABLE studentTopic (
	student BIGINT UNSIGNED NOT NULL REFERENCES students(id) ON DELETE CASCADE,
	topic BIGINT UNSIGNED NOT NULL REFERENCES topics(id) ON DELETE CASCADE,
	PRIMARY KEY (student, topic)
);

DROP TABLE IF EXISTS rooms CASCADE;
CREATE TABLE rooms (
    id SERIAL PRIMARY KEY,
    creator BIGINT UNSIGNED NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    cod TEXT NOT NULL,
	capacity INTEGER NOT NULL,
	pc BOOLEAN NOT NULL
);

DROP TABLE IF EXISTS topicAuxiliars CASCADE;
CREATE TABLE topicAuxiliars (
    id SERIAL PRIMARY KEY,
    topic BIGINT UNSIGNED NOT NULL REFERENCES topics(id) ON DELETE CASCADE,
    auxiliar BIGINT UNSIGNED NOT NULL REFERENCES professors(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS professorUnavailable CASCADE;
CREATE TABLE professorUnavailable (
    id SERIAL PRIMARY KEY,
    creator BIGINT UNSIGNED NOT NULL REFERENCES users(id) ON DELETE CASCADE,
	professor BIGINT UNSIGNED NOT NULL REFERENCES professors(id) ON DELETE CASCADE,
	day DATE NOT NULL,
	time INTEGER NOT NULL,
	UNIQUE(professor, day, time),
	FOREIGN KEY (creator) REFERENCES users(id) ON DELETE CASCADE,
	FOREIGN KEY (professor) REFERENCES professors(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS roomPeriodUnavailable CASCADE;
CREATE TABLE roomPeriodUnavailable (
    id SERIAL PRIMARY KEY,
    creator BIGINT UNSIGNED NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    room BIGINT UNSIGNED NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
    day DATE NOT NULL,
    time INT NOT NULL
);

SET FOREIGN_KEY_CHECKS=1;

-- INSERT INTO users (email) VALUES ('up201304143@fe.up.pt');
-- INSERT INTO requests (creator, startingDate) VALUES (1, '2014-11-22');
-- INSERT INTO professors (creator, name, acronym, cod) VALUES (1, 'António Augusto de Sousa', 'AAS', 209500);
-- INSERT INTO topics (creator, name, acronym, code, year, difficulty) VALUES (1, 'Computação Gráfica', 'CGRA', 'EIC0019', 2, 1);
-- INSERT INTO topicRegent (topic, regent) VALUES (1, 1);
-- INSERT INTO exams (creator, topic, normal, pc) VALUES (1, 1, TRUE, FALSE);
-- INSERT INTO exams (creator, topic, normal, pc) VALUES (1, 1, FALSE, FALSE);
-- INSERT INTO rooms (creator, cod, capacity, pc) VALUES (1, 'B116', 69, FALSE);
-- INSERT INTO rooms (creator, cod, capacity, pc) VALUES (1, 'B120', 69, FALSE);
-- INSERT INTO rooms (creator, cod, capacity, pc) VALUES (1, 'B232C', 44, FALSE);
-- INSERT INTO students (creator, name, cod) VALUES (1, 'Test student', '201301234');
-- INSERT INTO studentTopic (student, topic) VALUES (1, 1);

SELECT * FROM requests;