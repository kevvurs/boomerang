var showErrorMessage = true;
var errorMessage = "username or password is invalid"

$(function() {
    $('#clearall').on('click', function() {
    	$('#username').val("");
    	$('#password').val("");
    });
});

$(function() {
    $('#signin').on('click', function() {
    	var usernameValue = $('#username').val();
    	var passwordValue = $('#password').val();
    	$('#username').val("");
    	$('#password').val("");
    	
    	var userAuth = {username: usernameValue,
    			password: passwordValue };
    	
    	$.ajax({
    		url: "api/user/login",
    		method: "POST",
    		contentType: "application/json",
    		data: JSON.stringify(userAuth),
    		dataType: "json",
    		success: function(userInfo) {
    			var userInfoSerial = JSON.stringify(userInfo);
    			sessionStorage.setItem("user",userInfoSerial);
    		},
    		fail: function() {
    			alert("Error- ");
    		}
    	});
    });
});

$(function() {
    $('#register').on('click', function() {
    	var failure = function() {
    		if (showErrorMessage) {
    			showErrorMessage = false;
    			var blurb = $('#intro').html();
    			$('#intro').html(blurb 
    					+ "<br /><br />" 
    					+ "<i>" 
    					+ errorMessage 
    					+ "</i>");
    		}
    	};
    	
    	var usernameValue = $('#username').val();
    	var passwordValue = $('#password').val();
    	
    	if (!usernameValue || !passwordValue) {
    		failure();
    		return;
    	}
    	
    	$('#username').val("");
    	$('#password').val("");
    	
    	var userAuth = {username: usernameValue,
    			password: passwordValue };
    	
    	$.ajax({
    		url: "api/user/register",
    		method: "POST",
    		contentType: "application/json",
    		data: JSON.stringify(userAuth),
    		dataType: "json",
    		success: function(userInfo) {
    			var userInfoSerial = JSON.stringify(userInfo);
    			sessionStorage.setItem("user",userInfoSerial);
    		},
    		fail: function() {
    			failure();
    		}
    	});
    });
});