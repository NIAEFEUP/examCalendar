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
  //database
  import: function (userID, file0, file1, file2) {
    //TODO connect to the API that is being made by Lenhador/CC

    //Error in the files 0 and 2
    return '{"error":"0,2"}';
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
  }
};
