	initParms = function(data) {
		 	var par = {
		 		token: $.cookie("token")
		 	};
		 	for (var i in data) {
		 		par[i] =  data[i];
		 	}
		 	return par;
	};
	
	AJAX = function(cmdName, params, callBack) {
		$.ajax({
			url: cmdName,
			timeout: 30000,
			type: "POST",
			dataType: "json",
			contentType: "application/x-www-form-urlencoded",
			data: initParms(params),
			async: false,
			success: callBack
		});
	};

	errorHandlers = function (data) {
	 	if (data.result != 0) {
			genNoty('error', data.note);	
			return true;
		}
		return false;
	 };

	 genNoty = function(type, text, layout){
    	var title = '';
    	switch(type){
    	case 'success':
    		title = '成功';
    		break;
    	case 'error':
    		title = '错误';
    		break;
    	case 'warning':
    		title = '警告';
    		break;
    	default:
    		title = '';
    	}
        noty({
            text        : '<strong>'+title+'  </strong>'+text,
            type        : type,
            dismissQueue: true,
            timeout     : 3000,
            layout      : layout == null ? 'topCenter' : layout,
            theme       : 'bootstrapTheme',
            closeWith   : ['button', 'click'],
            maxVisible  : 20,
            maxVisible: 1, // you can set max visible notification for dismissQueue true option,
            killer: true
        });
    };
    
    initBootstrapPaginator = function(pageId,selectId,callFunc,data){
		var pages = data.pages;
		var pageNo = data.pageNo+1;
		if(pageNo>pages) pageNo=pages;
		var options = {
				bootstrapMajorVersion:3,
                currentPage: pageNo,
                totalPages: pages,
                size:'normal',
                alignment:'right',
                useBootstrapTooltip:false,
                onPageClicked:function(event, originalEvent, type,page){
                	callFunc(page-1);
                	event.stopImmediatePropagation();
                }
            };

       $('#'+pageId).bootstrapPaginator(options);
       if(pages <=1 || typeof pages == "undefined"){
    	   $("#"+selectId).hide();
       }else{
    	   var content = "";
    	   for(var i=0; i<pages;i++){
    		   content += "<option value='"+(i+1)+"'>"+(i+1)+"</option>";
    	   }
    	   $("#"+selectId).html(content).val(pageNo);
    	   $("#"+selectId).unbind().on("change",function(){
        	   callFunc($(this).val()-1);
           });
       }
	};
	
	var wordbook = {
		onlineStatus : function(val){
			return ['离线', '在线静止', '在线运行'][val];
		}
	};
	
	
$(function(){
	if($('.wrap').height()<$(window).height() -189)
	{$('.wrap').height(parseInt($(window).height() -189));}
});
