//add/remove/list users
// GET: list
// POST: add
// DELETE: remove

var database = require('./database');

module.exports = {
  get: function (res, userID) {
    var users = database.getUsers(userID);
    var response = {};
    users.forEach(function(element, index, array) {
      response[index] = element;
    });
    res.end(response);
  },
  add: function (res, userID, email) {
    database.addUser(userID, email) ? success(res) : error(res);
  },
  remove: function (res, userID, email) {
    database.removeUser(userID, email) ? success(res) : error(res);
  }
};

function success(res) {
  res.end('{"msg":"Success"}');
};

function error(res) {
  res.end('{"msg":"Error"}');
};
