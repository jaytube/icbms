package com.wz.modules.lora.dao;

import com.wz.modules.lora.entity.GatewayInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: Cherry
 * @Date: 2021/1/29
 * @Desc: GatewayInfoDao
 */
@Mapper
public interface GatewayInfoDao {

    List<GatewayInfo> findAll();

    GatewayInfo findById(int gatewayId);

    void insert(GatewayInfo gatewayInfo);

    void batchInsert(List<GatewayInfo> gatewayInfos);
}
