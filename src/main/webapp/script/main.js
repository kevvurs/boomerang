var userToken;
$( document ).ready(function() {
	try{
		userToken = JSON.parse(sessionStorage.getItem("user"));
	} catch(error) {
		console.log("cached user token not valid- " + error);
		alert("You are not signed-in properly. Note: disabling browser storage may prevent you from signing in completely.");
	}
	if (!userToken) {
		window.location = "./index.html"
	}
});

$(function() {
  $('#searchButton').on('click', function() {
	var otherUser = $('#searchField').val();
	if (!otherUser) {
	  return;
	}
	
	var conversation = { user: userToken.username, other: otherUser };
	showUser(conversation.other, true);
	$.ajax({
		  url: "api/conv/exists",
		  method: "POST",
		  contentType: "application/json",
		  data: JSON.stringify(conversation),
		  dataType: "json",
		  success: function(response) {
			  showUser(conversation.other, response.exists);
		  },
		  fail: function() {
			  showUser(conversation.other, false);
		  }
	});
  });
});

function showUser(userName, exists) {
  var div = document.getElementById('searchDiv');
 
  var notifMessage = document.createElement('p');
  notifMessage.setAttribute('class','notif');
  notifMessage.onclick = function() {
	div.removeChild(notifMessage);
  };

  if (exists) {
	notifMessage.innerHTML = (userName + " added");
  } else {
	notifMessage.innerHTML = ("user not found");
  }
  setTimeout(notifMessage.onclick, 4000);

  div.appendChild(notifMessage);
}