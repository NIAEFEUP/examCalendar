//remove/list
//add has different options
// GET: list
// PUT: add
// POST: update
// DELETE: remove

var database = require('./database');
var shared = require('./shared');

module.exports = {
  get: function (res, userID) {
    var constraints = database.getConstraints(userID, msg);
    res.end(shared.arrayToJSON(constraints));
  },
  add: function (res, userID, constraint) {
    //TODO constraint must be looked into
    database.addConstraint(userID, constraint) ? shared.success(res) : shared.error(res);
  },
  update: function (res, userID, constraintID, constraint) {
    //TODO constraint must be looked into
    database.updateConstraint(userID, constraintID, constraint) ? shared.success(res) : shared.error(res);
  },
  remove: function (res, userID, constraintID) {
    database.removeConstraint(userID, constraintID) ? shared.success(res) : shared.error(res);
  }
};
