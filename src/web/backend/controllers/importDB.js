//add students/teachers/rooms (all of them are .xlxs or .xls)
// POST: add

var database = require('./database');
var http = require('http');
var nconf = require('nconf');

module.exports = {
    createCalendar: function (res, creatorID, startingDate, normalSeasonDuration, appealSeasonDuration) {
        database.createCalendar(creatorID, startingDate, normalSeasonDuration, appealSeasonDuration);
        res.end();
    },
    deleteCalendar: function (res, creatorID) {
        database.deleteCalendar(creatorID);
        res.end();
    },

    import: function (res, userID, req) {
        database.getCalendar(userID, function (calendarID) {
            console.log('callback started');
            var content_type = req.headers['content-type'];
            var boundary = content_type.split('; ')[1].split('=')[1];
            var calendarIDString = '--'
                + boundary
                + '\r\n'
                + 'Content-Disposition: form-data; name="calendarid"'
                + '\r\n'
                + '\r\n'
                + calendarID
                + '\r\n';

                console.log('callback 1');
            req.headers['content-length'] = '' + (parseInt(req.headers['content-length'], 10) + calendarIDString.length);
            var options = {
                hostname: nconf.get("scheduler:host"),
                port: nconf.get("scheduler:port"),
                path: '/parser',
                method: 'POST',
                headers: req.headers
            };

            console.log('callback 2');
            var req2 = http.request(options, function (res2) {
                console.log('callback 2.1');
                res2.pipe(res, {
                    end: true
                });
                console.log('callback 2.2');
                res.statusCode = res2.statusCode;
            console.log('callback 2.3');
            });

            console.log('callback 3');
            var body = [];
            req.on('data', function (chunk) {
                console.log('callback 3.1');
                body.push(chunk);
            }).on('end', function () {
                console.log('callback 3.2');
                body = Buffer.concat(body);
                body = Buffer.concat([new Buffer(calendarIDString), body]);
                req2.write(body);
                console.log('callback 3.3');
                req2.end();
                console.log('callback 3.4');
            });
            console.log('callback 4');
        });
    },
    getTopics: function (res, userID) {
        database.getTopics(userID, function (err, rows, fields) {
            if (err) throw err;
            res.json(rows);
        });
    },
    setTopics: function (res, userID, topics) {
        database.setTopics(userID, topics);
        res.end();
    }
};
