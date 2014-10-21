$(function() {
	window.doc = {
		init: function() {
			var navH = $(".boxRight").offset().top;
			$(window).scroll(function(){
				var scroH = $(this).scrollTop();
				if(scroH>=navH){
					$(".boxRight").addClass('fixer');
				}else if(scroH<navH){
					$(".boxRight").removeClass('fixer');
				}
			});
			$(".mt").each(function(){
				var $mt = $(this);
				$mt.find("span").click(function(){
					$mt.find("ul").show();
					$mt.siblings().find("ul").hide();
				});
			});
		}
	};
	doc.init();
});