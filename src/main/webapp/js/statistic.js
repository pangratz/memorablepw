$(document).ready(function() {
	
	$.getJSON('/statistic', function(statisticData) {
		var data = statisticData.passwords.map(function(item) {
			return [item.length, item.count];
		});
		$.plot($('#graph'), [data]);
		$('#overallPasswordsCount').html(statisticData.overallPasswordsCount + ' passwords');
	});
	
});