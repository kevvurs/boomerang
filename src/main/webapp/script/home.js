var auth, database, storage;

var entrance = function(user) {
  if (user) {
	var userToken = user.uid;
	database.ref('/users/' + userToken).once('value').then(function(snapshot) {
	  if (!snapshot.val() || !snapshot.val().created) {
		this.database.ref('users/' + userToken).set({
		  created: true,
		  appsInstalled: "facebook"
		});
	    window.location = "./boomerang.html";
	  } else {
		console.log('welcome back');
		window.location = "./boomerang.html";
	  }
	});
  }
}

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

function activateFirebase() {
	$.ajax({
        type: 'GET',
        url: 'api/firebase/config',
        dataType: 'json',
        success: function(config) {
        	firebase.initializeApp(config);
        	return true;
        },
        error: function(a,b,c) {
            console.log('web error: '+ c);
            return false;
        }
	});
}