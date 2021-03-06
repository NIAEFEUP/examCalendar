//TODO database integration

var mysql = require('mysql');
var async = require('async');
var nconf = require('nconf');
var config = require('../config');

var connection = mysql.createConnection({
    host: nconf.get("database:host"),
    port: nconf.get("database:port"),
    user: config.db.user,
    password: config.db.password,
    database: config.db.database
});
connection.connect();

module.exports = {
    connection: connection,
    //users
    getUser: function (id, callback) {
        connection.query("SELECT email FROM users WHERE id = ?", [id], function (err, rows, fields) {
            if (!err) {
                if (rows.length == 0) {
                    callback(null);
                }
                else {
                    callback(rows[0].email);
                }
            }
        });
    },
    getIDByUserID: function (userID, callback) {
        connection.query("SELECT id FROM calendars WHERE creator = ?", [userID], function (err, rows, fields) {
            if (!err) {
                if (rows.length == 0) {
                    callback(null);
                } else {
                    callback(rows[0].id);
                }
            }
        });
    },
    //notes
    getNotes: function (userID) {
        return [];
    },
    addNote: function (userID, msg) {
        return false;
    },
    updateNote: function (userID, noteID, msg) {
        return false;
    },
    removeNote: function (userID, noteID) {
        return false;
    },
    //adminHome
    getLogs: function (userID, limit, page) {
        return [];
    },
    //constraints
    getConstraints: function (userID, msg) {
        return [];
    },
    addConstraint: function (userID, constraint) {
        return false;
    },
    updateConstraint: function (userID, constraintID, constraint) {
        return false;
    },
    removeConstraint: function (userID, constraintID) {
        return false;
    },
    //adminUsers
    getUsers: function (userID) {
        return [];
    },
    addUser: function (userID, email) {
        return false;
    },
    removeUser: function (userID, email) {
        return false;
    },
    //calendar
    getCalendar: function (userID, callback) {
        console.log('querying');
        connection.query('SELECT id FROM calendars WHERE creator = ?',
            [userID],
            function (err, rows, fields) {
                if (err) {
                    throw err;
                }
                callback(rows[0].id);
            }
        );
        console.log('query done');
    },
    importCalendar: function (userID, calendar) {
        return false;
    },
    generateCalendar: function (userID) {
        return false;
    },
    setExamPeriod: function (examID, day, time, callback) {
        connection.query('UPDATE exams SET day = ?, time = ? WHERE id = ?',
            [day, time, examID],
            callback);
        return true;
    },
    getExam: function (userID, examID, callback) {
        // Fetch single-row data
        connection.query('SELECT topics.name, exams.id, exams.normal, topics.year, exams.day, exams.time, (SELECT count(studenttopic.topic) FROM studenttopic WHERE studenttopic.topic = topics.id) AS students'
            + ' FROM exams'
            + ' INNER JOIN topics ON exams.topic = topics.id'
            + ' INNER JOIN calendars ON topics.calendar = calendars.id'
            + ' WHERE exams.id = ? AND calendars.creator = ?',
            [examID, userID],
            function (err, rows, fields) {
                if (err) {
                    callback(err);
                    return;
                }
                if (rows.length == 0) {
                    callback("Exam not found.");
                    return;
                }

                var calls = [function (callback) {
                    connection.query('SELECT professors.name'
                        + ' FROM professors'
                        + ' INNER JOIN topicprofessor ON topicprofessor.professor = professors.id'
                        + ' INNER JOIN exams ON exams.topic = topicprofessor.topic'
                        + ' WHERE exams.id = ?',
                        [examID],
                        function (err, rows, fields) {
                            if (err)
                                console.error(err);
                            else
                                callback(null, rows);
                        });
                }, function (callback) {
                    connection.query('SELECT rooms.id, rooms.cod, rooms.capacity, rooms.pc, rooms.id IN (SELECT examrooms.room FROM examrooms WHERE examrooms.exam = ?) AS checked'
                        + ' FROM rooms'
                        + ' INNER JOIN topics ON rooms.calendar = topics.calendar'
                        + ' INNER JOIN exams ON exams.topic = topics.id'
                        + ' WHERE exams.id = ?',
                        [examID, examID],
                        function (err, rows, fields) {
                            if (err)
                                console.error(err);
                            else
                                callback(null, rows);
                        });
                }];
                async.parallel(calls, function (err, result) {
                    rows[0].professors = result[0];
                    rows[0].classrooms = result[1];
                    callback(null, rows[0]);
                });
            }
        );
    },
    //importDB
    deleteCalendar: function (userID) {
        connection.query('DELETE FROM calendars WHERE creator = ?',
            [userID],
            function (err, rows, fields) {
                if (err) throw err;
            });
        return true;
    },

    //TODO MUDAR este método PARA CREATE CALENDAR MUDAR QUERY PARA CREATE
    createCalendar: function (userID, startingDate, normalSeasonDuration, appealSeasonDuration) {
        connection.query('INSERT INTO calendars (startingDate, normalSeasonDuration, appealSeasonDuration, creator) VALUES (?, ?, ?, ?)',
            [startingDate, normalSeasonDuration, appealSeasonDuration, userID],
            function (err, rows, fields) {
                if (err) throw err;
            });
        return true;
    },
    getTopics: function (userID, callback) {
        connection.query('SELECT topics.* FROM topics INNER JOIN calendars ON calendars.id = topics.calendar WHERE calendars.creator = ? ORDER BY year, name',
            [userID],
            callback);
        return true;
    },
    setTopics: function (userID, topics) {
        for (var i = 0; i < topics.length; i++) {
            console.log(topics[i]);
            connection.query('UPDATE topics INNER JOIN calendars ON topics.calendar = calendars.id SET topics.difficulty = ? WHERE topics.id = ? AND calendars.creator = ?',
                [topics[i].difficulty, topics[i].id, userID],
                function (err, rows, fields) {
                    if (err) throw err;
                });
            createOrDeleteExam(topics[i].normal, topics[i].id, 'normal', topics[i].normal_pc ? 1 : 0);
            createOrDeleteExam(topics[i].appeal, topics[i].id, 'appeal', topics[i].appeal_pc ? 1 : 0);
        }
        return true;
    },
    addExamRoom: function (userID, examID, roomID, callback) {
        // TODO check if exam and room belong to the same calendar
        connection.query('INSERT INTO examrooms (exam, room) VALUES (?, ?)',
            [examID, roomID],
            callback);
    },
    removeExamRoom: function (userID, examID, roomID, callback) {
        // TODO check if exam and room belong to the same calendar
        connection.query('DELETE FROM examrooms WHERE exam = ? AND room = ?',
            [examID, roomID],
            callback);
    }
};

var createOrDeleteExam = function (create, topic, seasonName, pc) {
    //console.log("Deleting " + seasonName + " exam for topic #" + topic);
    connection.query('DELETE FROM exams WHERE topic = ? AND normal = ?',
        [topic, seasonName == 'normal' ? 1 : 0],
        function (err, rows, fields) {
            if (err) throw err;
        });
    if (create) {
        //console.log("Creating " + seasonName + " exam for topic #" + topic);
        connection.query('INSERT INTO exams (topic, normal, pc) VALUES (?, ?, ?)',
            [topic, seasonName == 'normal', pc],
            function (err, rows, fields) {
                if (err) throw err;
            });
    }
}
