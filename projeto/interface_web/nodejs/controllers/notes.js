var database = require('./database');

module.exports = {
  //GET
  get: function (res, userID) {
    var notes = database.getNotes(userID);
    var response = {};
    notes.forEach(function(element, index, array) {
      response[index] = element;
    });
    res.end(response);
  },
  //PUT
  add: function (res, userID, msg) {
    database.addNote(userID, msg) ? success(res) : error(res);
  },
  //POST
  update: function (res, userID, noteID, msg) {
    database.updateNote(userID, noteID, msg) ? success(res) : error(res);
  },
  //DELETE
  remove: function (res, userID, noteID) {
    database.removeNote(userID, noteID) ? success(res) : error(res);
  }
};

function success(res) {
  res.end('{"msg":"Success"}');
};

function error(res) {
  res.end('{"msg":"Error"}');
};
