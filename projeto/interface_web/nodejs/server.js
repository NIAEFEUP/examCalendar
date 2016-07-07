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

/* TODO delete this block
Example
console.log(typeof login.foo); // => 'function'
console.log(typeof login.new_foo); // => 'function'
console.log(typeof login.bar); // => undefined
*/

var sess;

app.post('/login',function(req,res){
	res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
	login.authenticate(req, res);
});

app.get('/logout',function(req,res){
	req.session.destroy(function(err){
		if(err){
			console.log(err);
		}
		else
		{
			res.end('{"authenticated":false}');
		}
	});
});

//TODO the code bellow is not suitable for this project, but is useful as an example. Customize it!
app.get('/admin',function(req,res){
	sess=req.session;
	if(sess.email)
	{
		res.write('<h1>Hello '+sess.email+'</h1><br>');
		res.end('<a href='+'/logout'+'>Logout</a>');
	}
	else
	{
		res.write('<h1>Please login first.</h1>');
		res.end('<a href='+'/'+'>Login</a>');
	}

});

//TODO the code bellow this comment is ready for the project

var port = 8888;

app.listen(port,function(){
	console.log("Server running on port " + port);
});
