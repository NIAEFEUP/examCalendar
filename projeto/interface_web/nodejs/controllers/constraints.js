//remove/list
//add has different options
// GET: list
// PUT: add
// POST: update
// DELETE: remove

var database = require('./database');

module.exports = {
  get: function (res, userID) {
    var constraints = database.getConstraints(userID, msg);
    var response = {};
    constraints.forEach(function(element, index, array) {
      response[index] = element;
    });
    res.end(response);
  },
  add: function (res, userID, constraint) {
    //TODO constraint must be looked into
    database.addConstraint(userID, constraint) ? success(res) : error(res);
  },
  update: function (res, userID, constraintID, constraint) {
    //TODO constraint must be looked into
    database.updateConstraint(userID, constraintID, constraint) ? success(res) : error(res);
  },
  remove: function (res, userID, constraintID) {
    database.removeConstraint(userID, constraintID) ? success(res) : error(res);
  }
};

function success(res) {
  res.end('{"msg":"Success"}');
};

function error(res) {
  res.end('{"msg":"Error"}');
};
