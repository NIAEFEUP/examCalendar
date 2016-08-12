//dependencies required
var express =	require('express');
var session	=	require('express-session');
var bodyParser = require('body-parser');
var app = express();

app.engine('html', require('ejs').renderFile);

app.use(session({secret: 'secret_key', saveUninitialized: true, resave: true}));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

//controllers required
var login = require('./controllers/login');
var adminHome = require('./controllers/adminHome');
var adminUsers = require('./controllers/adminUsers');
var calendar = require('./controllers/calendar');
var constraints = require('./controllers/constraints');
var importDB = require('./controllers/importDB');

// This is required due to security issues. The response has specific details
// such as the response got to be from the same domain.
// This function tells the browser that it's secure.
function allowRedirectAnswer(res) {
	res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
	return res;
}

//////////////////////////////////////////////////////////////////
//                            Login                             //
//////////////////////////////////////////////////////////////////
//Should be a JSON request like {"email":{{email}}, "password":{{password}}}
//The answer is also in JSON like {"authenticated":false, "msg":user.erro_msg}
app.post('/login',function(req,res){
	res = allowRedirectAnswer(res);
	login.authenticate(req, res);
});

//////////////////////////////////////////////////////////////////
//                            Logout                            //
//////////////////////////////////////////////////////////////////
app.get('/logout',function(req,res){
	res = allowRedirectAnswer(res);
	req.session.destroy(function(err){
		if(err){
			console.err(err);
		}	else {
			res.end('{"authenticated":false}');
		}
	});
});

//////////////////////////////////////////////////////////////////
//                          Database                            //
//////////////////////////////////////////////////////////////////
app.post('/database',function(req,res){
	res = allowRedirectAnswer(res);
	var id = req.session.userID;
	var file0 = req.body.file0;
	var file1 = req.body.file1;
	var file2 = req.body.file2;
	importDB.import(res, id, file0, file1, file2);
});

//////////////////////////////////////////////////////////////////
//                         Admin Home                           //
//////////////////////////////////////////////////////////////////
app.get('/adminHome',function(req,res){
	res = allowRedirectAnswer(res);
	var id = req.session.userID;
	var limit = req.body.limit;
	var page = req.body.page;
	importDB.getLogs(res, id, limit, page);
});

//////////////////////////////////////////////////////////////////
//                         Constraints                          //
//////////////////////////////////////////////////////////////////
app.get('/constraints',function(req,res){
	res = allowRedirectAnswer(res);
	constraints.get(res, req.session.userID);
});

app.put('/constraints',function(req,res){
	res = allowRedirectAnswer(res);
	constraints.add(res, req.session.userID, req.body.constraint);
});

app.post('/constraints',function(req,res){
	res = allowRedirectAnswer(res);
	constraints.update(res, req.session.userID, req.body.constraintID, req.body.constraint);
});

app.delete('/constraints',function(req,res){
	res = allowRedirectAnswer(res);
	constraints.remove(res, req.session.userID, req.body.constraintID);
});

//////////////////////////////////////////////////////////////////
//                        Admin Users                           //
//////////////////////////////////////////////////////////////////
app.get('/adminUsers',function(req,res){
 res = allowRedirectAnswer(res);
 adminUsers.get(res, req.session.userID);
});

app.put('/adminUsers',function(req,res){
 res = allowRedirectAnswer(res);
 adminUsers.add(res, req.session.userID, req.body.email);
});

app.delete('/adminUsers',function(req,res){
 res = allowRedirectAnswer(res);
 adminUsers.remove(res, req.session.userID, req.body.email);
});

//////////////////////////////////////////////////////////////////
//                          Calendar                            //
//////////////////////////////////////////////////////////////////
app.get('/calendar',function(req,res){
 res = allowRedirectAnswer(res);
 calendar.get(res, req.session.userID);
});

app.post('/calendar',function(req,res){
 res = allowRedirectAnswer(res);
 calendar.generate(res, req.session.userID);
});

//////////////////////////////////////////////////////////////////
//                         Connection                           //
//////////////////////////////////////////////////////////////////
var port = 8080;
app.listen(port,function(){
	console.log("Server running on port " + port);
});
