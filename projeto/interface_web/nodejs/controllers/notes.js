var database = require('./database');

module.exports = {
  //GET
  get: function (req, res) {
    var id = req.session.userID;
    var notes = database.getNotes(id);
    var response = {};
    notes.forEach(function(element, index, array) {
      response[index] = element;
    });
    res.end(response);
  },
  //PUT
  add: function (req, res, msg) {
    var id = req.session.userID;
    database.addNote(id, msg) ? success(res) : error(res);
  },
  //POST
  update: function (req, res, noteID, msg) {
    var id = req.session.userID;
    database.updateNote(id, noteID, msg) ? success(res) : error(res);
  },
  //DELETE
  remove: function (req, res, noteID) {
    var id = req.session.userID;
    database.removeNote(id, noteID) ? success(res) : error(res);
  }
};

var success = function(res) {
  res.end('{"msg":"Success"}');
};

var error = function(res) {
  res.end('{"msg":"Error"}');
};
