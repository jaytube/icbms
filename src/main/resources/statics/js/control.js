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

var locationinfo = {
    parentName: null,
    parentId: 0,
    type: 1,
    sort: 0,
    icon: ""
};

var ztree;
var ztree2;


function zTreeOnClick(event, treeId, treeNode) {
    electric.getElectricBox(0, treeNode.id);
};

var electric = new Vue({
    el: "#control",
    data: {
        currentProject: "",
        //联动部分数据
        deviceBoxArr: [], //当前位置对应所有电箱数组
        deviceBoxNum: "", //当前电箱Mac地址
        currentBoxId:'',
        allSwitches: [], //当前电箱里面空开得数据
        addressInfo: "", //单个线路电压电流等实时状态
        selectAddressIndex: 0, //选择实时状态得开关,默认选择第一个
        tableData: [], // 告警数据;
        show: false,
        //控制部分数据
        status: true, //电箱实时状态和实时告警切换，true是实时参数
        addressStatus: true, //单个线路的开关状态 
        deviceSwitchStatus:false,
        boxInfo: {},
        online:2, // 0是在线 1是离线  2代表所有
        isShishi:"1", // 默认展示实时数据
        switchInfo:{}, // 开关数据
        nodeId:'',//保存当前点击的nodeId
        locationList:[],
        hours:"1", // 默认展示一小时内
        types:'1',// 默认展示电流统计图  1电流2 电压 3功率,
        historyInfo:[], //历史数据
        showright: false,
        loading: true
    },
    created: function () {
        var _this = this;
        $.ajax({
            type: "POST",
            url: "../projectinfo/getCurrentProject",
            dataType: "json",
            cache: false,
            success: function (data) {
                if (data.code == 0) {
                    _this.currentProject = data.currentProject;
                    var apiUrl = api.API_GET_LOCATION + "?projectId=" + _this.currentProject.id;
                    util.ajax(apiUrl, "get", function (r) {
                        for(let i=0; i< r.length; i++){
                            let isparent = false;
                            for(let j=0;j < r.length; j++) {
                                if(r[i].id == r[j].parentId){
                                    isparent = true;
                                    break;
                                }
                            }
                            if(!isparent) {
                                if(r[i].online == 0) { // 在线
                                    r[i].icon = '../images/notOnline.png'
                                } else if(r[i].online == '1' || r[i].online == null){
                                    r[i].icon = '../images/online1.png'
                                }
                            }
                        }
                        _this.locationList = r;
                        let arr = [],arr1 = [];
                        for(let i=0; i< _this.locationList.length; i++){
                            let isparent = false;
                            for(let j=0;j < _this.locationList.length; j++) {
                                if(_this.locationList[i].id == _this.locationList[j].parentId){
                                    isparent = true;
                                    break;
                                }
                            }
                            if(isparent) {
                                arr.push(_this.locationList[i]); // 所有父节点
                            } else { 
                                arr1.push(_this.locationList[i]); // 所有子节点
                            }
                        }
                        //electric.getElectricBox(0, _this.locationList[i].id);
                        ztree2 = $.fn.zTree.init($("#allLocationinfoTree"), setting2, r).expandAll(true);
                        var treeObj = $.fn.zTree.getZTreeObj("allLocationinfoTree");
                        var node = treeObj.getNodeByParam("id", arr1[0].id);
                        treeObj.selectNode(node);
                        electric.getElectricBox(0, arr1[0].id);
                        new MtrSearchZTree();
                    });
                }
            }
        })
    },
    watch:{
        currentBoxId(oldval){
            if(this.isShishi == '2') {
                this.getHistoryData(this.hours);
            }
        }
    },
    methods: {
        tabsChange(value){
            if(value.label == '历史数据') {
                this.getHistoryData(this.hours);
            }
        },
        dateFormat(date, format) {
            date = new Date(date);
            var map = {
              M: date.getMonth() + 1, // 月份
              d: date.getDate(), // 日
              h: date.getHours(), // 小时
              m: date.getMinutes(), // 分
              s: date.getSeconds(), // 秒
              q: Math.floor((date.getMonth() + 3) / 3), // 季度
              S: date.getMilliseconds() // 毫秒
            };
            format = format.replace(/([yMdhmsqS])+/g, function(all, t) {
              var v = map[t];
              if (v !== undefined) {
                if (all.length > 1) {
                  v = "0" + v;
                  v = v.substr(v.length - 2);
                }
                return v;
              } else if (t === "y") {
                return (date.getFullYear() + "").substr(4 - all.length);
              }
              return all;
            });
            return format;
        },
        changeTime(value){
            this.getHistoryData(value);
        },
        changeTypes(valule){
            this.getHistoryData(this.hours);
        },
        getArr(arr, attr){
            let arr1 = [];
            arr.forEach(value => {
                if(attr == 'createTime') {
                    arr1.push(this.dateFormat(value[attr], 'yyyy-MM-dd hh:mm:ss'));
                } else {
                    arr1.push(value[attr]);
                }
                
            })
            return arr1;
        },
        getHistoryData(hours){
            let that = this;
            let timeType = function(){
                if(hours == '1'){
                    return '1小时内'
                }else if(hours == '24'){
                    return '1天内'
                }else if(hours == '72') {
                    return '3天内'
                }
            }();
            let chartType = function(){
                if(that.types == '1') {
                    return '电流'
                } else if(that.types == '2') {
                    return '电压'
                } else if(that.types == '3') {
                    return '功率'
                }
            }();
            $.ajax({
                url:`/icbms/app/dashbaord/getDeviceBoxHistory?deviceBoxId=${this.currentBoxId}+&projectId=${this.currentProject.id}&hours=${hours}`,
                type:'GET',
                success(res){
                    res.result.forEach((value) => {
                        value.createTime = that.dateFormat(value.createTime, 'yyyy-MM-dd hh:mm:ss');
                    })
                    that.historyInfo = res.result;
                    var option = {
                        title: {
                          text: `${timeType}${chartType}统计图`,
                          textStyle:{
                            fontSize: 18,//字体大小
                            color: '#ffffff'//字体颜色
                          }
                        },
                        tooltip: {
                          trigger: 'axis',
                          textStyle:{
                            fontSize: 18,//字体大小
                            color: '#ffffff'//字体颜色
                          },
                        },
                        xAxis: {
                          data: that.getArr(res.result, 'createTime'),
                          axisLabel: {
                            show: true,
                            textStyle: {
                                color: '#ffffff'
                            }
                          },
                          axisLine:{
                            lineStyle:{
                               color:'#ffffff' //更改坐标轴颜色
                            }
                          }
                        },
                        yAxis: {
                          splitLine: {
                            show: false
                          },
                          axisLabel: {
                            show: true,
                            textStyle: {
                                color: '#ffffff'
                            }
                          },
                          axisLine:{
                            lineStyle:{
                               color:'#ffffff' //更改坐标轴颜色
                            }
                          }
                        },
                        toolbox: {
                          left: 'center',
                          textStyle:{
                            fontSize: 18,//字体大小
                            color: '#ffffff'//字体颜色
                          },
                        },
                        dataZoom: [{
                          type: 'inside'
                        }],
                        visualMap: {
                        textStyle:{
                            fontSize: 18,//字体大小
                            color: '#ffffff'//字体颜色
                        },
                          top: 10,
                          right: 10,
                          pieces:function(){
                            if(that.types == '1') { // 电流
                                return  [{
                                gt: 0,
                                lte: 10,
                                color: '#096'
                                }, {
                                gt: 10,
                                lte: 20,
                                color: '#ffde33'
                                }, {
                                gt: 20,
                                lte: 30,
                                color: '#ff9933'
                                }, {
                                gt: 30,
                                lte: 40,
                                color: '#cc0033'
                                }, {
                                gt: 40,
                                color: 'red'
                                }]
                            } else if(that.types == '2') {
                                return  [{
                                gt: 0,
                                lte: 100,
                                color: '#096'
                                }, {
                                gt: 100,
                                lte: 200,
                                color: '#ffde33'
                                }, {
                                gt: 200,
                                lte: 300,
                                color: '#ff9933'
                                }, {
                                gt: 300,
                                lte: 400,
                                color: '#cc0033'
                                }, {
                                gt: 400,
                                color: 'red'
                                }]
                            } else if( that.types == '3') { // 电箱容量15
                                return [{
                                    gt: 0,
                                    lte: 1000 * (Number(that.boxInfo.boxCapacity)/15),
                                    color: '#096'
                                    }, {
                                    gt: 1000 * (Number(that.boxInfo.boxCapacity)/15),
                                    lte: 2000 * (Number(that.boxInfo.boxCapacity)/15),
                                    color: '#ffde33'
                                    }, {
                                    gt: 2000 * (Number(that.boxInfo.boxCapacity)/15),
                                    lte: 3000 * (Number(that.boxInfo.boxCapacity)/15),
                                    color: '#ff9933'
                                    }, {
                                    gt: 3000 * (Number(that.boxInfo.boxCapacity)/15),
                                    lte: 4000 * (Number(that.boxInfo.boxCapacity)/15),
                                    color: '#cc0033'
                                    }, {
                                    gt: 4000 * (Number(that.boxInfo.boxCapacity)/15),
                                    color: 'red'
                                }]
                            }
                          }(),
                          outOfRange: {
                            color: '#999'
                          }
                        },
                        series: [{
                          name: chartType,
                          type: 'line',
                          data: function () {
                              if(that.types == '1') { // 电流   
                                return that.getArr(res.result, 'switchElectric')
                              } else if(that.types == '2') { // 电压
                                return that.getArr(res.result, 'switchVoltage')
                              } else if(that.types == '3') {
                                return that.getArr(res.result, 'switchPower')
                              }
                          }(),
                          textStyle:{
                            fontSize: 18,//字体大小
                            color: '#ffffff'//字体颜色
                          },
                          markLine: {
                            silent: true,
                            textStyle:{
                                fontSize: 18,//字体大小
                                color: '#ffffff'//字体颜色
                              },
                            data: function(){
                                if(that.types == '1') {
                                    return [{
                                        yAxis: 10
                                      }, {
                                        yAxis: 20
                                      }, {
                                        yAxis: 30
                                      }, {
                                        yAxis: 40
                                      }, {
                                        yAxis: 50
                                    }]
                                } else if(that.types == '2'){
                                    return [{
                                        yAxis: 100
                                      }, {
                                        yAxis: 200
                                      }, {
                                        yAxis: 300
                                      }, {
                                        yAxis: 400
                                      }, {
                                        yAxis: 500
                                    }]
                                } else if(that.types == 3) {
                                    return [{
                                        yAxis: 1000 * (Number(that.boxInfo.boxCapacity)/15)
                                      }, {
                                        yAxis: 2000 * (Number(that.boxInfo.boxCapacity)/15)
                                      }, {
                                        yAxis: 3000 * (Number(that.boxInfo.boxCapacity)/15)
                                      }, {
                                        yAxis: 4000 * (Number(that.boxInfo.boxCapacity)/15)
                                      }, {
                                        yAxis: 5000 * (Number(that.boxInfo.boxCapacity)/15)
                                    }]
                                }
                            }()
                          }
                        }]
                      }
                    that.$nextTick(() => {
                        if(document.getElementById('charts')) {
                            var myChart = echarts.init(document.getElementById('charts'));
                            myChart.setOption(option);
                        }
                    })
                }
            })
        },
        //通过位置id获取电箱
        getElectricBox: function (x, id) {
            this.loading = true;
            var that = this;
            that.nodeId = id;
            var apiUrl = api.API_GET_SWITCHBOX + "?locationId=" + id + '&projectId=' + that.currentProject.id;
            util.ajax(apiUrl, "get", function (data) {
                that.deviceBoxArr = data;
                if (x == 0) {
                    if (data.length > 0) {
                        that.show = true;
                        that.deviceBoxNum = data[0].deviceBoxNum; //默认第一个电箱
                        that.currentBoxId = data[0].id
                        that.switchBox(data[0].deviceBoxNum, data[0].id);
                    } else {
                        that.loading = false;
                        that.deviceBoxNum = "";
                        that.allSwitches = [];
                        that.show = false;
                       // that.$message('抱歉，当前位置暂时没有电箱');
                    }
                }
            })
        },
        resetValue(){
            this.getElectricBox(0, this.nodeId);
        },
        //选择电箱时候触发 获取对应得线路地址
        switchBox(mac, id) {
            this.deviceBoxNum = mac; // 匹配激活得电箱
            var that = this;
            this.selectAddressIndex = 0;
            var apiUrl = api.API_GET_ADDRESS + "?deviceBoxMac=" + mac + '&projectId=' + that.currentProject.id;
            var alarmUrl = api.API_GET_ALARMS + "?deviceBoxId=" + id;
            util.ajax(apiUrl, "get", function (data) {
                that.loading = false;
                if(data.switchList.length) {
                    that.show = true;
                    for (var i = 0; i < data.switchList.length; i++) {
                        data.switchList[i].deviceSwitchStatus = (data.switchList[i].deviceSwitchStatus == 'false') ? true : false;
                    }
                    that.allSwitches = data.switchList;
                    that.boxInfo = data;
                    that.switchInfo = data.switchList[0];
                    that.deviceSwitchStatus = data.switchList[0].deviceSwitchStatus;
                } else {
                    //that.$message('抱歉,当前电箱暂无线路返回');
                    that.show = false;
                }
            }) //获取电箱实时状态
            util.ajax(alarmUrl, "get", function (data) {
                that.tableData = data;
            })
        },
        switchChange: function () {
            let item = this.switchInfo;
            /*if(JSON.parse(document.cookie).loginName == "icbms"){*/
            var _this = this;
            var control = (item.deviceSwitchStatus == true) ? "close" : "open",
                address = item.address;
            var apiUrl = api.API_CONTROL + "?cmd=" + control + "&deviceBoxMac=" + this.deviceBoxNum + "&switchAddStrs=" + address
            var chinase = (control == "open") ? "打开" : "关闭";
            this.$confirm('此操作将' + chinase + '开关, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                util.ajax(apiUrl, "get", function (data) {
                    if (data == 0) {
                        item.deviceSwitchStatus = !item.deviceSwitchStatus;
                        _this.$message({
                            type: 'success',
                            message: chinase + '成功!'
                        });
                    }
                })

            }).catch(() => {
                _this.$message({
                    type: 'info',
                    message: '已取消操作',
                });
                _this.deviceSwitchStatus = this.switchInfo.deviceSwitchStatus;
            });
        },
        change(value){
            if(value == 2) { // 全部
                ztree2 = $.fn.zTree.init($("#allLocationinfoTree"), setting2, this.locationList).expandAll(true);
                new MtrSearchZTree();
            } else {
                this.filterTree(value);
            }
        },
        filterTree(values){
            if(values == 1) {
                values = null
            }
            let arr = [],arr1 = [],arr2 = [];
            for(let i=0; i< this.locationList.length; i++){
                let isparent = false;
                for(let j=0;j < this.locationList.length; j++) {
                    if(this.locationList[i].id == this.locationList[j].parentId){
                        isparent = true;
                        break;
                    }
                }
                if(isparent) {
                    arr.push(this.locationList[i]); // 所有父节点
                } else { 
                    arr1.push(this.locationList[i]); // 所有子节点
                }
            }
            arr1.forEach((value) => {
                if(value.online == values) {
                    arr2.push(value);
                }
            })
            ztree2 = $.fn.zTree.init($("#allLocationinfoTree"), setting2, arr2.concat(arr) ).expandAll(true);
            new MtrSearchZTree();
        },
        allcon: function (handle) {
            /* if(JSON.parse(document.cookie).loginName == "icbms"){*/
            var _this = this;
            var address = "";
            for (i = 0; i < this.allSwitches.length; i++) {
                if (i != this.allSwitches.length - 1) {
                    address += this.allSwitches[i].address + ',';
                } else {
                    address += this.allSwitches[i].address;
                }
            }
            if (address == "") {
                address = "1"
            };
            var apiUrl = api.API_CONTROL + "?cmd=" + handle + "&deviceBoxMac=" + this.deviceBoxNum + "&switchAddStrs=" + address;

            var chinase = (handle == "open") ? "打开" : "关闭";
            this.$confirm('此操作将' + chinase + '当前电箱所有开关, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                util.ajax(apiUrl, "get", function (data) {
                    if (data == 0) {
                        _this.$message({
                            type: 'success',
                            message: chinase + '成功!'
                        });
                        for (i = 0; i < _this.allSwitches.length; i++) {
                            if (handle == "open") {
                                _this.allSwitches[i].deviceSwitchStatus = true;
                            } else {
                                _this.allSwitches[i].deviceSwitchStatus = false;
                            }
                        }
                    }
                })
            }).catch(() => {
                _this.$message({
                    type: 'info',
                    message: '已取消操作'
                });
            });
        }
    },

    mounted: function () {

    },

    computed: {},

    updated: function () {

    }
});