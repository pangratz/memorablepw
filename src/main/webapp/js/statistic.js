$(document).ready(function() {
	
	$.getJSON('/statistic', function(statisticData) {
		
		var i = 0;
		var datasets = [];
		for (i in statisticData) {
			var statistic = statisticData[i];
			var lang = statistic['lang'];
			
			$('#overallPasswordsCount').append(statistic.overallPasswordsCount + ' <b>' + lang + '</b> passwords</br>');
			
			var lastTweetDateHtml = 'no more ' + lang +' passwords to tweet :(';
			if (statistic.overallPasswordsCount > 0) {
				var days = statistic.period.days;
				var hours = statistic.period.hours;
				var minutes = statistic.period.minutes;
				var periodStr = days + (days == 1 ? ' day ' : ' days ');
				periodStr += hours + (hours == 1 ? ' hour ' : ' hours ');
				periodStr += minutes + (minutes== 1 ? ' minute' : ' minutes');
				lastTweetDateHtml = periodStr + ' until the end of <b>' + lang + '</b> passwords</br>';
			}
			$('#lastTweetPeriod').append(lastTweetDateHtml);
			
			datasets[i] = {
				label: lang,
				data: statistic.passwords.map(function(item) {
					return [item.length, item.count];
				})
			};
		}
				
		$.plot($('#graph'), datasets);
	});
	
});