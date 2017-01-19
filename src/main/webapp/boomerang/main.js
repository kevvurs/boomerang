var userToken;
var userPrefs;

var exit = function(user) {
  if (!user) {
	window.location = "./..";
  }
}

$( window ).on('load', function() {
  firebase.auth().onAuthStateChanged(exit.bind(this));
});

$( '#sidebar' ).on('click', function() {

});
$(function() {
  $( '#sysExit' ).on('click', function() {
	console.log('click! :)')
	firebase.auth().signOut().then(function() {
	  console.log('Signed Out');
	}, function(error) {
	  console.error('Sign Out Error', error);
	});
  });
});

$( '#appFacebook' ).on('click', function() {
  
});