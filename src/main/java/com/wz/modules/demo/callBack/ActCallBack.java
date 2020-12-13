package com.wz.modules.demo.callBack;

import org.springframework.stereotype.Component;

import com.wz.modules.activiti.dto.ProcessTaskDto;

/**
 * 类的功能描述.
 *
 * @Auther hxy
 * @Date 2017/7/27
 */
@Component
public class ActCallBack {

    public void leaveBack(ProcessTaskDto processTaskDto){
        System.out.println("请假回调成功啦！！！！！！！");
    }
}
