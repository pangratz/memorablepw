### Prerequisites

* Node.js
* Mac OS X (tested with 10.7 Lion)

### Installation

* checkout source by

		git clone git@github.com:pangratz/memorablepw.git
		cd memorablepw

* install required node packages
		
		npm install applescript
		npm install twitter
		
* create a an **read-and-write** app on [https://dev.twitter.com/](https://dev.twitter.com/) and save the OAuth credentials in a file named `twitter_properties.json` and structured like follows:

		{
			"consumer_key": "YOUR CONSUMER KEY HERE",
			"consumer_secret": "YOUR CONSUMER SECRET HERE",
			"access_token_key": "YOUR ACCESS TOKEN KEY HERE",
			"access_token_secret": "YOUR ACCESS TOKEN SECRET HERE"
		}

	
		
		
* download code poetry's Mac OS X Password Assistant wrapper from [http://www.codepoetry.net/products/passwordassistant](http://www.codepoetry.net/products/passwordassistant) and store it inside the `memorablepw` folder

### Run da app

to start the nodejs script which generates memorable passwords and tweets them, simply execute
		
	node tweet_passwords.js
