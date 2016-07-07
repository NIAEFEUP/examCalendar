var database = require('./database');
var shared = require('./shared');

module.exports = {
  //GET
  get: function (res, userID) {
    var notes = database.getNotes(userID);
    res.end(shared.arrayToJSON(notes));
  },
  //PUT
  add: function (res, userID, msg) {
    database.addNote(userID, msg) ? shared.success(res) : shared.error(res);
  },
  //POST
  update: function (res, userID, noteID, msg) {
    database.updateNote(userID, noteID, msg) ? shared.success(res) : shared.error(res);
  },
  //DELETE
  remove: function (res, userID, noteID) {
    database.removeNote(userID, noteID) ? shared.success(res) : shared.error(res);
  }
};
