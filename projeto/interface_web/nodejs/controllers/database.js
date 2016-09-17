//TODO database integration

var mysql = require('mysql');
var connection = mysql.createConnection({
		host : 'localhost',
		port : 3306,
		user : 'root',
		password : '',
		database : 'examcalendar'
});
connection.connect();

module.exports = {
  connection,
  //login
  getUser: function (email) {
    //return user's id
    // -1 = error, positive otherwise
    return -1;
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
  getCalendar: function (userID) {
    return [];
  },
  importCalendar: function (userID, calendar) {
    return false;
  },
  generateCalendar: function (userID) {
    return false;
  },
  //importDB
  setTimespan: function (calendarID, startingDate, normalSeasonDuration, appealSeasonDuration) {
	connection.query('UPDATE calendars SET startingDate = ?, normalSeasonDuration = ?, appealSeasonDuration = ? WHERE id = ?',
	[startingDate, normalSeasonDuration, appealSeasonDuration, calendarID],
	function(err, rows, fields) {
		if (err) throw err;
	});
	return true;
  },
  getTopics: function (calendarID, callback) {
	connection.query('SELECT * FROM topics WHERE calendar = ?',
	[calendarID],
	callback);
	return true;
  },
  setTopics: function (calendarID, topics) {
	for (var i = 0; i < topics.length; i++) {
		console.log(topics[i]);
		connection.query('UPDATE topics SET difficulty = ? WHERE id = ?',
		[topics[i].difficulty, topics[i].id],
		function(err, rows, fields) {
			if (err) throw err;
		});
		createOrDeleteExam(topics[i].normal, topics[i].id, 'normal', topics[i].normal_pc ? 1 : 0);
		createOrDeleteExam(topics[i].appeal, topics[i].id, 'appeal', topics[i].appeal_pc ? 1 : 0);
	}
	return true;
  }
};

var createOrDeleteExam = function(create, topic, seasonName, pc) {
	if (create) {
		connection.query('INSERT INTO exams (topic, normal, pc) VALUES (?, ?, ?)',
		[topic, seasonName == 'normal', pc],
		function(err, rows, fields) {
			if (err) throw err;
		});
	} else {
		connection.query('DELETE FROM exams WHERE topic = ? AND normal = ?',
		[topic, seasonName == 'normal' ? 1 : 0],
		function(err, rows, fields) {
			if (err) throw err;
		});
	}
}