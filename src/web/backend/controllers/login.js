//authenticate on sigarra (https://sigarra.up.pt/feup/pt/mob_val_geral.autentica?pv_login={{user}}&pv_password={{pass}})
//search in the database
//
// POST

var database = require('./database');
var async = require('async');
var https = require('https');

var options = {
    host: 'sigarra.up.pt',
    path: '/feup/pt/mob_val_geral.autentica'
};

module.exports = {
    authenticate: function (req, res) {
        validate(req, res);
    }
};

var validate = function (req, res) {
    var email = req.body.email;
    var password = req.body.password;
    options.path = '/feup/pt/mob_val_geral.autentica' + '?pv_login=' + email + '&pv_password=' + password;

    //add the calls to be made asynchronously
    var calls = [function (callback) {
        var req1 = https.get(options, function (res1) {
            res1.on('data', function (d) {
                var data = JSON.parse(d);
                callback(null, data);
            });
        });

        req1.end();
    }];

    //when all the calls are finished, this function is called
    async.parallel(calls, function (err, result) {
        //return JSON response
        var user = result[0];
        if (user.authenticated) {
            registered(req, res);
        } else {
            console.log("not registered");
            res.status(401);
            res.send();
        }
    });
};

var registered = function (req, res) {
    var email = req.body.email;

    var calls = [function (callback) {
        database.connection.query('SELECT id FROM users WHERE email LIKE ?', [email + "@fe.up.pt"], function (err, rows, fields) {
            if (!err)
                callback(null, rows[0]);
        });
    }];

    //when all the calls are finished, this function is called
    async.parallel(calls, function (err, result) {

        if (result[0] != null && result[0].id >= 0) {
            console.log("auth");
            req.session.userID = result[0].id;
            res.status(200);
        } else {
            console.log("not auth");
            res.status(401);
        }
        res.send();
    });
};
