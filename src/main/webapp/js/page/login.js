$(function() {
	window.login = {
		init: function() {
			if ($.cookie("rmbUser") == "true") { 
				$("#ck_rmbUser").prop("checked", true); 
				$("#username").val($.cookie("username")); 
			} 
			$("form").submit(function(){
	            login.save();
	            return true;
	        });
		},
		save: function() {
			if ($("#ck_rmbUser").prop("checked")) { 
				var username = $("#username").val(); 
				$.cookie("rmbUser", "true", { expires: 7 });
				$.cookie("username", username, { expires: 7 }); 
			}else{
				$.cookie("rmbUser", "false", { expire: -1 }); 
				$.cookie("username", "", { expires: -1 }); 
			} 
		}
		
	};
	login.init();
});