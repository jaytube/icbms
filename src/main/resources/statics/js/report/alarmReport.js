$(function () {
    $("#jqGrid").jqGrid({
        url: '../report/alarm/list',
        datatype: "json",
        colModel: [
//            { label: 'ID', name: 'id', width: 30, key: true },
            { label: '电箱(MAC)', name: 'deviceBoxMac', width: 60 },
            { label: '展位号', name: 'standNo', width: 60 },
            { label: '线路', name: 'node', width: 60 }, 
            { label: '告警类型', name: 'type', width: 100 },
            { label: '告警等级', name: 'alarmLevelName', width: 100 },
            { label: '告警信息', name: 'info', width: 100 },
            { label: '发生时间', name: 'recordTime', width: 80, sortable: true}
        ],
        viewrecords: true,
        height: 385,
        rowNum: 10,
        rowList : [10,30,50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth:true,
        multiselect: false,
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
        q:{
            beginTime: null,
            endTime: null
        },
        showList: true,
        title: null,
        config: {}
    },
    methods: {
	   search: function (event) {
          vm.showList = true;
          $("#jqGrid").jqGrid('setGridParam',{
              postData:{'beginTime': $("#beginTime").val(),
                  'endTime':$("#endTime").val()
              },
              page:1
          }).trigger("reloadGrid");
       },
        query: function () {
            vm.reload();
        },
        exportAll: function() {
            var form = $("<form></form>").attr("action", "../report/alarm/exportAll").attr("method", "post");

            form.append($("<input></input>").attr("type", "hidden").attr("name", "beginTime").attr("value", $("#beginTime").val()));
            form.append($("<input></input>").attr("type", "hidden").attr("name", "endTime").attr("value", $("#endTime").val()));
            form.appendTo('body').submit().remove();
        },
        reload: function (event) {
            vm.showList = true;
            
            
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{'beginTime': $("#beginTime").val(),
                    'endTime':$("#endTime").val()
                },
                page:page
            }).trigger("reloadGrid");
        }
    }
});