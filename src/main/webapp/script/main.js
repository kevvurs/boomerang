var userToken;
var selfDestruct;

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

// Open a new conversation.
$(function() {
  $('#searchButton').on('click', function() {
	var otherUser = $('#searchField').val();
	if (!otherUser) {
	  return;
	}
	$('#searchField').val("");
	var conversation = { user: userToken.username, other: otherUser };
  // Dev
	showUser(conversation.other, true);
  openConversation(conversation);
  
  return;
	// end#
  
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
  clearNotif(div);
  selfDestruct = setTimeout(clearNotif, 4000, div);
  
  var notifMessage = document.createElement('p');
  notifMessage.setAttribute('class','notif');
  notifMessage.onclick = function() {
	  clearNotif(div);
    clearTimeout(selfDestruct);
  };

  if (exists) {
	notifMessage.innerHTML = (userName + " added");
  } else {
	notifMessage.innerHTML = ("user not found");
  }

  div.appendChild(notifMessage);
}

function clearNotif(div) {
  var desc = div.children;
  if (desc.length > 3) {
    div.removeChild(desc[desc.length-1]);
    clearNotif(div);
  } else {
    return;
  }
}

function openConversation(conv) {
  var div = document.getElementById('conversationDiv');
  
  var container = document.createElement('div');
  var contactName = document.createElement('p');
  var isOnline = document.createElement('p');
  var lastMessage = document.createElement('p');
  
  container.setAttribute('class','contact');
  contactName.setAttribute('class', 'contact');
  isOnline.setAttribute('class', 'online');
  lastMessage.setAttribute('class', 'prevmsg');
  isOnline.innerHTML = "\u25bc";
  contactName.innerHTML = conv.other;
  lastMessage.innerHTML = "previous message here";
  
  container.appendChild(isOnline);
  container.appendChild(contactName);
  container.appendChild(lastMessage);
  
  div.appendChild(container);
}