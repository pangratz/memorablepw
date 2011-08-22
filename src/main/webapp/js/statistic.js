$(document).ready(function() {
	
	$.getJSON('/statistic', function(statisticData) {
		var data = statisticData.passwords.map(function(item) {
			return [item.length, item.count];
		});
		$.plot($('#graph'), [data]);
		$('#overallPasswordsCount').html(statisticData.overallPasswordsCount + ' passwords');
		
		var days = statisticData.period.days;
		var hours = statisticData.period.hours;
		var minutes = statisticData.period.minutes;
		var periodStr = days + (days == 1 ? ' day ' : ' days ');
		periodStr += hours + (hours == 1 ? ' hour ' : ' hours ');
		periodStr += minutes + (minutes== 1 ? ' minute' : ' minutes');
		$('#lastTweetPeriod').html(periodStr);
	});
	
});