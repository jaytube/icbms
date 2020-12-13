$(function() {
	var control = {
		init_page: function() {
			control.initVue();
		},
		initVue: function() {
			var app = new Vue({
				el: "#app",
				data: {		
					currentProject:{},
					projectId:"", //项目id
					locationId:"", //位置id
					switchBoxId:"", // 电箱id									
					projectInfoList:[], //项目列表
					locationInfos:[], // 位置列表
					switchBoxList:[], //电箱列表
					initarr: [1, 2, 3, 4, 5, 6, 7, 8], // 初始化线路
					alladdress: [], //所有线路
					selectaddress: [], // 选中线路
					selectstatus: [], // 选中线路状态
					allbox: [], // 所有线路
					status: true, // 多个开关状态
					initgen:false//总路是否加载
				},
				created: function() {
					var _this = this;
					$.ajax({
						type: "POST",
		 			    url: "../projectinfo/getCurrentProject", 			  
		 			    dataType: "json",
		 			    cache:false, 
		 			    async:false, 
						success: function(data) {												
							if (data.code  == 0) {	
								_this.currentProject = data.currentProject;
								_this.locationInfos = data.locInfos;
							}
						}
					})
				},
				methods: {
					newselect: function(e,address,state) {
						Array.prototype.remove = function(val) {
							var index = this.indexOf(val);
							if (index > -1) {
								this.splice(index, 1);
							}
						};
						var _this = this;
						if ($(e.currentTarget).css("border") == "2px solid rgb(27, 159, 148)") {
							$(e.currentTarget).css("border", "none")
							console.log("取消选中");
							_this.selectaddress.remove(address)
							_this.selectstatus.remove(state)
						} else {
							$(e.currentTarget).css("border", "2px solid #1b9f94")
							console.log("选中")
							_this.selectaddress.push(address)
							_this.selectstatus.push(state)
						}
						console.log(_this.selectaddress)
						console.log(_this.selectstatus)
						var newarr = [_this.selectstatus[0]];
						for (i = 0; i < _this.selectstatus.length; i++) {
							var repeat = false;
							for (j = 0; j < newarr.length; j++) {
								if (_this.selectstatus[i] == newarr[j]) {
									repeat = true;
									break;
								}
							}
							if (repeat == false) {
								newarr.push(_this.selectstatus[i])
							}
						}
						if (newarr.length == 2) {
							alert("请选择相同得状态")
							$(e.currentTarget).css("border", "none")
							console.log("取消选中");
							_this.selectaddress.remove(address)
							_this.selectstatus.remove(state)
						} else {
							if (newarr[0] == "true") {
								$(".c_white").animate({
									left: '32px'
								}, 500)
								$(".c_box").css("backgroundColor", "#1b9f94")
							} else if (newarr[0] == "false") {
								$(".c_white").animate({
									left: '2px'
								}, 500);
								$(".c_box").css("backgroundColor", "#989898")
							}
						}
					},
					getlocationInfos : function(id,cb){	
						//alert(id);				
						$.ajax({
							url: "../locationinfo/findLocInfoByPId",
							type: "POST",
							dataType: "json",
							contentType:"application/json;charset=utf-8",
							data:$.trim(id),		
							cache:false, 
			 			    async:false, 
							success: function(data) {
								console.log(data);
								if (data.code == 0) {									
									cb&&cb(data);
								}
							}
						})
					},
					getswitchBoxList:function(id,cb){
						//alert(id);
						$.ajax({
							url: "../deviceboxinfo/findDeviceBoxsInfoByLId",
							type: "post",
							dataType: "json",
							contentType:"application/json;charset=utf-8",
							data:$.trim(id),		
							success: function(data) {
								console.log(data);
								if (data.code == 0) {
									// this.locationInfos = data.locInfos;
									cb&&cb(data);
								}
							}
						})
					},
					getbox:function(id){
						var _this = this;
						$.ajax({
							url: "../KK/getBoxChannelsRealData",
							type: "POST",
							dataType: "json",
							contentType:"application/json;charset=utf-8",
							data: id,
							cache:false, 
			 			    async:false, 
							success: function(data) {
								console.log(data);
								if (data.code == 0) {
									if(data.resultList.length != 0){
										_this.initgen = true;
										if (data.resultList.length >= 9) {
											_this.initarr = [];
										} else {
											_this.initarr = [];
											if(data.resultList.length == 0){
												_this.initarr = [1,2,3,4,5,6,7,8]
											}else{
												for (i = data.resultList.length; i < 9; i++) {
													_this.initarr.push(i)
													console.log(_this.initarr)
												}
											}
										}
										_this.allbox = data.resultList;
										for (j = 0; j < data.resultList.length; j++) {
											_this.alladdress.push(data.resultList[j].address)
										}
										console.log(_this.initgen)
									}
								}
							}
						})
					},
					li_con: function(e, address) { //单体控制
						event.stopPropagation();
						var _this = this;
						var param = {
							cmd: "close",
							deviceBoxMac: _this.switchBoxId, //电箱id
							switchAddStrs: address,// 线路
						}
						if ($(e.currentTarget).find($(".li_white")).css("left") == "26px") {
							$.ajax({
								url: "../KK/setCmdSwitch",
								type: "post",
								dataType: "json",
								async:false,
								data: {
									cmd: "close",
									deviceBoxMac: _this.switchBoxId, //电箱id
									switchAddStrs: address,
								},
								success: function(data) {
									console.log(data);
									if (data.code == "0") {
										if(address != "1"){
											$(e.currentTarget).find($(".li_white")).removeClass(".li_open").addClass("li_close")
					                        $(e.currentTarget).removeClass("li_gren").addClass("li_grey")
					                        $(e.currentTarget).parent().parent().addClass("li_grey").removeClass("li_green").addClass(".close_li")
					                        $(e.currentTarget).parent().parent().find($(".span1")).css("backgroundColor", "red")
					                        $(e.currentTarget).parent().parent().find($(".span2")).css("backgroundColor", "red")
					                        $(e.currentTarget).parent().parent().find($(".span3")).text("已断")
					                        $(e.currentTarget).parent().parent().find($(".images")).css("backgroundColor", "#989898")
					                        var boxindex = Number(address) -1;
					                        _this.allbox[boxindex].deviceSwitchStatus = "false";
										}else{
											_this.allbox[0].deviceSwitchStatus = "false";
										}
									} else {
										alert("未关闭成功")
									}
								}
							})
						} else {
							$.ajax({
								url: "../KK/setCmdSwitch",
								type: "post",
								dataType: "json",
								async:false,
								data: {
									cmd: "open",
									deviceBoxMac: _this.switchBoxId, //电箱id
									switchAddStrs: address, //线路
								},
								success: function(data) {
									if (data.code == 0) {
										if(address != "1"){
                                        // _this.onChange(2);
											$(e.currentTarget).find($(".li_white")).removeClass(".li_open").addClass("li_close")
											$(e.currentTarget).removeClass("li_gren").addClass("li_grey")
											$(e.currentTarget).parent().parent().addClass("li_grey").removeClass("li_green").addClass(".close_li")
											$(e.currentTarget).parent().parent().find($(".span1")).css("backgroundColor", "green")
											$(e.currentTarget).parent().parent().find($(".span2")).css("backgroundColor", "green")
											$(e.currentTarget).parent().parent().find($(".span3")).text("已通")
											$(e.currentTarget).parent().parent().find($(".images")).css("backgroundColor", "#1b9f94")
											var boxindex = Number(address) -1;
				                            _this.allbox[boxindex].deviceSwitchStatus = "true";
										}else{
											_this.allbox[0].deviceSwitchStatus = "true";
										}
									}
								}
							})
						}
					},
					allcontro: function(state) {
						var _this = this;
						  var string1 = "";
                          for(n=0;n<_this.alladdress.length;n++){
                             var last = _this.alladdress.length - 1;
                                   if(n!= last){
                                        string1 += _this.alladdress[n]+","
                                   }else if(n == last){
                                        string1 += _this.alladdress[n]
                                  }
                         }
                         console.log(string1)
						var param = {
							cmd: state, //开关状态
							deviceBoxMac: this.switchBoxId, //电箱id
							switchAddStrs: string1, //开关总闸
						};
						$.ajax({
							url: "../KK/setCmdSwitch",
							type: "post",
							async:false,
							dataType: "json",
							data: {
								cmd: state, //开关状态
								deviceBoxMac: this.switchBoxId, //电箱id
								switchAddStrs: string1, //1是总闸_this.alladdress所有线路
							},
							success: function(data) {
								if (state == "close") { //一键关闭
									for (i = 0; i < _this.alladdress.length; i++) {
										var index = Number(_this.alladdress[i]) - 2;
										var boxindex = Number(_this.alladdress[i]) -1;
										$("ul li").eq(index).find($(".li_white")).removeClass(".li_open").addClass("li_close")
										$("ul li").eq(index).find(".li_box").removeClass("li_gren").addClass("li_grey")
										$("ul li").eq(index).find($(".find")).addClass("li_grey").removeClass("li_green").addClass(".close_li")
										$("ul li").eq(index).find($(".span1")).css("backgroundColor", "red")
										$("ul li").eq(index).find($(".span2")).css("backgroundColor", "red")
										$("ul li").eq(index).find($(".span3")).text("已断")
										$("ul li").eq(index).find($(".images")).css("backgroundColor", "#989898")
										_this.allbox[boxindex].deviceSwitchStatus = "false";
									}
								} else { //一键开
									for (n = 0; n < _this.alladdress.length; n++) {
										var index = Number(_this.alladdress[n]) - 2;
										if(index == -1){
											index = ""
										}
										var boxindex = Number(_this.alladdress[n]) -1;
										$("ul li").eq(index).find($(".li_white")).removeClass(".li_close").addClass("li_open")
										$("ul li").eq(index).find(".li_box").removeClass("li_grey").addClass("li_green")
										$("ul li").eq(index).find($(".find")).addClass("li_green").removeClass("li_grey").removeClass(".close_li")
										$("ul li").eq(index).find($(".span1")).css("backgroundColor", "green")
										$("ul li").eq(index).find($(".span2")).css("backgroundColor", "green")
										$("ul li").eq(index).find($(".span3")).text("已通")
										$("ul li").eq(index).find($(".images")).css("backgroundColor", "#1b9f94")
										_this.allbox[boxindex].deviceSwitchStatus = "true";
									}
								}
							}
						})
					},
					lotContro: function() { // 一键多个控制
						var _this = this;
						if (_this.selectstatus.length > 0) {
							console.log(_this.selectstatus[0])
							if (_this.selectstatus[0] == "true") { //目前状态是开
								var string = ""
								for(x=0;x<this.selectaddress.length;x++){
									 var last = this.selectaddress.length - 1;
                                     if(x != last){
                                          string += this.selectaddress[x]+","
                                     }else if(x == last){
                                          string += this.selectaddress[x]
                                     }
								}
								$.ajax({
									url: "../KK/setCmdSwitch",
									type: "post",
									dataType: "json",
									data: {
										cmd:"close", //开关状态
										deviceBoxMac: this.switchBoxId, //电箱id
										switchAddStrs: string, //线路
									},
									success: function(data) {
										if (data.code == 0) {
											_this.status = false; // 关闭成功。目前状态是关闭
											$(".c_white").animate({
												left: '2px'
											}, 500);
											$(".c_box").css("backgroundColor", "#989898")
											for (i = 0; i < _this.selectaddress.length; i++) {
												console.log(2)
												var index = Number(_this.selectaddress[i]) - 2;
												var boxindex = Number(_this.selectaddress[i]) - 1;
												$("ul li").eq(index).find($(".li_white")).removeClass(".li_open").addClass("li_close")
												$("ul li").eq(index).find(".li_box").removeClass("li_gren").addClass("li_grey")
												$("ul li").eq(index).find($(".find")).addClass("li_grey").removeClass("li_green").addClass(".close_li")
												$("ul li").eq(index).find($(".span1")).css("backgroundColor", "red")
												$("ul li").eq(index).find($(".span2")).css("backgroundColor", "red")
												$("ul li").eq(index).find($(".span3")).text("已断")
												$("ul li").eq(index).find($(".images")).css("backgroundColor", "#989898")
												_this.allbox[boxindex].deviceSwitchStatus = "false";
											}
											_this.selectaddress = [];
											_this.selectstatus = [];
											$("ul li").css("border", "none");
										}
									}
								})
							} else if (_this.selectstatus[0] == "false") { //目前状态是关闭
								var string = ""
								for(x=0;x<this.selectaddress.length;x++){
									 var last = this.selectaddress.length - 1;
                                     if(x != last){
                                          string += this.selectaddress[x]+","
                                     }else if(x == last){
                                          string += this.selectaddress[x]
                                     }
								}
								$.ajax({
									url: "../KK/setCmdSwitch",
									type: "post",
									dataType: "json",
									data: {
										cmd:"open" , //开关状态
										deviceBoxMac: _this.switchBoxId, //电箱id
										switchAddStrs: string, //线路数组
									},
									success: function(data) {
										if (data.code == 0) {
											_this.status = true; // 打开成功。目前状态是打开
											$(".c_white").animate({
												left: '32px'
											}, 500)
											$(".c_box").css("backgroundColor", "#1b9f94")
											for (i = 0; i < _this.selectaddress.length; i++) {
												var index = Number(_this.selectaddress[i]) - 2
												var boxindex = Number(_this.selectaddress[i]) - 1;
												$("ul li").eq(index).find($(".li_white")).removeClass(".li_close").addClass("li_open")
												$("ul li").eq(index).find(".li_box").removeClass("li_grey").addClass("li_green")
												$("ul li").eq(index).find($(".find")).addClass("li_green").removeClass("li_grey").removeClass(".close_li")
												$("ul li").eq(index).find($(".span1")).css("backgroundColor", "green")
												$("ul li").eq(index).find($(".span2")).css("backgroundColor", "green")
												$("ul li").eq(index).find($(".span3")).text("已通")
												$("ul li").eq(index).find($(".images")).css("backgroundColor", "#1b9f94")
												_this.allbox[boxindex].deviceSwitchStatus = "true";
											}
											_this.selectaddress = [];
											_this.selectstatus = [];
											$("ul li").css("border", "none");
										}
									}
								})
							};
						} else {
							alert("请选择线路")
						}
					},
					Change:function(){
						setInterval(() => { 
				            this.onChange(2);
				        }, 10000)
					},
					onChange: function(x) {
						var _this = this;
						if(x == 0){
							if(this.projectId){
								this.locationId = "";
								this.switchBoxId = "";
								this.locationInfos = [];
								this.switchBoxList = [];
								this.allbox = [];
								this.initgen = false;
								_this.initarr = [1,2,3,4,5,6,7,8];
								this.getlocationInfos(this.projectId,function(data){
									console.log(data)
									if(data.code == 0){
										_this.locationInfos = data.locInfos;
										console.log(_this.locationInfos)
									}
								})
								console.log(_this.initgen)
							}
						}else if(x == 1){
							if(this.locationId){
								this.switchBoxId = "";
								this.switchBoxList = [];
								this.allbox = [];
								this.initgen = false;
								_this.initarr = [1,2,3,4,5,6,7,8];
								if(this.locationId){
									this.getswitchBoxList(this.locationId,function(data){
										console.log(data)
										if(data.code == 0){
											_this.switchBoxList = data.deviceBoxs;
											console.log(_this.switchBoxList)
										}
									})
								}
								console.log(_this.initgen)
							}
						}else if(x == 2){
							if(this.switchBoxId){
								this.allbox = [];
								if (this.switchBoxId) {
									this.getbox(this.switchBoxId)
								}
							}
						}/*else if(x == 3){
							if(this.switchBoxId){
								this.allbox = [];
								if (this.switchBoxId) {									
									this.setUpdateDataTime(this.switchBoxId, "true");
								}
							}
						}*/
					}
				}
			})
		}
	};
	control.init_page();
})