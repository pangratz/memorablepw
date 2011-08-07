var applescript = require('applescript');
var Twitter = require('twitter');
var fs = require("fs");

var props = JSON.parse(fs.readFileSync('twitter_properties.json', 'utf8'));
var twitter = new Twitter(props);

var executeAppleScript = function(pwLength){
	pwLength = pwLength || 18;
	pwLength = Math.min(Math.max(8, pwLength), 31);
	applescript.execFile('get_password.scpt', [pwLength], function(err, rtn) {
		if (err) {
			// Something went wrong!
		}
		console.log('length: ' + pwLength + ' password = ' + rtn);
		if (rtn) {
			twitter.updateStatus(rtn, function(data){
			});
		}
		
		setTimeout(function(){
			executeAppleScript(pwLength < 31 ? pwLength + 1 : 8);
		}, 1000*60*10);
	});
};

// get latest tweet character count
var params = {
	count: 1
};
twitter.getUserTimeline(params, function(json){
	if (Array.isArray(json) && json.length > 0) {
		var tweet = json[0];
		var length = tweet.text.length + 1;
		console.log('start generating passwords of length ' + length);
		executeAppleScript(length);
	} else {
		console.log('unable to get latest tweet');
		console.log(json);
	}
});