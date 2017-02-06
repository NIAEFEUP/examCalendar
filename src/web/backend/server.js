//dependencies required
var express = require('express');
var session = require('express-session');
var bodyParser = require('body-parser');
var async = require('async');
var cors = require('cors');
var app = express();

//allow different domain requests
var whitelist = [
    'http://localhost' // TODO (hardcoded)
];
var corsOptions = {
    origin: function (origin, callback) {
        var originIsWhitelisted = whitelist.indexOf(origin) !== -1;
        callback(null, originIsWhitelisted);
    },
    credentials: true
};
app.use(cors(corsOptions));

app.engine('html', require('ejs').renderFile);

app.use(session({secret: 'secret_key', saveUninitialized: true, resave: false}));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

//controllers required
var users = require('./controllers/users');
var adminHome = require('./controllers/adminHome');
var adminUsers = require('./controllers/adminUsers');
var calendar = require('./controllers/calendar');
var constraints = require('./controllers/constraints');
var importDB = require('./controllers/importDB');

// This is required due to security issues. The response has specific details
// such as the response got to be from the same domain.
// This function tells the browser that it's secure.
function allowRedirectAnswer(res) {
    res.header("Access-Control-Allow-Origin", "http://localhost");
    res.header("Access-Control-Allow-Credentials", "true");
    //res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    return res;
}

function isAuthenticated(req) {
    console.log(req.session);
    return req.session != null && req.session.userID != null && req.session.userID >= 0;
}

function unauthorizedAccess(res) {
    res.status(401);
    res.send();
}

// For all requests
app.all('*', function (req, res, next) {
    allowRedirectAnswer(res);
    next(); // Do the action specific to the request
});

app.get('/currentUser', function (req, res) {
    users.getCurrent(req, res);
});


//////////////////////////////////////////////////////////////////
//                            Login                             //
//////////////////////////////////////////////////////////////////
app.post('/login', function (req, res) {
    if (isAuthenticated(req)) {
        res.status(200);
        res.send();
    } else {
        users.authenticate(req, res);
    }
});

//////////////////////////////////////////////////////////////////
//                            Logout                            //
//////////////////////////////////////////////////////////////////
app.get('/logout', function (req, res) {
    console.log("LOGOUT");
    req.session.userID = null;
    res.status(200);
    res.send();
});

//////////////////////////////////////////////////////////////////
//                          Database                            //
//////////////////////////////////////////////////////////////////

app.post('/setTimespan', function (req, res) {
    if (!isAuthenticated(req)) {
        unauthorizedAccess(res);
        return;
    }
    var id = req.session.userID;
    importDB.setTimespan(res, id, req.body.normalStartDate, req.body.normalDuration, req.body.appealDuration);
});

app.post('/createCalendar', function (req, res) {
    if (!isAuthenticated(req)) {
        unauthorizedAccess(res);
        return;
    }
    var id = req.session.userID;
    importDB.createCalendar(res, id, req.body.normalStartDate, req.body.normalDuration, req.body.appealDuration);
});
app.post('/deleteCalendar', function (req, res) {
    if (!isAuthenticated(req)) {
        unauthorizedAccess(res);
        return;
    }
    var id = req.session.userID;
    importDB.deleteCalendar(res, id);
});
app.post('/database', function (req, res) {
    if (!isAuthenticated(req)) {
        unauthorizedAccess(res);
        return;
    }
    var id = req.session.userID;
    importDB.import(res, id, req);
});
app.get('/importDatabase/topics', function (req, res) {
    res = allowRedirectAnswer(res);
    var id = req.session.userID;
    importDB.getTopics(res, id);
});
app.post('/importDatabase/topics', function (req, res) {
    res = allowRedirectAnswer(res);
    var id = req.session.userID;
    importDB.setTopics(res, id, req.body.topics);
});

//////////////////////////////////////////////////////////////////
//                         Admin Home                           //
//////////////////////////////////////////////////////////////////
app.get('/adminHome', function (req, res) {
    if (!isAuthenticated(req)) {
        unauthorizedAccess(res);
        return;
    }
    var id = req.session.userID;
    var limit = req.body.limit;
    var page = req.body.page;
    importDB.getLogs(res, id, limit, page);
});

//////////////////////////////////////////////////////////////////
//                         Constraints                          //
//////////////////////////////////////////////////////////////////
app.get('/constraints', function (req, res) {
    if (!isAuthenticated(req)) {
        unauthorizedAccess(res);
        return;
    }
    constraints.get(res, req.session.userID);
});

app.put('/constraints', function (req, res) {
    if (!isAuthenticated(req)) {
        unauthorizedAccess(res);
        return;
    }
    constraints.add(res, req.session.userID, req.body.constraint);
});

app.post('/constraints', function (req, res) {
    if (!isAuthenticated(req)) {
        unauthorizedAccess(res);
        return;
    }
    constraints.update(res, req.session.userID, req.body.constraintID, req.body.constraint);
});

app.delete('/constraints', function (req, res) {
    if (!isAuthenticated(req)) {
        unauthorizedAccess(res);
        return;
    }
    constraints.remove(res, req.session.userID, req.body.constraintID);
});

//////////////////////////////////////////////////////////////////
//                        Admin Users                           //
//////////////////////////////////////////////////////////////////
app.get('/adminUsers', function (req, res) {
    if (!isAuthenticated(req)) {
        unauthorizedAccess(res);
        return;
    }
    adminUsers.get(res, req.session.userID);
});

app.put('/adminUsers', function (req, res) {
    if (!isAuthenticated(req)) {
        unauthorizedAccess(res);
        return;
    }
    adminUsers.add(res, req.session.userID, req.body.email);
});

app.delete('/adminUsers', function (req, res) {
    if (!isAuthenticated(req)) {
        unauthorizedAccess(res);
        return;
    }
    adminUsers.remove(res, req.session.userID, req.body.email);
});

//////////////////////////////////////////////////////////////////
//                          Calendar                            //
//////////////////////////////////////////////////////////////////
app.get('/calendar', function (req, res) {
    if (!isAuthenticated(req)) {
        unauthorizedAccess(res);
        return;
    }
    calendar.get(res, req.session.userID);
});

app.get('/exams/:id', function (req, res) {
    if (!isAuthenticated(req)) {
        unauthorizedAccess(res);
        return;
    }
    calendar.getExam(res, req.session.userID, req.params.id);
});

app.post('/calendar', function (req, res) {
    if (!isAuthenticated(req)) {
        unauthorizedAccess(res);
        return;
    }
    calendar.generate(res, req.session.userID);
});

app.post('/calendar/exams', function (req, res) {
    if (!isAuthenticated(req)) {
        unauthorizedAccess(res);
        return;
    }
    calendar.moveExam(res, req.session.userID, req.body);
});

app.put('/calendar/exams/:examid/rooms/:roomid', function (req, res) {
    if (!isAuthenticated(req)) {
        unauthorizedAccess(res);
        return;
    }
    calendar.updateExamRoom(res, req.session.userID, req.params.examid, req.params.roomid, true);
});

app.delete('/calendar/exams/:examid/rooms/:roomid', function (req, res) {
    if (!isAuthenticated(req)) {
        unauthorizedAccess(res);
        return;
    }
    calendar.updateExamRoom(res, req.session.userID, req.params.examid, req.params.roomid, false);
});

//////////////////////////////////////////////////////////////////
//                         Connection                           //
//////////////////////////////////////////////////////////////////
var port = 8080;
app.listen(port, function () {
    console.log("Server running on port " + port);
});
