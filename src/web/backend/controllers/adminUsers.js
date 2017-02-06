//add/remove/list users
// GET: list
// PUT: add
// DELETE: remove

var database = require('./database');
var shared = require('./shared');

module.exports = {
    get: function (res, userID) {
        var users = database.getUsers(userID);
        res.json(users);
    },
    add: function (res, userID, email) {
        database.addUser(userID, email) ? shared.success(res) : shared.error(res);
    },
    remove: function (res, userID, email) {
        database.removeUser(userID, email) ? shared.success(res) : shared.error(res);
    }
};
