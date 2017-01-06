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