$(document).ready(function() {
	
	$.getJSON('/statistic', function(statisticData) {
		
		var i = 0;
		var datasets = [];
		for (i in statisticData) {
			var statistic = statisticData[i];
			console.log(statistic);
			var lang = statistic['lang'];
			datasets[i] = {
				label: lang,
				data: statistic.passwords.map(function(item) {
					return [item.length, item.count];
				})
			};
		}
		
		console.log(datasets);
		
		$.plot($('#graph'), datasets);
		// $('#overallPasswordsCount').html(statisticData.overallPasswordsCount + ' passwords');
		
		/*
		var lastTweetDateHtml = 'no more passwords to tweet :(';
		if (statisticData.overallPasswordsCount > 0) {
			var days = statisticData.period.days;
			var hours = statisticData.period.hours;
			var minutes = statisticData.period.minutes;
			var periodStr = days + (days == 1 ? ' day ' : ' days ');
			periodStr += hours + (hours == 1 ? ' hour ' : ' hours ');
			periodStr += minutes + (minutes== 1 ? ' minute' : ' minutes');
			lastTweetDateHtml = periodStr;
		}
		$('#lastTweetPeriod').html(lastTweetDateHtml);
		*/
	});
	
});