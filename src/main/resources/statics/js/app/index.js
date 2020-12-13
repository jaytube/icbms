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
        // init_boxInfo(treeNode.id); //点击获取当前节点所有电箱信息
        clearInterval(reload)
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
$.ajax({
    type: "POST",
    url: "../projectinfo/getCurrentProject",
    dataType: "json",
    cache: false,
    success: function (data) {
        if (data.code == 0) {
            currentProject = data.currentProject.id;
            $.ajax({
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
        url: getBoxUrl,
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
                    "<option value=" + r.deviceBoxs[i].id + "---" +
                    r.deviceBoxs[i].deviceBoxNum +
                    " deviceBoxNum=" +
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
            "click",
            function () {
                menu.style.display = "none";
                for (m = 0; m < hideid.length; m++) {
                    document.getElementById(hideid[m]).parentNode.style.display = "block"
                }
            });

    // 放置位置点的集合

    // 所有点定义
    var initbox = function (arrSort) {
        if (document.getElementsByClassName("dots")[0] != null) {
            var thisNode = document.getElementsByClassName("dots")[0];
            thisNode.parentNode.removeChild(thisNode);
        }

        var container_dots = container.append("g").attr("class", "dots");
        $(".dots").empty();
        $.each(arrSort, function (index, val) {
            var switchImage = "";
            if (val.online == "0") { // 在线
                if (val.alarm != null) {
                    if (val.alarm.alarmStatus == "1") { // 告警
                        if (val.alarm.alarmLevel == "4") {
                            switchImage = "../images/icon/alarm3.png";
                        } else {
                            switchImage = imageUrl;
                        }
                    } else { // 告警解除
                        switchImage = imageUrl;
                    }
                } else {
                    switchImage = imageUrl;
                }
            } else if (val.online == "1") { // 不在线状态
                switchImage = "../images/icon/alarm2.png";
            }
            var container_ci = container_dots.append("g").attr(
                "class", "ci sort" + val.sort);
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
                    switchImage).attr("id", index).attr("x",
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
                .attr("x", parseFloat(val.fx) - RECT_W / 2 + offset_x)
                .attr("y", parseFloat(val.fy) - RECT_H + 2 + offset_y)
                .attr("width", RECT_W)
                .attr("height", RECT_H)
                .on("click", function (e) {
                    var index1 = this.id;
                    $.ajax({
                        url: "../app/dashbaord/getRealDataByBoxMac?deviceBoxMac=" + index1+'&projectId='+currentProject,
                        type: "get",
                        success: function (data) {
                            $(".tit1").text(data.locationName + "-" + data.deviceBoxName + "电箱实时数据")
                            var zTree = $.fn.zTree.getZTreeObj("allLocationinfoTree");
                            var node = zTree.getNodeByParam("id", data.locationId);
                            zTree.selectNode(node);
                            if (data.config != null) {
                                $(".gatewayAddress").text("网关地址:" + (data.config.gatewayAddress || '暂无数据'))
                                $(".gatewayMac").text( "网关MAC:" + (data.config.gatewayMac ||"暂无数据"))
                                $(".locationName").text("电箱地址:" + (data.config.deviceBoxAddress || '暂无数据'));
                            } else {
                               /* $(".gatewayAddress").text("网关地址:" + "暂无数据")
                                $(".gatewayMac").text("网关MAC:" + "暂无数据")
                                $(".locationName").text( "电箱地址:暂无数据"); */
                            }
                            $(".standNo").text("展位号:" + (data.standNo || "暂无数据"))
                            $('.secBoxGateway').text('二级网关号:' + data.secBoxGateway || "暂无数据")
                            $(".deviceBoxNum").text( "电箱MAC:" + (data.deviceBoxNum ||"暂无数据"));
                            $(".boxCapacity").text("电箱容量:" + (data.boxCapacity || '暂无数据'));
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
                                    res1 += "<tr>" +
                                        "<td>" +
                                        data[m].phaseVoltageA +
                                        "V</td>" +
                                        "<td>" +
                                        data[m].phaseCurrentA +
                                        "A</td>" +
                                        "<td>" +
                                        data[m].phasePowerA +
                                        "W</td>" +
                                        "<td>" +
                                        data[m].phaseVoltageB +
                                        "V</td>" +
                                        "<td>" +
                                        data[m].phaseCurrentB +
                                        "A</td>" +
                                        "<td>" +
                                        data[m].phasePowerB +
                                        "W</td>" +
                                        "</tr>"
                                }
                                res3 = "<tr><th>C相电压</th><th>C相电流</th><th>C相功率</th></tr>"
                                for (m = 0; m < data.length; m++) {
                                    res3 += "<tr>" +
                                        "<td>" +
                                        data[m].phaseVoltageC +
                                        "V</td>" +
                                        "<td>" +
                                        data[m].phaseCurrentC +
                                        "A</td>" +
                                        "<td>" +
                                        data[m].phasePowerC +
                                        "W</td>" +
                                        "</tr>"
                                }
                                $(".table").append(res + res1 + res3);
                                $('.pop').slideDown('200');
                                console.log(index1)
                                $(".shuaxin").attr("id", index1)
                                $("#" + index1).click(function () {
                                    $.ajax({
                                        url: "../app/dashbaord/getRealDataByBoxMac?deviceBoxMac=" +
                                            index1+'&projectId='+currentProject,
                                        type: "get",
                                        success: function (
                                            data) {
                                            $(".tit1").text(data.locationName + "-" + data.deviceBoxName + "电箱实时数据")
                                            var zTree = $.fn.zTree.getZTreeObj("allLocationinfoTree");
                                            var node = zTree.getNodeByParam("id", data.locationId);
                                            zTree.selectNode(node);
                                            if (data.config != null) {
                                                $(".gatewayAddress").text("网关地址:" + (data.config.gatewayAddress || '暂无数据'))
                                                $(".gatewayMac").text("网关MAC:" + (data.config.gatewayMac || '暂无数据'))
                                                $(".locationName").text("电箱地址:" + (data.config.deviceBoxAddress || '暂无数据'));
                                            } else {
                                               /* $(".gatewayAddress").text("网关地址:" + "暂无数据")
                                                $(".gatewayMac").text("网关MAC:" + "暂无数据")
                                                $(".locationName").text("电箱地址:暂无数据"); */
                                            }
                                            $(".standNo").text("展位号:" + (data.standNo || '暂无数据'))
                                            $('.secBoxGateway').text('二级网关号:' + data.secBoxGateway || "暂无数据")
                                            $(".deviceBoxNum").text("电箱MAC:" + (data.deviceBoxNum || '暂无数据'));
                                            $(".boxCapacity").text("电箱容量:" + (data.boxCapacity || '暂无数据'));
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
                                                    res1 += "<tr>" +
                                                        "<td>" +
                                                        data[m].phaseVoltageA +
                                                        "V</td>" +
                                                        "<td>" +
                                                        data[m].phaseCurrentA +
                                                        "A</td>" +
                                                        "<td>" +
                                                        data[m].phasePowerA +
                                                        "W</td>" +
                                                        "<td>" +
                                                        data[m].phaseVoltageB +
                                                        "V</td>" +
                                                        "<td>" +
                                                        data[m].phaseCurrentB +
                                                        "A</td>" +
                                                        "<td>" +
                                                        data[m].phasePowerB +
                                                        "W</td>" +
                                                        "</tr>"
                                                }
                                                res3 = "<tr><th>C相电压</th><th>C相电流</th><th>C相功率</th></tr>"
                                                for (m = 0; m < data.length; m++) {
                                                    res3 += "<tr>" +
                                                        "<td>" +
                                                        data[m].phaseVoltageC +
                                                        "V</td>" +
                                                        "<td>" +
                                                        data[m].phaseCurrentC +
                                                        "A</td>" +
                                                        "<td>" +
                                                        data[m].phasePowerC +
                                                        "W</td>" +
                                                        "</tr>"
                                                }
                                                $(".table").empty().append(res + res1 + res3);
                                            } else {
                                                $(".table").css( "display", "none");
                                                $(".not").show();
                                            }
                                        }
                                    })
                                })

                            } else {
                                $(".table").css("display","none");
                                $(".not")
                                    .show();
                                $('.pop')
                                    .slideDown(
                                        '200');
                            }
                        }
                    })
                }).on("mouseover", function (e) {
                    $(this).contip({
                        align: 'bottom',
                        bg: 'blue',
                        fade: 1,
                        html: '展位号:' + val.standNo + "</br>" + "电箱MAC:" + val.deviceBoxNum
                    }).show();
                    $(this).css("z-index", "1000")
                    $(this).css("transfrom", "scale(2)")
                    TweenMax.to($(this).prev(), .1, {
                        y: "-=8",
                        repeat: 5,
                        yoyo: true
                    });
                }).on("mouseout", function (e) {
                    $(this).css("transfrom", "scale(1)")
                    $(this).css("z-index", "0")
                    TweenMax.to($(this).prev(), .1, {
                        y: "0"
                    });
                })
        });

    }

    var interval = function () {
        // 初始化如果有x，y值的话展示出来。
        var arrSort1 = [];
        // 获取电箱信息 ,默认读取第一个中国馆的id。
        $.ajax({
            type: "POST",
            url: getBoxUrl,
            dataType: "json",
            data: {
                locationId: nodeid,
                showBoxOnline: "true"
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
    }
    interval();
    window.reload = setInterval(function () {
        interval()
    }, 50000)

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
        $(".shuaxin").unbind();
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