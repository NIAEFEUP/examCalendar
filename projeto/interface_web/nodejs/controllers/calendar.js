//import/export/list
//generate calendar with different options
// TODO mudar a página (em AngularJS) de modo a não permitir opções
// GET: list/export (JSON)
// PUT: import
// POST: generate/validate

var database = require('./database');
var shared = require('./shared');

module.exports = {
  get: function (res, userID) {
    res.end(JSON.stringfy({"calendar":database.getCalendar(userID)}));
  },
  import: function (res, userID, calendar) {
    //TODO calendar must be looked into
    database.importCalendar(userID, calendar) ? shared.success(res) : shared.error(res);
  },
  generate: function (res, userID) {
    database.generateCalendar(userID) ? shared.success(res) : shared.error(res);
  }
};
