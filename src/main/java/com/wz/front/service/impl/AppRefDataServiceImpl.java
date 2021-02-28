package com.wz.front.service.impl;

import com.wz.front.service.AppRefDataService;
import com.wz.modules.common.utils.CommonResponse;
import com.wz.modules.lora.dao.GatewayAddressDao;
import com.wz.modules.lora.dao.GatewayInfoDao;
import com.wz.modules.lora.dao.GymMasterDao;
import com.wz.modules.lora.entity.GatewayAddress;
import com.wz.modules.lora.entity.GatewayInfo;
import com.wz.modules.lora.entity.GymMaster;
import com.wz.modules.lora.service.LoRaCommandService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Cherry
 * @Date: 2021/1/29
 * @Desc: AppRefDataServiceImpl
 */
@Service
public class AppRefDataServiceImpl implements AppRefDataService {

    @Autowired
    private GymMasterDao gymMasterDao;

    @Autowired
    private GatewayInfoDao gatewayInfoDao;

    @Autowired
    private GatewayAddressDao gatewayAddressDao;

    @Autowired
    private LoRaCommandService loRaCommandService;

    @Override
    public List<GymMaster> getGyms() {
        return gymMasterDao.findAll();
    }

    @Override
    public List<GatewayInfo> getGateways() {
        List<GatewayInfo> all = gatewayInfoDao.findAll();
        if(CollectionUtils.isNotEmpty(all))
            all = all.stream().sorted(Comparator.comparing(GatewayInfo::getGymName)).collect(Collectors.toList());

        return all;
    }

    @Override
    public CommonResponse initGatewayInfos() {
        List<GatewayAddress> allAddress = gatewayAddressDao.findAll();
        if (CollectionUtils.isEmpty(allAddress)) {
            return CommonResponse.success("初始化成功,0个网关");
        }
        List<GatewayInfo> allInDb = gatewayInfoDao.findAll();
        Map<String, GatewayInfo> map = new HashMap<>();
        allInDb.forEach(item -> map.put(item.getIpAddress(), item));
        List<GatewayInfo> allGateWays = new ArrayList<>();
        for (GatewayAddress address : allAddress) {
            CommonResponse<List<GatewayInfo>> gatewayListData = loRaCommandService.getGatewayList(address.getIpAddress());
            List<GatewayInfo> gatewayInfos = gatewayListData.getData();
            if (CollectionUtils.isEmpty(gatewayInfos)) {
                continue;
            }
            allGateWays.addAll(gatewayInfos);
        }
        List<GatewayInfo> infoList = allGateWays.stream().filter(gatewayInfo -> !map.containsKey(gatewayInfo.getIpAddress())).collect(Collectors.toList());
        if (infoList.size() > 0) {
            gatewayInfoDao.batchInsert(infoList);
        }
        return CommonResponse.success("初始化成功" + infoList.size() + "个网关");
    }
}
