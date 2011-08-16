var applescript = require('applescript');
var http = require('http');

var postPassword = function(pw) {
	
	var pwArr = pw.map(function(item){
		return {
			text: item,
			lang: 'en'
		};
	});	
	var body = JSON.stringify(pwArr);
	console.log(body);
	var options = {
		host: 'memorablepw.appspot.com',
		path: '/password',
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
			'Content-Length': body.length
		}
	};
	
	var req = http.request(options, function(res) {
		res.setEncoding('utf8');
		console.log("Got response: " + res.statusCode);
		console.log('HEADERS: ' + JSON.stringify(res.headers));  
		res.on('data', function(chunk) {
			console.log("Body: \n" + chunk);
		});
	});
	req.write(body);
	req.end();
};

var executeAppleScript = function(pwLength){
	pwLength = pwLength || 18;
	pwLength = Math.min(Math.max(8, pwLength), 31);
	applescript.execFile('get_password.applescript', [pwLength], function(err, rtn) {
		if (err) {
			// Something went wrong!
			console.log(err);
			return;
		}
		postPassword(rtn);
	});
};

var length = process.argv[2] || 18;
executeAppleScript( length );