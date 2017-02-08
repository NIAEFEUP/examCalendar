//import/export/list
//generate calendar with different options
// TODO mudar a página (em AngularJS) de modo a não permitir opções
// GET: list/export (JSON)
// POST: generate/validate

var database = require('./database');
var shared = require('./shared');
var async = require('async');

module.exports = {
    get: function (res, userID) {
        database.getIDByUserID(userID, function (calendarId) {
            if (calendarId == null) {
                res.end();
                return;
            }
            //add the calls to be made asynchronously
            var calls = [function (callback) {
                database.connection.query('SELECT UNIX_TIMESTAMP(startingDate) AS startingDate, normalSeasonDuration, appealSeasonDuration from calendars where id = ?', [calendarId], function (err, rows, fields) {
                    if (err)
                        console.error(err);
                    else
                        callback(null, rows[0]);
                });
            },
                function (callback) {
                    //assigned
                    database.connection.query('SELECT exams.id, year, name, (CASE day WHEN NULL THEN NULL ELSE UNIX_TIMESTAMP(day) END) AS day, time FROM exams, topics where topic = topics.id and topics.calendar = ? order by day asc', [calendarId], function (err, rows, fields) {
                        if (err)
                            console.error(err);
                        else
                            callback(null, rows);
                    });
                },
                function (callback) {
                    database.connection.query('SELECT id, cod, pc FROM rooms where calendar = ?', [calendarId], function (err, rows, fields) {
                        if (err)
                            console.error(err);
                        else
                            callback(null, rows);
                    });
                }];

            //when all the calls are finished, this function is called
            async.parallel(calls, function (err, result) {

                var json = {};
                json.normalSeasonDays = result[0].normalSeasonDuration;
                json.weeks = [];
                json.unassigneds = [];
                json.rooms = {'no_pc': [], 'pc': []};

                var startDate = new Date(result[0].startingDate * 1000);

                //process dates
                for (var i = 0; i < (result[0].normalSeasonDuration + result[0].appealSeasonDuration) / 7; i++) {
                    var dates = generateWeekDays(startDate, i);
                    var periods = generateWeekPeriods();
                    json.weeks.push({'dates': dates, 'periods': periods});
                }

                //process exams assigned
                for (var i = 0; i < result[1].length; i++) {
                    var day = result[1][i].day;
                    if (day == null || day == 0) {
                        json.unassigneds.push({
                            'id': result[1][i].id,
                            'name': result[1][i].name,
                            'year': result[1][i].year
                        });
                    } else {
                        var examDate = new Date(result[1][i].day * 1000);
                        var week = DateDiff.inWeeks(startDate, examDate);
                        var period = ['mornings', 'afternoons', 'evenings'][result[1][i].time];
                        //console.log(week, json.weeks[week]);
                        json.weeks[week]['periods'][period][DateDiff.inDays(startDate, examDate) - (week * 7)].push({
                            'id': result[1][i].id,
                            'name': result[1][i].name,
                            'year': result[1][i].year
                        });
                    }
                }

                //process rooms
                for (var i = 0; i < result[2].length; i++) {
                    json.rooms[result[2][i].pc ? 'pc' : 'no_pc'].push({
                        'id': result[2][i].id,
                        'name': result[2][i].cod
                    });
                }

                /*console.log(startDate.getDay()+7);
                 console.log(startDate.getMonth()+1);
                 console.log(startDate.getYear()+1900);*/
                res.json(json);//database.getCalendar(userID));
            });
        });
    },
    generate: function (res, userID) {
        database.generateCalendar(userID) ? shared.success(res) : shared.error(res);
    },
    moveExam: function (res, userID, body) {
        // TODO check if the user is the creator of the exam
        // TODO validate day and time values
        database.setExamPeriod(body.id, body.day, body.time, function (err) {
            if (err)
                console.error(err);
            res.end();
        });
    },
    getExam: function (res, userID, examID) {
        database.getExam(userID, examID, function (err, result) {
            if (err) {
                res.send(404, err);
            } else {
                res.json(result);
            }
        });
    },
    updateExamRoom: function (res, userID, examID, roomID, checked) {
        var callback = function (err, result) {
            if (err) {
                res.send(404, err);
            } else {
                res.json(result);
            }
        };
        if (checked)
            database.addExamRoom(userID, examID, roomID, callback);
        else
            database.removeExamRoom(userID, examID, roomID, callback);
    }
};

function generateWeekDays(startDate, week) {
    var dates = [];
    for (var i = 0; i < ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'].length; i++) {
        var date = new Date(startDate);
        date.setDate(startDate.getDate() + week * 7 + i);
        dates.push(date.toDateString());
    }
    return dates;
}

function generateWeekPeriods() {
    return {
        "mornings": [
            [],
            [],
            [],
            [],
            [],
            []
        ],
        "afternoons": [
            [],
            [],
            [],
            [],
            [],
            []
        ],
        "evenings": [
            [],
            [],
            [],
            [],
            [],
            []
        ]
    };
}

var DateDiff = {

    inDays: function (d1, d2) {
        var t2 = d2.getTime();
        var t1 = d1.getTime();

        return parseInt((t2 - t1) / (24 * 3600 * 1000));
    },

    inWeeks: function (d1, d2) {
        var t2 = d2.getTime();
        var t1 = d1.getTime();

        return parseInt((t2 - t1) / (24 * 3600 * 1000 * 7));
    },

    inMonths: function (d1, d2) {
        var d1Y = d1.getFullYear();
        var d2Y = d2.getFullYear();
        var d1M = d1.getMonth();
        var d2M = d2.getMonth();

        return (d2M + 12 * d2Y) - (d1M + 12 * d1Y);
    },

    inYears: function (d1, d2) {
        return d2.getFullYear() - d1.getFullYear();
    }
}
