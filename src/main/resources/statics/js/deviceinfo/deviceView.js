$(function() {
	$("#jqGrid").jqGrid({
		url : '../deviceboxinfo/deviceView',
		datatype : "json",
		colModel : [{
			label : '电箱MAC地址',
			name : 'deviceBoxNum',
			index : 'device_box_num'
		},
		{
			label : "电箱状态",
			name : 'online',
			sortable: false,
			formatter : function(value, grid, rows, state) {
				if (rows.online != null && rows.online == "0") {
					return "在线";
				} else {
					return "不在线";
				}
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
					'deviceBoxMac' : vm.q.deviceBoxMac,
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