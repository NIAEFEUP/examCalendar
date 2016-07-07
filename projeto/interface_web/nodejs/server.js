//dependencies required
var express =	require('express');
var session	=	require('express-session');
var bodyParser = require('body-parser');
var app = express();

/* Small test with database integration
var mysql = require('mysql');
var connection = mysql.createConnection({
		host : 'localhost',
		port : 3000,
		user : 'root',
		password : 'toor'
});
connection.connect(function(err) {
		console.err('ERROR : ' + err);
		exit(-1);
});
*/

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
var notes = require('./controllers/notes');

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
//                            Notes                             //
//////////////////////////////////////////////////////////////////
app.get('/notes',function(req,res){
	res = allowRedirectAnswer(res);
	notes.get(req, res);
});

app.put('/notes',function(req,res){
	res = allowRedirectAnswer(res);
	notes.add(req, res, req.body.note);
});

app.post('/notes',function(req,res){
	res = allowRedirectAnswer(res);
	notes.update(req, res, req.body.noteID, req.body.msg);
});

app.delete('/notes',function(req,res){
	res = allowRedirectAnswer(res);
	notes.remove(req, res, req.body.noteID);
});

//////////////////////////////////////////////////////////////////
//                          Database                            //
//////////////////////////////////////////////////////////////////
app.post('/database',function(req,res){
	res = allowRedirectAnswer(res);
	var id = req.session.userID;
	var file0 = req.session.file0;
	var file1 = req.session.file1;
	var file2 = req.session.file2;
	importDB.import(res, id, file0, file1, file2);
});

//////////////////////////////////////////////////////////////////
//                         Admin Home                           //
//////////////////////////////////////////////////////////////////
app.get('/adminHome',function(req,res){
	res = allowRedirectAnswer(res);
	var id = req.session.userID;
	var limit = req.session.limit;
	var page = req.session.page;
	importDB.getLogs(res, id, limit, page);
});

//////////////////////////////////////////////////////////////////
//                         Connection                           //
//////////////////////////////////////////////////////////////////
var port = 8888;
app.listen(port,function(){
	console.log("Server running on port " + port);
});
