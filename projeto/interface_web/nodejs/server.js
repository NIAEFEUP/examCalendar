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

/* TODO delete this block
Example
console.log(typeof login.foo); // => 'function'
console.log(typeof login.new_foo); // => 'function'
console.log(typeof login.bar); // => undefined
*/

var sess;

//TODO the code bellow is not suitable for this project, but is useful as an example. Customize it!
app.post('/login',function(req,res){
	sess=req.session;
	sess.email=req.body.email;
	res.end('done');
});

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

app.get('/logout',function(req,res){

	req.session.destroy(function(err){
		if(err){
			console.log(err);
		}
		else
		{
			res.redirect('/');
		}
	});

});

//TODO the code bellow this comment is ready for the project

var port = 3000;

app.listen(port,function(){
	console.log("Server running on port " + port);
});
