$(function () {
    $("#jqGrid").jqGrid({
        url: '../report/electricdaily/list',
        datatype: "json",
        colModel: [
// { label: 'ID', name: 'id', width: 30, key: true },
        	{ label: '日期', name: 'statDate', width: 60 },
            { label: '电箱(MAC)', name: 'deviceBoxNum', width: 60 },
            { label: '展位号', name: 'standNo', width: 60 },
            { label: '二级开关号', name: 'secBoxGateway', width: 60 },
            { label: '线路', name: 'addr', width: 60 }, 
            { label: '电量', name: 'electricity', width: 100 }
            
        ],
        viewrecords: true,
        height: 385,
        rowNum: 50,
        rowList : [10,30,50,100,300],
        rownumbers: true,
        rownumWidth: 25,
        autowidth:true,
        multiselect: true,
        onSelectRow: function(rowid, status){
        	totalElectricCnt();
        },
        onSelectAll:function(rowids,statue){ 
        	totalElectricCnt();
        },
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
            // 隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        },
        loadComplete: function () {
        	$("#cb_jqGrid").click();
        }
    });
});

function totalElectricCnt() {
	var rowIds=$("#jqGrid").jqGrid("getGridParam", "selarrrow");
	// var rowIds = jQuery("#jqGrid").jqGrid('getDataIDs');
	var totalElectricCnt = 0;
    for(var k=0; k < rowIds.length; k++) {
       var curRowData = jQuery("#jqGrid").jqGrid('getRowData', rowIds[k]);
      
       totalElectricCnt = add(parseFloat(totalElectricCnt),parseFloat(curRowData.electricity));
    }
    console.log(totalElectricCnt);
    $("#totalNumDiv").html(totalElectricCnt);
}

function add(arg1,arg2)
{
	var r1, r2, m, c;	    
	try {	        
		r1 = arg1.toString().split(".")[1].length;	    
	}	    
	catch (e) {	       
		r1 = 0;	    
	}	    
	try {	        
		r2 = arg2.toString().split(".")[1].length;	    
	}	    
	catch (e) {	       
		r2 = 0;	    
	}	    
	c = Math.abs(r1 - r2);	    
	m = Math.pow(10, Math.max(r1, r2));	   
	if (c > 0) {	        
		var cm = Math.pow(10, c);	       
		if (r1 > r2) {	            
			arg1 = Number(arg1.toString().replace(".", ""));	          
			arg2 = Number(arg2.toString().replace(".", "")) * cm;	        
		} else {	           
		    arg1 = Number(arg1.toString().replace(".", "")) * cm;	            
		    arg2 = Number(arg2.toString().replace(".", ""));	        
		}	    
	 } else {	        
		 arg1 = Number(arg1.toString().replace(".", ""));	        
		 arg2 = Number(arg2.toString().replace(".", ""));	    
	 }	   
	 return allmuna = (arg1 + arg2) / m;
}



var vm = new Vue({
    el:'#rrapp',
    data:{
        q:{
        	deviceBoxNum: null,
        	secBoxGateway: null,
        	standNo: null
        },
        showList: true,
        title: null,
        config: {}
    },
    methods: {
    	search: function (event) {
            vm.showList = true;
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{'deviceBoxNum': vm.q.deviceBoxNum,
                    'secBoxGateway':vm.q.secBoxGateway,
                    'standNo':vm.q.standNo
                },
                page:1
            }).trigger("reloadGrid");
        },
        query: function () {
            vm.reload();
        },
// exportAll: function() {
// var form = $("<form></form>").attr("action",
// "../report/electric/exportAll").attr("method", "post");
//
// form.append($("<input></input>").attr("type", "hidden").attr("name",
// "beginTime").attr("value", $("#beginTime").val()));
// form.append($("<input></input>").attr("type", "hidden").attr("name",
// "endTime").attr("value", $("#endTime").val()));
// form.appendTo('body').submit().remove();
// },
        reload: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{'deviceBoxNum': vm.q.deviceBoxNum,
                    'secBoxGateway':vm.q.secBoxGateway,
                    'standNo':vm.q.standNo
                },
                page:1
            }).trigger("reloadGrid");
        }
    }
});