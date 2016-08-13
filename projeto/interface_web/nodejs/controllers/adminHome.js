//see logs (limit and pagination)
// GET

var database = require('./database');
var shared = require('./shared');

module.exports = {
  getLogs: function (res, userID, limit, page) {
    var logs = database.getLogs(userID, limit, page);
    res.json(shared.arrayToJSON(logs));
  }
};
