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

var submitPasswords = function(pwLengthArr, pws) {
	applescript.execFile('get_password.applescript', pwLengthArr, function(err, rtn) {
		if (err) {
			// Something went wrong!
			console.log(err);
			return;
		}
		
		rtn = rtn.map(function(item){
			return querystring.escape(item);
		});
		
		console.log('submit passwords: ' + rtn);
		postPassword(rtn);
	});
};

var executeAppleScript = function(pwLength, pwCount){
	pwLength = pwLength || 18;
	pwLength = Math.min(Math.max(8, pwLength), 31);
	
	pwCount = pwCount || 10;
	pwCount = Math.max(pwCount, 0);
	
	// taken from http://stackoverflow.com/questions/2044760/default-array-values/2044990#2044990
	Array.prototype.repeat = function(what, L) {
		while(L) this[--L] = what;
		return this;
	};
	
	// new array of length 31-8 = 23
	var pwLengthArr = [].repeat(0, 23);
	pwLengthArr[pwLength - 8] = pwCount;
	
	submitPasswords(pwLengthArr);
};

var length = process.argv[2] || 18;
var count = process.argv[3] || 10;
executeAppleScript( length, count );