<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>二维码</title>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<%@include file="/common/commonCSS.jsp"%>
<%@include file="/common/commonJS.jsp"%>
<%@include file="/WEB-INF/jsp/include/taglib.jsp"%>
</head>
<style>
	*{ font-size: 16px;}
.top1 {
	width: 100%;
	display: flex;
	flex-direction: row;
	text-align: center;
	line-height: 52px;
	background-color: #FFFFFF;
	border-bottom: solid 1px #f1f1f1;
	

}

.left {
	width: 30%;
	text-align: left;
	    padding-left: 15px; color: #333333;
}

.btn {
	width: 20%;
	height: 30px;
	margin-top: 4px;
	color: white;
	background-color: blue;
	line-height: 19px;
	border-radius: 3px;
	position: absolute;
	right: 5px;
}

input {
	border: none;
	outline: none;
	background-color: white;
	border-bottom: 1px solid bla
}

.commit {
	width: 80%;
	height: 38px;
	border-radius: 3px;
	background-color: rgb(84, 248, 8);
	color: white;
	text-align: center;
	line-height: 38px;
	position: fixed;
	bottom: 10px;
	left: 10%;
}
</style>
<body>
	<h2 style=" text-align: center; line-height: 50px; font-weight: bold; background: #0064B6; color: #ffffff; font-weight: bold;">电箱详情</h2>
	<div class="top1">
		<span class="left">归属项目:</span><span style="text-align: right; width: 70%; float: right; padding-right: 15px; font-weight: bold;">${projectName}</span>
	</div>

<!-- 	<div class="top1">
		<span class="left">归属位置</span><input type="text" id="location"
			value="${deviceBox.locationName}" />
	</div> -->
	<div class="top1">
		<span class="left">设备号MAC:</span><span style="text-align: right; width: 70%; float: right; padding-right: 15px; font-weight: bold;"> ${deviceBox.deviceBoxNum}</span>
	</div>
	<div class="top1 top2">
		<span class="left">二级箱编号:</span><input style="text-align: right; width: 70%; float: right; padding-right: 15px;  font-weight: bold;" type="text" id="secBoxGateway"
			readonly="readonly" />
		<%-- <div class="btn">
			<a
				href="http://sao315.com/w/api/saoyisao?redirect_uri=http://119.96.233.75:8090/icbms/app/dashbaord/getWxQrInfo?deviceBoxMac=${deviceBox.deviceBoxNum}">扫一扫</a>
		</div> --%>
	</div>
	<div class="top1">
		<span class="left">展位号:</span><input style="text-align: right;width: 70%; float: right; padding-right: 15px; font-weight: bold;" type="text" id="standNo"
			value='${deviceBox.standNo}' />
	</div>
	<div class="top1">
		<span class="left">电箱容量:</span><input style="text-align: right;width: 70%; float: right; padding-right: 15px; font-weight: bold;" type="text" id="capacity"
			value="${deviceBox.boxCapacity}" />
	</div>
<!-- 	<div class="top1">
		<span class="left">是否受控</span> <input type="radio" name="controlFlag"
			id="controlFlag1" value="1" />是 <input type="radio"
			name="controlFlag" id="controlFlag0" value="0" />否
	</div>
	<div class="top1">
		<span class="left">备注</span><input type="text" id="comments"
			value="${deviceBox.remark}" />
	</div>

	<div class="top1">
		<span class="left">口令</span><input type="text" id="dpswd" />
	</div> -->
</body>
<script>
	$(document).ready(function() {
		var qrresult = "${qrresult}";
		var controlFlag = "${deviceBox.controlFlag}";
		if (qrresult == "") {
			$("#secBoxGateway").val("${deviceBox.secBoxGateway}");
		} else {
			$("#secBoxGateway").val(qrresult);
		}

		if (controlFlag == "" || controlFlag == "1") {
			$("#controlFlag1").attr("checked", "checked");
		} else {
			$("#controlFlag0").attr("checked", "checked");
		}
	})

	function f_submit() {
		var controlFlag = $("input[name='controlFlag']:checked").val();
		var remark = $("#comments").val();
		var boxCapacity = $("#capacity").val();
		var deviceBoxNum = "${deviceBox.deviceBoxNum}";
		var secBoxGateway = $("#secBoxGateway").val();
		var dpswd = $("#dpswd").val();
		var standNo = $("#standNo").val();

		$.ajax({
			type : "POST",
			url : "${webRoot}/app/dashbaord/postQrInfo",
			dataType : "json",
			data : {
				"standNo" : standNo,
				"deviceBoxNum" : deviceBoxNum,
				"controlFlag" : controlFlag,
				"remark" : remark,
				"boxCapacity" : boxCapacity,
				"secBoxGateway" : secBoxGateway,
				"dpswd" : dpswd
			},
			cache : false,
			success : function(r) {
				if (r.code == 0) {
					alert("提交成功");
				} else {
					alert("提交失败[" + r.msg + "]");
				}
			}
		});
	}
</script>
</html>