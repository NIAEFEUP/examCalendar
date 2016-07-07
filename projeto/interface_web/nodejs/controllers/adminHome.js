//see logs (limit and pagination)
// GET

var database = require('./database');

module.exports = {
  getLogs: function (res, userID, limit, page) {
    var logs = database.getLogs(userID, limit, page);
    var response = {};
    logs.forEach(function(element, index, array) {
      response[index] = element;
    });
    res.end(response);
  }
};
