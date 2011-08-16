$(document).ready(function() {
	
	$.getJSON('/statistic', function(statisticData) {
		var data = statisticData.map(function(item) {
			return [item.length, item.count];
		});
		$.plot($('#graph'), [data]);
	});
	
});