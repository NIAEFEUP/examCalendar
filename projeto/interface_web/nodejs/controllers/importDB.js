//add students/teachers/rooms (all of them are .xlxs or .xls)
// POST: add

var database = require('./database');
var http = require('http');

module.exports = {
  import: function (res, userID, req ) {
		var options = {
			hostname: 'localhost',
			port: 8081,
			path: '/parser',
			method: 'POST',
			headers: req.headers
		};
		
		var req2 = http.request(options, function(res2){
			res2.pipe(res, {
			  end: true
			});
		});
		req.pipe(req2);
  }
};
