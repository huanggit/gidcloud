$(function() {
	window.register = {
		init: function() {
			/*
			$("#agreeContract").change(function(){
				if($(this).is(":checked")){
				    $("#doRegister").removeClass("btngreyLonger");
				    $("#doRegister").attr('onClick', 'register.doRegister()'); 
				}
				else{
					$("#doRegister").addClass("btngreyLonger");
					$("#doRegister").removeAttr('onClick'); 
				}
			});*/
		},
		doRegister: function() {
			AJAX("doRegister", {
				"userName": $('#userName').val(),
				"password": $('#password').val(), 
				"confirmPassword": $('#confirmPassword').val(), 
				"email": $('#email').val()
			}, function(data) {
				if (errorHandlers(data)) return;
				$(".p-first").removeClass("current");
				$(".p-third").addClass("current");
				$("#main").html("<p class='regSuccess'>恭喜您，注册成功，请登录邮箱激活您的账号！</p>");
			});
		}
		
	};
	register.init();
});