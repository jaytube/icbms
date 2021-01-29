package com.wz.front.service;

import com.wz.modules.common.utils.CommonResponse;
import com.wz.modules.lora.entity.GatewayInfo;
import com.wz.modules.lora.entity.GymMaster;

import java.util.List;

/**
 * @Author: Cherry
 * @Date: 2021/1/29
 * @Desc: AppRefDataService
 */
public interface AppRefDataService {

    List<GymMaster> getGyms();

    List<GatewayInfo> getGateways();

    CommonResponse initGatewayInfos();

}
