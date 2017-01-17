var userToken;
var userPrefs;

// firebase
var auth, database, storage;

var exit = function(user) {
  if (!user) {
	window.location = "./index.html";
  }
}

$( window ).on('load', function() {
  this.auth = firebase.auth();
  this.database = firebase.database();
  this.storage = firebase.storage();
  this.auth.onAuthStateChanged(exit.bind(this));
});

$( '#sidebar' ).on('click', function() {

});

$( '#sysExit' ).on('click', function() {
  this.auth.signOut();
});

$( '#appFacebook' ).on('click', function() {
  
});