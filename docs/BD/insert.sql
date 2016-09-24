INSERT INTO users (email) VALUES ("bar@foo.com");

INSERT INTO calendars (creator, startingDate) VALUES (1, '2016-08-08');

INSERT INTO professors (calendar, acronym) VALUES (1, 'FOO');

INSERT INTO topics (calendar, name, code, year) VALUES (1, 'IART', '123', 3);
INSERT INTO topics (calendar, name, code, year) VALUES (1, 'COMP', '321', 3);

INSERT INTO topicProfessor (topic, professor) VALUES (1, 1);
INSERT INTO topicProfessor (topic, professor) VALUES (2, 1);

INSERT INTO exams (topic, normal, day, time) VALUES (1, TRUE, '2016-08-10', 1);
INSERT INTO exams (topic, normal, day, time) VALUES (1, FALSE, '2016-08-17', 2);
INSERT INTO exams (topic, normal, day, time) VALUES (2, TRUE, '2016-08-11', 3);
INSERT INTO exams (topic, normal, day, time) VALUES (2, FALSE, '2016-08-18', 1);

INSERT INTO rooms (calendar, cod, capacity, pc) VALUES (1, 'B314', 20, TRUE);
INSERT INTO rooms (calendar, cod, capacity, pc) VALUES (1, 'B315', 20, TRUE);
INSERT INTO rooms (calendar, cod, capacity, pc) VALUES (1, 'B316', 20, FALSE);
INSERT INTO rooms (calendar, cod, capacity, pc) VALUES (1, 'B317', 20, FALSE);

INSERT INTO examRooms (exam, room) VALUES (1, 1);
INSERT INTO examRooms (exam, room) VALUES (2, 2);
INSERT INTO examRooms (exam, room) VALUES (3, 3);
INSERT INTO examRooms (exam, room) VALUES (4, 4);
