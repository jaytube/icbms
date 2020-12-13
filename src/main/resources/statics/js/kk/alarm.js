var setting = {
	data: {
		simpleData: {
			enable: true,
			idKey: "id",
			pIdKey: "parentId",
			rootPId: -1
		},
		key: {
			url: "nourl"
		}
	}
};

var setting2 = {
	data: {
		simpleData: {
			enable: true,
			idKey: "id",
			pIdKey: "parentId",
			rootPId: -1
		},
		key: {
			url: "nourl"
		}
	},
	check: {
		nocheckInherit: true
	},
	callback: {
		onClick: zTreeOnClick
	}
};

function zTreeOnClick(event, treeId, treeNode) {
	console.log(treeNode.id)
	//initTable(treeNode.id)
	vm.reload(treeNode.id)
};
var ztree;
var ztree2;
var currentProject = "";
$.ajax({
	type: "POST",
	url: "../projectinfo/getCurrentProject",
	dataType: "json",
	cache: false,
	async: false,
	success: function (data) {
		if (data.code == 0) {
			currentProject = data.currentProject.id;
		}
	}
})
var locationinfo = {
	parentName: null,
	parentId: 0,
	type: 1,
	sort: 0,
	icon: ""
};
$.ajax({
	url: "../app/dashbaord/findLocInfoByPId?projectId=" + currentProject,
	type: "get",
	success: function (r) {
		ztree2 = $.fn.zTree.init($("#allLocationinfoTree"), setting2, r).expandAll(true);
	    ztree = $.fn.zTree.init($("#locationinfoTree"), setting, r);
		var node = ztree.getNodeByParam("id", locationinfo.parentId); //得到当前上级位置节点
		ztree.selectNode(node);
	}
})



$(function () {
	$("#jqGrid").jqGrid({
		url: '/icbms/KK/getAlarmsByTime',
		datatype: "json",
		postData: {
			locationId: $("#locationId").val(),
			startTime: null,
			endTime: null,
			deviceBoxMac: null,
			type: null,
			pageSize: "10", //每次返回多少个数据
			page: "1"
		},
		colModel: [
        {   
            label:"告警等级", 
            name : 'alarmLevel',
            width : 60,
            align : "center",
            formatter: function (value, grid, rows, state) {
                if(rows.alarmStatus == "0"){
                    return "<div style='width:60px;border-radius:5px;background-color:green;height:30px;border:1px solid white; margin:0 auto;line-height:30px;'>恢复</div>"
                }else{
                    if(rows.alarmLevel == "1"){
                        return "<div style='width:60px;border-radius:5px;background-color:#1041ca;height:30px;border:1px solid white; margin:0 auto;line-height:30px;'>预警</div>"
                    }else if(rows.alarmLevel == "2"){
                        return "<div style='width:60px;border-radius:5px;background-color:#02969a;height:30px;border:1px solid white; margin:0 auto;line-height:30px;'>一般</div>"
                    }else if(rows.alarmLevel == "3"){
                        return "<div style='width:60px;border-radius:5px;background-color:#ffa600;height:30px;border:1px solid white; margin:0 auto;line-height:30px;'>重要</div>"
                    }else if(rows.alarmLevel == "4"){
                        return "<div style='width:60px;border-radius:5px;background-color:#e2432d;height:30px;border:1px solid white; margin:0 auto;line-height:30px;'>紧急</div>"
                    }
                    
                }
            }
        },    
        {
			label: '电箱(MAC)',
			name: 'deviceBoxMac',
			align: "center",
			width: 65
        }, 
        {
			label: '展位号',
			name: 'standNo',
			align: "center",
			width: 65
        }, 
        {
			label: '线路',
			name: 'node',
			align: "center",
			width: 75
		}, {
			label: '告警类型',
			name: 'type',
			align: "center",
			width: 75
		}, 
//			{
//			label: '告警等级',
//			name: 'alarmLevelName',
//			align: "center",
//			width: 75
//		}, 
			{
			label: '告警信息',
			name: 'info',
			align: "center",
			width: 100
		},{
			label: '发生时间',
			name: 'recordTime',
			align: "center",
			width: 100
		}],
		viewrecords: true, //是否显示总记录数
		height: 385,
		rowNum: 10, //每页显示多少个
		rownumbers: true,
		rownumWidth: 30,
		autowidth: true,
		multiselect: false, //是否可以多选
		pager: "#jqGridPager",
		jsonReader: {
			root: "page.list",
			page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
//			page: "2",
//			total: "pageInfo.totalPage"
		},
	    prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
		gridComplete: function () {
			//隐藏grid底部滚动条
			$("#jqGrid").closest(".ui-jqgrid-bdiv").css({
				"overflow-x": "hidden"
			});
		}
	});
});

var organTree = "";
var setting = {
	data: {
		simpleData: {
			enable: true,
			idKey: "id",
			pIdKey: "parentId",
			rootPId: -1
		},
		key: {
			url: "nourl"
		}
	}
};


$("table[role='grid']").each(function () {//jqgrid 创建的表格都有role属性为grid
    $('.' + $(this).attr("class") + ' tr:first th:first').css("width", "45"); //使表头的序号列宽度为40
    $('.' + $(this).attr("class") + ' tr:first td:first').css("width", "45"); // 使表体的序号列宽度为40
});
var vm = new Vue({
	el: '#rrapp',
	data: {
		currentProject: {},
		projectId: "", //项目id
		locationId: "", //位置id
		switchBoxId: "", // 电箱id									
		projectInfoList: [], //项目列表
		locationInfos: [], // 位置列表
		switchBoxList: [], //电箱列表
		q: {
			locationId: null,
			startTime: null,
			endTime: null,
			deviceBoxMac: null,
			type: null,
			alarmStatus:null
		},
		showList: true,
		title: null,
		roleList: {},
		user: {
			status: 1,
			baid: '',
			baName: '',
			roleIdList: []
		}
	},
	created: function () {
		var _this = this;
		$.ajax({
			type: "POST",
			url: "../projectinfo/getCurrentProject",
			dataType: "json",
			cache: false,
			async: false,
			success: function (data) {
				if (data.code == 0) {
					_this.currentProject = data.currentProject;
					_this.locationInfos = data.locInfos;
				}
			}
		})
	},
	methods: {
		getlocationInfos: function (id, cb) {
			//alert(id);				
			$.ajax({
				url: "/icbms/locationinfo/findLocInfoByPId",
				type: "POST",
				dataType: "json",
				contentType: "application/json;charset=utf-8",
				data: $.trim(id),
				cache: false,
				async: false,
				success: function (data) {
					console.log(data);
					if (data.code == 0) {
						cb && cb(data);
					}
				}
			})
		},
		getswitchBoxList: function (id, cb) {
			//alert(id);
			$.ajax({
				url: "/icbms/deviceboxinfo/findDeviceBoxsInfoByLId",
				type: "post",
				dataType: "json",
				contentType: "application/json;charset=utf-8",
				data: $.trim(id),
				success: function (data) {
					console.log(data);
					if (data.code == 0) {
						// this.locationInfos = data.locInfos;
						cb && cb(data);
					}
				}
			})
		},

		query: function () {
			vm.showList = true;
			$('#jqGrid').jqGrid('clearGridData');  // 先清空postdata
			//var page = $("#jqGrid").jqGrid('getGridParam','page');
			//console.log(page);
			$("#jqGrid").jqGrid('setGridParam', {
				postData: {
					locationId: vm.q.locationId,
					startTime: $("#startTime").val(),
					endTime: $("#endTime").val(),
					deviceBoxMac: vm.q.deviceBoxMac,
					type: vm.q.type,
					alarmLevel: vm.q.alarmStatus,
					pageSize: "10",
					page: 1
				}
			}).trigger("reloadGrid");
		},

		del: function () {
			var ids = getSelectedRows();
			if (ids == null) {
				return;
			}
			confirm('确定要删除选中的记录？', function () {
				$.ajax({
					type: "POST",
					url: "../sys/user/delete",
					data: JSON.stringify(ids),
					success: function (r) {
						if (r.code == 0) {
							toast(r, function (index) {
								vm.reload();
							});
						} else {
							alertMsg(r.msg);
						}
					}
				});
			});
		},

		getUser: function (id) {
			$.get("../sys/user/info/" + id, function (r) {
				vm.user = r.user;
			});
		},
		getRoleList: function () {
			$.get("../sys/role/select", function (r) {
				vm.roleList = r.list;
			});
		},
		reload: function (locationId) {
			vm.showList = true;
			console.log(vm.q.locationId);
			vm.q.locationId = locationId;
			$('#jqGrid').jqGrid('clearGridData');  // 先清空postdata
			//var page = $("#jqGrid").jqGrid('getGridParam','page');
			//console.log(page);
			$("#jqGrid").jqGrid('setGridParam', {
				postData: {
					locationId: locationId,
					startTime: $("#startTime").val(),
					endTime: $("#endTime").val(),
					deviceBoxMac: vm.q.deviceBoxMac,
					type: vm.q.type,
					pageSize: "10",
					page: 1
				}
			}).trigger("reloadGrid");
		}
	}
});