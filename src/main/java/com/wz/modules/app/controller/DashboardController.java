package com.wz.modules.app.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.wz.front.service.AppDeviceBoxService;
import com.wz.front.util.DeviceType;
import com.wz.front.util.DeviceUtils;
import com.wz.modules.app.entity.ServerMessage;
import com.wz.modules.app.entity.SwitchBoxInfo;
import com.wz.modules.common.controller.BaseController;
import com.wz.modules.common.jiguang.JiguangPush;
import com.wz.modules.common.utils.*;
import com.wz.modules.deviceinfo.dao.DeviceBoxInfoDao;
import com.wz.modules.deviceinfo.dao.DeviceMacSnMapDao;
import com.wz.modules.deviceinfo.entity.DeviceBoxInfoEntity;
import com.wz.modules.deviceinfo.entity.DeviceMacSnEntity;
import com.wz.modules.deviceinfo.entity.DeviceSwitchInfoEntity;
import com.wz.modules.deviceinfo.service.DeviceBoxInfoService;
import com.wz.modules.deviceinfo.service.DeviceOperationService;
import com.wz.modules.devicelog.entity.*;
import com.wz.modules.devicelog.service.DeviceAlarmFlowService;
import com.wz.modules.devicelog.service.DeviceAlarmInfoLogService;
import com.wz.modules.devicelog.service.DeviceElectricityLogService;
import com.wz.modules.devicelog.service.DeviceSwitchInfoLogService;
import com.wz.modules.kk.entity.PageInfo;
import com.wz.modules.kk.service.KkService;
import com.wz.modules.lora.dao.GatewayDeviceMapDao;
import com.wz.modules.lora.dao.GatewayInfoDao;
import com.wz.modules.lora.dao.GymMasterDao;
import com.wz.modules.lora.dto.DeviceBindInfoDto;
import com.wz.modules.lora.entity.GatewayDeviceMap;
import com.wz.modules.lora.entity.GatewayInfo;
import com.wz.modules.lora.entity.GymMaster;
import com.wz.modules.lora.enums.LoRaCommand;
import com.wz.modules.projectinfo.entity.LocationInfoEntity;
import com.wz.modules.projectinfo.entity.ProjectInfoEntity;
import com.wz.modules.projectinfo.service.LocationInfoService;
import com.wz.modules.projectinfo.service.ProjectInfoService;
import com.wz.modules.sys.entity.RoleEntity;
import com.wz.modules.sys.entity.UserEntity;
import com.wz.modules.sys.service.RoleService;
import com.wz.modules.sys.service.SysConfigService;
import com.wz.modules.sys.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
import java.util.*;

@CrossOrigin
@Api(value = "APP及面板controller", tags = {"APP及面板操作接口"})
@Controller
@RequestMapping("/app/dashbaord")
public class DashboardController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(DashboardController.class);
    @Autowired
    private DeviceAlarmInfoLogService deviceAlarmInfoLogService;

    @Autowired
    private DeviceSwitchInfoLogService deviceSwitchInfoLogService;

    @Autowired
    private DeviceElectricityLogService deviceElectricityLogService;

    @Autowired
    private KkService kkService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ProjectInfoService projectInfoService;

    @Autowired
    private DeviceBoxInfoService deviceBoxInfoService;

    @Autowired
    private DeviceOperationService deviceOperationService;

    @Autowired
    private DeviceBoxInfoDao deviceBoxInfoDao;

    @Autowired
    private LocationInfoService locationInfoService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DeviceAlarmFlowService deviceAlarmFlowService;

    @Autowired
    private GatewayDeviceMapDao gatewayDeviceMapDao;

    @Autowired
    private GatewayInfoDao gatewayInfoDao;

    @Autowired
    private GymMasterDao gymMasterDao;

    @Autowired
    private AppDeviceBoxService appDeviceBoxService;

    @Autowired
    private DeviceMacSnMapDao deviceMacSnMapDao;

    /**
     * 警告分页
     *
     * @param startTime
     * @param endTime
     * @param pageSize
     * @param page
     * @return
     */
    @ApiOperation(value = "获取告警分页信息", notes = "开始时间[startTime],[endTime]为空则不受时间限制")
    @RequestMapping(value = "/getAlarmsPage", method = {RequestMethod.GET})
    @ResponseBody
    public PageInfo<DeviceAlarmInfoLogEntity> getAlarmsPage(String startTime, String endTime, String pageSize,
                                                            String page, String projectId, String deviceBoxId, String alarmLevel) {
        logger.info("================>获取告警数据: startTime:" + startTime + ", endTime:" + endTime);
        PageInfo<DeviceAlarmInfoLogEntity> pageInfo = deviceAlarmInfoLogService.queryAppPage(startTime, endTime, page,
                pageSize, projectId, deviceBoxId, alarmLevel);
        logger.info("================>获取告警返回数据：pageInfo:" + pageInfo.getDataList().size());
        return pageInfo;
    }

    /**
     * 警告柱状图
     *
     * @param startDate
     * @return
     */
    @ApiOperation(value = "警告柱状图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目ID", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = false, dataType = "String", paramType = "query")})
    @RequestMapping(value = "getAlarmsStat", method = RequestMethod.GET)
    @ResponseBody
    public List<DeviceAlarmStatEntity> getAlarmsStat(String projectId, String startDate) {
        List<DeviceAlarmStatEntity> result = deviceAlarmInfoLogService.doStatDeviceAlarm(projectId, startDate);
        return result;
    }

    /**
     * 电箱分页
     *
     * @param startTime
     * @param endTime
     * @param pageSize
     * @param page
     * @return
     */
    @ApiOperation(value = "电箱数据分页(电流电压等)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目ID", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "当前页", required = false, dataType = "String", paramType = "query")})
    @RequestMapping(value = "getSwitchesPage", method = RequestMethod.GET)
    @ResponseBody
    public PageInfo<DeviceSwitchInfoLogEntity> getSwitchesPage(String projectId, String startTime, String endTime,
                                                               String pageSize, String page) {
        logger.info("================>获取电箱分页数据: startTime:" + startTime + ", endTime:" + endTime);
        PageInfo<DeviceSwitchInfoLogEntity> pageInfo = deviceSwitchInfoLogService.queryAppPage(projectId, startTime,
                endTime, page, pageSize);
        logger.info("================>获取电箱分页数据：pageInfo:" + pageInfo.getDataList().size());
        return pageInfo;
    }

    @ApiOperation(value = "获取电箱总数,在线数量,离线数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目ID", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "getBoxInfoCnt", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> getBoxInfoCnt(String projectId) {
        Map<String, String> resultMap = new HashMap<String, String>();
        List<DeviceBoxInfoEntity> boxInfoList = deviceBoxInfoService.findDeviceBoxsInfoByProjectId(projectId);
        if (boxInfoList != null && boxInfoList.size() > 0) {
            Map<String, Integer> onlineMaps = this.kkService.statDeviceBoxOnline(projectId);
            resultMap.put("boxTotal", boxInfoList.size() + "");
            try {
                resultMap.put("switchOnlineTotal", onlineMaps.get("onlineNums") + "");
                resultMap.put("switchLeaveTotal", (boxInfoList.size() - onlineMaps.get("onlineNums")) + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            resultMap.put("boxTotal", "0");
            resultMap.put("switchOnlineTotal", "0");
            resultMap.put("switchLeaveTotal", "0");
        }
        return resultMap;
    }

    @ApiOperation(value = "每日用电柱状图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目ID", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = false, dataType = "String", paramType = "query")})
    @RequestMapping(value = "getElecStat", method = RequestMethod.GET)
    @ResponseBody
    public List<DeviceElecStatEntity> getElecStat(String projectId, String startDate) {
        List<DeviceElecStatEntity> result = deviceElectricityLogService.doStatDeviceElec(projectId, startDate);
        return result;
    }

    @ApiOperation(value = "查询项目中所有电箱")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目ID", required = true, dataType = "String", paramType = "query")})
    @RequestMapping(value = "getAllBoxInfos", method = RequestMethod.GET)
    @ResponseBody
    public List<DeviceBoxInfoEntity> getAllBoxInfos(String projectId) {
        List<DeviceBoxInfoEntity> boxInfoList = deviceBoxInfoService.findDeviceBoxsInfoByProjectId(projectId);
        if(CollectionUtils.isNotEmpty(boxInfoList)) {
            try {
                this.kkService.processDeviceBoxOnline(boxInfoList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return boxInfoList;
    }

    @ApiOperation(value = "查询项目中电箱上线下线信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目ID", required = true, dataType = "String", paramType = "query")})
    @RequestMapping(value = "getAllBoxInfo", method = RequestMethod.GET)
    @ResponseBody
    public List<DeviceBoxInfoEntity> getAllBoxInfo(String projectId) {
        List<DeviceBoxInfoEntity> boxInfoList = deviceBoxInfoService.findDeviceBoxInfosByProjectId(projectId);
        if(CollectionUtils.isNotEmpty(boxInfoList)) {
            try {
                this.kkService.processDeviceBoxOnline(boxInfoList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return boxInfoList;
    }

    @RequestMapping(value = "getBoxInfosLike", method = RequestMethod.GET)
    @ResponseBody
    public List<DeviceBoxInfoEntity> getBoxInfosLike(String projectId, String deviceBoxNum) {
        List<DeviceBoxInfoEntity> boxInfoList = deviceBoxInfoService.findDeviceBoxsInfoLike(projectId, deviceBoxNum);
        if(CollectionUtils.isNotEmpty(boxInfoList)) {
            try {
                this.kkService.processDeviceBoxOnline(boxInfoList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return boxInfoList;
    }

    @ApiOperation(value = "查询电箱实时数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceBoxMac", value = "电箱MAC地址", required = true, dataType = "String", paramType = "query")})
    @RequestMapping(value = "getRealDataByBoxMac", method = RequestMethod.GET)
    @ResponseBody
    public DeviceBoxInfoEntity getRealDataByBoxMac(String deviceBoxMac, String projectId) {
        DeviceBoxInfoEntity device = deviceBoxInfoService.queryByMac(deviceBoxMac, projectId);
        List<DeviceSwitchInfoEntity> switchList = kkService.getBoxChannelsRealData(deviceBoxMac, projectId);
        device.setSwitchList(switchList);
        return device;
    }

    @ApiOperation(value = "查询所有项目信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "登陆名称", dataType = "String", paramType = "query")})
    @RequestMapping(value = "getAllProject", method = RequestMethod.GET)
    @ResponseBody
    public List<ProjectInfoEntity> listAll(String loginName) {
        List<ProjectInfoEntity> projectinfoList = projectInfoService.queryListAll();
        List<ProjectInfoEntity> tempProjectinfoList = new ArrayList<ProjectInfoEntity>();
        if (!StringUtils.isBlank(loginName)) {
            UserEntity user = this.userService.queryByLoginName(loginName);
            if (null != user) {
                if (!StringUtils.isBlank(user.getProjectIds())) {
                    String relProjectIds = user.getProjectIds();
                    for (ProjectInfoEntity p : projectinfoList) {
                        if (relProjectIds.indexOf(p.getId()) != -1) {
                            tempProjectinfoList.add(p);
                        }
                    }
                }
            }
        } else {
            tempProjectinfoList = projectinfoList;
        }
        return tempProjectinfoList;
    }

    @ApiOperation(value = "查询项目下所有位置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目ID", required = true, dataType = "String", paramType = "query")})
    @RequestMapping(value = "findLocInfoByPId", method = RequestMethod.GET)
    @ResponseBody
    public List<LocationInfoEntity> findLocInfoByPId(String projectId) {
        List<LocationInfoEntity> locInfos = locationInfoService.findLocInfoByPId(projectId);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("projectId", projectId);
        params.put("page", "1");
        params.put("limit", "99999");
        Query query = new Query(params);
        List<DeviceBoxInfoEntity> deviceBoxInfoList = deviceBoxInfoService.queryList(query);
        try {
            this.kkService.processDeviceBoxOnline(deviceBoxInfoList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> maps = new HashMap<String, String>();
        for (DeviceBoxInfoEntity boxEntity : deviceBoxInfoList) {
            if ("0".equals(boxEntity.getOnline())) {
                maps.put(boxEntity.getLocationId(), "0");
            }
        }
        for (LocationInfoEntity loc : locInfos) {
            if (maps.containsKey(loc.getId())) {
                loc.setOnline("0");
            }
        }
        return locInfos;
    }

    @ApiOperation(value = "根据位置信息获取电箱信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目ID", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "locationId", value = "位置ID", required = false, dataType = "String", paramType = "query")})
    @RequestMapping(value = "getDeviceBoxInfoByLocation", method = RequestMethod.GET)
    @ResponseBody
    public List<DeviceBoxInfoEntity> findDeviceBoxsInfoByLId(String projectId, String locationId) {
        List<DeviceBoxInfoEntity> deviceBoxs = deviceBoxInfoService.findDeviceBoxsInfoByLId(projectId, locationId);
        try {
            this.kkService.processDeviceBoxOnline(deviceBoxs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceBoxs;
    }

    @ApiOperation(value = "获取某个电箱的警告信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceBoxId", value = "电箱ID", required = true, dataType = "String", paramType = "query")})
    @RequestMapping(value = "queryDeviceAlarmList", method = RequestMethod.GET)
    @ResponseBody
    public List<DeviceAlarmInfoLogEntity> queryDeviceAlarmList(String deviceBoxId) {
        List<DeviceAlarmInfoLogEntity> result = deviceAlarmInfoLogService.queryDeviceAlarmList(deviceBoxId);
        return result;
    }

    @RequestMapping(value = "queryDeviceAlarmPage", method = RequestMethod.GET)
    @ResponseBody
    public List<DeviceAlarmInfoLogEntity> queryDeviceAlarmPage(String pageSize, String page, String deviceBoxId) {
        List<DeviceAlarmInfoLogEntity> result = deviceAlarmInfoLogService.queryDeviceAlarmList(deviceBoxId);
        return result;
    }

    @ApiOperation(value = "设置电箱开和关")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceBoxMac", value = "电箱MAC地址", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "switchAddStrs", value = "线路地址", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "cmd", value = "控制命令[开-open,关-close]", required = true, dataType = "String", paramType = "query"),})
    @RequestMapping(value = "setCmdSwitch", method = RequestMethod.GET)
    @ResponseBody
    public String setCmdSwitch(String deviceBoxMac, String switchAddStrs, String cmd, String projectId) {
        String code = kkService.setCmdSwitch(projectId, deviceBoxMac, switchAddStrs, cmd);
        return code;
    }

    @ApiOperation(value = "设置电箱开")
    @RequestMapping(value = "setCmdSwitchsOpen", method = RequestMethod.POST)
    @ResponseBody
    public Result setCmdSwitchsOpen(@RequestBody SwitchBoxInfo boxInfo) {
        String message = doValidateProjectRoles(boxInfo);
        if (!StringUtils.isBlank(message)) {
            return Result.error(message);
        }
        if(boxInfo != null && boxInfo.getVersion() != null && boxInfo.getVersion() == 1) {
            return appDeviceBoxService.remoteControlSwitches(boxInfo, LoRaCommand.OPEN_CMD);
        } else {
            new Thread(new Runnable() {
                public void run() {
                    for (String boxNum : boxInfo.getBoxList()) {
                        kkService.setCmdSwitch(boxInfo.getProjectId(), boxNum, "0", "open");
                    }
                }
            }).start();
            return Result.ok();
        }
    }

    @ApiOperation(value = "设置电箱关")
    @RequestMapping(value = "setCmdSwitchsClose", method = RequestMethod.POST)
    @ResponseBody
    public Result setCmdSwitchsClose(@RequestBody SwitchBoxInfo boxInfo) {
        String message = doValidateProjectRoles(boxInfo);
        if (!StringUtils.isBlank(message)) {
            return Result.error(message);
        }
        if(boxInfo != null && boxInfo.getVersion() != null && boxInfo.getVersion() == 1) {
            return appDeviceBoxService.remoteControlSwitches(boxInfo, LoRaCommand.CLOSE_CMD);
        } else {
            new Thread(new Runnable() {
                public void run() {
                    for (String boxNum : boxInfo.getBoxList()) {
                        kkService.setCmdSwitch(boxInfo.getProjectId(), boxNum, "0", "close");
                    }
                }
            }).start();
            return Result.ok();
        }
    }

    public String doValidateProjectRoles(SwitchBoxInfo boxInfo) {
        String message = "";

        if (null == boxInfo) {
            message = "请求参数不能为空";
        } else {
            if (StringUtils.isBlank(boxInfo.getProjectId())) {
                message = "项目ID为空";
            }

            if (StringUtils.isBlank(boxInfo.getUserId())) {
                message = "操作人ID为空";
            }

            if (null == boxInfo.getBoxList()) {
                message = "电箱为空";
            }
        }

        if (!StringUtils.isEmpty(message)) {
            return message;
        } else {
            List<String> roleIds = projectInfoService.queryRoleIdList(boxInfo.getProjectId());
            List<RoleEntity> roleList = roleService.queryByUserId(boxInfo.getUserId(), "0");
            boolean authFlag = false;
            for (RoleEntity r : roleList) {
                if (roleIds.contains(r.getId())) {
                    authFlag = true;
                    break;
                }
            }

            if (!authFlag) {
                message = "该用户没有权限";
            }
        }
        return message;
    }

    @ApiOperation(value = "电箱新增", httpMethod = "POST")
    @RequestMapping(value = "deviceBoxSave")
    @ResponseBody
    public Result deviceBoxSave(@RequestBody DeviceBoxInfoEntity deviceBoxInfo) {
        try {
            deviceBoxInfoService.save(deviceBoxInfo);
            return Result.ok();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @ApiOperation(value = "电箱修改", httpMethod = "POST")
    @RequestMapping(value = "deviceBoxUpdate")
    @ResponseBody
    public Result deviceBoxUpdate(@RequestBody DeviceBoxInfoEntity deviceBoxInfo) {
        try {
            deviceBoxInfoService.update(deviceBoxInfo);
            return Result.ok();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @ApiOperation(value = "电箱删除", httpMethod = "POST")
    @RequestMapping(value = "deviceBoxDelete")
    @ResponseBody
    public Result deviceBoxDelete(@RequestBody String[] ids) {
        try {
            deviceOperationService.deleteDevices(ids);
            return Result.ok();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @ApiOperation(value = "位置新增", httpMethod = "POST")
    @RequestMapping(value = "locationInfoSave")
    @ResponseBody
    public Result locationInfoSave(@RequestBody LocationInfoEntity location) {
        try {
            locationInfoService.save(location);
            return Result.ok();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @ApiOperation(value = "位置修改", httpMethod = "POST")
    @RequestMapping(value = "locationInfoUpdate")
    @ResponseBody
    public Result locationInfoUpdate(@RequestBody LocationInfoEntity location) {
        try {
            locationInfoService.update(location);
            return Result.ok();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @ApiOperation(value = "位置删除", httpMethod = "POST")
    @RequestMapping(value = "locationInfoDelete")
    @ResponseBody
    public Result locationInfoDelete(@RequestBody String[] ids) {
        try {
            locationInfoService.deleteBatch(ids);
            return Result.ok();
        } catch (Exception e) {
            return Result.error();
        }
    }

    @ApiOperation(value = "推送告警信息至websocket和app", httpMethod = "POST")
    @RequestMapping(value = "alarmSend", method = RequestMethod.POST)
    @ResponseBody
    public Result alarmSend(@RequestBody DeviceAlarmInfoLogEntity alarm) {
        try {
            if (!StringUtils.isBlank(alarm.getId())) {
                alarm = this.deviceAlarmInfoLogService.queryObject(alarm.getId());
                if (null == alarm) {
                    return Result.error("告警信息(" + alarm.getId() + ")不存在");
                }
                JSONObject jsonObject = JSONObject.fromObject(alarm);
                String deviceBoxId = alarm.getDeviceBoxId();
                DeviceBoxInfoEntity deviceBoxInfo = deviceBoxInfoService.queryObject(deviceBoxId);
                deviceBoxInfo.setAlarmLogId(alarm.getId());
                deviceBoxInfoService.update(deviceBoxInfo);
                if ("4".equals(alarm.getAlarmLevel())) {
                    messagingTemplate.convertAndSend("/topic/subscribeTest", new ServerMessage(jsonObject.toString()));
                    ProjectInfoEntity project = this.projectInfoService.queryObject(deviceBoxInfo.getProjectId());
                    String projectInfo = "{id:" + project.getId() + ", projectName:" + project.getProjectName() + "}";
                    List<UserEntity> userList = this.userService.queryObjectByRole();
                    for (UserEntity u : userList) {
                        JiguangPush.push(u.getLoginName(),
                                deviceBoxInfo.getDeviceBoxName() + "-" + alarm.getNode() + "发生" + alarm.getInfo(),
                                projectInfo);
                    }
                }
                return Result.ok();
            } else {
                return Result.error();
            }
        } catch (Exception e) {
            // e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "根据电箱MAC获取二维码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceBoxMac", value = "电箱MAC地址", required = true, dataType = "String", paramType = "query")})
    @RequestMapping(value = "getQrcode", method = RequestMethod.GET)
    public void getQrcode(String deviceBoxMac, HttpServletRequest request, HttpServletResponse resp) throws Exception {
        String fileName = deviceBoxMac + ".png";
        resp.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

        String projectPath = "http://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath();
        String url = projectPath + "/app/dashbaord/getWxQrInfo?deviceBoxMac=" + deviceBoxMac;
        ServletOutputStream stream = null;
        try {
            stream = resp.getOutputStream();
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bm = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 300, 300);
            MatrixToImageWriter.writeToStream(bm, "png", stream);
        } catch (WriterException e) {
            e.getStackTrace();
        } finally {
            if (stream != null) {
                stream.flush();
                stream.close();
            }
        }
    }

    @ApiOperation(value = "根据电箱二维码获取电箱详情")
    @RequestMapping(value = "getQrInfo", method = RequestMethod.GET)
    public Object getQrInfo(HttpServletRequest request, Model model, String deviceBoxMac, String devEUI) throws Exception {
        DeviceType deviceType = DeviceUtils.checkDeviceType(request);
        if (deviceType == DeviceType.MOBILE_APP) {
            return "redirect:/app/dashbaord/getDeviceInfo?deviceBoxMac=" + deviceBoxMac + "&devEUI=" + devEUI;
        }
        String qrresult = "";
        String temp = deviceBoxMac;
        Map<String, Object> params = new HashMap<String, Object>();
        if (!StringUtils.isBlank(temp) && temp.indexOf("qrresult") != -1) {
            deviceBoxMac = temp.substring(0, temp.indexOf("qrresult="));
            qrresult = temp.substring(temp.indexOf("qrresult=") + 9);
        }
        params.put("deviceBoxMac", deviceBoxMac);
        params.put("page", "1");
        params.put("limit", "1");
        params.put("sidx", "create_time");
        params.put("order", "desc");
        Query query = new Query(params);
        List<DeviceBoxInfoEntity> deviceBoxInfoList = deviceBoxInfoService.queryList(query);

        if (deviceBoxInfoList.size() > 0) {
            ProjectInfoEntity project = projectInfoService.queryObject(deviceBoxInfoList.get(0).getProjectId());
            model.addAttribute("projectName", project.getProjectName());
            model.addAttribute("deviceBox", deviceBoxInfoList.get(0));
        }

        if (!StringUtils.isBlank(qrresult)) {
            model.addAttribute("qrresult", qrresult);
        }
        return "dashboard/qr";
    }

    @ApiOperation(value = "获取电箱详情")
    @RequestMapping(value = "getDeviceInfo", method = RequestMethod.GET)
    @ResponseBody
    public DeviceBindInfoDto getDeviceInfo(String deviceBoxMac, String devEUI) {
        String temp = deviceBoxMac;
        Map<String, Object> params = new HashMap<String, Object>();
        if (!StringUtils.isBlank(temp) && temp.indexOf("qrresult") != -1) {
            deviceBoxMac = temp.substring(0, temp.indexOf("qrresult="));
        }
        params.put("deviceBoxMac", deviceBoxMac);
        params.put("page", "1");
        params.put("limit", "1");
        params.put("sidx", "create_time");
        params.put("order", "desc");
        Query query = new Query(params);
        DeviceBoxInfoEntity boxInfoEntity = deviceBoxInfoDao.queryByBoxNum(deviceBoxMac);
        DeviceBindInfoDto deviceBindInfoDto = new DeviceBindInfoDto();
        deviceBindInfoDto.setDeviceBoxNum(deviceBoxMac);
        deviceBindInfoDto.setDeviceBoxSn(devEUI);
        if (boxInfoEntity != null) {
            ProjectInfoEntity project = projectInfoService.queryObject(boxInfoEntity.getProjectId());
            GatewayDeviceMap device = gatewayDeviceMapDao.findDevice(project.getId(), boxInfoEntity.getId());
            GatewayInfo gatewayInfo = gatewayInfoDao.findById(device.getGatewayId());
            GymMaster gymMaster = gymMasterDao.findById(device.getGymId());
            if (device == null) {
                deviceBindInfoDto.setNew(true);
                return deviceBindInfoDto;
            }
            deviceBindInfoDto.setNew(false);
            deviceBindInfoDto.setDeviceBoxId(boxInfoEntity.getId());
            deviceBindInfoDto.setGymId(device.getGymId());
            deviceBindInfoDto.setGymName(gymMaster.getName());
            deviceBindInfoDto.setProjectId(project.getId());
            deviceBindInfoDto.setProjectName(project.getProjectName());
            deviceBindInfoDto.setStandNo(boxInfoEntity.getStandNo());
            deviceBindInfoDto.setGatewayId(gatewayInfo.getGatewayId());
            deviceBindInfoDto.setGatewayName(gatewayInfo.getGymName());
            deviceBindInfoDto.setGatewayIp(gatewayInfo.getIpAddress());
        } else {
            deviceBindInfoDto.setNew(true);
        }
        return deviceBindInfoDto;
    }

    @ApiOperation(value = "查询电箱历史数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceBoxId", value = "电箱ID", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "projectId", value = "项目id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "hours", value = "N小时前[1天:24小时,3天:72小时]", required = true, dataType = "String", paramType = "query")})
    @RequestMapping(value = "getDeviceBoxHistory", method = RequestMethod.GET)
    @ResponseBody
    public Result getDeviceBoxHistory(String deviceBoxId, String projectId, String hours) {
        List<DeviceSwitchInfoLogEntity> result = deviceSwitchInfoLogService.getDeviceBoxHistory(deviceBoxId, projectId,
                hours);
        return Result.ok().put("result", result);
    }

    @ApiOperation(value = "保存二维码电信信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目ID", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deviceBoxId", value = "电箱ID", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deviceBoxSn", value = "电箱SN号", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deviceBoxMac", value = "电箱MAC地址", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "thirdLocation", value = "三级位置", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "controlFlag", value = "是否受控[0:否 1:是]", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "remark", value = "备注", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "boxCapacity", value = "电箱容量", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "secBoxGateway", value = "二级网关号", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "standNo", value = "展位号", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "gymId", value = "场馆ID", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "gatewayId", value = "网关ID", required = false, dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value = "postQrInfo", method = RequestMethod.POST)
    @ResponseBody
    public Result postQrInfo(HttpServletRequest request, String projectId, String deviceBoxId, String deviceBoxSn, String deviceBoxMac, String thirdLocation,
                             String controlFlag, String remark, String boxCapacity, String secBoxGateway, String standNo, String userId, Integer gymId, Integer gatewayId)
            throws Exception {
        if (StringUtils.isBlank(projectId)) {
            return Result.error("项目id为空");
        }

        if (StringUtils.isBlank(deviceBoxMac)) {
            return Result.error("电箱MAC地址为空");
        }

        if(StringUtils.isBlank(deviceBoxSn)) {
            DeviceMacSnEntity macSnEntity = deviceMacSnMapDao.findById(deviceBoxMac);
            if(macSnEntity == null || StringUtils.isBlank(macSnEntity.getDeviceSn()))
                return Result.error("找不到电箱MAC对应的电箱SN");

            deviceBoxSn = macSnEntity.getDeviceSn();
        }

        if (StringUtils.isBlank(thirdLocation)) {
            return Result.error("三级位置为空");
        }

        if (StringUtils.isBlank(controlFlag)) {
            return Result.error("是否受控为空");
        }

        if (StringUtils.isBlank(boxCapacity)) {
            return Result.error("电箱容量为空");
        }

        if (gymId == 1 && StringUtils.isBlank(secBoxGateway)) {
            return Result.error("二级网关号为空");
        }

        if (StringUtils.isBlank(standNo)) {
            return Result.error("展位号为空");
        }

        List<Map<String, String>> result = new ArrayList<>();
        Map<String, String> map = new LinkedHashMap<>();
        String forthLoc = standNo + "(" + deviceBoxMac + ")";
        map.put("firstLoc", "根目录");
        if(gymId != null) {
            GymMaster gym = gymMasterDao.findById(gymId);
            map.put("secLoc", gym.getName());
        } else {
            map.put("secLoc", "世博展览馆");
        }
        map.put("thirdLoc", thirdLocation);
        map.put("forthLoc", forthLoc);
        map.put("fifthLoc", null);
        map.put("deviceMac", deviceBoxMac);
        map.put("remark", remark);
        map.put("secBoxGateway", secBoxGateway);
        map.put("standNo", standNo);
        map.put("boxCapacity", boxCapacity);
        map.put("controlFlag", controlFlag);
        map.put("deviceBoxId", deviceBoxId);
        result.add(map);
        return deviceOperationService.addDevice(result, userId, deviceBoxMac, projectId, request, deviceBoxSn, gymId, gatewayId);
    }

    @ApiOperation(value = "更新收电箱标记")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目ID", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deviceBoxMac", value = "电箱MAC地址", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "placedFlag", value = "0:未收,1:已收", required = true, dataType = "String", paramType = "query")})
    @RequestMapping(value = "updateBoxPlaced", method = RequestMethod.POST)
    @ResponseBody
    public Result updateBoxPlaced(String projectId, String deviceBoxMac, String placedFlag) throws Exception {
        if (StringUtils.isBlank(projectId)) {
            return Result.error("项目id为空");
        }

        if (StringUtils.isBlank(deviceBoxMac)) {
            return Result.error("电箱MAC地址为空");
        }

        if (StringUtils.isBlank(placedFlag)) {
            return Result.error("收电箱标记为空");
        }
        int n = this.deviceBoxInfoService.updateBoxPlacedFlag(projectId, deviceBoxMac, placedFlag);
        if (n > 0) {
            return Result.ok();
        } else {
            return Result.error("未更新到记录");
        }
    }

    @ApiOperation(value = "根据电箱MAC获取二维码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceBoxMac", value = "电箱MAC地址", required = true, dataType = "String", paramType = "query")})
    @RequestMapping(value = "downQrcode", method = RequestMethod.GET)
    public void downQrcode(String deviceBoxMac, HttpServletRequest request, HttpServletResponse resp) throws Exception {
        String fileName = deviceBoxMac + ".png";
        resp.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        String projectPath = "http://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath();
        String url = projectPath + "/app/dashbaord/getQrInfo?deviceBoxMac=" + deviceBoxMac;
        ServletOutputStream stream = null;
        try {
            stream = resp.getOutputStream();
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bm = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 300, 300);
            MatrixToImageWriter.writeToStream(bm, "png", stream);
        } catch (WriterException e) {
            e.getStackTrace();
        } finally {
            if (stream != null) {
                stream.flush();
                stream.close();
            }
        }
    }

    @RequestMapping(value = "setTerminalPsrams", method = RequestMethod.GET)
    public Result setTerminalPsrams(String terminalAddr, String switchAddr, String maxPower, String maxEletric) {
        try {
            this.kkService.setTerminalPsrams(terminalAddr, switchAddr, maxPower, maxEletric);
            return Result.ok();
        } catch (Exception e) {
            return Result.error("服务器内部错误");
        }
    }

    @RequestMapping(value = "getTerminalParams", method = RequestMethod.GET)
    public Result getTerminalParams(String termainlAddr, String switchAddr) {
        Map<String, String> result = this.kkService.getTerminalParams(termainlAddr, switchAddr);
        return Result.ok().put("result", result);
    }

    @ApiOperation(value = "创建告警工单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "alarmId", value = "告警ID", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "projectId", value = "项目ID", required = true, dataType = "String", paramType = "query")})
    @RequestMapping(value = "createFlowOrder", method = RequestMethod.GET)
    @ResponseBody
    public Result createFlowOrder(String alarmId, String projectId) {
        DeviceAlarmInfoLogEntity alarm = deviceAlarmInfoLogService.queryObject(alarmId);
        DeviceAlarmFlowEntity flow = new DeviceAlarmFlowEntity();
        flow.setAlarmId(alarmId);
        flow.setProjectId(projectId);
        String workNo = this.getRandomOrderCode(12);
        flow.setWorkNo(workNo);
        flow.setAlarmContent(alarm.getInfo());
        flow.setStatus("0");
        this.deviceAlarmFlowService.save(flow);
        return Result.ok("创建成功");
    }

    @ApiOperation(value = "更新告警工单状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "alarmFlowId", value = "告警工单ID", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态[1:处理中 2:已完成]", required = true, dataType = "String", paramType = "query")})
    @RequestMapping(value = "updateFlowOrderStatus", method = RequestMethod.GET)
    @ResponseBody
    public Result updateFlowOrderStatus(String alarmFlowId, String status) {
        DeviceAlarmFlowEntity flow = this.deviceAlarmFlowService.queryObject(alarmFlowId);
        flow.setStatus(status);
        if ("1".equals(status)) {
            flow.setDealTime(new Date());
        }
        this.deviceAlarmFlowService.update(flow);
        return Result.ok("更新成功");
    }

    @ApiOperation(value = "工单结果上报")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "alarmFlowId", value = "告警工单ID", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pictureNames", value = "图片名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "remark", value = "备注", required = true, dataType = "String", paramType = "query")})
    @RequestMapping(value = "flowOrderSubmit", method = RequestMethod.GET)
    @ResponseBody
    public Result flowOrderSubmit(String alarmFlowId, String pictureNames, String remark) {
        DeviceAlarmFlowEntity flow = this.deviceAlarmFlowService.queryObject(alarmFlowId);
        if (null != flow) {
            flow.setFilePath(pictureNames);
            flow.setRemark(remark);
            this.deviceAlarmFlowService.update(flow);
            return Result.ok("上报成功").put("flow", flow);
        } else {
            return Result.error("未查询到工单记录");
        }
    }

    @ApiOperation(value = "查看工单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "alarmFlowId", value = "告警工单ID", required = true, dataType = "String", paramType = "query")})
    @RequestMapping(value = "queryFlowOrderDetail", method = RequestMethod.GET)
    @ResponseBody
    public Result queryFlowOrderDetail(String alarmFlowId) {
        DeviceAlarmFlowEntity flow = this.deviceAlarmFlowService.queryObject(alarmFlowId);
        if (null != flow) {
            String alarmId = flow.getAlarmId();
            DeviceAlarmInfoLogEntity alarm = this.deviceAlarmInfoLogService.queryObject(alarmId);
            flow.setAlarmInfoLog(alarm);
        }
        return Result.ok("查询成功").put("flow", flow);
    }

    /**
     * 警告分页
     *
     * @param pageSize
     * @param page
     * @return
     */
    @ApiOperation(value = "获取工单分页")
    @ApiImplicitParams({@ApiImplicitParam(name = "status", value = "状态", dataType = "String", paramType = "query"),

            @ApiImplicitParam(name = "alarmStartDate", value = "告警开始时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "alarmEndDate", value = "告警结束时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "deviceBoxMac", value = "电箱MAC", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "standNo", value = "展位号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "alarmType", value = "告警类型", dataType = "String", paramType = "query"),

            @ApiImplicitParam(name = "projectId", value = "项目ID", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "当前页", required = true, dataType = "String", paramType = "query")})
    @RequestMapping(value = "getFlowPage", method = RequestMethod.GET)
    @ResponseBody
    public PageInfo<DeviceAlarmFlowEntity> getFlowPage(String status, String alarmStartDate, String alarmEndDate,
                                                       String deviceBoxMac, String standNo, String alarmType, String pageSize, String page, String projectId) {
        PageInfo<DeviceAlarmFlowEntity> pageInfo = this.deviceAlarmFlowService.queryFlowPage(projectId, status,
                alarmStartDate, alarmEndDate, deviceBoxMac, standNo, alarmType, page, pageSize);
        return pageInfo;
    }

    @ApiOperation(value = "工单图片上传")
    @RequestMapping(value = "flowOrderUpload", method = RequestMethod.POST)
    @ResponseBody
    public Result upload(@RequestPart("file") MultipartFile file, HttpServletResponse response) {
        if (file.isEmpty()) {
            return Result.error("图片不能为空");
        }
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String pictureId = UUID.randomUUID().toString();
        String pictureName = pictureId + "." + suffix;
        try {
            String fileSavePath = this.getFileUploadPath();
            File tmpFile = new File(fileSavePath);
            if (!tmpFile.exists()) {
                tmpFile.mkdir();
            }
            file.transferTo(new File(fileSavePath + pictureName));
            // DeviceAlarmFlowEntity flow =
            // this.deviceAlarmFlowService.queryObject(alarmFlowId);
            // flow.setFilePath(pictureName);
            // flow.setRemark(remark);
            // this.deviceAlarmFlowService.update(flow);
            return Result.ok("保存成功").put("pictureName", pictureName);
        } catch (Exception e) {
            return Result.error();
        }
    }

    @ApiOperation(value = "工单图片查看")
    @RequestMapping(value = "/viewFlowImg/{fileName:.+}", method = RequestMethod.GET)
    public void renderSfCheckPicture(@PathVariable("fileName") String fileName, HttpServletRequest request,
                                     HttpServletResponse response) {
        if (StringUtils.isEmpty(fileName)) {
            return;
        }
        String fileUploadPath = this.getFileUploadPath();
        String path = fileUploadPath + fileName;
        try {
            byte[] bytes = FileUtil.toByteArray(path);
            response.getOutputStream().write(bytes);
        } catch (Exception e) {

        }
    }

    @ApiOperation(value = "查询工单角色用户")
    @RequestMapping(value = "queryRoleUser", method = RequestMethod.GET)
    @ResponseBody
    public Result queryRoleUser() {
        List<RoleEntity> roleUser = this.userService.queryRoleUser();
        return Result.ok().put("roleUser", roleUser);
    }

    @ApiOperation(value = "更新告警工单处理人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "alarmFlowId", value = "告警工单ID", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "dealId", value = "告警工单处理人ID", required = true, dataType = "String", paramType = "query")})
    @RequestMapping(value = "updateFlowOrderDeal", method = RequestMethod.GET)
    @ResponseBody
    public Result updateFlowOrderDeal(String alarmFlowId, String dealId) {
        DeviceAlarmFlowEntity flow = this.deviceAlarmFlowService.queryObject(alarmFlowId);
        flow.setDealId(dealId);
        this.deviceAlarmFlowService.update(flow);
        return Result.ok("更新成功");
    }

    public String getFileUploadPath() {
        // 如果没有写文件上传路径,保存到临时目录
        if (isWinOs()) {
            return System.getProperty("java.io.tmpdir") + "icbmsUpload\\";
        } else {
            return System.getProperty("java.io.tmpdir") + "/icbmsUpload/";
        }
    }

    public Boolean isWinOs() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            return true;
        } else {
            return false;
        }
    }

    public String getRandomOrderCode(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 取得大写字母还是小写字母
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val.toUpperCase() + DateUtils.format(new Date(), "yyMMdd");
    }
}