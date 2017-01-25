$(function() {
	$.ajax({
		type: 'GET',
        url: 'api/firebase/config',
        dataType: 'json',
        success: function(config) {
  		  firebase.initializeApp(config);
        },
        error: function(E) {
            console.log('server-side error');
        }
	});
});