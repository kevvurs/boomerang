$( window ).on('load', function() {
  google.charts.load('current', {'packages':['line']});
});

$(function() {
  $('#submit').on('click', function() {
	// clear div
	// splash loader
	var symbol = $('#field').val();
	var apiCall  = ('market/'+symbol);
	$.ajax({
	  type: 'GET',
	  url: apiCall,
	  dataType: 'json',
	  success: function(data) {
		drawChart(data);
	  },
	  error: function(E) {
		// show error
	    console.log('server-side error');
	  }
    });
  });
});

function drawChart(data) {

  var data = new google.visualization.DataTable();
  data.addColumn('date', 'Date');
  data.addColumn('number', 'quote');
  
  data.addRows(data);

  var options = {
    chart: {
      title: 'Box Office Earnings in First Two Weeks of Opening',
      subtitle: 'in millions of dollars (USD)'
    },
    width: 900,
    height: 500
  };

  var chart = new google.charts.Line(document.getElementById('linechart_material'));

  chart.draw(data, options);
}