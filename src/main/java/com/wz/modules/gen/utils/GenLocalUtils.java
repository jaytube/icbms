package com.wz.modules.gen.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wz.IcbFrameBootApplication;
import com.wz.modules.common.common.Constant;
import com.wz.modules.gen.service.SysGeneratorService;

import javax.annotation.Resource;

/**
 * 类的功能描述.
 *
 * @Auther hxy
 * @Date 2017/11/15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = IcbFrameBootApplication.class)
public class GenLocalUtils {
    @Resource
    private SysGeneratorService generatorService;

    @Test
    public void generatorCode(){
        byte[] bytes=generatorService.generatorCode(new String[]{"sys_gentest"}, Constant.genType.local.getValue());
    }
}
