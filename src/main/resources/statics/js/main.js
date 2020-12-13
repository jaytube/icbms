$(function() {
	var box_data = {
		init : function(currentProject) {
			box_data.initChartData(currentProject);
			box_data.getBarDataInfo(currentProject);
			// setInterval(function(){ box_data.initDate(); }, 1000);
			// setInterval(function(){ box_data.getAlarmCounts(currentProject); },
			//   30000);
		},
		getAlarmCounts : function(currentProject) {
			$.ajax({
				url : "app/dashbaord/getAlarmsPage",
				datatype : 'json',
				async : true,
				type : 'get',
				data : {
					pageSize : 5,
					page : 1,
					projectId : currentProject.id
				},
				success : function(data) {
					$("#alarmCnt").text(data.total);
				}
			})
		},
		getBarDataInfo : function(currentProject) {
			// 电箱统计数据
			$.ajax({
				url : "app/dashbaord/getBoxInfoCnt",
				datatype : 'json',
				type : 'get',
				data : {
					projectId : currentProject.id
				},
				success : function(data) {
					$("#boxTotal").text(data.boxTotal);
					$("#switchOnlineTotal").text(data.switchOnlineTotal);
					$("#switchLeaveTotal").text(data.switchLeaveTotal);
				}
			});
		},
		initDate : function() {
			// 日期
			var date = new Date();
			var year = date.getFullYear(); // 获取完整的年份(4位)
			var month = date.getMonth(); // 获取当前月份(0-11,0代表1月)
			var day = date.getDate(); // 获取当前日(1-31)
			$("#nowDate").text(year + "年" + (month + 1) + "月" + day + "日");
			$("#nowXq").text("星期" + ("日一二三四五六".charAt(date.getDay())));

			// 时间
			var hour = date.getHours(); // 获取当前小时数(0-23)
			var minutes = date.getMinutes(); // 获取当前分钟数(0-59)
			if (minutes < 10) {
				minutes = "0" + minutes;
			}
			var seconds = date.getSeconds(); // 获取当前秒数(0-59)
			if (seconds < 10) {
				seconds = "0" + seconds;
			}
			$("#nowTime").text(hour + ":" + minutes + ":" + seconds);
		},
		initChartData : function(currentProject) {
			$.ajax({
				url : "app/dashbaord/getAlarmsStat",
				datatype : 'json',
				type : 'get',
				data : {
					startDate : '2018-05-18',
					projectId : currentProject.id
				},
				success : function(data) {
					// 告警按照日统计
					var dom = document.getElementById("chart-alarm-container");
					var xdata = [];
					var ydata = [];
					if (data.length > 0) {
						for (var i = 0; i < data.length; i++) {
							xdata.push(data[i].type);
							ydata.push(data[i].count);
						}
					}
					box_data.initChart(dom, xdata, ydata, "1");
				}
			});
			$.ajax({
				url : "app/dashbaord/getElecStat",
				datatype : 'json',
				type : 'get',
				data : {
					startDate : '2018-05-18',
					projectId : currentProject.id
				},
				success : function(data) {
					// 用电量按照日统计
					dom = document.getElementById("chart-data-container");
					var xdata = [];
					var ydata = [];
					if (data.length > 0) {
						for (var i = 0; i < data.length; i++) {
							xdata.push(data[i].date);
							ydata.push(data[i].elec);
						}
					}
					box_data.initChart(dom, xdata, ydata, "0");
				}
			})
		},
		initChart : function(dom, xdata, ydata, type) {
			var myChart = echarts.init(dom);
			var app = {};
			var option0 = null;
			var option1 = null;
			app.title = '坐标轴刻度与标签对齐';
			option0 = {
				color : [ '#3398DB' ],
				tooltip : {
					trigger : 'axis',
					axisPointer : { // 坐标轴指示器，坐标轴触发有效
						type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
					}
				},
				grid : {
					left : '3%',
					right : '4%',
					bottom : '3%',
					containLabel : true
				},
				xAxis : [ {
					type : 'category',
					data : xdata,// ['Mon', 'Tue', 'Wed', 'Thu', 'Fri',
					// 'Sat',
					// 'Sun'],
					axisTick : {
						alignWithLabel : true
					},
					axisLabel : {
						show : true,
						textStyle : {
							color : '#28c6de'
						}
					},
					axisLine : {
						lineStyle : {
							color : '#fff'
						}
					}

				} ],
				yAxis : [ {
					type : 'value',
					axisLabel : {
						show : true,
						textStyle : {
							color : '#28c6de'
						}
					},
					axisLine : {
						lineStyle : {
							color : '#fff'
						}
					}
				} ],
				series : [ {
					name : '用电量',
					type : 'bar',
					barWidth : '60%',
					data : ydata
				// [10, 52, 200, 334, 390, 330, 220]
				} ]
			};

			option1 = {
				color : [ '#3398DB' ],
				tooltip : {
					trigger : 'axis',
					axisPointer : { // 坐标轴指示器，坐标轴触发有效
						type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
					}
				},
				grid : {
					left : '3%',
					right : '4%',
					bottom : '3%',
					containLabel : true
				},
				xAxis : [ {
					type : 'category',
					data : xdata,// ['Mon', 'Tue', 'Wed', 'Thu', 'Fri',
					// 'Sat',
					// 'Sun'],
					axisTick : {
						alignWithLabel : true
					},
					axisLabel : {
						interval : 0,
						rotate : 40,
						show : true,
						textStyle : {
							color : '#28c6de'
						}
					},
					axisLine : {
						lineStyle : {
							color : '#fff'
						}
					}

				} ],
				yAxis : [ {
					type : 'value',
					axisLabel : {
						show : true,
						textStyle : {
							color : '#28c6de'
						}
					},
					axisLine : {
						lineStyle : {
							color : '#fff'
						}
					}
				} ],
				series : [ {
					name : '数量',
					type : 'bar',
					barWidth : '60%',
					data : ydata
				// [10, 52, 200, 334, 390, 330, 220]
				} ]
			};

			if (type == "0") {
				if (option0 && typeof option0 === "object") {
					myChart.setOption(option0, true);
				}
			} else if (type == "1") {
				if (option1 && typeof option1 === "object") {
					myChart.setOption(option1, true);
				}
			}
        },

        //获取告警数据
        getAlarms:function(_this){
            $.ajax({
				url : "app/dashbaord/getAlarmsPage",
				datatype : 'json',
				async : true,
				type : 'get',
				data : {
					pageSize : 5,
					page : 1,
					projectId : _this.currentProject.id
				},
				success : function(data) {
					_this.slidecont = data.dataList;
				}
			})
        },

        //获取实时电箱状态
        getBoxRelData:function(_this){
            $.ajax({
				url : "app/dashbaord/getSwitchesPage",
				datatype : 'json',
				async : true,
				type : 'get',
				data : {
					pageSize : 5,
					page : 1,
					projectId : _this.currentProject.id
				},
				success : function(data) {
					$("#alarmCnt").text(data.total);
					_this.switchcont = data.dataList;
				}
			})
        }
	};

	var data = new Vue({
		el : "#data-wrap",
		data : {
			currentProject : {},
			swiperData : [],
			swiperIndex : "0",// 初始化swiper的index
			slidecount : [ 1 ],// 默认1页
			slidecont : [],
			switchcount : [ 1 ],
			switchcont : []
		},
		created : function() {
			var _this = this;
			$.ajax({
				type : "POST",
				url : "projectinfo/getCurrentProject",
				dataType : "json",
				cache : false,
				async : false,
				success : function(data) {
					if (data.code == 0) {
						_this.currentProject = data.currentProject;
					}
				}
			})
            box_data.init(_this.currentProject);
            box_data.getAlarms(_this);
			box_data.getBoxRelData(_this);
		},
		mounted : function() {
			$("#mapframe").width($("#map-main").width());
            $("#mapframe").height($("#map").height() * 0.9);
            var _this = this;
            // setInterval(function(){
            //     box_data.getAlarms(_this);
			//     box_data.getBoxRelData(_this);
            // },30000)
		},
		methods : {

		}
	});
});