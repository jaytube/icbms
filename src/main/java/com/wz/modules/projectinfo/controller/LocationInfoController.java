package com.wz.modules.projectinfo.controller;

import com.wz.front.util.FileUtils;
import com.wz.modules.app.annotation.LoginRequired;
import com.wz.modules.common.annotation.SysLog;
import com.wz.modules.common.controller.BaseController;
import com.wz.modules.common.utils.FileUtil;
import com.wz.modules.common.utils.Result;
import com.wz.modules.common.utils.StringUtils;
import com.wz.modules.projectinfo.entity.LocationInfoEntity;
import com.wz.modules.projectinfo.service.LocationInfoService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 位置基础表; InnoDB free: 401408 kB
 *
 * @author Raymond
 * @email rui.sun.java@gmail.com
 * @date 2018-03-13 15:14:33
 */
@CrossOrigin
@RestController
@Api(tags = "位置基础信息接口")
@RequestMapping("/locationinfo")
public class LocationInfoController extends BaseController {
    @Autowired
    private LocationInfoService locationInfoService;

    @Autowired
    private FileUtils fileUtils;

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("projectinfo:location:info")
    @SysLog("查看位置信息")
    public Result info(@PathVariable("id") String id) {
        LocationInfoEntity location = locationInfoService.queryObject(id);
        return Result.ok().put("locationinfo", location);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("projectinfo:location:update")
    @SysLog("保存位置信息")
    public Result save(@RequestBody LocationInfoEntity location) {
        location.setProjectId(this.getCurrentProjectId());
        String id = locationInfoService.save(location);
        LocationInfoEntity locationEntity = locationInfoService.queryObject(id);
        return Result.ok().put("locationinfo", locationEntity);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("projectinfo:location:update")
    @SysLog("修改位置")
    public Result update(@RequestBody LocationInfoEntity location) {
        locationInfoService.update(location);
        return Result.ok().put("locationinfo", location);
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("projectinfo:location:delete")
    @SysLog("删除位置")
    public Result delete(@RequestBody String ids) {
        locationInfoService.deleteBatch(StringUtils.getArrayByArray(ids.split(",")));
        return Result.ok();
    }

    /**
     * 根据项目ID查找所有位置信息
     */
    @RequestMapping("/findLocInfoByPId")
    public Result findLocInfoByPId(@RequestBody String projectId) {
        List<LocationInfoEntity> locInfos = locationInfoService.findLocInfoByPId(projectId);
        return Result.ok().put("locInfos", locInfos);
    }

    /**
     * 初始化位置树数据
     *
     * @return
     */
    @RequestMapping(value = "/initTreeData")
    public Result initTreeData() {
        List<LocationInfoEntity> locationList = null;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("projectId", this.getCurrentProjectId());
        locationList = locationInfoService.queryList(params);
        return Result.ok().put("locationList", locationList);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/upload")
    public Result upload(@RequestPart("file") MultipartFile file, HttpServletResponse response) {
        if (file.isEmpty()) {
            return Result.error("图片不能为空");
        }
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String pictureId = UUID.randomUUID().toString();
        String pictureName = pictureId + "." + suffix;
        try {
            String fileSavePath = fileUtils.getFileUploadPath();
            File tmpFile = new File(fileSavePath);
            if (!tmpFile.exists()) {
                tmpFile.mkdir();
            }
            file.transferTo(new File(fileSavePath + pictureName));
            return Result.ok().put("pictureName", pictureName);
        } catch (Exception e) {
            return Result.error();
        }
    }

    @RequestMapping("/viewImg/{fileName:.+}")
    @LoginRequired
    public void renderSfCheckPicture(@PathVariable("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(fileName)) {
            return;
        }
        String fileUploadPath = fileUtils.getFileUploadPath();
        String path = fileUploadPath + fileName;
        try {
            byte[] bytes = FileUtil.toByteArray(path);
            response.getOutputStream().write(bytes);
        } catch (Exception e) {
        }
    }
}