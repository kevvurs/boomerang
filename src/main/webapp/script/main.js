var userToken, userPrefs, guest;

var exit = function(user) {
  if (user) {
	guest = false;
  } else {
	guest = true;
  }
}

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

$( '#sidebar' ).on('click', function() {

});

$(function() {
  $( '#sysExit' ).on('click', function() {
	if (guest) {
	  window.location = "./index.html";
	}
	firebase.auth().signOut().then(function() {
	  console.log('Signed out.');
	  window.location = "./index.html";
	}, function(error) {
	  console.error('Sign out error', error);
	});
  });
});

$(function() {
	$( '#appFacebook' ).on('click', function() {
		var dataReq = mkXMLRequest('api/app/bing');
		var appInstance = $.ajax(dataReq);
		$( '.appWin' ).draggable({ iframeFix: true, stack: '.appWin' });
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
	var container = document.createElement('div');
	var menubar = document.createElement('div');
	var frame = document.createElement('iframe');
	var byline = document.createElement('h3');
	container.setAttribute('class','appWin');
	menubar.setAttribute('class','appMenu');
	if (appData.window.fullscreen) {
	  container.height = window.innerHeight;
	  container.width = window.innerWidth;
	} else {
	  container.height = appData.window.height;
	  container.width = appData.window.width;
	}
	frame.src = appData.target;
	frame.width = container.width;
	frame.height = container.height - 36;
	byline.innerHTML = "TITLE";
	menubar.appendChild(byline);
	container.appendChild(menubar);
	container.appendChild(frame);
	if(document.body != null){ document.body.appendChild(container); }
}