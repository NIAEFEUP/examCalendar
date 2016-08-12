//import/export/list
//generate calendar with different options
// TODO mudar a página (em AngularJS) de modo a não permitir opções
// GET: list/export (JSON)
// PUT: import
// POST: generate/validate

var database = require('./database');
var shared = require('./shared');
var async = require('async');

module.exports = {
  get: function (res, userID) {
    //add the calls to be made asynchronously
    var calls = [function(callback) {
      database.connection.query('SELECT UNIX_TIMESTAMP(startingDate) AS startingDate, normalSeasonDuration, appealSeasonDuration from calendars where id = 1', function(err, rows, fields) {
        if (!err)
          callback(null, rows[0]);
      });
    },
    function(callback) {
      database.connection.query('SELECT exams.id, year, name, UNIX_TIMESTAMP(day) AS day, time FROM exams, topics where topic = topics.id and exams.calendar = 1 order by day asc', function(err, rows, fields) {
        if (!err)
          callback(null, rows);
      });
    },
    function(callback) {
      database.connection.query('SELECT id, cod FROM rooms where pc = false', function(err, rows, fields) {
        if (!err)
          callback(null, rows);
      });
    },
    function(callback) {
      database.connection.query('SELECT id, cod FROM rooms where pc = true', function(err, rows, fields) {
        if (!err)
          callback(null, rows);
      });
    }];

    //when all the calls are finished, this function is called
    async.parallel(calls, function(err, result) {

      var json = {};
      json.normalSeasonDays = result[0].normalSeasonDuration;
      json.weeks = [];
      json.unassigneds = [];
      json.rooms = {'no_pc':[], 'pc':[]};

      var startDate = new Date(result[0].startingDate * 1000);

      //process dates
      for (var i = 0; i < (result[0].normalSeasonDuration + result[0].appealSeasonDuration) / 7; i++) {
        var dates = ["July 1", "July 2", "July 3", "July 4", "July 5",  "July 6"];
        var dates = generateWeekDays(startDate, i);
        var periods = generateWeekPeriods();
        json.weeks.push({'dates' : dates, 'periods' : periods});
      }

      //process exams
      for (var i = 0; i < result[1].length; i++) {
        var examDate = new Date(result[1][i].day * 1000);
        var week = DateDiff.inWeeks(startDate, examDate);
        var period = ['mornings', 'afternoons', 'evenings'][result[1][i].time-1];
        json.weeks[week]['periods'][period][DateDiff.inDays(startDate, examDate) - (week * 7)].push({
          'id' : result[1][i].id,
          'name' : result[1][i].name,
          'year' : result[1][i].year
        });
      }

      //process rooms
      for (var i = 0; i < result[2].length; i++) {
        json.rooms['no_pc'].push({'id' : result[2][i].id, 'name' : result[2][i].cod});
      }
      for (var i = 0; i < result[3].length; i++) {
        json.rooms['pc'].push({'id' : result[3][i].id, 'name' : result[3][i].cod});
      }

      /*console.log(startDate.getDay()+7);
      console.log(startDate.getMonth()+1);
      console.log(startDate.getYear()+1900);*/
      res.json(json);//database.getCalendar(userID));
    });
  },
  import: function (res, userID, calendar) {
    //TODO calendar must be looked into
    database.importCalendar(userID, calendar) ? shared.success(res) : shared.error(res);
  },
  generate: function (res, userID) {
    database.generateCalendar(userID) ? shared.success(res) : shared.error(res);
  }
};

function generateWeekDays(startDate, week) {
  var dates = []; var date = new Date();
  for (var i = 0; i < ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'].length; i++) {
    date.setDate(startDate.getDate()+week*7+i);
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

    inDays: function(d1, d2) {
        var t2 = d2.getTime();
        var t1 = d1.getTime();

        return parseInt((t2-t1)/(24*3600*1000));
    },

    inWeeks: function(d1, d2) {
        var t2 = d2.getTime();
        var t1 = d1.getTime();

        return parseInt((t2-t1)/(24*3600*1000*7));
    },

    inMonths: function(d1, d2) {
        var d1Y = d1.getFullYear();
        var d2Y = d2.getFullYear();
        var d1M = d1.getMonth();
        var d2M = d2.getMonth();

        return (d2M+12*d2Y)-(d1M+12*d1Y);
    },

    inYears: function(d1, d2) {
        return d2.getFullYear()-d1.getFullYear();
    }
}
