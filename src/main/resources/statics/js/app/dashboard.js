$(function() {	
	var box_data = {
		init: function() {
			// box_data.initMap();
			box_data.initChartData();
			box_data.getBarDataInfo();
			/*setInterval(function(){
				box_data.initDate();
			}, 1000);
			setInterval(function(){
				box_data.getAlarmCounts();
			}, 60000);*/
		},
		getAlarmCounts:function(){
			$.ajax({
				url:"../app/dashbaord/getAlarmsPage",
				datatype:'json',
				async:false,
				type:'get',
				data:{
					startTime:'2018-05-18 00:00:00',
					endTime:'2018-10-01 00:00:00',
					pageSize:5,
					page:0
				},
				success:function(data){
					$("#alarmCnt").text(data.total);
				}
			})
		},
		getBarDataInfo:function(){
			// 电箱统计数据
			$.ajax({
				url:"../app/dashbaord/getBoxInfoCnt",
				datatype:'json',
				type:'get',
				data:{
					projectId:'6cb926fdfa8a4ba998f4c4025969225a'
				},
				success:function(data){
					$("#boxTotal").text(data.boxTotal);
					$("#switchOnlineTotal").text(data.switchOnlineTotal);
					$("#switchLeaveTotal").text(data.switchLeaveTotal);
				}
			});
		},
		initDate:function(){
			// 日期
			var date = new Date();
			var year = date.getFullYear(); // 获取完整的年份(4位)
			var month = date.getMonth(); // 获取当前月份(0-11,0代表1月)
			var day = date .getDate(); // 获取当前日(1-31)
			$("#nowDate").text(year+"年"+(month+1)+"月"+day+"日");
			$("#nowXq").text("星期"+("日一二三四五六".charAt(date.getDay())));
			
			// 时间
			var hour = date.getHours(); // 获取当前小时数(0-23)
			var minutes = date.getMinutes(); // 获取当前分钟数(0-59)
			var seconds = date.getSeconds(); // 获取当前秒数(0-59)
			
			$("#nowTime").text(hour+":"+minutes+":"+seconds);
		},
		initChartData:function(){
			$.ajax({
				url:"../app/dashbaord/getAlarmsStat",
				datatype:'json',
				type:'get',
				data:{
					startDate:'2018-05-18'
				},
				success:function(data){
					// 告警按照日统计
					var dom = document.getElementById("chart-alarm-container");
					var xdata = [];
					var ydata = [];
					if(data.length>0){
						for(var i=0; i<data.length; i++){
					
							xdata.push(data[i].type);
							ydata.push(data[i].count);
						}
					}
					
					box_data.initChart(dom, xdata, ydata,"1");
				}
			});
			$.ajax({
				url:"../app/dashbaord/getElecStat",
				datatype:'json',
				type:'get',
				data:{
					startDate:'2018-05-18'
				},
				success:function(data){
					// 用电量按照日统计
					dom = document.getElementById("chart-data-container");
					var xdata = [];
					var ydata = [];
					if(data.length>0){
						for(var i=0; i<data.length; i++){
							xdata.push(data[i].date);
							ydata.push(data[i].elec);
						}
					}
					box_data.initChart(dom, xdata, ydata,"0");
				}
			})
		},
//		initMap: function(){
//			$(document).ready(function() {
//				var canvas = document.getElementById('canvas');
//				canvas.width = $("#map-main").width();
//				canvas.height = $("#map-main").height();
//
//				var stage = new JTopo.Stage(canvas);
//				// 显示工具栏
//				showJTopoToobar(stage);
//				var scene = new JTopo.Scene(stage);
//				scene.background = '../images/dishini.jpg';
//				scene.mode = "select"; // scene不能被拖动
//				scene.areaSelect = false;
//
//
//				function createNode(index,name, x, y,deviceBoxNum) { // index是电箱序号
//					var circleNode = new JTopo.CircleNode(index);
//					circleNode.radius = 8; // 半径
//					circleNode.alpha = 0.8; // 透明度
//					circleNode.fontColor = "0,0,0";
//					circleNode.fillColor = '255, 255, 255'; // 填充颜色
//					circleNode.setLocation(x, y); // 电箱初始化位置
//					circleNode.textPosition = 'Middle_Center'; // 文本位置
//					circleNode.textOffsetY = -2;
//					circleNode.tip = name;
//					circleNode.mouseover(function(){
//	                    this.text = this.tip;
//	                    circleNode.textOffsetY = 14;
//	                    circleNode.fontColor = "255,255,255";
//	                });
//					circleNode.mouseout(function(){
//	                    this.text = index;
//	                    circleNode.textOffsetY = -2;
//	                    circleNode.fontColor = "0,0,0";
//	                });
//					scene.add(circleNode);
//					circleNode.click(function(event) {
//                    $.ajax({
//                           url:"../app/dashbaord/getRealDataByBoxMac?deviceBoxMac="+deviceBoxNum,
//                           type:'get',
//                          success:function(data){   
//                        	  $("#switchRealData").empty();
//                        	  if(data.length>0){
//                        		  for(var i=0; i<data.length; i++){
//                        			  var str = "<tr>"
//                        			  			+"<td>"+data[i].deviceSwitchName+"</td>"
//                        			  			+"<td>"+data[i].switchVoltage+"</td>"
//                        			  			+"<td>"+data[i].switchElectric+"</td>"
//                        			  			+"<td>"+data[i].switchTemperature+"</td>"
//                        			  			+"<td>"+data[i].switchLeakage+"</td>"
//                        			  			+"<td>"+data[i].totalPower+"</td></tr>";
//                        			  $("#switchRealData").append(str);
//                        		  }
//                        	  }
//                                  
//                          }
//                     });
//						var x = circleNode.x + 24,
//							y = circleNode.y + 24
//					});
//				}
//				$.ajax({
//                    url:"../app/dashbaord/getAllBoxInfos?projectId=6cb926fdfa8a4ba998f4c4025969225a",
//                    type:'get',
//                    success:function(data){
//					var w = $("#map-main").width(),h=$("#map-main").height();
//					
//                	for(i=0;i<data.length;i++){
//                			createNode(data[i].deviceBoxName,data[i].locationName, (i%6+1)*(w/8)+8, (Math.floor(i/6)+1)*(h/5),data[i].deviceBoxNum);        
//                    	}
//                   	}
//               	});
//				
//			});
//		},
		
		initMap:function(){
			$(document).ready(function(){                    
	            var canvas = document.getElementById('canvas');
                     canvas.width = $("#map-main").width();
		  canvas.height = $("#map-main").height();
	            var stage = new JTopo.Stage(canvas);
	            //显示工具栏
	            showJTopoToobar(stage);
	            var scene = new JTopo.Scene();
	            stage.add(scene);
	            scene.background = '../images/dishini.jpg';
                     scene.mode = "select"; // scene不能被拖动
		   scene.areaSelect = false;
		       function createNode(index,name, x, y,deviceBoxNum){
		    	   var cloudNode = new JTopo.Node('root');
		            cloudNode.setSize(50, 26);
		            cloudNode.setLocation(x,y); 
		            cloudNode.fillColor = '255, 255, 255'; // 填充颜色
		            cloudNode.layout = {type: 'tree', width:30, height: 45}
		            cloudNode.text= index+'号箱';
		            cloudNode.fontColor = "0,0,0";
		            cloudNode.textOffsetY = -20;
		            scene.add(cloudNode);
		            cloudNode.click(function(event) {
	                    $.ajax({
	                           url:"../app/dashbaord/getRealDataByBoxMac?deviceBoxMac="+deviceBoxNum,
	                           type:'get',
	                          success:function(data){   
	                        	  $("#switchRealData").empty();
	                        	  if(data.length>0){
	                        		  for(var i=0; i<data.length; i++){
	                        			  var str = "<tr>"
	                        			  			+"<td>"+data[i].deviceSwitchName+"</td>"
	                        			  			+"<td>"+data[i].switchVoltage+"</td>"
	                        			  			+"<td>"+data[i].switchElectric+"</td>"
	                        			  			+"<td>"+data[i].switchTemperature+"</td>"
	                        			  			+"<td>"+data[i].switchLeakage+"</td>"
	                        			  			+"<td>"+data[i].totalPower+"</td></tr>";
	                        			  $("#switchRealData").append(str);
	                        		  }
	                        	  }
	                                  
	                          }
	                     });
					});
		            var arr = name.split("/");
		            for(var i=0; i<arr.length; i++){
		                var node = new JTopo.CircleNode(arr[i]);
		                node.fillStyle = '255,255,255';
		                node.fillColor = '255, 255, 255'; // 填充颜色
		                node.radius = 10;
		                node.setLocation(scene.width * Math.random(), scene.height * Math.random());
		                node.layout = {type: 'tree', width:50, height: 100};
		                
		                scene.add(node);                                
		                var link = new JTopo.Link(cloudNode, node);
		                link.strokeColor='255,255,255'
		                scene.add(link);
		                
		            }
		            JTopo.layout.layoutNode(scene, cloudNode, true);
		            
		            scene.addEventListener('mouseup', function(e){
		                if(e.target && e.target.layout){
		                    JTopo.layout.layoutNode(scene, e.target, true);    
		                }                
		            });
		       };
		       $.ajax({
                 url:"../app/dashbaord/getAllBoxInfos?projectId=6cb926fdfa8a4ba998f4c4025969225a",
                 type:'get',
                 success:function(data){
					var w = $("#map-main").width(),h=$("#map-main").height();
					
             	     for(i=0;i<data.length;i++){
             	    	 if(i==0){
             	    		createNode(data[i].deviceBoxName,data[i].locationName,w*0.15,h*0.07,data[i].deviceBoxNum);
             	    	 }else if(i==1){
             	    		createNode(data[i].deviceBoxName,data[i].locationName,w*0.39,h*0.07,data[i].deviceBoxNum);
             	    	 } else if(i==2){
             	    		createNode(data[i].deviceBoxName,data[i].locationName,w*0.55,h*0.07,data[i].deviceBoxNum);
             	    	 }else if(i==3){
             	    		createNode(data[i].deviceBoxName,data[i].locationName,w*0.05,h*0.35,data[i].deviceBoxNum);
             	    	 }else if(i == 4){
             	    		createNode(data[i].deviceBoxName,data[i].locationName,w*0.78,h*0.07,data[i].deviceBoxNum);
             	    	 }else if(i==5){
             	    		createNode(data[i].deviceBoxName,data[i].locationName,w*0.18,h*0.35,data[i].deviceBoxNum);
             	    	 }else if(i==6){
             	    		createNode(data[i].deviceBoxName,data[i].locationName,w*0.3,h*0.35,data[i].deviceBoxNum);
             	    	 }else if(i==7){
             	    		createNode(data[i].deviceBoxName,data[i].locationName,w*0.63,h*0.35,data[i].deviceBoxNum);
             	    	 }else if(i==8){
             	    		createNode(data[i].deviceBoxName,data[i].locationName,w*0.03,h*0.65,data[i].deviceBoxNum);
             	    	 }else if(i==9){
             	    		createNode(data[i].deviceBoxName,data[i].locationName,w*0.15,h*0.65,data[i].deviceBoxNum);
             	    	 }else if(i==10){
             	    		createNode(data[i].deviceBoxName,data[i].locationName,w*0.27,h*0.65,data[i].deviceBoxNum);
             	    	 }else if(i==11){
             	    		createNode(data[i].deviceBoxName,data[i].locationName,w*0.48,h*0.65,data[i].deviceBoxNum);
             	    	 }else if(i==12){
             	    		createNode(data[i].deviceBoxName,data[i].locationName,w*0.65,h*0.65,data[i].deviceBoxNum);
             	    	 }else if(i==13){
             	    		createNode(data[i].deviceBoxName,data[i].locationName,w*0.8,h*0.65,data[i].deviceBoxNum);
             	    	 }
                 	}
                  }
               });
	        });
		},
		initChart: function(dom, xdata, ydata,type) {
			var myChart = echarts.init(dom);
			var app = {};
			var option0 = null;
			var option1 = null;
			app.title = '坐标轴刻度与标签对齐';

			option0 = {
				color: ['#3398DB'],
				tooltip: {
					trigger: 'axis',
					axisPointer: { // 坐标轴指示器，坐标轴触发有效
						type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
					}
				},
				grid: {
					left: '3%',
					right: '4%',
					bottom: '3%',
					containLabel: true
				},
				xAxis: [{
					type: 'category',
					data: xdata,// ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat',
								// 'Sun'],
					axisTick: {
						alignWithLabel: true
					},
					axisLabel : {
						show : true,
						textStyle : {
						color : '#28c6de'
						}
					},
					axisLine:{
						lineStyle:{
							color:'#fff'
						}
					}
					
				}],
				yAxis: [{
					type: 'value',
					axisLabel : {
						show : true,
						textStyle : {
						color : '#28c6de'
						}
					},
					axisLine:{
						lineStyle:{
							color:'#fff'
						}
					}
				}],
				series: [{
					name: '用电量',
					type: 'bar',
					barWidth: '60%',
					data: ydata// [10, 52, 200, 334, 390, 330, 220]
				}]
			};
			
			option1 = {
					color: ['#3398DB'],
					tooltip: {
						trigger: 'axis',
						axisPointer: { // 坐标轴指示器，坐标轴触发有效
							type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
						}
					},
					grid: {
						left: '3%',
						right: '4%',
						bottom: '3%',
						containLabel: true
					},
					xAxis: [{
						type: 'category',
						data: xdata,// ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat',
									// 'Sun'],
						axisTick: {
							alignWithLabel: true
						},
						axisLabel : {
							interval:0,  
							rotate:40,
							show : true,
							textStyle : {
							color : '#28c6de'
							}
						},
						axisLine:{
							lineStyle:{
								color:'#fff'
							}
						}
						
					}],
					yAxis: [{
						type: 'value',
						axisLabel : {
							show : true,
							textStyle : {
							color : '#28c6de'
							}
						},
						axisLine:{
							lineStyle:{
								color:'#fff'
							}
						}
					}],
					series: [{
						name: '数量',
						type: 'bar',
						barWidth: '60%',
						data: ydata// [10, 52, 200, 334, 390, 330, 220]
					}]
				};
			
			if(type == "0"){
				if (option0 && typeof option0 === "object") {
					myChart.setOption(option0, true);
				}
			} else if (type == "1"){
				if (option1 && typeof option1 === "object") {
					myChart.setOption(option1, true);
				}
			}
		}
	};

	var data = new Vue({
		el:"#data-wrap",
		data:{
			swiperData:[],
			swiperIndex:"0",// 初始化swiper的index
			slidecount:[1,2,3],// 默认三页
			slidecont:[],
			switchcount:[1,2,3],
			switchcont:[]
		},
		created:function(){
			box_data.init();
			var _this=this;
			// 告警表格
			$.ajax({
				url:"../app/dashbaord/getAlarmsPage",
				datatype:'json',
				async:false,
				type:'get',
				data:{
					startTime:'2018-05-18 00:00:00',
					endTime:'2018-10-01 00:00:00',
					pageSize:5,
					page:0
				},
				success:function(data){
			 		_this.slidecont=data.dataList
					var arr = [];
			 		var totalPage = 0;
			 		if(data.totalPage>4){
			 			totalPage=4;
			 		}else{
			 			totalPage = data.totalPage;
			 		}
					for(i=0;i<totalPage;i++){
					 	arr.push(i);
					}
				 	_this.slidecount = arr;// 动态设置 swiper的页数
				}
			}) 
			// 实时数据表格
			$.ajax({
				url:"../app/dashbaord/getSwitchesPage",
				datatype:'json',
				async:false,
				type:'get',
				data:{
					startTime:'2018-05-18 00:00:00',
					endTime:'2018-10-01 00:00:00',
					pageSize:5,
					page:0
				},
				success:function(data){
					$("#alarmCnt").text(data.total);
			 		_this.switchcont=data.dataList
					var arr1 = [];
					for(i=0;i<data.totalPage;i++){
					 	arr1.push(i);
					}
				 	_this.switchcount = arr1;// 动态设置 swiper的页数
				}
			}) 
		},
		mounted:function(){
			var _this = this;
			var mySwiper = new Swiper('.swiper-container2', {
			         //loop:true,
				autoplay:60000,
				onSlideChangeStart:function(){
					_this.slidecont={}
                                             
				},
				onSlideChangeEnd:function(){
				    $.ajax({
					 	url:"../app/dashbaord/getAlarmsPage",
				 	         datatype:'json',
					 	type:'get',
					 	async:false,
					 	data:{
					 		startTime:'2018-05-18 00:00:00',
					 		endTime:'2018-10-01 00:00:00',
					 		pageSize:5,
					 		page:mySwiper.realIndex
					 	},
					 	success:function(data){
							 _this.slidecont = data.dataList;
					 	}
					 });
				}
			})	
			var secondSwiper = new Swiper('.swiper-container1', {
			    //loop:true,
				autoplay:60000,
				onSlideChangeStart:function(){
					_this.switchcont={}
				},
				onSlideChangeEnd:function(){
				    $.ajax({
					 	url:"../app/dashbaord/getSwitchesPage",
				 	    datatype:'json',
					 	type:'get',
					 	async:false,
					 	data:{
					 		startTime:'2018-05-18 00:00:00',
					 		endTime:'2018-10-01 00:00:00',
					 		pageSize:5,
					 		page:secondSwiper.realIndex
					 	},
					 	success:function(data){
							 _this.switchcont = data.dataList;
					 	}
					 });
				}
			})
		},
		methods:{

		}
	});
});