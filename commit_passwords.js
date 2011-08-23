/*globals process*/
var applescript = require('applescript');
var http = require('http');
var querystring = require('querystring');

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

var submitPasswords = function(pwLength, pws, pwCount) {
	applescript.execFile('get_password.applescript', [pwLength], function(err, rtn) {
		if (err) {
			// Something went wrong!
			console.log(err);
			return;
		}
		rtn = rtn.map(function(item){
			return querystring.escape(item);
		});
		
		pws = pws.concat(rtn);
		if (pws.length >= pwCount) {
			var diff = pws.length - pwCount;
			pws.splice(pwCount, diff);
			
			console.log('submit passwords: ' + pws);
			postPassword(pws);
		} else {
			submitPasswords(pwLength, pws, pwCount);
		}
	});
};

var executeAppleScript = function(pwLength, pwCount){
	pwLength = pwLength || 18;
	pwLength = Math.min(Math.max(8, pwLength), 31);
	
	pwCount = pwCount || 10;
	pwCount = Math.max(pwCount, 0);
	
	var allPws = [];
	
	submitPasswords(pwLength, allPws, pwCount);
};

var length = process.argv[2] || 18;
var count = process.argv[3] || 10;
executeAppleScript( length, count );