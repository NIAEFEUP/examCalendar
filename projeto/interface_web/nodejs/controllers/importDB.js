//add students/teachers/rooms (all of them are .xlxs or .xls)
// POST: add

var database = require('./database');

module.exports = {
  import: function (res, userID, file0, file1, file2 ) {
    res.end(database.import(userID, file0, file1, file2));
  }
};
