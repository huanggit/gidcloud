$(function() {
	window.group = {
		init: function() {
			group.search(0);
			$( "#viewDev" ).dialog({
				 title:"组成员",resizable: false,autoOpen: false, modal:true, position:'center', height:"auto", width:"400"
				 ,buttons: {
				      "关闭": function() { $('#viewDev').dialog("close"); }
				  }
			});
		},
		search: function(pageNo) {
			AJAX("getGroups", {
				"groupNameKeyword": $("#groupNameKeyword").val(),
				"groupIdKeyword": $("#groupIdKeyword").val(),
				"pageNo": pageNo 
			}, function(data) {
				if (errorHandlers(data)) return;
				$("#dataWrap").html(template.render('dataList', data));
				initBootstrapPaginator("page", "pageSelect", group.search, data);
				$(".viewDev").click(function(){
					var data = $(this).attr('data');
					group.getDevicesByGroup(data);
			    });
			});
		},
		searchByDeviceId: function(pageNo) {
			AJAX("getGroupsByDeviceId", {
				"deviceIdKeyword": $("#deviceIdKeyword").val(),
				"pageNo": pageNo 
			}, function(data) {
				if (errorHandlers(data)) return;
				$("#dataWrap").html(template.render('dataList', data));
				initBootstrapPaginator("page", "pageSelect", group.searchByDeviceId, data);
				$(".viewDev").click(function(){
					var data = $(this).attr('data');
					group.getDevicesByGroup(data);
			    });
			});
		},
		getDevicesByGroup: function(groupId) {
			AJAX("getDevicesByGroup", {
				"groupId": groupId
			}, function(data) {
				if (errorHandlers(data)) return;
				$("#tipDataWrap").html(template.render('tipData', data));
				$( "#viewDev" ).dialog("open");
			});
		}
		
	};
	group.init();
});