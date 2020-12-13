package com.wz.modules.report.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.Api;
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
import com.wz.modules.report.entity.ElectricReportEntity;
import com.wz.modules.report.service.ElectricReportService;

@CrossOrigin
@RestController
@RequestMapping("/report/electric")
@Api(tags = "用电报表接口")
public class ElectricReportController extends BaseController {
	@Autowired
	private ElectricReportService electricReportService;

	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("report:electric:list")
	@SysLog("用电查询")
	public Result list(@RequestParam Map<String, Object> params) {
		params.put("projectId", this.getCurrentProjectId());
		// 查询列表数据
		Query query = new Query(params);
		List<ElectricReportEntity> list = electricReportService.queryList(query);
		int total = electricReportService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());
		return Result.ok().put("page", pageUtil);
	}

	/**
	 * 导出全量表
	 */
	@RequestMapping("/exportAll")
	@SysLog("用电报表导出")
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
		List<ElectricReportEntity> list = electricReportService.queryList(params);

		Map<String, Object> model = new HashMap<>();
		model.put("electricList", list);
		return new ModelAndView(new JxlsExcelView("electricAll.xlsx", "电量统计.xlsx"), model);
	}
}
