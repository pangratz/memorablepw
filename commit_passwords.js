/*globals process*/
var applescript = require('applescript');
var http = require('http');
var querystring = require('querystring');

var HOST = 'memorablepw.appspot.com';

var postPassword = function(lang, pw) {
	
	var pwArr = pw.map(function(item){
		return {
			text: item,
			lang: lang
		};
	});	
	var body = JSON.stringify(pwArr);
	console.log(body);
	var options = {
		host: HOST,
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

var submitPasswords = function(lang, pwLength, pwCount) {
	Array.prototype.repeat = function(what, L){
		while(L) this[--L]= what;
		return this;
	};
	// 31 - 8 = 23
	var counts = [].repeat(0, 23);
	counts[pwLength-8] = pwCount;
	applescript.execFile('get_password.applescript', counts, function(err, rtn) {
		if (err) {
			// Something went wrong!
			console.log(err);
			return;
		}
		
		var pws = rtn.map(function(item){
			return querystring.escape(item);
		});
		
		postPassword(lang, pws);
	});
};

var fillStorage = function(lang, pwCounts) {
	applescript.execFile('get_password.applescript', pwCounts, function(err, rtn) {
		if (err) {
			// Something went wrong!
			console.log(err);
			return;
		}
		
		var pws = rtn.map(function(item){
			return querystring.escape(item);
		});
		
		postPassword(lang, pws);
	});
};

var executeAppleScript = function(lang, pwLength, pwCount){
	pwLength = pwLength || 18;
	pwLength = Math.min(Math.max(8, pwLength), 31);
	
	pwCount = pwCount || 10;
	pwCount = Math.max(pwCount, 0);
	
	submitPasswords(lang, pwLength, pwCount);
};

applescript.execFile('get_lang.applescript', [], function(err, lang) {
	if (err) {
		// Something went wrong!
		console.log(err);
		return;
	}
	
	var firstParam = process.argv[2];
	var secondParam = process.argv[3];
	if (firstParam === 'fill') {
		var fillUpTo = process.argv[3] || 0;
		if (fillUpTo === 0) {
			console.log('please specifiy a password count');
		} else {
			// get statistic
			var opts = {
				host: HOST,
				path: '/statistic'
			};
			http.get(opts, function(res){
				res.setEncoding('utf8');
				var str = '';
				res.on('data', function(chunk) {
					str += chunk;
				});
				res.on('end', function(){
					var statistic = JSON.parse(str);
					var pws = statistic.passwords;
					var pwCounts = [];
					var x = 0;
					for (x in pws) {
						var count = pws[x].count;

						// caluclate diff
						var diff = fillUpTo - count;
						pwCounts[x] = Math.max(0, diff);
					}
					fillStorage(lang, pwCounts);
				});
			});
		}
	} else {
		var length = firstParam || 18;
		var count = secondParam || 10;
		executeAppleScript(lang, length, count);
	}
	
});