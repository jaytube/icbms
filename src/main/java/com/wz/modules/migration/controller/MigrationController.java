package com.wz.modules.migration.controller;

import com.wz.modules.common.utils.Result;
import com.wz.modules.deviceinfo.dao.DeviceMacSnMapDao;
import com.wz.modules.deviceinfo.entity.DeviceMacSnEntity;
import com.wz.modules.deviceinfo.service.DeviceOperationService;
import com.wz.modules.migration.service.MigrationService;
import io.swagger.annotations.Api;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("migration")
@Api("数据迁移测试")
public class MigrationController {

    @Autowired
    private MigrationService migrationService;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private DeviceMacSnMapDao deviceMacSnMapDao;

    @Autowired
    private DeviceOperationService deviceOperationService;

    @GetMapping("/userProjects")
    public Result migrateUserProject() {
        Boolean result = migrationService.migrateUserProjects();
        return Result.ok().put("msg", "Migration UserProjects Successfully!");
    }

    @GetMapping("/locationInfo")
    public Result migrateLocationInfo() {
        Boolean result = migrationService.migrateLocationInfos();
        return Result.ok().put("msg", "Migration LocationInfos Successfully!");
    }

    @GetMapping("/getPath")
    public Result getPath() {
        String realPath = servletContext.getRealPath(File.separator);
        return Result.ok().put("msg", realPath);
    }

    @GetMapping("/loadDevice")
    public Result loadDevice() {
        migrationService.loadDeviceMacSnMap();
        return Result.ok().put("msg", "load devices success!");
    }

    @RequestMapping(value = "/loadAllDevice", method = RequestMethod.POST)
    @ResponseBody
    public Result loadAllDevice(HttpServletRequest request, String projectId, String thirdLocation, String controlFlag,
                                String remark, String boxCapacity, String secBoxGateway, String standNo, String userId, int gymId, int gatewayId) {
        if (StringUtils.isBlank(projectId)) {
            return Result.error("项目id为空");
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

        if (StringUtils.isBlank(secBoxGateway)) {
            return Result.error("二级网关号为空");
        }

        if (StringUtils.isBlank(standNo)) {
            return Result.error("展位号为空");
        }

        List<DeviceMacSnEntity> all = deviceMacSnMapDao.findAll();

        if(CollectionUtils.isEmpty(all))
            return Result.ok();

        for (DeviceMacSnEntity device : all) {
            String deviceBoxMac = device.getDeviceMac();
            String deviceBoxSn = device.getDeviceSn();
            List<Map<String, String>> result = new ArrayList<>();
            Map<String, String> map = new LinkedHashMap<>();
            String forthLoc = standNo + "(" + deviceBoxMac + ")";
            map.put("firstLoc", "根目录");
            map.put("secLoc", "世博展览馆");
            map.put("thirdLoc", thirdLocation);
            map.put("forthLoc", forthLoc);
            map.put("fifthLoc", null);
            map.put("deviceMac", deviceBoxMac);
            map.put("remark", remark);
            map.put("secBoxGateway", secBoxGateway);
            map.put("standNo", standNo);
            map.put("boxCapacity", boxCapacity);
            map.put("controlFlag", controlFlag);
            result.add(map);
            deviceOperationService.addDevice(result, userId, deviceBoxMac, projectId, request, deviceBoxSn, gymId, gatewayId);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return Result.ok();

    }
}
