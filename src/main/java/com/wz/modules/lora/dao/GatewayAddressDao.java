package com.wz.modules.lora.dao;

import com.wz.modules.lora.entity.GatewayAddress;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: Cherry
 * @Date: 2021/1/29
 * @Desc: GatewayAddressDao
 */
@Mapper
public interface GatewayAddressDao {

    List<GatewayAddress> findAll();

    void insert(GatewayAddress gateWayAddress);
}
