$(function () {
    $("#jqGrid").jqGrid({
        url: '../projectinfo/list',
        datatype: "json",
        colModel: [			
			{ label: '项目名称', name: 'projectName', index: 'project_name'}, 	
			{ label: '生效日期', name: 'effectiveDate', index: 'effective_date'}, 	
			{ label: '失效日期', name: 'expireDate', index: 'expire_date'},
			{ label: '网关', name: 'gatewayAddress', index: 'gateway_address' },
			{ label: '备注', name: 'remark', index: 'remark'},
			{ label: '创建时间', name: 'createTimeStr', index: 'create_time' }
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		roleList:[],
		roleIdList:[],
		projectInfo: {
			effectiveDate:'',
			expireDate:''
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.roleList = [];
			//获取角色信息
			this.getRoleList();
			vm.projectInfo = {};
			vm.roleIdList = [];
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            //获取角色信息
			this.getRoleList();
            vm.getInfo(id);
		},
		changeConsole(){
			console.log(this.projectInfo);
		},
		saveOrUpdate: function (event) {
			var url = vm.projectInfo.id == null ? "../projectinfo/save" : "../projectinfo/update";
			if($("#effectiveDate").val()!=""){
				vm.projectInfo.effectiveDate = $("#effectiveDate").val();
			}
			
			if($("#expireDate").val()!=""){
				vm.projectInfo.expireDate = $("#expireDate").val();
			}
			if(vm.projectInfo.projectName==undefined){
				alert("项目名称不能为空");
				return;
			}
			
			if(vm.projectInfo.effectiveDate==undefined){
				alert("生效日期不能为空");
				return;
			}
			
			if(vm.projectInfo.gatewayAddress==undefined){
				alert("网关不能为空");
				return;
			} else {
				var reg = /^(\d+,?)+$/;
				if(!reg.test(vm.projectInfo.gatewayAddress)){
					alert("输入不合法,网关地址为数字,多个网关之间用英文逗号相隔");
					return;
				}
			}
			
			if(vm.projectInfo.expireDate==undefined){
				alert("失效日期不能为空");
				return;
			}
			
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify({...vm.projectInfo, roleIdList: vm.roleIdList}),
			    success: function(r){
			    	if(r.code === 0){
						alert(r, function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../projectinfo/delete",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code == 0){
							alert(r, function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		change(value){
			console.log(this.roleList);
			console.log(value);
		},
		getRoleList: function(){
			$.get("../sys/role/select", function(r){
				vm.roleList = r.list;
			});
		},
		getInfo: function(id){
			$.get("../projectinfo/info/"+id, function(r){
                if(r.code == 0){
					vm.projectInfo = r.projectInfo;
					vm.roleIdList = r.projectInfo.roleIdList;
                }else{
                    alert(r.msg);
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});