package com.wz.modules.migration.controller;

import com.wz.modules.common.utils.Result;
import com.wz.modules.migration.service.MigrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("migration")
public class MigrationController {

    @Autowired
    private MigrationService migrationService;

    @GetMapping("/userProjects")
    public Result migrateUserProject() {
        Boolean result = migrationService.migrateUserProjects();
        return Result.ok().put("msg", "Migration Successfully!");
    }
}
