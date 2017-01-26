var auth, database, storage;

var entrance = function(user) {
  if (user) {
	var userToken = user.uid;
	database.ref('/users/' + userToken).once('value').then(function(snapshot) {
	  if (!snapshot.val() || !snapshot.val().created) {
		this.database.ref('users/' + userToken).set({
		  created: true,
		  name: user.displayName
		});
	    window.location = "./boomerang.html";
	  } else {
		window.location = "./boomerang.html";
	  }
	});
  }
}

$( window ).on('load', function() {
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

$(function() {
  $('#login').on('click', function() {
	auth = firebase.auth();
	database = firebase.database();
	storage = firebase.storage();
	auth.onAuthStateChanged(entrance.bind(this));
	var provider = new firebase.auth.GoogleAuthProvider();
	auth.signInWithPopup(provider);
  });
});

$(function() {
  $('#guest').on('click', function() {
	window.location = "./boomerang.html";
  });
});