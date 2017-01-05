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
    		data: userAuth,
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