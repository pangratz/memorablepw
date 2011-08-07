var applescript = require('applescript');

var executeAppleScript = function(pwLength){
	pwLength = pwLength || 18;
	pwLength = Math.min(Math.max(8, pwLength), 31);
	applescript.execFile('get_password.scpt', [pwLength], function(err, rtn) {
		if (err) {
			// Something went wrong!
		}
		console.log('length: ' + pwLength + ' password = ' + rtn);
		setTimeout(function(){
			executeAppleScript(pwLength < 31 ? pwLength + 1 : 8);
		}, 500);
	});
};

executeAppleScript(8);