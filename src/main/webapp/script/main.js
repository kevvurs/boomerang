var userToken;
var userPrefs;

var exit = function(user) {
  if (!user) {
	window.location = "./index.html";
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

$(function() {
	$( '#appFacebook' ).on('click', function() {
		var dataReq = mkXMLRequest('api/app/facebook');
		var appInstance = $.ajax(dataReq);
	});
});


function mkXMLRequest(appURL) {
	var settings = 
	{
        type: 'GET',
        url: appURL,
        dataType: 'json',
        success: function(data) {
	    	if (data) {
				openWindow(data);
			} else {
				console.log('client-side error');
			}
        },
        error: function(E) {
            console.log('server-side error');
        }
	};
	return settings;
}

function openWindow(appData) {
	var frame = document.createElement('iframe');
	if (appData.window.fullscreen) {
		frame.height = window.innerHeight;
		frame.width = window.innerWidth;
	} else {
		frame.height = appData.window.height;
		frame.width = appData.window.width;
	}
	frame.src = appData.target;
	frame.setAttribute('class','appWin')
	if(document.body != null){ document.body.appendChild(frame); }
}