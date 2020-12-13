/**
 * 
 */
package com.wz.modules.report.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wz.modules.common.annotation.SysLog;
import com.wz.modules.common.controller.BaseController;
import com.wz.modules.common.utils.DateUtils;
import com.wz.modules.common.utils.PageUtils;
import com.wz.modules.common.utils.Query;
import com.wz.modules.common.utils.Result;
import com.wz.modules.report.entity.ElectricDailyEntity;
import com.wz.modules.report.service.ElectricReportService;

/**
 * @author lkpc
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/report/electricdaily")
public class ElectricDailyController extends BaseController {

	@Autowired
	private ElectricReportService electricReportService;

	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("report:electricdaily:list")
	@SysLog("当日用电统计查询")
	public Result list(@RequestParam Map<String, Object> params) {
		params.put("projectId", this.getCurrentProjectId());
		params.put("statDate", DateUtils.format(new Date()));
		// 查询列表数据
		Query query = new Query(params);
		List<ElectricDailyEntity> list = electricReportService.queryDailyElectricCnt(query);
		int total = electricReportService.queryDailyElectricCntTotal(query);
		
		PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());
		return Result.ok().put("page", pageUtil);
	}

}
