//authenticate on sigarra (https://sigarra.up.pt/feup/pt/mob_val_geral.autentica?pv_login={{user}}&pv_password={{pass}})
//search in the database
//
// POST

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
  options.path += '?pv_login=' + email + '&pv_password=' + password;

  //add the calls to be made asynchronously
  var calls = [function(callback) {
    var req = https.get(options, function(res) {
      res.on('data', function(d) {
        var data = JSON.parse(d);
        callback(null, data);
      });
    });

    req.end();
  }];

  //when all the calls are done, this function is called
  async.parallel(calls, function(err, result) {
    //return JSON response
    var user = result[0];
    if (user.authenticated) {
      console.log(user);
      registrated(req, res);
    } else {
      res.end("{\"authenticated\":false, \"msg\":user.erro_msg}\"");
    }
  });
};

var registrated = function (req, res) {
  var email = req.body.email;

  //TODO add session email if successful
	/*if (response.authenticated) {
		sess=req.session;
		sess.email=req.body.email;
	}*/

  //TODO missing database connection
  res.end("{\"authenticated\":true, \"msg\":\"TODO missing database connection\"}");
};
