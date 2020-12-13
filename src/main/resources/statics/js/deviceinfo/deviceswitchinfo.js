$(function () {
    $("#jqGrid").jqGrid({
        url: '../deviceswitchinfo/list',
        datatype: "json",
        colModel: [			
        	{ label: '归属项目', name: 'projectName',  width: 100 }, 			
        	{ label: '位置', name: 'locationName',  width: 100 }, 			
			{ label: '所属电箱', name: 'deviceBoxName',  width: 80 }, 			
			{ label: '线路名称', name: 'deviceSwitchName', index: 'device_switch_name', width: 80 }, 
			{ label: '线路地址', name: 'address', index: 'address', width: 80 }, 			
			{ label: '设备状态', name: 'deviceSwitchStatus', index: 'device_switch_status', width: 80 }, 			
			{ label: '实时电流', name: 'switchElectric', index: 'switch_electric', width: 80 }, 			
			{ label: '实时电压', name: 'switchVoltage', index: 'switch_voltage', width: 80 }, 			
			{ label: '实时温度', name: 'switchTemperature', index: 'switch_temperature', width: 80 }, 			
			{ label: '实时功率', name: 'switchPower', index: 'switch_power', width: 80 }, 			
			{ label: '漏电流', name: 'switchLeakage', index: 'switch_leakage', width: 80 }, 						
			{ label: '备注', name: 'remark', index: 'remark', width: 80 }			
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
		deviceSwitchInfo: {},
	
		locinfos:[],
		deviceBoxs:[]
	},
    computed:{
        projects:function(){
        	 var array= [];                
        	 $.ajax({
 				type: "POST",
 			    url: "../projectinfo/listAll", 			  
 			    dataType: "json",
 			    cache:false, 
 			    async:false, 
 			    success: function(r){
 		           if(r.code == 0){ 		        	   
 	                    var projectInfoList = r.projectInfoList; 	 	
 	                    for(var i=0; i<projectInfoList.length; i++){ 	                    	
 	                    	var pi = {text:projectInfoList[i].projectName, value:projectInfoList[i].id}; 	
 	                    	array.push(pi); 	                    	
 	                    } 	    	                   	             
 	                }else{
 	                    alert(r.msg);
 	                }
 		           }
 			});   
        	 return array;
        },
      },
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.deviceSwitchInfo = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
			var url = vm.deviceSwitchInfo.id == null ? "../deviceswitchinfo/save" : "../deviceswitchinfo/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.deviceSwitchInfo),
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
				    url: "../deviceswitchinfo/delete",
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
		getInfo: function(id){
			$.get("../deviceswitchinfo/info/"+id, function(r){
                if(r.code == 0){
                	vm.chooseLocationInfo(r.deviceSwitchInfo.projectId);
                	vm.chooseDeviceBox(r.deviceSwitchInfo.locationId);
                    vm.deviceSwitchInfo = r.deviceSwitchInfo;
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
		},
		chooseLocationInfo: function(projectId){
		 $.ajax({
 				type: "POST",
 			    url: "../locationinfo/findLocInfoByPId", 			  
 			    dataType: "json",
 			    data:projectId,
 			    cache:false, 
 			    async:false, 
 			    success: function(r){
 		           if(r.code == 0){ 		        	   
 		        	  vm.locinfos=r.locInfos; 	 
 	                }else{
 	                    alert(r.msg);
 	                }
 		         }
 			});  			 
		},
		chooseDeviceBox: function(locationId){
		//	alert(locationId);
		 $.ajax({
 				type: "POST",
 			    url: "../deviceboxinfo/findDeviceBoxsInfoByLId", 			  
 			    dataType: "json",
 			    data:locationId,
 			    cache:false, 
 			    async:false, 
 			    success: function(r){
 		           if(r.code == 0){ 		        	   
 		        	  vm.deviceBoxs=r.deviceBoxs; 	 
 	                }else{
 	                    alert(r.msg);
 	                }
 		         }
 			});  			 
		}
	}
});