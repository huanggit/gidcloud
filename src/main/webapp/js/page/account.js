$(function() {
	window.account = {
		init: function() {
			account.getAccount();
		},
		getAccount: function() {
			AJAX("getAccount", {
				//"token": $.cookie("token")
			}, function(data) {
				if (errorHandlers(data)) return;
				$("#dataWrap").html(template.render('accountData', data));
			});
		},
		modAccount: function() {
			AJAX("modAccount", {
				"userId": $("#userId").text(),
				"org":  $("#org").val(),
				"phone":  $("#phone").val(),
			}, function(data) {
				if (errorHandlers(data)) return;
				genNoty("success",data.note);
			});
		},
		modPassword: function() {
			AJAX("modPassword", {
				"oldPasswd": $("#oldPasswd").val(),
				"newPasswd":  $("#newPasswd").val(),
				"confirmPasswd":  $("#confirmPasswd").val(),
			}, function(data) {
				if (errorHandlers(data)) return;
				genNoty("success",data.note);
			});
		},
		accountTab: function(){
			$("#accountTab").addClass("current");
			$("#passwordTab").removeClass("current");
			account.getAccount();
		},
		passwordTab: function(){
			$("#passwordTab").addClass("current");
			$("#accountTab").removeClass("current");
			$("#dataWrap").html(template.render('passwordData'));
		}
		
	};
	account.init();
});