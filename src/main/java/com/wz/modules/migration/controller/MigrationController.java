package com.wz.modules.migration.controller;

import com.wz.modules.common.utils.Result;
import com.wz.modules.migration.service.MigrationService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import java.io.File;

@RestController
@RequestMapping("migration")
@Api("数据迁移测试")
public class MigrationController {

    @Autowired
    private MigrationService migrationService;

    @Autowired
    private ServletContext servletContext;

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
}
