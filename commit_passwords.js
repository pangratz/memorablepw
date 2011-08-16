var applescript = require('applescript');
var http = require('http');

var httpOptions = {
	host: 'memorablepw.appspot.com',
	path: '/password',
	'content-type': 'application/json',
	method: 'POST'
};

var executeAppleScript = function(pwLength){
	pwLength = pwLength || 18;
	pwLength = Math.min(Math.max(8, pwLength), 31);
	applescript.execFile('get_password.applescript', [pwLength], function(err, rtn) {
		if (err) {
			// Something went wrong!
		}
		console.log('length: ' + pwLength + ' password = ' + rtn);
		if (rtn) {
			var req = http.request(httpOptions, function(res){
				res.setEncoding('utf8');
				
				res.on('data',function(chunk){
					console.log(chunk);
				});
			});
			req.on('error', function(e) {
			  console.log('problem with request: ' + e.message);
			});
			var pw = {
				text: rtn,
				lang: "en"
			};
			req.write(JSON.stringify(pw));
			req.end();
		}
	});
};

executeAppleScript(18);