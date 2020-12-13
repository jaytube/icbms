var setting = {
	data : {
		simpleData : {
			enable : true,
			idKey : "id",
			pIdKey : "parentId",
			rootPId : -1
		},
		key : {
			url : "nourl"
		}
	}
};

var setting2 = {
	data : {
		simpleData : {
			enable : true,
			idKey : "id",
			pIdKey : "parentId",
			rootPId : -1
		},
		key : {
			url : "nourl"
		}
	},
	check : {
		// 复选框
		// enable:true,
		nocheckInherit : true
	},
	callback : {
		onClick : zTreeOnClick
	}
};

function zTreeOnClick(event, treeId, treeNode) {
	vm.reload();
};
var ztree;
var ztree2;

/**
 * 初始化位置
 */
function initTree() {
	// 加载位置树
	$.get("../locationinfo/initTreeData", function(r) {
		// alert(r.locationList);
		ztree2 = $.fn.zTree.init($("#allLocationinfoTree"), setting2,
				r.locationList);
		// 展开所有节点
		ztree2.expandAll(true);
	});

	$.get("../locationinfo/initTreeData", function(r) {
		// 初始化zTree，三个参数一次分别是容器(zTree 的容器 className 别忘了设置为 "ztree")、参数配置、数据源
		ztree = $.fn.zTree
				.init($("#locationinfoTree"), setting, r.locationList);
		var node = ztree.getNodeByParam("id", vm.deviceBoxInfo.locationId);// 得到当前上级位置节点
		ztree.selectNode(node);// 选中新增加的节点
		// vm.locationinfo.parentName = node.name;
	})
}

$(function() {
	$("#jqGrid")
			.jqGrid(
					{
						url : '../deviceboxinfo/list',
						datatype : "json",
						colModel : [
								{
									label : '位置信息',
									name : 'locationName',
									index : 'location_id'
								},
								{
									label : '电箱(MAC)',
									name : 'deviceBoxNum',
									index : 'device_box_num'
								},

								{
									label : '展位号',
									name : 'standNo',
									index : 'standNo'
								},

								{
									label : '二级电箱网关号',
									name : 'secBoxGateway',
									index : 'secBoxGateway'
								},
								{
									label : '电箱容量',
									name : 'boxCapacity',
									index : 'boxCapacity'
								},

								{
									label : '是否受控',
									name : 'controlFlagName',
									index : 'controlFlag'
								}],
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
							var ids = $("#jqGrid").jqGrid('getDataIDs');
							for (var i = 0; i < ids.length; i++) {
								var id = ids[i];
								var row = $("#jqGrid").jqGrid("getRowData", id);
								var enableRealtimeBtn = "<span class='label label-success' style='cursor:Pointer' onclick='setGetDataWay(\""
										+ row.deviceBoxNum
										+ "\", 1)'>启用</span>";
								var disableRealtimeBtn = "<span class='label label-danger' style='cursor:Pointer' onclick='setGetDataWay(\""
										+ row.deviceBoxNum + "\",0)'>禁用</span>";
								console.log(enableRealtimeBtn);
								$("#jqGrid").jqGrid('setRowData', ids[i], {
									EnableRealtimeBtn : enableRealtimeBtn,
									DisableRealtimeBtn : disableRealtimeBtn
								});
							}
						}
					});
});

function setGetDataWay(deviceMac, flag) {
	if (flag == '1') {
		// 启用实时获取数据
		flag = "true";
	} else if (flag == '0') {
		// 禁用实时获取数据 10分钟一次
		flag = "false";
	}
	confirm('确认设置吗？', function() {
		$.ajax({
			url : "../KK/setUpdateDateTime/" + deviceMac + "/" + flag,
			type : "get",
			dataType : "json",
			cache : false,
			async : false,
			success : function(data) {
				console.log(data.code);
				var obj = JSON.parse(data.code);
				if (obj.code == "0") {
					alert("设置成功!");
				}
			}
		});
	});
}

var vm = new Vue({
	el : '#rrapp',
	data : {
		q : {
			deviceBoxNum : null,
			secBoxGateway : null,
			standNo : null
		},
		showList : true,
		title : null,
		deviceBoxInfo : {},
		locinfos : []
	},
	created : function() {
		initTree();
	},
	computed : {
		currentProjectAndLoc : function() {
			var currentProjectAndLoc = {};
			$.ajax({
				type : "POST",
				url : "../projectinfo/getCurrentProject",
				dataType : "json",
				cache : false,
				async : false,
				success : function(r) {
					if (r.code == 0) {
						currentProjectAndLoc = r;
						return currentProjectAndLoc;
					} else {
						alert(r.msg);
					}
				}
			});
			return currentProjectAndLoc;
		}
	},
	methods : {
		search : function() {
			vm.showList = true;
			$("#jqGrid").jqGrid('setGridParam', {
				postData : {
					'deviceBoxNum' : vm.q.deviceBoxNum,
					'secBoxGateway' : vm.q.secBoxGateway,
					'standNo' : vm.q.standNo
				},
				page : 1
			}).trigger("reloadGrid");
		},
		query : function() {
			vm.reload();
		},
		add : function() {
			var nodes = ztree2.getSelectedNodes();
			vm.title = "新增电箱";
			if (nodes.length > 0) {
				vm.deviceBoxInfo.locationId = nodes[0].id;
				vm.deviceBoxInfo.locationName = nodes[0].name;
			} else {
				vm.deviceBoxInfo = {};
			}
			vm.showList = false;

		},
		imp : function() {
			layui.use('upload', function() {
				var upload = layui.upload;
				// 执行上传
				var uploadInst = upload.render({
					elem : '#upload' // 绑定元素
					,
					url : '../deviceboxinfo/batchImpDevices' // 上传接口
					,
					method : 'POST',
					accept : 'file',
					size : 2000,
					before : function(obj) {
						layer.load();
					},
					done : function(res) {// 上传完毕回调
						layer.closeAll('loading');
						var result = '';

						for (var i = 0; i < res.length; i++) {
							result = result + res[i].firstLoc + ","
									+ res[i].secLoc + "," + res[i].thirdLoc
									+ "," + res[i].deviceMac + "\n";
							result = result + res[i].secBoxGateway + ","
									+ res[i].standNo + "," + res[i].controlFlag
									+ "," + res[i].boxCapacity + "\n";
						}
						$("#result").html(result);
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
				btn : [ '取消' ],
				btn1 : function(index) {
					layer.close(index);
					vm.reload();
				}
			});
		},
		update : function(event) {
			var id = getSelectedRow();
			if (id == null) {
				return;
			}
			vm.showList = false;
			vm.title = "修改";
			vm.getInfo(id);
		},
		saveOrUpdate : function(event) {
			vm.deviceBoxInfo.projectId = $("#currentProjectId").val();
			var url = vm.deviceBoxInfo.id == null ? "../deviceboxinfo/save"
					: "../deviceboxinfo/update";
			$.ajax({
				type : "POST",
				url : url,
				data : JSON.stringify(vm.deviceBoxInfo),
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
					url : "../deviceboxinfo/delete",
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
			$.get("../deviceboxinfo/info/" + id, function(r) {
				// alert(r.code);
				if (r.code == 0) {
					vm.chooseLocationInfo(r.deviceBoxInfo.projectId);
					vm.deviceBoxInfo = r.deviceBoxInfo;
				} else {
					alert(r.msg);
				}
			});
		},
		reload : function(event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam', 'page');
			var nodes = ztree2.getSelectedNodes();
			var locationId = "";
			if (nodes.length > 0) {
				locationId = nodes[0].id;
			}
			// alert(locationId);
			$("#jqGrid").jqGrid('setGridParam', {
				page : page,
				postData : {
					'locationId' : locationId
				},
			}).trigger("reloadGrid");

			$("#deviceboxinfoFrom")[0].reset();

		},
		chooseLocationInfo : function(projectId) {
			$.ajax({
				type : "POST",
				url : "../locationinfo/findLocInfoByPId",
				dataType : "json",
				data : projectId,
				cache : false,
				async : false,
				success : function(r) {
					if (r.code == 0) {
						vm.locinfos = r.locInfos;
					} else {
						alert(r.msg);
					}
				}
			});
		},
		locationinfoTree : function() {
			layer.open({
				type : 1,
				offset : '50px',
				skin : 'layui-layer-molv',
				title : "选择位置",
				area : [ '300px', '450px' ],
				shade : 0,
				shadeClose : false,
				content : jQuery("#locationinfoLayer"),
				btn : [ '确定', '取消' ],
				btn1 : function(index) {
					var node = ztree.getSelectedNodes();
					// 选择位置
					vm.deviceBoxInfo.locationId = node[0].id;
					vm.deviceBoxInfo.locationName = node[0].name;
					vm.showList = true;
					vm.showList = false;
					layer.close(index);
				}
			});
		}
	}
});