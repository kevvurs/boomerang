var entrance = function(user) {
  if (user) {
	var userToken = user.uid;
	this.database.ref('/users/' + userToken).once('value').then(function(snapshot) {
	  if (!snapshot.val()) {
		// Register
		this.database.ref('users/' + userToken).set({
		  appsInstalled: "facebook"
		});
	  }
	});
	window.location = "./boomerang.html";
  }
}

$(function() {
  $('#login').on('click', function() {
	this.auth = firebase.auth();
	this.database = firebase.database();
	this.storage = firebase.storage();
	this.auth.onAuthStateChanged(entrance.bind(this));
	var provider = new firebase.auth.GoogleAuthProvider();
	this.auth.signInWithPopup(provider);
  });
});

$(function() {
    $('#register').on('click', function() {
    	window.location = "./register.html";
    });
});