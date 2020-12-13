$(function() {
	$("#jqGrid")
			.jqGrid(
					{
						url : '../deviceboxconfig/list',
						datatype : "json",
						colModel : [
								{
									label : '网关MAC地址',
									name : 'gatewayMac',
									index : 'gateway_mac'
								},
								{
									label : '网关地址',
									name : 'gatewayAddress',
									index : 'gateway_address'
								},
								{
									label : '电箱MAC地址',
									name : 'deviceBoxMac',
									index : 'device_box_mac'
								},
								{
									label : '电箱地址',
									name : 'deviceBoxAddress',
									index : 'device_box_address'
								},
								{
									label : '电箱状态',
									name : 'deviceBoxStatus',
									index : 'device_box_status',
									formatter : function(value, grid, rows,
											state) {
										if (value == "0") {
											return "空闲";
										} else if (value == "1") {
											return "在线";
										}
									}
								},
								{
									label : '二维码查看',
									name : 'deviceBoxNum',
									index : 'device_box_num',
									formatter : function(value, grid, rows,
											state) {
										return " <button class=\"layui-btn layui-btn-small\" type=\"button\" onclick=\"showQr('"
												+ rows.deviceBoxMac
												+ "')\"><i class=\"layui-icon\"></i>查看</button>";
									}
								} ],
						viewrecords : true,
						height : 385,
						rowNum : 10,
						rowList : [ 10, 30, 50 ],
						rownumbers : true,
						rownumWidth : 25,
						autowidth : true,
						multiselect : true,
						pager : "#jqGridPager",
						jsonReader : {
							root : "page.list",
							page : "page.currPage",
							total : "page.totalPage",
							records : "page.totalCount"
						},
						prmNames : {
							page : "page",
							rows : "limit",
							order : "order"
						},
						gridComplete : function() {
							// 隐藏grid底部滚动条
							$("#jqGrid").closest(".ui-jqgrid-bdiv").css({
								"overflow-x" : "hidden"
							});
						}
					});
});

function showQr(deviceBoxNum) {
	var imgUrl = "../app/dashbaord/getQrcode?deviceBoxMac=" + deviceBoxNum;
	var html = '<div class="form-body" style="padding: 25px;">';
	html += '<img src=' + imgUrl + '></img>';
	html += '</div>';
	layer.open({
		scrollbar : false,
		type : 1,
		offset : 'top',
		title : [ "查看二维码", true ],
		content : html,
		shadeClose : true,
		btn : [ '下载' ],
		yes : function(index) {
			location.href = "../app/dashbaord/downQrcode?deviceBoxMac="
					+ deviceBoxNum;
		}
	});
}

var vm = new Vue({
	el : '#rrapp',
	data : {
		q : {
			gatewayMac : null,
			deviceBoxMac : null
		},
		showList : true,
		title : null,
		deviceBoxConfig : {}
	},
	methods : {
		search : function() {
			vm.showList = true;
			$("#jqGrid").jqGrid('setGridParam', {
				postData : {
					'gatewayMac' : vm.q.gatewayMac,
					'deviceBoxMac' : vm.q.deviceBoxMac
				},
				page : 1
			}).trigger("reloadGrid");
		},
		query : function() {
			vm.reload();
		},
		add : function() {
			vm.showList = false;
			vm.title = "新增";
			vm.deviceBoxConfig = {};
		},
		update : function(event) {
			var id = getSelectedRow();
			if (id == null) {
				return;
			}
			vm.showList = false;
			vm.title = "修改";

			vm.getInfo(id)
		},
		saveOrUpdate : function(event) {
			var url = vm.deviceBoxConfig.id == null ? "../deviceboxconfig/save"
					: "../deviceboxconfig/update";
			$.ajax({
				type : "POST",
				url : url,
				data : JSON.stringify(vm.deviceBoxConfig),
				success : function(r) {
					if (r.code === 0) {
						alert(r, function(index) {
							vm.reload();
						});
					} else {
						alert(r.msg);
					}
				}
			});
		},
		del : function(event) {
			var ids = getSelectedRows();
			if (ids == null) {
				return;
			}

			confirm('确定要删除选中的记录？', function() {
				$.ajax({
					type : "POST",
					url : "../deviceboxconfig/delete",
					data : JSON.stringify(ids),
					success : function(r) {
						if (r.code == 0) {
							alert(r, function(index) {
								$("#jqGrid").trigger("reloadGrid");
							});
						} else {
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo : function(id) {
			$.get("../deviceboxconfig/info/" + id, function(r) {
				if (r.code == 0) {
					vm.deviceBoxConfig = r.deviceBoxConfig;
				} else {
					alert(r.msg);
				}
			});
		},

		imp : function() {
			layui.use('upload', function() {
				var upload = layui.upload;
				// 执行上传
				var uploadInst = upload.render({
					elem : '#upload' // 绑定元素
					,
					url : '../deviceboxconfig/batchImpDevices' // 上传接口
					,
					method : 'POST',
					accept : 'file',
					size : 2000,
					before : function(obj) {
						layer.load();
					},
					done : function(res) {// 上传完毕回调
						layer.closeAll('loading');

						alert("上传成功!");
						$("#jqGrid").trigger("reloadGrid");
					},
					error : function() {// 请求异常回调
						layer.closeAll('loading');
						layer.msg('网络异常，请稍后重试！');
					}
				});
			});
			layer.open({
				type : 1,
				skin : 'layui-layer-molv',
				title : "批量导入",
				area : [ '500px', '400px' ],
				shadeClose : false,
				content : $("#batchImpLayer"),
				btn : [ '取消','确定' ],
				btn2: function(index, layero){
					vm.reload();
				}
			});
		},

		reload : function(event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
				postData : {
					'gatewayMac' : vm.q.gatewayMac,
					'deviceBoxMac' : vm.q.deviceBoxMac
				},
				page : page
			}).trigger("reloadGrid");
		}
	}
});