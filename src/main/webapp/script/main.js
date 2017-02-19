/*
$( window ).on('load', function() {
  $.ajax({
	type: 'GET',
	url: 'api/firebase/config',
	dataType: 'json',
	success: function(config) {
	  firebase.initializeApp(config);
	  firebase.auth().onAuthStateChanged(exit.bind(this));
	},
	error: function(E) {
	  console.log('server-side error');
	  guest = true;
	}
  });
});
*/