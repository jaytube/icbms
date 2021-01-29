package com.wz.modules.lora.dao;

import com.wz.modules.lora.entity.GymMaster;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: Cherry
 * @Date: 2021/1/29
 * @Desc: GymMasterDao
 */
@Mapper
public interface GymMasterDao {

    List<GymMaster> findAll();
}
