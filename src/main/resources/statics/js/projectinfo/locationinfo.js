var setting = {
	data : {
		simpleData : {
			enable : true,
			idKey : "id",
			pIdKey : "parentId",
			rootPId : -1
		},
		key : {
			url : "nourl"
		}
	}
};

var setting2 = {
	data : {
		simpleData : {
			enable : true,
			idKey : "id",
			pIdKey : "parentId",
			rootPId : -1
		},
		key : {
			url : "nourl"
		}
	},
	check : {
		// 复选框
		// enable:true,
		nocheckInherit : true
	},
	callback : {
		onClick : zTreeOnClick
	}
};

function zTreeOnClick(event, treeId, treeNode) {
	var url = "../locationinfo/info/" + treeNode.id;
	$.get(url, function(result) {
		vm.locationinfo = result.locationinfo;
		vm.showInfo = true;
		vm.title = '修改位置';
		if(vm.locationinfo.fileName!="" && vm.locationinfo.fileName!=null){
			$("#uploadImg").show();
			$("#uploadImg").attr("src","../locationinfo/viewImg/" + vm.locationinfo.fileName);
		}else{
			$("#uploadImg").hide();
		}
		
	})
};
var ztree;
var ztree2;

/**
 * 初始化位置
 */
function initTree() {
	// 加载位置树
	$.get("../locationinfo/initTreeData", function(r) {
		// alert(r.locationList);
		ztree2 = $.fn.zTree.init($("#allLocationinfoTree"), setting2,
				r.locationList);
		// 展开所有节点
		 ztree2.expandAll(true);
	});

	$.get("../locationinfo/initTreeData", function(r) {
		// 初始化zTree，三个参数一次分别是容器(zTree 的容器 className 别忘了设置为 "ztree")、参数配置、数据源
		ztree = $.fn.zTree.init($("#locationinfoTree"), setting, r.locationList);
		var node = ztree.getNodeByParam("id", vm.locationinfo.parentId);// 得到当前上级位置节点
		ztree.selectNode(node);// 选中新增加的节点
		// vm.locationinfo.parentName = node.name;
	})
}

var vm = new Vue({
	el : '#rrapp',
	data : {
		showInfo : false,
		title : null,
		locationinfo : {
			parentName : null,
			parentId : 0,
			type : 1,
			sort : 0,
			fileName : "",
			upath : ""
		},
	},
	created : function() {
		initTree();
	},
	methods : {
		getlocationinfo : function(locationinfoId) {
			// 加载位置树
			$.get("..//locationinfo/selectlocationinfo",
					function(r) {
						// 初始化zTree，三个参数一次分别是容器(zTree 的容器 className 别忘了设置为
						// "ztree")、参数配置、数据源
						ztree = $.fn.zTree.init($("#allLocationinfoTree"),
								setting, r.locationinfoEntities);
						var node = ztree.getNodeByParam("id",
								vm.locationinfo.parentId);// 得到当前上级位置节点
						ztree.selectNode(node);// 选中新增加的节点
						// vm.locationinfo.parentName = node.name;
					})
		},
		add : function() {
			var nodes = ztree2.getSelectedNodes();
			if (nodes.length <= 0) {
				alertMsg("请选择父级位置");
				return;
			}
			vm.showInfo = true;
			vm.title = "新增位置";
			vm.locationinfo.type = '1';
			vm.locationinfo.parentId = nodes[0].id;
			vm.locationinfo.name = "";
			vm.locationinfo.parentName = nodes[0].name;
			vm.locationinfo.id = null;
		},
		del : function(event) {
			var nodes = ztree2.getSelectedNodes();
			if (nodes.length <= 0) {
				alertMsg("请选择要删除的位置");
			}
			confirm('确定要删除位置【' + nodes[0].name + '】,及下面的子位置么？', function() {
				var url = "..//locationinfo/delete";
				var childs = nodes[0].id;
				childs = getAllNodes(nodes[0], childs);
				$.post(url, childs, function(r) {
					if (r.code == 0) {
						toast(r.msg, function() {
							ztree2.removeNode(nodes[0]);
						});
					} else {
						alertMsg(r.msg);
					}
				});
			});
		},
		saveOrUpdate : function(event) {
			var url = vm.locationinfo.id == null ? "..//locationinfo/save"
					: "..//locationinfo/update";
			vm.locationinfo.fileName = $("#fileName").val();
			$.ajax({
				type : "POST",
				url : url,
				data : JSON.stringify(vm.locationinfo),
				success : function(r) {
					if (r.code == 0) {
						var locationinfo = r.locationinfo;
						var nodes = ztree2.getSelectedNodes();
						if (vm.locationinfo.id == null) {
							toast(r.msg, function() {
								ztree2.addNodes(nodes[0], {
									id : locationinfo.id,
									parentId : locationinfo.parentId,
									name : locationinfo.name
								});
							});
						} else {
							toast(r.msg, function() {
								nodes[0].name = locationinfo.name;
								ztree2.updateNode(nodes[0]);
							});
						}
					} else {
						alertMsg(r.msg);
					}
				}
			});
		},
		locationinfoTree : function() {
			layer.open({
				type : 1,
				offset : '50px',
				skin : 'layui-layer-molv',
				title : "选择位置",
				area : [ '300px', '450px' ],
				shade : 0,
				shadeClose : false,
				content : jQuery("#locationinfoLayer"),
				btn : [ '确定', '取消' ],
				btn1 : function(index) {
					var node = ztree.getSelectedNodes();
					// 选择上级位置
					vm.locationinfo.parentId = node[0].id;
					vm.locationinfo.parentName = node[0].name;
					layer.close(index);
				}
			});
		},
		reload : function(event) {
			$("#locationinfoFrom")[0].reset();
		},

		upload : function() {
			var formData = new FormData();
			// filename是键，file是值，就是要传的文件，test.zip是要传的文件名
			formData.append('file', this.upath);
			$.ajax({
			       url: '../locationinfo/upload',
			       type: 'POST',
			       data: formData,
			       dataType: 'json',
			       contentType: false,    //不可缺
			       processData: false,    //不可缺
			       success: function(data) {
			           if(data.code == '0' ){
			        	   alert("上传成功");
			        	   $("#uploadImg").show();
			        	   $("#uploadImg").attr("src","../locationinfo/viewImg/" + data.pictureName);
			        	   vm.locationinfo.fileName = data.pictureName;
			           }
			       },
			       error: function(data) {
			    	   alertMsg("上传失败");
			       }
			});
		},

		getFile : function(event) {
			vm.upath = event.target.files[0];
		},
		
		delFile : function(event) {
		   $("#uploadImg").hide();
     	   $("#uploadImg").attr("src","");
     	   vm.locationinfo.fileName = null;
		},

		locationinfoIcons : function() {
			var url = "../sys/menuIcons.html";
			layer.open({
				type : 2,
				skin : 'layui-layer-molv',
				title : "选择位置图片",
				area : [ '90%', '70%' ],
				shade : 0,
				shadeClose : false,
				content : [ url, 'no' ],
			});
		}
	}
});