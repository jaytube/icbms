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
        },
    },
    check: {
        nocheckInherit: true
    },
    callback: {
        onClick: zTreeOnClick
    }
};

function zTreeOnClick(event, treeId, treeNode) {
    if (treeNode.level == "2") { // 只有点击倒数第二级别的节点时候才能设置
        init_boxInfo(treeNode.id); // 点击获取当前节点所有电箱信息
        init_map("../images/icon/switchbox.png", getBoxUrl, treeNode.id);
    } else if (treeNode.level == "1" || treeNode.level == "0") {
        document.getElementById("menu").style.display = "none"
        init_pic();

    } else if (treeNode.level == "3") {
        document.getElementById("menu").style.display = "none"
        var obj = $("." + treeNode.id);
        TweenMax.to(obj, .1, {
            y: "+=-13",
            repeat: 5,
            yoyo: true
        });
    }
};

var ztree;
var ztree2;
var currentProject = "";
var getBoxUrl = "../deviceboxinfo/findMapDeviceBoxsInfo";
// 默认树节点ID
var defaultNodeId = "";

// 通过项目id 获取所有位置信息
$
    .ajax({
        type: "POST",
        url: "../projectinfo/getCurrentProject",
        dataType: "json",
        cache: false,
        success: function (data) {
            if (data.code == 0) {
                currentProject = data.currentProject.id;
                $
                    .ajax({
                        url: "../app/dashbaord/findLocInfoByPId?projectId=" +
                            currentProject,
                        type: "get",
                        success: function (r) {
                            ztree2 = $.fn.zTree.init(
                                $("#allLocationinfoTree"),
                                setting2, r).expandAll(true);
                            var treeObj = $.fn.zTree
                                .getZTreeObj("allLocationinfoTree");
                            var nodes = treeObj.getNodes();
                            for (var i = 0; i < nodes.length; i++) { // 设置节点展开
                                treeObj.expandNode(nodes[i], true,
                                    false, true);
                            }
                            //初始化根节点下倒数第二个节点
                            init_defaultNodeId(nodes[0]);
                            init_boxInfo(defaultNodeId);
                            init_map("../images/icon/switchbox.png", getBoxUrl, defaultNodeId);
                        }
                    })

            }
        }
    })

var init_defaultNodeId = function getDefaultNode(treeNode) {
    if (treeNode.children) {
        var childNodes = treeNode.children;
        if (childNodes) {
            getDefaultNode(childNodes[0]);
        }
    } else {
        var parentNode = treeNode.getParentNode();
        defaultNodeId = parentNode.id;
    }
}

// 通过第二级别的位置获取所有电箱
var init_boxInfo = function (locationId) {
    $.ajax({
        type: "POST",
        url: getBoxUrl+ "?xyControl=true",
        dataType: "json",
        data: {
            locationId: locationId
        },
        cache: false,
        async: true,
        success: function (r) {
            $("#boxInfo").empty();
            for (var i in r.deviceBoxs) {
                $("#boxInfo").append(
                    "<option id=\"option_"+r.deviceBoxs[i].id+"\"  value=" + r.deviceBoxs[i].id + "---" +
                    r.deviceBoxs[i].deviceBoxNum + "---" +
                    r.deviceBoxs[i].locationId + " deviceBoxNum=" +
                    r.deviceBoxs[i].deviceBoxNum + ">" +
                    r.deviceBoxs[i].deviceBoxName + "</option>");
            }
        }
    });
}

var init_pic = function () {
    var defaultBackground = "../images/ground.png";
    if (document.getElementById("svgchild") != null) {
        var thisNode = document.getElementById("svgchild");
        thisNode.parentNode.removeChild(thisNode);
    }
    var margin = {
            left: 0,
            top: 0,
            right: 10,
            bottom: 10
        },
        width = $("#new_flashcontent").parent().width(),
        height = $(
            "#new_flashcontent").parent().height(),

        bg_width = 2105,
        bg_height = 1005,
        ratio_init = width / bg_width;

    var zoom = d3.behavior.zoom().scaleExtent([ratio_init, 5]).on("zoom",
        zoomed);

    var drag = d3.behavior.drag().origin(function (d) {
            return d;
        }).on("dragstart", dragstarted).on("drag", dragged)
        .on("dragend", dragended);
    var paddingtop = (height - bg_height * ratio_init) / 2;
    var _svg = d3.select("#new_flashcontent").append("svg")
        .attr("width", width).attr("height", height).attr("id", "svgchild")
        .attr("style", "padding-top:" + paddingtop + "px").call(zoom);
    var container = _svg.append("g");
    container.attr("class", "all").append("image").attr("xlink:href",
        defaultBackground).attr("x", "0").attr("y", "0").attr("width",
        bg_width).attr("height", bg_height)

    function zoomed() {
        $('.tip i').text((d3.event.scale).toFixed(2));
        container.attr("transform", "translate(" + d3.event.translate +
            ")scale(" + d3.event.scale + ")");
    }

    $('.tip i').text(ratio_init.toFixed(2));

    function dragstarted(d) {
        d3.event.sourceEvent.stopPropagation();
        d3.select(this).classed("dragging", true);
    }

    function dragged(d) {
        d3.select(this).attr("cx", d.x = d3.event.x).attr("cy",
            d.y = d3.event.y);
    }

    function dragended(d) {
        d3.select(this).classed("dragging", false);

    }
    // 将画布调整到起始比率
    zoom.scale(ratio_init);
    zoom.event(_svg.transition().duration(200));
}
var init_map = function (imageUrl, getBoxUrl, nodeid) {
    var defaultBackground = "../images/ground.png";
    $.ajax({
        type: "POST",
        url: "../locationinfo/info/" + nodeid,
        dataType: "json",
        cache: false,
        async: false,
        success: function (r) {
            if (r.code == "0") {
                if (r.locationinfo.fileName != null) {
                    defaultBackground = "../locationinfo/viewImg/" +
                        r.locationinfo.fileName;
                }
            }
        }
    });

    // 先移除页面现有svg再重新加载
    if (document.getElementById("svgchild") != null) {
        var thisNode = document.getElementById("svgchild");
        thisNode.parentNode.removeChild(thisNode);
    }

    var hideid = [];
    var margin = {
            left: 0,
            top: 0,
            right: 10,
            bottom: 10
        },
        width = $("#new_flashcontent").parent().width(),
        height = $(
            "#new_flashcontent").parent().height(),

        bg_width = 2105,
        bg_height = 1005,
        ratio_init = width / bg_width;

    var zoom = d3.behavior.zoom().scaleExtent([ratio_init, 5]).on("zoom",
        zoomed);

    var drag = d3.behavior.drag().origin(function (d) {
            return d;
        }).on("dragstart", dragstarted).on("drag", dragged)
        .on("dragend", dragended);
    var paddingtop = (height - bg_height * ratio_init) / 2;
    var _svg = d3.select("#new_flashcontent").append("svg")
        .attr("width", width).attr("height", height).attr("id", "svgchild")
        .attr("style", "padding-top:" + paddingtop + "px").call(zoom);
    // document.querySelector('svg').innerHTML = '';
    // 总容器
    var container = _svg.append("g");
    var thisLocation = [];
    container
        .attr("class", "all")
        .append("image")
        .attr("xlink:href", defaultBackground)
        .attr("x", "0")
        .attr("y", "0")
        .attr("width", bg_width)
        .attr("height", bg_height)
        .on(
            "contextmenu",
            function (ev) {
            	if($("#boxInfo").val()!=null){
            		$.ajax({
                        url: "../app/dashbaord/getRealDataByBoxMac?deviceBoxMac=" +
                            $("#boxInfo").val().split("---")[1]+ "&projectId="+ currentProject,
                        type: "GET",
                        async: false,
                        success: function (data) {
                            if (data.config != null) {
                                $(".gatewayAddress")
                                    .text(
                                        "网关地址:" +
                                        data.config.gatewayAddress)
                                $(".gatewayMac")
                                    .text(
                                        "网关MAC:" +
                                        data.config.gatewayMac)
                                $(".locationName")
                                    .text(
                                        "电箱地址:" +
                                        data.config.deviceBoxAddress);
                            } else {
                                $(".gatewayAddress").text(
                                    "网关地址:null")
                                $(".gatewayMac").text("网关MAC:null")
                                $(".locationName")
                                    .text("电箱地址:null");
                            }
                            $(".standNo").text(
                                "展位号:" + data.standNo)
                            $(".deviceBoxNum").text(
                                "电箱MAC:" + data.deviceBoxNum);
                            $(".boxCapacity").text(
                                "电箱容量:" + data.boxCapacity);
                        }
                    })
            	}
                
                var ev = ev || event;
                event.preventDefault();
                var scrollTop = document.documentElement.scrollTop ||
                    document.body.scrollTop;
                menu.style.display = "block";
                menu.style.left = ev.clientX + "px";
                // 当滑动滚动条时也能准确获取菜单位置
                menu.style.top = ev.clientY + scrollTop + "px";

                thisLocation = d3.mouse(this);
                var m = d3.mouse(this);
                thisX = m[0];
                thisY = m[1];
                document.getElementById("thisX").innerText = "x: " +
                    thisX;
                document.getElementById("thisY").innerText = "y: " +
                    thisY;
                
              

                // 点击确定 上报并且添加到页面中来。
                document.getElementById("yes").onclick = function () {
                    // 上报
                    var arrSort = [];
                    // locationList和deviceboxNumber 选择电箱之后获取
                    $
                        .ajax({
                            type: "POST",
                            url: "../deviceboxinfo/setDeboxInfoXy",
                            dataType: "json",
                            data: {
                                fx: thisX,
                                fy: thisY,
                                deviceBoxInfoId: $("#boxInfo")
                                    .val().split("---")[0],
                            },
                            cache: false,
                            async: true,
                            success: function (r) {
                                if (r.code == "0") {
                                    arrSort[0] = {
                                        fx: thisX,
                                        fy: thisY,
                                        deviceBoxNum: $("#boxInfo").val().split("---")[1],
                                        locationId: $("#boxInfo") .val().split("---")[2],
                                        id: $("#boxInfo").val().split("---")[0]
                                    };
                                    document.getElementById("menu").style.display = "none";
                                    // 判断当前页面中是否已经有当前电箱，如果有的话则先删除掉再，没有得话就添加
                                    if ($("." + arrSort[0].id).length > 0) {
                                        var thisNode = document.getElementsByClassName(arrSort[0].id)[0];
                                        thisNode.parentNode.removeChild(thisNode);
                                    } else {
                                        console.log("当前电箱尚未被设置")
                                    }
                                    initbox(arrSort);
                                    $("#option_" + r.deviceBoxInfo.id).remove();
                                    
                                }
                            }
                        });
                };
                document.getElementById("cancel").onclick = function () {
                    document.getElementById("menu").style.display = "none"
                }
            })
        .on(
            "click",
            function () {
                menu.style.display = "none";
                for (m = 0; m < hideid.length; m++) {
                    document.getElementById(hideid[m]).parentNode.style.display = "block"
                }
            });

    // 放置位置点的集合
    var container_dots = container.append("g").attr("class", "dots");

    // 所有点定义
    var initbox = function (arrSort) {
        $
            .each(
                arrSort,
                function (index, val) {
                    var container_ci = container_dots.append("g").attr(
                        "class", "ci " + val.id);
                    // 负值绝对值越大 越向下
                    var offset_x = 0,
                        offset_y = 0;
                    var RECT_W = 40,
                        RECT_H = 82;

                    // 定位小园点
                    // container_ci.append("circle").attr("cx",
                    // parseFloat(val.fx) +
                    // offset_x).attr("cy",
                    // parseFloat(val.fy) + offset_y);

                    // 坐标点上的图片

                    container_ci.append("image").attr("xlink:href",
                            imageUrl).attr("id", index).attr("x",
                            parseFloat(val.fx) - RECT_W / 2 + offset_x)
                        .attr(
                            "y",
                            parseFloat(val.fy) - RECT_H + 2 +
                            offset_y).attr("width",
                            RECT_W).attr("height", RECT_H)
                        .attr("class", val.locationId).call(drag);

                    // 坐标点上的点击区

                    container_ci
                        .append("rect")
                        .attr("id", val.deviceBoxNum)
                        .attr(
                            "x",
                            parseFloat(val.fx) - RECT_W / 2 +
                            offset_x)
                        .attr(
                            "y",
                            parseFloat(val.fy) - RECT_H + 2 +
                            offset_y)
                        .attr("width", RECT_W)
                        .attr("height", RECT_H)
                        .on(
                            "click",
                            function (e) {
                                var index = this.id;
                                $
                                    .ajax({
                                        url: "../app/dashbaord/getRealDataByBoxMac?deviceBoxMac=" +
                                            index + "&projectId="+ currentProject,
                                        type: "get",
                                        success: function (data) {
                                            $(".tit1").text(data.locationName + "-" + data.deviceBoxName + "电箱实时数据");
                                            var zTree = $.fn.zTree.getZTreeObj("allLocationinfoTree");
                                            var node = zTree.getNodeByParam("id",data.locationId);
                                            zTree.selectNode(node);
                                            if (data.config != null) {
                                                $(
                                                        ".gatewayAddress")
                                                    .text(
                                                        "网关地址:" +
                                                        data.config.gatewayAddress)
                                                $(
                                                        ".gatewayMac")
                                                    .text(
                                                        "网关MAC:" +
                                                        data.config.gatewayMac)
                                                $(
                                                        ".locationName")
                                                    .text(
                                                        "电箱地址:" +
                                                        data.config.deviceBoxAddress);
                                            } else {
                                                $(
                                                        ".gatewayAddress")
                                                    .text(
                                                        "网关地址:null")
                                                $(
                                                        ".gatewayMac")
                                                    .text(
                                                        "网关MAC:null")
                                                $(
                                                        ".locationName")
                                                    .text(
                                                        "电箱地址:null");
                                            }
                                            $(".standNo")
                                                .text(
                                                    "展位号:" +
                                                    data.standNo)
                                            $(
                                                    ".deviceBoxNum")
                                                .text(
                                                    "电箱MAC:" +
                                                    data.deviceBoxNum);
                                            $(
                                                    ".boxCapacity")
                                                .text(
                                                    "电箱容量:" +
                                                    data.boxCapacity);
                                            var innerDeviceBoxId = data.id;
                                            var data = data.switchList;
                                            if (data.length != 0) {
                                                $(".not")
                                                    .hide();
                                                $(".table")
                                                    .empty()
                                                    .show();
                                                var res = "<tr><th>线路</th><th>漏电流</th><th>电流</th><th>温度</th><th>电压</th><th>功率</th></tr>"
                                                for (m = 0; m < data.length; m++) {
                                                    res += "<tr>" +
                                                        "<td>" +
                                                        data[m].address +
                                                        "</td>" +
                                                        "<td>" +
                                                        data[m].switchLeakage +
                                                        "MA</td>" +
                                                        "<td>" +
                                                        data[m].switchElectric +
                                                        "A</td>" +
                                                        "<td>" +
                                                        data[m].switchTemperature +
                                                        "℃</td>" +
                                                        "<td>" +
                                                        data[m].switchVoltage +
                                                        "V</td>" +
                                                        "<td>" +
                                                        data[m].switchPower +
                                                        "W</td>" +
                                                        "</tr>"
                                                }
                                                res1 = "<tr><th>A相电压</th><th>A相电流</th><th>A相功率</th><th>B相电压</th><th>B相电流</th><th>B相功率</th></tr>"
                                                                    for (m = 0; m < data.length; m++) {
																		res1 += "<tr>"
																				+ "<td>"
																				+ data[m].phaseVoltageA
																				+ "V</td>"
																				+ "<td>"
																				+ data[m].phaseCurrentA
																				+ "A</td>"
																				+ "<td>"
																				+ data[m].phasePowerA
                                                                                + "W</td>"
                                                                                + "<td>"
																				+ data[m].phaseVoltageB
																				+ "V</td>"
																				+ "<td>"
																				+ data[m].phaseCurrentB
																				+ "A</td>"
																				+ "<td>"
																				+ data[m].phasePowerB
																				+ "W</td>"
																				+ "</tr>"
                                                                    }
                                                                    res3 = "<tr><th>C相电压</th><th>C相电流</th><th>C相功率</th></tr>"
                                                                    for (m = 0; m < data.length; m++) {
																		res3 += "<tr>"
																				+ "<td>"
																				+ data[m].phaseVoltageC
																				+ "V</td>"
																				+ "<td>"
																				+ data[m].phaseCurrentC
																				+ "A</td>"
																				+ "<td>"
																				+ data[m].phasePowerC
																				+ "W</td>"
																				+ "</tr>"
                                                                    }
                                                $(".table").append(res+res1+res3);
                                                var resetButtonHtml = "<tr><td colspan='6' align=\"right\"><button id=\"p_reset\" >位置重置</button></td></tr>"
                                                $(".table").append(resetButtonHtml);
                                                $('.pop').slideDown('200');
                                                
                                                document.getElementById("p_reset").onclick = function () {
                                                	$.ajax({
                                                        type: "GET",
                                                        url: "../deviceboxinfo/resetXy?deviceBoxInfoId=" + innerDeviceBoxId,
                                                        success: function (data) {
                                                        	//alert(data.deviceBoxInfo.id);

                                                        	//$("#option_" + data.deviceBoxInfo.id).remove();
                                                        	
                                                        	 $("#boxInfo").append(
                                                                     "<option id=\"option_"+data.deviceBoxInfo.id+"\"  value=" + data.deviceBoxInfo.id + "---" +
                                                                     data.deviceBoxInfo.deviceBoxNum + "---" +
                                                                     data.deviceBoxInfo.locationId + " deviceBoxNum=" +
                                                                     data.deviceBoxInfo.deviceBoxNum + ">" +
                                                                     data.deviceBoxInfo.deviceBoxName + "</option>");
                                                        	 var tempArrSort = [];
                                                        	 for ( var i = 0; i <arrSort1.length; i++){
                                                        		 if(arrSort1[i].id != innerDeviceBoxId){
                                                        			 tempArrSort.push(arrSort1[i]);
                                                        		 }
                                                        	 }
                                                        	 var thisNode = document.getElementsByClassName(innerDeviceBoxId)[0];
                                                             thisNode.parentNode.removeChild(thisNode);
                                                             $('.pop').hide();
                                                        }
                                                    });
                                                }
                                            } else {
                                                var resetButtonHtml = "<tr><td colspan='6' align=\"right\"><button id=\"p_reset\" >位置重置</button></td></tr>"
                                                $(".table").html(resetButtonHtml);
                                                
                                                $(".not").show();
                                                $('.pop').slideDown('200');
                                                
                                                document.getElementById("p_reset").onclick = function () {
                                                	$.ajax({
                                                        type: "GET",
                                                        url: "../deviceboxinfo/resetXy?deviceBoxInfoId=" + innerDeviceBoxId,
                                                        success: function (data) {
                                                        	//alert(data.deviceBoxInfo.id);

                                                        	//$("#option_" + data.deviceBoxInfo.id).remove();
                                                        	
                                                        	 $("#boxInfo").append(
                                                                     "<option id=\"option_"+data.deviceBoxInfo.id+"\"  value=" + data.deviceBoxInfo.id + "---" +
                                                                     data.deviceBoxInfo.deviceBoxNum + "---" +
                                                                     data.deviceBoxInfo.locationId + " deviceBoxNum=" +
                                                                     data.deviceBoxInfo.deviceBoxNum + ">" +
                                                                     data.deviceBoxInfo.deviceBoxName + "</option>");
                                                        	 var tempArrSort = [];
                                                        	 for ( var i = 0; i <arrSort1.length; i++){
                                                        		 if(arrSort1[i].id != innerDeviceBoxId){
                                                        			 tempArrSort.push(arrSort1[i]);
                                                        		 }
                                                        	 }
                                                        	 var thisNode = document.getElementsByClassName(innerDeviceBoxId)[0];
                                                             thisNode.parentNode.removeChild(thisNode);
                                                             $('.pop').hide();
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    })
                            }).on("mouseover", function (e) {
                            TweenMax.to($(this).prev(), .1, {
                                y: "-=8",
                                repeat: 5,
                                yoyo: true
                            });
                        }).on("mouseout", function (e) {
                            TweenMax.to($(this).prev(), .1, {
                                y: "0"
                            });
                        }).on("contextmenu", function () {
                            hideid.push(this.id)
                            this.parentNode.style.display = "none";
                            event.preventDefault();
                        });
                });

    }

    // 初始化如果有x，y值的话展示出来。
    var arrSort1 = [];

    // 获取电箱信息 ,默认读取第一个中国馆的id。
    $.ajax({
        type: "POST",
        url: getBoxUrl,
        dataType: "json",
        data: {
            locationId: nodeid
        },
        cache: false,
        async: true,
        success: function (r) {
            if (r.code == 0) {
                for (var i in r.deviceBoxs) {
                    if (r.deviceBoxs[i].fx != null) {
                        arrSort1.push(r.deviceBoxs[i]);
                    }
                }
                document.getElementById("menu").style.display = "none";
                initbox(arrSort1);
            }
        }
    });

    // 将画布调整到起始比率
    zoom.scale(ratio_init);
    zoom.event(_svg.transition().duration(200));

    function dottype(d) {
        d.x = +d.x;
        d.y = +d.y;
        d.id = +d.id;
        return d;
    }

    function zoomed() {
        $('.tip i').text((d3.event.scale).toFixed(2));
        container.attr("transform", "translate(" + d3.event.translate +
            ")scale(" + d3.event.scale + ")");
    }

    function dragstarted(d) {
        d3.event.sourceEvent.stopPropagation();
        d3.select(this).classed("dragging", true);
    }

    function dragged(d) {
        d3.select(this).attr("cx", d.x = d3.event.x).attr("cy",
            d.y = d3.event.y);
    }

    function dragended(d) {
        d3.select(this).classed("dragging", false);

    }
    // 弹窗
    $('.pop i').click(function (event) {
        $('.pop').slideUp().children('span').text("");
    });

    // 右上角的比例显示
    $('.tip i').text(ratio_init.toFixed(2));

    // 变尺寸
    var _box = $(".box1");
    $('.btn').click(function (event) {
        var url = location.href;
        window.open(url);
    });

    function box_resize(width, height) {
        // 变换画布
        /*
         * width = $("#new_flashcontent").parent().width(), height =
         * $("#new_flashcontent").parent().height();
         */
        ratio_init = width / 2105;
        // 改变画布尺寸
        TweenMax.to($('svg'), .3, {
            width: width,
            height: height
        });
        // 改变位移和比例
        zoom.translate([0, 0]).scale(ratio_init);
        zoom.event(_svg.transition().duration(200));
    }
}

// 位置树的收缩
$('.side').click(function (event) {
    if ($('.panel').hasClass('active')) {
        $('.panel').removeClass('active')
    } else {
        {
            $('.panel').addClass('active')
        }
    }
});