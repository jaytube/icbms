package com.wz.modules.report.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.wz.modules.common.annotation.SysLog;
import com.wz.modules.common.controller.BaseController;
import com.wz.modules.common.excel.JxlsExcelView;
import com.wz.modules.common.utils.PageUtils;
import com.wz.modules.common.utils.Query;
import com.wz.modules.common.utils.Result;
import com.wz.modules.report.entity.AlarmInfoReportEntity;
import com.wz.modules.report.service.AlarmReportService;
import com.wz.modules.sys.service.CodeService;

@CrossOrigin
@RestController
@RequestMapping("/report/alarm")
public class AlarmReportController extends BaseController {

	@Autowired
	private AlarmReportService alarmReportService;

	@Autowired
	private CodeService codeService;

	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("report:alarm:list")
	@SysLog("告警报表")
	public Result list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		params.put("projectId", this.getCurrentProjectId());
		params.put("alarmStatus", "1");
		Query query = new Query(params);

		List<AlarmInfoReportEntity> deviceAlarmInfoLogList = alarmReportService.queryList(query);
		if (null != deviceAlarmInfoLogList && 0 < deviceAlarmInfoLogList.size()) {
			Map<String, String> levelMap = this.codeService.queryChildsMapByMark("alarm_level");

			for (AlarmInfoReportEntity alarm : deviceAlarmInfoLogList) {
				if (levelMap.containsKey(alarm.getAlarmLevel())) {
					alarm.setAlarmLevelName(levelMap.get(alarm.getAlarmLevel()));
				}
			}
		}
		int total = alarmReportService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(deviceAlarmInfoLogList, total, query.getLimit(), query.getPage());

		return Result.ok().put("page", pageUtil);
	}

	/**
	 * 导出全量表
	 */
	@RequestMapping("/exportAll")
	@SysLog("告警报表导出")
	public ModelAndView export(@RequestParam(required = false) String beginTime,
			@RequestParam(required = false) String endTime, HttpServletRequest request) {
		Map<String, Object> params = new HashMap<>();
		if (StringUtils.isNotBlank(beginTime)) {
			params.put("beginTime", beginTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			params.put("endTime", endTime);
		}

		// 查询列表数据
		params.put("projectId", this.getCurrentProjectId());
		params.put("alarmStatus", "1");
		// Query query = new Query(params);
		List<AlarmInfoReportEntity> deviceAlarmInfoLogList = alarmReportService.queryList(params);
		if (null != deviceAlarmInfoLogList && 0 < deviceAlarmInfoLogList.size()) {
			Map<String, String> levelMap = this.codeService.queryChildsMapByMark("alarm_level");

			for (AlarmInfoReportEntity alarm : deviceAlarmInfoLogList) {
				if (levelMap.containsKey(alarm.getAlarmLevel())) {
					alarm.setAlarmLevelName(levelMap.get(alarm.getAlarmLevel()));
				}
			}
		}
		Map<String, Object> model = new HashMap<>();
		model.put("alarmList", deviceAlarmInfoLogList);
		return new ModelAndView(new JxlsExcelView("alarmAll.xlsx", "告警记录.xlsx"), model);
	}
}
