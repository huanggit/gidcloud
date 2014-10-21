$(function() {
	window.device = {
		init: function() {
			device.search(0);
			$( "#setDisplacement" ).dialog({
				 title:"设置排量",autoOpen: false, resizable: false, modal:true, position:'center', height:"auto", width:"380"
				 ,buttons: {
				      "设置": function() { 
							AJAX("setDisplacementByDeviceId", {
								"deviceId":  $('#setDisplacement input:eq(0)').val(),
								"displacement": $('#setDisplacement input:eq(1)').val(),
							}, function(data) {
								if (errorHandlers(data)) return;
								genNoty('success', '设置排量成功');
								device.search($('#page').bootstrapPaginator("getPages").current - 1);
								$('#setDisplacement').dialog("close");
							});
					      } ,
				      "取消": function() { $('#setDisplacement').dialog("close"); }
				  }
			});
			$( "#gpsInfo" ).dialog({
				 title:"查看最新数据",autoOpen: false, resizable: false, modal:true, position:'center', height:"auto", width:"auto"
					 ,buttons: {
					      "关闭": function() { $('#gpsInfo').dialog("close"); }
					 }
			});
		},
		search: function(pageNo) {
			AJAX("getDevices", {
				"deviceIdKeyword": $("#deviceIdKeyword").val(),
				"pageNo": pageNo 
			}, function(data) {
				if (errorHandlers(data)) return;
				$("#dataWrap").html(template.render('dataList', data));
				initBootstrapPaginator("page", "pageSelect", device.search, data);
				
				$(".setDisplacement").click(function(){
					var index = $(this).attr('data');
					$("#tipDataWrap").html(template.render('tipData', data.dataList[index]));
					$( "#setDisplacement" ).dialog("open");
			    });
				$(".gpsInfo").click(function(){
					var deviceId = $(this).attr('data');
					AJAX("getGpsInfo", {
						"deviceId":  deviceId,
					}, function(data) {
						if (errorHandlers(data)) return;
						data.deviceId = deviceId;
						data.onlineStatus = wordbook.onlineStatus(data.onlineStatus);
						$("#tipDataWrapGps").html(template.render('tipDataGps', data));
						$( "#gpsInfo" ).dialog("open");
					});

			    });
			});
		}
		
	};
	device.init();
});